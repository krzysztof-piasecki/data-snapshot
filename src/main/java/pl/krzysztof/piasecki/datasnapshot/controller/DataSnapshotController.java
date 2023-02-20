package pl.krzysztof.piasecki.datasnapshot.controller;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.krzysztof.piasecki.datasnapshot.exceptions.DataSnapshotNotFoundException;
import pl.krzysztof.piasecki.datasnapshot.model.DataSnapshot;
import pl.krzysztof.piasecki.datasnapshot.service.DataSnapshotService;
import pl.krzysztof.piasecki.datasnapshot.utils.Counter;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/data-snapshots")
public class DataSnapshotController {
    private DataSnapshotService dataSnapshotService;

    @Autowired
    public DataSnapshotController(DataSnapshotService dataSnapshotService) {
        this.dataSnapshotService = dataSnapshotService;
    }

    private static final Logger logger = LoggerFactory.getLogger(DataSnapshotController.class);

    private static final int batchSize = 2000;
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsvFile(@RequestParam("file") MultipartFile file) {
        try {
            parseCsv(file.getInputStream());
            return ResponseEntity.ok("Data snapshots uploaded successfully");
        } catch (IOException e) {
            String message = "Error processing CSV file: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(message);
        }
    }

    @GetMapping("/{primaryKey}")
    public DataSnapshot getDataSnapshotByPrimaryKey(@PathVariable("primaryKey") String primaryKey) {
        DataSnapshot dataSnapshot = dataSnapshotService.getDataSnapshotByPrimaryKey(primaryKey);
        if (dataSnapshot == null) {
            throw new DataSnapshotNotFoundException("DataSnapshot with primary key " + primaryKey + " not found");
        }
        return dataSnapshot;
    }

    @DeleteMapping("/{primaryKey}")
    public ResponseEntity deleteDataSnapshotByPrimaryKey(@PathVariable("primaryKey") String primaryKey) {
        DataSnapshot dataSnapshot = dataSnapshotService.getDataSnapshotByPrimaryKey(primaryKey);
        if (dataSnapshot == null) {
            throw new DataSnapshotNotFoundException("DataSnapshot with primary key " + primaryKey + " not found");
        }
        dataSnapshotService.deleteDataSnapshotByPrimaryKey(primaryKey);
        return new ResponseEntity<>(
                "Deleted element with primary key: " + primaryKey,
                HttpStatus.OK);
    }

    private void parseCsv(InputStream inputStream) throws IOException {
        List<DataSnapshot> dataSnapshots = new ArrayList<>();
        Counter counter = new Counter();
        CSVReader csvReader = new CSVReaderBuilder(new InputStreamReader(inputStream))
                .withCSVParser(new CSVParserBuilder()
                        .withSeparator(';')
                        .build()
                )
                .withSkipLines(1) // Skip header row
                .build();
        String[] nextRecord;
        while ((nextRecord = csvReader.readNext()) != null) {
            try {
                String primaryKey = nextRecord[0];
                String name = nextRecord[1];
                String description = nextRecord[2];
                LocalDateTime updatedTimestamp = null;
                if (nextRecord.length > 3 && !nextRecord[3].isEmpty()) {
                    updatedTimestamp = LocalDateTime.parse(nextRecord[3], DateTimeFormatter.ISO_DATE_TIME);
                }
                DataSnapshot snapshot = new DataSnapshot(primaryKey, name, description, updatedTimestamp);
                dataSnapshots.add(snapshot);
                if (dataSnapshots.size() >= batchSize) {
                    dataSnapshotService.saveDataSnapshots(dataSnapshots, counter);
                    dataSnapshots.clear();
                }
            } catch (ArrayIndexOutOfBoundsException | DateTimeParseException ignored) {
            }
        }
        if (!dataSnapshots.isEmpty()) {
            dataSnapshotService.saveDataSnapshots(dataSnapshots, counter);
        }
        logger.info("Total: " + counter.getValidCounter() + " of inserted valid elements");
        logger.info("Total: " + counter.getInvalidCounter() + " of invalid elements");
    }
}

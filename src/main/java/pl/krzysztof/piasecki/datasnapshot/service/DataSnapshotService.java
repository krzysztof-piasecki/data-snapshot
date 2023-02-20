package pl.krzysztof.piasecki.datasnapshot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.krzysztof.piasecki.datasnapshot.exceptions.DataSnapshotNotFoundException;
import pl.krzysztof.piasecki.datasnapshot.model.DataSnapshot;
import pl.krzysztof.piasecki.datasnapshot.repository.DataSnapshotRepository;
import pl.krzysztof.piasecki.datasnapshot.utils.Counter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static pl.krzysztof.piasecki.datasnapshot.utils.Utils.isValid;

@Service
public class DataSnapshotService {

    private DataSnapshotRepository dataSnapshotRepository;

    private static Logger logger = LoggerFactory.getLogger(DataSnapshotService.class);

    @Autowired
    public DataSnapshotService(DataSnapshotRepository dataSnapshotRepository) {
        this.dataSnapshotRepository = dataSnapshotRepository;
    }

    @Transactional
    public void saveDataSnapshots(List<DataSnapshot> dataSnapshots, Counter counter) {
        List<DataSnapshot> validDataSnapshots = new ArrayList<>();
        List<String> invalidRows = new ArrayList<>();
        for (DataSnapshot dataSnapshot : dataSnapshots) {
            if (isValid(dataSnapshot)) {
                validDataSnapshots.add(dataSnapshot);
            } else {
                invalidRows.add(dataSnapshot.toString());
            }
        }
        if (!invalidRows.isEmpty()) {
            logger.info("There have been: " + invalidRows.size() + " of invalid rows in this batch");
            counter.addInvalid(invalidRows.size());
        }
        logger.info("There have been inserted: " + validDataSnapshots.size() + " of valid rows in this batch");
        counter.addValid(validDataSnapshots.size());
        dataSnapshotRepository.saveAll(validDataSnapshots);
    }


    public DataSnapshot getDataSnapshotByPrimaryKey(String primaryKey) {
        return dataSnapshotRepository.findByPrimaryKey(primaryKey).orElse(null);
    }

    @Transactional
    public void deleteDataSnapshotByPrimaryKey(String primaryKey) {
        Optional<DataSnapshot> optionalDataSnapshot = dataSnapshotRepository.findByPrimaryKey(primaryKey);
        if (optionalDataSnapshot.isPresent()) {
            dataSnapshotRepository.delete(optionalDataSnapshot.get());
        } else {
            throw new DataSnapshotNotFoundException("DataSnapshot with PRIMARY_KEY '" + primaryKey + "' not found");
        }
    }

}

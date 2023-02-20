package pl.krzysztof.piasecki.datasnapshot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
public class DataSnapshotApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataSnapshotApplication.class, args);
	}

}

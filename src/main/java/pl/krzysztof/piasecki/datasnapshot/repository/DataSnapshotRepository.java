package pl.krzysztof.piasecki.datasnapshot.repository;

import org.springframework.data.repository.CrudRepository;
import pl.krzysztof.piasecki.datasnapshot.model.DataSnapshot;

import java.util.Optional;

public interface DataSnapshotRepository extends CrudRepository<DataSnapshot, String> {
    Optional<DataSnapshot> findByPrimaryKey(String primaryKey);

    void deleteByPrimaryKey(String primaryKey);
}

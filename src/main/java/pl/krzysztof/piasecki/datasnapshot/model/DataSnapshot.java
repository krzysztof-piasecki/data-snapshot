package pl.krzysztof.piasecki.datasnapshot.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class DataSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String primaryKey;

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private LocalDateTime updatedTimestamp;

    public DataSnapshot() {
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUpdatedTimestamp(LocalDateTime updatedTimestamp) {
        this.updatedTimestamp = updatedTimestamp;
    }

    public Long getId() {
        return id;
    }

    public String getPrimaryKey() {
        return primaryKey;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public DataSnapshot(Long id, String primaryKey, String name, String description, LocalDateTime updatedTimestamp) {
        this.id = id;
        this.primaryKey = primaryKey;
        this.name = name;
        this.description = description;
        this.updatedTimestamp = updatedTimestamp;
    }

    public DataSnapshot(String primaryKey, String name, String description, LocalDateTime updatedTimestamp) {
        this.primaryKey = primaryKey;
        this.name = name;
        this.description = description;
        this.updatedTimestamp = updatedTimestamp;
    }
}

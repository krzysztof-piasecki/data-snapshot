package pl.krzysztof.piasecki.datasnapshot.exceptions;

public class DataSnapshotNotFoundException extends RuntimeException {

    public DataSnapshotNotFoundException(String message) {
        super(message);
    }
}
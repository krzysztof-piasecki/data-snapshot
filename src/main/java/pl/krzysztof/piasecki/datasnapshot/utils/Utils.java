package pl.krzysztof.piasecki.datasnapshot.utils;

import pl.krzysztof.piasecki.datasnapshot.model.DataSnapshot;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Utils {
    public static boolean isValid(DataSnapshot dataSnapshot) {
        if (dataSnapshot.getPrimaryKey() == null || dataSnapshot.getPrimaryKey().isBlank()) {
            return false;
        }
        if (dataSnapshot.getUpdatedTimestamp() != null) {
            try {
                LocalDateTime.parse(dataSnapshot.getUpdatedTimestamp().toString());
            } catch (DateTimeParseException e) {
                return false;
            }
        }
        return true;
    }
}

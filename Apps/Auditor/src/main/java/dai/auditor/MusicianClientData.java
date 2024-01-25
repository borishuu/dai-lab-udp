package dai.auditor;

import java.time.LocalDateTime;

public class MusicianClientData {
    String uuid;
    String instrument;
    LocalDateTime lastActivity;

    public MusicianClientData(String uuid, String instrument, LocalDateTime lastActivity) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.lastActivity = lastActivity;
    }
}

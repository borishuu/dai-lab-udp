package dai.auditor;

import java.time.LocalDateTime;
import java.util.UUID;

public class MusicianData {
    private String uuid;
    private String instrument;
    private String lastActivity;

    public MusicianData(String uuid, String instrument, String lastActivity) {
        this.uuid = uuid;
        this.instrument = instrument;
        this.lastActivity = lastActivity;
    }
}

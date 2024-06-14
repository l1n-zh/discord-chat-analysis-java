package crawler;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.time.Instant;


class Message {
    String authorID;
    int charCount;
}

class Channel {
    int channelID;
    Instant lastUpdate;
    @JsonUnwrapped
    Message[] messages;
}

public class Record {
    @JsonUnwrapped
    Channel[] channels;
}
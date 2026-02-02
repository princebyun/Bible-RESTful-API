package bible.bible.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CleanBotResult {

    @JsonProperty("isSafe")
    private boolean isSafe;

    @JsonProperty("reason")
    private String reason;

    public CleanBotResult() {
    }

    public boolean isSafe() {
        return isSafe;
    }

    public String getReason() {
        return reason;
    }
}
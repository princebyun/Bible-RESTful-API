package bible.bible.domain;

public class CleanBotResponse {

    private boolean isSafe;
    private String reason;

    public boolean isSafe() {
        return isSafe;
    }

    public void setSafe(boolean safe) {
        isSafe = safe;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

}

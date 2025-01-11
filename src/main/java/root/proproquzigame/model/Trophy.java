package root.proproquzigame.model;

public class Trophy {
    private int trophyId;
    private String trophyImagePath;
    private boolean isNotified;

    public int getTrophyId() {
        return trophyId;
    }

    public String getTrophyImagePath() {
        return trophyImagePath;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setTrophyId(int trophyId) {
        this.trophyId = trophyId;
    }

    public void setTrophyImagePath(String trophyImagePath) {
        this.trophyImagePath = trophyImagePath;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    public Trophy(int trophyId, String trophyImagePath, boolean isNotified) {
        this.trophyId = trophyId;
        this.trophyImagePath = trophyImagePath;
        this.isNotified = isNotified;
    }
}

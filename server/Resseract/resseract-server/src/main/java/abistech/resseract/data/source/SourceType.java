package abistech.resseract.data.source;

public enum SourceType {
    CSV(false),
    INTERNAL(false);

    private final boolean isLive;

    SourceType(boolean isLive) {
        this.isLive = isLive;
    }

    public boolean isLive() {
        return isLive;
    }
}

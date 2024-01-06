package abistech.resseract.data.frame;

public enum DataPeriod {
    SECONDS(60),
    MINUTES(60),
    HOURLY(24),
    DAILY(365),
    WEEKLY(52),
    MONTHLY(12),
    YEARLY(1);

    private final int value;

    DataPeriod(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

package abistech.resseract.data.source;

import abistech.resseract.data.source.impl.CSVUploadDataSource;
import abistech.resseract.data.source.impl.LiveCSVDataSource;

public class SourceFactory {

    public static Source getSource(SourceType sourceType) {
        return switch (sourceType) {
            case CSV -> new CSVUploadDataSource();
            case LIVE_CSV -> new LiveCSVDataSource();
            default -> null;
        };
    }
}

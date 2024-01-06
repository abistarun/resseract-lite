package abistech.resseract.data.source;

import abistech.resseract.data.source.impl.CSVDataSource;
import abistech.resseract.data.source.impl.PostGresDBSource;

public class SourceFactory {

    public static Source getSource(SourceType sourceType) {
        return switch (sourceType) {
            case CSV -> new CSVDataSource();
            default -> null;
        };
    }
}

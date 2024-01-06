package abistech.resseract.data;

import abistech.resseract.config.Config;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.source.SourceType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@EqualsAndHashCode
public class DataInfo {

    private final DataKey dataKey;
    private final SourceType sourceType;
    private final Map<String, Map<String, Object>> columnProperties;
    private final Config config;

    public DataInfo(DataKey dataKey, SourceType sourceType, Map<String, Map<String, Object>> columnProperties, Config config) {
        this.dataKey = dataKey;
        this.sourceType = sourceType;
        this.columnProperties = columnProperties;
        this.config = config;
    }
}

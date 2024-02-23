package abistech.resseract.data.source;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

public enum SourceType {
    @JsonProperty("CSV Upload") CSV("CSV Upload", false),
    @JsonProperty("CSV Remote") LIVE_CSV("CSV Remote", true),
    INTERNAL("Internal", false);

    private final String friendlyName;
    private final boolean isLive;

    final static Map<String, SourceType> nameVsSourceType = new HashMap<>(SourceType.values().length);
    static {
        for (SourceType sourceType : SourceType.values()) {
            nameVsSourceType.put(sourceType.friendlyName, sourceType);
        }
    }

    SourceType(String friendlyName, boolean isLive) {
        this.friendlyName = friendlyName;
        this.isLive = isLive;
    }

    public boolean isLive() {
        return isLive;
    }

    public static SourceType getSourceType(String source) {
        return nameVsSourceType.get(source);
    }
}

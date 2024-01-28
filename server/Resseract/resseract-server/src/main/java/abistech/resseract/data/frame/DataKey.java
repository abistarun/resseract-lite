package abistech.resseract.data.frame;

import lombok.*;

@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class DataKey {
    private String key;

    public DataKey(String key) {
        this.key = key.trim();
    }

    public void setKey(String key) {
        this.key = key.trim();
    }

    public String getKey() {
        return key.trim();
    }
}

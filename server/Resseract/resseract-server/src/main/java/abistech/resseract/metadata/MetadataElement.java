package abistech.resseract.metadata;

import abistech.resseract.metadata.impl.ChartElement;
import abistech.resseract.metadata.impl.TabularElement;
import abistech.resseract.metadata.impl.ValueElement;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "jackson_type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = ValueElement.class, name = "VALUE"),
        @JsonSubTypes.Type(value = ChartElement.class, name = "CHART"),
        @JsonSubTypes.Type(value = TabularElement.class, name = "TABLE")
})
@Getter
@Setter
@NoArgsConstructor
public abstract class MetadataElement {

    private String key;
    private String description;

    protected MetadataElement(String key) {
        this.key = key;
    }

    public abstract ElementType getType();
}

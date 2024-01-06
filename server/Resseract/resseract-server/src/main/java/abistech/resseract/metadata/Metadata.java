package abistech.resseract.metadata;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
public class Metadata {

    private List<MetadataElement> elements;

    public Metadata() {
        this.elements = new ArrayList<>();
    }

    public void add(MetadataElement element) {
        elements.add(element);
    }

    public void setElements(List<MetadataElement> elements) {
        this.elements = elements;
    }
}

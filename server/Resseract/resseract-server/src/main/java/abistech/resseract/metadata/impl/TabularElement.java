package abistech.resseract.metadata.impl;

import abistech.resseract.metadata.ElementType;
import abistech.resseract.metadata.MetadataElement;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TabularElement extends MetadataElement {

    private String[] headers;
    private List<Object[]> data;

    @Override
    public ElementType getType() {
        return ElementType.TABLE;
    }
}

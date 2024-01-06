package abistech.resseract.metadata.impl;

import abistech.resseract.metadata.ElementType;
import abistech.resseract.metadata.MetadataElement;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValueElement extends MetadataElement {

    private double value;
    private double rangeStart;
    private double rangeEnd;
    private boolean isHigherBetter;

    @Override
    public ElementType getType() {
        return ElementType.VALUE;
    }
}

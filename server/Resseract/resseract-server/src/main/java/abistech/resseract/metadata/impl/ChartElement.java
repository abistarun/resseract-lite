package abistech.resseract.metadata.impl;

import abistech.resseract.metadata.ElementType;
import abistech.resseract.metadata.MetadataElement;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChartElement extends MetadataElement {

    private ChartType chartType;
    private String[] xAxisData;
    private String xAxisLabel;
    private double[] yAxisData;
    private String yAxisLabel;

    @Override
    public ElementType getType() {
        return ElementType.CHART;
    }
}

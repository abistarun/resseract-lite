package abistech.resseract.analysis;

import abistech.resseract.config.Config;
import abistech.resseract.data.frame.DataKey;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisSpecification {

    private String analysisName;
    private AnalysisType analysisType;
    private DataKey dataKey;
    private Config configurations;
}

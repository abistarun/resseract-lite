package abistech.resseract.step.impl.processing;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.step.elements.Dataset;
import abistech.resseract.step.elements.DatasetImpl;
import abistech.resseract.step.impl.AbstractStep;

import java.util.Collections;
import java.util.List;

public class TopN extends AbstractStep {
    @Override
    public Dataset execute(Dataset input, Config config) throws ResseractException {
        Double n = (Double) config.get(ConfigKey.TOP_N);
        if (n == -1d) {
            return input;
        }
        return new DatasetImpl(input.getData().subset(0, n.intValue()));
    }

    @Override
    public List<ConfigKey> getRequiredConfigs() {
        return Collections.singletonList(ConfigKey.TOP_N);
    }
}

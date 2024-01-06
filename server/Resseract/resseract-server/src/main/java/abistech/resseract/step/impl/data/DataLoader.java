package abistech.resseract.step.impl.data;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.DataService;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.step.elements.Dataset;
import abistech.resseract.step.elements.DatasetImpl;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.step.impl.AbstractStep;

import java.util.Collections;
import java.util.List;

public class DataLoader extends AbstractStep {
    @Override
    public Dataset execute(Dataset input, Config config) throws ResseractException {
        DataKey dataKey = new DataKey((String) config.get(ConfigKey.DATA_KEY));
        Data data = DataService.getData(dataKey);
        return new DatasetImpl(data);
    }

    @Override
    public List<ConfigKey> getRequiredConfigs() {
        return Collections.emptyList();
    }
}

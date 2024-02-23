package abistech.resseract.data.source.impl;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.ResseractException;

import java.util.LinkedList;
import java.util.List;

public class CSVUploadDataSource extends AbstractCSVDataSource {

    @Override
    public List<ConfigKey> getSourceConfigs() {
        LinkedList<ConfigKey> configKeys = new LinkedList<>();
        configKeys.add(ConfigKey.CSV_FILE);
        return configKeys;
    }

    @Override
    public List<ConfigKey> getDataConfigs(Config sourceConfigs) throws ResseractException {
        String filePath = (String) sourceConfigs.get(ConfigKey.CSV_FILE);
        return getDataConfigs(filePath);
    }
    @Override
    public Data fetch(Config config) throws ResseractException {
        String filePath = (String) config.get(ConfigKey.CSV_FILE);
        return fetch(config, filePath, false);
    }
}

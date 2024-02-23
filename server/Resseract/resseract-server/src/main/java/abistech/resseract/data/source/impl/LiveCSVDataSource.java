package abistech.resseract.data.source.impl;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.ResseractException;

import java.util.LinkedList;
import java.util.List;

public class LiveCSVDataSource extends AbstractCSVDataSource {

    @Override
    public List<ConfigKey> getSourceConfigs() {
        LinkedList<ConfigKey> configKeys = new LinkedList<>();
        configKeys.add(ConfigKey.FILE_PATH);
        return configKeys;
    }

    @Override
    public List<ConfigKey> getDataConfigs(Config sourceConfigs) throws ResseractException {
        String filePath = (String) sourceConfigs.get(ConfigKey.FILE_PATH);
        return getDataConfigs(filePath);
    }
    @Override
    public Data fetch(Config config) throws ResseractException {
        String filePath = (String) config.get(ConfigKey.FILE_PATH);
        return fetch(config, filePath, false);
    }

    public Data fetchSchematicData(Config config) throws ResseractException {
        String filePath = (String) config.get(ConfigKey.FILE_PATH);
        return fetch(config, filePath, true);
    }
}

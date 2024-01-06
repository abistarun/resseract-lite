package abistech.resseract.data.source;

import abistech.resseract.auth.Feature;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.ResseractException;

import java.util.List;

public interface Source {

    List<ConfigKey> getSourceConfigs();

    List<ConfigKey> getDataConfigs(Config sourceConfigs) throws ResseractException;

    Data fetch(Config config) throws ResseractException;

    void close();

    int getDataCount(Config configurations) throws ResseractException;

    Data fetchSchematicData(Config configurations) throws ResseractException;

    Feature getAuthorizedFeature();
}

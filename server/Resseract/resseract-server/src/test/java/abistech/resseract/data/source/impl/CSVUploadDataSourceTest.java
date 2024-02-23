package abistech.resseract.data.source.impl;

import abistech.resseract.auth.Feature;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

public class CSVUploadDataSourceTest {

    private static CSVUploadDataSource source;

    @BeforeClass
    public static void initialize() {
        source = new CSVUploadDataSource();
    }

    @AfterClass
    public static void stop() {
    }

    @Test
    public void testGetSourceConfigs() {
        List<ConfigKey> actualSourceConfigs = source.getSourceConfigs();

        Assert.assertEquals(Collections.singletonList(ConfigKey.CSV_FILE), actualSourceConfigs);
    }

    @Test
    public void testGetDataConfigs() throws ResseractException {
        final String path = TestUtil.getResource("/DataDirectory/Data/AirPassengers/Data.csv");
        Config sourceConfig = new Config();
        sourceConfig.put(ConfigKey.CSV_FILE, path);

        List<ConfigKey> actualDataConfigs = source.getDataConfigs(sourceConfig);

        Assert.assertEquals(4, actualDataConfigs.size());
    }

    @Test
    public void testFetch() throws ResseractException {
        final String path = TestUtil.getResource("/DataDirectory/Data/AirPassengers/Data.csv");
        Config config = new Config();
        config.put(ConfigKey.CSV_FILE, path);
        List<ConfigKey> dataConfigs = source.getDataConfigs(config);
        for (ConfigKey dataConfig : dataConfigs) {
            config.put(dataConfig, dataConfig.getDefaultValue().toString());
        }

        Data actualData = source.fetch(config);

        Assert.assertEquals(144, actualData.noOfRows());
        Assert.assertEquals(2, actualData.noOfCols());
    }

    @Test
    public void testFetchSchematicData() throws ResseractException {
        Assert.assertNull(source.fetchSchematicData(null));
    }

    @Test
    public void testGetDataCount() {
        Assert.assertEquals(1, source.getDataCount(null));
    }

    @Test
    public void testGetAuthorizedFeature() {
        Assert.assertEquals(Feature.BASIC_DATA_SOURCES, source.getAuthorizedFeature());
    }
}
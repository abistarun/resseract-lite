package abistech.resseract.data;

import abistech.resseract.CoreServer;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class UploadTest {

    @BeforeClass
    public static void initialize() throws ResseractException {
        TestUtil.initialize();
    }

    @AfterClass
    public static void stop() {
        TestUtil.closeServer();
    }

    @Test
    public void testCSVDataUploadForTimeSeries() throws ResseractException {
        DataKey datakey = new DataKey("TestData1");
        try {
            Config config = new Config();
            config.put(ConfigKey.CSV_FILE, TestUtil.getResource("/DataDirectory/Data/AirPassengers/Data.csv"));
            List<ConfigKey> dataConfigurations = CoreServer.getDataConfigurations(SourceType.CSV, config);
            for (ConfigKey dataConfiguration : dataConfigurations) {
                config.put(dataConfiguration, dataConfiguration.getDefaultValue().toString());
            }
            config.put(ConfigKey.DATA_KEY, datakey.getKey());
            CoreServer.uploadData(SourceType.CSV, config, false);

            Data data = DataService.getData(datakey);

            Assert.assertEquals(144, data.noOfRows());
            Assert.assertEquals(2, data.noOfCols());
        } finally {
            DataService.deleteData(datakey);
        }
    }

    @Test
    public void testDataUploadAsync() throws ResseractException, InterruptedException {
        DataKey datakey = new DataKey("TestData2");
        try {
            Config config = new Config();
            config.put(ConfigKey.CSV_FILE, TestUtil.getResource("/DataDirectory/Data/AirPassengers/Data.csv"));
            List<ConfigKey> dataConfigurations = CoreServer.getDataConfigurations(SourceType.CSV, config);
            for (ConfigKey dataConfiguration : dataConfigurations) {
                config.put(dataConfiguration, dataConfiguration.getDefaultValue().toString());
            }
            config.put(ConfigKey.DATA_KEY, datakey.getKey());
            CoreServer.uploadData(SourceType.CSV, config, true);

            while (CoreServer.getDataUploadProgress(datakey) < 100) {
                //noinspection BusyWait
                Thread.sleep(100);
            }
            Data data = DataService.getData(datakey);

            Assert.assertEquals(144, data.noOfRows());
            Assert.assertEquals(2, data.noOfCols());
        } finally {
            DataService.deleteData(datakey);
        }
    }

    @Test
    public void testCSVDataUploadForAllTypes() throws ResseractException {
        DataKey datakey = new DataKey("TestData3");
        try {
            Config config = new Config();
            config.put(ConfigKey.CSV_FILE, TestUtil.getResource("/DataDirectory/Data/GenericSampleData/Data.csv"));
            List<ConfigKey> dataConfigurations = CoreServer.getDataConfigurations(SourceType.CSV, config);
            for (ConfigKey dataConfiguration : dataConfigurations) {
                config.put(dataConfiguration, dataConfiguration.getDefaultValue().toString());
            }
            config.put(ConfigKey.DATA_KEY, datakey.getKey());
            CoreServer.uploadData(SourceType.CSV, config, false);

            Data data = DataService.getData(datakey);

            Assert.assertEquals(9, data.noOfRows());
            Assert.assertEquals(13, data.noOfCols());
            Assert.assertEquals(DataType.NUMERICAL, data.getColumn("IN").getDataType());
            Assert.assertEquals(DataType.CATEGORICAL, data.getColumn("C2").getDataType());
            Assert.assertEquals(DataType.BOOLEAN, data.getColumn("B1").getDataType());
            Assert.assertEquals(DataType.DATE, data.getColumn("D1").getDataType());
            Assert.assertEquals(DataType.NUMERICAL, data.getColumn("N1").getDataType());
            Assert.assertEquals(DataType.CATEGORICAL, data.getColumn("C1").getDataType());
        } finally {
            DataService.deleteData(datakey);
        }
    }
}

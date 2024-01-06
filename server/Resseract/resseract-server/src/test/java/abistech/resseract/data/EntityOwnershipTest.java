package abistech.resseract.data;

import abistech.resseract.CoreServer;
import abistech.resseract.auth.AuthenticationService;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.dao.DAOFactory;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.TestUtil;
import org.junit.*;

import java.util.List;

public class EntityOwnershipTest {

    @BeforeClass
    public static void initialize() throws ResseractException {
        TestUtil.initialize();
    }

    @AfterClass
    public static void stop() {
        TestUtil.closeServer();
    }

    @Before
    public void authenticate() throws ResseractException {
        AuthenticationService.authenticate("dummy");
    }

    @Test(expected = ResseractException.class)
    public void testReadDataForUser() throws ResseractException {
        DataKey dataKey = new DataKey("AirPassengers");
        Data data = DataService.getData(dataKey);
        Assert.assertNotNull(data);
        AuthenticationService.authenticate("dummy2");
        DataService.getData(dataKey);
    }

    @Test
    public void testGetAllDataInfoForUser() throws ResseractException {
        List<DataInfo> dataInfo = DataService.getAllDataInfo();
        Assert.assertEquals(4, dataInfo.size());
        AuthenticationService.authenticate("dummy2");
        dataInfo = DataService.getAllDataInfo();
        Assert.assertEquals(0, dataInfo.size());
    }

    @Test(expected = ResseractException.class)
    public void testAddFormulaColumnsForUser() throws ResseractException {
        AuthenticationService.authenticate("dummy2");
        DataKey dataKey = new DataKey("GenericSampleData");
        String columnName = "Sum_N1_N2";
        Column<?> column = new DoubleColumn(columnName);
        DAOFactory.getDefaultDAO().addColumn(dataKey, column);
    }

    @Test
    public void testGetDashboardsForUser() throws ResseractException {
        List<String> dashboards = DataService.getAllDashboards();
        Assert.assertEquals(3, dashboards.size());
        AuthenticationService.authenticate("dummy2");
        dashboards = DataService.getAllDashboards();
        Assert.assertEquals(0, dashboards.size());
    }

    @Test(expected = ResseractException.class)
    public void testGetDashboardDataForUser() throws ResseractException {
        String data = DataService.getDashboardData("T1");
        Assert.assertEquals(data, "D1");
        AuthenticationService.authenticate("dummy2");
        DataService.getDashboardData("T1");
    }

    @Test
    public void testUploadSameNameData() throws ResseractException {
        DataKey datakey = new DataKey("TestData");
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

            Assert.assertEquals(data.noOfRows(), 144);
            Assert.assertEquals(data.noOfCols(), 2);

            AuthenticationService.authenticate("dummy2");

            config = new Config();
            config.put(ConfigKey.CSV_FILE, TestUtil.getResource("/DataDirectory/Data/GenericSampleData/Data.csv"));
            dataConfigurations = CoreServer.getDataConfigurations(SourceType.CSV, config);
            for (ConfigKey dataConfiguration : dataConfigurations) {
                config.put(dataConfiguration, dataConfiguration.getDefaultValue().toString());
            }
            config.put(ConfigKey.DATA_KEY, datakey.getKey());
            CoreServer.uploadData(SourceType.CSV, config, false);

            data = DataService.getData(datakey);

            Assert.assertEquals(9, data.noOfRows());
            Assert.assertEquals(13, data.noOfCols());
        } finally {
            AuthenticationService.authenticate("dummy");
            DataService.deleteData(datakey);
            AuthenticationService.authenticate("dummy2");
            DataService.deleteData(datakey);
        }
    }

    @Test
    public void testUploadSameNameDashboard() throws ResseractException {
        String dashboardName = "TestDashboard";
        try {
            DataService.saveDashboard(dashboardName, "D1");
            String data = DataService.getDashboardData(dashboardName);
            Assert.assertEquals("D1", data);

            AuthenticationService.authenticate("dummy2");

            DataService.saveDashboard(dashboardName, "D2");
            data = DataService.getDashboardData(dashboardName);
            Assert.assertEquals("D2", data);
        } finally {
            AuthenticationService.authenticate("dummy");
            DataService.deleteDashboard(dashboardName);
            AuthenticationService.authenticate("dummy2");
            DataService.deleteDashboard(dashboardName);
        }
    }
}

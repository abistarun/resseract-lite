package abistech.resseract.data;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.dao.impl.HibernateUtil;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;
import abistech.resseract.util.TestUtil;
import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DataServiceTest {

    @BeforeClass
    public static void initialize() throws ResseractException {
        TestUtil.initialize();
    }

    @AfterClass
    public static void stop() {
        TestUtil.closeServer();
    }

    @Test
    public void testAddFormulaColumns() throws ResseractException {
        DataKey dataKey = new DataKey("GenericSampleData");
        String columnName = "Sum_N1_N2";
        try {
            DataService.addCustomColumn(dataKey, columnName, "[N1]+[N2]");
            DoubleColumn result = (DoubleColumn) DataService.getData(dataKey).getColumn(columnName);
            DoubleColumn expectedColumn = new DoubleColumn(columnName, new double[]{2018, 2019, 2020, 2010, 2011, 2012, 2013, 2014, 2015});
            Assert.assertEquals(expectedColumn, result);
        } finally {
            DataService.deleteColumn(dataKey, columnName);
        }
    }

    @Test
    public void testSaveAndGetDashboard() throws ResseractException {
        String dashboardName = "TestDashboard";
        String dashboardData = "Data";
        try {
            DataService.saveDashboard(dashboardName, dashboardData);
            String data = DataService.getDashboardData(dashboardName);
            Assert.assertEquals(dashboardData, data);
        } finally {
            DataService.deleteDashboard(dashboardName);
        }
    }

    @Test
    public void testReSaveAndGetDashboard() throws ResseractException {
        String dashboardName = "TestDashboard";
        String dashboardData = "Data";
        try {
            DataService.saveDashboard(dashboardName, dashboardData);
            DataService.saveDashboard(dashboardName, dashboardData);
            String data = DataService.getDashboardData(dashboardName);
            Assert.assertEquals(dashboardData, data);
        } finally {
            DataService.deleteDashboard(dashboardName);
        }
    }

    @Test
    public void testGetAllDashboards() {
        List<String> allDashboards = DataService.getAllDashboards();
        Assert.assertEquals(allDashboards.size(), 3);
    }

    @Test
    public void testGetAllDataInfo() throws ResseractException {
        List<DataInfo> dataInfo = DataService.getAllDataInfo();
        Assert.assertEquals(4, dataInfo.size());
    }

    @Test
    public void testGetDataInfos() throws ResseractException {
        List<DataInfo> dataInfo = DataService.getAllDataInfo();
        DataInfo airPassengerInfo = null;
        for (DataInfo info : dataInfo) {
            if (info.getDataKey().getKey().equals("AirPassengers")) {
                airPassengerInfo = info;
                break;
            }
        }

        assert airPassengerInfo != null;
        Assert.assertEquals("AirPassengers", airPassengerInfo.getDataKey().getKey());
        Assert.assertEquals(2, airPassengerInfo.getColumnProperties().size());

        Assert.assertEquals(SourceType.CSV, airPassengerInfo.getSourceType());
    }

    @Test
    public void testGetDataInfo() throws ResseractException {
        String directory = TestUtil.getResource("/DataDirectory/Data");
        DataKey dataKey = new DataKey("AirPassengers");
        DataInfo dataInfo = DataService.getDataInfo(dataKey);
        Map<String, Map<String, Object>> columnProperties = new HashMap<>();
        columnProperties.put("Month", new HashMap<String, Object>() {{
            put(Constants.DATA_TYPE, DataType.DATE.name());
        }});
        columnProperties.put("#Passengers", new HashMap<String, Object>() {{
            put(Constants.DATA_TYPE, DataType.NUMERICAL.name());
        }});

        Map<String, Object> config = new HashMap<>();
        config.put("#Passengers Data Type", DataType.NUMERICAL.name());
        config.put("Month Data Type", DataType.DATE.name());
        config.put("CSV_FILE", directory + "/AirPassengers/Data.csv");
        config.put("DATA_KEY", "AirPassengers");
        config.put("Month Format", "yyyy-MM");

        Config expectedConfig = new Config();
        expectedConfig.setProperties(config);
        DataInfo expectedDataInfo = new DataInfo(dataKey, SourceType.CSV, columnProperties, expectedConfig);
        Assert.assertEquals(expectedDataInfo, dataInfo);
    }

    @Test
    public void testDeleteData() throws ResseractException {
        DataKey dataKey = new DataKey("testSaveAnalysisResultSuite1DK");
        Config config = new Config();
        config.put(ConfigKey.CSV_FILE, TestUtil.getResource("/DataDirectory/Data/AirPassengers/Data.csv"));
        List<ConfigKey> dataConfigurations = DataService.getDataConfigurations(SourceType.CSV, config);
        for (ConfigKey dataConfiguration : dataConfigurations) {
            config.put(dataConfiguration, dataConfiguration.getDefaultValue().toString());
        }
        config.put(ConfigKey.DATA_KEY, dataKey.getKey());
        DataService.uploadData(SourceType.CSV, config, false);
        Long dataId;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            dataId = session.createQuery("select id from DBData where dataKey = :key", Long.class)
                    .setParameter("key", dataKey.getKey()).uniqueResult();
        }
        DataService.deleteData(dataKey);
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            List<Long> dataIds = session.createQuery("select id from DBData where dataKey = :key", Long.class)
                    .setParameter("key", dataKey.getKey()).getResultList();
            Assert.assertEquals(0, dataIds.size());

            List<Long> accessDataIds = session.createQuery("select id from DBDataAccess where dataId = :id", Long.class)
                    .setParameter("id", dataId).getResultList();
            Assert.assertEquals(0, accessDataIds.size());
        }
    }

    @Test
    public void testDeleteDashboard() {
        DataService.saveDashboard("Dummy1", "Dummy1");
        Long id;
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            id = session.createQuery("select id from DBDashboard where name = :name", Long.class)
                    .setParameter("name", "Dummy1").uniqueResult();
        }
        DataService.deleteDashboard("Dummy1");
        try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
            session.beginTransaction();
            List<Long> ids = session.createQuery("select id from DBDashboard where name = :name", Long.class)
                    .setParameter("name", "Dummy1").getResultList();
            Assert.assertEquals(0, ids.size());

            List<Long> accessIds = session.createQuery("select id from DBDashboardAccess where dashboardId = :id", Long.class)
                    .setParameter("id", id).getResultList();
            Assert.assertEquals(0, accessIds.size());
        }
    }
}

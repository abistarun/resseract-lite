package abistech.resseract;

import abistech.resseract.auth.AuthenticationService;
import abistech.resseract.auth.Feature;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class CommonAPITest {

    @BeforeClass
    public static void initialize() throws ResseractException {
        TestUtil.initialize();
    }

    @AfterClass
    public static void stop() {
        TestUtil.closeServer();
    }

    @Test
    public void testGetAllSources() {
        Map<SourceType, List<ConfigKey>> allSources = CoreServer.getAllSources();
        Map<SourceType, List<ConfigKey>> assertSources = new HashMap<>(2);

        LinkedList<ConfigKey> csvConfigs = new LinkedList<>();
        csvConfigs.add(ConfigKey.CSV_FILE);
        assertSources.put(SourceType.CSV, csvConfigs);

        Assert.assertEquals(assertSources, allSources);
    }

    @Test
    public void testGetDataConfiguration() throws ResseractException {
        for (SourceType sourceType : SourceType.values()) {
            if (sourceType == SourceType.INTERNAL)
                continue;
            Config config = createSourceConfig(sourceType);
            List<ConfigKey> dataConfigurations = CoreServer.getDataConfigurations(sourceType, config);
            Assert.assertEquals(getExpectedConfigSize(sourceType), dataConfigurations.size());
        }
    }

    @Test
    public void testGetAccessibleFeatures() {
        Collection<Feature> accessibleFeatures = AuthenticationService.getAccessibleFeatures();
        List<Feature> expectedFeatures = new ArrayList<>(Arrays.asList(Feature.values()));
        Assert.assertEquals(expectedFeatures, accessibleFeatures);
    }

    private int getExpectedConfigSize(SourceType sourceType) {
        switch (sourceType) {
            case CSV:
                return 4;
        }
        return 0;
    }

    private Config createSourceConfig(SourceType sourceType) throws ResseractException {
        Config config = new Config();
        switch (sourceType) {
            case CSV:
                config.put(ConfigKey.CSV_FILE, TestUtil.getResource("/DataDirectory/Data/AirPassengers/Data.csv"));
                break;
        }
        return config;
    }
}

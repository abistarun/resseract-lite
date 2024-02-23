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

        LinkedList<ConfigKey> liveCsvConfigs = new LinkedList<>();
        liveCsvConfigs.add(ConfigKey.FILE_PATH);
        assertSources.put(SourceType.LIVE_CSV, liveCsvConfigs);

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
        return switch (sourceType) {
            case CSV, LIVE_CSV -> 4;
            default -> 0;
        };
    }

    private Config createSourceConfig(SourceType sourceType) throws ResseractException {
        Config config = new Config();
        final String path = TestUtil.getResource("/DataDirectory/Data/AirPassengers/Data.csv");
        switch (sourceType) {
            case CSV -> config.put(ConfigKey.CSV_FILE, path);
            case LIVE_CSV -> config.put(ConfigKey.FILE_PATH, path);
        }
        return config;
    }
}

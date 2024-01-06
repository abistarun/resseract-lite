package abistech.resseract.analysis;

import abistech.resseract.CoreServer;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.TestUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupByAnalysisTest extends AbstractAnalysisTest {

    @BeforeClass
    public static void initialize() throws ResseractException {
        TestUtil.initialize();
    }

    @AfterClass
    public static void stop() {
        TestUtil.closeServer();
    }

    @Test
    public void testDefault() throws ResseractException {
        DataKey dataKey = new DataKey("GenericSampleData");
        Config config = new Config();
        config.put(ConfigKey.GROUPBY_COLUMN_NAME, "C1");
        config.put(ConfigKey.TARGET_COLUMNS, Arrays.asList("N3", "N1"));
        config.put(ConfigKey.SLICE_EXPRESSION, "[N2] == 2009");
        List<AnalysisSpecification> specs = Collections.singletonList(new AnalysisSpecification("Test", AnalysisType.GROUP_BY, dataKey, config));
        List<AnalysisResult> results = CoreServer.runAnalysis(specs);
        Assert.assertEquals(results.get(0).getColumns().size(), 2);
        Assert.assertEquals(Arrays.asList("Hey", "Hi", "Bye"), results.get(0).getIndex());
        Assert.assertEquals(Arrays.asList(6d, 9d, 6d), results.get(0).getColumns().get("N1").getData());
        Assert.assertEquals(Arrays.asList(11d, 22d, 43d), results.get(0).getColumns().get("N3").getData());
    }

    @Test
    public void testGroupByWithExpression() throws ResseractException {
        DataKey dataKey = new DataKey("GenericSampleData");
        Config config = new Config();
        config.put(ConfigKey.EXPRESSION, "[N3]+[N1]");
        config.put(ConfigKey.SLICE_EXPRESSION, "[N2] == 2009");
        List<AnalysisSpecification> specs = Collections.singletonList(new AnalysisSpecification("Test", AnalysisType.GROUP_BY, dataKey, config));
        List<AnalysisResult> results = CoreServer.runAnalysis(specs);
        Assert.assertEquals(results.get(0).getColumns().size(), 1);
        Assert.assertEquals(Collections.singletonList(""), results.get(0).getIndex());
        Assert.assertEquals(Collections.singletonList(97d), results.get(0).getColumns().get("Expression Result").getData());
    }
}

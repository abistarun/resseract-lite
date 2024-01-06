package abistech.resseract.analysis;

import abistech.resseract.CoreServer;
import abistech.resseract.config.Config;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.JSONHandler;
import abistech.resseract.util.TestUtil;
import org.junit.Assert;

import java.util.*;

abstract class AbstractAnalysisTest {

    void testAnalysis(String analysisAssertFileName, AnalysisType analysisType, DataKey dataKey, Config configurations) throws ResseractException {
        testAnalysis(analysisAssertFileName, analysisType, dataKey, configurations, false);
    }

    void testAnalysis(String analysisAssertFileName, AnalysisType analysisType, DataKey dataKey, Config configurations, boolean hardDataCompare) throws ResseractException {
        AnalysisSpecification specification = new AnalysisSpecification();
        specification.setAnalysisType(analysisType);
        specification.setConfigurations(configurations);
        specification.setDataKey(dataKey);
        List<AnalysisSpecification> analysisSpecification = Collections.singletonList(specification);
        List<AnalysisResult> analysisResults = CoreServer.runAnalysis(analysisSpecification);

        String expectedJSON = TestUtil.readJSONFromAnalysisAssert(analysisAssertFileName);
        String actualJSON = JSONHandler.serialize(analysisResults);

        AnalysisResult expectedResult = Objects.requireNonNull(JSONHandler.deserialize(expectedJSON, AnalysisResult[].class))[0];
        AnalysisResult actaulResult = Objects.requireNonNull(JSONHandler.deserialize(actualJSON, AnalysisResult[].class))[0];

        Assert.assertEquals(expectedResult.getIndex(), actaulResult.getIndex());
        Assert.assertEquals(expectedResult.getMetadata(), actaulResult.getMetadata());
        Map<String, AnalysisElement> columns = expectedResult.getColumns();
        for (String columnName : columns.keySet()) {
            AnalysisElement expected = expectedResult.getColumns().get(columnName);
            AnalysisElement actual = actaulResult.getColumns().get(columnName);

            if (hardDataCompare)
                Assert.assertEquals(expected.getData(), actual.getData());
            else
                Assert.assertEquals(expected.getData().size(), actual.getData().size());
        }
    }

}

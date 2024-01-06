package abistech.resseract.util;

import abistech.resseract.CoreServer;
import abistech.resseract.analysis.AnalysisResult;
import abistech.resseract.analysis.AnalysisSpecification;
import abistech.resseract.analysis.AnalysisType;
import abistech.resseract.config.Config;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

class AnalysisAssertGenerator {

    public static void main(String[] args) throws ResseractException {
        TestUtil.initialize();
        AnalysisType[] types = new AnalysisType[]{
                AnalysisType.GROUP_BY
        };
        String[] names = new String[]{
                "GroupBy"
        };
        try {
            for (int i = 0; i < types.length; i++) {
                AnalysisType type = types[i];
                String name = names[i];
                AnalysisSpecification specification = new AnalysisSpecification();
                specification.setAnalysisType(type);
                specification.setConfigurations(Config.emptyConfig());
                specification.setDataKey(new DataKey("AirPassengers"));
                generateAnalysisAssert(specification, "AirPassenger-" + name + ".json");
            }
        } finally {
            TestUtil.closeServer();
        }
    }

    private static void generateAnalysisAssert(AnalysisSpecification analysisSpecification, String fileName) throws ResseractException {
        List<AnalysisSpecification> analysisSpecifications = Collections.singletonList(analysisSpecification);
        List<AnalysisResult> analysisResults = CoreServer.runAnalysis(analysisSpecifications);
        Path currentRelativePath = Paths.get("./resseract-server/src/test/resources/AnalysisAsserts");
        String basePath = currentRelativePath.toAbsolutePath().toString();
        File file = new File(basePath, fileName);
        writeJSONToFile(analysisResults, file);
    }

    private static void writeJSONToFile(Object object, File file) throws ResseractException {
        try (FileWriter fileWriter = new FileWriter(file); BufferedWriter br = new BufferedWriter(fileWriter)) {
            br.write(JSONHandler.serialize(object));
        } catch (Exception e) {
            throw new ResseractException(CustomErrorReports.DATA_WRITE_ERROR, e);
        }
    }
}

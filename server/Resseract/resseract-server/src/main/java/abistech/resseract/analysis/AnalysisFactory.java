package abistech.resseract.analysis;

import abistech.resseract.analysis.impl.GroupByAnalysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AnalysisFactory {

    private static final Map<AnalysisType, Analysis> analysisMap = new HashMap<>();

    static {
        for (AnalysisType analysisType : AnalysisType.values()) {
            Analysis analysis = createAnalysis(analysisType);
            analysisMap.put(analysisType, analysis);
        }
    }

    private AnalysisFactory() {
    }

    public static Analysis getAnalysis(AnalysisType analysisType) {
        return analysisMap.get(analysisType);
    }

    private static Analysis createAnalysis(AnalysisType analysisType) {
        if (Objects.requireNonNull(analysisType) == AnalysisType.GROUP_BY) {
            return new GroupByAnalysis();
        }
        return null;
    }
}

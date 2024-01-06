package abistech.resseract.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author abisTarun
 */
public enum AnalysisType {
    GROUP_BY(AnalysisCategory.DESCRIPTIVE);

    private static final Map<AnalysisCategory, List<AnalysisType>> analysisCategoryVsTypes = new TreeMap<>();

    static {
        for (AnalysisType value : values()) {
            if (!analysisCategoryVsTypes.containsKey(value.category))
                analysisCategoryVsTypes.put(value.category, new ArrayList<>());
            analysisCategoryVsTypes.get(value.category).add(value);
        }
    }

    private final AnalysisCategory category;

    AnalysisType(AnalysisCategory category) {
        this.category = category;
    }

    public AnalysisCategory getCategory() {
        return category;
    }
}

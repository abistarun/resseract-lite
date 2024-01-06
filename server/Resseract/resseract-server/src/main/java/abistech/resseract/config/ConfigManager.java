package abistech.resseract.config;

import abistech.resseract.analysis.Analysis;
import abistech.resseract.analysis.AnalysisFactory;
import abistech.resseract.analysis.AnalysisType;
import abistech.resseract.pipeline.Pipeline;
import abistech.resseract.analysis.AnalysisSpecification;
import abistech.resseract.step.Step;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author abisTarun
 */
public class ConfigManager {

    private static final Map<AnalysisType, List<ConfigKey>> analysisVsConfigKeys = new HashMap<>();

    static {
        for (AnalysisType analysisType : AnalysisType.values()) {
            Analysis analysis = AnalysisFactory.getAnalysis(analysisType);
            Pipeline pipeline = analysis.getPipeline();
            List<ConfigKey> configKeys = new ArrayList<>();
            for (Step step : pipeline.getSteps()) {
                List<ConfigKey> requiredConfigsByStep = step.getRequiredConfigs();
                addAll(configKeys, requiredConfigsByStep);
            }
            analysisVsConfigKeys.put(analysisType, configKeys);
        }
    }

    private static void addAll(List<ConfigKey> dest, List<ConfigKey> src) {
        for (ConfigKey currKey : src) {
            if (!dest.contains(currKey))
                dest.add(currKey);
        }
    }

    public static void refactorConfig(AnalysisSpecification analysisSpecification) {
        Config config = analysisSpecification.getConfigurations();
        config.put(ConfigKey.DATA_KEY, analysisSpecification.getDataKey().getKey());

        List<ConfigKey> requiredConfigKeys = getRequiredConfigs(analysisSpecification.getAnalysisType());
        addMissingConfig(config, requiredConfigKeys);
    }

    public static List<ConfigKey> getRequiredConfigs(AnalysisType analysisType) {
        return analysisVsConfigKeys.get(analysisType);
    }

    private static void addMissingConfig(Config config, List<ConfigKey> requiredConfigKeys) {
        for (ConfigKey configKey : requiredConfigKeys) {
            if (config.get(configKey) == null) {
                Object defaultValue = configKey.getDefaultValue();
                if (configKey.getValueType() == ConfigValueType.LIST && defaultValue != null)
                    config.put(configKey, defaultValue.toString());
                else
                    config.put(configKey, defaultValue);
            }
            if (configKey.getValueType() == ConfigValueType.DOUBLE && config.get(configKey) instanceof Integer)
                config.put(configKey, (int) config.get(configKey) * 1.0);
        }
    }
}

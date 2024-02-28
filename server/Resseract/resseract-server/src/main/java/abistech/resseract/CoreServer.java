package abistech.resseract;

import abistech.resseract.analysis.*;
import abistech.resseract.async.DaemonContext;
import abistech.resseract.async.threadpool.ThreadPool;
import abistech.resseract.auth.AuthenticationService;
import abistech.resseract.auth.Feature;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.config.ConfigManager;
import abistech.resseract.data.DataInfo;
import abistech.resseract.data.DataService;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.migration.MigrationService;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.data.summary.DataSummary;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.pipeline.Pipeline;
import abistech.resseract.pipeline.executor.PipelineExecutionEngine;
import abistech.resseract.pipeline.executor.PipelineExecutor;
import abistech.resseract.step.elements.Dataset;
import abistech.resseract.util.Constants;
import abistech.resseract.util.Util;
import abistech.resseract.util.cache.ResseractCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StopWatch;

import java.util.*;

/**
 * @author abisTarun
 */
public class CoreServer {

    private static final Logger logger = LogManager.getLogger(CoreServer.class.getName());

    private static Map<SourceType, List<ConfigKey>> sourceConfigs;
    private static ResseractCache<AnalysisSpecification, Dataset> analysisCache;

    private CoreServer() {
    }

    public static void initialize(String profile) {
        try {
            logger.debug("Initializing Resseract with profile : " + profile);
            Properties properties = Util.loadProperties(profile);
            MigrationService.doMigration(properties);
            DataService.initialize(properties);
            sourceConfigs = DataService.fetchSourceConfigs();
            ThreadPool.initialize(properties);
            AuthenticationService.initialize(properties);
            DaemonContext.initialize();
            analysisCache = new ResseractCache<>(Constants.CACHE_INTERVAL_SEC);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static Collection<Feature> getAccessibleFeatures() {
        return AuthenticationService.getAccessibleFeatures();
    }

    public static Map<SourceType, List<ConfigKey>> getAllSources() {
        Set<SourceType> accessibleSourceTypes = AuthenticationService.getAccessibleSourceTypes();
        Map<SourceType, List<ConfigKey>> result = new HashMap<>();
        for (SourceType accessibleSourceType : accessibleSourceTypes) {
            result.put(accessibleSourceType, sourceConfigs.get(accessibleSourceType));
        }
        return result;
    }

    public static List<ConfigKey> getDataConfigurations(SourceType source, Config sourceConfig) throws ResseractException {
        return DataService.getDataConfigurations(source, sourceConfig);
    }

    public static void uploadData(SourceType source, Config configurations, boolean async) throws ResseractException {
        AuthenticationService.validateFeature(Feature.MANAGE_DATA);
        DataService.uploadData(source, configurations, async);
    }

    public static void addCustomColumn(DataKey dataKey, String columnName, String expression) throws ResseractException {
        AuthenticationService.validateFeature(Feature.MANAGE_DATA);
        DataService.addCustomColumn(dataKey, columnName, expression);
    }

    public static List<AnalysisResult> runAnalysis(List<AnalysisSpecification> specifications) throws ResseractException {
        List<AnalysisResult> results = new LinkedList<>();
        for (AnalysisSpecification analysisSpecification : specifications) {
            Dataset dataset = runAnalysis(analysisSpecification);
            AnalysisResult analysisResult = AnalysisResult.build(dataset);
            results.add(analysisResult);
        }
        return results;
    }

    private static Dataset runAnalysis(AnalysisSpecification analysisSpecification) throws ResseractException {
        ConfigManager.refactorConfig(analysisSpecification);
        return analysisCache.handle(analysisSpecification, key -> {
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            AuthenticationService.validateAnalysisType(analysisSpecification.getAnalysisType());
            Dataset dataset = runAnalysisCore(analysisSpecification);
            stopWatch.stop();
            logger.debug("Analysis executed for spec {} in {} ms", analysisSpecification, stopWatch.getTotalTimeMillis());
            return dataset;
        });
    }

    private static Dataset runAnalysisCore(AnalysisSpecification analysisSpecification) throws ResseractException {
        AnalysisType analysisType = analysisSpecification.getAnalysisType();
        Analysis analysis = AnalysisFactory.getAnalysis(analysisType);
        Pipeline pipeline = analysis.getPipeline();
        PipelineExecutor pipelineExecutor = PipelineExecutionEngine.getExecutor(analysis);
        return pipelineExecutor.execute(pipeline, analysisSpecification.getConfigurations());
    }

    public static void saveDashboard(String name, String data) {
        DataService.saveDashboard(name, data);
    }

    public static String getDashboardData(String name) throws ResseractException {
        return DataService.getDashboardData(name);
    }

    public static List<String> getAllDashboards() {
        return DataService.getAllDashboards();
    }

    public static List<String> exportAnalysis(List<AnalysisSpecification> configurations) throws ResseractException {
        List<AnalysisResult> analysisResults = runAnalysis(configurations);
        return DataService.convertAnalysisResultsToCSV(analysisResults);
    }

    public static Map<AnalysisType, List<ConfigKey>> getRequiredConfigurations(List<AnalysisType> analysisTypes) {
        Map<AnalysisType, List<ConfigKey>> result = new HashMap<>(analysisTypes.size());
        for (AnalysisType analysisType : analysisTypes) {
            List<ConfigKey> requiredConfigs = ConfigManager.getRequiredConfigs(analysisType);
            result.put(analysisType, requiredConfigs);
        }
        return result;
    }

    public static Double getDataUploadProgress(DataKey dataKey) throws ResseractException {
        return DataService.getDataUploadProgress(dataKey);
    }

    public static List<DataInfo> getAllDataInfo() throws ResseractException {
        return DataService.getAllDataInfo();
    }

    public static void deleteData(DataKey dataKey) throws ResseractException {
        AuthenticationService.validateFeature(Feature.MANAGE_DATA);
        DataService.deleteData(dataKey);
    }

    public static void deleteDashboard(String dashboardName) {
        DataService.deleteDashboard(dashboardName);
    }

    public static DataSummary getDataSummary(DataKey dataKey) throws ResseractException {
        return DataService.getDataSummary(dataKey);
    }

    public static void shutdown() {
        DataService.shutDown();
        DaemonContext.stop();
        ThreadPool.close();
        AuthenticationService.shutdown();
    }
}

package abistech.resseract.data;

import abistech.resseract.analysis.AnalysisElement;
import abistech.resseract.analysis.AnalysisResult;
import abistech.resseract.async.threadpool.ThreadPool;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.dao.DAOFactory;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;
import abistech.resseract.auth.AuthenticationService;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.source.Source;
import abistech.resseract.data.source.SourceFactory;
import abistech.resseract.data.source.SourceType;
import abistech.resseract.util.cache.ResseractCache;
import abistech.resseract.util.expression.ExpressionEvaluator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class DataService {

    private static final Map<DataKey, Object> dataUploadProgress = new HashMap<>();
    private static final Logger logger = LogManager.getLogger(DataService.class.getName());
    private static ResseractCache<List<Object>, Data> dataCache;

    public static void initialize(Properties properties) {
        DAOFactory.getDefaultDAO().initialize(properties);
        dataCache = new ResseractCache<>(30);
    }

    public static Map<SourceType, List<ConfigKey>> fetchSourceConfigs() {
        Map<SourceType, List<ConfigKey>> result = new HashMap<>();
        for (SourceType sourceType : SourceType.values()) {
            Source source = SourceFactory.getSource(sourceType);
            if (source == null)
                continue;
            List<ConfigKey> requiredConfigs = source.getSourceConfigs();
            result.put(sourceType, requiredConfigs);
        }
        return result;
    }

    public static List<ConfigKey> getDataConfigurations(SourceType sourceType, Config sourceConfig) throws ResseractException {
        Source source = SourceFactory.getSource(sourceType);
        assert source != null;
        return source.getDataConfigs(sourceConfig);
    }

    public static void uploadData(SourceType sourceType, Config configurations, boolean async) {
        if (async)
            ThreadPool.getInstance().submitTask(() -> uploadDataCore(sourceType, configurations));
        else
            uploadDataCore(sourceType, configurations);
    }

    private static void uploadDataCore(SourceType sourceType, Config configurations) {
        String dataKeyStr = (String) configurations.get(ConfigKey.DATA_KEY);
        DataKey dataKey = new DataKey(dataKeyStr);
        Source source = SourceFactory.getSource(sourceType);
        try {
            boolean isDataPresent = DAOFactory.getDefaultDAO().isDataPresent(dataKey);
            if (isDataPresent)
                throw new ResseractException(CustomErrorReports.DATA_DUPLICATE);
            dataUploadProgress.put(dataKey, 0d);
            assert source != null;

            int totalCount = source.getDataCount(configurations);
            int currCount = 1;
            boolean isLive = sourceType.isLive();
            for (int i = 0; i < totalCount; i++) {
                Data data;
                if (isLive)
                    data = source.fetchSchematicData(configurations);
                else
                    data = source.fetch(configurations);
                DAOFactory.getDefaultDAO().writeData(data, sourceType, configurations);
                dataUploadProgress.put(dataKey, (double) currCount++ / totalCount * 100);
            }
            dataUploadProgress.put(dataKey, 100d);
        } catch (ResseractException e) {
            logger.error("Data upload failed for data key : {} ", dataKeyStr, e);
            dataUploadProgress.put(dataKey, e);
        } catch (Exception e) {
            logger.error("Data upload failed for data key : {} ", dataKeyStr, e);
            dataUploadProgress.put(dataKey, new ResseractException(CustomErrorReports.UNKNOWN_ERROR, e));
        } finally {
            if (source != null)
                source.close();
        }
    }

    public static List<DataInfo> getAllDataInfo() throws ResseractException {
        return DAOFactory.getDefaultDAO().getAllDataInfo();
    }

    public static void addCustomColumn(DataKey dataKey, String columnName, String expression) throws ResseractException {
        Data data = DAOFactory.getDefaultDAO().readData(dataKey);
        ExpressionEvaluator evaluator = new ExpressionEvaluator(data);
        Column<?> column = evaluator.evaluateExpression(expression, columnName);
        DAOFactory.getDefaultDAO().addColumn(dataKey, column);
        removeFromDataCache(dataKey);
    }

    private static void removeFromDataCache(DataKey dataKey) {
        List<Object> cacheKey = new ArrayList<>(2);
        cacheKey.add(dataKey);
        cacheKey.add(AuthenticationService.getUserIdentifier());
        dataCache.remove(cacheKey);
    }

    public static Data getData(DataKey dataKey) throws ResseractException {
        List<Object> cacheKey = new ArrayList<>(2);
        cacheKey.add(dataKey);
        cacheKey.add(AuthenticationService.getUserIdentifier());
        Data result = dataCache.handle(cacheKey, key -> {
            DataInfo dataInfo = getDataInfo(dataKey);
            SourceType sourceType = dataInfo.getSourceType();
            if (sourceType.isLive()) {
                Source source = SourceFactory.getSource(sourceType);
                assert source != null;
                Data data = source.fetch(dataInfo.getConfig());
                fillCustomColumns(data);
                return data;
            }
            return DAOFactory.getDefaultDAO().readData(dataKey);
        });
        return new DataFrame((DataFrame) result);
    }

    private static void fillCustomColumns(Data data) throws ResseractException {
        Data internalData = DAOFactory.getDefaultDAO().readData(data.getDataKey());
        ExpressionEvaluator evaluator = new ExpressionEvaluator(data);
        for (Column<?> internalColumn : internalData.getAllColumns()) {
            Object expr = internalColumn.getProperty(Constants.EXPRESSION);
            if (expr != null) {
                String columnName = internalColumn.getName();
                Column<?> column = evaluator.evaluateExpression((String) expr, columnName);
                data.removeColumn(columnName);
                data.addColumn(column);
            }
        }
    }

    public static DataInfo getDataInfo(DataKey dataKey) throws ResseractException {
        return DAOFactory.getDefaultDAO().readDataInfo(dataKey);
    }

    public static void deleteData(DataKey dataKey) throws ResseractException {
        DAOFactory.getDefaultDAO().deleteData(dataKey);
        removeFromDataCache(dataKey);
    }

    static void deleteColumn(DataKey dataKey, String columnName) throws ResseractException {
        DAOFactory.getDefaultDAO().deleteColumn(dataKey, columnName);
        removeFromDataCache(dataKey);
    }

    public static List<String> convertAnalysisResultsToCSV(List<AnalysisResult> analysisResults) {
        List<String> csvStrings = new ArrayList<>(analysisResults.size());
        for (AnalysisResult analysisResult : analysisResults) {
            List<Object> index = analysisResult.getIndex();
            Map<String, AnalysisElement> columns = analysisResult.getColumns();
            List<String> columnNames = new ArrayList<>(columns.size());
            List<List<Object>> columnValues = new ArrayList<>(columns.size());
            for (Map.Entry<String, AnalysisElement> entry : columns.entrySet()) {
                columnNames.add(entry.getKey());
                columnValues.add(entry.getValue().getData());
            }
            String csvString = generateCSVString(index, columnNames, columnValues, false, null);
            csvStrings.add(csvString);
        }
        return csvStrings;
    }

    static String generateCSVString(List<Object> index, List<String> columnNames, List<List<Object>> columnValues, boolean includeHeaders, String identifier) {
        StringBuilder csvString = new StringBuilder();
        if (includeHeaders) {
            csvString.append(Constants.QUOTE).append(Constants.INDEX).append(Constants.QUOTE);
            if (identifier != null)
                csvString.append(Constants.CSV_SEPARATOR).append(Constants.QUOTE).append(Constants.IDENTIFIER).append(Constants.QUOTE);
            for (String columnName : columnNames) {
                csvString.append(Constants.CSV_SEPARATOR).append(Constants.QUOTE).append(columnName).append(Constants.QUOTE);
            }
            csvString.append(Constants.NEW_LINE);
        }

        SimpleDateFormat formatter = Constants.INDEX_FORMATTER;
        for (int i = 0; i < index.size(); i++) {
            Object date = index.get(i);
            if (date instanceof Date)
                date = formatter.format((Date) date);
            csvString.append(Constants.QUOTE).append(date).append(Constants.QUOTE);
            if (identifier != null)
                csvString.append(Constants.CSV_SEPARATOR).append(Constants.QUOTE).append(identifier).append(Constants.QUOTE);
            for (List<Object> column : columnValues) {
                csvString.append(Constants.CSV_SEPARATOR).append(Constants.QUOTE).append(column.get(i)).append(Constants.QUOTE);
            }
            csvString.append(Constants.NEW_LINE);
        }
        return csvString.toString();
    }

    public static Double getDataUploadProgress(DataKey dataKey) throws ResseractException {
        Object prog = dataUploadProgress.get(dataKey);
        if (prog instanceof ResseractException) {
            dataUploadProgress.remove(dataKey);
            throw (ResseractException) prog;
        }
        if (prog instanceof RuntimeException) {
            dataUploadProgress.remove(dataKey);
            throw (RuntimeException) prog;
        }
        Double progress = (Double) prog;
        if (progress == null)
            progress = 0d;
        if (progress == 100) {
            dataUploadProgress.remove(dataKey);
        }
        return progress;
    }

    public static void saveDashboard(String name, String data) {
        DAOFactory.getDefaultDAO().saveDashboard(name, data);
    }

    public static String getDashboardData(String dashboardName) throws ResseractException {
        return DAOFactory.getDefaultDAO().getDashboardData(dashboardName);
    }

    public static void deleteDashboard(String dashboardName) {
        DAOFactory.getDefaultDAO().deleteDashboard(dashboardName);
    }

    public static List<String> getAllDashboards() {
        return DAOFactory.getDefaultDAO().getAllDashboards();
    }

    public static void shutDown() {
        DAOFactory.getDefaultDAO().close();
    }
}

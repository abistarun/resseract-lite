package abistech.resseract.data.source.impl;

import abistech.resseract.auth.Feature;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.builder.ObjectBasedDataFrameBuilder;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.JSONHandler;
import abistech.resseract.util.Util;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.data.source.Source;

import java.sql.*;
import java.util.*;

abstract class AbstractDBSource implements Source {

    @Override
    public Feature getAuthorizedFeature() {
        return Feature.DB_DATA_SOURCES;
    }

    @Override
    public List<ConfigKey> getSourceConfigs() {
        LinkedList<ConfigKey> configKeys = new LinkedList<>();
        configKeys.add(new ConfigKey(ConfigKey.HOST).setDefaultValue("localhost"));
        configKeys.add(new ConfigKey(ConfigKey.DB_PORT).setDefaultValue(getDefaultPort()));
        configKeys.add(ConfigKey.DB_NAME);
        configKeys.add(ConfigKey.USERNAME);
        configKeys.add(ConfigKey.PASSWORD);
        configKeys.add(ConfigKey.QUERY);
        return configKeys;
    }


    @Override
    public List<ConfigKey> getDataConfigs(Config config) throws ResseractException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = (String) config.get(ConfigKey.QUERY);

            connection = getConnection(config);
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            Map<String, DataType> dataTypes = extractColumnDataTypes(resultSet);

            LinkedList<ConfigKey> configKeys = new LinkedList<>();
            configKeys.add(new ConfigKey(ConfigKey.DATA_KEY).setDefaultValue(getTableName(query)));
            configKeys.add(new ConfigKey(ConfigKey.COLUMN_DATA_TYPES).setDefaultValue(JSONHandler.serialize(dataTypes)));

            return configKeys;
        } catch (SQLException e) {
            throw new ResseractException(CustomErrorReports.DB_SQL_ERROR, e, e.getMessage());
        } finally {
            Util.close(resultSet);
            Util.close(preparedStatement);
            Util.close(connection);
        }
    }

    private Connection getConnection(Config config) throws ResseractException, SQLException {
        try {
            Class.forName(getDBDriverName());
            String username = (String) config.get(ConfigKey.USERNAME);
            String password = (String) config.get(ConfigKey.PASSWORD);
            String host = (String) config.get(ConfigKey.HOST);
            String port = (String) config.get(ConfigKey.DB_PORT);
            String dbName = (String) config.get(ConfigKey.DB_NAME);

            return DriverManager.getConnection(getUrl(host, port, dbName), username, password);
        } catch (ClassNotFoundException e) {
            throw new ResseractException(CustomErrorReports.DB_DRIVER_NOT_FOUND, e);
        }
    }

    @Override
    public Data fetchSchematicData(Config config) throws ResseractException {
        String dataKeyStr = (String) config.get(ConfigKey.DATA_KEY);
        DataKey dataKey = new DataKey(dataKeyStr);

        Map<String, DataType> dataTypes = extractDataTypesMapFromConfig(config);

        ObjectBasedDataFrameBuilder dataFrameBuilder = new ObjectBasedDataFrameBuilder(dataKey, dataTypes, Collections.emptyMap());
        return dataFrameBuilder.build();
    }

    @Override
    public Data fetch(Config config) throws ResseractException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            String query = (String) config.get(ConfigKey.QUERY);

            connection = getConnection(config);
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            String dataKeyStr = (String) config.get(ConfigKey.DATA_KEY);
            DataKey dataKey = new DataKey(dataKeyStr);

            Map<String, DataType> dataTypes = extractDataTypesMapFromConfig(config);
            int columnCount = dataTypes.keySet().toArray(new String[0]).length;
            Map<Integer, String> columnNameVsIndex = buildIndexVsNameMap(resultSet);

            ObjectBasedDataFrameBuilder dataFrameBuilder = new ObjectBasedDataFrameBuilder(dataKey, dataTypes, columnNameVsIndex);
            while (resultSet.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    row[i] = resultSet.getObject(i + 1);
                }
                dataFrameBuilder.addDataPoint(row);
            }
            return dataFrameBuilder.build();
        } catch (SQLException e) {
            throw new ResseractException(CustomErrorReports.DB_DRIVER_NOT_FOUND, e);
        } finally {
            Util.close(resultSet);
            Util.close(preparedStatement);
            Util.close(connection);
        }
    }

    private Map<Integer, String> buildIndexVsNameMap(ResultSet resultSet) throws SQLException {
        Map<Integer, String> columnNameVsIndex = new HashMap<>();
        int columnCount = resultSet.getMetaData().getColumnCount();
        for (int i = 0; i < columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i + 1).trim();
            columnNameVsIndex.put(i, columnName);
        }
        return columnNameVsIndex;
    }


    @Override
    public void close() {

    }

    @Override
    public int getDataCount(Config configurations) {
        return 1;
    }

    @SuppressWarnings("unchecked")
    private Map<String, DataType> extractDataTypesMapFromConfig(Config config) throws ResseractException {
        String columnDataTypesJSON = (String) config.get(ConfigKey.COLUMN_DATA_TYPES);
        Map<String, String> dataTypesMap = JSONHandler.deserialize(columnDataTypesJSON, HashMap.class);
        assert dataTypesMap != null;
        Map<String, DataType> result = new HashMap<>(dataTypesMap.size());
        dataTypesMap.forEach((key, value) -> result.put(key, DataType.valueOf(value)));
        return result;
    }

    private Map<String, DataType> extractColumnDataTypes(ResultSet resultSet) throws SQLException, ResseractException {
        int columnCount = resultSet.getMetaData().getColumnCount();
        Map<String, DataType> columnDataTypes = new HashMap<>(columnCount);
        for (int i = 0; i < columnCount; i++) {
            String columnName = resultSet.getMetaData().getColumnName(i + 1).trim();
            columnDataTypes.put(columnName, decideDataType(resultSet.getMetaData().getColumnType(i + 1)));
        }
        return columnDataTypes;
    }

    private DataType decideDataType(int columnType) throws ResseractException {
        switch (columnType) {
            case Types.TINYINT:
            case Types.SMALLINT:
            case Types.INTEGER:
            case Types.BIGINT:
            case Types.FLOAT:
            case Types.REAL:
            case Types.DOUBLE:
            case Types.NUMERIC:
            case Types.DECIMAL:
                return DataType.NUMERICAL;
            case Types.CHAR:
            case Types.VARCHAR:
            case Types.LONGVARCHAR:
            case Types.NVARCHAR:
            case Types.BLOB:
            case Types.CLOB:
                return DataType.CATEGORICAL;
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
            case Types.TIME_WITH_TIMEZONE:
            case Types.TIMESTAMP_WITH_TIMEZONE:
                return DataType.DATE;
            case Types.ARRAY:
            case Types.REF:
            case Types.DATALINK:
            case Types.ROWID:
            case Types.BOOLEAN:
            case Types.NCHAR:
            case Types.SQLXML:
            case Types.LONGNVARCHAR:
            case Types.NCLOB:
            case Types.REF_CURSOR:
            case Types.BIT:
            case Types.BINARY:
            case Types.VARBINARY:
            case Types.LONGVARBINARY:
            case Types.NULL:
            case Types.OTHER:
            case Types.JAVA_OBJECT:
            case Types.DISTINCT:
            case Types.STRUCT:
                throw new ResseractException(CustomErrorReports.INVALID_DATA_TYPE);
        }
        return null;
    }

    private String getTableName(String query) {
        if (query.contains("from"))
            return query.split("from")[1].trim().split("\\s")[0];
        return null;
    }

    abstract String getDBDriverName();

    abstract String getUrl(String host, String port, String dbName);

    abstract String getDefaultPort();
}

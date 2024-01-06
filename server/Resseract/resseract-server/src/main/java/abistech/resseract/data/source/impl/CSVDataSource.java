package abistech.resseract.data.source.impl;

import abistech.resseract.auth.Feature;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.config.ConfigValueType;
import abistech.resseract.data.builder.StringBasedDataFrameBuilder;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;
import abistech.resseract.util.Util;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.data.source.DateFormat;
import abistech.resseract.data.source.Source;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CSVDataSource implements Source {

    @Override
    public List<ConfigKey> getSourceConfigs() {
        LinkedList<ConfigKey> configKeys = new LinkedList<>();
        configKeys.add(ConfigKey.CSV_FILE);
        return configKeys;
    }

    @Override
    public List<ConfigKey> getDataConfigs(Config sourceConfigs) throws ResseractException {
        String filePath = (String) sourceConfigs.get(ConfigKey.CSV_FILE);
        File csvFile = new File(filePath);

        try (FileReader fileReader = new FileReader(csvFile);
             CSVReader csvReader = new CSVReader(fileReader)) {
            String[] allColumns = csvReader.readNext();
            List<Integer> columnIndexScanned = new ArrayList<>(allColumns.length);
            String[] elements;
            List<ConfigKey> result = new ArrayList<>(4);
            result.add(new ConfigKey(ConfigKey.DATA_KEY).setDefaultValue(csvFile.getName()).setEditable(true));
            while ((elements = csvReader.readNext()) != null) {
                for (int i = 0; i < elements.length; i++) {
                    String element = elements[i];
                    if (Util.isValidString(element) && !columnIndexScanned.contains(i)) {
                        columnIndexScanned.add(i);
                        String columnName = allColumns[i];
                        ConfigKey dataTypeConfig = new ConfigKey(columnName + Constants.DATA_TYPE_POSTFIX,
                                columnName + Constants.DATA_TYPE_POSTFIX, null, ConfigValueType.LIST, DataType.values(), true);
                        result.add(dataTypeConfig);
                        refactorConfigForColumn(result, element, columnName, dataTypeConfig);
                    }
                }
                if (columnIndexScanned.size() == allColumns.length)
                    break;
            }
            return result;
        } catch (IOException | CsvValidationException e) {
            throw new ResseractException(CustomErrorReports.FILE_UPLOAD_ERROR, e);
        }
    }

    private void refactorConfigForColumn(List<ConfigKey> result, String element, String columnName, ConfigKey dataTypeConfig) {
        if (isDouble(element))
            dataTypeConfig.setDefaultValue(DataType.NUMERICAL);
        else if (getDateFormat(element) != null) {
            dataTypeConfig.setDefaultValue(DataType.DATE);
            String dateFormat = getDateFormat(element);
            ConfigKey formatConfig = new ConfigKey(columnName + Constants.FORMAT_POSTFIX, columnName + Constants.FORMAT_POSTFIX,
                    dateFormat, ConfigValueType.LIST, DateFormat.getAllFormats(), true);
            result.add(formatConfig);
        } else if (element.equalsIgnoreCase(Constants.TRUE) || element.equalsIgnoreCase(Constants.FALSE)) {
            dataTypeConfig.setDefaultValue(DataType.BOOLEAN);
        } else {
            dataTypeConfig.setDefaultValue(DataType.CATEGORICAL);
        }
    }

    private String getDateFormat(String element) {
        return DateFormat.identifyFormat(element);
    }

    private boolean isDouble(String element) {
        try {
            Double.parseDouble(element.trim());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Data fetch(Config config) throws ResseractException {
        String filePath = (String) config.get(ConfigKey.CSV_FILE);
        String dataKeyStr = (String) config.get(ConfigKey.DATA_KEY);
        DataKey dataKey = new DataKey(dataKeyStr);
        File csvFile = new File(filePath);
        try (FileReader fileReader = new FileReader(csvFile);
             CSVReader csvReader = new CSVReader(fileReader)) {
            String[] columnNames = csvReader.readNext();

            Map<String, DataType> dataTypes = new HashMap<>();
            Map<String, String> formats = new HashMap<>();
            Map<Integer, String> columnIndexMap = new HashMap<>();
            for (int i = 0; i < columnNames.length; i++) {
                String columnName = columnNames[i];
                DataType dataType = DataType.valueOf((String) config.get(columnName + Constants.DATA_TYPE_POSTFIX));
                dataTypes.put(columnName, dataType);
                columnIndexMap.put(i, columnName);
                if (dataType == DataType.DATE) {
                    String format = (String) config.get(columnName + Constants.FORMAT_POSTFIX);
                    formats.put(columnName, format);
                }
            }
            StringBasedDataFrameBuilder builder = new StringBasedDataFrameBuilder(dataKey, dataTypes, formats, columnIndexMap);

            String[] values;
            while ((values = csvReader.readNext()) != null) {
                builder.addDataPoint(values);
            }
            return builder.build();
        } catch (IOException | CsvValidationException e) {
            throw new ResseractException(CustomErrorReports.FILE_UPLOAD_ERROR, e);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public int getDataCount(Config configurations) {
        return 1;
    }

    @Override
    public Data fetchSchematicData(Config configurations) {
        return null;
    }

    @Override
    public Feature getAuthorizedFeature() {
        return Feature.BASIC_DATA_SOURCES;
    }

}

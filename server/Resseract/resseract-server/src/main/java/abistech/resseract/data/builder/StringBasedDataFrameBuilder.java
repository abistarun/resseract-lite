package abistech.resseract.data.builder;

import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.impl.column.*;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Util;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.DataFrame;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class StringBasedDataFrameBuilder {

    private final Map<String, SimpleDateFormat> formats;
    private final Map<String, DataType> dataTypes;
    private final Map<Integer, String> columnIndexMap;
    private final Data data;

    public StringBasedDataFrameBuilder(DataKey dataKey, Map<String, DataType> dataTypes, Map<String, String> formats, Map<Integer, String> columnIndexMap) {
        this.formats = new HashMap<>(dataTypes.size());
        this.dataTypes = dataTypes;
        this.columnIndexMap = columnIndexMap;
        data = new DataFrame(dataKey);
        for (Map.Entry<String, DataType> entry : dataTypes.entrySet()) {
            String columnName = entry.getKey().trim();
            DataType dataType = entry.getValue();
            switch (dataType) {
                case DATE -> {
                    data.addDateColumn(new DateColumn(columnName));
                    this.formats.put(columnName, new SimpleDateFormat(formats.get(columnName)));
                }
                case NUMERICAL -> data.addNumericColumn(new DoubleColumn(columnName));
                case CATEGORICAL -> data.addCategoricalColumn(new StringColumn(columnName));
                case BOOLEAN -> data.addBooleanColumn(new BooleanColumn(columnName));
            }
        }

    }

    public void addDataPoint(String[] values) throws ResseractException {
        for (int i = 0; i < values.length; i++) {
            String value = values[i];
            String columnName = columnIndexMap.get(i).trim();
            if (!Util.isValidString(value))
                value = null;
            switch (dataTypes.get(columnName)) {
                case DATE -> {
                    try {
                        data.getDateColumn(columnName).add(value == null ? null : formats.get(columnName).parse(value));
                    } catch (ParseException e) {
                        throw new ResseractException(CustomErrorReports.INVALID_DATE_FORMAT, columnName, e);
                    }
                }
                case NUMERICAL -> {
                    try {
                        data.getNumericColumn(columnName).add(value == null ? null : Double.valueOf(value));
                    } catch (Exception e) {
                        throw new ResseractException(CustomErrorReports.INVALID_NUMBER, value, columnName, e);
                    }
                }
                case CATEGORICAL -> data.getCategoricalColumn(columnName).add(value);
                case BOOLEAN -> data.getBooleanColumn(columnName).add(Util.parseBoolean(value));
            }
        }
    }

    public Data build() {
        return data;
    }
}

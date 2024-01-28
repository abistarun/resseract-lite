package abistech.resseract.data.builder;

import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.*;
import abistech.resseract.util.Util;

import java.util.Date;
import java.util.Map;

public class ObjectBasedDataFrameBuilder {

    private final Map<String, DataType> dataTypes;
    private final Map<Integer, String> columnIndexMap;
    private final Data data;

    public ObjectBasedDataFrameBuilder(DataKey dataKey, Map<String, DataType> dataTypes, Map<Integer, String> columnIndexMap) {
        this.dataTypes = dataTypes;
        this.columnIndexMap = columnIndexMap;
        data = new DataFrame(dataKey);
        for (Map.Entry<String, DataType> entry : dataTypes.entrySet()) {
            String columnName = entry.getKey();
            DataType dataType = entry.getValue();
            switch (dataType) {
                case DATE:
                    data.addDateColumn(new DateColumn(columnName));
                    break;
                case NUMERICAL:
                    data.addNumericColumn(new DoubleColumn(columnName));
                    break;
                case CATEGORICAL:
                    data.addCategoricalColumn(new StringColumn(columnName));
                    break;
                case BOOLEAN:
                    data.addBooleanColumn(new BooleanColumn(columnName));
                    break;
            }
        }

    }

    public void addDataPoint(Object[] values) {
        for (int i = 0; i < values.length; i++) {
            Object value = values[i];
            String columnName = columnIndexMap.get(i);
            switch (dataTypes.get(columnName)) {
                case DATE:
                    data.getDateColumn(columnName).add((Date) value);
                    break;
                case NUMERICAL:
                    data.getNumericColumn(columnName).add(value == null ? null : ((Number) value).doubleValue());
                    break;
                case CATEGORICAL:
                    if (!Util.isValidString((String) value))
                        value = null;
                    data.getCategoricalColumn(columnName).add((String) value);
                    break;
                case BOOLEAN:
                    data.getBooleanColumn(columnName).add(Util.parseBoolean(value));
                    break;
            }
        }
    }

    public Data build() {
        return data;
    }
}
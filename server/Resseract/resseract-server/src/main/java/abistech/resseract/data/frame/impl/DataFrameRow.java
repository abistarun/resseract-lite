package abistech.resseract.data.frame.impl;

import abistech.resseract.data.frame.DataProperty;
import abistech.resseract.data.frame.Row;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author abisTarun
 */
@ToString
public class DataFrameRow implements Row {

    private final Map<String, Object> values;
    private final DataProperty dataProperty;
    private final Map<String, ColumnIndex> columnNameVsIndex;

    DataFrameRow(DataProperty dataProperty, Map<String, ColumnIndex> columnNameVsIndex) {
        this.dataProperty = dataProperty;
        this.columnNameVsIndex = columnNameVsIndex;
        this.values = new HashMap<>();
    }

    @Override
    public Object getIndex() {
        String indexColumnName = dataProperty.getIndexColumnName();
        return getValue(indexColumnName);
    }

    @Override
    public Set<String> getColumns() {
        return values.keySet();
    }

    @Override
    public Object[] toObjectArray() {
        Object[] result = new Object[this.values.size()];
        for (Map.Entry<String, ColumnIndex> entry : this.columnNameVsIndex.entrySet()) {
            String name = entry.getKey();
            ColumnIndex columnIndex = entry.getValue();
            result[columnIndex.getGlobalIndex()] = this.values.get(name);
        }
        return result;
    }

    @Override
    public void addValue(String columnName, Object value) {
        this.values.put(columnName, value);
    }

    @Override
    public Object getValue(String columnName) {
        return values.get(columnName);
    }

    @Override
    public Object getDefaultValue() {
        String primaryColumnName = dataProperty.getPrimaryColumnName();
        return getValue(primaryColumnName);
    }
}

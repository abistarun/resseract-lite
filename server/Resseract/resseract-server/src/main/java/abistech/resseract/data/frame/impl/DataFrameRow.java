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

    DataFrameRow(DataProperty dataProperty) {
        this.dataProperty = dataProperty;
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

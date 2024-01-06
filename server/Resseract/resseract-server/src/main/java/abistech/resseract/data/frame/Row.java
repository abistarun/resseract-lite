package abistech.resseract.data.frame;

import java.util.Set;

public interface Row {

    void addValue(String columnName, Object value);

    Object getValue(String columnName);

    Object getDefaultValue();

    Object getIndex();

    Set<String> getColumns();
}

package abistech.resseract.data.frame;

import abistech.resseract.data.frame.impl.column.BooleanColumn;
import abistech.resseract.data.frame.impl.column.DateColumn;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;

import java.util.Collection;
import java.util.List;

public interface Data extends Iterable<Row> {

    DataKey getDataKey();

    void setDataProperty(DataProperty dataProperty);

    DataProperty getDataProperty();

    Column<?> getIndex();

    void setIndex(Column<?> column);

    Column<?> getColumn(String columnName);

    DoubleColumn getNumericColumn(int columnIndex);

    StringColumn getCategoricalColumn(int columnIndex);

    BooleanColumn getBooleanColumn(int columnIndex);

    DateColumn getDateColumn(int columnIndex);

    DoubleColumn getNumericColumn(String columnName);

    StringColumn getCategoricalColumn(String columnName);

    BooleanColumn getBooleanColumn(String columnName);

    DateColumn getDateColumn(String columnName);

    void addColumn(Column<?> column);

    void addCategoricalColumn(StringColumn column);

    void addBooleanColumn(BooleanColumn column);

    void addDateColumn(DateColumn column);

    void addNumericColumn(DoubleColumn column);

    Column<?> getPrimaryColumn();

    void setPrimaryColumn(Column<?> column);

    void setPrimaryColumn(String columnName);

    void removeColumn(String columnName);

    void removeRow(int index);

    Collection<String> getColumnNames();

    void renameColumn(String oldName, String newName);

    int noOfRows();

    int noOfCols();

    List<DoubleColumn> getAllNumericColumns();

    List<Column<?>> getAllColumns();

    Data subset(int start, int end);

    void append(Data newData);

    void addRow(Row row);

    Row getRow(int index);

    Object getDefaultValueAtIndex(Object index);

    void rearrange(List<Integer> indicies);

    String getPrimaryColumnName();

    String getIndexName();
}

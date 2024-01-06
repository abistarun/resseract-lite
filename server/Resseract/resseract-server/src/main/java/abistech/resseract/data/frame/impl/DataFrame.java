package abistech.resseract.data.frame.impl;

import abistech.resseract.data.frame.*;
import abistech.resseract.data.frame.impl.column.*;
import abistech.resseract.util.Util;

import java.util.*;
import java.util.Map.Entry;

/**
 * @author abisTarun
 */
public class DataFrame implements Data {

    private final DataKey dataKey;
    private final List<DoubleColumn> numericColumns;
    private final List<DateColumn> dateColumns;
    private final List<StringColumn> categoricalColumns;
    private final List<BooleanColumn> booleanColumns;
    private final transient Map<String, ColumnIndex> columnNameVsIndex;
    private DataProperty dataProperty;

    public DataFrame(DataKey dataKey) {
        this.dataKey = dataKey;
        this.numericColumns = new ArrayList<>();
        this.dateColumns = new ArrayList<>();
        this.categoricalColumns = new ArrayList<>();
        this.booleanColumns = new ArrayList<>();
        this.columnNameVsIndex = new HashMap<>();
        this.dataProperty = new DataProperty();
    }

    public DataFrame(DataFrame data) {
        dataKey = new DataKey(data.dataKey.getKey());
        DataProperty dataProperty = data.dataProperty;
        this.dataProperty = DataProperty.build(dataProperty.getIndexColumnName(), dataProperty.getPrimaryColumnName());
        List<DoubleColumn> numericColumns = data.numericColumns;
        this.numericColumns = new ArrayList<>(numericColumns.size());
        for (DoubleColumn column : numericColumns) {
            this.numericColumns.add(new DoubleColumn(column));
        }
        List<DateColumn> dateColumns = data.dateColumns;
        this.dateColumns = new ArrayList<>(dateColumns.size());
        for (DateColumn column : dateColumns) {
            this.dateColumns.add(new DateColumn(column));
        }
        List<StringColumn> categoricalColumns = data.categoricalColumns;
        this.categoricalColumns = new ArrayList<>(categoricalColumns.size());
        for (StringColumn column : categoricalColumns) {
            this.categoricalColumns.add(new StringColumn(column));
        }
        List<BooleanColumn> booleanColumns = data.booleanColumns;
        this.booleanColumns = new ArrayList<>(booleanColumns.size());
        for (BooleanColumn column : booleanColumns) {
            this.booleanColumns.add(new BooleanColumn(column));
        }
        this.columnNameVsIndex = new HashMap<>(data.columnNameVsIndex);
    }

    public DataFrame(DataKey dataKey, DataProperty dataProperty) {
        this(dataKey);
        this.dataProperty = dataProperty;
    }

    @Override
    public DataKey getDataKey() {
        return dataKey;
    }

    @Override
    public void setDataProperty(DataProperty dataProperty) {
        this.dataProperty = dataProperty;
    }

    @Override
    public DataProperty getDataProperty() {
        return dataProperty;
    }

    @Override
    public Column<?> getIndex() {
        String indexColumnName = dataProperty.getIndexColumnName();
        return getColumn(indexColumnName);
    }

    @Override
    public void setIndex(Column<?> column) {
        addColumn(column);
        this.dataProperty.setIndexColumnName(column.getName());
    }

    @Override
    public Column<?> getColumn(String columnName) {
        ColumnIndex columnIndex = columnNameVsIndex.getOrDefault(columnName, null);
        if (columnIndex == null)
            return null;
        return switch (columnIndex.getDataType()) {
            case DATE -> dateColumns.get(columnIndex.getIndex());
            case NUMERICAL -> numericColumns.get(columnIndex.getIndex());
            case CATEGORICAL -> categoricalColumns.get(columnIndex.getIndex());
            case BOOLEAN -> booleanColumns.get(columnIndex.getIndex());
        };
    }

    @Override
    public DoubleColumn getNumericColumn(int columnIndex) {
        return this.numericColumns.get(columnIndex);
    }

    @Override
    public StringColumn getCategoricalColumn(int columnIndex) {
        return this.categoricalColumns.get(columnIndex);
    }

    @Override
    public BooleanColumn getBooleanColumn(int columnIndex) {
        return this.booleanColumns.get(columnIndex);
    }

    @Override
    public DateColumn getDateColumn(int columnIndex) {
        return this.dateColumns.get(columnIndex);
    }

    @Override
    public DoubleColumn getNumericColumn(String columnName) {
        ColumnIndex columnIndex = this.columnNameVsIndex.getOrDefault(columnName, null);
        if (columnIndex == null)
            return null;
        return this.getNumericColumn(columnIndex.getIndex());
    }

    @Override
    public StringColumn getCategoricalColumn(String columnName) {
        ColumnIndex columnIndex = this.columnNameVsIndex.getOrDefault(columnName, null);
        if (columnIndex == null)
            return null;
        return this.getCategoricalColumn(columnIndex.getIndex());
    }

    @Override
    public BooleanColumn getBooleanColumn(String columnName) {
        ColumnIndex columnIndex = this.columnNameVsIndex.getOrDefault(columnName, null);
        if (columnIndex == null)
            return null;
        return this.getBooleanColumn(columnIndex.getIndex());
    }

    @Override
    public DateColumn getDateColumn(String columnName) {
        ColumnIndex columnIndex = this.columnNameVsIndex.getOrDefault(columnName, null);
        if (columnIndex == null)
            return null;
        return this.getDateColumn(columnIndex.getIndex());
    }

    @Override
    public void addColumn(Column<?> column) {
        switch (column.getDataType()) {
            case DATE:
                addDateColumn((DateColumn) column);
                break;
            case NUMERICAL:
                addNumericColumn((DoubleColumn) column);
                break;
            case CATEGORICAL:
                addCategoricalColumn((StringColumn) column);
                break;
            case BOOLEAN:
                addBooleanColumn((BooleanColumn) column);
                break;
        }
    }

    @Override
    public void addCategoricalColumn(StringColumn column) {
        removeColumn(column.getName());
        this.categoricalColumns.add(column);
        this.columnNameVsIndex.put(column.getName(), new ColumnIndex(this.categoricalColumns.size() - 1, DataType.CATEGORICAL));
    }

    @Override
    public void addBooleanColumn(BooleanColumn column) {
        removeColumn(column.getName());
        this.booleanColumns.add(column);
        this.columnNameVsIndex.put(column.getName(), new ColumnIndex(this.booleanColumns.size() - 1, DataType.BOOLEAN));
    }

    @Override
    public void addDateColumn(DateColumn column) {
        removeColumn(column.getName());
        this.dateColumns.add(column);
        this.columnNameVsIndex.put(column.getName(), new ColumnIndex(this.dateColumns.size() - 1, DataType.DATE));
    }

    @Override
    public void addNumericColumn(DoubleColumn column) {
        removeColumn(column.getName());
        this.numericColumns.add(column);
        this.columnNameVsIndex.put(column.getName(), new ColumnIndex(this.numericColumns.size() - 1, DataType.NUMERICAL));
    }

    @Override
    public void addRow(Row row) {
        for (Entry<String, ColumnIndex> entry : columnNameVsIndex.entrySet()) {
            String name = entry.getKey();
            ColumnIndex index = entry.getValue();
            Object value = row.getValue(name);
            switch (index.getDataType()) {
                case DATE:
                    this.dateColumns.get(index.getIndex()).add((Date) value);
                    break;
                case NUMERICAL:
                    this.numericColumns.get(index.getIndex()).add((Double) value);
                    break;
                case CATEGORICAL:
                    this.categoricalColumns.get(index.getIndex()).add((String) value);
                    break;
                case BOOLEAN:
                    this.booleanColumns.get(index.getIndex()).add(Util.parseBoolean(value));
                    break;
            }
        }
    }

    @Override
    public Row getRow(int index) {
        Row row = new DataFrameRow(dataProperty);
        for (String columnName : getColumnNames()) {
            Object value = getColumn(columnName).get(index);
            row.addValue(columnName, value);
        }
        return row;
    }

    @Override
    public Object getDefaultValueAtIndex(Object index) {
        int indexRow = getIndex().indexOf(index);
        if (indexRow == -1)
            return null;
        return getPrimaryColumn().get(indexRow);
    }

    @Override
    public Column<?> getPrimaryColumn() {
        String primaryColumnName = dataProperty.getPrimaryColumnName();
        if (primaryColumnName == null)
            return null;
        return getColumn(primaryColumnName);
    }

    @Override
    public void setPrimaryColumn(Column<?> column) {
        addColumn(column);
        dataProperty.setPrimaryColumnName(column.getName());
    }

    @Override
    public void setPrimaryColumn(String columnName) {
        dataProperty.setPrimaryColumnName(columnName);
    }

    @Override
    public void removeColumn(String columnName) {
        ColumnIndex columnIndex = this.columnNameVsIndex.get(columnName);
        if (columnIndex == null)
            return;
        if (columnIndex == this.columnNameVsIndex.get(getPrimaryColumnName()))
            dataProperty.setPrimaryColumnName(null);
        switch (columnIndex.getDataType()) {
            case DATE:
                dateColumns.remove(columnIndex.getIndex());
                break;
            case NUMERICAL:
                numericColumns.remove(columnIndex.getIndex());
                break;
            case CATEGORICAL:
                categoricalColumns.remove(columnIndex.getIndex());
                break;
            case BOOLEAN:
                booleanColumns.remove(columnIndex.getIndex());
                break;
        }
        this.columnNameVsIndex.remove(columnName);
        for (Entry<String, ColumnIndex> entry : this.columnNameVsIndex.entrySet()) {
            ColumnIndex currIndex = entry.getValue();
            if (currIndex.getIndex() > columnIndex.getIndex() && columnIndex.getDataType() == currIndex.getDataType())
                currIndex.setIndex(currIndex.getIndex() - 1);
        }
    }

    @Override
    public void removeRow(int index) {
        for (DoubleColumn column : this.numericColumns) {
            column.remove(index);
        }
        for (DateColumn column : this.dateColumns) {
            column.remove(index);
        }
        for (StringColumn column : this.categoricalColumns) {
            column.remove(index);
        }
        for (BooleanColumn column : this.booleanColumns) {
            column.remove(index);
        }
    }

    @Override
    public Collection<String> getColumnNames() {
        return new HashSet<>(this.columnNameVsIndex.keySet());
    }

    @Override
    public String getPrimaryColumnName() {
        return dataProperty.getPrimaryColumnName();
    }

    @Override
    public String getIndexName() {
        return dataProperty.getIndexColumnName();
    }

    @Override
    public void rearrange(List<Integer> indicies) {
        for (Column<?> column : getAllColumns()) {
            column.rearrange(indicies);
        }
    }

    @Override
    public void renameColumn(String oldName, String newName) {
        Column<?> column = this.getColumn(oldName);
        Column<?> renamedColumn = column.rename(newName);
        this.addColumn(renamedColumn);
        if (oldName.equals(getPrimaryColumnName()))
            this.setPrimaryColumn(renamedColumn.getName());
        if (oldName.equals(getIndexName()))
            this.dataProperty.setIndexColumnName(renamedColumn.getName());
        this.removeColumn(oldName);
    }

    @Override
    public int noOfRows() {
        if (!numericColumns.isEmpty())
            return this.numericColumns.get(0).size();
        if (!dateColumns.isEmpty())
            return this.dateColumns.get(0).size();
        if (!categoricalColumns.isEmpty())
            return this.categoricalColumns.get(0).size();
        if (!booleanColumns.isEmpty())
            return this.booleanColumns.get(0).size();
        return 0;
    }

    @Override
    public int noOfCols() {
        return this.numericColumns.size() + this.dateColumns.size() + this.categoricalColumns.size() + this.booleanColumns.size();
    }

    @Override
    public List<DoubleColumn> getAllNumericColumns() {
        return this.numericColumns;
    }

    @Override
    public List<Column<?>> getAllColumns() {
        List<Column<?>> columns = new ArrayList<>(this.numericColumns);
        columns.addAll(this.dateColumns);
        columns.addAll(this.categoricalColumns);
        columns.addAll(this.booleanColumns);
        return columns;
    }

    @Override
    public Data subset(int start, int end) {
        Data data = new DataFrame(dataKey, new DataProperty(dataProperty));
        for (Column<?> column : this.numericColumns) {
            Column<?> newColumn = column.subset(start, end);
            data.addColumn(newColumn);
        }
        for (Column<?> column : this.dateColumns) {
            Column<?> newColumn = column.subset(start, end);
            data.addColumn(newColumn);
        }
        for (Column<?> column : this.categoricalColumns) {
            Column<?> newColumn = column.subset(start, end);
            data.addColumn(newColumn);
        }
        for (Column<?> column : this.booleanColumns) {
            Column<?> newColumn = column.subset(start, end);
            data.addColumn(newColumn);
        }
        return data;
    }

    @Override
    public void append(Data newData) {
        for (String columnName : newData.getColumnNames()) {
            Column<?> column = getColumn(columnName);
            if (column == null)
                continue;
            column.append(newData.getColumn(columnName));
        }
    }

    @Override
    public Iterator<Row> iterator() {
        return new DataFrameIterator();
    }

    private class DataFrameIterator implements Iterator<Row> {

        private int curr;

        DataFrameIterator() {
            curr = 0;
        }

        @Override
        public boolean hasNext() {
            return curr != DataFrame.this.noOfRows();
        }

        @Override
        public Row next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Row row = new DataFrameRow(dataProperty);
            for (String columnName : getColumnNames()) {
                Object value = getColumn(columnName).get(curr);
                row.addValue(columnName, value);
            }
            curr++;
            return row;
        }
    }
}
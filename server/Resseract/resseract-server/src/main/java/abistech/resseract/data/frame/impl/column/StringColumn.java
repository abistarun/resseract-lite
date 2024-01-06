package abistech.resseract.data.frame.impl.column;

import abistech.resseract.data.frame.Column;

import java.util.*;

public class StringColumn extends AbstractColumn<String> {

    private List<String> data;

    public StringColumn(String name, String[] data) {
        this(name, data.length);
        for (String datum : data) {
            this.add(datum);
        }
    }

    public StringColumn(String name) {
        this(name, 200);
    }

    public StringColumn(String name, int rowCnt) {
        super(name);
        this.data = new ArrayList<>(rowCnt);
    }

    public StringColumn(StringColumn column) {
        super(column);
        this.data = new ArrayList<>(column.data);
    }

    public StringColumn(String columnName, Map<String, Object> properties) {
        super(columnName, properties);
        this.data = new ArrayList<>(200);
    }

    @Override
    protected void setData(List<String> newData) {
        this.data = newData;
    }

    @Override
    public List<String> sort() {
        ArrayList<String> newData = new ArrayList<>(data);
        Collections.sort(newData);
        return newData;
    }

    @Override
    public String get(int index) {
        return this.data.get(index);
    }

    @Override
    public void set(int index, String data) {
        this.data.set(index, data);
    }

    @Override
    public void add(int index, String value) {
        this.data.add(index, value);
    }

    @Override
    public void add(String data) {
        this.data.add(data);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public DataType getDataType() {
        return DataType.CATEGORICAL;
    }

    @Override
    public void append(Column<?> column) {
        this.data.addAll(((StringColumn) column).data);
    }

    @Override
    public void remove(int index) {
        this.data.remove(index);
    }

    @Override
    public Column<String> rename(String newName) {
        StringColumn StringColumn = new StringColumn(newName);
        StringColumn.data = new ArrayList<>(this.data);
        return StringColumn;
    }

    @Override
    public StringColumn subset(int start, int end) {
        StringColumn newColumn = new StringColumn(this.getName(), this.size());
        newColumn.data = new ArrayList<>(this.data.subList(start, end));
        return newColumn;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int indexOf(Object value) {
        return this.data.indexOf(value);
    }

    @Override
    public void addAll(List<String> list) {
        this.data.addAll(list);
    }

    public List<String> toList() {
        return data;
    }

    @Override
    public Iterator<String> iterator() {
        return new StringColumn.StringColumnItr();
    }

    private class StringColumnItr implements Iterator<String> {

        private int curr;

        StringColumnItr() {
            this.curr = 0;
        }

        @Override
        public boolean hasNext() {
            return this.curr != StringColumn.this.size();
        }

        @Override
        public String next() {
            if (!hasNext())
                throw new NoSuchElementException();
            String num = StringColumn.this.get(curr);
            curr++;
            return num;
        }

    }

    @Override
    public String toString() {
        return "StringColumn{" +
                "data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringColumn Strings = (StringColumn) o;
        return Objects.equals(data, Strings.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}

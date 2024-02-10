package abistech.resseract.data.frame.impl.column;

import abistech.resseract.data.frame.Column;

import java.util.*;

public class BooleanColumn extends AbstractColumn<Boolean> {

    private List<Boolean> data;

    public BooleanColumn(String name, boolean[] data) {
        this(name, data.length);
        for (boolean datum : data) {
            this.add(datum);
        }
    }

    public BooleanColumn(String name) {
        this(name, 200);
    }

    public BooleanColumn(String name, int rowCnt) {
        super(name);
        this.data = new ArrayList<>(rowCnt);
    }

    public BooleanColumn(BooleanColumn column) {
        super(column);
        this.data = new ArrayList<>(column.data);
    }

    public BooleanColumn(String columnName, Map<String, Object> properties) {
        super(columnName, properties);
        this.data = new ArrayList<>(200);
    }

    @Override
    protected void setData(List<Boolean> newData) {
        this.data = newData;
    }

    @Override
    public List<Boolean> sort() {
        ArrayList<Boolean> newData = new ArrayList<>(data);
        Collections.sort(newData);
        return newData;
    }

    @Override
    public Boolean get(int index) {
        return this.data.get(index);
    }

    @Override
    public void set(int index, Boolean data) {
        this.data.set(index, data);
    }

    @Override
    public void add(int index, Boolean value) {
        this.data.add(index, value);
    }

    @Override
    public void add(Boolean data) {
        this.data.add(data);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public DataType getDataType() {
        return DataType.BOOLEAN;
    }

    @Override
    public void append(Column<?> column) {
        this.data.addAll(((BooleanColumn) column).data);
    }

    @Override
    public void remove(int index) {
        this.data.remove(index);
    }

    @Override
    public Column<Boolean> rename(String newName) {
        BooleanColumn StringColumn = new BooleanColumn(newName);
        StringColumn.data = new ArrayList<>(this.data);
        return StringColumn;
    }

    @Override
    public BooleanColumn subset(int start, int end) {
        BooleanColumn newColumn = new BooleanColumn(this.getName(), this.size());
        newColumn.data = new ArrayList<>(this.data.subList(start, end));
        return newColumn;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int indexOf(Object value) {
        return this.data.indexOf(value);
    }

    @Override
    public void addAll(List<Boolean> list) {
        this.data.addAll(list);
    }

    public List<Boolean> toList() {
        return data;
    }

    @Override
    public Iterator<Boolean> iterator() {
        return new BooleanColumnItr();
    }

    private class BooleanColumnItr implements Iterator<Boolean> {

        private int curr;

        BooleanColumnItr() {
            this.curr = 0;
        }

        @Override
        public boolean hasNext() {
            return this.curr != BooleanColumn.this.size();
        }

        @Override
        public Boolean next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Boolean num = BooleanColumn.this.get(curr);
            curr++;
            return num;
        }

    }

    @Override
    public String toString() {
        return "BooleanColumn{" +
                "data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BooleanColumn Strings = (BooleanColumn) o;
        return Objects.equals(data, Strings.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}

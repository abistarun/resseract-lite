package abistech.resseract.data.frame.impl.column;

import abistech.resseract.data.frame.Column;

import java.util.*;

/**
 * @author abisTarun
 */
public class DoubleColumn extends AbstractColumn<Double> {

    private List<Double> data;

    public DoubleColumn(String name, double[] data) {
        this(name, data.length);
        for (double datum : data) {
            this.add(datum);
        }
    }

    public DoubleColumn(String name) {
        this(name, 200);
    }

    public DoubleColumn(String name, int rowCnt) {
        super(name);
        this.data = new ArrayList<>(rowCnt);
    }

    public DoubleColumn(DoubleColumn column) {
        super(column);
        this.data = new ArrayList<>(column.data);
    }

    public DoubleColumn(String columnName, Map<String, Object> properties) {
        super(columnName, properties);
        this.data = new ArrayList<>(200);
    }

    @Override
    protected void setData(List<Double> newData) {
        this.data = newData;
    }

    @Override
    public List<Double> sort() {
        ArrayList<Double> newData = new ArrayList<>(data);
        Collections.sort(newData);
        return newData;
    }

    @Override
    public Double get(int index) {
        return this.data.get(index);
    }

    @Override
    public void set(int index, Double data) {
        this.data.set(index, data);
    }

    @Override
    public void add(int index, Double value) {
        this.data.add(index, value);
    }

    @Override
    public void add(Double data) {
        this.data.add(data);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public DataType getDataType() {
        return DataType.NUMERICAL;
    }

    @Override
    public void append(Column<?> column) {
        this.data.addAll(((DoubleColumn) column).data);
    }

    @Override
    public void remove(int index) {
        this.data.remove(index);
    }

    @Override
    public Column<Double> rename(String newName) {
        DoubleColumn doubleColumn = new DoubleColumn(newName);
        doubleColumn.data = new ArrayList<>(this.data);
        return doubleColumn;
    }

    @Override
    public DoubleColumn subset(int start, int end) {
        DoubleColumn newColumn = new DoubleColumn(this.getName(), this.size());
        newColumn.data = new ArrayList<>(this.data.subList(start, end));
        return newColumn;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int indexOf(Object value) {
        return this.data.indexOf(value);
    }

    @Override
    public void addAll(List<Double> list) {
        this.data.addAll(list);
    }

    public double[] toDouble() {
        final double[] result = new double[this.data.size()];
        for (int i = 0; i < this.data.size(); i++) {
            Double value = this.data.get(i);
            if (value == null)
                continue;
            result[i] = value;
        }
        return result;
    }

    public List<Double> toList() {
        return data;
    }

    @Override
    public Iterator<Double> iterator() {
        return new DoubleColumnItr();
    }

    private class DoubleColumnItr implements Iterator<Double> {

        private int curr;

        DoubleColumnItr() {
            this.curr = 0;
        }

        @Override
        public boolean hasNext() {
            return this.curr != DoubleColumn.this.size();
        }

        @Override
        public Double next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Double num = DoubleColumn.this.get(curr);
            curr++;
            return num;
        }

    }

    @Override
    public String toString() {
        return "DoubleColumn{" +
                "data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleColumn doubles = (DoubleColumn) o;
        return Objects.equals(data, doubles.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }
}

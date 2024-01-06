package abistech.resseract.data.frame.impl.column;

import abistech.resseract.data.frame.Column;

import java.util.*;

/**
 * @author abisTarun
 */
public class DateColumn extends AbstractColumn<Date> {

    private List<Date> data;

    public DateColumn(String name) {
        this(name, 200);
    }

    public DateColumn(DateColumn dateColumn) {
        this(dateColumn.getName(), dateColumn.size());
        this.data = new ArrayList<>(dateColumn.data);
    }

    public DateColumn(String name, int rowCnt) {
        super(name);
        this.data = new ArrayList<>(rowCnt);
    }

    public DateColumn(String columnName, Map<String, Object> properties) {
        super(columnName, properties);
        this.data = new ArrayList<>(200);
    }

    @Override
    public Date get(int index) {
        return this.data.get(index);
    }

    @Override
    protected void setData(List<Date> newData) {
        this.data = newData;
    }

    @Override
    public List<Date> sort() {
        ArrayList<Date> newData = new ArrayList<>(data);
        Collections.sort(newData);
        return newData;
    }

    @Override
    public void set(int index, Date data) {
        this.data.set(index, data);
    }

    @Override
    public void add(int index, Date value) {
        this.data.add(index, value);
    }

    @Override
    public void add(Date data) {
        this.data.add(data);
    }

    @Override
    public int size() {
        return this.data.size();
    }

    @Override
    public DataType getDataType() {
        return DataType.DATE;
    }

    @Override
    public void append(Column<?> column) {
        this.data.addAll(((DateColumn) column).data);
    }

    @Override
    public void remove(int index) {
        this.data.remove(index);
    }

    @Override
    public Column<Date> rename(String newName) {
        DateColumn dateColumn = new DateColumn(newName);
        dateColumn.data = new ArrayList<>(this.data);
        return dateColumn;
    }

    @Override
    public List<Date> toList() {
        return data;
    }

    @Override
    public DateColumn subset(int start, int end) {
        DateColumn newColumn = new DateColumn(this.getName(), this.size());
        newColumn.data = new ArrayList<>(this.data.subList(start, end));
        return newColumn;
    }

    @SuppressWarnings("SuspiciousMethodCalls")
    @Override
    public int indexOf(Object value) {
        return this.data.indexOf(value);
    }

    @Override
    public void addAll(List<Date> list) {
        this.data.addAll(list);
    }

    @Override
    public Iterator<Date> iterator() {
        return new DateColumnItr();
    }

    private class DateColumnItr implements Iterator<Date> {

        private int curr;

        DateColumnItr() {
            this.curr = 0;
        }

        @Override
        public boolean hasNext() {
            return this.curr != DateColumn.this.size();
        }

        @Override
        public Date next() {
            if (!hasNext())
                throw new NoSuchElementException();
            Date date = DateColumn.this.get(curr);
            curr++;
            return date;
        }

    }

    @Override
    public String toString() {
        return "DateColumn{" +
                "data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DateColumn dates = (DateColumn) o;
        return Objects.equals(data, dates.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), data);
    }
}

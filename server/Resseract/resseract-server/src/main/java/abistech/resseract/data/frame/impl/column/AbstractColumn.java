package abistech.resseract.data.frame.impl.column;

import abistech.resseract.util.Constants;
import abistech.resseract.data.frame.Column;

import java.util.*;

/**
 * @param <D>
 * @author abisTarun
 */
abstract class AbstractColumn<D> implements Column<D> {

    private final String name;
    private final Map<String, Object> properties;

    AbstractColumn(String name) {
        this.name = name;
        this.properties = new HashMap<>();
        properties.put(Constants.DATA_TYPE, getDataType());
    }

    AbstractColumn(AbstractColumn<D> column) {
        this.name = column.name;
        this.properties = new HashMap<>(column.properties);
    }

    AbstractColumn(String columnName, Map<String, Object> properties) {
        name = columnName;
        this.properties = properties;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Map<String, Object> getProperties() {
        return properties;
    }

    @Override
    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    @Override
    public void addProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    @Override
    public List<Integer> getSortIndices() {
        List<D> data = new ArrayList<>(toList());
        List<D> sortedList = sort();
        List<Integer> sortIndices = new ArrayList<>(data.size());

        for (D datum : sortedList) {
            int index = data.indexOf(datum);
            sortIndices.add(index);
            data.set(index, null);
        }
        return sortIndices;
    }

    @Override
    public void rearrange(List<Integer> indicies) {
        List<D> newData = new ArrayList<>(size());
        for (Integer index : indicies) {
            newData.add(get(index));
        }
        setData(newData);
    }

    protected abstract void setData(List<D> newData);

    @Override
    public String toString() {
        return "AbstractColumn{" +
                "name='" + name + '\'' +
                ", properties=" + properties +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractColumn<?> that = (AbstractColumn<?>) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(properties, that.properties);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, properties);
    }

}

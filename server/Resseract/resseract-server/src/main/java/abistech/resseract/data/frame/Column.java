package abistech.resseract.data.frame;

import abistech.resseract.data.frame.impl.column.DataType;

import java.util.List;
import java.util.Map;

/**
 * @author abisTarun
 */
public interface Column<D> extends Iterable<D> {

    String getName();

    D get(int index);

    void set(int index, D data);

    int size();

    DataType getDataType();

    void add(int index, D value);

    void add(D data);

    void append(Column<?> column);

    void remove(int index);

    Column<D> rename(String newName);

    List<D> toList();

    Column<D> subset(int start, int end);

    int indexOf(Object value);

    void addAll(List<D> list);

    Map<String, Object> getProperties();

    Object getProperty(String key);

    void addProperty(String key, Object value);

    List<Integer> getSortIndices();

    void rearrange(List<Integer> indicies);

    List<D> sort();
}

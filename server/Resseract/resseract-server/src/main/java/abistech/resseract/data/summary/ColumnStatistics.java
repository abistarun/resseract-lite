package abistech.resseract.data.summary;

import abistech.resseract.data.frame.impl.column.DataType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class ColumnStatistics<T> {

    private final String name;
    private final DataType type;
    private final int uniqueValueCount;
    private final int nullValueCount;
    private final Map<T, Integer> valueCount;
}

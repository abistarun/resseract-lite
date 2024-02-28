package abistech.resseract.data.summary;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class DataSummary {
    private final int rowCount;
    private final int columnCount;
    private final List<Object[]> head;
    private List<ColumnStatistics<?>> columnStatistics;
}

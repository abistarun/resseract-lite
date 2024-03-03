package abistech.resseract.data.summary;

import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.impl.column.BooleanColumn;
import abistech.resseract.data.frame.impl.column.DateColumn;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.util.HistogramUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataSummaryBuilder {

    private static final int MAX_VALUE_COUNT = 10;
    private static final String OTHERS = "Others";
    private static final int HEAD_COUNT = 10;

    public static DataSummary buildSummary(Data data) {
        List<Column<?>> allColumns = data.getAllColumns();
        List<ColumnStatistics<?>> columnStatisticsList = new ArrayList<>(allColumns.size());
        for (Column<?> column : allColumns) {
            switch (column.getDataType()) {
                case CATEGORICAL -> {
                    ColumnStatistics<String> columnStatistics = extractCategoricalColumnStatistics((StringColumn) column);
                    columnStatisticsList.add(columnStatistics);
                }
                case DATE -> {
                    ColumnStatistics<Date> columnStatistics = extractDateColumnStatistics((DateColumn) column);
                    columnStatisticsList.add(columnStatistics);
                }
                case NUMERICAL -> {
                    ColumnStatistics<Double> columnStatistics = extractNumericalColumnStatistics((DoubleColumn) column);
                    columnStatisticsList.add(columnStatistics);
                }
                case BOOLEAN -> {
                    ColumnStatistics<Boolean> columnStatistics = extractBooleanColumnStatistics((BooleanColumn) column);
                    columnStatisticsList.add(columnStatistics);
                }
            }
        }
        return new DataSummary(data.noOfRows(), data.noOfCols(), data.head(HEAD_COUNT), columnStatisticsList);
    }

    private static ColumnStatistics<String> extractCategoricalColumnStatistics(StringColumn column) {
        int uniqueValueCount = 0;
        int nullValueCount = 0;
        List<String> columnData = column.toList();
        Map<String, Integer> valueCount = new HashMap<>();
        for (String d : columnData) {
            if (d == null) {
                nullValueCount++;
                continue;
            }
            if (!valueCount.containsKey(d)) {
                uniqueValueCount++;
                valueCount.put(d, 0);
            }
            valueCount.put(d, valueCount.get(d) + 1);
        }
        Map<String, Integer> filteredValues = valueCount.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(MAX_VALUE_COUNT)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        Integer othersCount = valueCount.entrySet().stream()
                .filter(x -> !filteredValues.containsKey(x.getKey())).map(Map.Entry::getValue).
                reduce(0, Integer::sum);
        if (othersCount > 0)
            filteredValues.put(OTHERS, othersCount);
        return new ColumnStatistics<>(column.getName(), column.getDataType(), uniqueValueCount, nullValueCount, filteredValues);
    }

    private static ColumnStatistics<Boolean> extractBooleanColumnStatistics(BooleanColumn column) {
        int uniqueValueCount = 0;
        int nullValueCount = 0;
        int trueValueCount = 0;
        int falseValueCount = 0;
        List<Boolean> columnData = column.toList();
        for (Boolean d : columnData) {
            if (d == null) {
                nullValueCount++;
                continue;
            }
            if (d) {
                trueValueCount++;
            } else {
                falseValueCount++;
            }
        }
        Map<Boolean, Integer> valueCount = new HashMap<>(2);
        valueCount.put(true, trueValueCount);
        valueCount.put(false, falseValueCount);
        return new ColumnStatistics<>(column.getName(), column.getDataType(), uniqueValueCount, nullValueCount, valueCount);
    }

    private static ColumnStatistics<Double> extractNumericalColumnStatistics(DoubleColumn column) {
        List<Double> columnData = column.toList();
        int nullValueCount = (int) columnData.stream().filter(Objects::isNull).count();
        int uniqueValueCount = (int) columnData.stream().distinct().count();
        Map<Double, Integer> valueCount = HistogramUtil.createHistogramDouble(columnData);
        return new ColumnStatistics<>(column.getName(), column.getDataType(), uniqueValueCount, nullValueCount, valueCount);
    }

    private static ColumnStatistics<Date> extractDateColumnStatistics(DateColumn column) {
        List<Date> columnData = column.toList();
        int nullValueCount = (int) columnData.stream().filter(Objects::isNull).count();
        int uniqueValueCount = (int) columnData.stream().distinct().count();
        Map<Date, Integer> valueCount = HistogramUtil.createHistogramDate(columnData);
        return new ColumnStatistics<>(column.getName(), column.getDataType(), uniqueValueCount, nullValueCount, valueCount);
    }
}

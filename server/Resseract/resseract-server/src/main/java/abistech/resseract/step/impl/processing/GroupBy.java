package abistech.resseract.step.impl.processing;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.ColumnNames;
import abistech.resseract.data.frame.Data;
import abistech.resseract.step.elements.CategoricalAggregationType;
import abistech.resseract.step.elements.Dataset;
import abistech.resseract.step.elements.DatasetImpl;
import abistech.resseract.step.elements.NumericalAggregationType;
import abistech.resseract.util.Util;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.data.frame.impl.column.DateColumn;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.step.impl.AbstractStep;

import java.util.*;
import java.util.stream.Collectors;

public class GroupBy extends AbstractStep {

    @Override
    public Dataset execute(Dataset input, Config config) {
        Data data = input.getData();
        String groupByColumn = (String) config.get(ConfigKey.GROUPBY_COLUMN_NAME);
        if (groupByColumn == null)
            groupByColumn = ColumnNames.INDEX;
        @SuppressWarnings({"unchecked", "rawtypes"})
        List<String> targetColumns = (List) config.get(ConfigKey.TARGET_COLUMNS);
        if (targetColumns == null || targetColumns.isEmpty())
            targetColumns = data.getAllColumns().stream().map(Column::getName).collect(Collectors.toList());
        targetColumns.remove(groupByColumn);

        DataType indexDataType = data.getColumn(groupByColumn) == null ? DataType.CATEGORICAL : data.getColumn(groupByColumn).getDataType();
        Map<String, DataType> targetDataTypes = extractDataTypes(data, targetColumns);

        Map<Object, Map<String, List<Object>>> groupedValues = getGroupedValues(data, groupByColumn, targetColumns, indexDataType, targetDataTypes);

        Data result = buildData(data, groupByColumn, targetColumns, groupedValues, indexDataType, targetDataTypes);
        fillValues(groupedValues, groupByColumn, config, result, indexDataType, targetDataTypes);

        result.setPrimaryColumn(targetColumns.get(0));
        return new DatasetImpl(result);
    }

    private Map<String, DataType> extractDataTypes(Data data, List<String> targetColumns) {
        Map<String, DataType> targetDataTypes = new HashMap<>();
        for (String targetColumn : targetColumns) {
            targetDataTypes.put(targetColumn, data.getColumn(targetColumn).getDataType());
        }
        return targetDataTypes;
    }

    private void fillValues(Map<Object, Map<String, List<Object>>> groupedValues, String groupByColumn, Config config, Data result, DataType indexDataType, Map<String, DataType> dataTypeMap) {
        Aggregator numericalAgg = extractNumericalAggregator(config);
        Aggregator categoricalAgg = extractCategoricalAggregator(config);
        for (Map.Entry<Object, Map<String, List<Object>>> entry : groupedValues.entrySet()) {
            Object groupedValue = entry.getKey();
            switch (indexDataType) {
                case NUMERICAL -> result.getNumericColumn(groupByColumn).add((Double) groupedValue);
                case CATEGORICAL -> result.getCategoricalColumn(groupByColumn).add((String) groupedValue);
                case DATE -> result.getDateColumn(groupByColumn).add((Date) groupedValue);
            }
            Map<String, List<Object>> targetColumnValues = entry.getValue();
            for (Map.Entry<String, List<Object>> targetEntry : targetColumnValues.entrySet()) {
                String columnName = targetEntry.getKey();
                DataType dataType = dataTypeMap.get(columnName);
                if (dataType == DataType.NUMERICAL) {
                    Double aggregatedValue = (Double) numericalAgg.aggregate(targetEntry.getValue());
                    result.getNumericColumn(columnName).add(aggregatedValue);
                } else if (dataType == DataType.CATEGORICAL) {
                    String aggregatedValue = (String) categoricalAgg.aggregate(targetEntry.getValue());
                    result.getCategoricalColumn(columnName).add(aggregatedValue);
                }
            }
        }
    }

    private Data buildData(Data data, String groupByColumn, List<String> targetColumns, Map<Object, Map<String, List<Object>>> groupedValues, DataType indexDataType, Map<String, DataType> targetDataTypes) {
        Data result = new DataFrame(data.getDataKey(), data.getDataProperty());
        Column<?> groupedColumn = getIndexColumn(groupByColumn, indexDataType);
        result.setIndex(groupedColumn);
        for (String targetColumn : targetColumns) {
            if (targetDataTypes.get(targetColumn) == DataType.NUMERICAL)
                result.addNumericColumn(new DoubleColumn(targetColumn, groupedValues.size()));
            else if (targetDataTypes.get(targetColumn) == DataType.CATEGORICAL)
                result.addCategoricalColumn(new StringColumn(targetColumn, groupedValues.size()));
        }
        return result;
    }

    private Column<?> getIndexColumn(String groupByColumn, DataType indexDataType) {
        return switch (indexDataType) {
            case NUMERICAL -> new DoubleColumn(groupByColumn);
            case CATEGORICAL -> new StringColumn(groupByColumn);
            case DATE -> new DateColumn(groupByColumn);
            default -> null;
        };
    }

    private Aggregator extractNumericalAggregator(Config config) {
        NumericalAggregationType aggregationType = NumericalAggregationType.valueOf((String) config.get(ConfigKey.NUMERICAL_AGGREGATION));
        return AggregatorFactory.getAggregator(aggregationType);
    }

    private Aggregator extractCategoricalAggregator(Config config) {
        CategoricalAggregationType aggregationType = CategoricalAggregationType.valueOf((String) config.get(ConfigKey.CATEGORICAL_AGGREGATION));
        return AggregatorFactory.getAggregator(aggregationType);
    }

    private Map<Object, Map<String, List<Object>>> getGroupedValues(Data data, String groupByColumn, List<String> targetColumns, DataType indexDataType, Map<String, DataType> targetDataTypes) {
        Map<Object, Map<String, List<Object>>> groupedValues = new TreeMap<>();
        for (Row row : data) {
            Object value = (row.getValue(groupByColumn) == null) ? Util.getNullValue(indexDataType) : row.getValue(groupByColumn);
            if (!groupedValues.containsKey(value)) {
                groupedValues.put(value, new HashMap<>());
            }
            Map<String, List<Object>> targetValues = groupedValues.get(value);
            for (String targetColumn : targetColumns) {
                if (!targetValues.containsKey(targetColumn))
                    targetValues.put(targetColumn, new LinkedList<>());
                Object targetValue = row.getValue(targetColumn);
                if (targetDataTypes.get(targetColumn) == DataType.NUMERICAL) {
                    targetValue = targetValue == null ? 0d : targetValue;
                }
                targetValues.get(targetColumn).add(targetValue);
            }
        }
        return groupedValues;
    }

    @Override
    public List<ConfigKey> getRequiredConfigs() {
        return Arrays.asList(ConfigKey.NUMERICAL_AGGREGATION, ConfigKey.CATEGORICAL_AGGREGATION, ConfigKey.GROUPBY_COLUMN_NAME, ConfigKey.TARGET_COLUMNS);
    }

    private static class AggregatorFactory {
        static Aggregator getAggregator(NumericalAggregationType aggregationType) {
            return switch (aggregationType) {
                case SUM -> new SumNumericalAggregator();
                case AVERAGE -> new AverageNumericalAggregator();
                case LAST_VALUE -> new LastValueAggregator();
                case FIRST_VALUE -> new FirstValueAggregator();
            };
        }

        static Aggregator getAggregator(CategoricalAggregationType aggregationType) {
            return switch (aggregationType) {
                case LAST_VALUE -> new LastValueAggregator();
                case FIRST_VALUE -> new FirstValueAggregator();
            };
        }
    }

    private interface Aggregator {
        Object aggregate(List<Object> values);
    }

    private static class SumNumericalAggregator implements Aggregator {

        @Override
        public Double aggregate(List<Object> values) {
            return values.stream().map(i -> (Double) i).mapToDouble(Double::doubleValue).sum();
        }
    }

    private static class AverageNumericalAggregator implements Aggregator {

        @Override
        public Double aggregate(List<Object> values) {
            OptionalDouble average = values.stream().filter(Objects::nonNull).map(i -> (Double) i).mapToDouble(Double::doubleValue).average();
            return average.isPresent() ? average.getAsDouble() : -1;
        }
    }

    private static class LastValueAggregator implements Aggregator {
        @Override
        public Object aggregate(List<Object> values) {
            return values.get(values.size() - 1);
        }
    }

    private static class FirstValueAggregator implements Aggregator {
        @Override
        public Object aggregate(List<Object> values) {
            return values.get(0);
        }
    }
}

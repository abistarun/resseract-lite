package abistech.resseract.step.impl.processing;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.DateColumn;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.step.elements.CategoricalAggregationType;
import abistech.resseract.step.elements.DatasetImpl;
import abistech.resseract.step.elements.NumericalAggregationType;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GroupByTest {

    @Test
    public void testGroupByAggregationSum() {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("GroupByCol", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));
        input.addNumericColumn(new DoubleColumn("N2", new double[]{10, 20, 30, 40, 50, 60, 70, 80}));
        input.addNumericColumn(new DoubleColumn("N3", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));

        GroupBy step = new GroupBy();
        Config config = new Config();
        config.put(ConfigKey.GROUPBY_COLUMN_NAME, "GroupByCol");
        config.put(ConfigKey.TARGET_COLUMNS, Arrays.asList("N1", "N2"));
        config.put(ConfigKey.NUMERICAL_AGGREGATION, NumericalAggregationType.SUM.name());
        config.put(ConfigKey.CATEGORICAL_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());
        config.put(ConfigKey.DATE_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(3, result.noOfCols());
        Assert.assertEquals(Arrays.asList("A", "B", "C"), result.getCategoricalColumn("GroupByCol").toList());
        Assert.assertEquals(Arrays.asList(10d, 15d, 11d), result.getNumericColumn("N1").toList());
        Assert.assertEquals(Arrays.asList(100d, 150d, 110d), result.getNumericColumn("N2").toList());
    }

    @Test
    public void testGroupByAggregationAvg() {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("GroupByCol", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));
        input.addNumericColumn(new DoubleColumn("N2", new double[]{10, 20, 30, 40, 50, 60, 70, 80}));
        input.addNumericColumn(new DoubleColumn("N3", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));

        GroupBy step = new GroupBy();
        Config config = new Config();
        config.put(ConfigKey.GROUPBY_COLUMN_NAME, "GroupByCol");
        config.put(ConfigKey.TARGET_COLUMNS, Arrays.asList("N1", "N2"));
        config.put(ConfigKey.NUMERICAL_AGGREGATION, NumericalAggregationType.AVERAGE.name());
        config.put(ConfigKey.CATEGORICAL_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());
        config.put(ConfigKey.DATE_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(3, result.noOfCols());
        Assert.assertEquals(Arrays.asList("A", "B", "C"), result.getCategoricalColumn("GroupByCol").toList());
        Assert.assertEquals(Arrays.asList(10d / 3, 15d / 3, 11d / 2), result.getNumericColumn("N1").toList());
        Assert.assertEquals(Arrays.asList(100d / 3, 150d / 3, 110d / 2), result.getNumericColumn("N2").toList());
    }

    @Test
    public void testGroupByAggregationFirstValue() {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("GroupByCol", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addCategoricalColumn(new StringColumn("C1", new String[]{"a", "b", "c", "d", "e", "f", "g", "h"}));

        GroupBy step = new GroupBy();
        Config config = new Config();
        config.put(ConfigKey.GROUPBY_COLUMN_NAME, "GroupByCol");
        config.put(ConfigKey.TARGET_COLUMNS, Collections.singletonList("C1"));
        config.put(ConfigKey.NUMERICAL_AGGREGATION, NumericalAggregationType.SUM.name());
        config.put(ConfigKey.CATEGORICAL_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());
        config.put(ConfigKey.DATE_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(2, result.noOfCols());
        Assert.assertEquals(Arrays.asList("A", "B", "C"), result.getCategoricalColumn("GroupByCol").toList());
        Assert.assertEquals(Arrays.asList("a", "b", "c"), result.getCategoricalColumn("C1").toList());
    }

    @Test
    public void testGroupByAggregationDate() throws ParseException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("GroupByCol", new String[]{"A", "B", "C", "A", "A"}));
        DateColumn dateColumn = new DateColumn("D1", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        dateColumn.add(sdf.parse("01/19"));
        dateColumn.add(sdf.parse("02/19"));
        dateColumn.add(sdf.parse("03/19"));
        dateColumn.add(sdf.parse("04/19"));
        dateColumn.add(sdf.parse("05/19"));
        input.addDateColumn(dateColumn);

        GroupBy step = new GroupBy();
        Config config = new Config();
        config.put(ConfigKey.GROUPBY_COLUMN_NAME, "GroupByCol");
        config.put(ConfigKey.TARGET_COLUMNS, Collections.singletonList("D1"));
        config.put(ConfigKey.NUMERICAL_AGGREGATION, NumericalAggregationType.SUM.name());
        config.put(ConfigKey.CATEGORICAL_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());
        config.put(ConfigKey.DATE_AGGREGATION, CategoricalAggregationType.LAST_VALUE.name());

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(2, result.noOfCols());
        Assert.assertEquals(Arrays.asList("A", "B", "C"), result.getCategoricalColumn("GroupByCol").toList());
        Assert.assertEquals(Arrays.asList(sdf.parse("05/19"), sdf.parse("02/19"), sdf.parse("03/19")), result.getDateColumn("D1").toList());
    }

    @Test
    public void testGroupByNullGroupByCol() {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("GroupByCol", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));

        GroupBy step = new GroupBy();
        Config config = new Config();
        config.put(ConfigKey.TARGET_COLUMNS, Collections.singletonList("N1"));
        config.put(ConfigKey.NUMERICAL_AGGREGATION, NumericalAggregationType.SUM.name());
        config.put(ConfigKey.CATEGORICAL_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());
        config.put(ConfigKey.DATE_AGGREGATION, CategoricalAggregationType.FIRST_VALUE.name());

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(2, result.noOfCols());
        Assert.assertEquals(List.of(""), result.getCategoricalColumn("Index").toList());
        Assert.assertEquals(List.of(36d), result.getNumericColumn("N1").toList());
    }
}

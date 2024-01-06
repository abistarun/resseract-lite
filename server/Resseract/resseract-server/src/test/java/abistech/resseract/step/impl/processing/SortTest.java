package abistech.resseract.step.impl.processing;

import abistech.resseract.config.Boolean;
import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.step.elements.DatasetImpl;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class SortTest {

    @Test
    public void testAscendingSort() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("C1", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 3, 2, 5, 4, 7, 6, 8}));
        input.addNumericColumn(new DoubleColumn("N2", new double[]{10, 20, 30, 40, 50, 60, 70, 80}));

        Sort step = new Sort();
        Config config = new Config();
        config.put(ConfigKey.SORT_COLUMN, "N1");
        config.put(ConfigKey.SORT_ASCENDING, Boolean.TRUE.toString());

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(Arrays.asList("A", "C", "B", "A", "A", "B", "B", "C"), result.getCategoricalColumn("C1").toList());
        Assert.assertEquals(Arrays.asList(1d, 2d, 3d, 4d, 5d, 6d, 7d, 8d), result.getNumericColumn("N1").toList());
        Assert.assertEquals(Arrays.asList(10d, 30d, 20d, 50d, 40d, 70d, 60d, 80d), result.getNumericColumn("N2").toList());
    }

    @Test
    public void testDescendingSort() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("C1", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 3, 2, 5, 4, 7, 6, 8}));
        input.addNumericColumn(new DoubleColumn("N2", new double[]{10, 20, 30, 40, 50, 60, 70, 80}));

        Sort step = new Sort();
        Config config = new Config();
        config.put(ConfigKey.SORT_COLUMN, "N1");
        config.put(ConfigKey.SORT_ASCENDING, Boolean.FALSE.toString());

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(Arrays.asList("C", "B", "B", "A", "A", "B", "C", "A"), result.getCategoricalColumn("C1").toList());
        Assert.assertEquals(Arrays.asList(8d, 7d, 6d, 5d, 4d, 3d, 2d, 1d), result.getNumericColumn("N1").toList());
        Assert.assertEquals(Arrays.asList(80d, 60d, 70d, 40d, 50d, 20d, 30d, 10d), result.getNumericColumn("N2").toList());
    }
}

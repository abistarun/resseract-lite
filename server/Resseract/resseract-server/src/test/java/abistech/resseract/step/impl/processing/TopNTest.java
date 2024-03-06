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
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class TopNTest {

    @Test
    public void testTopN() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("C1", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 3, 2, 5, 4, 7, 6, 8}));
        input.addNumericColumn(new DoubleColumn("N2", new double[]{10, 20, 30, 40, 50, 60, 70, 80}));

        TopN step = new TopN();
        Config config = new Config();
        config.put(ConfigKey.TOP_N, 3d);

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(Arrays.asList("A", "B", "C"), result.getCategoricalColumn("C1").toList());
        Assert.assertEquals(Arrays.asList(1d, 3d, 2d), result.getNumericColumn("N1").toList());
        Assert.assertEquals(Arrays.asList(10d, 20d, 30d), result.getNumericColumn("N2").toList());
    }
}
package abistech.resseract.step.impl.processing;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.ColumnNames;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.step.Step;
import abistech.resseract.step.elements.DatasetImpl;
import org.junit.Assert;
import org.junit.Test;

public class EvaluateExpressionTest {

    @Test
    public void testExpressionEvaluator() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("C1", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));
        input.addNumericColumn(new DoubleColumn("N2", new double[]{10, 20, 30, 40, 50, 60, 70, 80}));
        input.addNumericColumn(new DoubleColumn("N3", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));

        Step step = new EvaluateExpression();
        Config config = new Config();
        config.put(ConfigKey.EXPRESSION, "([C1] == \"B\") && (([N2] + [N1]) < 75) ");

        Data result = step.execute(new DatasetImpl(input), config).getData();
        Assert.assertEquals(8, result.getBooleanColumn(ColumnNames.EXPRESSION_RESULT).size());
    }
}
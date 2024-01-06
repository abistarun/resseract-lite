package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.DataProperty;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.expression.ExpressionEvaluator;
import org.junit.Assert;
import org.junit.Test;

public class CumulativeSumTest {

    @Test
    public void testFormatDate() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        DoubleColumn doubleColumn = new DoubleColumn("N1", 5);
        doubleColumn.add(10d);
        doubleColumn.add(11d);
        doubleColumn.add(12d);
        doubleColumn.add(13d);
        doubleColumn.add(14d);


        DataProperty dataProperty = DataProperty.build(null, null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addNumericColumn(doubleColumn);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        DoubleColumn column = (DoubleColumn) expressionEvaluator.evaluateExpression("cumulativesum([N1])", "T1");
        DoubleColumn expectedColumn = new DoubleColumn("T1", new double[]{10d, 21d, 33d, 46d, 60d});
        Assert.assertEquals(expectedColumn, column);
    }


}
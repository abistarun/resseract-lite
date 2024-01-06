package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.DataProperty;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.DateColumn;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.expression.ExpressionEvaluator;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class ToStringTest {

    @Test
    public void testToString() throws ResseractException {
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
        StringColumn column = (StringColumn) expressionEvaluator.evaluateExpression("tostring([N1])", "T1");
        StringColumn expectedColumn = new StringColumn("T1", new String[]{"10", "11", "12", "13", "14"});
        Assert.assertEquals(expectedColumn, column);
    }

}
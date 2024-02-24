package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.DataProperty;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.DateColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.expression.ExpressionEvaluator;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormatDateTest {

    @Test
    public void testFormatDate() throws ResseractException, ParseException {
        DataKey dataKey = new DataKey("Test");
        DateColumn dateColumn = new DateColumn("INDEX", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        dateColumn.add(sdf.parse("01/19"));
        dateColumn.add(sdf.parse("02/19"));


        DataProperty dataProperty = DataProperty.build(dateColumn.getName(), null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addColumn(dateColumn);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        StringColumn column = (StringColumn) expressionEvaluator.evaluateExpression("formatdate([INDEX], \"MM/yy\")", "T1");
        StringColumn expectedColumn = new StringColumn("T1", new String[]{"01/19", "02/19"});
        Assert.assertEquals(expectedColumn, column);
    }

}
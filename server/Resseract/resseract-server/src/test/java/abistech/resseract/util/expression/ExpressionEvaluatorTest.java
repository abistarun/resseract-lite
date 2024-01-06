package abistech.resseract.util.expression;

import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.DataKey;
import abistech.resseract.data.frame.DataProperty;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.data.frame.impl.column.BooleanColumn;
import abistech.resseract.data.frame.impl.column.DateColumn;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.exception.ResseractException;
import org.junit.Assert;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ExpressionEvaluatorTest {

    @Test
    public void testNumericEvaluation() throws ParseException, ResseractException {
        DataKey dataKey = new DataKey("Test");
        DateColumn dateColumn = new DateColumn("INDEX", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        dateColumn.add(sdf.parse("01/19"));
        dateColumn.add(sdf.parse("02/19"));
        dateColumn.add(sdf.parse("03/19"));
        dateColumn.add(sdf.parse("04/19"));
        dateColumn.add(sdf.parse("05/19"));
        DoubleColumn a = new DoubleColumn("A1", new double[]{1, 2, 3, 4, 5});
        DoubleColumn b = new DoubleColumn("B1", new double[]{5, 4, 3, 2, 1});
        DoubleColumn c = new DoubleColumn("C1", new double[]{1, 2, 1, 2, 1});
        DoubleColumn d = new DoubleColumn("D1", new double[]{2, 1, 2, 1, 2});

        DataProperty dataProperty = DataProperty.build(dateColumn.getName(), null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addColumn(dateColumn);
        data.addNumericColumn(a);
        data.addNumericColumn(b);
        data.addNumericColumn(c);
        data.addNumericColumn(d);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        DoubleColumn column = (DoubleColumn) expressionEvaluator.evaluateExpression("([A1]+[B1] - ([C1]*[D1]))/2", "T1");
        DoubleColumn expectedColumn = new DoubleColumn("Result", new double[]{2, 2, 2, 2, 2});
        Assert.assertEquals(expectedColumn, column);
    }

    @Test
    public void testBooleanEvaluation() throws ParseException, ResseractException {
        DataKey dataKey = new DataKey("Test");
        DateColumn dateColumn = new DateColumn("INDEX", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        dateColumn.add(sdf.parse("01/19"));
        dateColumn.add(sdf.parse("02/19"));
        dateColumn.add(sdf.parse("03/19"));
        dateColumn.add(sdf.parse("04/19"));
        dateColumn.add(sdf.parse("05/19"));
        DoubleColumn a = new DoubleColumn("A", new double[]{1, 2, 3, 4, 5});
        DoubleColumn b = new DoubleColumn("B", new double[]{5, 2, 1, 4, 1});

        DataProperty dataProperty = DataProperty.build(dateColumn.getName(), null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addColumn(dateColumn);
        data.addNumericColumn(a);
        data.addNumericColumn(b);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        BooleanColumn column = (BooleanColumn) expressionEvaluator.evaluateExpression("([A]==[B]) && ([B]==[A])", "T1");
        BooleanColumn expectedColumn = new BooleanColumn("Result", new boolean[]{false, true, false, true, false});
        Assert.assertEquals(expectedColumn, column);
    }

    @Test
    public void testSumEvaluation() throws ParseException, ResseractException {
        DataKey dataKey = new DataKey("Test");
        DateColumn dateColumn = new DateColumn("INDEX", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        dateColumn.add(sdf.parse("01/19"));
        dateColumn.add(sdf.parse("02/19"));
        dateColumn.add(sdf.parse("03/19"));
        dateColumn.add(sdf.parse("04/19"));
        dateColumn.add(sdf.parse("05/19"));
        DoubleColumn a = new DoubleColumn("A1", new double[]{1, 2, 3, 4, 5});
        DoubleColumn b = new DoubleColumn("B1", new double[]{5, 4, 3, 2, 1});
        DoubleColumn c = new DoubleColumn("C1", new double[]{1, 2, 1, 2, 1});
        DoubleColumn d = new DoubleColumn("D1", new double[]{2, 1, 2, 1, 2});

        DataProperty dataProperty = DataProperty.build(dateColumn.getName(), null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addColumn(dateColumn);
        data.addNumericColumn(a);
        data.addNumericColumn(b);
        data.addNumericColumn(c);
        data.addNumericColumn(d);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        DoubleColumn column = (DoubleColumn) expressionEvaluator.evaluateExpression("sum([A1]) + sum([C1])", "T1");
        DoubleColumn expectedColumn = new DoubleColumn("Result", new double[]{22, 22, 22, 22, 22});
        Assert.assertEquals(expectedColumn, column);
    }

    @Test
    public void testIfEvaluation() throws ParseException, ResseractException {
        DataKey dataKey = new DataKey("Test");
        DateColumn dateColumn = new DateColumn("INDEX", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        dateColumn.add(sdf.parse("01/19"));
        dateColumn.add(sdf.parse("02/19"));
        dateColumn.add(sdf.parse("03/19"));
        dateColumn.add(sdf.parse("04/19"));
        dateColumn.add(sdf.parse("05/19"));
        DoubleColumn a = new DoubleColumn("A1", new double[]{1, 2, 3, 4, 5});
        DoubleColumn b = new DoubleColumn("B1", new double[]{1, 4, 3, 2, 5});

        DataProperty dataProperty = DataProperty.build(dateColumn.getName(), null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addColumn(dateColumn);
        data.addNumericColumn(a);
        data.addNumericColumn(b);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        DoubleColumn column = (DoubleColumn) expressionEvaluator.evaluateExpression("if([A1]==[B1],[A1] + [B1],0)", "T1");
        DoubleColumn expectedColumn = new DoubleColumn("Result", new double[]{2, 0, 6, 0, 10});
        Assert.assertEquals(expectedColumn, column);
    }

    @Test
    public void testNestedIfEvaluation() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("C1", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));
        input.addNumericColumn(new DoubleColumn("N2", new double[]{10, 20, 30, 40, 50, 60, 70, 80}));
        input.addNumericColumn(new DoubleColumn("N3", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(input);
        StringColumn column = (StringColumn) expressionEvaluator.evaluateExpression(
                "if([C1]!=\"A\", if([C1] == \"B\", \"It is B\", \"It is C\"), \"It is A\")", "T1");

        Assert.assertEquals(Arrays.asList("It is A", "It is B", "It is C", "It is A", "It is A", "It is B", "It is B", "It is C"),
                column.toList());
    }

    @Test
    public void testGroupedSumEvaluation() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addCategoricalColumn(new StringColumn("C1", new String[]{"A", "B", "C", "A", "A", "B", "B", "C"}));
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));
        input.addNumericColumn(new DoubleColumn("N2", new double[]{10, 20, 30, 40, 50, 60, 70, 80}));
        input.addNumericColumn(new DoubleColumn("N3", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(input);
        DoubleColumn column = (DoubleColumn) expressionEvaluator.evaluateExpression("groupedsum([N1], \"C1\")", "T1");

        Assert.assertEquals(Arrays.asList(10.0, 15.0, 11.0, 10.0, 10.0, 15.0, 15.0, 11.0), column.toList());
    }

    @Test
    public void testMaxEvaluationForNumeric() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        input.addNumericColumn(new DoubleColumn("N1", new double[]{1, 2, 3, 4, 5, 6, 7, 8}));

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(input);
        DoubleColumn column = (DoubleColumn) expressionEvaluator.evaluateExpression("max([N1])", "T1");

        Assert.assertEquals(Arrays.asList(8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0, 8.0), column.toList());
    }

    @Test
    public void testMaxEvaluationForDates() throws ResseractException, ParseException {
        DataKey dataKey = new DataKey("Test");
        Data input = new DataFrame(dataKey);
        DateColumn dateColumn = new DateColumn("D1");
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");
        dateColumn.add(sdf.parse("03/2020"));
        dateColumn.add(sdf.parse("04/2020"));
        input.addDateColumn(dateColumn);


        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(input);
        BooleanColumn column = (BooleanColumn) expressionEvaluator.evaluateExpression("max([D1]) == [D1]", "T1");

        Assert.assertEquals(Arrays.asList(false, true), column.toList());
    }

    @Test
    public void testStringEvaluation() throws ParseException, ResseractException {
        DataKey dataKey = new DataKey("Test");
        DateColumn dateColumn = new DateColumn("INDEX", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        dateColumn.add(sdf.parse("01/19"));
        dateColumn.add(sdf.parse("02/19"));
        dateColumn.add(sdf.parse("03/19"));
        dateColumn.add(sdf.parse("04/19"));
        dateColumn.add(sdf.parse("05/19"));
        dateColumn.add(sdf.parse("06/19"));
        dateColumn.add(sdf.parse("07/19"));
        dateColumn.add(sdf.parse("08/19"));
        dateColumn.add(sdf.parse("09/19"));

        StringColumn a = new StringColumn("A 1", new String[]{"A X", "B X", null, "A X", "A X", "B X ", "B X ", "C X", "B X"});

        DataProperty dataProperty = DataProperty.build(dateColumn.getName(), null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addColumn(dateColumn);
        data.addCategoricalColumn(a);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        BooleanColumn column = (BooleanColumn) expressionEvaluator.evaluateExpression("[A 1] == \"B X\"", "T1");
        BooleanColumn expectedColumn = new BooleanColumn("T1", new boolean[]{false, true, false, false, false, false, false, false, true});
        Assert.assertEquals(expectedColumn, column);
    }

    @Test
    public void testParseDateEvaluation() throws ParseException, ResseractException {
        DataKey dataKey = new DataKey("Test");
        DateColumn dateColumn = new DateColumn("INDEX", 5);
        SimpleDateFormat sdf = new SimpleDateFormat("MM/yy");
        dateColumn.add(sdf.parse("01/19"));
        dateColumn.add(sdf.parse("02/19"));
        dateColumn.add(sdf.parse("03/19"));
        dateColumn.add(sdf.parse("04/19"));
        dateColumn.add(sdf.parse("05/19"));
        dateColumn.add(sdf.parse("06/19"));
        dateColumn.add(sdf.parse("07/19"));
        dateColumn.add(sdf.parse("08/19"));
        dateColumn.add(sdf.parse("09/19"));


        DataProperty dataProperty = DataProperty.build(dateColumn.getName(), null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addColumn(dateColumn);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        BooleanColumn column = (BooleanColumn) expressionEvaluator.evaluateExpression("[INDEX] == parsedate(\"08/19\", \"MM/yy\")", "T1");
        BooleanColumn expectedColumn = new BooleanColumn("T1", new boolean[]{false, false, false, false, false, false, false, true, false});
        Assert.assertEquals(expectedColumn, column);
    }

    @Test
    public void testNowEvaluation() throws ResseractException {
        DataKey dataKey = new DataKey("Test");
        DateColumn dateColumn = new DateColumn("INDEX", 5);
        Date today = Calendar.getInstance().getTime();
        Calendar yestInstance = Calendar.getInstance();
        yestInstance.add(Calendar.DAY_OF_MONTH, -1);
        Date yesterday = yestInstance.getTime();
        dateColumn.add(yesterday);
        dateColumn.add(today);
        dateColumn.add(today);

        DataProperty dataProperty = DataProperty.build(dateColumn.getName(), null);
        Data data = new DataFrame(dataKey, dataProperty);
        data.addColumn(dateColumn);

        ExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);
        BooleanColumn column = (BooleanColumn) expressionEvaluator.evaluateExpression("formatdate([INDEX], \"dd/MM/yyyy\") == formatdate(now(), \"dd/MM/yyyy\")", "T1");
        BooleanColumn expectedColumn = new BooleanColumn("T1", new boolean[]{false, true, true});
        Assert.assertEquals(expectedColumn, column);
    }
}

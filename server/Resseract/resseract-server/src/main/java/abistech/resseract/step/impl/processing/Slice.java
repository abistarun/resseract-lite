package abistech.resseract.step.impl.processing;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.impl.column.BooleanColumn;
import abistech.resseract.data.frame.impl.column.DateColumn;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.step.elements.Dataset;
import abistech.resseract.step.elements.DatasetImpl;
import abistech.resseract.util.expression.ExpressionEvaluator;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.ColumnNames;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.DataFrame;
import abistech.resseract.step.impl.AbstractStep;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Slice extends AbstractStep {
    @Override
    public Dataset execute(Dataset input, Config config) throws ResseractException {
        Object sliceExpr = config.get(ConfigKey.SLICE_EXPRESSION);
        if (sliceExpr == null || ((String) sliceExpr).isEmpty())
            return input;
        Data data = input.getData();
        ExpressionEvaluator evaluator = new ExpressionEvaluator(data);
        BooleanColumn slicedIndex = (BooleanColumn) evaluator.evaluateExpression((String) sliceExpr, ColumnNames.SLICE_COLUMNS);
        Data result = new DataFrame(data.getDataKey(), data.getDataProperty());
        for (Column<?> column : data.getAllColumns()) {
            switch (column.getDataType()) {
                case DATE:
                    result.addDateColumn(new DateColumn(column.getName()));
                    break;
                case NUMERICAL:
                    result.addNumericColumn(new DoubleColumn(column.getName()));
                    break;
                case CATEGORICAL:
                    result.addCategoricalColumn(new StringColumn(column.getName()));
                    break;
                case BOOLEAN:
                    result.addBooleanColumn(new BooleanColumn(column.getName()));
                    break;
            }
        }

        int i = 0;
        for (Iterator<Row> iterator = data.iterator(); iterator.hasNext(); i++) {
            Row datum = iterator.next();
            if (slicedIndex.get(i)) {
                result.addRow(datum);
            }
        }

        return new DatasetImpl(result);
    }

    @Override
    public List<ConfigKey> getRequiredConfigs() {
        return Collections.singletonList(ConfigKey.SLICE_EXPRESSION);
    }
}

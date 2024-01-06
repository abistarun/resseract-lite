package abistech.resseract.step.impl.processing;

import abistech.resseract.config.Config;
import abistech.resseract.config.ConfigKey;
import abistech.resseract.data.frame.Data;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.step.elements.Dataset;
import abistech.resseract.util.Util;
import abistech.resseract.util.expression.ExpressionEvaluator;
import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.ColumnNames;
import abistech.resseract.step.impl.AbstractStep;

import java.util.Collections;
import java.util.List;

public class EvaluateExpression extends AbstractStep {

    @Override
    public Dataset execute(Dataset input, Config config) throws ResseractException {
        String expr = (String) config.get(ConfigKey.EXPRESSION);
        if (!Util.isValidString(expr))
            return input;

        Data data = input.getData();
        ExpressionEvaluator evaluator = new ExpressionEvaluator(data);
        Column<?> column = evaluator.evaluateExpression(expr, ColumnNames.EXPRESSION_RESULT);
        for (String columnName : data.getColumnNames()) {
            if (!columnName.equals(data.getIndexName()))
                data.removeColumn(columnName);
        }
        data.setPrimaryColumn(column);
        return input;
    }

    @Override
    public List<ConfigKey> getRequiredConfigs() {
        return Collections.singletonList(ConfigKey.EXPRESSION);
    }
}

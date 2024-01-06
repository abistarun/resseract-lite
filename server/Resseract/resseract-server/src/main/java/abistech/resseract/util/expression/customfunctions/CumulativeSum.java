package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.util.expression.CustomFunction;
import abistech.resseract.util.expression.Expression;

import java.util.List;

public class CumulativeSum implements CustomFunction {

    public static final String NAME = "cumulativesum";
    private final Expression expression;
    private double sum;

    public CumulativeSum(List<Expression> arguments) {
        expression = arguments.get(0);
        sum = 0;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void cache(Row row) {
    }

    @Override
    public boolean isCacheRequired() {
        return false;
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        sum += (Double) expression.eval(row);
        return sum;
    }

    @Override
    public DataType getDataType() {
        return DataType.NUMERICAL;
    }
}

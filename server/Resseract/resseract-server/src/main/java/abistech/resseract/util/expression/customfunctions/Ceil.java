package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.util.expression.CustomFunction;
import abistech.resseract.util.expression.Expression;

import java.util.List;

public class Ceil implements CustomFunction {

    public static final String NAME = "ceil";

    private final List<Expression> expressions;

    public Ceil(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return Math.ceil((Double) expressions.get(0).eval(row));
    }

    @Override
    public DataType getDataType() {
        return DataType.NUMERICAL;
    }

    @Override
    public void cache(Row row) {

    }

    @Override
    public boolean isCacheRequired() {
        for (Expression expression : expressions) {
            if (expression.isCacheRequired())
                return true;
        }
        return false;
    }
}

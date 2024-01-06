package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.util.expression.CustomFunction;
import abistech.resseract.util.expression.Expression;

import java.util.List;

public class If implements CustomFunction {
    public static final String NAME = "if";
    private final List<Expression> expressions;

    public If(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return (boolean) expressions.get(0).eval(row) ? expressions.get(1).eval(row) : expressions.get(2).eval(row);
    }

    @Override
    public DataType getDataType() {
        return expressions.get(1).getDataType();
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

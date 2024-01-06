package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.util.expression.CustomFunction;
import abistech.resseract.util.expression.Expression;

import java.util.List;

public class ToString implements CustomFunction {

    public static final String NAME = "tostring";

    private final List<Expression> expressions;

    public ToString(List<Expression> expressions) {
        this.expressions = expressions;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        Object data = expressions.get(0).eval(row);
        if ((data instanceof Double) && (((double) data % 1.0) == 0))
            return String.format("%.0f", data);
        else
            return String.format("%s", data);
    }

    @Override
    public DataType getDataType() {
        return DataType.CATEGORICAL;
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

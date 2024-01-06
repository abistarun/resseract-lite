package abistech.resseract.util.expression;

import abistech.resseract.util.Util;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;

class Constant implements Expression {

    private final DataType dataType;
    private final Object constant;

    Constant(String expression) {
        if (expression.startsWith("\"") && expression.endsWith("\"")) {
            dataType = DataType.CATEGORICAL;
            constant = expression.substring(expression.indexOf("\"") + 1, expression.lastIndexOf("\""));
        } else if (expression.equalsIgnoreCase("true") || expression.equalsIgnoreCase("false")) {
            dataType = DataType.BOOLEAN;
            constant = Util.parseBoolean(expression);
        } else {
            dataType = DataType.NUMERICAL;
            constant = Double.parseDouble(expression);
        }
    }

    @Override
    public Object eval(Row row) {
        return constant;
    }

    @Override
    public DataType getDataType() {
        return dataType;
    }

    @Override
    public boolean isCacheRequired() {
        return false;
    }

    @Override
    public void cache(Row row) {

    }
}

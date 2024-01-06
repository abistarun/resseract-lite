package abistech.resseract.util.expression;

import abistech.resseract.util.Constants;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;

public class Variable implements Expression {

    private final String variableName;
    private final DataType dataType;

    Variable(String variableName, DataType dataType) {
        this.variableName = variableName.substring(1, variableName.length() - 1);
        this.dataType = dataType;
    }

    @Override
    public Object eval(Row row) {
        Object value = row.getValue(variableName);
        if (value == null)
            if (dataType == DataType.NUMERICAL)
                return 0d;
            else if (dataType == DataType.CATEGORICAL)
                return Constants.NULL;

        return value;
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

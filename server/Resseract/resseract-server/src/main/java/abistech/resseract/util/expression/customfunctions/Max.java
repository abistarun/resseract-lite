package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.util.expression.CustomFunction;
import abistech.resseract.util.expression.Expression;

import java.util.Date;
import java.util.List;

public class Max implements CustomFunction {
    public static final String NAME = "max";
    private final Expression expression;
    private final MaxComparator maxComparator;

    public Max(List<Expression> arguments) throws ResseractException {
        expression = arguments.get(0);
        DataType dataType = expression.getDataType();
        maxComparator = MaxComparator.getComparator(dataType);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public void cache(Row row) throws ResseractException {
        maxComparator.compare(expression.eval(row));
    }

    @Override
    public boolean isCacheRequired() {
        return true;
    }

    @Override
    public Object eval(Row row) {
        return maxComparator.getMaxValue();
    }

    @Override
    public DataType getDataType() {
        return expression.getDataType();
    }
}

interface MaxComparator {

    static MaxComparator getComparator(DataType dataType) throws ResseractException {
        switch (dataType) {
            case DATE:
                return new DateMaxComparator();
            case NUMERICAL:
                return new NumericMaxComparator();
            case CATEGORICAL:
            case BOOLEAN:
                throw new ResseractException(CustomErrorReports.INVALID_DATA_TYPE_FOR_MAX_FUNCTION);
        }
        return null;
    }

    void compare(Object value);

    Object getMaxValue();
}

class NumericMaxComparator implements MaxComparator {

    private double max;

    NumericMaxComparator() {
        max = Double.MIN_VALUE;
    }

    @Override
    public void compare(Object value) {
        if ((Double) value > max)
            max = (double) value;
    }

    @Override
    public Object getMaxValue() {
        return max;
    }
}

class DateMaxComparator implements MaxComparator {

    private Date max;

    DateMaxComparator() {
        max = new Date(Long.MIN_VALUE);
    }

    @Override
    public void compare(Object value) {
        if (max.compareTo((Date) value) < 0)
            max = (Date) value;
    }

    @Override
    public Object getMaxValue() {
        return max;
    }
}

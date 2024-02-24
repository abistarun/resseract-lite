package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.util.expression.CustomFunction;
import abistech.resseract.util.expression.Expression;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FormatDate implements CustomFunction {
    public static final String NAME = "formatdate";
    private final Expression expression;
    private final SimpleDateFormat sdf;

    public FormatDate(List<Expression> arguments) throws ResseractException {
        expression = arguments.get(0);
        String format = (String) arguments.get(1).eval(null);
        sdf = new SimpleDateFormat(format, Locale.ENGLISH);
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
        Date date = (Date) expression.eval(row);
        return date == null ? null : sdf.format(date);
    }

    @Override
    public DataType getDataType() {
        return DataType.CATEGORICAL;
    }
}

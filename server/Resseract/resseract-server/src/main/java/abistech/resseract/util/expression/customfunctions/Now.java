package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.util.expression.CustomFunction;

import java.util.Calendar;
import java.util.Date;

public class Now implements CustomFunction {

    public static final String NAME = "now";
    private final Date now;

    public Now() {
        now = Calendar.getInstance().getTime();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return now;
    }

    @Override
    public DataType getDataType() {
        return DataType.DATE;
    }

    @Override
    public void cache(Row row) throws ResseractException {

    }

    @Override
    public boolean isCacheRequired() {
        return false;
    }
}

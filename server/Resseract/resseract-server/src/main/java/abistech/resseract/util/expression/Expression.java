package abistech.resseract.util.expression;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;

public interface Expression {

    Object eval(Row row) throws ResseractException;

    DataType getDataType();

    void cache(Row row) throws ResseractException;

    boolean isCacheRequired();
}

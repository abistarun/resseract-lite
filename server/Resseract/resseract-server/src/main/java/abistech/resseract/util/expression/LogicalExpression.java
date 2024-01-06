package abistech.resseract.util.expression;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;

abstract class LogicalExpression implements Expression {

    final Expression e1;
    final Expression e2;

    LogicalExpression(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public DataType getDataType() {
        return DataType.BOOLEAN;
    }

    @Override
    public boolean isCacheRequired() {
        return e1.isCacheRequired() || e2.isCacheRequired();
    }

    @Override
    public void cache(Row row) throws ResseractException {
        e1.cache(row);
        e2.cache(row);
    }
}

class And extends LogicalExpression {

    And(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return (Boolean) e1.eval(row) && (Boolean) e2.eval(row);
    }
}


class Or extends LogicalExpression {

    Or(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return (Boolean) e1.eval(row) || (Boolean) e2.eval(row);
    }
}

class Equal extends LogicalExpression {

    Equal(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return e1.eval(row).equals(e2.eval(row));
    }
}


class NotEqual extends LogicalExpression {

    NotEqual(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return !e1.eval(row).equals(e2.eval(row));
    }
}


class GreaterThan extends LogicalExpression {

    GreaterThan(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return ((Double) e1.eval(row)) > ((Double) e2.eval(row));
    }
}

class LessThan extends LogicalExpression {

    LessThan(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return ((Double) e1.eval(row)) < ((Double) e2.eval(row));
    }
}

class GreaterThanEqual extends LogicalExpression {

    GreaterThanEqual(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return ((Double) e1.eval(row)) >= ((Double) e2.eval(row));
    }
}

class LessThanEqual extends LogicalExpression {

    LessThanEqual(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return ((Double) e1.eval(row)) <= ((Double) e2.eval(row));
    }
}
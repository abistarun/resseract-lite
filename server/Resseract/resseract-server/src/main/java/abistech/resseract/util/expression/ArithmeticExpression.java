package abistech.resseract.util.expression;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;

abstract class ArithmeticExpression implements Expression {

    final Expression e1;
    final Expression e2;

    ArithmeticExpression(Expression e1, Expression e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    @Override
    public DataType getDataType() {
        return DataType.NUMERICAL;
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

class Addition extends ArithmeticExpression {

    Addition(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return (double) e1.eval(row) + (double) e2.eval(row);
    }
}

class Subtraction extends ArithmeticExpression {

    Subtraction(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return (double) e1.eval(row) - (double) e2.eval(row);
    }
}


class Multiplication extends ArithmeticExpression {

    Multiplication(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return (double) e1.eval(row) * (double) e2.eval(row);
    }
}


class Division extends ArithmeticExpression {

    Division(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return (double) e1.eval(row) / (double) e2.eval(row);
    }
}

class Mod extends ArithmeticExpression {

    Mod(Expression e1, Expression e2) {
        super(e1, e2);
    }

    @Override
    public Object eval(Row row) throws ResseractException {
        return (double) e1.eval(row) % (double) e2.eval(row);
    }
}

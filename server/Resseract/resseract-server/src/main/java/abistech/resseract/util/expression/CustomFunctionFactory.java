package abistech.resseract.util.expression;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.expression.customfunctions.*;

import java.util.Arrays;
import java.util.List;

class CustomFunctionFactory {

    static CustomFunction getCustomFunction(String name, List<Expression> arguments) throws ResseractException {
        if (name.equals(Sum.NAME))
            return new Sum(arguments);
        if (name.equals(CumulativeSum.NAME))
            return new CumulativeSum(arguments);
        if (name.equals(If.NAME))
            return new If(arguments);
        if (name.equals(Ceil.NAME))
            return new Ceil(arguments);
        if (name.equals(GroupedSum.NAME))
            return new GroupedSum(arguments);
        if (name.equals(Max.NAME))
            return new Max(arguments);
        if (name.equals(ParseDate.NAME))
            return new ParseDate(arguments);
        if (name.equals(FormatDate.NAME))
            return new FormatDate(arguments);
        if (name.equals(Now.NAME))
            return new Now();
        if (name.equals(ToString.NAME))
            return new ToString(arguments);
        return null;
    }

    static List<String> getAllNames() {
        return Arrays.asList(
                Sum.NAME,
                CumulativeSum.NAME,
                If.NAME,
                Ceil.NAME,
                GroupedSum.NAME,
                Max.NAME,
                ParseDate.NAME,
                FormatDate.NAME,
                Now.NAME,
                ToString.NAME
        );
    }
}

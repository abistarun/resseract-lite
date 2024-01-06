package abistech.resseract.util.expression.customfunctions;

import abistech.resseract.exception.ResseractException;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.util.expression.CustomFunction;
import abistech.resseract.util.expression.Expression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupedSum implements CustomFunction {
    public static final String NAME = "groupedsum";
    private final Expression expression;
    private final List<String> groupBys;
    private final Map<List<Object>, Double> groupwiseSum;

    public GroupedSum(List<Expression> arguments) throws ResseractException {
        expression = arguments.get(0);
        groupBys = new ArrayList<>(arguments.size() - 1);
        for (int i = 1; i < arguments.size(); i++) {
            Expression argument = arguments.get(i);
            String colName = (String) argument.eval(null);
            groupBys.add(colName);
        }
        groupwiseSum = new HashMap<>();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Object eval(Row row) {
        List<Object> groupValues = new ArrayList<>(groupBys.size());
        for (String groupByColumn : groupBys) {
            groupValues.add(row.getValue(groupByColumn));
        }
        return groupwiseSum.get(groupValues);
    }

    @Override
    public DataType getDataType() {
        return DataType.NUMERICAL;
    }

    @Override
    public void cache(Row row) throws ResseractException {
        List<Object> groupValues = new ArrayList<>(groupBys.size());
        for (String groupByColumn : groupBys) {
            groupValues.add(row.getValue(groupByColumn));
        }
        if (!groupwiseSum.containsKey(groupValues))
            groupwiseSum.put(groupValues, 0d);
        groupwiseSum.put(groupValues, groupwiseSum.get(groupValues) + (Double) expression.eval(row));
    }

    @Override
    public boolean isCacheRequired() {
        return true;
    }
}

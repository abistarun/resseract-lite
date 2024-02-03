package abistech.resseract.util.expression;

import abistech.resseract.data.frame.Column;
import abistech.resseract.data.frame.Data;
import abistech.resseract.data.frame.Row;
import abistech.resseract.data.frame.impl.column.BooleanColumn;
import abistech.resseract.data.frame.impl.column.DataType;
import abistech.resseract.data.frame.impl.column.DoubleColumn;
import abistech.resseract.data.frame.impl.column.StringColumn;
import abistech.resseract.exception.CustomErrorReports;
import abistech.resseract.exception.ResseractException;
import abistech.resseract.util.Constants;

import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEvaluator {

    private static final Pattern variablePatterns = Pattern.compile("\\[[^]]*]");
    private static final Pattern stringLiteralsPattern = Pattern.compile("\"[^\"]*\"");

    private final Data data;

    public ExpressionEvaluator(Data data) {
        this.data = data;
    }

    public Column<?> evaluateExpression(String expression, String columnName) throws ResseractException {
        try {
            Expression expr = parse(expression);
            if (expr.isCacheRequired())
                for (Row row : data) {
                    expr.cache(row);
                }
            Column<?> result = buildColumn(expr.getDataType(), columnName, expression);
            for (Row row : data) {
                Object resultValue = expr.eval(row);
                fillColumn(expr.getDataType(), result, resultValue);
            }
            return result;
        } catch (ResseractException e) {
            throw e;
        } catch (Exception e) {
            throw new ResseractException(CustomErrorReports.EXPRESSION_EVALUATION_ERROR, e);
        }
    }

    private Expression parse(String expression) throws IOException, ResseractException {
        List<String> tokens = extractTokens(expression);
        return parse(tokens);
    }

    private Expression parse(List<String> tokens) throws IOException, ResseractException {
        Expression expr = null;
        AtomicInteger i = new AtomicInteger(0);
        while (i.get() < tokens.size())
            expr = parse(tokens, expr, i);
        return expr;
    }

    private Expression parse(List<String> tokens, Expression expr, AtomicInteger i) throws IOException, ResseractException {
        String token = tokens.get(i.getAndIncrement());

        if (token.equals("(")) {
            int start = i.get();
            searchClosingBracket(tokens, i);
            return parse(tokens.subList(start, i.get()));
        }
        if (token.equals(")"))
            return expr;

        if (token.equals("=="))
            return new Equal(expr, parse(tokens, expr, i));
        if (token.equals("!="))
            return new NotEqual(expr, parse(tokens, expr, i));
        if (token.equals(">="))
            return new GreaterThanEqual(expr, parse(tokens, expr, i));
        if (token.equals("<="))
            return new LessThanEqual(expr, parse(tokens, expr, i));
        if (token.equals(">"))
            return new GreaterThan(expr, parse(tokens, expr, i));
        if (token.equals("<"))
            return new LessThan(expr, parse(tokens, expr, i));

        if (token.equals("&&"))
            return new And(expr, parse(tokens, expr, i));
        if (token.equals("||"))
            return new Or(expr, parse(tokens, expr, i));

        if (token.equals("-"))
            return new Subtraction(expr, parse(tokens, expr, i));
        if (token.equals("+"))
            return new Addition(expr, parse(tokens, expr, i));
        if (token.equals("*"))
            return new Multiplication(expr, parse(tokens, expr, i));
        if (token.equals("/"))
            return new Division(expr, parse(tokens, expr, i));
        if (token.equals("%"))
            return new Mod(expr, parse(tokens, expr, i));

        if (CustomFunctionFactory.getAllNames().contains(token)) {
            return handleCustomFunctions(tokens, i, token);
        }

        if (token.length() > 1 && data.getColumnNames().stream().map(e -> "[" + e + "]").toList().contains(token))
            return new Variable(token, data.getColumn(token.substring(1, token.length() - 1)).getDataType());
        return new Constant(token);
    }

    private Expression handleCustomFunctions(List<String> tokens, AtomicInteger i, String token) throws IOException, ResseractException {
        int start = i.incrementAndGet();
        searchClosingBracket(tokens, i);
        List<Expression> arguments = new ArrayList<>();
        List<String> tokensForCustomFunc = new ArrayList<>();
        List<String> subList = tokens.subList(start, i.get());
        for (int i1 = 0; i1 < subList.size(); i1++) {
            String tok = subList.get(i1);
            if (tok.equals("(")) {
                AtomicInteger closingBracketIndex = new AtomicInteger(i1 + 1);
                searchClosingBracket(subList, closingBracketIndex);
                for (int i2 = i1; i2 < closingBracketIndex.get(); i2++) {
                    tokensForCustomFunc.add(subList.get(i2));
                    i1++;
                }
                i1--;
            } else if (tok.equals(",")) {
                Expression expression = parse(tokensForCustomFunc);
                arguments.add(expression);
                tokensForCustomFunc = new ArrayList<>();
            } else {
                tokensForCustomFunc.add(tok);
            }
        }
        Expression expression = parse(tokensForCustomFunc);
        arguments.add(expression);
        return CustomFunctionFactory.getCustomFunction(token, arguments);
    }

    private void searchClosingBracket(List<String> tokens, AtomicInteger i) {
        int bracketCnt = 1;
        while (bracketCnt != 0) {
            String t = tokens.get(i.getAndIncrement());
            if (t.equals("("))
                bracketCnt++;
            if (t.equals(")"))
                bracketCnt--;
        }
    }

    private List<String> extractTokens(String expression) throws IOException {
        Map<String, String> tokens = new HashMap<>();
        List<String> doubleOps = Arrays.asList("==", "!=", ">=", "<=", "&&", "||");
        expression = fillDoubleOps(expression, tokens, doubleOps);
        expression = fillLiterals(expression, variablePatterns, tokens);
        expression = fillLiterals(expression, stringLiteralsPattern, tokens);

        StreamTokenizer tokenizer = new StreamTokenizer(new StringReader(expression));
        tokenizer.ordinaryChar('-');
        tokenizer.ordinaryChar('/');
        tokenizer.ordinaryChar('"');
        List<String> tokBuf = new ArrayList<>();
        while (tokenizer.nextToken() != StreamTokenizer.TT_EOF) {
            switch (tokenizer.ttype) {
                case StreamTokenizer.TT_NUMBER -> tokBuf.add(String.valueOf(tokenizer.nval));
                case StreamTokenizer.TT_WORD -> tokBuf.add(tokenizer.sval);
                default -> tokBuf.add(String.valueOf((char) tokenizer.ttype));
            }
        }
        for (int i = 0; i < tokBuf.size(); i++) {
            String tok = tokBuf.get(i);
            if (tokens.containsKey(tok))
                tokBuf.set(i, tokens.get(tok));
        }
        return tokBuf;
    }

    private String fillDoubleOps(String expression, Map<String, String> tokens, List<String> doubleOps) {
        for (String op : doubleOps) {
            if (expression.contains(op))
                expression = replaceExprAndFillTokens(expression, tokens, op);
        }
        return expression;
    }

    private String replaceExprAndFillTokens(String expression, Map<String, String> tokens, String op) {
        String uuid = generateRandom();
        tokens.put(uuid.trim(), op);
        return expression.replace(op, uuid);
    }

    private static String generateRandom() {
        String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rand = new Random();
        StringBuilder res = new StringBuilder();
        res.append(" ");
        for (int i = 0; i < 15; i++) {
            int randIndex = rand.nextInt(aToZ.length());
            res.append(aToZ.charAt(randIndex));
        }
        res.append(" ");
        return res.toString();
    }

    private String fillLiterals(String expression, Pattern variablePatterns, Map<String, String> tokens) {
        Matcher matcher = variablePatterns.matcher(expression);
        while (matcher.find()) {
            String match = matcher.group();
            expression = replaceExprAndFillTokens(expression, tokens, match);
        }
        return expression;
    }

    private void fillColumn(DataType returnDataType, Column<?> result, Object res) {
        switch (returnDataType) {
            case BOOLEAN -> ((BooleanColumn) result).add((Boolean) res);
            case NUMERICAL -> ((DoubleColumn) result).add((Double) res);
            case CATEGORICAL -> ((StringColumn) result).add((String) res);
        }
    }

    private Column<?> buildColumn(DataType returnDataType, String columnName, String expression) throws ResseractException {
        Column<?> result = switch (returnDataType) {
            case BOOLEAN -> new BooleanColumn(columnName, data.noOfRows());
            case NUMERICAL -> new DoubleColumn(columnName, data.noOfRows());
            case CATEGORICAL -> new StringColumn(columnName, data.noOfRows());
            case DATE -> throw new ResseractException(CustomErrorReports.EXPR_DATA_TYPE_NOT_SUPPORTED);
        };
        result.addProperty(Constants.EXPRESSION, expression);
        return result;
    }
}
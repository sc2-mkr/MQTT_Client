package regexExpressions;

import regexExpressions.expressions.Expression;
import regexExpressions.expressions.RandomExpression;
import services.utils.regex.RegexUtil;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Check if the payload of a message contains a function like:
 * {RANDOM(x,y)} or {COUNTER}
 * end replace the function statement with the relative value.
 *
 * The syntax of a function must be:
 * { + <Expression name> + (Optional: (<param>,<param>, + }
 */
public class ExpressionsAnalyzer {

    // ArrayList with interpreted interpretedExpressions
    private ArrayList<String> interpretedExpressions = new ArrayList<>();

    private ArrayList<Expression> regexExpression = new ArrayList<>();

    private String result = "";

    public ExpressionsAnalyzer() {
        addRegexExpression();
    }

    // Add regex expression here
    private void addRegexExpression() {
        regexExpression.add(new RandomExpression());
    }

    public String compute(String expression) {
        for (String exp : splitExpression(expression)) {
            findMatch(exp);
        }
        return interpretedExpressions.stream().collect(Collectors.joining(" "));
    }

    private void findMatch(String exp) {
        for (Expression e : regexExpression) {
            if(RegexUtil.getInstance().match(e.getPattern(), exp)) {
                interpretedExpressions.add(e.interpret(exp));
                return;
            }
        }
        interpretedExpressions.add(exp);
    }

    private String[] splitExpression(String expression) {
        return expression.split(" ");
    }

    public String getResult() {
        return result;
    }
}

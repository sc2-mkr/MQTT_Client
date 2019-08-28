package regexExpressions;

import regexExpressions.expressions.Expression;
import regexExpressions.expressions.RandomExpression;
import services.utils.regex.RegexUtil;

import java.util.ArrayList;

/**
 * Check if the payload of a message contains a function like:
 * {RANDOM(x,y)} or {COUNTER}
 * end replace the function statement with the relative value.
 *
 * The syntax of a function must be:
 * { + <Expression name> + (Optional: <param>,<param>,etc.) + }
 */
public class ExpressionsAnalyzer {

    private ArrayList<Expression> regexExpressions = new ArrayList<>();

    private String result = "";

    public ExpressionsAnalyzer() {
        addRegexExpressions();
    }

    // Add regex expression here
    private void addRegexExpressions() {
        regexExpressions.add(new RandomExpression());
    }

    public String compute(String expression) {
        return findMatch(expression);
    }

    private String findMatch(String exp) {
        for (Expression e : regexExpressions) {
            if(RegexUtil.getInstance().match(e.getPattern(), exp)) {
                exp = e.interpret(exp);
                // return removed because may there are multiple expression
            }
        }
        return exp;
    }
}

package regexExpressions.expressions;

import java.util.Random;
import java.util.regex.*;

/**
 * Expression for random number generation in message payload.
 * Syntax:
 * {RANDOM(<min>,<max>)}
 */
public class RandomExpression implements Expression {

    private final String pattern = "\\{RANDOM\\(([^,]+),([^)]+)\\)\\}";

    @Override
    public String interpret(String exp) {
        Pattern p = Pattern.compile(pattern);
        while (true) {
            Matcher m = p.matcher(exp);
            if(!m.find()) break;
            int min = Integer.parseInt(m.group(1));
            int max = Integer.parseInt(m.group(2));
            String randomNumber = Integer.toString(new Random().nextInt(((max - min) + 1) + min));
            exp = m.replaceFirst(randomNumber);
        }
        return exp;
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}

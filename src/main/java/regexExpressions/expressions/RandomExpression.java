package regexExpressions.expressions;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RandomExpression implements Expression {

    private final String pattern = "RANDOM\\(([^,]+),([^)]+)\\)";

    @Override
    public String interpret(String exp) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(exp);
        if (m.find()) {
            int min = Integer.parseInt(m.group(1));
            int max = Integer.parseInt(m.group(2));
            return Integer.toString(new Random().nextInt(
                    ((max - min) + 1) + min));
        }
        return "RandomNAN";
    }

    @Override
    public String getPattern() {
        return pattern;
    }
}

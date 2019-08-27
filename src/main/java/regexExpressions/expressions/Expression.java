package regexExpressions.expressions;

public interface Expression {
    String interpret(String exp);
    String getPattern();
}

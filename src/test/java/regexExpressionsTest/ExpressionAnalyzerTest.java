package regexExpressionsTest;

import org.junit.Test;
import static org.junit.Assert.*;
import regexExpressions.ExpressionsAnalyzer;

public class ExpressionAnalyzerTest {

    @Test
    public void computeTest() {
        ExpressionsAnalyzer ea = new ExpressionsAnalyzer();
        assertEquals(ea.compute("hello world"), "hello world");
    }
}

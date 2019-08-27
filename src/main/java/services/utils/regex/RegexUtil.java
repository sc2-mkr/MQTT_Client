package services.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    private static RegexUtil instance = new RegexUtil();

    public static RegexUtil getInstance() {
        return instance;
    }

    private RegexUtil() {
    }

    public boolean match(String pattern, String value) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(value);

        /**
         * p.matcher(value).matches() method doesn't work in this case because it check the pattern
         * on all over the value.
         * Example:
         * pattern: *pattern for random expression*
         * value: {RANDOM(1,50)} -> return: true
         * value: n.{RANDOM(1,50)} -> return: false
         */
        if(m.find()) return true;
        else return false;
    }
}

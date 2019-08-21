package services.utils.regex;

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
        return p.matcher(value).matches();
    }
}

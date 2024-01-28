import java.util.regex.*;

/**
 * Provides utility methods for sanitizing HTML content in order to prevent
 * potential cross-site scripting (XSS) attacks.
 */
public class HTMLSanitizer {

    private static final Pattern SCRIPT_PATTERNS = Pattern.compile(
            "<script.*?>.*?</script>" +                   // <script> tags
                    "|src[\r\n]*=[\r\n]*['\"].*?['\"]" +          // src attributes
                    "|</script>" +                                // closing script tag
                    "|<script(.*?)" +                             // opening script tag
                    "|eval\\((.*?)\\)" +                          // eval
                    "|expression\\((.*?)\\)" +                    // expression
                    "|javascript:" +                              // javascript:
                    "|vbscript:" +                                // vbscript:
                    "|onload(.*?)=",                              // onload=
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL
    );

    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<.*?>");

    /**
     * Sanitizes the input string by removing or replacing potentially malicious HTML content.
     *
     * @param input The input string to sanitize.
     * @return The sanitized string.
     */
    public static String sanitize(String input) {
        if (input == null) {
            return null;
        }

        input = removeMatches(input);
        input = replaceMatches(input);

        return input;
    }

    /**
     * Removes all matches of potentially malicious patterns from the input.
     *
     * @param input The input string.
     * @return The string with removed malicious patterns.
     */
    private static String removeMatches(String input) {
        return HTMLSanitizer.SCRIPT_PATTERNS.matcher(input).replaceAll("");
    }

    /**
     * Replaces all HTML tags in the input string with the word "censur".
     *
     * @param input The input string.
     * @return The string with HTML tags replaced.
     */
    private static String replaceMatches(String input) {
        return HTMLSanitizer.HTML_TAG_PATTERN.matcher(input).replaceAll("censur");
    }
}

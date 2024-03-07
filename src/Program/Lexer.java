package Program;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public static String[] splitByComma(String code) {
        List<String> parts = new ArrayList<>();
        boolean inSingleQuotes = false;
        boolean inDoubleQuotes = false;
        StringBuilder part = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
            // Handle single quotes
            if (c == '\\' && i < code.length() - 1 && code.charAt(i + 1) == '\'') {
                part.append('\\');
                i++; // Skip the next character
            } else if (c == '\'') {
                inSingleQuotes = !inSingleQuotes;
                part.append(c);
            } else if (!inSingleQuotes && c == '\"') {
                inDoubleQuotes = !inDoubleQuotes;
                part.append(c);
            } else if (!inSingleQuotes && !inDoubleQuotes && c == '(') {
                // Entering a parenthesis, remember the position
            } else if (!inSingleQuotes && !inDoubleQuotes && c == ')') {
                // Exiting a parenthesis, remember the position
            } else {
                part.append(c);
            }
            // Check if we are at a comma not inside quotes or parentheses
            if (!inSingleQuotes && !inDoubleQuotes && i < code.length() - 1 && c == ',' && code.charAt(i + 1) != '(') {
                parts.add(part.toString());
                part = new StringBuilder();
                i++; // Skip the comma
            }
        }
        parts.add(part.toString());
        return parts.toArray(new String[0]);
    }
}
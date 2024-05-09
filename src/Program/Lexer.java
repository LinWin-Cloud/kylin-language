package Program;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    public static String[] splitByComma(String code) {
        List<String> parts = new ArrayList<>();
        boolean inQuotes = false;
        StringBuilder part = new StringBuilder();

        for (char c : code.toCharArray()) {
            if (c == '\'') {
                inQuotes = !inQuotes;
            }
            if (!inQuotes && c == ',') {
                parts.add(part.toString());
                part = new StringBuilder();
            } else {
                part.append(c);
            }
        }
        parts.add(part.toString());
        return parts.toArray(new String[0]);
    }
}
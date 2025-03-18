package ru.nsu.fit.data;

public class Util {
    public static String processEscapingCharsFromInput(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 1; i + 1 < s.length(); ++i) {
            if (s.charAt(i) == '\\') {
                if (s.charAt(i + 1) == '\"') {
                    result.append('\"');
                    ++i;
                } else if (s.charAt(i + 1) == '\\') {
                    result.append('\\');
                    ++i;
                } else if (s.charAt(i + 1) == '/') {
                    result.append('/');
                    ++i;
                } else if (s.charAt(i + 1) == 'b') {
                    result.append('\b');
                    ++i;
                } else if (s.charAt(i + 1) == 'f') {
                    result.append('\f');
                    ++i;
                } else if (s.charAt(i + 1) == 'n') {
                    result.append('\n');
                    ++i;
                } else if (s.charAt(i + 1) == 'r') {
                    result.append('\r');
                    ++i;
                } else if (s.charAt(i + 1) == 't') {
                    result.append('\t');
                } else if (s.charAt(i + 1) == 'u') {
                    char[] c = new char[4];
                    s.getChars(i + 2, i + 2 + 4, c, 0);
                    int id = Integer.parseInt(String.valueOf(c), 16);
                    result.append((char) id);
                    i += 5;
                }
            } else result.append(s.charAt(i));
        }
        return result.toString();
    }

    public static String processEscapingCharsToInput(String s) {
        StringBuilder result = new StringBuilder();
        result.append("\"");
        for (char c : s.toCharArray()) {
            if (c == '\\') {
                result.append("\\\\");
            } else if (c == '/') {
                result.append("\\/");
            } else if (c == '\"') {
                result.append("\\\"");
            } else if (c == '\b') {
                result.append("\\b");
            } else if (c == '\f') {
                result.append("\\f");
            } else if (c == '\r') {
                result.append("\\r");
            } else if (c == '\n') {
                result.append("\\n");
            } else if (c == '\t') {
                result.append("\\t");
            } else if ((int) c < 128) {
                result.append(c);
            } else {
                result.append("\\u").append(Integer.toHexString(c));
            }
        }
        result.append("\"");
        return result.toString();
    }
}

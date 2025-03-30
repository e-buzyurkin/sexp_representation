package ru.nsu.fit.data;

import ru.nsu.fit.data.node.Attribute;
import ru.nsu.fit.data.node.ElementNode;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.data.node.ValueNode;
import ru.nsu.fit.schema.attribute.SchemaAttribute;
import ru.nsu.fit.schema.node.SchemaElementNode;
import ru.nsu.fit.schema.node.SchemaNode;
import ru.nsu.fit.schema.node.SchemaValueNode;

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

    public static String showTheData(Node node, String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        if (node.isElement()) {
            ElementNode elementNode = (ElementNode) node;
            sb.append("element ").append(elementNode.getName());
            for (Attribute attr : elementNode.getAttributes()) {
                sb.append(" ").append(attr.getName()).append("=").append(attr.getValue());
            }
            sb.append('\n');
            String newPrefix = prefix + "\t";
            for (int i = 0; i < elementNode.getChildrenNumber(); ++i) {
                sb.append(showTheData(elementNode.getChild(i), newPrefix));
            }
        } else {
            ValueNode valueNode = (ValueNode) node;
            sb.append("value ").append(valueNode.getValue()).append('\n');
        }
        return sb.toString();
    }

    public static String showTheData(SchemaNode node, String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix);
        if (node.isElement()) {
            SchemaElementNode elementNode = (SchemaElementNode) node;
            sb.append("element ").append(elementNode.getName());
            for (SchemaAttribute attr : elementNode.getAttributes()) {
                sb.append(" ").append(attr.getName());
            }
            sb.append('\n');
            String newPrefix = prefix + "\t";
            for (int i = 0; i < elementNode.getChildrenNumber(); ++i) {
                sb.append(showTheData(elementNode.getChild(i), newPrefix));
            }
        } else {
            SchemaValueNode valueNode = (SchemaValueNode) node;
            sb.append("value ").append(valueNode.getType()).append('\n');
        }
        return sb.toString();
    }
}

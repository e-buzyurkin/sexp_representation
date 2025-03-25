package ru.nsu.fit.schema.type;

import java.util.regex.Pattern;

public class SimpleType {
    private final ValueType valueType;
    private final String pattern;
    private Pattern regexPattern;

    public SimpleType(ValueType valueType, String pattern) {
        this.valueType = valueType;
        this.pattern = pattern;
        parsePattern();
    }

    private void parsePattern() {
        if (pattern == null) return;
        if (valueType == ValueType.STRING) {
            regexPattern = Pattern.compile(pattern);
        } else if (valueType == ValueType.INT) {
            regexPattern = Pattern.compile(pattern);
        } else if (valueType == ValueType.DOUBLE) {
            regexPattern = Pattern.compile(pattern);
        }
    }

    public boolean validate(String value) {
        if (regexPattern == null) return true;
        return regexPattern.matcher(value).matches();
    }

    public ValueType getValueType() {
        return valueType;
    }
}

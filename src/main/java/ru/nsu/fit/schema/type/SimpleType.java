package ru.nsu.fit.schema.type;

import lombok.Getter;

import java.util.regex.Pattern;

public class SimpleType {
    @Getter
    private final ValueType valueType;
    private final String pattern;

    private Pattern regexPattern;
    private double min;
    private double max;

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
            min = Double.parseDouble(pattern.split(" ")[0]);
            max = Double.parseDouble(pattern.split(" ")[1]);
        } else if (valueType == ValueType.DOUBLE) {
            min = Double.parseDouble(pattern.split(" ")[0]);
            max = Double.parseDouble(pattern.split(" ")[1]);
        }
    }

    public boolean validate(String value) {
        if (regexPattern == null) return true;
        return regexPattern.matcher(value).matches();
    }

    public boolean validate(long value) {
        return value >= min && value <= max;
    }

    public boolean validate(double value) {
        return value >= min && value <= max;
    }
}

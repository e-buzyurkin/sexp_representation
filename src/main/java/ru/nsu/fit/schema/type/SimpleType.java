package ru.nsu.fit.schema.type;

import lombok.Getter;
import ru.nsu.fit.exceptions.ValidationFailException;

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
        
        switch (valueType) {
            case STRING -> {
                regexPattern = Pattern.compile(pattern);
            }
            case DOUBLE, INT -> {
                min = Double.parseDouble(pattern.split(" ")[0]);
                max = Double.parseDouble(pattern.split(" ")[1]);
            }
            case ARRAY_INT, ARRAY_DOUBLE, ARRAY_STRING -> {
                min = Integer.parseInt(pattern.split(" ")[0]);
                max = Integer.parseInt(pattern.split(" ")[1]);
            }
        }
    }

    public boolean validate(String value) throws ValidationFailException {
        if (regexPattern == null) {
            return true;
        }
        if (regexPattern.matcher(value).matches()) {
            return true;
        }
        throw new ValidationFailException(value, pattern, "Pattern mismatch");
    }

    public boolean validate(long value) throws ValidationFailException {
        if (value >= min && value <= max) {
            return true;
        }
        throw new ValidationFailException(value, pattern, "Pattern mismatch");
    }

    public boolean validate(double value) throws ValidationFailException {
        if (value >= min && value <= max) {
            return true;
        }
        throw new ValidationFailException(value, pattern, "Pattern mismatch");
    }
}

package ru.nsu.fit.data;
import ru.nsu.fit.schema.type.ValueType;

public class Value {
    private final Object value;
    private final ValueType valueType;

    public Value(String value) {
        this.value = value;
        this.valueType = ValueType.STRING;
    }

    public Value(double value) {
        this.value = value;
        this.valueType = ValueType.DOUBLE;
    }

    public Value(long value) {
        this.value = value;
        this.valueType = ValueType.INT;
    }

    public Object getValue() {
        return value;
    }

    public String asString() {
        return value.toString();
    }

    public ValueType getValueType() {
        return valueType;
    }
}
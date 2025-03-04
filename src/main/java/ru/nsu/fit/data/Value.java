package ru.nsu.fit.data;

public class Value {
    private final Object value;

    public Value(String value) { this.value = value; }
    public Value(double value) { this.value = value; }
    public Value(long value) { this.value = value; }

    public Object getValue() {
        return value;
    }

    public String asString() {
        return value.toString();
    }
}

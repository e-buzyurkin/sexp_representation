package ru.nsu.fit.data;

import java.util.Objects;

public class ValueNode extends Node {
    private Value value;

    public ValueNode(Value value) {
        this.value = Objects.requireNonNull(value, "Value cannot be null");
    }

    public boolean isValue() {
        return true;
    }

    public boolean isElement() {
        return false;
    }

    public Value getValue() {
        return value;
    }
}

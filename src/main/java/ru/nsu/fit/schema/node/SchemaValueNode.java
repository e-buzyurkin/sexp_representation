package ru.nsu.fit.schema.node;

import ru.nsu.fit.schema.type.ValueType;

public class SchemaValueNode extends SchemaNode {
    ValueType type = null;

    @Override
    public boolean isValue() {
        return true;
    }

    @Override
    public boolean isElement() {
        return false;
    }

    public SchemaValueNode setValueType(ValueType type) {
        this.type = type;
        return this;
    }

    public ValueType getType() {
        return type;
    }
}
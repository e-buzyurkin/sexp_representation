package ru.nsu.fit.schema.node;

import lombok.Getter;
import lombok.Setter;
import ru.nsu.fit.schema.type.ValueType;

@Getter
@Setter
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
}
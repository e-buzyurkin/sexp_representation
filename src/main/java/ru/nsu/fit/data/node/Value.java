package ru.nsu.fit.data.node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import ru.nsu.fit.schema.type.ValueType;

@Setter
@Getter
@NoArgsConstructor
public class Value {
    private Object value;
    private ValueType valueType = null;

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

    @Override
    public String toString() {
        return value.toString();
    }

    public static Value ofStringArray(List<String> value) {
        Value v = new Value();
        v.value = value;
        v.valueType = ValueType.ARRAY_STRING;
        return v;
    }

    public static Value ofIntArray(List<Long> value) {
        Value v = new Value();
        v.value = value;
        v.valueType = ValueType.ARRAY_INT;
        return v;
    }

    public static Value ofDoubleArray(List<Double> value) {
        Value v = new Value();
        v.value = value;
        v.valueType = ValueType.ARRAY_DOUBLE;
        return v;
    }
}

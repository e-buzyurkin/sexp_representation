package ru.nsu.fit.data.node;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class Value {
    private Object value;


    public Value(String value) { this.value = value; }
    public Value(double value) { this.value = value; }
    public Value(long value) { this.value = value; }

    public String asString() {
        return value.toString();
    }
}

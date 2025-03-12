package ru.nsu.fit.data.node;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ValueNode extends Node {
    private final Value value;

    public boolean isValue() {
        return true;
    }

    public boolean isElement() {
        return false;
    }

}

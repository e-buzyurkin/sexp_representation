package ru.nsu.fit.data.node;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class Node {
    private Node parent;

    public abstract boolean isValue();
    public abstract boolean isElement();
}

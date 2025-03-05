package ru.nsu.fit.data;

public abstract class Node {
    private Node parent;

    public abstract boolean isValue();
    public abstract boolean isElement();

    public Node getParent() {
        return parent;
    }

    protected void setParent(Node parent) {
        this.parent = parent;
    }
}

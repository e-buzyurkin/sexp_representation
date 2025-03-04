package ru.nsu.fit.data;

import java.util.*;

public class ElementNode extends Node {
    private final List<Attribute> attributes = new ArrayList<>();
    private final List<Node> childNodes = new ArrayList<>();
    private String name;

    public ElementNode(String name) {
        this.name = Objects.requireNonNull(name, "Element name cannot be null");
    }

    public boolean isValue() {
        return false;
    }

    public boolean isElement() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = Objects.requireNonNull(name, "Element name cannot be null");
    }

    public void addAttribute(Attribute attribute) {
        attributes.removeIf(a -> a.getName().equals(attribute.getName()));
        attributes.add(attribute);
    }

    public Attribute getAttributeByName(String name) {
        return attributes.stream().filter(a -> a.getName().equals(name)).findFirst().orElse(null);
    }

    public void addChildNode(Node node) {
        Objects.requireNonNull(node, "Child node cannot be null");
        if (node.getParent() != null) {
            throw new IllegalArgumentException("Node already has a parent");
        }
        childNodes.add(node);
        node.setParent(this);
    }

    public void removeChildNode(Node node) {
        Objects.requireNonNull(node, "Child node cannot be null");
        if (node.getParent() == this) {
            childNodes.remove(node);
            node.setParent(null);
        }
    }
}

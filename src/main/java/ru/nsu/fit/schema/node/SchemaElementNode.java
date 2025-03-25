package ru.nsu.fit.schema.node;

import lombok.Getter;
import ru.nsu.fit.schema.attribute.SchemaAttribute;
import ru.nsu.fit.schema.type.SimpleType;

import java.util.ArrayList;
import java.util.Collection;

public class SchemaElementNode extends SchemaNode {
    private final ArrayList<SchemaNode> childNodes = new ArrayList<>();
    private final ArrayList<SchemaAttribute> attributes = new ArrayList<>();
    @Getter
    private String name;
    private SimpleType simpleType;

    @Override
    public boolean isValue() {
        return false;
    }

    @Override
    public boolean isElement() {
        return true;
    }

    public int getChildrenNumber() {
        return childNodes.size();
    }

    public SchemaNode getChild(int index) {
        return childNodes.get(index);
    }

    public Collection<SchemaAttribute> getAttributes() {
        return attributes;
    }

    public SchemaAttribute getAttributeByName(String name) {
        for (SchemaAttribute attribute : attributes) {
            if (attribute.getName().equals(name)) {
                return attribute;
            }
        }
        return null;
    }

    public SchemaElementNode addChildNode(SchemaNode node) {
        if (node == null) {
            throw new NullPointerException("parameter node must not be null");
        }
        childNodes.add(node);
        return this;
    }

    public SchemaElementNode removeChildNode(SchemaNode node) {
        if (node == null) {
            throw new NullPointerException("parameter node must not be null");
        }
        childNodes.remove(node);
        return this;
    }

    public SchemaElementNode setName(String name) {
        this.name = name;
        return this;
    }

    public SchemaElementNode addAttribute(SchemaAttribute attribute) {
        if (attribute == null) {
            throw new NullPointerException("parameter attribute must not be null");
        }
        attributes.add(attribute);
        return this;
    }
    public SchemaElementNode setSimpleType(SimpleType simpleType) {
        this.simpleType = simpleType;
        return this;
    }

    public SimpleType getSimpleType() {
        return simpleType;
    }
}
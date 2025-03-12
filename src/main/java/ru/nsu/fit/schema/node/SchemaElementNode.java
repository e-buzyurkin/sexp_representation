package ru.nsu.fit.schema.node;

import ru.nsu.fit.schema.attribute.SchemaAttribute;

import java.util.ArrayList;
import java.util.Collection;

public class SchemaElementNode extends SchemaNode {
    private final ArrayList<SchemaNode> childNodes = new ArrayList<>();
    private final ArrayList<SchemaAttribute> attributes = new ArrayList<>();
    private String name;

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
        if (name == null) {
            throw new NullPointerException("parameter name must not be null");
        }
        this.name = name;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public SchemaElementNode addAttribute(SchemaAttribute attribute) {
        if (attribute == null) {
            throw new NullPointerException("parameter attribute must not be null");
        }
        attributes.add(attribute);
        return this;
    }
}

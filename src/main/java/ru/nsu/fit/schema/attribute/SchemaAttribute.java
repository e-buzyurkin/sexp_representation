package ru.nsu.fit.schema.attribute;

import java.security.InvalidParameterException;

public class SchemaAttribute {
    private String name;
    private AttributeUse use;

    // Конструктор с параметром name (use по умолчанию OPTIONAL)
    public SchemaAttribute(String name) {
        this.name = name;
        this.use = AttributeUse.OPTIONAL;
    }

    // Конструктор с параметрами name и use
    public SchemaAttribute(String name, AttributeUse use) {
        this.name = name;
        this.use = use;
    }

    public void setName(String name) {
        if (name == null) {
            throw new InvalidParameterException("name must be not null");
        }
        this.name = name;
    }

    public void setUse(AttributeUse use) {
        this.use = use;
    }

    public String getName() {
        return name;
    }

    public AttributeUse getUse() {
        return use;
    }
}

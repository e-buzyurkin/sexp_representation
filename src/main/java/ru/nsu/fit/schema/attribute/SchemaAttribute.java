package ru.nsu.fit.schema.attribute;

import lombok.Getter;
import lombok.Setter;

import java.security.InvalidParameterException;

@Getter
public class SchemaAttribute {
    private String name;
    @Setter
    private AttributeUse use;

    public SchemaAttribute(String name) {
        this.name = name;
        this.use = AttributeUse.OPTIONAL;
    }

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

}
package ru.nsu.fit.data;

import java.util.Objects;

public class Attribute {
    private String name;
    private String value;

    public Attribute(String name, String value) {
        this.name = Objects.requireNonNull(name, "Attribute name cannot be null");
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}

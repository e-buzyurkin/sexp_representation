package ru.nsu.fit.schema.attribute;

public enum AttributeUse {
    OPTIONAL,   // Атрибут может присутствовать, но необязателен
    REQUIRED,   // Атрибут должен присутствовать обязательно
    PROHIBITED  // Атрибут запрещен
}

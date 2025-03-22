package ru.nsu.fit.schema.node;

import lombok.Getter;

import java.security.InvalidParameterException;

@Getter
public abstract class SchemaNode {
    private int minOccurs = 0;
    private int maxOccurs = Integer.MAX_VALUE;

    public abstract boolean isValue();

    public abstract boolean isElement();

    //мин и макс количество вхождений
    public void setOccurs(int minOccurs, int maxOccurs) {
        if (minOccurs > maxOccurs) {
            throw new InvalidParameterException("maxOccurs must be not lower than minOccurs");
        }
        if (minOccurs < 0) {
            throw new InvalidParameterException("minOccurs must be not negative");
        }
        this.minOccurs = minOccurs;
        this.maxOccurs = maxOccurs;
    }

    // Устанавливает minOccurs и делает maxOccurs "unbounded"
    public void setMinOccursWithMaxOccursUnbounded(int minOccurs) {
        if (minOccurs < 0) {
            throw new InvalidParameterException("minOccurs must be not negative");
        }
        this.minOccurs = minOccurs;
        this.maxOccurs = Integer.MAX_VALUE;
    }

}
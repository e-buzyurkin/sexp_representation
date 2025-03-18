package ru.nsu.fit.schema.node;

import java.security.InvalidParameterException;

public abstract class SchemaNode {
    private int minOccurs = 1;
    private int maxOccurs = 1;

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

    public int getMinOccurs() {
        return minOccurs;
    }

    public int getMaxOccurs() {
        return maxOccurs;
    }
}
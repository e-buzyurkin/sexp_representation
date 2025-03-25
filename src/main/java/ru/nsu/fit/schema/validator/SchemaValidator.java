package ru.nsu.fit.schema.validator;

import ru.nsu.fit.data.node.ElementNode;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.data.node.ValueNode;
import ru.nsu.fit.schema.attribute.SchemaAttribute;
import ru.nsu.fit.schema.node.SchemaElementNode;
import ru.nsu.fit.schema.node.SchemaNode;
import ru.nsu.fit.schema.node.SchemaValueNode;
import ru.nsu.fit.schema.type.SimpleType;
import ru.nsu.fit.schema.type.ValueType;

public class SchemaValidator {
    public static boolean validate(Node data, SchemaNode schema) {
        if ((data.isElement() && schema.isValue()) || (data.isValue() && schema.isElement())) {
            return false;
        }
        if (data.isValue()) {
            return validateAsValue((ValueNode) data, (SchemaValueNode) schema);
        }
        if (data.isElement()) {
            return validateAsElement((ElementNode) data, (SchemaElementNode) schema);
        }
        return true;
    }

    private static boolean validateAsValue(ValueNode data, SchemaValueNode schema) {
        ValueType expectedType = schema.getType();
        if (expectedType != null && data.getValue().getValueType() != expectedType) {
            return false;
        }
        SimpleType simpleType = schema.getSimpleType();
        if (simpleType != null) {
            return simpleType.validate(data.getValue().toString());
        }

        return true;
    }

    private static boolean validateAsElement(ElementNode data, SchemaElementNode schema) {
        if (schema.getName() != null && !data.getName().equals(schema.getName())) {
            return false;
        }
        for (SchemaAttribute attribute : schema.getAttributes()) {
            switch (attribute.getUse()) {
                case REQUIRED:
                    if (data.getAttributeByName(attribute.getName()) == null) {
                        return false;
                    }
                    break;
                case PROHIBITED:
                    if (data.getAttributeByName(attribute.getName()) != null) {
                        return false;
                    }
                    break;
            }
        }
        int dataChildrenNumber = data.getChildrenNumber();
        int schemaChildrenNumber = schema.getChildrenNumber();
        int i = 0, j = 0, matchedWithJ = 0;
        while (j < schemaChildrenNumber) {
            if (matchedWithJ == schema.getChild(j).getMaxOccurs()) {
                ++j;
                matchedWithJ = 0;
                continue;
            }
            if (i < dataChildrenNumber && validate(data.getChild(i), schema.getChild(j))) {
                ++matchedWithJ;
                ++i;
            } else {
                if (matchedWithJ < schema.getMinOccurs()) {
                    return false;
                }
                ++j;
                matchedWithJ = 0;
            }
        }
        return i >= dataChildrenNumber;
    }
}
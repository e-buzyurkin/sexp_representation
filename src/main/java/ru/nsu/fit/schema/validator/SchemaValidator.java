package ru.nsu.fit.schema.validator;

import ru.nsu.fit.data.node.ElementNode;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.data.node.ValueNode;
import ru.nsu.fit.exceptions.ValidationFailException;
import ru.nsu.fit.schema.attribute.SchemaAttribute;
import ru.nsu.fit.schema.node.SchemaElementNode;
import ru.nsu.fit.schema.node.SchemaNode;
import ru.nsu.fit.schema.node.SchemaValueNode;
import ru.nsu.fit.schema.type.SimpleType;
import ru.nsu.fit.schema.type.ValueType;

import java.util.List;

public class SchemaValidator {
    public static boolean validate(Node data, SchemaNode schema) throws ValidationFailException {
        if ((data.isElement() && schema.isValue()) || (data.isValue() && schema.isElement())) {

            throw new ValidationFailException(data, schema, "Mismatching element and value");
        }
        if (data.isValue()) {
            return validateAsValue((ValueNode) data, (SchemaValueNode) schema);
        }
        if (data.isElement()) {
            return validateAsElement((ElementNode) data, (SchemaElementNode) schema);
        }
        return true;
    }

    private static boolean validateAsValue(ValueNode data, SchemaValueNode schema) throws ValidationFailException {
        ValueType expectedType = schema.getType();
        if (expectedType != null && data.getValue().getValueType() != expectedType) {
            throw new ValidationFailException(data, schema, "Value type mismatch");
        }
        SimpleType simpleType = schema.getSimpleType();
        if (simpleType != null) {
            switch (data.getValue().getValueType()) {
                case STRING -> {
                    return simpleType.validate(data.getValue().toString());
                }
                case INT -> {
                    return simpleType.validate((long) data.getValue().getValue());
                }
                case DOUBLE -> {
                    return simpleType.validate((double) data.getValue().getValue());
                }
                case ARRAY_INT, ARRAY_DOUBLE, ARRAY_STRING -> {
                    return simpleType.validate(((List<?>) data.getValue().getValue()).size());
                }
            }
        }

        return true;
    }

    private static boolean validateAsElement(ElementNode data, SchemaElementNode schema) throws ValidationFailException {
        if (schema.getName() != null && !data.getName().equals(schema.getName())) {
            return false;
//            throw new ValidationFailException(data, schema, "Element name mismatch");
        }
        for (SchemaAttribute attribute : schema.getAttributes()) {
            switch (attribute.getUse()) {
                case REQUIRED -> {
                    if (data.getAttributeByName(attribute.getName()) == null) {
                        throw new ValidationFailException(data, schema, "Required attribute missing");
                    }
                }
                case PROHIBITED -> {
                    if (data.getAttributeByName(attribute.getName()) != null) {
                        throw new ValidationFailException(data, schema, "Prohibited attribute present");
                    }
                }
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
                    throw new ValidationFailException(data, schema, "Mismatching children");
                }
                ++j;
                matchedWithJ = 0;
            }
        }
        return i >= dataChildrenNumber;
    }
}
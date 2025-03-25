package ru.nsu.fit.schema.parser;

import ru.nsu.fit.data.node.Attribute;
import ru.nsu.fit.data.node.ElementNode;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.exceptions.IllegalSchemaException;
import ru.nsu.fit.names.SchemaNames;
import ru.nsu.fit.schema.attribute.*;
import ru.nsu.fit.schema.node.*;
import ru.nsu.fit.schema.type.SimpleType;
import ru.nsu.fit.schema.type.ValueType;

import java.util.HashMap;

public class DataToSchemeTranslator {
    private SchemaElementNode schema;
    private final HashMap<String, SchemaElementNode> types = new HashMap<>();

    public static SchemaNode translate(Node schema) throws Exception {
        DataToSchemeTranslator translator = new DataToSchemeTranslator();
        translator.translateNode(schema, null);
        return translator.getSchema();
    }

    private void translateNode(Node node, SchemaElementNode root) throws IllegalSchemaException {
        ElementNode elementNode = (ElementNode) node;
        switch (elementNode.getName()) {
            case SchemaNames.SCHEMA -> {
                schema = new SchemaElementNode().setName(SchemaNames.SCHEMA);
                for (int i = 0; i < elementNode.getChildrenNumber(); i++) {
                    Node child = elementNode.getChild(i);
                    translateNode(child, schema);
                }
            }
            case SchemaNames.ELEMENT -> {
                SchemaElementNode schemaElementNode = new SchemaElementNode();
                Attribute attribute = elementNode.getAttributeByName(SchemaNames.TYPE);
                if (attribute != null) {
                    SchemaElementNode type = types.get(attribute.getValue());
                    if (type == null) {
                        throw new IllegalSchemaException("Type " + attribute.getValue() + " hasn't been declared.");
                    }
                    schemaElementNode.setName(type.getName());
                    for (var i : type.getAttributes()) {
                        schemaElementNode.addAttribute(i);
                    }
                    for (int i = 0; i < type.getChildrenNumber(); ++i) {
                        schemaElementNode.addChildNode(type.getChild(i));
                    }
                }

                attribute = elementNode.getAttributeByName("name");
                if (attribute != null) {
                    schemaElementNode.setName(attribute.getValue());
                }

                schemaElementNode = (SchemaElementNode) setOccurs(schemaElementNode, elementNode);

                for (int i = 0; i < elementNode.getChildrenNumber(); i++) {
                    Node child = elementNode.getChild(i);
                    translateNode(child, schemaElementNode);
                }
                root.addChildNode(schemaElementNode);
            }
            case SchemaNames.ATTRIBUTE -> {
                Attribute attribute = elementNode.getAttributeByName("name");
                if (attribute != null) {
                    SchemaAttribute schemaAttribute = new SchemaAttribute(attribute.getValue());
                    attribute = elementNode.getAttributeByName(SchemaNames.USE);
                    if (attribute != null) {
                        switch (attribute.getValue()) {
                            case "required" -> schemaAttribute.setUse(AttributeUse.REQUIRED);
                            case "optional" -> schemaAttribute.setUse(AttributeUse.OPTIONAL);
                            case "prohibited" -> schemaAttribute.setUse(AttributeUse.PROHIBITED);
                        }
                    }
                    root.addAttribute(schemaAttribute);
                } else {
                    throw new IllegalSchemaException("Attribute must have a name.");
                }
                if (elementNode.getChildrenNumber() > 0) {
                    throw new IllegalSchemaException("Attribute cannot have any nested elements.");
                }
            }
            case SchemaNames.VALUE -> {
                Attribute attribute = elementNode.getAttributeByName(SchemaNames.BASETYPE);
                if (attribute != null) {
                    SchemaValueNode schemaValueNode = new SchemaValueNode();
                    switch (attribute.getValue()) {
                        case "string" -> schemaValueNode.setType(ValueType.STRING);
                        case "int" -> schemaValueNode.setType(ValueType.INT);
                        case "double" -> schemaValueNode.setType(ValueType.DOUBLE);
                    }
                    schemaValueNode = (SchemaValueNode) setOccurs(schemaValueNode, elementNode);
                    root.addChildNode(schemaValueNode);
                }
                if (elementNode.getChildrenNumber() > 0) {
                    throw new IllegalSchemaException("Value cannot have any nested elements.");
                }
            }
            case SchemaNames.SIMPLETYPE -> {
                Attribute typeNameAttr = elementNode.getAttributeByName(SchemaNames.TYPENAME);
                if (typeNameAttr == null) {
                    throw new IllegalSchemaException("Type must have a \"type-name\" attribute.");
                }
                String typeName = typeNameAttr.getValue();

                Attribute baseTypeAttr = elementNode.getAttributeByName(SchemaNames.BASETYPE);
                if (baseTypeAttr == null) {
                    throw new RuntimeException("SimpleType must have a \"base-type\" attribute.");
                }

                ValueType valueType;
                switch (baseTypeAttr.getValue()) {
                    case "string" -> valueType = ValueType.STRING;
                    case "int" -> valueType = ValueType.INT;
                    case "double" -> valueType = ValueType.DOUBLE;
                    default -> throw new RuntimeException("Unknown base-type: " + baseTypeAttr.getValue());
                }

                String pattern = null;
                if (valueType == ValueType.STRING) {
                    Attribute patternAttr = elementNode.getAttributeByName("pattern");
                    pattern = (patternAttr != null) ? patternAttr.getValue() : null;
                }

                SimpleType simpleType = new SimpleType(valueType, pattern);

                SchemaElementNode type = new SchemaElementNode();
                type.setSimpleType(simpleType);

                Attribute elementNameAttr = elementNode.getAttributeByName("element-name");
                if (elementNameAttr != null) {
                    type.setName(elementNameAttr.getValue());
                }

                types.put(typeName, type);

                for (int i = 0; i < elementNode.getChildrenNumber(); i++) {
                    Node child = elementNode.getChild(i);
                    translateNode(child, type);
                }
            }
        }
    }

    private SchemaNode setOccurs(SchemaNode schemaNode, ElementNode elementNode) throws IllegalSchemaException {
        Attribute attribute = elementNode.getAttributeByName(SchemaNames.MINOCCURS);
        if (attribute != null) {
            int minOccurs = Integer.parseInt(attribute.getValue());
            attribute = elementNode.getAttributeByName(SchemaNames.MAXOCCURS);
            if (attribute != null) {
                if (attribute.getValue().equals("unbounded")) {
                    schemaNode.setMinOccursWithMaxOccursUnbounded(minOccurs);
                } else {
                    schemaNode.setOccurs(
                            minOccurs,
                            Integer.parseInt(attribute.getValue()));
                }
            } else {
                throw new IllegalSchemaException("Both minOccurs and maxOccurs must be specified.");
            }
        }

        return schemaNode;
    }

    private SchemaNode getSchema() throws IllegalSchemaException {
        if (schema == null || schema.getChildrenNumber() != 1) {
            throw new Exception("schema must have exactly 1 not complex type element");
        }
        return schema.getChild(0);
    }
}

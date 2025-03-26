package ru.nsu.fit.path.step.requirement;

import lombok.Data;
import ru.nsu.fit.data.node.Attribute;
import ru.nsu.fit.data.node.ElementNode;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.data.node.ValueNode;
import ru.nsu.fit.path.Context;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

@Data
public class Requirement {
    private String leftPart;
    private String rightPart;
    private RequirementPartType leftPartType;
    private RequirementPartType rightPartType;
    private PredicateType type;
    private String predicateName;

    public Requirement(PredicateType type, RequirementPartType leftPartType, String leftPart, RequirementPartType rightPartType, String rightPart) {
        this.leftPartType = leftPartType;
        this.leftPart = leftPart;
        this.rightPartType = rightPartType;
        this.rightPart = rightPart;
        this.type = type;
        this.predicateName = null;
    }

    public Requirement(String predicateName, RequirementPartType leftPartType, String leftPart, RequirementPartType rightPartType, String rightPart) {
        this.leftPartType = leftPartType;
        this.leftPart = leftPart;
        this.rightPartType = rightPartType;
        this.rightPart = rightPart;
        this.type = null;
        this.predicateName = predicateName;
    }


    public boolean validateNode(Node node, Context context) {
        Map<String, String> attributes = getAttributes(node);

        BiFunction<String, String, Boolean> predicate;
        if (type == PredicateType.EQUALITY) {
            predicate = String::equals;
        } else if (type == PredicateType.INEQUALITY) {
            predicate = (s1, s2) -> !s1.equals(s2);
        } else {
            predicate = context.getPredicateByName(predicateName);
        }

        if (leftPartType == RequirementPartType.VALUE) {
            if (rightPartType == RequirementPartType.VALUE) {
                return predicate.apply(leftPart, rightPart);
            }
            if (rightPartType == RequirementPartType.ATTR_NAME) {
                if (!rightPart.equals("*")) {
                    if (attributes.get(rightPart) == null) {
                        return false;
                    }
                    return predicate.apply(leftPart, attributes.get(rightPart));
                }
                boolean result = false;
                for (String i: attributes.values()) {
                    result |= predicate.apply(leftPart, i);
                }
                return result;
            }
        }
        if (leftPartType == RequirementPartType.ATTR_NAME) {
            if (rightPartType == RequirementPartType.VALUE) {
                if (!leftPart.equals("*")) {
                    if (attributes.get(leftPart) == null) {
                        return false;
                    }
                    return predicate.apply(attributes.get(leftPart), rightPart);
                }
                boolean result = false;
                for (String i: attributes.values()) {
                    result |= predicate.apply(i, rightPart);
                }
                return result;
            }
            if (rightPartType == RequirementPartType.ATTR_NAME) {
                if (!leftPart.equals("*")) {
                    if (attributes.get(leftPart) == null) {
                        return false;
                    }
                    if (!rightPart.equals("*")) {
                        if (attributes.get(rightPart) == null) {
                            return false;
                        }
                        return predicate.apply(attributes.get(leftPart), attributes.get(rightPart));
                    } else {
                        boolean result = false;
                        for (String i: attributes.values()) {
                            result |= predicate.apply(attributes.get(leftPart), i);
                        }
                        return result;
                    }
                } else {
                    if (!rightPart.equals("*")) {
                        if (attributes.get(rightPart) == null) {
                            return false;
                        }
                        boolean result = false;
                        for (String i: attributes.values()) {
                            result |= predicate.apply(i, attributes.get(rightPart));
                        }
                        return result;
                    } else {
                        boolean result = false;
                        for (String i: attributes.values()) {
                            for (String j : attributes.values()) {
                                result |= predicate.apply(i, j);
                            }
                        }
                        return result;
                    }
                }
            }
        }
        return false;
    }

    private static Map<String, String> getAttributes(Node node) {
        Map<String, String> attributes = new HashMap<>();
        if (node.isValue()) {
            ValueNode valueNode = (ValueNode) node;
            switch (valueNode.getValue().getValueType()) {
                case STRING -> attributes.put("type", "string");
                case INT -> attributes.put("type", "int");
                case DOUBLE -> attributes.put("type", "double");
            }
        }
        if (node.isElement()) {
            assert node instanceof ElementNode;
            ElementNode elementNode = (ElementNode) node;
            for (Attribute attribute : elementNode.getAttributes()) {
                attributes.put(attribute.getName(), attribute.getValue());
            }
        }
        return attributes;
    }
}
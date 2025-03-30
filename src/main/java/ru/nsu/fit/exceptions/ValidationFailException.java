package ru.nsu.fit.exceptions;

import ru.nsu.fit.data.Util;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.schema.node.SchemaNode;

public class ValidationFailException extends Exception {
    public ValidationFailException(Node node, SchemaNode schemaNode, String message) {
        super(message + "\n" + "Data: " + Util.showTheData(node, "")
                + "Schema: " + Util.showTheData(schemaNode, ""));
    }
}

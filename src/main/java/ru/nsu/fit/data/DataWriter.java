package ru.nsu.fit.data;

import ru.nsu.fit.data.node.Attribute;
import ru.nsu.fit.data.node.ElementNode;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.data.node.ValueNode;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;

public class DataWriter {
    public static void writeToFile(String filename, Node data) throws IOException {
        if (filename == null) {
            throw new NullPointerException("Filename cannot be null.");
        }
        if (data == null) {
            throw new NullPointerException("Data cannot be null.");
        }
        String format = ".txt";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename + format));

        write(data, writer, "");
        writer.close();
    }

    public static void writeToWriter(Writer writer, Node data) throws IOException {
        write(data, writer, "");
    }

    private static void write(Node node, Writer writer, String prefix) throws IOException {
        writer.write(prefix);
        if (node.isValue()) {
            ValueNode valueNode = (ValueNode) node;

            if (valueNode.getValue().getValue() instanceof String) {
                writer.write(Util.processEscapingCharsToInput((String) valueNode.getValue().getValue()) + " ");
            } else {
                writer.write(valueNode.getValue() + " ");
            }
        } else if (node.isElement()) {
            ElementNode elementNode = (ElementNode) node;
            writer.write("(" + elementNode.getName());

            if (!elementNode.getAttributes().isEmpty()) {
                writer.write(" {");
                Iterator<Attribute> iterator = elementNode.getAttributes().iterator();
                Attribute attribute = iterator.next();
                while (iterator.hasNext()) {
                    writer.write(attribute.getName() + " " + Util.processEscapingCharsToInput(attribute.getValue()) + ", ");
                    attribute = iterator.next();
                }
                writer.write(attribute.getName() + " " + Util.processEscapingCharsToInput(attribute.getValue()) + "}");
            }

            if (elementNode.getChildrenNumber() > 0) {
                for (int i = 0; i < elementNode.getChildrenNumber(); i++) {
                    writer.write('\n');
                    Node child = elementNode.getChild(i);
                    write(child, writer, prefix + "\t");
                }
                writer.write('\n');
                writer.write(prefix + ")");
            } else {
                writer.write(")");
            }
        }
    }
}
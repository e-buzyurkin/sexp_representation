package ru.nsu.fit;

import ru.nsu.fit.data.node.*;
import ru.nsu.fit.data.*;
import ru.nsu.fit.path.Context;
import ru.nsu.fit.path.Path;
import ru.nsu.fit.schema.node.SchemaNode;
import ru.nsu.fit.schema.parser.SchemaReader;
import ru.nsu.fit.schema.validator.SchemaValidator;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

public class Main {
    static private void showTheData(Node node, String prefix) {
        System.out.print(prefix);
        if (node.isElement()) {
            ElementNode elementNode = (ElementNode) node;
            System.out.print("element " + elementNode.getName());
            for (Attribute attr : elementNode.getAttributes()) {
                System.out.print(" " + attr.getName() + "=" + attr.getValue());
            }
            System.out.print('\n');
            prefix = prefix + "\t";
            for (int i = 0; i < elementNode.getChildrenNumber(); ++i) {
                showTheData(elementNode.getChild(i), prefix);
            }
        } else {
            ValueNode valueNode = (ValueNode) node;
            System.out.println("value " + valueNode.getValue());
        }
    }

    public static void main(String[] args) throws Exception {
        Node dataNode = DataReader.parseData(new FileReader("src/main/resources/data1.txt"));
        SchemaNode schemaNode = SchemaReader.parseSchema(new FileReader("src/main/resources/data1_schema.txt"));

        boolean isValid = SchemaValidator.validate(dataNode, schemaNode);
        System.out.println("Data validation result: " + isValid);
        showTheData(dataNode, "");


        Path path = Path.compile("//tree/@value");
        Context context = new Context(dataNode);
        Collection<Node> res = path.evaluate(context);
        int n = 0;
        for (Node i : res) {
            ++n;
            System.out.println(n + ": ");
            showTheData(i, "   ");
        }


    }
}

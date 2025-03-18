package ru.nsu.fit;

import ru.nsu.fit.data.node.*;
import ru.nsu.fit.data.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

    public static void main(String[] args) throws IOException {
        Node dataNode = DataReader.parseData(new FileReader("src/main/resources/data1.txt"));
        showTheData(dataNode, "");
        System.out.println();
    }
}

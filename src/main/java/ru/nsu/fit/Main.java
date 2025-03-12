package ru.nsu.fit;

import ru.nsu.fit.data.node.Attribute;
import ru.nsu.fit.data.node.ElementNode;
import ru.nsu.fit.data.node.Value;
import ru.nsu.fit.data.node.ValueNode;

public class Main {
    public static void main(String[] args) {
        ElementNode root = new ElementNode("root");
        root.addAttribute(new Attribute("version", "1.0"));

        ElementNode child = new ElementNode("child");
        child.addAttribute(new Attribute("id", "123"));

        ValueNode textNode = new ValueNode(new Value("Hello, world!"));

        root.addChildNode(child);
        child.addChildNode(textNode);

        System.out.println("Root: " + root.getName());
        System.out.println("  Child: " + child.getName());
        System.out.println("    Text: " + textNode.getValue().asString());
    }
}

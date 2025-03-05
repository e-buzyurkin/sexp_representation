package ru.nsu.fit;

import ru.nsu.fit.data.Attribute;
import ru.nsu.fit.data.ElementNode;
import ru.nsu.fit.data.Node;
import ru.nsu.fit.data.Value;
import ru.nsu.fit.data.ValueNode;

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

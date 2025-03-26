package ru.nsu.fit.path;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import ru.nsu.fit.PathLexer;
import ru.nsu.fit.PathParser;
import ru.nsu.fit.data.node.ElementNode;
import ru.nsu.fit.data.node.Node;
import ru.nsu.fit.path.step.Step;
import ru.nsu.fit.path.step.StepTransition;

import java.util.*;
import java.util.stream.Collectors;

public class Path {
    private PathType type;
    private final ArrayList<Step> steps = new ArrayList<>();

    public Path(PathType type) {
        this.type = type;
    }

    public PathType getType() {
        return type;
    }

    public Path setType(PathType type) {
        this.type = type;
        return this;
    }

    public Path addStep(Step step) {
        steps.add(step);
        return this;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void removeStep(int index) {
        steps.remove(index);
    }

    public Collection<Node> evaluate(Context context) {
        Node imaginaryNode = null;
        Node contextNode = context.getContextNode();
        if (contextNode == null) {
            throw new NullPointerException("parameter contextNode must not be null");
        }
        if (this.steps.isEmpty()) {
            throw new IllegalStateException("path must contain at least one step");
        }
        Set<Node> result = new HashSet<>();
        switch (type) {
            case RELATIVE -> result.add(contextNode);
            case ABSOLUTE -> {
                imaginaryNode = new DocumentNode("documentNode").addChildNode(findRootOfData(contextNode));
                result.add(imaginaryNode);
            }
        }
        for (Step step : steps) {
            if (step.getTransition() == StepTransition.ANY_INNER) {
                Set<Node> nodesAfterTransition = new HashSet<>();
                for (Node node : result) {
                    addSubtreeOfNodes(node, nodesAfterTransition);
                }
                result = nodesAfterTransition;
            }

            Set<Node> nodesAfterAxis = new HashSet<>();
            for (Node node : result) {
                addNodesAfterAxis(node, step, nodesAfterAxis);
            }
            result = nodesAfterAxis;

            if (step.getRequirement() != null) {
                result = result.stream()
                        .filter(node -> step.getRequirement()
                                .validateNode(node, context))
                        .collect(Collectors.toSet());
            }
        }

        Node finalImaginaryNode = imaginaryNode;
        return result.stream().filter(node -> (node != finalImaginaryNode)).collect(Collectors.toSet());
    }


    private static void addNodesAfterAxis(Node node, Step step, Set<Node> result) {
        if (step.getAxisType() == null) {
            if (!node.isElement()) {
                return;
            }
            ElementNode elementNode = (ElementNode) node;
            for (int i = 0; i < elementNode.getChildrenNumber(); ++i) {
                Node child = elementNode.getChild(i);
                if (child.isElement() && ((ElementNode) child).getName().equals(step.getElementName())) {
                    result.add(child);
                }
            }
            return;
        }
        switch (step.getAxisType()) {
            case CHILD -> {
                if (!node.isElement()) {
                    return;
                }
                ElementNode elementNode = (ElementNode) node;
                for (int i = 0; i < elementNode.getChildrenNumber(); ++i) {
                    Node child = elementNode.getChild(i);
                    result.add(child);
                }
            }
            case PARENT -> {
                if (node.getParent() != null) {
                    result.add(node.getParent());
                }
            }
            case CURRENT -> result.add(node);
            case CHILD_ELEMENT -> {
                if (!node.isElement()) {
                    return;
                }
                ElementNode elementNode = (ElementNode) node;
                for (int i = 0; i < elementNode.getChildrenNumber(); ++i) {
                    Node child = elementNode.getChild(i);
                    if (child.isElement()) {
                        result.add(child);
                    }
                }
            }
            case CHILD_VALUE -> {
                if (!node.isElement()) {
                    return;
                }
                ElementNode elementNode = (ElementNode) node;
                for (int i = 0; i < elementNode.getChildrenNumber(); ++i) {
                    Node child = elementNode.getChild(i);
                    if (child.isValue()) {
                        result.add(child);
                    }
                }
            }
        }
    }

    private static void addSubtreeOfNodes(Node root, Set<Node> result) {
        if (result.contains(root)) {
            return;
        }
        result.add(root);
        if (root.isElement()) {
            ElementNode node = (ElementNode) root;
            for (int i = 0; i < node.getChildrenNumber(); ++i) {
                addSubtreeOfNodes(node.getChild(i), result);
            }
        }
    }

    private static class DocumentNode extends ElementNode {
        public DocumentNode(String name) {
            super(name);
        }

        public ElementNode addChildNode(Node node) {
            if (node == null) {
                throw new NullPointerException("parameter node must not be null");
            }
            if (node.getParent() != null) {
                throw new IllegalArgumentException("node must not have parent before it is added");
            }
            childNodes.add(node);
            return this;
        }
    }

    private static Node findRootOfData(Node node) {
        while (node.getParent() != null) {
            node = node.getParent();
        }
        return node;
    }

    public static Path compile(String pathAsString) throws Exception {
        PathLexer lexer = new PathLexer(CharStreams.fromString(pathAsString));
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PathParser parser = new PathParser(tokens);
        ParseTree tree = parser.calc();
        ParseTreeWalker walker = new ParseTreeWalker();
        PathWalker pathWalker = new PathWalker();
        walker.walk(pathWalker, tree);
        return pathWalker.getPath();
    }
}

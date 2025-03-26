package ru.nsu.fit.path;

import ru.nsu.fit.data.node.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class Context {
    private final Map<String, BiFunction<String, String, Boolean>> predicates = new HashMap<>();
    private Node contextNode;
    public Context(Node contextNode) {
        this.contextNode = contextNode;
    }

    public BiFunction<String, String, Boolean> getPredicateByName(String name) {
        return predicates.get(name);
    }

    public Set<String> getAllPredicateNames() {
        return predicates.keySet();
    }

    public Context addPredicate(String name, BiFunction<String, String, Boolean> predicate) {
        predicates.put(name, predicate);
        return this;
    }

    public Context removePredicateByName(String name) {
        predicates.remove(name);
        return this;
    }

    public Context setContextNode(Node contextNode) {
        this.contextNode = contextNode;
        return this;
    }

    public Node getContextNode() {
        return contextNode;
    }
}

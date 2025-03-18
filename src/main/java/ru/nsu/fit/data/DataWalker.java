package ru.nsu.fit.data;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.nsu.fit.DataListener;
import ru.nsu.fit.DataParser;
import ru.nsu.fit.data.node.*;

import java.util.Stack;

class DataWalker implements DataListener {
    private Node parentNode;
    private final Stack<Node> nodes = new Stack<>();
    private Attribute attribute;

    private void addNodeInTree(Node node) {
        if (nodes.empty()) {
            nodes.add(node);
            parentNode = node;
        } else {
            ElementNode last = (ElementNode) nodes.lastElement();
            last.addChildNode(node);
            nodes.add(node);
        }
    }

    @Override
    public void enterCalc(DataParser.CalcContext ctx) {
    }

    @Override
    public void exitCalc(DataParser.CalcContext ctx) {
    }

    @Override
    public void enterExpr(DataParser.ExprContext ctx) {

    }

    @Override
    public void exitExpr(DataParser.ExprContext ctx) {
        nodes.pop();
    }

    @Override
    public void enterExpr_name(DataParser.Expr_nameContext ctx) {

    }

    @Override
    public void exitExpr_name(DataParser.Expr_nameContext ctx) {
        this.addNodeInTree(new ElementNode(ctx.getText()));
    }

    @Override
    public void enterAttrs(DataParser.AttrsContext ctx) {
    }

    @Override
    public void exitAttrs(DataParser.AttrsContext ctx) {
    }

    @Override
    public void enterAttr(DataParser.AttrContext ctx) {
        this.attribute = new Attribute();
    }

    @Override
    public void exitAttr(DataParser.AttrContext ctx) {
        this.attribute.setValue(Util.processEscapingCharsFromInput(ctx.STRING().getText()));
        ((ElementNode) this.nodes.lastElement()).addAttribute(attribute);
        this.attribute = null;
    }

    @Override
    public void enterAttr_name(DataParser.Attr_nameContext ctx) {
    }

    @Override
    public void exitAttr_name(DataParser.Attr_nameContext ctx) {
        this.attribute.setName(ctx.NAME().getText());
    }

    @Override
    public void enterValue(DataParser.ValueContext ctx) {
    }

    @Override
    public void exitValue(DataParser.ValueContext ctx) {
        Value value = new Value();
        if (ctx.STRING() != null) {
            value.setValue(Util.processEscapingCharsFromInput(ctx.getText()));
        } else if (ctx.INT() != null) {
            value.setValue(Long.parseLong(ctx.getText()));
        } else if (ctx.DOUBLE() != null) {
            value.setValue(Double.parseDouble(ctx.getText()));
        }
        ValueNode valueNode = new ValueNode(value);
        this.addNodeInTree(valueNode);
    }

    @Override
    public void enterList(DataParser.ListContext ctx) {

    }

    @Override
    public void exitList(DataParser.ListContext ctx) {

    }

    public Node getDataNode() {
        return parentNode;
    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}

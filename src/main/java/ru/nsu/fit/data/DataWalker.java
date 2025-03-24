package ru.nsu.fit.data;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.nsu.fit.DataListener;
import ru.nsu.fit.DataParser;
import ru.nsu.fit.data.node.*;
import ru.nsu.fit.schema.type.ValueType;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

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
            value.setValueType(ValueType.STRING);
        } else if (ctx.INT() != null) {
            value.setValue(Long.parseLong(ctx.getText()));
            value.setValueType(ValueType.INT);
        } else if (ctx.DOUBLE() != null) {
            value.setValue(Double.parseDouble(ctx.getText()));
            value.setValueType(ValueType.DOUBLE);
        } else if (ctx.array_double() != null) {
            value = Value.ofDoubleArray(
                    Arrays.stream(ctx.getText().substring(1, ctx.getText().length() - 1).split(","))
                    .map(Double::parseDouble)
                    .collect(Collectors.toList())
            );
            value.setValueType(ValueType.ARRAY_DOUBLE);
        } else if (ctx.array_int() != null) {
            value = Value.ofIntArray(
                    Arrays.stream(ctx.getText().substring(1, ctx.getText().length() - 1).split(","))
                            .map(Long::parseLong)
                            .collect(Collectors.toList())
            );
            value.setValueType(ValueType.ARRAY_INT);
        } else if (ctx.array_string() != null) {
            value = Value.ofStringArray(
                    List.of(ctx.getText().substring(1, ctx.getText().length() - 1).split(",")));
            value.setValueType(ValueType.ARRAY_STRING);
        }
        ValueNode valueNode = new ValueNode(value);
        this.addNodeInTree(valueNode);
    }

    @Override
    public void enterArray_string(DataParser.Array_stringContext ctx) {

    }

    @Override
    public void exitArray_string(DataParser.Array_stringContext ctx) {

    }

    @Override
    public void enterArray_int(DataParser.Array_intContext ctx) {

    }

    @Override
    public void exitArray_int(DataParser.Array_intContext ctx) {

    }

    @Override
    public void enterArray_double(DataParser.Array_doubleContext ctx) {

    }

    @Override
    public void exitArray_double(DataParser.Array_doubleContext ctx) {

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

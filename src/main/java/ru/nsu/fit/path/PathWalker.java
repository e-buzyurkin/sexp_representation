package ru.nsu.fit.path;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import ru.nsu.fit.PathListener;
import ru.nsu.fit.PathParser;
import ru.nsu.fit.path.step.AxisType;
import ru.nsu.fit.path.step.Step;
import ru.nsu.fit.path.step.StepTransition;
import ru.nsu.fit.path.step.requirement.PredicateType;
import ru.nsu.fit.path.step.requirement.Requirement;
import ru.nsu.fit.path.step.requirement.RequirementPartType;

import java.util.ArrayList;
import java.util.List;

import static ru.nsu.fit.data.Util.processEscapingCharsFromInput;

class PathWalker implements PathListener {
    private Path path;
    private Step step;
    private StepTransition stepTransition = StepTransition.JUST_CURRENT;
    private List<RequirementPartType> requirementPartTypes;
    private List<String> requirementParts;
    private boolean wasError = false;

    public Path getPath() throws Exception {
        if (wasError) {
            throw new Exception("some problems with path (((. See stderr for more info");
        }
        return path;
    }

    @Override
    public void enterCalc(PathParser.CalcContext ctx) {

    }

    @Override
    public void exitCalc(PathParser.CalcContext ctx) {
    }

    @Override
    public void enterLocationPath(PathParser.LocationPathContext ctx) {
        if (ctx.relativeLocationPath() != null) {
            path = new Path(PathType.RELATIVE);
        } else {
            path = new Path(PathType.ABSOLUTE);
        }
    }

    @Override
    public void exitLocationPath(PathParser.LocationPathContext ctx) {
    }

    @Override
    public void enterAbsoluteLocationPathNoroot(PathParser.AbsoluteLocationPathNorootContext ctx) {
        if (ctx.getChild(0).getText().equals("//")) {
            path.addStep(new Step(StepTransition.JUST_CURRENT, AxisType.CURRENT));
            stepTransition = StepTransition.ANY_INNER;
        }
    }

    @Override
    public void exitAbsoluteLocationPathNoroot(PathParser.AbsoluteLocationPathNorootContext ctx) {
    }

    @Override
    public void enterRelativeLocationPath(PathParser.RelativeLocationPathContext ctx) {
    }

    @Override
    public void exitRelativeLocationPath(PathParser.RelativeLocationPathContext ctx) {
    }

    @Override
    public void enterStepSeparator(PathParser.StepSeparatorContext ctx) {
    }

    @Override
    public void exitStepSeparator(PathParser.StepSeparatorContext ctx) {
        if (ctx.getText().equals("/")) {
            stepTransition = StepTransition.JUST_CURRENT;
        } else if (ctx.getText().equals("//")) {
            stepTransition = StepTransition.ANY_INNER;
        }
    }


    @Override
    public void enterStep(PathParser.StepContext ctx) {

    }

    @Override
    public void enterAbbreviatedStep(PathParser.AbbreviatedStepContext ctx) {
    }

    @Override
    public void exitAbbreviatedStep(PathParser.AbbreviatedStepContext ctx) {
        if (ctx.getText().equals(".")) {
            step = new Step(stepTransition, AxisType.CURRENT);
        } else if (ctx.getText().equals("..")) {
            step = new Step(stepTransition, AxisType.PARENT);
        }
    }

    @Override
    public void enterType(PathParser.TypeContext ctx) {

    }

    @Override
    public void exitType(PathParser.TypeContext ctx) {
        if (ctx.getText().equals("@value")) {
            step = new Step(stepTransition, AxisType.CHILD_VALUE);
        } else if (ctx.getText().equals("@element")) {
            step = new Step(stepTransition, AxisType.CHILD_ELEMENT);
        }
    }

    @Override
    public void enterNCName(PathParser.NCNameContext ctx) {

    }

    @Override
    public void exitNCName(PathParser.NCNameContext ctx) {
        if (step == null) {
            if (ctx.getText().equals("*")) {
                step = new Step(stepTransition, AxisType.CHILD);
            } else {
                step = new Step(stepTransition, ctx.NAME().getText());
            }
        }
        //
    }

    @Override
    public void exitStep(PathParser.StepContext ctx) {
        path.addStep(step);
        step = null;
    }

    @Override
    public void enterPredicate(PathParser.PredicateContext ctx) {
        requirementParts = new ArrayList<>();
        requirementPartTypes = new ArrayList<>();
    }

    @Override
    public void exitPredicate(PathParser.PredicateContext ctx) {
    }


    @Override
    public void enterExpr(PathParser.ExprContext ctx) {
    }

    @Override
    public void exitExpr(PathParser.ExprContext ctx) {
    }

    @Override
    public void enterEqual_pred_expr(PathParser.Equal_pred_exprContext ctx) {

    }

    @Override
    public void exitEqual_pred_expr(PathParser.Equal_pred_exprContext ctx) {
        PredicateType type;
        if (ctx.getChild(1).getText().equals("!=")) {
            type = PredicateType.INEQUALITY;
        } else {
            type = PredicateType.EQUALITY;
        }
        step.setRequirement(new Requirement(type,
                requirementPartTypes.get(0), requirementParts.get(0),
                requirementPartTypes.get(1), requirementParts.get(1)));
    }

    @Override
    public void enterCustom_predicat_expr(PathParser.Custom_predicat_exprContext ctx) {
    }

    @Override
    public void exitCustom_predicat_expr(PathParser.Custom_predicat_exprContext ctx) {
        String name = ctx.NAME().getText();
        step.setRequirement(new Requirement(name,
                requirementPartTypes.get(0), requirementParts.get(0),
                requirementPartTypes.get(1), requirementParts.get(1)));
    }

    @Override
    public void enterAttribute_expr(PathParser.Attribute_exprContext ctx) {

    }

    @Override
    public void exitAttribute_expr(PathParser.Attribute_exprContext ctx) {
        if (ctx.STRING() != null) {
            requirementPartTypes.add(RequirementPartType.VALUE);
            requirementParts.add(processEscapingCharsFromInput(ctx.STRING().getText()));
        } else {
            requirementPartTypes.add(RequirementPartType.ATTR_NAME);
            requirementParts.add(ctx.nCName().getText());
        }
    }


    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {
        wasError = true;
    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}

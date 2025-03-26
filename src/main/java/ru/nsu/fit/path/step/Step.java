package ru.nsu.fit.path.step;

import ru.nsu.fit.path.step.requirement.Requirement;

public class Step {
    private StepTransition transition;
    private AxisType axisType;
    private String elementName;
    private Requirement requirement = null;


    public Step setTransition(StepTransition transition) {
        this.transition = transition;
        return this;
    }

    public StepTransition getTransition() {
        return transition;
    }

    public Step(StepTransition transition, AxisType axisType) {
        this.transition = transition;
        this.axisType = axisType;
        this.elementName = null;
    }

    public Step(StepTransition transition, String elementName) {
        this.transition = transition;
        this.axisType = null;
        this.elementName = elementName;
    }

    public AxisType getAxisType() {
        return axisType;
    }

    public Step setAxisType(AxisType type) {
        axisType = type;
        elementName = null;
        return this;
    }

    public Step setElementName(String elementName) {
        this.elementName = elementName;
        axisType = null;
        return this;
    }

    public String getElementName() {
        return elementName;
    }

    public Step setRequirement(Requirement requirement) {
        this.requirement = requirement;
        return this;
    }

    public Requirement getRequirement() {
        return requirement;
    }


    public Step removePredicate() {
        requirement = null;
        return this;
    }
}


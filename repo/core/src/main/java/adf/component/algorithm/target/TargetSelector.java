package adf.component.algorithm.target;


import rescuecore2.worldmodel.EntityID;

public abstract class TargetSelector {


    public TargetSelector calc() {
        return this;
    }

    public abstract EntityID getTarget();
}

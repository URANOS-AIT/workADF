package adf.component.algorithm.target;


import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

public abstract class TargetSelector<E extends StandardEntity> {


    public TargetSelector calc() {
        return this;
    }

    public abstract EntityID getTarget();

    //public abstract boolean hasTarget();

    //public abstract EntityID getNextPoint();
}

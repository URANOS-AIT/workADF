package adf.component.algorithm.search;


import rescuecore2.worldmodel.EntityID;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public abstract class Search {

    public Search calc() {
        return this;
    }

    public abstract void clear();

    public abstract List<EntityID> getResult();

    //public abstract void setFrom(EntityID id);

    //public abstract void setDist(Collection<EntityID> target);

    /*public void setDist(EntityID... target) {
        this.setDist(Arrays.asList(target));
    }*/
}

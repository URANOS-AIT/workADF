package adf.util.compatibility;

import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Blockade;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public abstract class RouteSearcher {

    private StandardWorldModel world;

    public RouteSearcher(StandardWorldModel standardWorldModel) {
        this.world = standardWorldModel;
    }

    public abstract List<EntityID> getPath(int time, EntityID from, EntityID to);

    public abstract List<EntityID> getFullPath(int time, EntityID from, EntityID to);


    public List<EntityID> getPath(int time, Human from, EntityID to) {
        return this.getPath(time, from.getPosition(), to);
    }

    public List<EntityID> getPath(int time, EntityID from, Area to) {
        return this.getPath(time, from, to.getID());
    }

    public List<EntityID> getPath(int time, Human from, Area to) {
        return this.getPath(time, from.getPosition(), to.getID());
    }

    public List<EntityID> getPath(int time, EntityID from, Blockade blockade) {
        return this.getPath(time, from, blockade.getPosition());
    }

    public List<EntityID> getPath(int time, Human from, Blockade blockade) {
        return this.getPath(time, from.getPosition(), blockade.getPosition());
    }

    public List<EntityID> getFullPath(int time, Human from, EntityID to) {
        return this.getFullPath(time, from.getPosition(), to);
    }

    public List<EntityID> getFullPath(int time, EntityID from, Area to) {
        return this.getFullPath(time, from, to.getID());
    }

    public List<EntityID> getFullPath(int time, Human from, Area to) {
        return this.getFullPath(time, from.getPosition(), to.getID());
    }

    public List<EntityID> getFullPath(int time, EntityID from, Blockade blockade) {
        return this.getFullPath(time, from, blockade.getPosition());
    }

    public List<EntityID> getFullPath(int time, Human from, Blockade blockade) {
        return this.getFullPath(time, from.getPosition(), blockade.getPosition());
    }
}

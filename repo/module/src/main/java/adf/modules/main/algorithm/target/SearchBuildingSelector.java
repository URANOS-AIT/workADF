package adf.modules.main.algorithm.target;


import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.path.PathPlanner;
import adf.component.algorithm.target.TargetSelector;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class SearchBuildingSelector extends TargetSelector<Building> {

    private PathPlanner pathPlanner;

    private Collection<EntityID> unexploredBuildings;
    private EntityID result;

    public SearchBuildingSelector(AgentInfo ai, WorldInfo wi, ScenarioInfo si, PathPlanner pp) {
        super(ai, wi, si);
        this.pathPlanner = pp;
        this.init();
    }

    private void init() {
        this.unexploredBuildings = new HashSet<>();
        for (StandardEntity next : this.worldInfo) {
            if(StandardEntityURN.BUILDING.equals(next.getStandardURN())) {
                this.unexploredBuildings.add(next.getID());
            }
        }
    }

    @Override
    public TargetSelector<Building> updateInfo() {
        for (EntityID next : this.worldInfo.getChanged().getChangedEntities()) {
            this.unexploredBuildings.remove(next);
        }
        return this;
    }

    @Override
    public TargetSelector<Building> calc() {
        this.pathPlanner.setFrom(this.agentInfo.getPosition());
        List<EntityID> path = this.pathPlanner.setDist(this.unexploredBuildings).getResult();
        if (path != null) {
            this.result = path.get(path.size() - 1);
        }
        return this;
    }

    @Override
    public EntityID getTarget() {
        return this.result;
    }
}

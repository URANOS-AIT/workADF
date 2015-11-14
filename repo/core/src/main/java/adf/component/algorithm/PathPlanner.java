package adf.algorithm;


import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public abstract class PathPlanner {

    public ScenarioInfo scenarionInfo;
    public AgentInfo agentInfo;
    public WorldInfo worldInfo;

    public PathPlanner(WorldInfo wi, AgentInfo ai, ScenarioInfo si) {
        this.worldInfo = wi;
        this.agentInfo = ai;
        this.scenarionInfo = si;
    }

    public abstract List<EntityID> getResult();

    public abstract void setFrom(EntityID id);

    public abstract void setDist(EntityID... target);
}

package adf.component.algorithm.cluster;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.EntityID;

import java.util.Collection;

public abstract class Clustering{

    protected WorldInfo worldInfo;
    protected AgentInfo agentInfo;
    protected ScenarioInfo scenarioInfo;

    protected int clusterSize;

    public Clustering(WorldInfo wi, AgentInfo ai, ScenarioInfo si, Collection<EntityID> ids) {
        this(wi, ai, si, -1, ids);
    }

    public Clustering(WorldInfo wi, AgentInfo ai, ScenarioInfo si, int size, Collection<EntityID> ids) {
        this.worldInfo = wi;
        this.agentInfo = ai;
        this.scenarioInfo = si;
        this.clusterSize = size;
    }

    public Clustering calc() {
        return this;
    }

    public abstract int getClusterNumber();

    public abstract int getClusterIndex(EntityID id);

    public abstract Collection<EntityID> getClusterEntities(int index);

    //public abstract Collection<ClusterData> getClusters();
}

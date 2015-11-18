package adf.component.algorithm.cluster;

import adf.agent.Agent;
import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import rescuecore2.worldmodel.EntityID;

import java.util.Collection;
import java.util.List;

public abstract class Clustering {

    /*protected WorldInfo worldInfo;
    protected AgentInfo agentInfo;
    protected ScenarioInfo scenarioInfo;

    protected int clusterSize;

    public Clustering(WorldInfo wi, AgentInfo ai, ScenarioInfo si) {
        this(wi, ai, si, -1);
    }

    public Clustering(WorldInfo wi, AgentInfo ai, ScenarioInfo si, int size) {
        this.worldInfo = wi;
        this.agentInfo = ai;
        this.scenarioInfo = si;
        this.clusterSize = size;
    }*/

    public Clustering calc() {
        return this;
    }

    public abstract ClusterData getCluster(EntityID id);

    public abstract Collection<ClusterData> getClusters();
}

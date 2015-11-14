package adf.algorithm.cluster;

import adf.agent.Agent;
import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;

import java.util.List;

public abstract class Clustering {

    public Clustering(WorldInfo worldInfo, AgentInfo agentInfo, ScenarioInfo scenarioInfo) {

    }

    public Clustering(WorldInfo worldInfo, AgentInfo agentInfo, ScenarioInfo scenarioInfo, int size) {

    }

    public abstract List<ClusterData> getClusters();
}

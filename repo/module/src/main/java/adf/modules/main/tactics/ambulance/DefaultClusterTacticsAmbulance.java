package adf.modules.main.tactics.ambulance;


import adf.agent.action.Action;
import adf.agent.action.common.ActionMove;
import adf.agent.action.common.ActionRest;
import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.agent.precompute.PrecomputeData;
import adf.component.algorithm.cluster.Clustering;
import adf.component.algorithm.path.PathPlanner;
import adf.component.algorithm.target.TargetSelector;
import adf.component.tactics.TacticsAmbulance;
import adf.modules.main.algorithm.cluster.PathBasedKMeans;
import adf.modules.main.algorithm.cluster.StandardKMeans;
import adf.modules.main.algorithm.path.DefaultPathPlanner;
import adf.modules.main.algorithm.target.SearchBuildingSelector;
import adf.modules.main.algorithm.target.VictimSelector;
import adf.modules.main.extaction.ActionTransport;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class DefaultClusterTacticsAmbulance extends TacticsAmbulance {

    private PathPlanner pathPlanner;

    private TargetSelector<Human> victimSelector;
    private TargetSelector<Building> buildingSelector;

    private Clustering clustering;

    @Override
    public void initialize(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
    }

    @Override
    public void precompute(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeData precomputeData) {
        worldInfo.indexClass(
                StandardEntityURN.CIVILIAN,
                StandardEntityURN.FIRE_BRIGADE,
                StandardEntityURN.POLICE_FORCE,
                StandardEntityURN.AMBULANCE_TEAM,
                StandardEntityURN.REFUGE,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.GAS_STATION,
                StandardEntityURN.BUILDING
        );
        this.pathPlanner = new DefaultPathPlanner(worldInfo, agentInfo, scenarioInfo);
        this.victimSelector = new VictimSelector(worldInfo, agentInfo, scenarioInfo);
        this.buildingSelector = new SearchBuildingSelector(worldInfo, agentInfo, scenarioInfo, this.pathPlanner);
        this.clustering = new PathBasedKMeans(worldInfo, agentInfo, scenarioInfo, worldInfo.getEntitiesOfType(
                StandardEntityURN.ROAD,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.REFUGE,
                StandardEntityURN.BLOCKADE,
                StandardEntityURN.GAS_STATION
            )
        );
        this.pathPlanner.precompute(precomputeData);
        this.victimSelector.precompute(precomputeData);
        this.buildingSelector.precompute(precomputeData);
        this.clustering.precompute(precomputeData);
    }

    @Override
    public void resume(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeData precomputeData) {
        worldInfo.indexClass(
                StandardEntityURN.CIVILIAN,
                StandardEntityURN.FIRE_BRIGADE,
                StandardEntityURN.POLICE_FORCE,
                StandardEntityURN.AMBULANCE_TEAM,
                StandardEntityURN.REFUGE,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.GAS_STATION,
                StandardEntityURN.BUILDING
        );
        this.pathPlanner = new DefaultPathPlanner(worldInfo, agentInfo, scenarioInfo);
        this.victimSelector = new VictimSelector(worldInfo, agentInfo, scenarioInfo);
        this.buildingSelector = new SearchBuildingSelector(worldInfo, agentInfo, scenarioInfo, this.pathPlanner);
        this.pathPlanner.resume(precomputeData);
        this.victimSelector.resume(precomputeData);
        this.buildingSelector.resume(precomputeData);
        this.clustering = new PathBasedKMeans(worldInfo, agentInfo, scenarioInfo, null);
        this.clustering.resume(precomputeData);
    }

    @Override
    public void preparate(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        worldInfo.indexClass(
                StandardEntityURN.CIVILIAN,
                StandardEntityURN.FIRE_BRIGADE,
                StandardEntityURN.POLICE_FORCE,
                StandardEntityURN.AMBULANCE_TEAM,
                StandardEntityURN.REFUGE,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.GAS_STATION,
                StandardEntityURN.BUILDING
        );
        this.pathPlanner = new DefaultPathPlanner(worldInfo, agentInfo, scenarioInfo);
        this.victimSelector = new VictimSelector(worldInfo, agentInfo, scenarioInfo);
        this.buildingSelector = new SearchBuildingSelector(worldInfo, agentInfo, scenarioInfo, this.pathPlanner);
        this.clustering = new StandardKMeans(worldInfo, agentInfo, scenarioInfo, worldInfo.getEntitiesOfType(
                StandardEntityURN.ROAD,
                StandardEntityURN.HYDRANT,
                StandardEntityURN.REFUGE,
                StandardEntityURN.BLOCKADE,
                StandardEntityURN.GAS_STATION
            )
        );
        this.clustering.calc();
    }

    @Override
    public Action think(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        this.victimSelector.updateInfo();
        this.buildingSelector.updateInfo();
        this.pathPlanner.updateInfo();

        Human injured = this.someoneOnBoard(worldInfo, agentInfo);
        if (injured != null) {
            return new ActionTransport(worldInfo, agentInfo, this.pathPlanner, injured).calc().getAction();
        }

        // Go through targets (sorted by distance) and check for things we can do
        EntityID target = this.victimSelector.calc().getTarget();
        if(target != null) {
            Action action = new ActionTransport(worldInfo, agentInfo, this.pathPlanner, (Human)worldInfo.getEntity(target)).calc().getAction();
            if(action != null) {
                return action;
            }
        }

        // Nothing to do
        EntityID searchBuildingID = this.buildingSelector.calc().getTarget();
        if(searchBuildingID != null) {
            this.pathPlanner.setFrom(agentInfo.getPosition());
            List<EntityID> path = this.pathPlanner.setDist(searchBuildingID).getResult();
            if (path != null) {
                return new ActionMove(path);
            }
        }
        return new ActionRest();
    }

    private Human someoneOnBoard(WorldInfo worldInfo, AgentInfo agentInfo) {
        for (StandardEntity next : worldInfo.getEntitiesOfType(StandardEntityURN.CIVILIAN)) {
            Human human = (Human)next;
            if (human.getPosition().equals(agentInfo.getID())) {
                return human;
            }
        }
        return null;
    }
}
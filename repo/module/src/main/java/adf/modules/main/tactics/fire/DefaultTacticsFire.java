package adf.modules.main.tactics.fire;

import adf.agent.action.Action;
import adf.agent.action.common.ActionMove;
import adf.agent.action.common.ActionRest;
import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.agent.precompute.PrecomputeData;
import adf.component.algorithm.path.PathPlanner;
import adf.component.algorithm.target.TargetSelector;
import adf.component.tactics.TacticsFire;
import adf.modules.main.algorithm.path.DefaultPathPlanner;
import adf.modules.main.algorithm.target.BurningBuildingSelector;
import adf.modules.main.algorithm.target.SearchBuildingSelector;
import adf.modules.main.extaction.ActionFireFighting;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class DefaultTacticsFire extends TacticsFire{

    private int maxWater;

    private PathPlanner pathPlanner;
    private TargetSelector<Building> burningBuildingSelector;
    private TargetSelector<Building> searchBuildingSelector;

    @Override
    public void initialize(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
    }

    @Override
    public void precompute(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeData precomputeData) {

    }

    @Override
    public void resume(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeData precomputeData) {
        this.preparate(agentInfo, worldInfo, scenarioInfo);
    }

    @Override
    public void preparate(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        worldInfo.indexClass(StandardEntityURN.BUILDING, StandardEntityURN.REFUGE,StandardEntityURN.HYDRANT,StandardEntityURN.GAS_STATION);
        this.pathPlanner = new DefaultPathPlanner(worldInfo, agentInfo, scenarioInfo);
        this.burningBuildingSelector = new BurningBuildingSelector(worldInfo, agentInfo, scenarioInfo);
        this.searchBuildingSelector = new SearchBuildingSelector(worldInfo, agentInfo, scenarioInfo, this.pathPlanner);
        maxWater = scenarioInfo.getFireTankMaximum();
    }

    @Override
    public Action think(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        this.burningBuildingSelector.updateInfo();
        this.searchBuildingSelector.updateInfo();
        this.pathPlanner.updateInfo();

        FireBrigade me = (FireBrigade) agentInfo.me();
        // Are we currently filling with water?
        if (me.isWaterDefined() && me.getWater() < maxWater && agentInfo.getLocation() instanceof Refuge) {
            return new ActionRest();
        }
        // Are we out of water?
        if (me.isWaterDefined() && me.getWater() == 0) {
            // Head for a refuge
            this.pathPlanner.setFrom(agentInfo.getPosition());
            this.pathPlanner.setDist(worldInfo.getEntityIDsOfType(StandardEntityURN.REFUGE));
            List<EntityID> path = this.pathPlanner.getResult();
            if (path != null) {
                return new ActionMove(path);
            }
            EntityID searchBuildingID = this.searchBuildingSelector.calc().getTarget();
            if(searchBuildingID != null) {
                this.pathPlanner.setFrom(agentInfo.getPosition());
                path = this.pathPlanner.setDist(searchBuildingID).getResult();
                if (path != null) {
                    return new ActionMove(path);
                }
            }
            return new ActionRest();
        }

        // Find all buildings that are on fire
        EntityID target = this.burningBuildingSelector.calc().getTarget();
        if(target != null) {
            Action action = new ActionFireFighting(worldInfo, agentInfo, scenarioInfo, this.pathPlanner, target).calc().getAction();
            if(action != null) {
                return action;
            }
        }

        /////////////////////////////////////////////////////
        EntityID searchBuildingID = this.searchBuildingSelector.calc().getTarget();
        if(searchBuildingID != null) {
            this.pathPlanner.setFrom(agentInfo.getPosition());
            List<EntityID> path = this.pathPlanner.setDist(searchBuildingID).getResult();
            if (path != null) {
                return new ActionMove(path);
            }
        }
        return new ActionRest();
    }
}

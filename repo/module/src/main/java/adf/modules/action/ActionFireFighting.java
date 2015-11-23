package adf.modules.action;

import adf.agent.info.AgentInfo;
import adf.agent.info.WorldInfo;
import adf.agent.action.common.ActionMove;
import adf.agent.action.common.ActionRest;
import adf.agent.action.fire.ActionExtinguish;
import adf.component.action.extaction.ExtAction;
import adf.component.algorithm.path.PathPlanner;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ActionFireFighting extends ExtAction {

    private static final String MAX_DISTANCE_KEY = "fire.extinguish.max-distance";
    private static final String MAX_POWER_KEY = "fire.extinguish.max-sum";

    private WorldInfo worldInfo;
    private AgentInfo agentInfo;
    private PathPlanner pathPlanner;
    private int maxDistance;
    private int maxPower;
    private EntityID target;

    public ActionFireFighting(WorldInfo worldInfo, AgentInfo agentInfo, PathPlanner pathPlanner, EntityID target) {
        super();
        this.worldInfo = worldInfo;
        this.agentInfo = agentInfo;
        this.pathPlanner = pathPlanner;
        this.target = target;
        this.maxDistance = this.agentInfo.config.getIntValue(MAX_DISTANCE_KEY);
        this.maxPower = this.agentInfo.config.getIntValue(MAX_POWER_KEY);
    }

    @Override
    public ExtAction calc() {
        this.result = new ActionRest();
        if (worldInfo.world.getDistance(agentInfo.getID(), this.target) <= maxDistance) {
            this.result = new ActionExtinguish(this.target, maxPower);
        }
        else {
            List<EntityID> path = planPathToFire(this.target);
            if (path != null) {
                this.result = new ActionMove(path);
            }
        }
        return this;
    }

    private List<EntityID> planPathToFire(EntityID target) {
        // Try to get to anything within maxDistance of the target
        Collection<StandardEntity> targets = this.worldInfo.world.getObjectsInRange(target, maxDistance);
        if (targets.isEmpty()) {
            return null;
        }

        List<EntityID> cvtList = targets.stream().map(StandardEntity::getID).collect(Collectors.toList());
        this.pathPlanner.setFrom(this.agentInfo.getPosition());
        this.pathPlanner.setDist(cvtList);
        return this.pathPlanner.getResult();
    }
}

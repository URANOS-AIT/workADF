package adf.modules.action;

import adf.agent.info.AgentInfo;
import adf.agent.info.WorldInfo;
import adf.component.action.action.ambulance.ActionLoad;
import adf.component.action.action.ambulance.ActionRescue;
import adf.component.action.action.common.ActionMove;
import adf.component.action.action.common.ActionRest;
import adf.component.action.extaction.ExtAction;
import adf.component.algorithm.path.PathPlanner;
import rescuecore2.standard.entities.Civilian;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.Refuge;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class ActionTransport extends ExtAction {

    private AgentInfo agentInfo;
    private PathPlanner pathPlanner;

    private Human target;

    public ActionTransport(AgentInfo agentInfo, PathPlanner pathPlanner, Human target) {
        super();
        this.agentInfo = agentInfo;
        this.pathPlanner = pathPlanner;
        this.target = target;

    }

    @Override
    public ExtAction calc() {
        this.result = new ActionRest();
        if (target.getPosition().equals(agentInfo.getPosition())) {
            // Targets in the same place might need rescueing or loading
            if ((target instanceof Civilian) && target.getBuriedness() == 0 && !(agentInfo.getLocation() instanceof Refuge)) {
                this.result = new ActionLoad(target.getID());
            }
            else if (target.getBuriedness() > 0) {
                this.result = new ActionRescue(target.getID());
            }
        }
        else {
            this.pathPlanner.setFrom(agentInfo.getPosition());
            this.pathPlanner.setDist(target.getPosition());
            List<EntityID> path = this.pathPlanner.getResult();
            if (path != null) {
                this.result = new ActionMove(path);
            }
        }
        return this;
    }

}

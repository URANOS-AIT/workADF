package adf.modules.algorithm.target;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.target.TargetSelector;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.EntityID;
import sample.DistanceSorter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VictimSelector extends TargetSelector<Human>{

    public VictimSelector(WorldInfo wi, AgentInfo ai, ScenarioInfo si) {
        super(wi, ai, si);
    }

    @Override
    public EntityID getTarget() {
        List<Human> targets = new ArrayList<>();
        for (StandardEntity next : worldInfo.getEntitiesOfType(
                StandardEntityURN.CIVILIAN,
                StandardEntityURN.FIRE_BRIGADE,
                StandardEntityURN.POLICE_FORCE,
                StandardEntityURN.AMBULANCE_TEAM)
                ) {
            Human h = (Human)next;
            if (agentInfo.getID() == h.getID()) {
                continue;
            }
            if (h.isHPDefined()
                    && h.isBuriednessDefined()
                    && h.isDamageDefined()
                    && h.isPositionDefined()
                    && h.getHP() > 0
                    && (h.getBuriedness() > 0 || h.getDamage() > 0)) {
                targets.add(h);
            }
        }
        Collections.sort(targets, new DistanceSorter(worldInfo.getEntity(agentInfo.getPosition()), worldInfo.getRawWorld()));
        return targets.get(0).getID();
    }
}

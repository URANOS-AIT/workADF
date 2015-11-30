package adf.modules.main.algorithm.target;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.target.TargetSelector;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.List;

public class BurningBuildingSelector extends TargetSelector<Building> {

    private EntityID result;

    public BurningBuildingSelector(WorldInfo wi, AgentInfo ai, ScenarioInfo si) {
        super(wi, ai, si);
    }

    @Override
    public void update() {
    }

    @Override
    public TargetSelector<Building> calc() {
        List<Building> buildingList = new ArrayList<>();
        for (StandardEntity next : this.worldInfo.getEntitiesOfType(StandardEntityURN.BUILDING)) {
            if (next.getStandardURN().equals(StandardEntityURN.BUILDING)) {
                Building b = (Building)next;
                if (b.isOnFire()) {
                    buildingList.add(b);
                }
            }
        }
        // Sort by distance
        buildingList.sort(new DistanceSorter(this.worldInfo, this.agentInfo.getLocation()));
        this.result = buildingList.isEmpty() ? null : buildingList.get(0).getID();
        return this;
    }

    @Override
    public EntityID getTarget() {
        return this.result;
    }
}

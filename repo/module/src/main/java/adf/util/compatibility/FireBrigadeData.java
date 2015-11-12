package adf.util.compatibility;

import adf.util.compatibility.AgentData;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

public class FireBrigadeData extends AgentData<FireBrigade> {

    private FireBrigade fire;

    public int maxWater;
    public int maxDistance;
    public int maxPower;

    public FireBrigadeData(EntityID id, int water, int distance, int power) {
        super(id);
        this.maxWater = water;
        this.maxDistance = distance;
        this.maxPower = power;
    }

    @Override
    public void updateAgent(FireBrigade agentObject, StandardEntity currentPosition) {
        this.agent = agentObject;
        this.fire = agentObject;
        this.position = currentPosition;
    }

    @Override
    public FireBrigade getAgentObject() {
        return this.fire;
    }

    public int getWater() {
        return this.fire.getWater();
    }
}

package adf.util.compatibility;


import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

public class PoliceForceData extends AgentData<PoliceForce> {

    private PoliceForce police;

    public int maxDistance;

    public PoliceForceData(EntityID id, int maxDistance) {
        super(id);
        this.maxDistance = maxDistance;
    }

    @Override
    public void updateAgent(PoliceForce agentObject, StandardEntity currentPosition) {
        this.police = agentObject;
        this.agent = agentObject;
        this.position = currentPosition;
    }

    @Override
    public PoliceForce getAgentObject() {
        return this.police;
    }


}

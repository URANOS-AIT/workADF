package adf.util.compatibility;

import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.EntityID;

public class AmbulanceTeamData extends AgentData<AmbulanceTeam> {

    private AmbulanceTeam ambulance;

    public AmbulanceTeamData(EntityID id) {
        super(id);
    }

    @Override
    public void updateAgent(AmbulanceTeam agentObject, StandardEntity currentPosition) {
        this.ambulance = agentObject;
        this.agent = agentObject;
        this.position = currentPosition;
    }

    @Override
    public AmbulanceTeam getAgentObject() {
        return this.ambulance;
    }
}

package adf.util.compatibility;

import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.EntityID;

import java.util.Objects;

public class AgentData<H extends Human> {

    protected H agent;

    public EntityID agentID;

    public StandardEntity position;

    public int sightDistance;

    public AgentData(EntityID id) {
        this.agentID = id;
    }

    public void updateAgent(H agentObject, StandardEntity currentPosition) {
        this.agent = agentObject;
        this.position = currentPosition;
    }

    public EntityID getID() {
        return this.agentID;
    }

    public StandardEntity getPosition() {
        return this.position;
    }

    public int getX() {
        return this.agent.getX();
    }

    public int getY() {
        return this.agent.getY();
    }

    public H getAgentObject() {
        return this.agent;
    }

    public StandardEntityURN getStandardURN() {
        return this.agent.getStandardURN();
    }

    public boolean equals(Human agentObject) {
        return this.agent.getID().getValue() == agentObject.getID().getValue();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Human && this.equals((Human) obj);
    }
}

package adf.agent.info;

import adf.agent.Agent;
import rescuecore2.config.Config;
import rescuecore2.messages.Command;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.Collection;

public class AgentInfo {
	private Agent agent;
	private StandardWorldModel world;
	private Config config;
	private int time;
	private ChangeSet changed;
	private Collection<Command> heard;

	public AgentInfo(Agent agent, StandardWorldModel world, Config config) {
		this.agent = agent;
		this.world = world;
		this.config = config;
		this.time = 0;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public int getTime()
	{
		return this.time;
	}

	public void setHeard(Collection<Command> heard)
	{
		this.heard = heard;
	}

	public Collection<Command> getHeard()
	{
		return this.heard;
	}

	public EntityID getID()
	{
		return agent.getID();
	}

	public Human me() {
		return (Human) this.world.getEntity(this.agent.getID());
	}

	public double getX()
	{
		return agent.getX();
	}

	public double getY()
	{
		return agent.getY();
	}

	public EntityID getPosition()
	{
		return ((Human)this.world.getEntity(this.agent.getID())).getPosition();
	}

	public Area getLocation()
	{
		return (Area)this.world.getEntity(this.getPosition());
	}

	public void setChanged(ChangeSet changed)
	{
		this.changed = changed;
	}

    public ChangeSet getChanged() {
        return this.changed;
    }
}

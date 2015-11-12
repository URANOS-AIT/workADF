package adf.agent.info;

import adf.agent.Agent;
import comlib.manager.MessageManager;
import comlib.message.CommunicationMessage;
import rescuecore2.config.Config;
import rescuecore2.messages.Command;
import rescuecore2.messages.Message;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.Collection;
import java.util.List;

public class AgentInfo
{
	public Agent agent;
	public StandardWorldModel world;
	public Config config;
	public MessageManager messageManager;
	public int time;
	public ChangeSet changed;

	public AgentInfo(Agent agent, StandardWorldModel world, Config config, MessageManager messageManager)
	{
		this.agent = agent;
		this.world = world;
		this.config = config;
		this.messageManager = messageManager;
		this.time = 0;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public void setHeard(Collection<Command> heard)
	{
		this.messageManager.receiveMessage(time, heard);
	}

	public List<Message> createSendMessage()
	{
		return this.messageManager.createSendMessage(agent.getID());
	}


	public List<CommunicationMessage> getReceivedMessage()
	{
		return this.messageManager.getReceivedMessage();
	}

	public EntityID getID()
	{
		return agent.getID();
	}

	public double getX()
	{
		return agent.getX();
	}

	public double getY()
	{
		return agent.getY();
	}

	public EntityID getPosition() {
		return ((Human)this.world.getEntity(this.agent.getID())).getPosition();
	}

	public Area getLocation() {
		return (Area)this.world.getEntity(this.getPosition());
	}


	public void setChanged(ChangeSet changed) {
		this.changed = changed;
	}
}

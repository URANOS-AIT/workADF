package adf.agent.platoon.action.common;

import adf.agent.platoon.Platoon;
import adf.agent.platoon.action.Action;
import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKRest;
import rescuecore2.worldmodel.EntityID;

public class ActionRest extends Action
{
	public ActionRest()
	{
		super();
	}

	@Override
	public Message getCommand(EntityID agentID, int time)
	{
		return new AKRest(agentID, time);
	}
}
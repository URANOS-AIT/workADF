package adf.component.action.action.common;

import adf.component.action.action.Action;
import rescuecore2.messages.Message;
import rescuecore2.standard.messages.AKRest;
import rescuecore2.worldmodel.EntityID;

public class ActionDummy extends Action
{
	public ActionDummy()
	{
		super();
	}

	@Override
	public Message getCommand(EntityID agentID, int time)
	{
		return new AKRest(agentID, time);
	}
}

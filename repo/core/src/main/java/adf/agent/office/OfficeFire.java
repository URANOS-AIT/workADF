package adf.agent.office;

import adf.agent.info.AgentInfo;
import adf.control.Control;
import adf.control.ControlFire;
import adf.util.datastorage.DataStorage;
import comlib.manager.MessageManager;
import rescuecore2.messages.Command;
import rescuecore2.standard.entities.Building;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.ChangeSet;

import java.util.Collection;
import java.util.EnumSet;

public class OfficeFire extends Office<Building>
{
	public OfficeFire(ControlFire control, boolean isPrecompute)
	{
		super(control, isPrecompute, DATASTORAGE_FILE_NAME_FIRE);
	}

	@Override
	protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum()
	{
		return EnumSet.of(StandardEntityURN.FIRE_STATION);
	}

	@Override
	protected void postConnect()
	{
		super.postConnect();
	}
}

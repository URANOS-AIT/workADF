package adf.agent.platoon;

import adf.agent.info.AgentInfo;
import adf.tactics.Tactics;
import adf.tactics.TacticsFire;
import adf.util.datastorage.DataStorage;
import comlib.manager.MessageManager;
import rescuecore2.messages.Command;
import rescuecore2.standard.entities.FireBrigade;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.ChangeSet;

import java.util.*;

public class PlatoonFire extends Platoon<FireBrigade>
{
	public PlatoonFire(TacticsFire tactics, boolean isPrecompute)
	{
		super(tactics, isPrecompute, DATASTORAGE_FILE_NAME_FIRE);
	}

	@Override
	protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum()
	{
		return EnumSet.of(StandardEntityURN.FIRE_BRIGADE);
	}

	@Override
	protected void postConnect()
	{
		super.postConnect();
	}
}

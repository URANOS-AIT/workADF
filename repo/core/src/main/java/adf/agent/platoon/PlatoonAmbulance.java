package adf.agent.platoon;

import adf.agent.info.AgentInfo;
import adf.tactics.Tactics;
import adf.tactics.TacticsAmbulance;
import adf.util.datastorage.DataStorage;
import comlib.manager.MessageManager;
import rescuecore2.messages.Command;
import rescuecore2.standard.entities.AmbulanceTeam;
import rescuecore2.standard.entities.PoliceForce;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.worldmodel.ChangeSet;

import java.util.*;

/**
 * Created by takamin on 10/12/15.
 */
public class PlatoonAmbulance extends Platoon<AmbulanceTeam>
{
	public PlatoonAmbulance(TacticsAmbulance tactics, boolean isPrecompute)
	{
		super(tactics, isPrecompute, DATASTORAGE_FILE_NAME_AMBULANCE);
	}

	@Override
	protected EnumSet<StandardEntityURN> getRequestedEntityURNsEnum()
	{
		return EnumSet.of(StandardEntityURN.AMBULANCE_TEAM);
	}

	@Override
	protected void postConnect()
	{
		super.postConnect();
	}
}

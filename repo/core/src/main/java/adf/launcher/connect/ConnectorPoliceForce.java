package adf.launcher.connect;

import adf.agent.platoon.PlatoonPolice;
import adf.launcher.AbstractLoader;
import adf.launcher.ConfigKey;
import adf.tactics.TacticsPolice;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

public class ConnectorPoliceForce implements Connector
{

	@Override
	public void connect(ComponentLauncher launcher, Config config, AbstractLoader loader)
	{
		int count = config.getIntValue(ConfigKey.KEY_POLICE_FORCE_COUNT, 0);
		int connected = 0;

		if (count == 0)
		{
			return;
		}

		/*
		String classStr = config.getValue(ConfigKey.KEY_POLICE_FORCE_NAME);
		if (classStr == null)
		{
			System.out.println("[ERROR] Cannot Load PoliceForce Tactics !!");
			return;
		}
		System.out.println("[START] Connect PoliceForce (teamName:" + classStr + ")");
		System.out.println("[INFO ] Load PoliceForce (teamName:" + classStr + ")");
		*/

		try
		{
			if (loader.getTacticsPolice() == null)
			{
				System.out.println("[ERROR ] Cannot Load PoliceForce Tactics !!");
				return;
			}
			for (int i = 0; i != count; ++i)
			{
				TacticsPolice tacticsPolice = loader.getTacticsPolice();
				boolean isPrecompute = config.getBooleanValue(ConfigKey.KEY_PRECOMPUTE, false);
				launcher.connect(new PlatoonPolice(tacticsPolice, isPrecompute));
				//System.out.println(name);
				connected++;
			}
		}
		catch (ComponentConnectionException | InterruptedException | ConnectionException e)
		{
			//e.printStackTrace();
			System.out.println("[ERROR ] Cannot Load PoliceForce Tactics !!");
		}
		System.out.println("[FINISH] Connect PoliceForce (success:" + connected + ")");
	}
}

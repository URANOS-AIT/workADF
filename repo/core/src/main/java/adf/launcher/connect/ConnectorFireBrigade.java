package adf.launcher.connect;

import adf.agent.platoon.PlatoonFire;
import adf.launcher.AbstractLoader;
import adf.launcher.ConfigKey;
import adf.tactics.TacticsFire;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

public class ConnectorFireBrigade implements Connector
{

	@Override
	public void connect(ComponentLauncher launcher, Config config, AbstractLoader loader)
	{
		int count = config.getIntValue(ConfigKey.KEY_FIRE_BRIGADE_COUNT, 0);
		int connected = 0;

		if (count == 0)
		{
			return;
		}

		/*
		String classStr = config.getValue(ConfigKey.KEY_FIRE_BRIGADE_NAME);
		if (classStr == null)
		{
			System.out.println("[ERROR] Cannot Load FireBrigade Tactics !!");
			return;
		}
		System.out.println("[START] Connect FireBrigade (teamName:" + classStr + ")");
		System.out.println("[INFO ] Load FireBrigade (teamName:" + classStr + ")");
		*/

		try
		{
			if (loader.getTacticsFire() == null)
			{
				System.out.println("[ERROR ] Cannot Load FireBrigade Tactics !!");
				return;
			}
			for (int i = 0; i != count; ++i)
			{
				TacticsFire tacticsFire = loader.getTacticsFire();
				boolean isPrecompute = config.getBooleanValue(ConfigKey.KEY_PRECOMPUTE, false);
				launcher.connect(new PlatoonFire(tacticsFire, isPrecompute));
				//System.out.println(name);
				connected++;
			}
		}
		catch (ComponentConnectionException | InterruptedException | ConnectionException e)
		{
			//e.printStackTrace();
			System.out.println("[ERROR ] Cannot Load FireBrigade Tactics !!");
		}
		System.out.println("[FINISH] Connect FireBrigade (success:" + connected + ")");
	}
}

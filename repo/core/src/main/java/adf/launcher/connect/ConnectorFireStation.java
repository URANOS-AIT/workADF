package adf.launcher.connect;

import adf.agent.office.OfficeFire;
import adf.control.ControlFire;
import adf.launcher.AbstractLoader;
import adf.launcher.ConfigKey;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

public class ConnectorFireStation implements Connector
{

	@Override
	public void connect(ComponentLauncher launcher, Config config, AbstractLoader loader)
	{
		int count = config.getIntValue(ConfigKey.KEY_FIRE_STATION_COUNT, 0);
		int connected = 0;

		if (count == 0)
		{
			return;
		}

		/*
		String classStr = config.getValue(ConfigKey.KEY_FIRE_STATION_NAME);
		if (classStr == null)
		{
			System.out.println("[ERROR] Cannot Load FireStation Tactics !!");
			return;
		}
		System.out.println("[START] Connect FireStation (teamName:" + classStr + ")");
		System.out.println("[INFO ] Load FireStation (teamName:" + classStr + ")");
		*/

		try
		{
			if (loader.getControlFire() == null)
			{
				System.out.println("[ERROR ] Cannot Load FireStation Control !!");
				return;
			}
			for (int i = 0; i != count; ++i)
			{
				ControlFire controlFire = loader.getControlFire();
				boolean isPrecompute = config.getBooleanValue(ConfigKey.KEY_PRECOMPUTE, false);
				launcher.connect(new OfficeFire(controlFire, isPrecompute));
				//System.out.println(name);
				connected++;
			}
		}
		catch (ComponentConnectionException | InterruptedException | ConnectionException e)
		{
			//e.printStackTrace();
			System.out.println("[ERROR ] Cannot Load FireStation Control !!");
		}
		System.out.println("[FINISH] Connect FireStation (success:" + connected + ")");
	}
}

package adf.launcher.connect;

import adf.agent.office.OfficeAmbulance;
import adf.control.ControlAmbulance;
import adf.launcher.AbstractLoader;
import adf.launcher.ConfigKey;
import rescuecore2.components.ComponentConnectionException;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.connection.ConnectionException;

public class ConnectorAmbulanceCentre implements Connector
{
	@Override
	public void connect(ComponentLauncher launcher, Config config, AbstractLoader loader)
	{
		int count = config.getIntValue(ConfigKey.KEY_AMBULANCE_CENTRE_COUNT, 0);
		int connected = 0;

		if (count == 0)
		{
			return;
		}

		/*
		String classStr = config.getValue(ConfigKey.KEY_AMBULANCE_CENTRE_NAME);
		if (classStr == null)
		{
			System.out.println("[ERROR] Cannot Load AmbulanceCentre Control !!");
			return;
		}
		System.out.println("[START] Connect AmbulanceCentre (teamName:" + classStr + ")");
		System.out.println("[INFO ] Load AmbulanceCentre (teamName:" + classStr + ")");
		*/

		try
		{
			if (loader.getControlAmbulance() == null)
			{
				System.out.println("[ERROR ] Cannot Load AmbulanceCentre Control !!");
				return;
			}
			for (int i = 0; i != count; ++i)
			{
				ControlAmbulance controlAmbulance = loader.getControlAmbulance();
				boolean isPrecompute = config.getBooleanValue(ConfigKey.KEY_PRECOMPUTE, false);
				launcher.connect(new OfficeAmbulance(controlAmbulance, isPrecompute));
				//System.out.println(name);
				connected++;
			}
		}
		catch (ComponentConnectionException | InterruptedException | ConnectionException e)
		{
			//e.printStackTrace();
			System.out.println("[ERROR ] Cannot Load AmbulanceCentre Control !!");
		}
		System.out.println("[FINISH] Connect AmbulanceCentre (success:" + connected + ")");
	}
}

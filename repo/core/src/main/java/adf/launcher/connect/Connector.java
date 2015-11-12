package adf.launcher.connect;

import adf.launcher.AbstractLoader;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.config.Config;

import java.net.URLClassLoader;

public interface Connector
{
	public abstract void connect(ComponentLauncher launcher, Config config, AbstractLoader loader);
}

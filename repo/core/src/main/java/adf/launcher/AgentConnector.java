package adf.launcher;

import adf.launcher.connect.*;
import com.google.common.collect.Lists;
import rescuecore2.Constants;
import rescuecore2.components.ComponentLauncher;
import rescuecore2.components.TCPComponentLauncher;
import rescuecore2.config.Config;
import rescuecore2.registry.Registry;
import rescuecore2.standard.entities.StandardEntityFactory;
import rescuecore2.standard.entities.StandardPropertyFactory;
import rescuecore2.standard.messages.StandardMessageFactory;

import java.net.URLClassLoader;
import java.util.List;

public class AgentConnector {
    
    private Config config;
    
    private AbstractLoader loader;

    private List<Connector> connectors;

	public AgentConnector(String... args) throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException {
	    this.init(args);
	}
	
	private void init(String... args) throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException {
	    this.initSystem();
		this.config = ConfigInitializer.getConfig(args);
        this.initConnector();
	}
	
	private void initSystem() {
	    //register rescue system
		Registry.SYSTEM_REGISTRY.registerEntityFactory(StandardEntityFactory.INSTANCE);
		Registry.SYSTEM_REGISTRY.registerMessageFactory(StandardMessageFactory.INSTANCE);
		Registry.SYSTEM_REGISTRY.registerPropertyFactory(StandardPropertyFactory.INSTANCE);
	}

	private void initConnector() throws ClassNotFoundException, ClassCastException, InstantiationException, IllegalAccessException {
		URLClassLoader classLoader = (URLClassLoader)this.getClass().getClassLoader();
		Class c = classLoader.loadClass(this.config.getValue(ConfigKey.KEY_LOADER_CLASS));
		Object classObj = c.newInstance();
		this.loader = (AbstractLoader)classObj;
		// set connectors
		this.connectors = Lists.newArrayList(
                        new ConnectorAmbulanceTeam(),
                        new ConnectorFireBrigade(),
                        new ConnectorPoliceForce(),
                        new ConnectorAmbulanceCentre(),
                        new ConnectorFireStation(),
                        new ConnectorPoliceOffice()
		);
		//this.config.getArrayValue("test").forEach(System.out::println);
	}
	
	public void setConnector(Connector connector) {
	    this.connectors.add(connector);
	}

	public void start() {
		String host = this.config.getValue(Constants.KERNEL_HOST_NAME_KEY, Constants.DEFAULT_KERNEL_HOST_NAME);
		int port = this.config.getIntValue(Constants.KERNEL_PORT_NUMBER_KEY, Constants.DEFAULT_KERNEL_PORT_NUMBER);
		ComponentLauncher launcher = new TCPComponentLauncher(host, port, this.config);
		System.out.println("[START ] Connect Server (host:" + host + ", port:" + port + ")");
		
		for (Connector connector : this.connectors) {
			connector.connect(launcher, this.config, loader);
		}
		System.out.println("[END   ] Success Connect Server");

		if (this.config.getBooleanValue(ConfigKey.KEY_PRECOMPUTE, false)) {
			System.exit(0);
		}
	}
}

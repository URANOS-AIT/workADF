package adf.agent.info;

import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;

public class WorldInfo {
	public StandardWorldModel world;
	public ChangeSet changed;

	public WorldInfo(StandardWorldModel world) {
		this.setWorld(world);
	}

	public void setWorld(StandardWorldModel world)
	{
		this.world = world;
	}

	public StandardWorldModel getWorld() {
		return this.world;
	}

	public void setChanged(ChangeSet changed) {
		this.changed = changed;
	}
}

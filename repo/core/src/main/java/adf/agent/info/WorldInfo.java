package adf.agent.info;

import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

	public Collection<EntityID> convert(Collection<StandardEntity> entities) {
		List<EntityID> list = new ArrayList<>();
		for(StandardEntity entity : entities) {
			list.add(entity.getID());
		}
		return list;
	}

}

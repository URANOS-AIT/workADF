package adf.agent.info;

import com.infomatiq.jsi.Rectangle;
import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardEntityURN;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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

	public Collection<EntityID> getObjectsInRange(EntityID entity, int range) {
		return this.convert(this.world.getObjectsInRange(entity, range));
	}

	public Collection<EntityID> getObjectsInRange(StandardEntity entity, int range) {
		return this.convert(this.world.getObjectsInRange(entity, range));
	}

	public Collection<EntityID> getObjectsInRange(int x, int y, int range) {
		return this.convert(this.world.getObjectsInRange(x,y,range));
	}

	public Collection<EntityID> getObjectsInRectangle(int x1, int y1, int x2, int y2) {
		return this.convert(this.world.getObjectsInRectangle(x1, y1, x2, y2));
	}

	public Collection<EntityID> getEntitiesOfType(StandardEntityURN urn) {
		return this.convert(this.world.getEntitiesOfType(urn));
	}

	public Collection<EntityID> getEntitiesOfType(StandardEntityURN... urns) {
		return this.convert(this.world.getEntitiesOfType(urns));
	}
}

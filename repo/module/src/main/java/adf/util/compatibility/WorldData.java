package adf.util.compatibility;

import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.ChangeSet;
import rescuecore2.worldmodel.EntityID;

import java.util.*;
import java.util.stream.Collectors;

public class WorldData {

    private StandardWorldModel world;
    private int time;
    private ChangeSet changed;

    public WorldData(StandardWorldModel standardWorldModel, EntityID agentID) {
        this.world = standardWorldModel;
    }

    public void updateData(StandardWorldModel standardWorldModel, int currentTime, ChangeSet changeSet) {
        this.time = currentTime;
        this.changed = changeSet;
    }

    public StandardWorldModel getWorldObject() {
        return this.world;
    }

    public int getTime() {
        return this.time;
    }

    public ChangeSet getChanged() {
        return this.changed;
    }


    public void initEntityCollection(StandardEntityURN... urns) {
        this.world.indexClass(urns);
    }

    public Collection<StandardEntity> getObjectsInRange(EntityID entity, int range) {
        return this.world.getObjectsInRange(entity, range);
    }
    public Collection<StandardEntity> getObjectsInRange(StandardEntity entity, int range) {
        return this.world.getObjectsInRange(entity, range);
    }

    public Collection<StandardEntity> getObjectsInRange(int x, int y, int range) {
        return this.world.getObjectsInRange(x, y, range);
    }

    public Collection<StandardEntity> getObjectsInRectangle(int x1, int y1, int x2, int y2) {
        return this.world.getObjectsInRectangle(x1, y1, x2, y2);
    }

    public List<Refuge> getRefuges() {
        return this.world.getEntitiesOfType(StandardEntityURN.REFUGE).stream().map(entity -> (Refuge) entity).collect(Collectors.toList());
    }

    public Collection<StandardEntity> getBuildings() {
        return this.world.getEntitiesOfType(
                StandardEntityURN.BUILDING,
                StandardEntityURN.REFUGE,
                StandardEntityURN.AMBULANCE_CENTRE,
                StandardEntityURN.FIRE_STATION,
                StandardEntityURN.POLICE_OFFICE,
                StandardEntityURN.GAS_STATION
        );
    }

    public Collection<StandardEntity> getRoads() {
        return this.world.getEntitiesOfType(
                StandardEntityURN.ROAD,
                StandardEntityURN.HYDRANT
        );
    }

    public Collection<StandardEntity> getAllEntities() {
        return this.world.getAllEntities();
    }

    public StandardEntity getEntity(EntityID id) {
        return this.world.getEntity(id);
    }

    /**
     * Get all entities of a particular type.
     *
     * @param urn The type urn to look up.
     * @return A new Collection of entities of the specified type.
     */
    public Collection<StandardEntity> getEntitiesOfType(StandardEntityURN urn) {
        return this.world.getEntitiesOfType(urn);
    }

    /**
     * Get all entities of a set of types.
     *
     * @param urns The type urns to look up.
     * @return A new Collection of entities of the specified types.
     */
    public Collection<StandardEntity> getEntitiesOfType(StandardEntityURN... urns) {
        return this.world.getEntitiesOfType(urns);
    }

    public Pair<Integer, Integer> getLocation(StandardEntity entity) {
        return entity.getLocation(this.world);
    }

    public StandardEntity getPosition(Human human) {
        return human.getPosition(this.world);
    }

    public StandardEntity getPosition(AgentData agentData) {
        return agentData.getAgentObject().getPosition(this.world);
    }

    public Area getPosition(Blockade blockade) {
        return (Area)this.world.getEntity(blockade.getPosition());
    }

    public Pair<Integer, Integer> getLocation(Area area) {
        return area.getLocation(this.world);
    }

    public Pair<Integer, Integer> getLocation(Human human) {
        return human.getLocation(this.world);
    }

    public Pair<Integer, Integer> getLocation(Blockade blockade) {
        return blockade.getLocation(this.world);
    }
}

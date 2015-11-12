package adf.util.fire;

import adf.util.compatibility.WorldData;
import rescuecore2.config.Config;
import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

public class Reservoir {
    //maxDistance
    public static final String KEY_MAX_DISTANCE = "fire.extinguish.max-maxDistance";
    //use water value
    public static final String KEY_MAX_POWER = "fire.extinguish.max-sum";
    //fire.tank.maximum:7500
    public static final String KEY_WATER_CAPACITY = "fire.tank.maximum";
    //fire.tank.refill_rate: 500
    public static final String KEY_REFILL_REFUGE = "fire.tank.refill-rate";
    //fire.tank.refill_hydrant_rate: 150
    public static final String KEY_REFILL_HYDRANT = "fire.tank.refill_hydrant_rate";

    private Area area;
    private EntityID id;
    private int supply;
    Pair<Integer, Integer> location;

    //private Refuge fire;
    //private Hydrant hydrant;

    private Reservoir(Refuge station, WorldData world, Config config) {
        //this.fire = station;
        this.area = station;
        this.id = station.getID();
        this.supply = config.getIntValue(KEY_REFILL_REFUGE, 500);
        this.location = world.getLocation(station);
    }

    private Reservoir(Hydrant station, WorldData world, Config config) {
        //this.hydrant = station;
        this.area = station;
        this.id = station.getID();
        this.supply = config.getIntValue(KEY_REFILL_HYDRANT, 150);
        this.location = world.getLocation(station);
    }

    public static Reservoir getInstance(StandardEntity station, WorldData world, Config config) {
        if(station == null) {
            return null;
        }
        if(station instanceof Refuge) {
            return new Reservoir((Refuge)station, world, config);
        }
        if(station instanceof Hydrant) {
            return new Reservoir((Hydrant)station, world, config);
        }
        return null;
    }

    public EntityID getID() {
        return this.id;
    }

    public int getSupply() {
        return this.supply;
    }

    public Area getArea() {
        return this.area;
    }

    public Pair<Integer, Integer> getLocation() {
        return this.location;
    }
}

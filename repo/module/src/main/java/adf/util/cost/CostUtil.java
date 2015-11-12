package adf.util.cost;

import adf.util.compatibility.WorldData;
import adf.util.map.DistanceUtil;
import adf.util.fire.Reservoir;
import rescuecore2.standard.entities.Human;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class CostUtil {
    public static final double DEFAULT_AVERAGE = 37615.0D;

    public static int getStep(WorldData world, List<EntityID> path) {
        return getStep(world.getWorldObject(), path);
    }

    public static int getStep(StandardWorldModel world, List<EntityID> path) {
        double distance = DistanceUtil.getPathDistance(world, path);
        return (int)Math.ceil(distance / DEFAULT_AVERAGE);
    }

    public static int getStep(Reservoir reservoir, int water) {
        return (int)Math.ceil((double)water / (double)reservoir.getSupply());
    }

    public static int getStep(Human human) {
        int hp = human.getHP();
        int damage = human.getDamage();
        int step = hp / damage;
        if(hp % damage != 0) {
            step++;
        }
        return step;
    }
}

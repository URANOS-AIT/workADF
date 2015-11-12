package adf.util.map;

import adf.util.compatibility.WorldData;
import rescuecore2.misc.Pair;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.List;

public class DistanceUtil {
    /**
     * 2つの座標から距離を求めます．
     *
     * @param positionX 1つ目の座標のX成分
     * @param positionY 1つ目の座標のY成分
     * @param anotherX 2つ目の座標のX成分
     * @param anotherY 2つ目の座標のY成分
     * @return 2つの座標間の距離
     */
    public static double getDistance(double positionX, double positionY, double anotherX, double anotherY) {
        double dx = positionX - anotherX;
        double dy = positionY - anotherY;
        return Math.hypot(dx, dy);
    }

    /**
     * 2つの座標から距離を求めます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @return 2つの座標間の距離
     */
    public static double getDistance(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        return getDistance(position.first(), position.second(), another.first(), another.second());
    }

    /**
     * 2つの座標から距離を求めます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @return 2つの座標間の距離
     */
    public static double getDistance(Point2D position, Point2D another) {
        return getDistance(position.getX(), position.getY(), another.getX(), another.getY());
    }

    /**
     * 2つのEdgeの直線距離を求めます．
     *
     * @param position 1つ目のEdge
     * @param another 2つ目のEdge
     * @see rescuecore2.standard.entities.Area
     * @see rescuecore2.standard.entities.Edge
     * @return 2つのEdge間の直線距離
     */
    public static double getDistance(Edge position, Edge another) {
        return getDistance(PositionUtil.getEdgePoint(position), PositionUtil.getEdgePoint(another));
    }

    /**
     * 2つの座標から距離を求めます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @return 2つの座標間の距離
     */
    public static double getDistance(Pair<Integer, Integer> position, Point2D another) {
        return getDistance(position.first(), position.second(), another.getX(), another.getY());
    }

    /**
     * Get the maxDistance between two entities.
     *
     * @param world World Object
     * @param first  The ID of the first entity.
     * @param second The ID of the second entity.
     * @return The maxDistance between the two entities. A negative value indicates that one or both objects either doesn't exist or could not be located.
     */
    public static double getDistance(WorldData world, EntityID first, EntityID second) {
        return getDistance(world.getWorldObject(), first, second);
    }

    /**
     * Get the maxDistance between two entities.
     *
     * @param world World Object
     * @param first  The ID of the first entity.
     * @param second The ID of the second entity.
     * @return The maxDistance between the two entities. A negative value indicates that one or both objects either doesn't exist or could not be located.
     */
    public static double getDistance(StandardWorldModel world, EntityID first, EntityID second) {
        StandardEntity a = world.getEntity(first);
        StandardEntity b = world.getEntity(second);
        if (a == null || b == null) {
            return -1;
        }
        return getDistance(world, a, b);
    }

    /**
     * Get the maxDistance between two entities.
     *
     * @param world  World Object
     * @param first  The first entity.
     * @param second The second entity.
     * @return The maxDistance between the two entities. A negative value indicates that one or both objects could not be located.
     */
    public static double getDistance(WorldData world, StandardEntity first, StandardEntity second) {
        return getDistance(world.getWorldObject(), first, second);
    }

    /**
     * Get the maxDistance between two entities.
     *
     * @param world  World Object
     * @param first  The first entity.
     * @param second The second entity.
     * @return The maxDistance between the two entities. A negative value indicates that one or both objects could not be located.
     */
    public static double getDistance(StandardWorldModel world, StandardEntity first, StandardEntity second) {
        Pair<Integer, Integer> a = first.getLocation(world);
        Pair<Integer, Integer> b = second.getLocation(world);
        if (a == null || b == null) {
            return -1;
        }
        return getDistance(a, b);
    }

    /**
     * 比較のための距離を求めます．<br>
     * {@link Math#hypot(double, double)}を行わず，Xの差，Yの差をそれぞれ2乗し足しあわせています．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @return 2つの座標間の距離
     */
    public static long valueOfCompare(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        long dx = position.first() - another.first();
        long dy = position.second() - another.second();
        return dx*dx + dy*dy;
    }

    /**
     * 比較のための距離を求めます．<br>
     * {@link Math#hypot(double, double)}を行わず，Xの差，Yの差をそれぞれ2乗し足しあわせています．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @return 2つの座標間の距離
     */
    public static double valueOfCompare(Point2D position, Point2D another) {
        return valueOfCompare(position.getX(), position.getY(), another.getX(), another.getY());
    }

    /**
     * 比較のための距離を求めます．<br>
     * {@link Math#hypot(double, double)}を行わず，Xの差，Yの差をそれぞれ2乗し足しあわせています．
     *
     * @param positionX 1つ目の座標のX成分
     * @param positionY 1つ目の座標のY成分
     * @param anotherX 2つ目の座標のX成分
     * @param anotherY 2つ目の座標のY成分
     * @return 2つの座標間の距離
     */
    public static double valueOfCompare(double positionX, double positionY, double anotherX, double anotherY) {
        double dX = positionX - anotherX;
        double dY = positionY - anotherY;
        return (dX * dX) + (dY * dY);
    }

    /**
     * 移動パスの距離を求めます．
     *
     * @param world World
     * @param path 移動パス
     * @return 移動パスの距離
     */
    public static double getPathDistance(WorldData world, List<EntityID> path) {
        return getPathDistance(world.getWorldObject(), path);
    }

    /**
     * 移動パスの距離を求めます．
     *
     * @param world World
     * @param path 移動パス
     * @return 移動パスの距離
     */
    public static double getPathDistance(StandardWorldModel world, List<EntityID> path) {
        double distance = 0.0D;
        int limit = path.size() - 1;

        Area area = (Area)world.getEntity(path.get(0));
        distance += getDistance(area.getLocation(world), PositionUtil.getEdgePoint(area.getEdgeTo(path.get(1))));
        area = (Area)world.getEntity(path.get(limit));
        distance += getDistance(area.getLocation(world), PositionUtil.getEdgePoint(area.getEdgeTo(path.get(limit - 1))));

        EntityID areaID;
        for(int i = 1; i < limit; i++) {
            areaID = path.get(i);
            area = (Area)world.getEntity(areaID);
            distance += getDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
        }
        return distance;
    }

    /**
     * Humanの実際に移動した距離を求めます．
     *
     * @param human 移動したHuman
     * @return 移動距離
     */
    public static double getMoveDistance(Human human) {
        int[] array = human.getPositionHistory();
        double result = 0.0D;
        int limit = array.length - 2;
        for(int i = 0; i < limit; i+=2) {
            result += getDistance(array[i], array[i + 1], array[i + 2], array[i + 3]);
        }
        return result;
    }
}

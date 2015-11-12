package adf.util.map;


import rescuecore2.misc.Pair;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.Edge;

import java.util.Collection;

public class PositionUtil {

    /**
     * 2つの座標が誤差を含め同一か判定します．<br>
     * このメソッドは{@link #equalsPoint(Pair, Pair, double)}の誤差を1000.0Dとして判定を行っています．<br>
     * エージェントの移動判定などに使用できます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @return 誤差を含め同一座標ならtrue
     */
    public static boolean equalsPoint(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        return equalsPoint(position, another, 1000.0D);
    }

    /**
     * 2つの座標が誤差を含め同一か判定します．<br>
     * このメソッドは{@link #equalsPoint(double, double, double, double, double)}を使用しています．<br>
     * エージェントの移動判定などに使用できます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @param range 2つの地点の誤差
     * @return 誤差を含め同一座標ならtrue
     */
    public static boolean equalsPoint(Pair<Integer, Integer> position, Pair<Integer, Integer> another, double range) {
        return equalsPoint(position.first(), position.second(), another.first(), another.second(), range);
    }

    /**
     * 2つの座標が誤差を含め同一か判定します．<br>
     * このメソッドは{@link #equalsPoint(Point2D, Point2D, double)}の誤差を1000.0Dとして判定を行っています．<br>
     * エージェントの移動判定などに使用できます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @return 誤差を含め同一座標ならtrue
     */
    public static boolean equalsPoint(Point2D position, Point2D another) {
        return equalsPoint(position, another, 1000.0D);
    }

    /**
     * 2つの座標が誤差を含め同一か判定します．<br>
     * このメソッドは{@link #equalsPoint(double, double, double, double, double)}を使用しています．<br>
     * エージェントの移動判定などに使用できます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @param range 2つの地点の誤差
     * @return 誤差を含め同一座標ならtrue
     */
    public static boolean equalsPoint(Point2D position, Point2D another, double range) {
        return equalsPoint(position.getX(), position.getY(), another.getX(), another.getY(), range);
    }

    /**
     * 2つの座標が誤差を含め同一か判定します．<br>
     * このメソッドは{@link #equalsPoint(Pair, Point2D, double)}の誤差を1000.0Dとして判定を行っています．<br>
     * エージェントの移動判定などに使用できます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @return 誤差を含め同一座標ならtrue
     */
    public static boolean equalsPoint(Pair<Integer, Integer> position, Point2D another) {
        return equalsPoint(position, another, 1000.0D);
    }

    /**
     * 2つの座標が誤差を含め同一か判定します．<br>
     * このメソッドは{@link #equalsPoint(double, double, double, double, double)}を使用しています．<br>
     * エージェントの移動判定などに使用できます．
     *
     * @param position 1つ目の座標
     * @param another 2つ目の座標
     * @param range 2つの地点の誤差
     * @return 誤差を含め同一座標ならtrue
     */
    public static boolean equalsPoint(Pair<Integer, Integer> position, Point2D another, double range) {
        return equalsPoint(position.first(), position.second(), another.getX(), another.getY(), range);
    }

    /**
     * 2つの座標が誤差を含め同一か判定します．<br>
     * このメソッドは{@link #equalsPoint(double, double, double, double, double)}の誤差を1000.0Dとして判定を行っています．<br>
     * エージェントの移動判定などに使用できます．
     *
     * @param positionX 1つ目の座標のX成分
     * @param positionY 1つ目の座標のY成分
     * @param anotherX 2つ目の座標のX成分
     * @param anotherY 2つ目の座標のY成分
     * @return 誤差を含め同一座標ならtrue
     */
    public static boolean equalsPoint(double positionX, double positionY, double anotherX, double anotherY) {
        return equalsPoint(positionX, positionY, anotherX, anotherY, 1000.0D);
    }

    /**
     * 2つの座標が誤差を含め同一か判定します．<br>
     * エージェントの移動判定などに使用できます．
     *
     * @param positionX 1つ目の座標のX成分
     * @param positionY 1つ目の座標のY成分
     * @param anotherX 2つ目の座標のX成分
     * @param anotherY 2つ目の座標のY成分
     * @param range 2つの地点の誤差
     * @return 誤差を含め同一座標ならtrue
     */
    public static boolean equalsPoint(double positionX, double positionY, double anotherX, double anotherY, double range) {
        return Double.compare(positionX, anotherX + range) <= 0
                && Double.compare(positionX, anotherX - range) >= 0
                && Double.compare(positionY, anotherY + range) <= 0
                && Double.compare(positionY, anotherY - range) >= 0;
    }

    /**
     * Areaを形作るEdgeの中点を計算します．
     *
     * @param edge 中点を求めるEdge
     * @see rescuecore2.standard.entities.Area
     * @see rescuecore2.standard.entities.Edge
     * @return Edgeの中点
     */
    public static Point2D getEdgePoint(Edge edge) {
        Point2D start = edge.getStart();
        Point2D end = edge.getEnd();
        return new Point2D(((start.getX() + end.getX()) / 2.0D), ((start.getY() + end.getY()) / 2.0D));
    }

    public static Pair<Integer, Integer> compareDistance(Pair<Integer, Integer> position, Pair<Integer, Integer> first, Pair<Integer, Integer> second) {
        return (DistanceUtil.valueOfCompare(position, first) <= DistanceUtil.valueOfCompare(position, second)) ? first : second;
    }

    public static Pair<Integer, Integer> getNearPosition(Pair<Integer, Integer> position, Collection<Pair<Integer, Integer>> targets) {
        Pair<Integer, Integer> result = null;
        for(Pair<Integer, Integer> target : targets) {
            result = (result != null) ? compareDistance(position, result, target) : target;
        }
        return result;
    }

    public static Point2D compareDistance(Point2D position, Point2D first, Point2D second) {
        return (DistanceUtil.valueOfCompare(position, first) <= DistanceUtil.valueOfCompare(position, second)) ? first : second;
    }

    public static Point2D getNearPosition(Point2D position, Collection<Point2D> targets) {
        Point2D result = null;
        for(Point2D target : targets) {
            result = (result != null) ? compareDistance(position, result, target) : target;
        }
        return result;
    }
}

package adf.util.fire;

import adf.util.map.DistanceUtil;
import rescuecore2.misc.Pair;

import java.util.Comparator;

public class ReservoirComparator implements Comparator<Reservoir>{

    private Pair<Integer, Integer> position;

    public ReservoirComparator(Pair<Integer, Integer> selfPosition) {
        this.position = selfPosition;
    }

    @Override
    public int compare(Reservoir o1, Reservoir o2) {
        return (int)(DistanceUtil.valueOfCompare(this.position, o1.getLocation()) - DistanceUtil.valueOfCompare(this.position, o2.getLocation()));
    }
}

package adf.util.fire;

import adf.util.compatibility.AgentData;
import adf.util.compatibility.WorldData;
import adf.util.compatibility.provider.RouteSearcherProvider;
import adf.util.cost.CostUtil;

import rescuecore2.config.Config;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class ReservoirSelector {

    private WorldData world;
    private Config config;
    private RouteSearcherProvider provider;

    public static final int DEFAULT_COUNT = 3;
    public int count;

    private List<Reservoir> stations;

    public ReservoirSelector(WorldData worldData, Config c, RouteSearcherProvider routeSearcherProvider) {
        this(worldData, c, routeSearcherProvider, DEFAULT_COUNT);
    }

    public ReservoirSelector(WorldData worldData, Config c, RouteSearcherProvider routeSearcherProvider, int operationCount) {
        this.world = worldData;
        this.config = c;
        this.provider = routeSearcherProvider;
        this.count = operationCount;
        this.initList();
    }

    private void initList() {
        this.stations = new ArrayList<>();
        for(StandardEntity entity : this.world.getEntitiesOfType(StandardEntityURN.REFUGE, StandardEntityURN.HYDRANT)) {
            this.stations.add(Reservoir.getInstance(entity, this.world, this.config));
        }
    }

    /*public void updateAverage(int time, Human human) {
        int[] array = human.getPositionHistory();
        if(array.length >= 4) {
            double maxDistance = DistanceUtil.getMoveDistance(human);
            if (firstUpdate) {
                this.averageDistance = maxDistance;
                this.firstUpdate = false;
            } else {
                this.averageDistance = ((this.averageDistance * (time - 1)) + maxDistance) / time;
            }
        }
    }

    public void updateAverage(int time, double maxDistance) {
        if(firstUpdate) {
            this.averageDistance = maxDistance;
            firstUpdate = false;
        }
        else {
            this.averageDistance = ((this.averageDistance * (time - 1)) + maxDistance) / time;
        }
    }

    public void resetAverage(double maxDistance) {
        this.averageDistance = maxDistance;
    }*/

    public ReservoirResult selectByWater(int time, AgentData agentData, int water) {
        return this.selectByWater(time, (Area) agentData.getPosition(), water);
    }

    public ReservoirResult selectByWater(int time, Area position, int water) {
        if(this.stations.isEmpty()) {
            this.initList();
            if(this.stations.isEmpty()) {
                return null;
            }
        }
        this.stations.sort(new ReservoirComparator(this.world.getLocation(position)));
        if(position.getID().getValue() == this.stations.get(0).getID().getValue()) {
            Reservoir reservoir = this.stations.get(0);
            List<EntityID> path = new ArrayList<>();
            path.add(position.getID());
            return new ReservoirResult(reservoir, water, (int)Math.ceil((double)water / (double)reservoir.getSupply()), path);
        }
        ReservoirResult best = null;
        List<EntityID> path;
        int step = Integer.MAX_VALUE;
        int c = this.count;
        for(int i = 0; i < c; i++) {
            if(i == this.stations.size()) {
                return best;
            }
            Reservoir station = this.stations.get(i);
            path = this.provider.getRouteSearcher().getFullPath(time, position.getID(), station.getArea());
            if(path == null || path.isEmpty()) {
                c++;
                continue;
            }
            int s = CostUtil.getStep(world, path);
            s += CostUtil.getStep(station, water);
            if(step > s) {
                best = new ReservoirResult(station, water, s, path);
                step = s;
            }
        }
        return best;
    }

    public ReservoirResult selectByStep(int time, AgentData agentData, int step) {
        return this.selectByStep(time, (Area)agentData.getPosition(), step);
    }

    public ReservoirResult selectByStep(int time, Area position, int step) {
        if(this.stations.isEmpty()) {
            this.initList();
            if(this.stations.isEmpty()) {
                return null;
            }
        }
        this.stations.sort(new ReservoirComparator(this.world.getLocation(position)));
        if(position.getID().getValue() == this.stations.get(0).getID().getValue()) {
            Reservoir reservoir = this.stations.get(0);
            List<EntityID> path = new ArrayList<>();
            path.add(position.getID());
            return new ReservoirResult(reservoir, step * reservoir.getSupply(), step, path);
        }
        ReservoirResult best = null;
        List<EntityID> path;
        int water = -1;
        int c = this.count;
        for(int i = 0; i < c; i++) {
            if(i == this.stations.size()) {
                return best;
            }
            Reservoir station = this.stations.get(i);
            path = this.provider.getRouteSearcher().getFullPath(time, position.getID(), station.getArea());
            if(path == null || path.isEmpty()) {
                c++;
                continue;
            }
            int s = CostUtil.getStep(world, path);
            if(step < s) {
                continue;
            }
            int w = (step - s) * station.getSupply();
            if(water < w) {
                best = new ReservoirResult(station, w, step, path);
                water = w;
            }
        }
        return best;
    }
}

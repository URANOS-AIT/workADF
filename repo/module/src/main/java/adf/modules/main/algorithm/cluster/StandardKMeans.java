package adf.modules.main.algorithm.cluster;


import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.cluster.Clustering;
import rescuecore2.config.Config;
import rescuecore2.config.ConfigException;
import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class StandardKMeans extends Clustering {

    List<StandardEntity> centerEntityList;
    List<List<StandardEntity>> clusterList;

    private int repeat = 50;

    public StandardKMeans(WorldInfo wi, AgentInfo ai, ScenarioInfo si, Collection<StandardEntity> elements) {
        super(wi, ai, si, elements);
    }

    public StandardKMeans(WorldInfo wi, AgentInfo ai, ScenarioInfo si, Collection<StandardEntity> elements, int size) {
        super(wi, ai, si, elements, size);
    }

    @Override
    public int getClusterIndex(StandardEntity entity) {
        for(int i = 0; i < this.clusterSize; i++) {
            if(this.clusterList.get(i).contains(entity)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getClusterIndex(EntityID id) {
        return this.getClusterIndex(this.worldInfo.getEntity(id));
    }

    @Override
    public Collection<StandardEntity> getClusterEntities(int index) {
        return this.clusterList.get(index);
    }

    @Override
    public Collection<EntityID> getClusterEntityIDs(int index) {
        return this.worldInfo.convertToID(this.getClusterEntities(index));
    }

    @Override
    public Clustering calc() {
        Config config = new Config();
        try {
            config.read(new File("naito15.cfg"));
        } catch (ConfigException e) {
            e.printStackTrace();
        }
        repeat = config.getIntValue("naito15.clustering.standardkmeans.repeat", 50);

        Random random = new Random();

        List<StandardEntity> entityList = new ArrayList<>(this.entities);
        this.centerEntityList = new ArrayList<>(this.clusterSize);
        this.clusterList = new ArrayList<>(this.clusterSize);

        //init list
        for (int index = 0; index < this.clusterSize; index++) {
            clusterList.add(index, new ArrayList<>());
            centerEntityList.add(index, entityList.get(0));
        }
        System.out.println("Cluster : " + this.clusterSize);
        //init center
        for (int index = 0; index < this.clusterSize; index++) {
            StandardEntity centerEntity;
            do {
                centerEntity = entityList.get(Math.abs(random.nextInt()) % entityList.size());
            } while (centerEntityList.contains(centerEntity));
            centerEntityList.set(index, centerEntity);
        }
        //calc center
        for (int i = 0; i < repeat; i++) {
            clusterList.clear();
            for (int index = 0; index < this.clusterSize; index++) {
                clusterList.add(index, new ArrayList<>());
            }
            for (StandardEntity entity : entityList) {
                StandardEntity tmp = this.getNearEntityByLine(this.worldInfo.getRawWorld(), centerEntityList, entity);
                clusterList.get(centerEntityList.indexOf(tmp)).add(entity);
            }
            for (int index = 0; index < this.clusterSize; index++) {
                int sumX = 0, sumY = 0;
                for (StandardEntity entity : clusterList.get(index)) {
                    Pair<Integer, Integer> location = entity.getLocation(this.worldInfo.getRawWorld());
                    sumX += location.first();
                    sumY += location.second();
                }
                int centerX = sumX / clusterList.get(index).size();
                int centerY = sumY / clusterList.get(index).size();
                centerEntityList.set(index, getNearEntityByLine(this.worldInfo.getRawWorld(), clusterList.get(index), centerX, centerY));
            }
            System.out.printf("*");
        }
        System.out.println();
        //set entity
        clusterList.clear();
        for (int index = 0; index < this.clusterSize; index++) {
            clusterList.add(index, new ArrayList<>());
        }
        for (StandardEntity entity : entityList) {
            StandardEntity tmp = this.getNearEntityByLine(worldInfo.getRawWorld(), centerEntityList, entity);
            clusterList.get(centerEntityList.indexOf(tmp)).add(entity);
        }

        //kMeansClusterList.sort(comparing(x -> x.getAreaIDList().size(), reverseOrder()));
        return this;
    }

    protected StandardEntity getNearEntityByLine(StandardWorldModel world, List<StandardEntity> srcEntityList, StandardEntity targetEntity) {
        Pair<Integer, Integer> location = targetEntity.getLocation(world);
        return this.getNearEntityByLine(world, srcEntityList, location.first(), location.second());
    }

    protected StandardEntity getNearEntityByLine(StandardWorldModel world, List<StandardEntity> srcEntityList, int targetX, int targetY) {
        StandardEntity result = null;
        for(StandardEntity entity : srcEntityList) {
            result = ((result != null) ? this.compareLineDistance(world, targetX, targetY, result, entity) : entity);
        }
        return result;
    }

    private StandardEntity compareLineDistance(StandardWorldModel world, int targetX, int targetY, StandardEntity first, StandardEntity second)
    {
        double firstDistance = getDistance(first.getLocation(world).first(), first.getLocation(world).second(), targetX, targetY);
        double secondDistance = getDistance(second.getLocation(world).first(), second.getLocation(world).second(), targetX, targetY);
        return (firstDistance < secondDistance ? first : second);
    }

    public double getDistance(double fromX, double fromY, double toX, double toY) {
        double dx = fromX - toX;
        double dy = fromY - toY;
        return Math.hypot(dx, dy);
    }
}

package adf.modules.main.algorithm.cluster;


import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.agent.precompute.PrecomputeData;
import adf.component.algorithm.cluster.Clustering;
import adf.util.WorldUtil;
import rescuecore2.misc.Pair;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public class StandardKMeans extends Clustering {
    public static final String KEY_ALL_ELEMENTS = "default.clustering.elements";
    public static final String KEY_CLUSTER_SIZE = "default.clustering.size";
    public static final String KEY_CLUSTER_CENTER = "default.clustering.centers";
    public static final String KEY_CLUSTER_ENTITY = "default.clustering.entities.";

    protected List<StandardEntity> centerList;
    protected List<List<StandardEntity>> clusterEntityList;

    private int repeat = 50;

    public StandardKMeans(WorldInfo wi, AgentInfo ai, ScenarioInfo si, Collection<StandardEntity> elements) {
        super(wi, ai, si, elements);
    }

    public StandardKMeans(WorldInfo wi, AgentInfo ai, ScenarioInfo si, Collection<StandardEntity> elements, int size) {
        super(wi, ai, si, elements, size);
    }

    @Override
    public void precompute(PrecomputeData precomputeData) {
        this.calc();
        precomputeData.setEntityIDList(KEY_ALL_ELEMENTS, (List<EntityID>)WorldUtil.convertToID(this.entities));
        precomputeData.setInteger(KEY_CLUSTER_SIZE, this.clusterSize);
        precomputeData.setEntityIDList(KEY_CLUSTER_CENTER, (List<EntityID>)WorldUtil.convertToID(this.centerList));
        for(int i = 0; i < this.clusterSize; i++) {
            precomputeData.setEntityIDList(KEY_CLUSTER_ENTITY + i, (List<EntityID>)WorldUtil.convertToID(this.clusterEntityList.get(i)));
        }
    }

    @Override
    public void resume(PrecomputeData precomputeData) {
        this.entities = WorldUtil.convertToEntity(precomputeData.getEntityIDList(KEY_ALL_ELEMENTS), this.worldInfo);
        this.clusterSize = precomputeData.getInteger(KEY_CLUSTER_SIZE);
        this.centerList = new ArrayList<>(WorldUtil.convertToEntity(precomputeData.getEntityIDList(KEY_CLUSTER_CENTER), this.worldInfo));
        this.clusterEntityList = new ArrayList<>(this.clusterSize);
        for(int i = 0; i < this.clusterSize; i++) {
            List<StandardEntity> list = new ArrayList<>(WorldUtil.convertToEntity(precomputeData.getEntityIDList(KEY_CLUSTER_ENTITY + i), this.worldInfo));
            this.clusterEntityList.add(i, list);
        }
        this.clusterEntityList.sort(comparing(List::size, reverseOrder()));
    }

    @Override
    public int getClusterIndex(StandardEntity entity) {
        for(int i = 0; i < this.clusterSize; i++) {
            if(this.clusterEntityList.get(i).contains(entity)) {
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
        return this.clusterEntityList.get(index);
    }

    @Override
    public Collection<EntityID> getClusterEntityIDs(int index) {
        return WorldUtil.convertToID(this.getClusterEntities(index));
    }

    @Override
    public Clustering calc() {
        Random random = new Random();

        List<StandardEntity> entityList = new ArrayList<>(this.entities);
        this.centerList = new ArrayList<>(this.clusterSize);
        this.clusterEntityList = new ArrayList<>(this.clusterSize);

        //init list
        for (int index = 0; index < this.clusterSize; index++) {
            this.clusterEntityList.add(index, new ArrayList<>());
            this.centerList.add(index, entityList.get(0));
        }
        System.out.println("Cluster : " + this.clusterSize);
        //init center
        for (int index = 0; index < this.clusterSize; index++) {
            StandardEntity centerEntity;
            do {
                centerEntity = entityList.get(Math.abs(random.nextInt()) % entityList.size());
            } while (this.centerList.contains(centerEntity));
            this.centerList.set(index, centerEntity);
        }
        //calc center
        for (int i = 0; i < this.repeat; i++) {
            this.clusterEntityList.clear();
            for (int index = 0; index < this.clusterSize; index++) {
                this.clusterEntityList.add(index, new ArrayList<>());
            }
            for (StandardEntity entity : entityList) {
                StandardEntity tmp = this.getNearEntityByLine(this.worldInfo.getRawWorld(), this.centerList, entity);
                this.clusterEntityList.get(this.centerList.indexOf(tmp)).add(entity);
            }
            for (int index = 0; index < this.clusterSize; index++) {
                int sumX = 0, sumY = 0;
                for (StandardEntity entity : this.clusterEntityList.get(index)) {
                    Pair<Integer, Integer> location = entity.getLocation(this.worldInfo.getRawWorld());
                    sumX += location.first();
                    sumY += location.second();
                }
                int centerX = sumX / this.clusterEntityList.get(index).size();
                int centerY = sumY / this.clusterEntityList.get(index).size();
                StandardEntity center = this.getNearEntityByLine(this.worldInfo.getRawWorld(), this.clusterEntityList.get(index), centerX, centerY);
                if(center instanceof Area) {
                    this.centerList.set(index, center);
                }
                else if(center instanceof Human) {
                    this.centerList.set(index, this.worldInfo.getEntity(((Human) center).getPosition()));
                }
                else if(center instanceof Blockade) {
                    this.centerList.set(index, this.worldInfo.getEntity(((Blockade) center).getPosition()));
                }
            }
            System.out.printf("*");
        }
        System.out.println();
        //set entity
        this.clusterEntityList.clear();
        for (int index = 0; index < this.clusterSize; index++) {
            this.clusterEntityList.add(index, new ArrayList<>());
        }
        for (StandardEntity entity : entityList) {
            StandardEntity tmp = this.getNearEntityByLine(this.worldInfo.getRawWorld(), this.centerList, entity);
            this.clusterEntityList.get(this.centerList.indexOf(tmp)).add(entity);
        }

        this.clusterEntityList.sort(comparing(List::size, reverseOrder()));
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

    private StandardEntity compareLineDistance(StandardWorldModel world, int targetX, int targetY, StandardEntity first, StandardEntity second) {
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

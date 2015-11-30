package adf.modules.main.algorithm.cluster;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.cluster.Clustering;
import rescuecore2.misc.Pair;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Edge;
import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;

public class PathBasedKMeans extends Clustering{

    List<StandardEntity> centerEntityList;
    List<List<StandardEntity>> clusterList;

    private int repeat = 10;

    private Map<EntityID, Set<EntityID>> shortestPathGraph;

    public PathBasedKMeans(WorldInfo wi, AgentInfo ai, ScenarioInfo si, Collection<StandardEntity> elements) {
        super(wi, ai, si, elements);
    }

    public PathBasedKMeans(WorldInfo wi, AgentInfo ai, ScenarioInfo si, Collection<StandardEntity> elements, int size) {
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
        this.initShortestPath(this.worldInfo);
        Random random = new Random();

        List<StandardEntity> entityList = new ArrayList<>(this.entities);
        this.centerEntityList = new ArrayList<>(this.clusterSize);
        this.clusterList = new ArrayList<>(this.clusterSize);

        for (int index = 0; index < this.clusterSize; index++) {
            this.clusterList.add(index, new ArrayList<>());
            this.centerEntityList.add(index, entityList.get(0));
        }

        for (int index = 0; index < this.clusterSize; index++) {
            StandardEntity centerEntity;
            do {
                centerEntity = entityList.get(Math.abs(random.nextInt()) % entityList.size());
            } while (this.centerEntityList.contains(centerEntity));
            this.centerEntityList.set(index, centerEntity);
        }

        for (int i = 0; i < this.repeat; i++) {
            this.clusterList.clear();
            for (int index = 0; index < this.clusterSize; index++) {
                this.clusterList.add(index, new ArrayList<>());
            }
            for (StandardEntity entity : entityList) {
                StandardEntity tmp = this.getNearEntity(this.worldInfo, this.centerEntityList, entity);
                this.clusterList.get(this.centerEntityList.indexOf(tmp)).add(entity);
            }

            for (int index = 0; index < this.clusterSize; index++) {
                int sumX = 0, sumY = 0;
                for (StandardEntity entity : this.clusterList.get(index)) {
                    Pair<Integer, Integer> location = entity.getLocation(this.worldInfo.getRawWorld());
                    sumX += location.first();
                    sumY += location.second();
                }
                int centerX = sumX / clusterList.get(index).size();
                int centerY = sumY / clusterList.get(index).size();

                this.centerEntityList.set(index, getNearEntity(this.worldInfo, this.clusterList.get(index), centerX, centerY));
            }
            System.out.print("*");
        }
        System.out.println();

        this.clusterList.clear();
        for (int index = 0; index < this.clusterSize; index++) {
            this.clusterList.add(index, new ArrayList<>());
        }
        for (StandardEntity entity : entityList) {
            StandardEntity tmp = this.getNearEntity(this.worldInfo, this.centerEntityList, entity);
            this.clusterList.get(this.centerEntityList.indexOf(tmp)).add(entity);
        }

        this.clusterList.sort(comparing(List::size, reverseOrder()));
        return this;
    }

    private StandardEntity getNearEntity(WorldInfo worldInfo, List<StandardEntity> srcEntityList, int targetX, int targetY) {
        StandardEntity result = null;
        for (StandardEntity entity : srcEntityList) {
            result = (result != null) ? this.compareLineDistance(worldInfo, targetX, targetY, result, entity) : entity;
        }
        return result;
    }

    public Point2D getEdgePoint(Edge edge) {
        Point2D start = edge.getStart();
        Point2D end = edge.getEnd();
        return new Point2D(((start.getX() + end.getX()) / 2.0D), ((start.getY() + end.getY()) / 2.0D));
    }


    public double getDistance(double fromX, double fromY, double toX, double toY) {
        double dx = fromX - toX;
        double dy = fromY - toY;
        return Math.hypot(dx, dy);
    }
    public double getDistance(Pair<Integer, Integer> from, Point2D to) {
			/*double dx = from.first() - to.getX();
			double dy = from.second() - to.getY();
			return Math.hypot(dx, dy);*/
        return getDistance(from.first(), from.second(), to.getX(), to.getY());
    }
    public double getDistance(Pair<Integer, Integer> from, Edge to) {
        return getDistance(from, getEdgePoint(to));
    }

    public double getDistance(Point2D from, Point2D to) {
        return getDistance(from.getX(), from.getY(), to.getX(), to.getY());
    }
    public double getDistance(Edge from, Edge to) {
        return getDistance(getEdgePoint(from), getEdgePoint(to));
    }

    private StandardEntity compareLineDistance(WorldInfo worldInfo, int targetX, int targetY, StandardEntity first, StandardEntity second)
    {
        double firstDistance = getDistance(first.getLocation(worldInfo.getRawWorld()).first(), first.getLocation(worldInfo.getRawWorld()).second(), targetX, targetY);
        double secondDistance = getDistance(second.getLocation(worldInfo.getRawWorld()).first(), second.getLocation(worldInfo.getRawWorld()).second(), targetX, targetY);
        return (firstDistance < secondDistance ? first : second);
    }

    private StandardEntity getNearEntity(WorldInfo worldInfo, List<StandardEntity> srcEntityList, StandardEntity targetEntity)
    {
        StandardEntity result = null;
        for (StandardEntity entity : srcEntityList) {
            result = (result != null) ? this.comparePathDistance(worldInfo, targetEntity, result, entity) : entity;
        }
        return result;
    }

    private StandardEntity comparePathDistance(WorldInfo worldInfo, StandardEntity target, StandardEntity first, StandardEntity second) {
        double firstDistance = getPathDistance(worldInfo, shortestPath(target.getID(), first.getID()));
        double secondDistance = getPathDistance(worldInfo, shortestPath(target.getID(), second.getID()));
        return (firstDistance < secondDistance ? first : second);
    }

    private double getPathDistance(WorldInfo worldInfo, List<EntityID> path) {
        double distance = 0.0D;

        if (path == null)
        {
            return Double.MAX_VALUE;
        }

        int limit = path.size() - 1;

        if(path.size() == 1)
        {
            return 0.0D;
        }

        Area area = (Area)worldInfo.getEntity(path.get(0));
        distance += getDistance(area.getLocation(worldInfo.getRawWorld()), area.getEdgeTo(path.get(1)));
        area = (Area)worldInfo.getEntity(path.get(limit));
        distance += getDistance(area.getLocation(worldInfo.getRawWorld()), area.getEdgeTo(path.get(limit - 1)));

        EntityID areaID;
        for(int i = 1; i < limit; i++) {
            areaID = path.get(i);
            area = (Area)worldInfo.getEntity(areaID);
            distance += getDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
        }
        return distance;
    }

    private void initShortestPath(WorldInfo worldInfo) {
        Map<EntityID, Set<EntityID>> neighbours = new LazyMap<EntityID, Set<EntityID>>() {
            @Override
            public Set<EntityID> createValue() {
                return new HashSet<>();
            }
        };
        for (Entity next : worldInfo) {
            if (next instanceof Area) {
                Collection<EntityID> areaNeighbours = ((Area) next).getNeighbours();
                neighbours.get(next.getID()).addAll(areaNeighbours);
            }
        }
        for (Map.Entry<EntityID, Set<EntityID>> graph : neighbours.entrySet()) // fix graph
        {
            for (EntityID entityID : graph.getValue())
            {
                neighbours.get(entityID).add(graph.getKey());
            }
        }
        this.shortestPathGraph = neighbours;
    }

    private List<EntityID> shortestPath(EntityID start, EntityID... goals) {
        return shortestPath(start, Arrays.asList(goals));
    }

    private List<EntityID> shortestPath(EntityID start, Collection<EntityID> goals) {
        List<EntityID> open = new LinkedList<>();
        Map<EntityID, EntityID> ancestors = new HashMap<>();
        open.add(start);
        EntityID next;
        boolean found = false;
        ancestors.put(start, start);
        do {
            next = open.remove(0);
            if (isGoal(next, goals)) {
                found = true;
                break;
            }
            Collection<EntityID> neighbours = shortestPathGraph.get(next);
            if (neighbours.isEmpty()) {
                continue;
            }
            for (EntityID neighbour : neighbours) {
                if (isGoal(neighbour, goals)) {
                    ancestors.put(neighbour, next);
                    next = neighbour;
                    found = true;
                    break;
                }
                else {
                    if (!ancestors.containsKey(neighbour)) {
                        open.add(neighbour);
                        ancestors.put(neighbour, next);
                    }
                }
            }
        } while (!found && !open.isEmpty());
        if (!found) {
            // No path
            return null;
        }
        // Walk back from goal to start
        EntityID current = next;
        List<EntityID> path = new LinkedList<>();
        do {
            path.add(0, current);
            current = ancestors.get(current);
            if (current == null) {
                throw new RuntimeException("Found a node with no ancestor! Something is broken.");
            }
        } while (current != start);
        return path;
    }

    private boolean isGoal(EntityID e, Collection<EntityID> test) {
        return test.contains(e);
    }
}

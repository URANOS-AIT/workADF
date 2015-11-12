package adf.algorithm.path;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.algorithm.path.PathPlanner;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Building;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

/**
 * Created by uranos on 15/10/23.
 */
public class SamplePathPlanner extends PathPlanner {

    private Map<EntityID, Set<EntityID>> graph;
    private Set<EntityID> buildingSet;

    EntityID from;
    private List<EntityID> result;

    public SamplePathPlanner(WorldInfo wi, AgentInfo ai, ScenarioInfo si) {
        super(wi,ai,si);
        this.init();
    }

    private void init() {
        Map<EntityID, Set<EntityID>> neighbours = new LazyMap<EntityID, Set<EntityID>>() {
            @Override
            public Set<EntityID> createValue() {
                return new HashSet<>();
            }
        };
        buildingSet= new HashSet<>();
        for (Entity next : this.worldInfo.world) {
            if (next instanceof Area) {
                Collection<EntityID> areaNeighbours = ((Area) next).getNeighbours();
                neighbours.get(next.getID()).addAll(areaNeighbours);
                if(next instanceof Building)
                    buildingSet.add(next.getID());
            }
        }
        this.setGraph(neighbours);
    }

    public void setGraph(Map<EntityID, Set<EntityID>> newGraph) {
        this.graph = newGraph;
    }

    @Override
    public List<EntityID> getResult() {
        return null;
    }

    @Override
    public void setFrom(EntityID from) {
        this.from = from;
    }

    /*@Override
    public void setDist(EntityID... target) {
        this.setDist(Arrays.asList(target));
    }*/

    public void setDist(Collection<EntityID> goals) {
        List<EntityID> open = new ArrayList<>();//LinkedList<>();
        Map<EntityID, EntityID> ancestors = new HashMap<>();
        open.add(this.from);
        EntityID next;
        boolean found = false;
        ancestors.put(this.from, this.from);
        do {
            next = open.remove(0);
            if (isGoal(next, goals)) {
                found = true;
                break;
            }
            Collection<EntityID> neighbours = graph.get(next);
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
            this.result = null;
        }
        // Walk back from goal to this.from
        EntityID current = next;
        List<EntityID> path = new ArrayList<>();//LinkedList<>();
        do {
            path.add(0, current);
            current = ancestors.get(current);
            if (current == null) {
                throw new RuntimeException("Found a node with no ancestor! Something is broken.");
            }
        } while (current != this.from);
        this.result = path;
    }

    private boolean isGoal(EntityID e, Collection<EntityID> test) {
        return test.contains(e);
    }
}

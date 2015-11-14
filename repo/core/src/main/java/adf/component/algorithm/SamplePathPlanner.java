package adf.algorithm;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
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
        Map<EntityID, Set<EntityID>> neighbours = new LazyMap<EntityID, Set<EntityID>>() {
            @Override
            public Set<EntityID> createValue() {
                return new HashSet<EntityID>();
            }
        };
        buildingSet=new HashSet<EntityID>();
        for (Entity next : wi.world) {
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

    @Override
    public void setDist(EntityID... target) {
        this.result =  breadthFirstSearch(Arrays.asList(target));
    }

    public List<EntityID> breadthFirstSearch(Collection<EntityID> goals) {
        List<EntityID> open = new LinkedList<>();
        Map<EntityID, EntityID> ancestors = new HashMap<>();
        open.add(this.from);
        EntityID next = null;
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
            return null;
        }
        // Walk back from goal to this.from
        EntityID current = next;
        List<EntityID> path = new LinkedList<>();
        do {
            path.add(0, current);
            current = ancestors.get(current);
            if (current == null) {
                throw new RuntimeException("Found a node with no ancestor! Something is broken.");
            }
        } while (current != this.from);
        return path;
    }

    private boolean isGoal(EntityID e, Collection<EntityID> test) {
        return test.contains(e);
    }
}

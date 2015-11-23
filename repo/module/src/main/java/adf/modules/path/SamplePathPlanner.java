package adf.modules.path;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.path.PathPlanner;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.Area;
import rescuecore2.standard.entities.Building;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class SamplePathPlanner extends PathPlanner {

    private Map<EntityID, Set<EntityID>> graph;
    private Set<EntityID> buildingSet;

    EntityID from;
    private List<EntityID> result;

    public SamplePathPlanner(WorldInfo wi, AgentInfo ai, ScenarioInfo si) {
        super(wi, ai, si);
        Map<EntityID, Set<EntityID>> neighbours = new LazyMap<EntityID, Set<EntityID>>() {
            @Override
            public Set<EntityID> createValue() {
                return new HashSet<>();
            }
        };
        buildingSet= new HashSet<>();
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
    public void clear() {
        this.from = null;
        this.result = null;
    }

    @Override
    public List<EntityID> getResult() {
        return this.result;
    }

    @Override
    public void setFrom(EntityID id) {
        this.from = from;
    }

    @Override
    public void setDist(Collection<EntityID> targets) {
        List<EntityID> open = new LinkedList<>();
        Map<EntityID, EntityID> ancestors = new HashMap<>();
        open.add(this.from);
        EntityID next;
        boolean found = false;
        ancestors.put(this.from, this.from);
        do {
            next = open.remove(0);
            if (isGoal(next, targets)) {
                found = true;
                break;
            }
            Collection<EntityID> neighbours = graph.get(next);
            if (neighbours.isEmpty()) {
                continue;
            }
            for (EntityID neighbour : neighbours) {
                if (isGoal(neighbour, targets)) {
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
        List<EntityID> path = new LinkedList<>();
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

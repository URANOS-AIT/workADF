package adf.modules.tactics.fire;

import adf.agent.action.Action;
import adf.agent.info.AgentInfo;
import adf.agent.info.PrecomputeData;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.path.PathPlanner;
import adf.component.tactics.TacticsFire;
import adf.modules.path.SamplePathPlanner;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;

import java.util.*;

public class DefaultTacticsFire extends TacticsFire{

    private int maxWater;
    private int maxDistance;
    private int maxPower;

    private static final int RANDOM_WALK_LENGTH = 50;

    protected List<EntityID> buildingIDs;
    protected List<EntityID> roadIDs;
    protected List<EntityID> refugeIDs;

    private Map<EntityID, Set<EntityID>> neighbours;
    private Random random;

    private PathPlanner pathPlanner;

    @Override
    public void initialize(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        this.random = new Random();
        buildingIDs = new ArrayList<>();
        roadIDs = new ArrayList<>();
        refugeIDs = new ArrayList<>();
    }

    @Override
    public void precompute(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeData precomputeInfo) {

    }

    @Override
    public void resume(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeData precomputeInfo) {
        this.preparate(agentInfo, worldInfo, scenarioInfo);
    }

    @Override
    public void preparate(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        for (StandardEntity next : worldInfo.world) {
            if (next instanceof Building) {
                buildingIDs.add(next.getID());
            }
            if (next instanceof Road) {
                roadIDs.add(next.getID());
            }
            if (next instanceof Refuge) {
                refugeIDs.add(next.getID());
            }
        }
        //pathplanner
        //neighbours = search.getGraph();
        this.init(worldInfo);
        this.pathPlanner = new SamplePathPlanner(worldInfo, agentInfo, scenarioInfo);
        maxWater = scenarioInfo.getFireTankMaximum();
        maxDistance = scenarioInfo.getFireExtinguishMaxDistance();
        maxPower = scenarioInfo.getFireExtinguishMaxSum();
    }

    @Override
    public Action think(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        return null;
    }

    private void init(WorldInfo worldInfo) {
        Map<EntityID, Set<EntityID>> neighbours = new LazyMap<EntityID, Set<EntityID>>() {
            @Override
            public Set<EntityID> createValue() {
                return new HashSet<>();
            }
        };
        Set<EntityID> buildingSet= new HashSet<>();
        for (Entity next : worldInfo.world) {
            if (next instanceof Area) {
                Collection<EntityID> areaNeighbours = ((Area) next).getNeighbours();
                neighbours.get(next.getID()).addAll(areaNeighbours);
                if (next instanceof Building)
                    buildingSet.add(next.getID());
            }
        }
        this.neighbours = neighbours;
    }

    protected List<EntityID> randomWalk(AgentInfo agentInfo) {
        List<EntityID> result = new ArrayList<>(RANDOM_WALK_LENGTH);
        Set<EntityID> seen = new HashSet<>();
        EntityID current = agentInfo.getPosition();
        for (int i = 0; i < RANDOM_WALK_LENGTH; ++i) {
            result.add(current);
            seen.add(current);
            if (neighbours.get(current) == null)
            {
                return result;
            }
            List<EntityID> possible = new ArrayList<>(neighbours.get(current));
            Collections.shuffle(possible, random);
            boolean found = false;
            for (EntityID next : possible) {
                if (seen.contains(next)) {
                    continue;
                }
                current = next;
                found = true;
                break;
            }
            if (!found) {
                // We reached a dead-end.
                break;
            }
        }
        return result;
    }
}

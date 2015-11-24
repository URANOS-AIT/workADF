package adf.modules.tactics.fire;

import adf.agent.action.Action;
import adf.agent.action.common.ActionMove;
import adf.agent.action.common.ActionRest;
import adf.agent.action.fire.ActionExtinguish;
import adf.agent.info.AgentInfo;
import adf.agent.precompute.PrecomputeData;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.component.algorithm.path.PathPlanner;
import adf.component.tactics.TacticsFire;
import adf.modules.extaction.ActionFireFighting;
import adf.modules.path.SamplePathPlanner;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;
import sample.DistanceSorter;

import java.util.*;

import static rescuecore2.misc.Handy.objectsToIDs;

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
    public void precompute(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeData precomputeData) {

    }

    @Override
    public void resume(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeData precomputeData) {
        this.preparate(agentInfo, worldInfo, scenarioInfo);
    }

    @Override
    public void preparate(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        for (StandardEntity next : worldInfo.world) {
            if (next instanceof Building) {
                buildingIDs.add(next.getID());
            }
            else if (next instanceof Road) {
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

    public FireBrigade me(WorldInfo worldInfo, AgentInfo agentInfo) {
        return (FireBrigade)worldInfo.getEntity(agentInfo.getID());
    }

    @Override
    public Action think(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo) {
        FireBrigade me = me(worldInfo, agentInfo);
        // Are we currently filling with water?
        if (me.isWaterDefined() && me.getWater() < maxWater && agentInfo.getLocation() instanceof Refuge) {
            //Logger.info("Filling with water at " + location());
            //sendRest(time);
            return new ActionRest();
        }
        // Are we out of water?
        if (me.isWaterDefined() && me.getWater() == 0) {
            // Head for a refuge
            //List<EntityID> path = search.breadthFirstSearch(me().getPosition(), refugeIDs);
            this.pathPlanner.setFrom(agentInfo.getPosition());
            this.pathPlanner.setDist(refugeIDs);
            List<EntityID> path = this.pathPlanner.getResult();
            return path != null ? new ActionMove(path) : new ActionMove(this.randomWalk(agentInfo));
        }
        // Find all buildings that are on fire
        Collection<EntityID> all = getBurningBuildings(worldInfo, agentInfo);
        // Can we extinguish any right now?
        List<Action> results = new ArrayList<>();
        for (EntityID next : all) {
            ActionFireFighting aff = new ActionFireFighting(worldInfo, agentInfo, scenarioInfo, this.pathPlanner, next);
            Action action = aff.calc().getAction();
            if(action != null) {
                //return action;
                results.add(action);
            }
        }
        ////////////////////////////////////////////////////
        for(Action action : results) {
            if(action instanceof ActionExtinguish) {
                return action;
            }
        }
        if(results.size() != 0) {
            //distance1 < distance2
            return results.get(0);
        }
        /////////////////////////////////////////////////////
        return new ActionMove(this.randomWalk(agentInfo));
    }

    private Collection<EntityID> getBurningBuildings(WorldInfo worldInfo, AgentInfo agentInfo) {
        Collection<StandardEntity> e = worldInfo.world.getEntitiesOfType(StandardEntityURN.BUILDING);
        List<Building> result = new ArrayList<>();
        for (StandardEntity next : e) {
            if (next instanceof Building) {
                Building b = (Building)next;
                if (b.isOnFire()) {
                    result.add(b);
                }
            }
        }
        // Sort by distance
        Collections.sort(result, new DistanceSorter(worldInfo.getEntity(agentInfo.getPosition()), worldInfo.getRawWorld()));
        return objectsToIDs(result);
    }

    private List<EntityID> planPathToFire(EntityID target, WorldInfo worldInfo, AgentInfo agentInfo) {
        // Try to get to anything within maxDistance of the target
        Collection<StandardEntity> targets = worldInfo.getObjectsInRange(target, maxDistance);
        if (targets.isEmpty()) {
            return null;
        }
        //return search.breadthFirstSearch(this.agentInfo.getPosition(), objectsToIDs(targets));
        this.pathPlanner.setFrom(agentInfo.getPosition());
        this.pathPlanner.setDist(objectsToIDs(targets));
        return this.pathPlanner.getResult();
        //return new ActionMove(path);
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

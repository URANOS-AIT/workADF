package adf.algorithm.cluster;

import rescuecore2.standard.entities.StandardEntity;
import rescuecore2.standard.entities.StandardWorldModel;
import rescuecore2.worldmodel.EntityID;

import java.util.ArrayList;
import java.util.List;

public class ClusterData {

    private boolean agentAddingLock;
    private EntityID centerAreaID;
    private List<EntityID> areaIDList = new ArrayList<>();
    private List<EntityID> agentIDList = new ArrayList<>();
    private List<EntityID> fireBregadeIDList = new ArrayList<>();
    private List<EntityID> policeForceIDList = new ArrayList<>();
    private List<EntityID> ambulanceTeamIDList = new ArrayList<>();

    public ClusterData(EntityID center, List<EntityID> areas)
    {
        this.agentAddingLock = false;
        this.centerAreaID = center;
        this.areaIDList = areas;
    }

    public void LockAddingAgents()
    {
        this.agentAddingLock = true;
    }

    public boolean addAgent(StandardEntity agent)
    {
        if (!this.agentAddingLock)
        {
            agentIDList.add(agent.getID());
            switch (agent.getStandardURN())
            {
                case FIRE_BRIGADE:
                    fireBregadeIDList.add(agent.getID());
                    break;
                case POLICE_FORCE:
                    policeForceIDList.add(agent.getID());
                    break;
                case AMBULANCE_TEAM:
                    ambulanceTeamIDList.add(agent.getID());
                    break;
            }
            return true;
        }
        else
        { return false; }
    }

    public EntityID getCenterAreaID()
    {
        return centerAreaID;
    }

    public List<EntityID> getAgentIDList()
    {
        return agentIDList;
    }

    public List<EntityID> getFireBrigadeIDList()
    {
        return fireBregadeIDList;
    }

    public List<EntityID> getPoliceForceIDList()
    {
        return policeForceIDList;
    }

    public List<EntityID> getAmbulanceTeamIDList()
    {
        return ambulanceTeamIDList;
    }

    public List<EntityID> getAreaIDList()
    {
        return areaIDList;
    }

    public int getCenterX(StandardWorldModel world)
    {
        return world.getEntity(centerAreaID).getLocation(world).first();
    }

    public int getCenterY(StandardWorldModel world)
    {
        return world.getEntity(centerAreaID).getLocation(world).second();
    }
}

package adf.algorithm.cluster;

import rescuecore2.config.Config;
import rescuecore2.config.ConfigException;
import rescuecore2.misc.Pair;
import rescuecore2.misc.collections.LazyMap;
import rescuecore2.misc.geometry.Point2D;
import rescuecore2.standard.entities.*;
import rescuecore2.worldmodel.Entity;
import rescuecore2.worldmodel.EntityID;

import java.io.File;
import java.util.*;

import static java.util.Collections.reverseOrder;
import static java.util.Comparator.comparing;


public class StandardKMeans
{
  private int repeat = 50;
  private List<ClusterData> kMeansClusterList;

  public StandardKMeans(int k, StandardWorldModel world)
  {
    initShortestPath(world);

    Config config = new Config();
    try
    {
      config.read(new File("naito15.cfg"));
    }
    catch (ConfigException e) { e.printStackTrace(); }

    repeat = config.getIntValue("naito15.clustering.standardkmeans.repeat", 50);

    List<StandardEntity> areaList = new ArrayList<StandardEntity>(world.getEntitiesOfType(StandardEntityURN.ROAD, StandardEntityURN.BUILDING, StandardEntityURN.GAS_STATION, StandardEntityURN.REFUGE));
    List<StandardEntity> agentList = new ArrayList<StandardEntity>(world.getEntitiesOfType(StandardEntityURN.AMBULANCE_TEAM, StandardEntityURN.POLICE_FORCE, StandardEntityURN.FIRE_BRIGADE));
    List<StandardEntity> buildingList = new ArrayList<StandardEntity>(world.getEntitiesOfType(StandardEntityURN.BUILDING));

    List<StandardEntity> centerAreaList = new ArrayList<StandardEntity>(k);
    List<List<StandardEntity>> clusterList = new ArrayList<List<StandardEntity>>(k);
    this.kMeansClusterList = new ArrayList<ClusterData>(k);
    
    for (int index = 0; index < k; index++)
    {
      clusterList.add(index,  new ArrayList<StandardEntity>());
      centerAreaList.add(index, buildingList.get(0));
      kMeansClusterList.add(index, new ClusterData(null,null));
    }
    
    for (int index = 0; index < k; index++)
    {
      Area centerArea;
      do
      {
        centerArea = (Area)buildingList.get(Math.abs(random()) % buildingList.size());
      } while (centerAreaList.contains(centerArea));
      centerAreaList.set(index, centerArea);

      //System.out.println("index: " + index + ", center: " + centerArea);
    }

		for (int i = 0; i < repeat; i++)
    { System.out.print("_"); }
    System.out.println();

    for (int i = 0; i < repeat; i++)
    {
      clusterList.clear();
      for (int index = 0; index < k; index++)
      {
        clusterList.add(index, new ArrayList<StandardEntity>());
      }
      for (StandardEntity entity : buildingList)
      {
        StandardEntity tmp = this.getNearAreaByLine(world, centerAreaList, entity);
        clusterList.get(centerAreaList.indexOf(tmp)).add((Area)entity);
      }

      for (int index = 0; index < k; index++)
      {
        int sumX = 0, sumY = 0;
        for (StandardEntity area : clusterList.get(index))
        {
          sumX += area.getLocation(world).first();
          sumY += area.getLocation(world).second();
        }
        int centerX = sumX / clusterList.get(index).size();
        int centerY = sumY / clusterList.get(index).size();
        
        //System.out.println("index: " + index + ", x: "+centerX+", y:"+centerY+", center: " + getNearArea(world, clusterList.get(index), centerX, centerY));

        centerAreaList.set(index, getNearAreaByLine(world, clusterList.get(index), centerX, centerY));
        // or
        //centerAreaList.set(i, getNearArea(world, areaList, centerX, centerY));
      }
      System.out.print("*");
      System.out.flush();
    }
    System.out.println();

    clusterList.clear();
    for (int index = 0; index < k; index++)
    {
      clusterList.add(index, new ArrayList<StandardEntity>());
    }
    for (StandardEntity entity : areaList)
    {
      StandardEntity tmp = this.getNearAreaByLine(world, centerAreaList, entity);
      clusterList.get(centerAreaList.indexOf(tmp)).add((Area)entity);
    }

    kMeansClusterList.clear();
    for (int index = 0; index < k; index++)
    {
      List<EntityID> clustersAreaIDList = new ArrayList<EntityID>(clusterList.get(index).size());
      for (StandardEntity entity : clusterList.get(index))
      { clustersAreaIDList.add(entity.getID()); }
      //System.out.println("index: " +index+", cluster size: " + clustersAreaIDList.size());
      kMeansClusterList.add(index, new ClusterData(centerAreaList.get(index).getID(), clustersAreaIDList));
    }

    kMeansClusterList.sort(comparing(x -> x.getAreaIDList().size(), reverseOrder()));

    List<StandardEntity> firebrigadeList = new ArrayList<StandardEntity>(world.getEntitiesOfType(StandardEntityURN.FIRE_BRIGADE));
    List<StandardEntity> policeforceList = new ArrayList<StandardEntity>(world.getEntitiesOfType(StandardEntityURN.POLICE_FORCE));
    List<StandardEntity> ambulanceteamList = new ArrayList<StandardEntity>(world.getEntitiesOfType(StandardEntityURN.AMBULANCE_TEAM));

    assignAgents(world, kMeansClusterList, firebrigadeList);
    assignAgents(world, kMeansClusterList, policeforceList);
    assignAgents(world, kMeansClusterList, ambulanceteamList);
    for (ClusterData cluster : kMeansClusterList)
    { cluster.LockAddingAgents(); }

    for (int index = 0; index < kMeansClusterList.size(); index++)
    {
      System.out.print(kMeansClusterList.get(index).getAreaIDList().size());
      System.out.print("+");
      System.out.print(kMeansClusterList.get(index).getAgentIDList().size());
      if (index < kMeansClusterList.size() -1)
      { System.out.print(","); }
      else
      { System.out.println(); }
    }
  }

  private void assignAgents(StandardWorldModel world, List<ClusterData> clusterList, List<StandardEntity> agentList)
  {
    int clusterIndex = 0;
    while (agentList.size() > 0)
    {
      EntityID center = clusterList.get(clusterIndex).getCenterAreaID();
      StandardEntity agent = getNearAgent(world, agentList, world.getEntity(center));
      clusterList.get(clusterIndex).addAgent(agent);
      agentList.remove(agent);
      clusterIndex++;
      if (clusterIndex >= clusterList.size()) { clusterIndex = 0; }
    }
  }

  private StandardEntity getNearAgent(StandardWorldModel world, List<StandardEntity> srcAgentList, StandardEntity targetEntity)
  {
    StandardEntity result = null;
    for (StandardEntity agent : srcAgentList)
    {
      if (result == null)
      { result = agent; }
      else
      {
        switch (agent.getStandardURN())
        {
          case FIRE_BRIGADE:
            if (this.comparePathDistance(world, targetEntity, result, ((FireBrigade)agent).getPosition(world)).equals(((FireBrigade)agent).getPosition(world)))
            { result = agent; }
            break;
          case POLICE_FORCE:
            if (this.comparePathDistance(world, targetEntity, result, ((PoliceForce)agent).getPosition(world)).equals(((PoliceForce)agent).getPosition(world)))
            { result = agent; }
            break;
          case AMBULANCE_TEAM:
            if (this.comparePathDistance(world, targetEntity, result, ((AmbulanceTeam)agent).getPosition(world)).equals(((AmbulanceTeam)agent).getPosition(world)))
            { result = agent; }
            break;
        }
      }
    }
    return result;
  }

  public List<ClusterData> getKMeansClusterList()
  {
    return kMeansClusterList;
  }

  // TODO: refactoring...(StandardEntity --> EntityID??)

  private Area getNearAreaByLine(StandardWorldModel world, List<StandardEntity> srcAreaList, int targetX, int targetY)
  {
    Area result = null;
    for (StandardEntity entity : srcAreaList)
    { result = (Area)((result != null) ? this.compareLineDistance(world, targetX, targetY, result, entity) : entity); }
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

    public  double getDistance(Pair<Integer, Integer> position, Pair<Integer, Integer> another) {
        /*double dx = (double)(position.first() - another.first());
        double dy = (double)(position.second() - another.second());
        return Math.hypot(dx, dy);*/
        return getDistance(position.first(), position.second(), another.first(), another.second());
    }
    public double getDistance(Point2D from, Point2D to) {
        /*double dx = from.getX() - to.getX();
        double dy = from.getY() - to.getY();
        return Math.hypot(dx, dy);*/
        return getDistance(from.getX(), from.getY(), to.getX(), to.getY());
    }
    public double getDistance(Edge from, Edge to) {
        return getDistance(getEdgePoint(from), getEdgePoint(to));
    }

  private StandardEntity compareLineDistance(StandardWorldModel world, int targetX, int targetY, StandardEntity first, StandardEntity second)
  {
    double firstDistance = getDistance(first.getLocation(world).first(), first.getLocation(world).second(), targetX, targetY);
    double secondDistance = getDistance(second.getLocation(world).first(), second.getLocation(world).second(), targetX, targetY);
    return (firstDistance < secondDistance ? first : second);
  }

  private Area getNearAreaByLine(StandardWorldModel world, List<StandardEntity> srcAreaList, StandardEntity targetEntity)
  {
    Area result = null;
    for (StandardEntity entity : srcAreaList)
    { result = (Area)((result != null) ? this.compareLineDistance(world, ((Area)targetEntity).getX(), ((Area)targetEntity).getY(), result, entity) : entity); }
    return result;
  }

  private Area getNearArea(StandardWorldModel world, List<StandardEntity> srcAreaList, StandardEntity targetEntity)
  {
    Area result = null;
    // for (StandardEntity entity : srcAreaList)
    // { result = (Area)((result != null) ? this.comparePathDistance(world, targetEntity, result, entity) : entity); }
    for (StandardEntity entity : srcAreaList)
    { result = (Area)((result != null) ? this.comparePathDistance(world, targetEntity, result, entity) : entity); }
    return result;
  }

  private StandardEntity comparePathDistance(StandardWorldModel world, StandardEntity target, StandardEntity first, StandardEntity second)
  {
    double firstDistance = getPathDistance(world, shortestPath(target.getID(), first.getID()));
    double secondDistance = getPathDistance(world, shortestPath(target.getID(), second.getID()));
    return (firstDistance < secondDistance ? first : second);
  }

  private double getPathDistance(StandardWorldModel world, List<EntityID> path)
  {
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

    Area area = (Area)world.getEntity(path.get(0));
    distance += getDistance(area.getLocation(world), area.getEdgeTo(path.get(1)));
    area = (Area)world.getEntity(path.get(limit));
    distance += getDistance(area.getLocation(world), area.getEdgeTo(path.get(limit - 1)));

    EntityID areaID;
    for(int i = 1; i < limit; i++)
    {
      areaID = path.get(i);
      area = (Area)world.getEntity(areaID);
      distance += getDistance(area.getEdgeTo(path.get(i - 1)), area.getEdgeTo(path.get(i + 1)));
    }
    return distance;
  }

  // genarate random int value
  private int randomX = 123456789;
  private int randomY = 362436069;
  private int randomZ = 521288629;
  private int randomW = 88675123;
  //Random random = new Random();
  private int random()
  {
    //return random.nextInt();
    int temp = randomX ^ (randomX << 11);
    randomX = randomY; randomY = randomZ; randomZ = randomW;
    return randomW = (randomW ^ (randomW >> 19)) ^ (temp ^ (temp >> 8));
  }

  private Map<EntityID, Set<EntityID>> shortestPathGraph;
  private Set<EntityID> buildingSet;

  private void initShortestPath(StandardWorldModel world) {
    Map<EntityID, Set<EntityID>> neighbours = new LazyMap<EntityID, Set<EntityID>>() {
      @Override
      public Set<EntityID> createValue() {
        return new HashSet<EntityID>();
      }
    };
    buildingSet=new HashSet<EntityID>();
    for (Entity next : world) {
      if (next instanceof Area) {
        Collection<EntityID> areaNeighbours = ((Area) next).getNeighbours();
        neighbours.get(next.getID()).addAll(areaNeighbours);
        if(next instanceof Building)
          buildingSet.add(next.getID());
      }
    }
    for (Map.Entry<EntityID, Set<EntityID>> graph : neighbours.entrySet()) // fix graph
    {
      for (EntityID entityID : graph.getValue())
      {
        neighbours.get(entityID).add(graph.getKey());
      }
    }
    setGraph(neighbours);
  }

  private void setGraph(Map<EntityID, Set<EntityID>> newGraph) {
    this.shortestPathGraph = newGraph;
  }

  private List<EntityID> shortestPath(EntityID start, EntityID... goals) {
    return shortestPath(start, Arrays.asList(goals));
  }

  private List<EntityID> shortestPath(EntityID start, Collection<EntityID> goals) {
    List<EntityID> open = new LinkedList<EntityID>();
    Map<EntityID, EntityID> ancestors = new HashMap<EntityID, EntityID>();
    open.add(start);
    EntityID next = null;
    boolean found = false;
    ancestors.put(start, start);
    do {
      next = open.remove(0);
      if (isGoal(next, goals)) {
        found = true;
        break;
      }
      Collection<EntityID> neighbours = shortestPathGraph.get(next);
      /*
      if (start.getValue() == 254 || start.getValue() == 953)
      {
        System.out.println("@"+goals+"|"+next+"@"+neighbours+((Area)swm.getEntity(next)).getNeighbours());
      }
      */
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
    List<EntityID> path = new LinkedList<EntityID>();
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

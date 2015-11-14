package adf.tactics;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.agent.platoon.action.Action;
import adf.util.datastorage.DataStorage;

public abstract class Tactics
{
	private Tactics parentTactics;

	public Tactics(Tactics parent)
	{
		this.parentTactics = parent;
	}

	public Tactics()
	{
		this(null);
	}

	abstract public void initialize(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo);
	abstract public void precompute(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, DataStorage dataStorage);
	abstract public void resume(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, DataStorage dataStorage);
	abstract public void preparate(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo);
	abstract public Action think(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo);

	public Tactics getParentTactics()
	{
		return parentTactics;
	}
}

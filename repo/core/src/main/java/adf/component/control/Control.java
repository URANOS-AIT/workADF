package adf.control;

import adf.agent.info.AgentInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;
import adf.util.datastorage.DataStorage;

public abstract class Control
{
	private Control parentControl;

	public Control(Control parent)
	{
		this.parentControl = parent;
	}

	public Control()
	{
		this(null);
	}

	abstract public void initialize(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo);
	abstract public void resume(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, DataStorage dataStorage);
	abstract public void preparate(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo);
	abstract public void think(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo);

	public Control getParentControl()
	{
		return parentControl;
	}
}

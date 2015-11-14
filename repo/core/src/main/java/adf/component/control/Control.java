package adf.component.control;

import adf.agent.info.AgentInfo;
import adf.agent.info.PrecomputeInfo;
import adf.agent.info.ScenarioInfo;
import adf.agent.info.WorldInfo;

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
	abstract public void resume(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo, PrecomputeInfo precomputeInfo);
	abstract public void preparate(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo);
	abstract public void think(AgentInfo agentInfo, WorldInfo worldInfo, ScenarioInfo scenarioInfo);

	public Control getParentControl()
	{
		return parentControl;
	}
}

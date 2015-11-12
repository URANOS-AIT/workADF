package adf.agent.info;

import rescuecore2.Constants;
import rescuecore2.config.Config;

public class ScenarioInfo
{
	public enum Mode
	{
		NON_PRECOMPUTE,
		PRECOMPUTED,
		PRECOMPUTATION_PHASE
	}

	public Config config;
	public Mode mode;

	public ScenarioInfo(Config config, Mode mode)
	{
		this.config = config;
		this.mode = mode;
	}

	public ScenarioInfo(Config config)
	{
		this(config, Mode.NON_PRECOMPUTE);
	}

	public void setConfig(Config config)
	{
		this.config = config;
	}

	public Mode getMode()
	{
		return mode;
	}

	/*
	public void test()
	{
		config.getIntValue("fire.exthinguish.max-sum");
	}
	*/
}

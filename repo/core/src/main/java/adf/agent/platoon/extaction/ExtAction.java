package adf.agent.platoon.extaction;

import adf.agent.platoon.action.Action;

/**
 * Created by takamin on 10/14/15.
 */
abstract public class ExtAction
{
	protected Action result;

	public ExtAction()
	{
		result = null;
	}

	public ExtAction calc()
	{
		return this;
	}

	public Action getAction()
	{
		return result;
	}
}

package adf.component.action.extaction;

import adf.component.action.action.Action;

abstract public class ExtAction {
	protected Action result;

	public ExtAction()
	{
		result = null;
	}

	public ExtAction calc() {
		return this;
	}

	//public abstract Action getAction();
	public Action getAction() {
		return result;
	}
}

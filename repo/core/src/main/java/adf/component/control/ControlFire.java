package adf.control;

public abstract class ControlFire extends Control
{
	public ControlFire(ControlFire parent)
	{
		super(parent);
	}

	public ControlFire()
	{
		this(null);
	}
}

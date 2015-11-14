package adf.tactics;

public abstract class TacticsFire extends Tactics
{
	public TacticsFire(TacticsFire parent)
	{
		super(parent);
	}

	public TacticsFire()
	{
		this(null);
	}
}

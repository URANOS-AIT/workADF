package adf.launcher;

import adf.control.ControlAmbulance;
import adf.control.ControlFire;
import adf.control.ControlPolice;
import adf.tactics.TacticsAmbulance;
import adf.tactics.TacticsFire;
import adf.tactics.TacticsPolice;

abstract public class AbstractLoader
{
	abstract public String getTeamName();

	abstract public TacticsAmbulance getTacticsAmbulance();
	abstract public TacticsFire getTacticsFire();
	abstract public TacticsPolice getTacticsPolice();

	abstract public ControlAmbulance getControlAmbulance();
	abstract public ControlFire getControlFire();
	abstract public ControlPolice getControlPolice();
}

package adf.modules.loader;

import adf.component.AbstractLoader;
import adf.component.control.ControlAmbulance;
import adf.component.control.ControlFire;
import adf.component.control.ControlPolice;
import adf.component.tactics.TacticsAmbulance;
import adf.component.tactics.TacticsFire;
import adf.component.tactics.TacticsPolice;
import adf.modules.tactics.ambulance.DefaultTacticsAmbulance;
import adf.modules.tactics.fire.DefaultTacticsFire;

public class DefaultLoader extends AbstractLoader {
    @Override
    public String getTeamName() {
        return "Sample";
    }

    @Override
    public TacticsAmbulance getTacticsAmbulance() {
        return new DefaultTacticsAmbulance();
    }

    @Override
    public TacticsFire getTacticsFire() {
        return new DefaultTacticsFire();
    }

    @Override
    public TacticsPolice getTacticsPolice() {
        return null;
    }

    @Override
    public ControlAmbulance getControlAmbulance() {
        return null;
    }

    @Override
    public ControlFire getControlFire() {
        return null;
    }

    @Override
    public ControlPolice getControlPolice() {
        return null;
    }
}

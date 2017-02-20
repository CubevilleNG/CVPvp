package org.cubeville.pvp.commands.commandparameters;

import org.cubeville.commons.commands.CommandParameterType;
import org.cubeville.pvp.CVPvp;

public class CommandParameterLoadoutContainer implements CommandParameterType
{
    public boolean isValid(String value) {
        return CVPvp.getInstance().getLoadoutManager().contains(value);
    }

    public String getInvalidMessage(String value) {
        return value + " is no valid loadout!";
    }

    public Object getValue(String value) {
        return CVPvp.getInstance().getLoadoutManager().getLoadoutByName(value);
    }
}

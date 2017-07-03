package org.cubeville.pvp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import org.cubeville.commons.commands.CommandParser;

import org.cubeville.pvp.commands.*;
import org.cubeville.pvp.loadout.LoadoutContainer;
import org.cubeville.pvp.loadout.LoadoutManager;

public class CVPvp extends JavaPlugin {

    private LoadoutManager loadoutManager;
    private CommandParser commandParser;
    
    private static CVPvp instance;

    public static CVPvp getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;

        ConfigurationSerialization.registerClass(LoadoutContainer.class, "LoadoutContainer");
        ConfigurationSerialization.registerClass(LoadoutManager.class, "LoadoutManager");

        loadoutManager = (LoadoutManager) getConfig().get("LoadoutManager");
        if(loadoutManager == null) loadoutManager = new LoadoutManager();
        loadoutManager.setManager(this);

        commandParser = new CommandParser();
        commandParser.addCommand(new LoadoutApply());
        commandParser.addCommand(new LoadoutBlacklistPlayer());
        commandParser.addCommand(new LoadoutCreate());
        commandParser.addCommand(new LoadoutClone());
        commandParser.addCommand(new LoadoutEdit());
        commandParser.addCommand(new LoadoutInfo());
        commandParser.addCommand(new LoadoutList());
        commandParser.addCommand(new LoadoutRemove());
        commandParser.addCommand(new LoadoutTagAdd());
        commandParser.addCommand(new LoadoutTagClear());
        commandParser.addCommand(new LoadoutTagRemove());
        commandParser.addCommand(new LoadoutUnblacklistPlayer());

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new EventListener(), this);
    }

    public void onDisable() {
       instance = null;
    }

    public LoadoutManager getLoadoutManager() {
        return loadoutManager;
    }

    public void saveLoadoutManager() {
        getConfig().set("LoadoutManager", loadoutManager);
        saveConfig();
    }
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("cvpvp")) {
            return commandParser.execute(sender, args);
        }
        return false;
    }
}

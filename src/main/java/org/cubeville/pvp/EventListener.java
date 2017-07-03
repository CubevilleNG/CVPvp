package org.cubeville.pvp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import org.cubeville.commons.utils.ColorUtils;

import org.cubeville.pvp.loadout.LoadoutContainer;
import org.cubeville.pvp.loadout.LoadoutHandler;
import org.cubeville.pvp.loadout.LoadoutManager;

public class EventListener implements Listener
{
     @EventHandler
     public void onInventoryClose(InventoryCloseEvent event) {
         CVPvp pvp = CVPvp.getInstance();
         if (pvp.getLoadoutManager().containsInventory(event.getInventory())) {
             pvp.saveLoadoutManager();
             String[] split = event.getInventory().getName().split(":");
             event.getPlayer().sendMessage(ColorUtils.addColor("&aLoadout &6" + split[0] + "&a:&6" + split[1] + " &asaved successfully!"));
         }
     }

    @EventHandler (priority = EventPriority.HIGH)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.WALL_SIGN) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Sign sign = (Sign) event.getClickedBlock().getState();
        if(sign.getLine(1).length() == 0 || sign.getLine(1).charAt(0) != '[') return;

        for (String lString: loadoutAliases) {
            if (sign.getLine(1).equalsIgnoreCase(lString)) {
                if (CVPvp.getInstance().getLoadoutManager().blacklistContains(event.getPlayer().getName())) {
                    event.getPlayer().sendMessage(ColorUtils.addColor("&cYou are currently blacklisted from using loadouts!"));
                    return;
                }
                LoadoutHandler.applyLoadoutFromSign(event.getPlayer(), sign);
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler (priority = EventPriority.MONITOR)
    public void onSignChange(SignChangeEvent event) {
        String[] lines = event.getLines();

        if(lines[1].length() == 0 || lines[1].charAt(0) != '[') return;

        {
            boolean found = false;
            for (String alias: loadoutAliases) {
                if (lines[1].equalsIgnoreCase(alias)) {
                    found = true;
                }
            }
            if(found == false) return;
        }

        Player player = event.getPlayer();

        if (!(player.hasPermission("cvpvp.admin") || player.hasPermission("snbt.loadout.commands"))) {
            player.sendMessage(ColorUtils.addColor("&cYou do not have permission to make loadout signs!"));
            event.setCancelled(true);
            return;
        }

        LoadoutManager loadoutManager = CVPvp.getInstance().getLoadoutManager();
        LoadoutContainer lc = loadoutManager.getLoadoutByName(lines[2]);
        if(lc == null) {
            player.sendMessage(ColorUtils.addColor("&cLoadout &6" + event.getLine(2) + " &cdoes not exist!"));
            event.setCancelled(true);
            return;
        }

        if(!lc.containsInventory(lines[3])) {
            player.sendMessage(ColorUtils.addColor("&cTag &6" + lines[2] + ":" + lines[3] + " &cdoes not exist!"));
            event.setCancelled(true);
            return;
        }

        player.sendMessage(ColorUtils.addColor("&aLoadout sign created successfully!"));

    }

    public static List<String> loadoutAliases = new ArrayList<>(Arrays.asList("[load-out]","[kit]","[class]"));
}

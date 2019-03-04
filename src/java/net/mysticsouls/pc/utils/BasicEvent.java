package net.mysticsouls.pc.utils;

import net.mysticsouls.pc.Main;
import org.bukkit.event.Listener;

public class BasicEvent implements Listener {
    protected Main plugin;

    public BasicEvent(Main main) {
        plugin = main;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}

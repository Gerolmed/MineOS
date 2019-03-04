package net.mysticsouls.pc.utils;

import org.bukkit.event.Listener;

import net.mysticsouls.pc.Main;

public class BasicEvent implements Listener{
	protected Main plugin;
	public BasicEvent(Main main) {
		plugin = main;
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
}

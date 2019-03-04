package net.mysticsouls.pc.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.utils.BasicEvent;

public class JoinLeaveEvent extends BasicEvent {

	public JoinLeaveEvent(Main main) {
		super(main);
	}
	
	@EventHandler
	public void join(PlayerJoinEvent event) {
		plugin.getUserManager().addUser(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void join(PlayerQuitEvent event) {
		plugin.getUserManager().removeUser(event.getPlayer().getUniqueId());
	}
	
	@EventHandler
	public void join(PlayerKickEvent event) {
		plugin.getUserManager().removeUser(event.getPlayer().getUniqueId());
	}

}

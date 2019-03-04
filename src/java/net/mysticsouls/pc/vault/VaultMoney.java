package net.mysticsouls.pc.vault;

import java.util.UUID;

import org.bukkit.Bukkit;

import net.milkbowl.vault.economy.Economy;
import net.mysticsouls.pc.Main;

public class VaultMoney {
	private Economy economy;
	
	public VaultMoney(Main plugin) {
		economy = plugin.getServer().getServicesManager().getRegistration(Economy.class).getProvider();
	}

	public double getMoney(UUID id) {
		return economy.getBalance(Bukkit.getPlayer(id));
	}

	public void payMoney(UUID id, double amount) {
		economy.withdrawPlayer(Bukkit.getPlayer(id), amount);
	}
}

package net.mysticsouls.pc.vault;

import net.milkbowl.vault.economy.Economy;
import net.mysticsouls.pc.Main;
import org.bukkit.Bukkit;

import java.util.UUID;

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

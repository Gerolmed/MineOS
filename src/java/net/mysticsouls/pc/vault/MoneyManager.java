package net.mysticsouls.pc.vault;

import net.mysticsouls.pc.Main;

import java.util.UUID;

public class MoneyManager {

    private VaultMoney money;

    public MoneyManager(Main plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") != null)
            money = new VaultMoney(plugin);
    }

    public double getMoney(UUID id) {

        if (money == null)
            return 0;

        return money.getMoney(id);
    }


    public void payMoney(UUID id, double amount) {

        if (money == null)
            return;

        money.payMoney(id, amount);
    }
}

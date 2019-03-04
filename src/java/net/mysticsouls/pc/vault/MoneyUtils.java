package net.mysticsouls.pc.vault;

import net.mysticsouls.pc.Main;

import java.util.UUID;

public class MoneyUtils {

    private static VaultMoney money;

    public static void init(Main plugin) {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") != null)
            money = new VaultMoney(plugin);
    }

    public static double getMoney(UUID id) {

        if (money == null)
            return 0;

        return money.getMoney(id);
    }


    public static void payMoney(UUID id, double amount) {

        if (money == null)
            return;

        money.payMoney(id, amount);
    }
}

package net.mysticsouls.pc.utils;

import net.mysticsouls.pc.Main;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    protected Main plugin;
    private String name;
    private String permission;
    private String usage;

    public SubCommand(Main main, String name, String permission, String usage) {
        plugin = main;
        this.name = name;
        this.permission = permission;
        this.usage = usage;
    }

    public abstract boolean execute(CommandSender sender, String alias, String[] args);

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }

    public String getUsage() {
        return usage;
    }
}

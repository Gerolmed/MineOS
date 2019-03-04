package net.mysticsouls.pc.utils;

import net.mysticsouls.pc.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;

public abstract class BasicCommand extends BukkitCommand {

    protected Main plugin;

    public BasicCommand(Main main, String name) {
        super(name);
        plugin = main;
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");

            bukkitCommandMap.setAccessible(true);
            CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            commandMap.register(name, this);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public abstract boolean execute(CommandSender sender, String alias, String[] args);
}

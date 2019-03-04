package net.mysticsouls.pc.commands;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.utils.BasicCommand;
import net.mysticsouls.pc.utils.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;

public class MineOSBaseCommand extends BasicCommand {

    private ArrayList<SubCommand> subCommands;

    public MineOSBaseCommand(Main main) {
        super(main, "mos");
        subCommands = new ArrayList<>();

        addSubCommand(new OpenComputer(plugin));
    }

    private void addSubCommand(SubCommand subCommand) {
        subCommands.add(subCommand);
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(args.length == 0) {
            sendHelp(sender);
            return true;
        }

        for(SubCommand subCommand : subCommands) {
            if(subCommand.getName().equalsIgnoreCase(args[0]) && (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))) {
                subCommand.execute(sender, alias, reduce(args));
                return true;
            }
        }

        sendHelp(sender);
        return true;
    }

    private String[] reduce(String[] args) {
        String[] output = new String[args.length-1];

        for(int i = 1; i < args.length; i++)
            output[i-1] = args[i];

        return output;
    }

    private void sendHelp(CommandSender sender) {
        sender.sendMessage("   ");
        sender.sendMessage("§7======§a[§6Mine OS§a]§7======");
        for(SubCommand subCommand : subCommands) {
            if(subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))
                sender.sendMessage("§7 - "+subCommand.getUsage());
        }
        sender.sendMessage("§7=================");
        sender.sendMessage("   ");
    }
}

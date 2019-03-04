package net.mysticsouls.pc.commands;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.user.User;
import net.mysticsouls.pc.utils.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenComputer extends SubCommand {

    public OpenComputer(Main main) {
        super(main, "open", null, "/mos open");
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {
        if(!(sender instanceof Player))
            return true;

        Player player = (Player) sender;

        User user = plugin.getUserManager().getUser(player.getUniqueId());

        if(user == null) {
            player.sendMessage("§cOops! An error occurred. You should consider reconnecting!");
            return true;
        }

        user.openComputer();
        return true;
    }
}

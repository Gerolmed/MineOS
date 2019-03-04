package net.mysticsouls.pc.events;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.user.User;
import net.mysticsouls.pc.utils.BasicEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickPCEvent extends BasicEvent {

    public ClickPCEvent(Main main) {
        super(main);
    }

    @EventHandler
    public void clickBlock(PlayerInteractEvent event) {
        User user = plugin.getUserManager().getUser(event.getPlayer().getUniqueId());
        if (user == null)
            return;

        if (user.isInComputer()) {
            if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)
                user.getComputer().handleInput(Input.RIGHT_CLICK);
            else if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK)
                user.getComputer().handleInput(Input.LEFT_CLICK);
        } else
            user.openComputer();
    }

    @EventHandler
    public void clickItem(InventoryClickEvent event) {
        if (event.getWhoClicked() == null || event.getClickedInventory() == null)
            return;

        Computer computer = plugin.getUserManager().getUser(event.getWhoClicked().getUniqueId()).getComputer();

        if (computer == null)
            return;

        if (computer.getScreen().isScreen(event.getClickedInventory().getTitle()))
            computer.handleClick(event);
    }

}

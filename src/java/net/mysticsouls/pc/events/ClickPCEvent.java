package net.mysticsouls.pc.events;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.user.User;
import net.mysticsouls.pc.utils.BasicEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

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

        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();

        //Disallow moving the pc item
        if(itemStack != null &&  plugin.getUserManager().getUser(player.getUniqueId()).isInComputer()){
            if(event.getWhoClicked().getInventory().getItemInMainHand().equals(itemStack)) {
                event.setCancelled(true);
                return;
            }
        }

        Computer computer = plugin.getUserManager().getUser(player.getUniqueId()).getComputer();

        if (computer == null)
            return;

        if (computer.getScreen().isScreen(event.getClickedInventory().getTitle()))
            computer.handleClick(event);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void switchHeldItem(PlayerSwapHandItemsEvent event) {
        if(plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).isInComputer())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void switchHeldItem(PlayerItemHeldEvent event) {
        if(plugin.getUserManager().getUser(event.getPlayer().getUniqueId()).isInComputer())
            event.setCancelled(true);
    }

}

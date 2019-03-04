package net.mysticsouls.pc.computer.apps.StandardApps;

import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.apps.App;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.computer.render.Render;
import net.mysticsouls.pc.textures.BasicResources;
import net.mysticsouls.pc.utils.ItemUtils;
import net.mysticsouls.pc.utils.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class SettingsApp extends App {

    private Settings settings;

    private String settingRenderType = "§&RenderType";

    public SettingsApp() {
        super("settings", ItemUtils.createItem(Material.LEVER,"§6Settings",""), BasicResources.getBasicImage("icons/settings.png"), "Settings");
    }

    @Override
    public App getNewInstance(Computer computer) {
        return new SettingsApp().setComputer(computer);
    }

    @Override
    protected App setComputer(Computer computer) {

        //Retrieve App settings of user
        settings = new Settings(Render.RenderType.MAP);

        return super.setComputer(computer);
    }

    @Override
    public boolean isStandardApp() {
        return true;
    }

    @Override
    public Render.RenderType getRender() {
        return Render.RenderType.INVENTORY;
    }

    @Override
    public Render draw(Render render) {
        if(render.getRenderType() == Render.RenderType.INVENTORY) {
            Inventory inventory = render.getInventory();

            for(int i = 0; i < inventory.getSize(); i++)
                inventory.setItem(i, ItemUtils.createItem(Material.STAINED_GLASS_PANE,"§6 ", null));

            inventory.setItem(0, ItemUtils.addLore(ItemUtils.createItem(Material.PAINTING, settingRenderType, "§7Click to toggle"), "§6Current: §a"+settings.getRenderType()));
        } else {
            //TODO: Map view
        }

        return super.draw(render);
    }


    @Override
    public void handleClick(InventoryClickEvent event) {
        ItemStack itemStack = event.getCurrentItem();


        if(!event.getClickedInventory().getTitle().equals(getComputer().getScreen().getDisplayedInventory().getTitle()))
            return;

        if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase(settingRenderType)) {
            settings.toggleRenderType();
            getComputer().render();
            return;
        }
        super.handleClick(event);
    }
}

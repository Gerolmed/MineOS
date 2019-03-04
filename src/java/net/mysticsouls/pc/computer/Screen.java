package net.mysticsouls.pc.computer;

import net.mysticsouls.pc.computer.render.MapRender;
import net.mysticsouls.pc.computer.render.Render;
import net.mysticsouls.pc.computer.render.Render.RenderType;
import net.mysticsouls.pc.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapPalette;
import org.bukkit.map.MapView;

public class Screen {

    private Inventory inventory;
    private Inventory lastInventory;
    private Computer computer;
    private MapView mapView;
    private MapRender mapRender;

    public Screen(Computer computer, int i, int j, MapView mapView) {
        this.computer = computer;
        inventory = Bukkit.createInventory(null, i * j, "Screen");
        mapView.getRenderers().clear();
        this.mapView = mapView;
        mapRender = new MapRender();
        mapView.getRenderers().clear();
        mapView.addRenderer(mapRender);
    }

    public Inventory getDisplayedInventory() {
        return inventory;
    }

    public MapView getMapView() {
        return mapView;
    }

    public boolean isScreen(String title) {
        if (inventory != null && inventory.getTitle().equals(title))
            return true;
        if (lastInventory != null && lastInventory.getTitle().equals(title))
            return true;
        return false;
    }

    public void clearScreen() {
        if (lastInventory != null)
            lastInventory.clear();
        if (inventory != null)
            inventory.clear();
    }

    @SuppressWarnings("deprecation")
    public void drawScreen(RenderType type) {
        Render render = new Render(type, getDisplayedInventory(), mapRender.getMapCanvas());
        if (type == RenderType.INVENTORY) {
            if (render.getInventory() != null)
                render.getInventory().clear();
        } else if (type == RenderType.MAP) {
            if (render.getRenderer() != null) {
                //Background
                for (int x = 0; x < 128; x++) {
                    for (int y = 0; y < 128; y++) {
                        render.getRenderer().setPixel(x, y, MapPalette.TRANSPARENT);
                    }
                }
            }
        }

        render = computer.draw(render);

        Player player = computer.getUser().getPlayer();
        if (render.getRenderType() == RenderType.INVENTORY) {
            lastInventory = render.getInventory();
            Inventory currInventory = player.getOpenInventory().getTopInventory();
            if (currInventory == null || !currInventory.equals(lastInventory))
                player.openInventory(lastInventory);
            removeMapItem();
        } else {
            player.closeInventory();
            createMapItem();

            mapRender.render();
        }
    }

    public void removeMapItem() {
        Player player = computer.getUser().getPlayer();

        ItemStack map = ItemUtils.createItem(Material.NETHER_STAR, "§6Computer", null);

        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), map);

    }

    private void createMapItem() {
        Player player = computer.getUser().getPlayer();

        @SuppressWarnings("deprecation")
        ItemStack map = ItemUtils.createItem(Material.MAP, "§6Computer", null, mapView.getId());

        ItemStack curItem = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        if (curItem != null && curItem.getDurability() == map.getDurability())
            return;

        player.getInventory().setItem(player.getInventory().getHeldItemSlot(), map);

    }

    public MapRender getRender() {
        return mapRender;
    }


}

package net.mysticsouls.pc.computer.render;

import org.bukkit.inventory.Inventory;
import org.bukkit.map.MapCanvas;

public class Render {

    private RenderType type;
    private Inventory inventory;
    private MapCanvas renderer;

    public Render(RenderType type, Inventory inventory, MapCanvas renderer) {
        this.type = type;
        this.setInventory(inventory);
        this.setRenderer(renderer);

    }


    public RenderType getRenderType() {
        return type;
    }

    public MapCanvas getRenderer() {
        return renderer;
    }


    public void setRenderer(MapCanvas renderer) {
        this.renderer = renderer;
    }

    public Inventory getInventory() {
        return inventory;
    }


    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public enum RenderType {
        INVENTORY, MAP
    }

}

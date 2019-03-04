package net.mysticsouls.pc.computer.apps;

import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.Drawable;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.computer.render.Render;
import net.mysticsouls.pc.computer.render.Render.RenderType;
import net.mysticsouls.pc.textures.BasicResources;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.awt.*;

public abstract class App implements Drawable {

    protected Image iconImg;
    protected ItemStack icon;
    private String name;
    private Computer computer;
    private SaleInformation information;
    private String registryName;


    //Image 64 * 64
    public App(String registryName, ItemStack icon, Image iconImg, String name) {
        this.icon = icon;
        this.iconImg = iconImg;
        this.registryName = registryName;

        if (this.iconImg == null) {
            this.iconImg = BasicResources.getBasicImage("icons/missing_icon.png");
        }

        this.name = name;

    }

    public abstract App getNewInstance(Computer computer);

    public RenderType getRender() {
        return getComputer().getSystemRenderType();
    }

    public void start() {

    }

    public void stop() {

    }

    public void shutdown() {

    }

    public boolean hotBarEnabled() {
        return false;
    }

    protected Computer getComputer() {
        return computer;
    }

    protected App setComputer(Computer computer) {
        this.computer = computer;
        return this;
    }

    public String getName() {
        return name;
    }

    public String getRegistryName() {
        return registryName;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public SaleInformation getInformation() {
        return information;
    }

    public Image getIconImage() {
        return iconImg;
    }

    @Override
    public Render draw(Render render) {
        return render;
    }

    public void handleClick(InventoryClickEvent event) {

    }

    public void loadAsync() {
    }

    protected void setSaleInformation(SaleInformation information) {
        this.information = information;
    }

    public void handleInput(Input input) {
        if (input == Input.SNEAK)
            getComputer().stopApp();
    }

    public boolean isStandardApp() {
        return false;
    }


}

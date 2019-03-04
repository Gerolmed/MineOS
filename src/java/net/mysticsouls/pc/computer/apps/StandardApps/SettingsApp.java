package net.mysticsouls.pc.computer.apps.StandardApps;

import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.apps.App;
import net.mysticsouls.pc.textures.BasicResources;
import net.mysticsouls.pc.utils.ItemUtils;
import org.bukkit.Material;

public class SettingsApp extends App {
    public SettingsApp() {
        super("settings", ItemUtils.createItem(Material.LEVER,"§6Settings",""), BasicResources.getBasicImage("icons/settings.png"), "Settings");
    }

    @Override
    public App getNewInstance(Computer computer) {
        return new SettingsApp().setComputer(computer);
    }

    @Override
    public boolean isStandardApp() {
        return true;
    }
}

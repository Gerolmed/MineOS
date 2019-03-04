package net.mysticsouls.pc.computer;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.computer.apps.App;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.computer.render.Render;
import net.mysticsouls.pc.computer.render.Render.RenderType;
import net.mysticsouls.pc.computer.threads.BootThread;
import net.mysticsouls.pc.user.User;
import net.mysticsouls.pc.utils.DisplayHelper;
import net.mysticsouls.pc.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.map.MapView;

import java.util.ArrayList;

public class Computer implements Drawable {

    private Desktop desktop;
    private App currApp;
    private Screen screen;
    private boolean loaded = false;
    private ArrayList<App> unlockedApps;

    private Main plugin;

    private User user;

    public Computer(User user, Main plugin) {
        this.plugin = plugin;
        unlockedApps = new ArrayList<>();
        this.user = user;

        Player player = user.getPlayer();

        plugin.getSeatManager().makeSitDown(player);

        MapView view = Bukkit.createMap(player.getLocation().getWorld());

        this.screen = new Screen(this, 9, 6, view);
        this.desktop = new Desktop(this, 9, 6);

        playBootUp();

        addApps();

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadApps();
            }
        }).start();

    }

    public void setLoaded() {
        loaded = true;
    }

    private void playBootUp() {
        BootThread boot = new BootThread(this);
        int taskId = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), boot, 0, boot.getTime()).getTaskId();
        boot.setId(taskId);
    }

    private void addApps() {
        unlockedApps = plugin.getAppManager().getApps(getUser(), this);
    }


    public Desktop getDesktop() {
        return desktop;

    }

    private void loadApps() {
        for (App app : unlockedApps)
            app.loadAsync();
    }

    public Main getPlugin() {
        return plugin;
    }

    public Screen getScreen() {
        return screen;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<App> getApps() {
        return unlockedApps;
    }

    public void startApp(App app) {
        if (!loaded)
            return;

        stopApp();
        currApp = app;
        currApp.start();
        render();
    }

    public App stopApp() {
        if (currApp != null)
            currApp.stop();
        App lastApp = currApp;
        currApp = null;
        render();
        return lastApp;
    }

    @Override
    public Render draw(Render render) {
        boolean hotbar = true;

        if (!loaded)
            return render;

        if (currApp != null) {
            render = currApp.draw(render);
            hotbar = currApp.hotBarEnabled();
        } else
            render = desktop.draw(render);

        if (hotbar && render.getRenderType() == RenderType.INVENTORY)
            drawHotbar(render.getInventory());


        return render;
    }

    private void drawHotbar(Inventory inventory) {
        for (int i = inventory.getSize() - 9; i < inventory.getSize(); i++) {
            inventory.setItem(i, ItemUtils.spacer(3));
        }
        inventory.setItem(inventory.getSize() - 9, ItemUtils.createItem(Material.STAINED_GLASS_PANE, "§cShutdown", null, (short) 14));
    }

    public void stop() {
        stopApp();
        Player player = user.getPlayer();
        plugin.getSeatManager().makeStandUp(player.getUniqueId());
        loaded = false;
        for (App app : unlockedApps)
            app.shutdown();
        screen.removeMapItem();
        user.clearComputer();
        if (player != null)
            player.closeInventory();
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getSlot() == DisplayHelper.getSlotFromCoord(0, 5))
            stop();

        if (currApp != null)
            currApp.handleClick(event);
        else
            desktop.handleClick(event);
    }

    public App getRunningApp() {
        return currApp;
    }

    public void render() {
        RenderType type = RenderType.INVENTORY;

        if (currApp != null)
            type = currApp.getRender();
        else
            type = getSystemRenderType();

        if (loaded)
            getScreen().drawScreen(type);

    }

    public RenderType getSystemRenderType() {
        return RenderType.MAP;
    }

    public void handleInput(Input input) {
        if (currApp != null)
            currApp.handleInput(input);
        else
            desktop.handleInput(input);
    }


    /**
     * Adds a App to the app list. If it is there already, it will not do anything
     *
     * @param app - The app to add
     */
    public void addApp(App app) {
        if (hasApp(app))
            return;

        getApps().add(app.getNewInstance(this));

        saveApps();

    }

    private void saveApps() {
        plugin.getAppManager().setApps(user, this);
    }

    public boolean hasApp(App app) {
        for (App ownedApp : getApps())
            if (ownedApp.getRegistryName().equals(app.getRegistryName()))
                return true;
        return false;
    }
}

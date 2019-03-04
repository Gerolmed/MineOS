package net.mysticsouls.pc.computer.apps;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.user.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class AppManager {

    private ArrayList<App> apps = new ArrayList<>();
    private Main plugin;

    public AppManager(Main plugin) {
        this.plugin = plugin;
    }

    public void registerApp(App app) {
        apps.add(app);
    }

    public App[] getApps() {
        return apps.toArray(new App[apps.size()]);
    }

    public App[] getAvailableApps(User user) {
        Player player = user.getPlayer();
        ArrayList<App> availables = new ArrayList<>();
        for (App app : apps) {
            if (app.getInformation() != null && !app.isStandardApp()) {
                if (app.getInformation().getPermission() != null) {
                    if (player.hasPermission(app.getInformation().getPermission()))
                        availables.add(app);
                } else {
                    availables.add(app);
                }
            }
        }
        return availables.toArray(new App[availables.size()]);
    }

    public ArrayList<App> getApps(User user, Computer computer) {
        ArrayList<App> appList = new ArrayList<>();

        ArrayList<String> appNameList = plugin.getBackend().getApps(user.getUUID());

        for (App app : apps) {
            if (app.isStandardApp() || appNameList.contains(app.getRegistryName()))
                appList.add(app.getNewInstance(computer));
        }

        return appList;
    }

    public void setApps(User user, Computer computer) {
        ArrayList<App> appList = computer.getApps();

        ArrayList<String> appNameList = new ArrayList<>();

        for (App app : appList) {
            if (!app.isStandardApp())
                appNameList.add(app.getRegistryName());
        }

        plugin.getBackend().setApps(user.getUUID(), appNameList);
    }

}

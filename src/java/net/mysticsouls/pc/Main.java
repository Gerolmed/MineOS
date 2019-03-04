package net.mysticsouls.pc;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import net.mysticsouls.pc.ConfigHolder.Configs;
import net.mysticsouls.pc.backend.Backend;
import net.mysticsouls.pc.backend.FileBackend;
import net.mysticsouls.pc.computer.apps.AppManager;
import net.mysticsouls.pc.computer.apps.StandardApps.AppStore;
import net.mysticsouls.pc.computer.apps.test.TestApp;
import net.mysticsouls.pc.computer.input.InputListener;
import net.mysticsouls.pc.computer.input.SeatManager;
import net.mysticsouls.pc.events.ClickPCEvent;
import net.mysticsouls.pc.events.JoinLeaveEvent;
import net.mysticsouls.pc.user.UserManager;
import net.mysticsouls.pc.utils.CooldownManager;
import net.mysticsouls.pc.vault.MoneyUtils;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    private static Main instance;

    private UserManager userManager;
    private CooldownManager cooldownManager;
    private AppManager appManager;
    private ProtocolManager protocolManager;
    private SeatManager seatManager;
    private ConfigHolder configHolder;
    private Backend backend;

    public static Main getInstance() {
        return instance;

    }

    @Override
    public void onEnable() {
        instance = this;

        protocolManager = ProtocolLibrary.getProtocolManager();
        configHolder = new ConfigHolder(this);
        seatManager = new SeatManager(this);
        userManager = new UserManager();
        appManager = new AppManager(this);
        cooldownManager = new CooldownManager();

        if ("FILE".equalsIgnoreCase(Configs.CONFIG.getConfig().getString("Backend")))
            backend = new FileBackend();

        cooldownManager.Update(this);

        new JoinLeaveEvent(this);
        new ClickPCEvent(this);

        protocolManager.addPacketListener(new InputListener(this));

        appManager.registerApp(new AppStore());
        appManager.registerApp(new TestApp());

        MoneyUtils.init(this);
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public AppManager getAppManager() {
        return appManager;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public SeatManager getSeatManager() {
        return seatManager;
    }

    public Backend getBackend() {
        return backend;
    }

    public ConfigHolder getConfigHolder() {
        return configHolder;
    }
}

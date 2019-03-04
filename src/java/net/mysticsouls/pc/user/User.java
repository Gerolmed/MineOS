package net.mysticsouls.pc.user;

import net.mysticsouls.pc.Main;
import net.mysticsouls.pc.computer.Computer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class User {
    private UUID uuid;

    private Computer computer;
    private boolean inComputer;

    public User(UUID uuid) {
        this.uuid = uuid;
        inComputer = false;
    }

    /**
     * Get UUID of User
     *
     * @return
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Get Bukkit Player instance of the User
     *
     * @return
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(getUUID());
    }


    public void openComputer() {
        if (computer == null)
            computer = new Computer(this, Main.getInstance());
        else
            computer.render();
        inComputer = true;
    }

    public Computer getComputer() {
        return computer;
    }

    public void closeComputer() {
        if (computer == null)
            return;
        computer.stop();
        inComputer = false;
    }

    public void clearComputer() {
        computer = null;
    }

    public boolean isInComputer() {
        return inComputer;
    }

}

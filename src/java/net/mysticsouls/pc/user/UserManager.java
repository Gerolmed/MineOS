package net.mysticsouls.pc.user;

import java.util.HashMap;
import java.util.UUID;

public class UserManager {

    private HashMap<UUID, User> users = new HashMap<>();

    public void addUser(UUID id) {
        users.put(id, new User(id));
    }

    public User getUser(UUID id) {
        return users.get(id);
    }

    public boolean isUser(UUID id) {
        return users.containsKey(id);
    }

    public void removeUser(UUID id) {
        User user = users.get(id);
        if (user == null)
            return;
        user.closeComputer();
        users.remove(id);
    }

}

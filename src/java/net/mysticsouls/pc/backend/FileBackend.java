package net.mysticsouls.pc.backend;

import net.mysticsouls.pc.ConfigHolder.Configs;

import java.util.ArrayList;
import java.util.UUID;

public class FileBackend extends Backend {

    @Override
    public ArrayList<String> getApps(UUID id) {
        Object obj = Configs.USER_DATA.getConfig().getList(id.toString());
        if (obj == null)
            return new ArrayList<String>();

        @SuppressWarnings("unchecked")
        ArrayList<String> list = (ArrayList<String>) obj;

        return list;
    }

    @Override
    public void setApps(UUID id, ArrayList<String> list) {
        Configs.USER_DATA.getConfig().set(id.toString(), list);
        Configs.USER_DATA.saveConfig();
    }

}

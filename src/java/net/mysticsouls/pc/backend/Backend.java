package net.mysticsouls.pc.backend;

import java.util.ArrayList;
import java.util.UUID;

public abstract class Backend {

    public abstract ArrayList<String> getApps(UUID id);

    public abstract void setApps(UUID id, ArrayList<String> list);

}

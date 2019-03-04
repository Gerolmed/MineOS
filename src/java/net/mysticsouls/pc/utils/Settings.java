package net.mysticsouls.pc.utils;

import net.mysticsouls.pc.computer.render.Render;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.LinkedHashMap;
import java.util.Map;


@SerializableAs("MineOSSettings")
public class Settings implements Cloneable, ConfigurationSerializable {
    private Render.RenderType renderType;

    public Settings(Render.RenderType renderType) {
        this.renderType = renderType;
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> list = new LinkedHashMap<>();

        list.put("renderType", renderType.toString());

        return list;
    }

    public static Settings deserialize(Map<String, Object> args) {
        return new Settings(
                Render.RenderType.valueOf((String) args.get("renderType"))
        );

    }

    public Render.RenderType getRenderType() {
        return renderType;
    }

    public void toggleRenderType() {
        int num = renderType.ordinal() +1;

        if(num >= Render.RenderType.values().length)
            num = 0;

        renderType = Render.RenderType.values()[num];
    }
}

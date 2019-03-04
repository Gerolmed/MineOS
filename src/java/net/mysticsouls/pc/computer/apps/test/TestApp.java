package net.mysticsouls.pc.computer.apps.test;

import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.apps.App;
import net.mysticsouls.pc.computer.apps.SaleInformation;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.computer.render.Render;
import net.mysticsouls.pc.computer.render.Render.RenderType;
import net.mysticsouls.pc.utils.ItemUtils;
import org.bukkit.Material;
import org.bukkit.map.MapPalette;

public class TestApp extends App {

    private boolean clicked;


    public TestApp() {
        super("test", ItemUtils.createItem(Material.ACACIA_DOOR, "§6Test App", "§cJust some simple test!"), null, "Test App");
        setSaleInformation(new SaleInformation(this, 0, null, "Amazing tests"));
    }

    @Override
    public App getNewInstance(Computer computer) {
        return new TestApp().setComputer(computer);
    }

    @Override
    public void start() {
        clicked = false;
        super.start();
    }

    @SuppressWarnings("deprecation")
    @Override
    public Render draw(Render render) {
        if (render.getRenderType() == RenderType.MAP) {
            if (clicked) {
                render.getRenderer().setPixel(0, 0, MapPalette.RED);
            }
        }
        return super.draw(render);

    }

    @Override
    public void handleInput(Input input) {
        if (input == null)
            return;

        String cdManager = "testInteractCooldown";
        if (getComputer().getPlugin().getCooldownManager().isOnCooldown(getComputer().getUser().getUUID(), cdManager))
            return;
        getComputer().getPlugin().getCooldownManager().add(getComputer().getUser().getUUID(), cdManager, 10);

        if (input == Input.JUMP) {

            if (clicked) {
                getComputer().stopApp();
                return;
            }

            clicked = true;
            getComputer().render();
        }

        super.handleInput(input);
    }

} 

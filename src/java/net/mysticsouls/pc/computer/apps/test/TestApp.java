package net.mysticsouls.pc.computer.apps.test;

import org.bukkit.Material;
import org.bukkit.map.MapPalette;

import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.apps.App;
import net.mysticsouls.pc.computer.apps.SaleInformation;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.computer.render.Render;
import net.mysticsouls.pc.computer.render.Render.RenderType;
import net.mysticsouls.pc.utils.ItemUtils;

public class TestApp extends App{

	private boolean clicked;
	
	
	public TestApp() {
		super("test", ItemUtils.createItem(Material.ACACIA_DOOR, "§6TESTAPP", "§cBigPICESHIT"), null, "TESTAPP");
		setSaleInformation(new SaleInformation(this, 0, null, "Amazing tests"));
	}

	@Override
	public App getNewInstance(Computer computer) {
		return new TestApp().setComputer(computer);
		
	}
	@Override
	public Render draw(Render render) {
		if(render.getRenderType() == RenderType.MAP) {
			if(clicked) {
				render.getRenderer().setPixel(0, 0, MapPalette.RED);
			}
		}
		return super.draw(render);
		
	} 
	@Override
	public void handleInput(Input input) {
		if(input == null)
			return;
		
		String cdManager = "desktopCooldown";
		if(getComputer().getPlugin().getCooldownManager().isOnCooldown(getComputer().getUser().getUUID(), cdManager))
			return;
		getComputer().getPlugin().getCooldownManager().add(getComputer().getUser().getUUID(), cdManager, 10);
		
		if(input == Input.JUMP) {
			
			if(clicked) {
				shutdown();
				return;
				
			}
			
			clicked = true;
		} 
		super.handleInput(input);
	}

} 

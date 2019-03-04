package net.mysticsouls.pc.computer.threads;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapPalette;

import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.render.Render.RenderType;
import net.mysticsouls.pc.utils.ItemUtils;

public class BootThread implements Runnable {
	
	private int taskId;
	
	private Inventory inventory;
	private Computer computer;
	
	public BootThread(Computer computer) {
		this.computer = computer;
		RenderType type = computer.getSystemRenderType();
		
		computer.getScreen().drawScreen(type);
		this.inventory = computer.getScreen().getDisplayedInventory();
		
		if(type == RenderType.INVENTORY)
		{
			for(int i = 0; i < inventory.getSize(); i++)
				inventory.setItem(i, ItemUtils.spacer(7));
		}
	}
	
	public void setId(int taskId) {
		this.taskId = taskId;
	}
	
	public int getTime() {return 10;}

	protected void stop() {
		computer.setLoaded();
		computer.render();
		Bukkit.getScheduler().cancelTask(taskId);
	}
	
	private int iteration = 0;
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if(iteration >= 8) {
			stop();
			return;
		}
		
		if(computer.getSystemRenderType() == RenderType.INVENTORY)
		{
			for(int i = 0; i < iteration; i++) {
				inventory.setItem(i+28, ItemUtils.createItem(Material.STAINED_GLASS_PANE, "§astarting", null, (short)5));
			}
		} else if(computer.getSystemRenderType() == RenderType.MAP)
		{
			MapCanvas mapCanvas = computer.getScreen().getRender().getMapCanvas();
			
			//Background
			for(int x = 0; x < 128; x++) {
				for(int y = 0; y < 128; y++) {
					mapCanvas.setPixel(x, y, MapPalette.PALE_BLUE);
				}
			}
			
			//dark gray bar
			{
				int length = 12 * 7+2;
				int height = 14; 
				int offsetY = 82;
				int offsetX = 20;
				for(int x = 0; x < length; x++) {
					for(int y = 0; y < height; y++) {
						mapCanvas.setPixel(x + offsetX, y + offsetY, MapPalette.DARK_GRAY);
					}
				}
			}
			
			//light gray bar
			{
				int length = 12 * 7;
				int height = 12; 
				int offsetY = 83;
				int offsetX = 21;
				for(int x = 0; x < length; x++) {
					for(int y = 0; y < height; y++) {
						mapCanvas.setPixel(x + offsetX, y + offsetY, MapPalette.LIGHT_GRAY);
					}
				}
			}
			
			//green bar
			for(int i = 0; i < iteration; i++) {
				int length = 12;
				int height = 12; 
				int offsetY = 83;
				int offsetX = 21+length * i;
				
				
				for(int x = 0; x < length; x++) {
					for(int y = 0; y < height; y++) {
						mapCanvas.setPixel(x + offsetX, y + offsetY, MapPalette.LIGHT_GREEN);
					}
				}
			}
		}
		
		iteration++;
	}

}

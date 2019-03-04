package net.mysticsouls.pc.computer;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.map.MinecraftFont;

import net.mysticsouls.pc.computer.apps.App;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.computer.render.Render;
import net.mysticsouls.pc.computer.render.Render.RenderType;
import net.mysticsouls.pc.textures.BasicResources;
import net.mysticsouls.pc.utils.ItemUtils;

public class Desktop implements Drawable{
	
	private App[] apps;
	private Computer computer;
	
	public Desktop(Computer computer, int xSize, int ySize) {
		this.computer = computer;
		apps = new App[xSize*(ySize-1)];
	}
	
	public Desktop(Computer computer, App[] apps) {
		this.computer = computer;
		this.apps = apps;
	}

	/**
	 * @param slot - use {@link net.mysticsouls.pc.utils.DisplayHelper}
	 */
	public App getApp(int slot) {
		if(slot < apps.length)
			return apps[slot];
		return null;
	}
	
	/**Set a App onto a specific desktop location
	 * @param slot - use {@link net.mysticsouls.pc.utils.DisplayHelper}
	 * @param app
	 */
	public void setApp(int slot, App app) {
		apps[slot] = app;
	}
	
	public int getSize() {
		return apps.length;
	}
	
	public int getAppCount() {
		int size = 0;
		
		for(App app : apps)
			if(app != null)
				size++;
		
		return size;
	}
	
	private int currentItemToDisplay = 0;
	
	@Override
	public Render draw(Render render) {
		
		if(render.getRenderType() == RenderType.INVENTORY) {
			Inventory inventory = render.getInventory();
			if(focusedApp >= 0) {
				for(int i = 0; i < apps.length; i++) {
					if(i == focusedApp)
						inventory.setItem(i, getApp(i).getIcon());
					else
						inventory.setItem(i, ItemUtils.createItem(Material.STAINED_GLASS_PANE, "§6Move here?", null, (short) 8));
				}
			} else {
				int slotId = 0;
				for(App app : apps) {
					
					if(app != null) {
						inventory.setItem(slotId, app.getIcon());
					}
					
					slotId++;
				}
			}
		} else if(render.getRenderType() == RenderType.MAP) {
			ArrayList<App> list = getShortList();
			App app = list.get(currentItemToDisplay);
			
			if(app != null)
			{
				//Display Icon
				{
					int xOffset = 32;
					int yOffset = 32;
					render.getRenderer().drawImage(xOffset, yOffset, app.getIconImage());
				}
				
				//Arrow right
				if(currentItemToDisplay<(list.size()-1))
				{
					int xOffset = 102;
					int yOffset = 60;
					render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/button_right.png"));
				}
				
				//Arrow left
				if(currentItemToDisplay > 0)
				{
					int xOffset = 16;
					int yOffset = 60;
					render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/button_left.png"));
				}
				
				//App Name
				{
					String name = app.getName();
					if(name == null)
						name = "";
					int xOffset = 5;
					int yOffset = 10;
					render.getRenderer().drawText(xOffset, yOffset, MinecraftFont.Font, name);
				}
			}
			
		}
		
		return render;
	}

	private ArrayList<App> getShortList() {
		
		ArrayList<App> list = new ArrayList<>();
		
		for(App app : computer.getApps()) {
			if(app != null)
				list.add(app);		
		}
		
		//Add Stop option
		
		list.add(new App("shutdown", null, BasicResources.getBasicImage("icons/shutdown.png"), " ") {
			
			@Override
			public void start() {
				computer.getUser().closeComputer();
			}
			
			@Override
			public App getNewInstance(Computer computer) {
				return this;
			}
		}.getNewInstance(computer));
		
		return list;
		
	}

	public void handleClick(InventoryClickEvent event) {
		int slot = event.getSlot();
		if(computer.getRunningApp() != null)
			return;
		
		if(slot >= apps.length) {
			if(focusedApp >= 0)
				moveCancel();
			return;
		}
		
		App app = null;
		app = getApp(slot);
		
		if(event.getClick() == ClickType.LEFT && focusedApp >= 0) {
			moveFinish(slot);
			return;
		} else if(focusedApp >= 0) {
			moveCancel();
		}
		
		if(app != null && focusedApp < 0) {
			if(event.getClick() == ClickType.RIGHT)
				moveMode(slot);
			if(event.getClick() == ClickType.DOUBLE_CLICK)
				computer.startApp(app);
		}
			
	}

	
	private int focusedApp = -1;
	
	private void moveMode(int slot) {
		focusedApp = slot;
		computer.render();
	}
	
	private void moveFinish(int slotId) {	
		
		App movingApp = getApp(focusedApp);
		App toReplace = getApp(slotId);
		setApp(slotId, movingApp);
		setApp(focusedApp, toReplace);
		
		focusedApp = -1;
		computer.render();
	}
	private void moveCancel() {	
		focusedApp = -1;
		computer.render();
	}
	
	public void handleInput(Input input) {
		
		if(input == null)
			return;
		
		String cdManager = "desktopCooldown";
		if(computer.getPlugin().getCooldownManager().isOnCooldown(computer.getUser().getUUID(), cdManager))
			return;
		computer.getPlugin().getCooldownManager().add(computer.getUser().getUUID(), cdManager, 10);
		
		if(input == Input.LEFT)
			scrollLeft();
		else if(input == Input.RIGHT)
			scrollRight();
		else if(input == Input.JUMP)
			computer.startApp(getShortList().get(currentItemToDisplay));
			
	}

	private void scrollRight() {
		if(currentItemToDisplay < getShortList().size()-1) {
			currentItemToDisplay++;
			computer.render();
		}
	}

	private void scrollLeft() {
		if(currentItemToDisplay > 0) {
			currentItemToDisplay--;
			computer.render();
		}
	}

}

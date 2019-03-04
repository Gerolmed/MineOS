package net.mysticsouls.pc.computer.apps.StandardApps;

import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.map.MinecraftFont;

import net.mysticsouls.pc.computer.Computer;
import net.mysticsouls.pc.computer.apps.App;
import net.mysticsouls.pc.computer.apps.SaleInformation;
import net.mysticsouls.pc.computer.input.Input;
import net.mysticsouls.pc.computer.render.Render;
import net.mysticsouls.pc.computer.render.Render.RenderType;
import net.mysticsouls.pc.textures.BasicResources;
import net.mysticsouls.pc.utils.ItemUtils;
import net.mysticsouls.pc.vault.MoneyUtils;

public class AppStore extends App {

	private App[] apps;
	private int currentItemToDisplay = 0;
	private App appToPurchase;
	private ShopState state = ShopState.SHOW_LIST;
	
	public AppStore() {
		super("appstore",ItemUtils.createItem(Material.WOOL, "§6AppStore", null),BasicResources.getBasicImage("icons/shop.png"),  "AppStore");
	}
	
	@Override
	public Render draw(Render render) {
		if(render.getRenderType() == RenderType.MAP) {
			
			//ShopTitle
			{
				int xOffset = 0;
				int yOffset = 0;
				render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/label_shop.png"));
			}
			
			if(state == ShopState.SHOW_LIST)
				render = renderApp(render);
			else if(state == ShopState.PURCHASE_SCREEN)
				render = renderPurchase(render);
			else if(state == ShopState.PURCHASE_FAIL || state == ShopState.PURCHASE_COMPLETE)
				render = renderPurchaseProgress(render);
		}
		return super.draw(render);
	}

	@Override
	public void start() {
		super.start();
		UpdateApps();
	}
	
	private void UpdateApps() {
		apps = getComputer().getPlugin().getAppManager().getAvailableApps(getComputer().getUser());
	}
	

	@Override
	public App getNewInstance(Computer computer) {
		return new AppStore().setComputer(computer);
	}
	
	@Override
	public void handleInput(Input input) {
		
		if(input == null)
			return;
		
		String cdManager = "desktopCooldown";
		if(getComputer().getPlugin().getCooldownManager().isOnCooldown(getComputer().getUser().getUUID(), cdManager))
			return;
		getComputer().getPlugin().getCooldownManager().add(getComputer().getUser().getUUID(), cdManager, 10);
		
		if(state == ShopState.SHOW_LIST) {
			if(input == Input.LEFT)
				scrollLeft();
			else if(input == Input.RIGHT)
				scrollRight();
			else if(input == Input.JUMP) {
				appToPurchase = apps[currentItemToDisplay];
				if(getComputer().hasApp(appToPurchase))
					return;
				state = ShopState.PURCHASE_SCREEN;
				getComputer().render();
			}
			super.handleInput(input);
		} else if(state == ShopState.PURCHASE_SCREEN) {
			if(input == Input.JUMP) {
				if(purchase(appToPurchase))
					state = ShopState.PURCHASE_COMPLETE;
				else
					state = ShopState.PURCHASE_FAIL;
				
				getComputer().render();
			} else if(input == Input.SNEAK) {
				state = ShopState.SHOW_LIST;
				getComputer().render();
			}
			return;
		} else if(state == ShopState.PURCHASE_FAIL || state == ShopState.PURCHASE_COMPLETE) {
			if(input != null) 
			{
				state = ShopState.SHOW_LIST;
				getComputer().render();
			}
			return;
		}
		super.handleInput(input);
	}
	
	private boolean purchase(App app) {
		SaleInformation info = app.getInformation();
		
		if(info == null)
			return false;
		
		if(info.getPrice() > 0) {
			UUID id = getComputer().getUser().getUUID();
			double balance = MoneyUtils.getMoney(id);
			
			if(balance < info.getPrice()) {
				return false;
			}
			
			MoneyUtils.payMoney(id, info.getPrice());
		}
		
		getComputer().addApp(app);
		
		return true;
	}

	private void scrollRight() {
		if(currentItemToDisplay < apps.length-1) {
			currentItemToDisplay++;
			getComputer().render();
		}
	}

	private void scrollLeft() {
		if(currentItemToDisplay > 0) {
			currentItemToDisplay--;
			getComputer().render();
		}
	}
	
	
	@Override
	public boolean isStandardApp() {
		return true;
	}
	
	private Render renderPurchaseProgress(Render render) {
		//Background
		{
			int xOffset = 0;
			int yOffset = 0;
			if(state == ShopState.PURCHASE_COMPLETE)
				render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/shop_purchase_complete.png"));
			else if(state == ShopState.PURCHASE_FAIL)
				render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/shop_purchase_fail.png"));
		}
		return render;
	}
	
	private Render renderPurchase(Render render) {
		if(appToPurchase == null)
			return render;
		
		App app = appToPurchase;
		//Background
		{
			int xOffset = 0;
			int yOffset = 13;
			render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/shop_purchase.png"));
		}
		
		//Display Icon
		{
			int xOffset = 32;
			int yOffset = 41;
			render.getRenderer().drawImage(xOffset, yOffset, app.getIconImage());
		}
		
		//App Name
		{
			String name = app.getName();
			if(name == null)
				name = "";
			int xOffset = 11;
			int yOffset = 30;
			render.getRenderer().drawText(xOffset, yOffset, MinecraftFont.Font, name);
		}
		
		//App Price
		{
			if(app.getInformation().getPrice() == 0)
				//price FREE
			{
				int xOffset = 30;
				int yOffset = 108;
				render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/price_free_shop.png"));
			} else
				//price
			{
				int xOffset = 30;
				int yOffset = 108;
				render.getRenderer().drawText(xOffset, yOffset, MinecraftFont.Font, app.getInformation().getPrice()+"");
			}
			
		}
		
		return render;
	}
	
	private Render renderApp(Render render) {
		App app = apps[currentItemToDisplay];
		
		if(app != null)
		{
			//Display Icon
			{
				int xOffset = 32;
				int yOffset = 16;
				render.getRenderer().drawImage(xOffset, yOffset, app.getIconImage());
			}
			
			//Arrow right
			if(currentItemToDisplay<(apps.length-1))
			{
				int xOffset = 102;
				int yOffset = 50;
				render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/button_right.png"));
			}
			
			//Arrow left
			if(currentItemToDisplay > 0)
			{
				int xOffset = 16;
				int yOffset = 50;
				render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/button_left.png"));
			}
			
			//App Name
			{
				String name = app.getName();
				if(name == null)
					name = "";
				int xOffset = 5;
				int yOffset = 81;
				render.getRenderer().drawText(xOffset, yOffset, MinecraftFont.Font, name);
			}
			
			//App Price
			{
				//price text
				{
					int xOffset = 5;
					int yOffset = 90;
					render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/price_shop.png"));
				}
				if(getComputer().hasApp(app))
					//Already owns app
				{
					int xOffset = 41;
					int yOffset = 90;
					render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/price_own_shop.png"));
				} else if(app.getInformation().getPrice() == 0)
					//price FREE
				{
					int xOffset = 41;
					int yOffset = 90;
					render.getRenderer().drawImage(xOffset, yOffset, BasicResources.getBasicImage("ui/price_free_shop.png"));
				} else
					//price
				{
					int xOffset = 41;
					int yOffset = 90;
					render.getRenderer().drawText(xOffset, yOffset, MinecraftFont.Font, "  "+ app.getInformation().getPrice());
				}
				
			}
			
			//App Description
			{
				SaleInformation info = app.getInformation();
				//Line 1
				{
					String line = info.getDescription()[0];
					int xOffset = 5;
					int yOffset = 100;
					render.getRenderer().drawText(xOffset, yOffset, MinecraftFont.Font, line);
				}
				//Line 2
				{
					String line = info.getDescription()[1];
					int xOffset = 5;
					int yOffset = 109;
					render.getRenderer().drawText(xOffset, yOffset, MinecraftFont.Font, line);
				}
				//Line 3
				{
					String line = info.getDescription()[2];
					int xOffset = 5;
					int yOffset = 118;
					render.getRenderer().drawText(xOffset, yOffset, MinecraftFont.Font, line);
				}
			}
		}
		return render;
	}
	
	private enum ShopState {
		SHOW_LIST,PURCHASE_SCREEN,PURCHASE_FAIL,PURCHASE_COMPLETE
	}
}
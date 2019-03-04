package net.mysticsouls.pc.computer.apps;

import org.bukkit.inventory.ItemStack;

public class SaleInformation {

    private int price;
    private String permission;
    private App app;
    private String[] description;

    /**
     * Creates the Informations needed in the Shop
     *
     * @param price
     * @param permission
     * @param description - Description below item in shop max. 3 lines
     */
    public SaleInformation(App app, int price, String permission, String... description) {
        this.price = price;
        this.description = description;
        this.permission = permission;
        this.app = app;

        if (this.description == null)
            this.description = new String[]{"", "", ""};
        else if (this.description.length < 3) {
            description = new String[]{"", "", ""};
            for (int i = 0; i < this.description.length; i++) {
                String message = this.description[i];
                if (message == null)
                    message = "";
                description[i] = message;
            }
            this.description = description;
        }
    }

    public int getPrice() {
        return price;
    }

    public String getPermission() {
        return permission;
    }

    public App getApp() {
        return app;
    }

    public String[] getDescription() {
        return description;
    }

    public ItemStack toItem() {
        return app.getIcon();
    }

}

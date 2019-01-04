package amata1219.hypering.economy.gui.home;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Type;

public class Notification implements GraphicalUserInterface {

	private Inventory inventory;

	private Notification(){

	}

	public static Notification load(){
		Notification notification = new Notification();

		Inventory inventory = ItemHelper.createInventory(27, Type.NOTIFICATION);

		inventory.setItem(0, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(8, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(9, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(17, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(18, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(26, ItemHelper.createSeasonalColorWool2());

		notification.inventory = inventory;

		return notification;
	}

	public Inventory getInventory(){
		return inventory;
	}

	@Override
	public int getId() {
		return 6;
	}

	@Override
	public void update() {

	}

	@Override
	public void clear() {

	}

	@Override
	public void push(int slotNumber) {

	}

	public void action(Player player, int slotNumber){
		switch(slotNumber){
		default:
			break;
		}
	}

}

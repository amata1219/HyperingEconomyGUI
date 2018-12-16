package amata1219.hypering.economy.gui.home;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import amata1219.hypering.economy.gui.GUIListener;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;

public class Notification implements GraphicalUserInterface {

	private Inventory inventory;

	private Notification(){

	}

	public static Notification load(){
		Notification notification = new Notification();

		Inventory inventory = ItemHelper.createInventory(27, Type.NOTIFICATION);

		ItemStack test = ItemHelper.createItem(Material.RAW_FISH, ChatColor.GOLD + "お知らせっ！", ChatColor.GRAY + "クリックでポップアップを表示");

		inventory.setItem(1, test);

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
		case 1:
			GUIListener.getListener().getGUIManager(player).close();

			Util.success(ChatColor.GOLD + "ポップアップ", ChatColor.GRAY + "説明文", Material.RAW_FISH, player);
			break;
		default:
			break;
		}
	}

}

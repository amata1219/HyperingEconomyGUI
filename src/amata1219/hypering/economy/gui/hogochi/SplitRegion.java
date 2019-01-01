package amata1219.hypering.economy.gui.hogochi;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import amata1219.hogochi.byebye.RegionByebye;
import amata1219.hypering.economy.gui.home.Confirmation;
import amata1219.hypering.economy.gui.home.GUIManager;
import amata1219.hypering.economy.gui.home.GraphicalUserInterface;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Type;

public class SplitRegion implements GraphicalUserInterface {

	private GUIManager manager;
	private Inventory inventory;

	private SplitRegion(){

	}

	public static SplitRegion load(GUIManager manager){
		SplitRegion split = new SplitRegion();

		split.manager = manager;

		Inventory inventory = ItemHelper.createInventory(9, Type.SPLIT_REGION);

		ItemStack result = ItemHelper.createItem(Material.WRITABLE_BOOK, ChatColor.GOLD + "", ChatColor.GRAY + "ここに説明文");

		ItemStack along = ItemHelper.createSkull(ItemHelper.ARROW_RIGHT, ChatColor.GOLD + "X軸に沿って分割する", ChatColor.GRAY + "");

		ItemStack enter = ItemHelper.createItem(Material.PAPER, ChatColor.GOLD + "エンター", ChatColor.GRAY + "");

		ItemStack cancel = ItemHelper.createItem(Material.BONE_MEAL, ChatColor.GOLD + "キャンセル", ChatColor.GRAY + "");

		inventory.setItem(2, result);
		inventory.setItem(3, along);
		inventory.setItem(5, enter);
		inventory.setItem(6, cancel);

		inventory.setItem(0, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(8, ItemHelper.createSeasonalColorWool2());

		split.inventory = inventory;

		return split;
	}

	@Override
	public int getId() {
		return 9;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void update(){
		clear();
	}

	@Override
	public void clear(){
		ItemStack along = ItemHelper.createSkull(isAlongX() ? ItemHelper.ARROW_UP : ItemHelper.ARROW_RIGHT, ChatColor.GOLD + "X軸に沿って分割する", ChatColor.GRAY + "");

		inventory.setItem(3, along);
	}

	@Override
	public void push(int slotNumber) {
		if(slotNumber == 6){
			manager.close();
			return;
		}

		if(slotNumber == 5){
			Confirmation confirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

			confirmation.setResult(ChatColor.GOLD + "確認 | 土地の分割");
			confirmation.changeDisplayNames("分割する", "破棄する");

			manager.display(Type.CONFIRMATION);
			return;
		}

		if(slotNumber != 3)
			return;

		if(!RegionByebye.is50x50((ProtectedRegion) manager.memory.get(3)))
			return;

		ItemStack along = ItemHelper.createSkull(isAlongX() ? ItemHelper.ARROW_UP : ItemHelper.ARROW_RIGHT, ChatColor.GOLD + (isAlongX() ? "Z" : "X") + "軸に沿って分割する", ChatColor.GRAY + "");

		inventory.setItem(3, along);
	}

	public boolean isAlongX(){
		return inventory.getItem(3).getItemMeta().getDisplayName().equals(ChatColor.GOLD + "X軸に沿って分割する");
	}

}


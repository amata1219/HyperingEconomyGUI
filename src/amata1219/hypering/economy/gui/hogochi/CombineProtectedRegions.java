package amata1219.hypering.economy.gui.hogochi;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import amata1219.hogochi.byebye.Compartment;
import amata1219.hogochi.byebye.Direction;
import amata1219.hogochi.byebye.DirectionNumberTable;
import amata1219.hogochi.byebye.ImageMap;
import amata1219.hogochi.byebye.Region;
import amata1219.hogochi.byebye.RegionByebye;
import amata1219.hypering.economy.gui.home.Confirmation;
import amata1219.hypering.economy.gui.home.GUIManager;
import amata1219.hypering.economy.gui.home.GraphicalUserInterface;
import amata1219.hypering.economy.gui.util.Case;
import amata1219.hypering.economy.gui.util.Color;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;

public class CombineProtectedRegions implements GraphicalUserInterface {

	private GUIManager manager;
	private Inventory inventory;

	private Compartment cpm;
	private ImageMap map;

	private CombineProtectedRegions(){

	}

	public static CombineProtectedRegions load(GUIManager manager){
		CombineProtectedRegions combine = new CombineProtectedRegions();

		combine.manager = manager;

		Inventory inventory = ItemHelper.createInventory(18, Type.COMBINE_REGIONS);

		ItemStack result = ItemHelper.createItem(Material.BOOK_AND_QUILL, ChatColor.GOLD + "", ChatColor.GRAY + "ここに説明文");

		ItemStack enter = ItemHelper.createItem(Material.PAPER, ChatColor.GOLD + "エンター", ChatColor.GRAY + "");

		ItemStack cancel = ItemHelper.createItem(Material.INK_SACK, 15, ChatColor.GOLD + "キャンセル", ChatColor.GRAY + "");

		inventory.setItem(2, result);
		inventory.setItem(5, ItemHelper.createColorWool(Color.LIGHT_GRAY, ChatColor.GOLD + ""));
		inventory.setItem(6, ItemHelper.createColorWool(Color.LIGHT_GRAY, ChatColor.GOLD + ""));
		inventory.setItem(14, ItemHelper.createColorWool(Color.LIGHT_GRAY, ChatColor.GOLD + ""));
		inventory.setItem(15, ItemHelper.createColorWool(Color.LIGHT_GRAY, ChatColor.GOLD + ""));
		inventory.setItem(11, enter);
		inventory.setItem(12, cancel);

		inventory.setItem(0, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(8, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(9, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(17, ItemHelper.createSeasonalColorWool1());

		combine.inventory = inventory;

		return combine;
	}

	@Override
	public int getId() {
		return 8;
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
		ItemHelper.changeWoolColor(inventory.getItem(5), Color.LIGHT_GRAY);
		ItemHelper.changeWoolColor(inventory.getItem(6), Color.LIGHT_GRAY);
		ItemHelper.changeWoolColor(inventory.getItem(14), Color.LIGHT_GRAY);
		ItemHelper.changeWoolColor(inventory.getItem(15), Color.LIGHT_GRAY);

		cpm = null;
		map = null;
	}

	@Override
	public void push(int slotNumber){
		if(slotNumber == 12){
			manager.close();
			return;
		}

		Player player = manager.getPlayer();
		Case cs = manager.getCase();

		if(slotNumber == 11){
			if(map.isNonSelected()){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_NOT_SELECTED, Util.caseToMaterial(cs), player);
				return;
			}

			if(map.isDiagonal()){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_DIAGONAL, Util.caseToMaterial(cs), player);
				return;
			}

			if(map.isL()){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_L, Util.caseToMaterial(cs), player);
				return;
			}

			if(map.isAllSelected() && RegionByebye.is25x25((ProtectedRegion) manager.memory.get(3))){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.NEED_MANY_STEP, Util.caseToMaterial(cs), player);
				return;
			}

			Confirmation confirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

			confirmation.update();

			confirmation.setResult(ChatColor.GOLD + "確認 | 土地の結合");

			confirmation.changeDisplayNames("結合する", "破棄する");

			manager.display(Type.CONFIRMATION);
			return;
		}

		if(slotNumber != 5 && slotNumber != 6 && slotNumber != 14 && slotNumber != 15)
			return;

		ItemStack item = inventory.getItem(slotNumber);
		if(ItemHelper.isMatchColor(item, Color.RED)){
			if(!manager.isCooldown()){
				Util.error(Message.FAILED + Message.caseToString(Case.COMBINE_HOGOCHIES), Message.THIS_HOGOCHI_IS_STANDARD, Util.caseToMaterial(Case.COMBINE_HOGOCHIES), manager.getPlayer());
				manager.setCooldown(50);
			}
			return;
		}

		DirectionNumberTable table = cpm.getDirectionNumberTable();

		Region region = cpm.getRegion(table.getDirection(slotNumber));

		ProtectedRegion pr = region.getProtectedRegion();

		if(ItemHelper.isMatchColor(item, Color.YELLOW_GREEN)){
			for(Direction direction : cpm.getDirections(pr))
				ItemHelper.changeWoolColor(inventory.getItem(table.getSlotNumber(direction)), Color.LIGHT_GRAY);

			return;
		}

		if(!region.isProtected()){
			if(!manager.isCooldown()){
				Util.error(Message.FAILED + Message.caseToString(Case.COMBINE_HOGOCHIES), Message.THIS_HOGOCHI_IS_NOT_PROTECTED, Util.caseToMaterial(Case.COMBINE_HOGOCHIES), manager.getPlayer());
				manager.setCooldown(50);
			}
			return;
		}else{
			if(RegionByebye.isAdminRegion(pr)){
				if(!manager.isCooldown()){
					Util.error(Message.FAILED + Message.caseToString(Case.COMBINE_HOGOCHIES), Message.CAN_NOT_COMBINE_ADMIN_REGION, Util.caseToMaterial(Case.COMBINE_HOGOCHIES), manager.getPlayer());
					manager.setCooldown(50);
				}
				return;
			}

			if(!RegionByebye.isOwner(manager.getPlayer(), pr)){
				if(!manager.isCooldown()){
					Util.error(Message.FAILED + Message.caseToString(Case.COMBINE_HOGOCHIES), Message.CAN_NOT_COMBINE_OTHER_PLAYERS_HOGOCHI, Util.caseToMaterial(Case.COMBINE_HOGOCHIES), manager.getPlayer());
					manager.setCooldown(50);
				}
				return;
			}

			if(RegionByebye.isBuyable(pr)){
				if(!manager.isCooldown()){
					Util.error(Message.FAILED + Message.caseToString(Case.COMBINE_HOGOCHIES), Message.CAN_NOT_COMBINE_HOGOCHI_SALE, Util.caseToMaterial(Case.COMBINE_HOGOCHIES), manager.getPlayer());
					manager.setCooldown(50);
				}
				return;
			}

			for(Direction direction : cpm.getDirections(pr))
				ItemHelper.changeWoolColor(inventory.getItem(table.getSlotNumber(direction)), Color.YELLOW_GREEN);
		}
	}

	public void applyProtectedRegion(){
		if(!manager.memory.containsKey(3))
			return;

		ProtectedRegion region = (ProtectedRegion) manager.memory.get(3);

		cpm = new Compartment(region);

		for(Direction direction : cpm.getDirections(region))
			ItemHelper.changeWoolColor(inventory.getItem(cpm.getDirectionNumberTable().getSlotNumber(direction)), Color.RED);

		map = new ImageMap(getResult());
	}

	public boolean[] getResult(){
		boolean[] args = new boolean[4];

		args[0] = ItemHelper.isMatchColor(inventory.getItem(5), Color.RED, Color.YELLOW_GREEN);
		args[1] = ItemHelper.isMatchColor(inventory.getItem(6), Color.RED, Color.YELLOW_GREEN);
		args[2] = ItemHelper.isMatchColor(inventory.getItem(14), Color.RED, Color.YELLOW_GREEN);
		args[3] = ItemHelper.isMatchColor(inventory.getItem(15), Color.RED, Color.YELLOW_GREEN);

		return args;
	}

	public ProtectedRegion[] getProtectedRegions(){
		ProtectedRegion[] regions = new ProtectedRegion[]{(ProtectedRegion) manager.memory.get(3), null};

		map.update(getResult());

		if(map.isNonSelected())
			return null;

		for(int i : map.getSlotNumbers()){
			ProtectedRegion pr = cpm.getRegion(cpm.getDirectionNumberTable().getDirection(i)).getProtectedRegion();
			if(regions[0].getId().equals(pr.getId()))
				continue;

			regions[1] = pr;
			break;
		}

		return regions;
	}

}

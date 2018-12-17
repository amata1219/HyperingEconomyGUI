package amata1219.hypering.economy.gui.hogochi;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import amata1219.hogochi.byebye.ClaimByebye;
import amata1219.hogochi.byebye.RegionByebye;
import amata1219.hypering.economy.gui.HyperingEconomyGUI;
import amata1219.hypering.economy.gui.home.Confirmation;
import amata1219.hypering.economy.gui.home.GUIManager;
import amata1219.hypering.economy.gui.home.GraphicalUserInterface;
import amata1219.hypering.economy.gui.home.NumberScanner;
import amata1219.hypering.economy.gui.util.Case;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Meta;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;
import me.ryanhamshire.GriefPrevention.Claim;

public class HogochiMenu implements GraphicalUserInterface {

	private GUIManager manager;
	private Inventory inventory;

	private HogochiMenu(){

	}

	public static HogochiMenu load(GUIManager manager){
		HogochiMenu menu = new HogochiMenu();

		menu.manager = manager;

		Inventory inventory = ItemHelper.createInventory(9, Type.HOGOCHI_MENU);

		menu.inventory = inventory;

		Player player = manager.getPlayer();

		ItemStack status = ItemHelper.createSkull(player, ChatColor.GOLD + player.getName(), (String[]) null);

		ItemStack buy = ItemHelper.createItem(Material.BOOK_AND_QUILL, ChatColor.GOLD + "土地を購入する", ChatColor.GRAY + "");

		ItemStack sell = ItemHelper.createItem(Material.MAP, ChatColor.GOLD + "土地を販売する", ChatColor.GRAY + "・土地を選択します。", ChatColor.GRAY + "・販売価格を設定します。");

		ItemStack withdraw = ItemHelper.createItem(Material.PAPER, ChatColor.GOLD + "土地の販売を撤回する", ChatColor.GRAY + "");

		inventory.setItem(1, status);
		inventory.setItem(2, buy);
		inventory.setItem(3, sell);
		inventory.setItem(4, withdraw);

		inventory.setItem(0, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(8, ItemHelper.createSeasonalColorWool2());

		menu.update();

		return menu;
	}

	@Override
	public int getId() {
		return 7;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void update(){
		Player player = manager.getPlayer();

		ItemStack status = ItemHelper.createSkull(player, ChatColor.GOLD + player.getName(), (String[]) null);

		ItemHelper.clearLore(status);

		ItemHelper.addLore(status, ChatColor.GRAY + "残りの保護ブロック数: " + HyperingEconomyGUI.getGriefPrevention().dataStore.getPlayerData(player.getUniqueId()).getRemainingClaimBlocks());
		ItemHelper.addLore(status, ChatColor.GRAY + "");
		ItemHelper.addLore(status, ChatColor.GRAY + "残りの保護数:");
		ItemHelper.addLore(status, ChatColor.GRAY + "  メイン: " + (3 - HyperingEconomyGUI.getMainClaimCount(player)));
		ItemHelper.addLore(status, ChatColor.GRAY + "  ネザー: " + (1 - HyperingEconomyGUI.getOtherClaimCount(player, true)));
		ItemHelper.addLore(status, ChatColor.GRAY + "  エンド: " + (1 - HyperingEconomyGUI.getOtherClaimCount(player, false)));
		ItemHelper.addLore(status, ChatColor.GRAY + "  メインフラット: " + (12 - HyperingEconomyGUI.getMainFlatRegionCount(player)));

		if(isUseWorldGuard()){
			ItemStack flatten = ItemHelper.createItem(Material.GRASS, ChatColor.GOLD + "土地を手放す", ChatColor.RED + "・建築物を全て削除し土地を手放します。", ChatColor.RED + "・この操作は元に戻せません。");

			ItemStack combine = ItemHelper.createItem(Material.IRON_SPADE, ChatColor.GOLD + "土地を結合する", ChatColor.GRAY + "・選択した土地を基準に、結合する土地を選択します。", ChatColor.GRAY + "・一度に結合出来る保護は2つまでです。");

			ItemStack split = ItemHelper.createItem(Material.IRON_AXE, ChatColor.GOLD + "土地を分割する", ChatColor.GRAY + "・選択した土地を分割します。", ChatColor.GRAY + "・最大サイズの保護はXZ軸のどちらに沿って分割するか選択出来ます。");

			inventory.setItem(5, flatten);
			inventory.setItem(6, combine);
			inventory.setItem(7, split);
		}else{
			inventory.setItem(5, ItemHelper.createItem(Material.AIR));
			inventory.setItem(6, ItemHelper.createItem(Material.AIR));
			inventory.setItem(7, ItemHelper.createItem(Material.AIR));
		}
	}

	@Override
	public void clear() {
	}

	@Override
	public void push(int slotNumber) {
		Case cs = getCase(slotNumber);
		if(cs == null)
			return;

		Player player = manager.getPlayer();

		Location location = player.getLocation();

		if(isUseWorldGuard()){
			if(!RegionByebye.isExistProtectedRegion(location.getBlockX(), location.getBlockZ())){
				manager.setCase(cs);
				manager.hide();
				Util.normal(Message.SELECT + Message.caseToString(cs), Message.LEFT_CLICK_TO_SELECT_HOGOCHI, Util.caseToMaterial(cs), player);
				return;
			}

			switch(cs){
			case BUY_HOGOCHI:
				buyRegion(location);
				break;
			case SELL_HOGOCHI:
				sellRegion(location);
				break;
			case WITHDRAW_HOGOCHI_SALE:
				withdrawRegionSale(location);
				break;
			case FLATTEN_HOGOCHI:
				flattenRegion(location);
				break;
			case COMBINE_HOGOCHIES:
				combineRegions(location);
				break;
			case SPLIT_HOGOCHI:
				splitRegion(location);
				break;
			default:
				return;
			}
		}else{
			if(!ClaimByebye.isExistClaim(location)){
				manager.setCase(cs);
				manager.hide();
				Util.normal(Message.SELECT + Message.caseToString(cs), Message.LEFT_CLICK_TO_SELECT_HOGOCHI, Util.caseToMaterial(cs), player);
				return;
			}

			switch(cs){
			case BUY_HOGOCHI:
				buyClaim(location);
				break;
			case SELL_HOGOCHI:
				sellClaim(location);
				break;
			case WITHDRAW_HOGOCHI_SALE:
				withdrawClaimSale(location);
				break;
			default:
				return;
			}
		}

		switch(cs){
		case BUY_HOGOCHI:
			Meta.setMeta(player, Meta.BUY_HOGOCHI);
			break;
		case SELL_HOGOCHI:
			Meta.setMeta(player, Meta.SELL_HOGOCHI);
			break;
		case WITHDRAW_HOGOCHI_SALE:
			Meta.setMeta(player, Meta.WITHDRAW_HOGOCHI_SALE);
			break;
		case FLATTEN_HOGOCHI:
			Meta.setMeta(player, Meta.FLATTEN_HOGOCHI);
			break;
		case COMBINE_HOGOCHIES:
			Meta.setMeta(player, Meta.COMBINE_HOGOCHIES);
			break;
		case SPLIT_HOGOCHI:
			Meta.setMeta(player, Meta.SPLIT_HOGOCHI);
			break;
		default:
			return;
		}
	}

	public boolean isUseWorldGuard(){
		return manager.getPlayer().getWorld().getName().equals("main_flat");
	}

	public void buyRegion(Location loc){
		Player player = manager.getPlayer();

		Confirmation confirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

		Location location = loc != null ? loc : player.getLocation();

		ProtectedRegion region = RegionByebye.getProtectedRegion(location.getBlockX(), location.getBlockZ());

		if(!RegionByebye.isBuyable(region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.BUY_HOGOCHI), Message.NOT_SOLD_THIS_HOGOCHI, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
			manager.close();
			return;
		}

		if(RegionByebye.isOwner(player, region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.BUY_HOGOCHI), Message.CAN_NOT_BUY_MY_HOGOCHI, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
			manager.close();
			return;
		}

		if(HyperingEconomyGUI.getMainFlatRegionCount(player) + Util.getCost(region) > 12){
			Util.warn(Message.WARN + Message.caseToString(Case.BUY_HOGOCHI), Message.TOO_MANY_HOGOCHI, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
			return;
		}

		manager.memory.put(3, region);
		manager.memory.put(4, region.getId());

		confirmation.update();

		if(RegionByebye.isAdminRegion(region)){
			confirmation.setResult(ChatColor.GOLD + "確認 | 土地の購入 - ID: " + region.getId() + " チケット160枚");
			Util.normal(Message.CONFIRM + Message.caseToString(Case.BUY_HOGOCHI), ChatColor.GRAY + "ID: " + region.getId() + " チケット160枚", Util.caseToMaterial(Case.BUY_HOGOCHI), player);
		}else{
			confirmation.setResult(ChatColor.GOLD + "確認 | 土地の購入 - ID: " + region.getId() + " ¥" + RegionByebye.getPrice(region));
			Util.normal(Message.CONFIRM + Message.caseToString(Case.BUY_HOGOCHI), ChatColor.GRAY + "ID: " + region.getId() + " ¥" + RegionByebye.getPrice(region), Util.caseToMaterial(Case.BUY_HOGOCHI), player);
		}

		confirmation.changeDisplayNames(ChatColor.GOLD + "購入する", ChatColor.RED + "破棄する");

		manager.setCase(Case.BUY_HOGOCHI);

		manager.display(Type.CONFIRMATION);
	}

	public void buyClaim(Location loc){
		Player player = manager.getPlayer();

		Confirmation confirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

		Location location = loc != null ? loc : player.getLocation();

		Claim claim = ClaimByebye.getClaim(location);

		if(HyperingEconomyGUI.isMax(player, true)){
			Util.warn(Message.WARN + Message.caseToString(Case.BUY_HOGOCHI), Message.TOO_MANY_HOGOCHI, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
			return;
		}

		if(ClaimByebye.isOwner(player, claim)){
			Util.warn(Message.FAILED + Message.caseToString(Case.BUY_HOGOCHI), Message.CAN_NOT_BUY_MY_HOGOCHI, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
			manager.close();
			return;
		}

		if(HyperingEconomyGUI.canBuy(player, claim)){
			Util.warn(Message.FAILED + Message.caseToString(Case.BUY_HOGOCHI), Message.NOT_ENOUGH_CLAIM_BLOCKS, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
			manager.close();
			return;
		}

		if(!ClaimByebye.isBuyable(claim) || claim.isAdminClaim()){
			Util.warn(Message.FAILED + Message.caseToString(Case.BUY_HOGOCHI), Message.NOT_SOLD_THIS_HOGOCHI, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
			manager.close();
			return;
		}

		manager.memory.put(2, claim);
		manager.memory.put(4, claim.getID());

		confirmation.update();

		confirmation.setResult(ChatColor.GOLD + "確認 | 土地の購入 - ID: " + claim.getID() + " ¥" + ClaimByebye.getPrice(claim));
		Util.normal(Message.CONFIRM + Message.caseToString(Case.BUY_HOGOCHI), ChatColor.GRAY + "ID: " + claim.getID() + " ¥" + ClaimByebye.getPrice(claim), Util.caseToMaterial(Case.BUY_HOGOCHI), player);

		confirmation.changeDisplayNames(ChatColor.GOLD + "購入する", ChatColor.RED + "破棄する");

		manager.setCase(Case.BUY_HOGOCHI);

		manager.display(Type.CONFIRMATION);
	}

	public void sellRegion(Location loc){
		Player player = manager.getPlayer();

		Location location = loc != null ? loc : player.getLocation();

		ProtectedRegion region = RegionByebye.getProtectedRegion(location.getBlockX(), location.getBlockZ());

		if(!RegionByebye.isOwner(player, region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.SELL_HOGOCHI), Message.CAN_NOT_SELL_OTHER_PLAYERS_HOGOCHI, Util.caseToMaterial(Case.SELL_HOGOCHI), player);
			manager.close();
			return;
		}

		if(RegionByebye.isBuyable(region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.SELL_HOGOCHI), Message.THIS_HOGOCHI_ALREADY_WAS_SOLD, Util.caseToMaterial(Case.SELL_HOGOCHI), player);
			manager.close();
			return;
		}

		manager.memory.put(3, region);
		manager.memory.put(4, region.getId());

		manager.setCase(Case.SELL_HOGOCHI);

		manager.display(Type.NUMBER_SCANNER);

		((NumberScanner) manager.getGUI(Type.NUMBER_SCANNER)).setDisplayYenMark(true);
	}

	public void sellClaim(Location loc){
		Player player = manager.getPlayer();

		Location location = loc != null ? loc : player.getLocation();

		Claim claim = ClaimByebye.getClaim(location);

		if(!ClaimByebye.isOwner(player, claim)){
			Util.warn(Message.FAILED + Message.caseToString(Case.SELL_HOGOCHI), Message.CAN_NOT_SELL_OTHER_PLAYERS_HOGOCHI, Util.caseToMaterial(Case.SELL_HOGOCHI), player);
			manager.close();
			return;
		}

		if(ClaimByebye.isBuyable(claim)){
			Util.warn(Message.FAILED + Message.caseToString(Case.SELL_HOGOCHI), Message.THIS_HOGOCHI_ALREADY_WAS_SOLD, Util.caseToMaterial(Case.SELL_HOGOCHI), player);
			manager.close();
			return;
		}

		manager.memory.put(2, claim);
		manager.memory.put(4, claim.getID());

		manager.setCase(Case.SELL_HOGOCHI);

		manager.display(Type.NUMBER_SCANNER);

		((NumberScanner) manager.getGUI(Type.NUMBER_SCANNER)).setDisplayYenMark(true);
	}

	public void withdrawRegionSale(Location loc){
		Player player = manager.getPlayer();

		Confirmation confirmation = ((Confirmation) manager.getGUI(Type.CONFIRMATION));

		Location location = loc != null ? loc : player.getLocation();

		ProtectedRegion region = RegionByebye.getProtectedRegion(location.getBlockX(), location.getBlockZ());

		if(!RegionByebye.isOwner(player, region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.WITHDRAW_HOGOCHI_SALE), Message.CAN_NOT_WITHDRAW_OTHER_PLAYERS_HOGOCHI_SALE, Util.caseToMaterial(Case.WITHDRAW_HOGOCHI_SALE), player);
			manager.close();
			return;
		}

		if(!RegionByebye.isBuyable(region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.WITHDRAW_HOGOCHI_SALE), Message.NOT_SOLD_THIS_HOGOCHI, Util.caseToMaterial(Case.WITHDRAW_HOGOCHI_SALE), player);
			manager.close();
			return;
		}

		manager.memory.put(3, region);
		manager.memory.put(4, region.getId());

		confirmation.update();

		confirmation.setResult(ChatColor.GOLD + "確認 | 土地販売の撤回 - ID: " + region.getId() + " 価格: " + "¥" + RegionByebye.getPrice(region));
		confirmation.changeDisplayNames("販売を撤回する", "破棄する");

		manager.setCase(Case.WITHDRAW_HOGOCHI_SALE);

		manager.display(Type.CONFIRMATION);
	}

	public void withdrawClaimSale(Location loc){
		Player player = manager.getPlayer();

		Confirmation confirmation = ((Confirmation) manager.getGUI(Type.CONFIRMATION));

		Location location = loc != null ? loc : player.getLocation();

		Claim claim = ClaimByebye.getClaim(location);

		if(!ClaimByebye.isOwner(player, claim)){
			Util.warn(Message.FAILED + Message.caseToString(Case.WITHDRAW_HOGOCHI_SALE), Message.CAN_NOT_WITHDRAW_OTHER_PLAYERS_HOGOCHI_SALE, Util.caseToMaterial(Case.WITHDRAW_HOGOCHI_SALE), player);
			manager.close();
			return;
		}

		if(!ClaimByebye.isBuyable(claim)){
			Util.warn(Message.FAILED + Message.caseToString(Case.WITHDRAW_HOGOCHI_SALE), Message.NOT_SOLD_THIS_HOGOCHI, Util.caseToMaterial(Case.WITHDRAW_HOGOCHI_SALE), player);
			manager.close();
			return;
		}

		manager.memory.put(2, claim);
		manager.memory.put(4, claim.getID());

		confirmation.setResult(ChatColor.GOLD + "確認 | 土地販売の撤回 - ID: " + claim.getID());
		confirmation.changeDisplayNames("販売を撤回する", "破棄する");

		manager.setCase(Case.WITHDRAW_HOGOCHI_SALE);

		manager.display(Type.CONFIRMATION);
	}

	public void flattenRegion(Location loc){
		Player player = manager.getPlayer();

		Confirmation confirmation = ((Confirmation) manager.getGUI(Type.CONFIRMATION));

		Location location = loc != null ? loc : player.getLocation();

		ProtectedRegion region = RegionByebye.getProtectedRegion(location.getBlockX(), location.getBlockZ());

		if(!RegionByebye.isOwner(player, region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.FLATTEN_HOGOCHI), Message.CAN_NOT_FLATTEN_OTHER_PLAYERS_HOGOCHI, Util.caseToMaterial(Case.FLATTEN_HOGOCHI), player);
			manager.close();
			return;
		}

		if(RegionByebye.isBuyable(region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.FLATTEN_HOGOCHI), Message.CAN_NOT_FLATTEN_HOGOCHI_SALE, Util.caseToMaterial(Case.FLATTEN_HOGOCHI), player);
			manager.close();
			return;
		}

		manager.memory.put(3, region);
		manager.memory.put(4, region.getId());

		confirmation.update();

		confirmation.setResult(ChatColor.GOLD + "土地の更地化 | ID: " + region.getId());
		confirmation.changeDisplayNames("更地化する" + ChatColor.DARK_RED + "(" + ChatColor.MAGIC + "i" + ChatColor.RESET + "" + ChatColor.DARK_RED + "元に戻せません" + ChatColor.MAGIC + "i" + ChatColor.RESET + "" + ChatColor.DARK_RED + ")", "破棄する");

		manager.setCase(Case.FLATTEN_HOGOCHI);

		manager.display(Type.CONFIRMATION);
	}

	public void combineRegions(Location loc){
		Player player = manager.getPlayer();

		Location location = loc != null ? loc : player.getLocation();

		ProtectedRegion region = RegionByebye.getProtectedRegion(location.getBlockX(), location.getBlockZ());

		if(!RegionByebye.isOwner(player, region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.COMBINE_HOGOCHIES), Message.CAN_NOT_COMBINE_OTHER_PLAYERS_HOGOCHI, Util.caseToMaterial(Case.COMBINE_HOGOCHIES), player);
			manager.close();
			return;
		}

		if(RegionByebye.isBuyable(region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.COMBINE_HOGOCHIES), Message.CAN_NOT_COMBINE_HOGOCHI_SALE, Util.caseToMaterial(Case.COMBINE_HOGOCHIES), player);
			manager.close();
			return;
		}

		if(RegionByebye.is50x50(region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.COMBINE_HOGOCHIES), Message.THIS_HOGOCHI_IS_MAX_SIZE, Util.caseToMaterial(Case.COMBINE_HOGOCHIES), player);
			manager.close();
			return;
		}

		CombineRegions combine = ((CombineRegions) manager.getGUI(Type.COMBINE_REGIONS));

		combine.update();

		manager.memory.put(3, region);
		manager.memory.put(4, region.getId());

		combine.applyProtectedRegion();

		manager.setCase(Case.COMBINE_HOGOCHIES);

		manager.display(Type.COMBINE_REGIONS);
	}

	public void splitRegion(Location loc){
		Player player = manager.getPlayer();

		Location location = loc != null ? loc : player.getLocation();

		ProtectedRegion region = RegionByebye.getProtectedRegion(location.getBlockX(), location.getBlockZ());

		if(!RegionByebye.isOwner(player, region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.SPLIT_HOGOCHI), Message.CAN_NOT_SPLIT_OTHER_PLAYERS_HOGOCHI, Util.caseToMaterial(Case.SPLIT_HOGOCHI), player);
			manager.close();
			return;
		}

		if(RegionByebye.isBuyable(region)){
			Util.warn(Message.FAILED + Message.caseToString(Case.SPLIT_HOGOCHI), Message.CAN_NOT_SPLIT_HOGOCHI_SALE, Util.caseToMaterial(Case.SPLIT_HOGOCHI), player);
			manager.close();
			return;
		}

		manager.memory.put(3, region);
		manager.memory.put(4, region.getId());

		manager.setCase(Case.SPLIT_HOGOCHI);

		manager.display(Type.SPLIT_REGION);
	}

	private Case getCase(int slotNumber){
		switch(slotNumber){
		case 2:
			return Case.BUY_HOGOCHI;
		case 3:
			return Case.SELL_HOGOCHI;
		case 4:
			return Case.WITHDRAW_HOGOCHI_SALE;
		case 5:
			return Case.FLATTEN_HOGOCHI;
		case 6:
			return Case.COMBINE_HOGOCHIES;
		case 7:
			return Case.SPLIT_HOGOCHI;
		default:
			return null;
		}
	}

}

package amata1219.hypering.economy.gui.home;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import amata1219.hogochi.byebye.ClaimByebye;
import amata1219.hogochi.byebye.Compartment;
import amata1219.hogochi.byebye.IdType;
import amata1219.hogochi.byebye.Point;
import amata1219.hogochi.byebye.Region;
import amata1219.hogochi.byebye.RegionByebye;
import amata1219.hypering.economy.Database;
import amata1219.hypering.economy.HyperingEconomyAPI;
import amata1219.hypering.economy.ServerName;
import amata1219.hypering.economy.gui.GUIListener;
import amata1219.hypering.economy.gui.HyperingEconomyGUI;
import amata1219.hypering.economy.gui.hogochi.CombineRegions;
import amata1219.hypering.economy.gui.hogochi.SplitRegion;
import amata1219.hypering.economy.gui.util.Case;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;
import amata1219.hypering.economy.spigot.Electron;
import me.ryanhamshire.GriefPrevention.Claim;

public class Confirmation implements GraphicalUserInterface {

	private GUIManager manager;
	private Inventory inventory;

	private Confirmation(){

	}

	public static Confirmation load(GUIManager manager){
		Confirmation confirmation = new Confirmation();

		confirmation.manager = manager;

		Inventory inventory = ItemHelper.createInventory(9, Type.CONFIRMATION);

		ItemStack result = ItemHelper.createItem(Material.BOOK_AND_QUILL, ChatColor.GOLD + "", ChatColor.GRAY + "・確認事項が上に表示されます。");

		ItemStack backSpace = ItemHelper.createSkull(ItemHelper.ARROW_LEFT, ChatColor.GOLD + "一つ前のページに戻る", ChatColor.GRAY + "");

		ItemStack enter = ItemHelper.createItem(Material.PAPER, ChatColor.GOLD + "エンター", ChatColor.GRAY + "");

		ItemStack cancel = ItemHelper.createItem(Material.INK_SACK, 15, ChatColor.GOLD + "キャンセル", ChatColor.GRAY + "");

		inventory.setItem(2, result);
		inventory.setItem(3, backSpace);
		inventory.setItem(5, enter);
		inventory.setItem(6, cancel);

		inventory.setItem(0, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(8, ItemHelper.createSeasonalColorWool2());

		confirmation.inventory = inventory;

		return confirmation;
	}

	@Override
	public int getId() {
		return 3;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void update(){
	}

	@Override
	public void clear(){
		setResult("");
		changeDisplayNames(ChatColor.GOLD + "リターン", ChatColor.GOLD + "エンター", ChatColor.GOLD + "キャンセル");
	}

	@Override
	public void push(int slotNumber) {
		Player player = manager.getPlayer();
		Case cs = manager.getCase();

		if(!manager.hasCase()){
			Util.warn(Message.CASE_NOT_FOUND_EXCEPTION, Util.caseToMaterial(cs), player);
			manager.close();
			return;
		}

		if(slotNumber == 6){
			manager.close();
			return;
		}

		if(slotNumber == 3){
			switch(manager.getCase()){
			case SEND_MONEY:
				manager.display(Type.CHARACTER_SCANNER);
				break;
			case BUY_TICKET:
				manager.display(Type.NUMBER_SCANNER);
				break;
			case CASH_TICKET:
				manager.display(Type.NUMBER_SCANNER);
				break;
			case BUY_HOGOCHI:
				manager.display(Type.HOGOCHI_MENU);
				break;
			case SELL_HOGOCHI:
				manager.display(Type.NUMBER_SCANNER);
				break;
			case WITHDRAW_HOGOCHI_SALE:
				manager.display(Type.HOGOCHI_MENU);
				break;
			case FLATTEN_HOGOCHI:
				manager.display(Type.NUMBER_SCANNER);
				break;
			case CONFIRMATION_FLATTEN_HOGOCHI:
				setResult(ChatColor.GOLD + "確認 | 土地の更地化 - ID: " + ((String) ((String) manager.memory.get(4))));
				changeDisplayNames("更地化する" + ChatColor.DARK_RED + "(" + ChatColor.MAGIC + "i" + ChatColor.RESET + "" + ChatColor.DARK_RED + "元に戻せません" + ChatColor.MAGIC + "i" + ChatColor.RESET + "" + ChatColor.DARK_RED + ")", ChatColor.RED + "破棄する");

				manager.setCase(Case.CONFIRMATION_FLATTEN_HOGOCHI);

				manager.display(Type.CONFIRMATION);
				break;
			case COMBINE_HOGOCHIES:
				manager.display(Type.COMBINE_REGIONS);
				break;
			case SPLIT_HOGOCHI:
				manager.display(Type.SPLIT_REGION);
				break;
			default:
				Util.warn(Message.CASE_NOT_FOUND_EXCEPTION, Util.caseToMaterial(cs), player);
				manager.close();
				break;
			}
		}

		HyperingEconomyAPI api = Database.getHyperingEconomyAPI();
		ServerName serverName = Electron.getServerName();

		UUID uuid = manager.getUniqueId();

		if(slotNumber == 5){
			switch(manager.getCase()){
			case SEND_MONEY:
				OfflinePlayer sendTo = (OfflinePlayer) manager.memory.get(1);

				long number1 = (long) manager.memory.get(0);

				api.getMoneyEditer(serverName, uuid).send(sendTo.getUniqueId(), number1);

				manager.close();

				Util.success(Message.COMPLETED + Message.caseToString(Case.SEND_MONEY), ChatColor.GRAY + "MCID: " + sendTo.getName() + "\n金額: ¥" + number1, Util.caseToMaterial(Case.SEND_MONEY), player);

				if(sendTo.isOnline())
					Util.success(Message.COMPLETED + "着金", ChatColor.GRAY + "MCID: " + manager.getPlayer().getName() + "\n金額: ¥" + number1, Util.caseToMaterial(Case.SEND_MONEY), sendTo.getPlayer());
				break;
			case BUY_TICKET:
				int number2 = Long.valueOf((long) manager.memory.get(0)).intValue();

				api.buyTickets(serverName, uuid, number2);

				manager.close();

				Util.success(Message.COMPLETED + Message.caseToString(Case.BUY_TICKET), ChatColor.GRAY + "チケット: " + number2 + "枚", Material.STORAGE_MINECART, player);
				break;
			case CASH_TICKET:
				long number3 = (long) manager.memory.get(0);

				api.cashTickets(serverName, uuid, number3);

				manager.close();

				Util.success(Message.COMPLETED + Message.caseToString(Case.CASH_TICKET), ChatColor.GRAY + "チケット: " + number3 + "枚", Material.MINECART, player);
				break;
			case BUY_HOGOCHI:
				if(manager.memory.containsKey(5)){
					long price = (long) manager.memory.get(5);

					if(!api.hasMoney(serverName, uuid, price)){
						Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.NOT_ENOUGH_POSSESSION_MONEY, Util.caseToMaterial(cs), player);
						break;
					}

					UUID target = null;

					if(manager.memory.containsKey(2)){
						Claim claim = (Claim) manager.memory.get(2);

						GUIListener.getListener().getGUIManagers().forEach(m -> {
							if(!player.getName().equals(m.getPlayer().getName())){
								m.checkClaim(claim, Message.HOGOCHI_WAS_BOUGHT);
							}
						});

						api.getMoneyEditer(serverName, uuid).remove(price);
						api.getMoneyEditer(serverName, target = claim.ownerID).add(price);

						ClaimByebye.buy(player, claim);

						Util.success(Message.COMPLETED + Message.caseToString(Case.BUY_HOGOCHI), ChatColor.GRAY + "ID: " + ((String) manager.memory.get(4)) + "\n価格: ¥" + price, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
					}else{
						ProtectedRegion region = (ProtectedRegion) manager.memory.get(3);

						GUIListener.getListener().getGUIManagers().forEach(m -> {
							if(!player.getName().equals(m.getPlayer().getName())){
								m.checkRegion(region, Message.HOGOCHI_WAS_BOUGHT);
							}
						});

						api.getMoneyEditer(serverName, uuid).remove(price);
						api.getMoneyEditer(serverName, target = region.getOwners().getUniqueIds().iterator().next()).add(price);

						RegionByebye.buy(player, region);

						Util.success(Message.COMPLETED + Message.caseToString(Case.BUY_HOGOCHI), ChatColor.GRAY + "ID: " + ((String) manager.memory.get(4)) + "\n価格: ¥" + price, Util.caseToMaterial(Case.BUY_HOGOCHI), player);
					}

					OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(target);
					if(offPlayer == null)
						return;

					if(offPlayer.isOnline())
						Util.success(Message.COMPLETED + Message.caseToString(Case.BUY_HOGOCHI), ChatColor.GRAY + "MCID: " + player.getName() + "\nID: " +  ((String) manager.memory.get(4)), Material.BOOK_AND_QUILL, offPlayer.getPlayer());

					manager.close();
				}else{
					int tickets = (int) manager.memory.get(6);

					if(!api.hasTickets(uuid, tickets)){
						Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.NOT_ENOUGH_POSSESSION_TICKET, Util.caseToMaterial(cs), player);
						return;
					}

					ProtectedRegion region = (ProtectedRegion) manager.memory.get(3);

					GUIListener.getListener().getGUIManagers().forEach(m -> {
						if(!player.getName().equals(m.getPlayer().getName())){
							m.checkRegion(region, Message.HOGOCHI_WAS_BOUGHT);
						}
					});

					ProtectedCuboidRegion newRegion = new ProtectedCuboidRegion(IdType.USER.getString() + System.currentTimeMillis(), region.getMinimumPoint(), region.getMaximumPoint());
					newRegion.getOwners().addPlayer(player.getUniqueId());

					HyperingEconomyGUI.getRegionManager().removeRegion(region.getId());
					HyperingEconomyGUI.getRegionManager().addRegion(newRegion);

					RegionByebye.buy(player, region);

					api.removeTickets(serverName, uuid, tickets);

					Util.success(Message.COMPLETED + Message.caseToString(Case.BUY_HOGOCHI), ChatColor.GRAY + "ID: " + ((String) manager.memory.get(4)) + "\nチケット: 160枚", Util.caseToMaterial(cs), player);

					manager.close();
				}
				break;
			case SELL_HOGOCHI:
				long number4 = (long) manager.memory.get(0);

				if(manager.memory.containsKey(2))
					ClaimByebye.sell((Claim) manager.memory.get(2), number4);
				else
					RegionByebye.sell((ProtectedRegion) manager.memory.get(3), number4);

				Util.success(Message.COMPLETED + Message.caseToString(Case.SELL_HOGOCHI), ChatColor.GRAY + "ID: " + ((String) manager.memory.get(4)) + "\n価格: ¥" + ((long) manager.memory.get(0)), Util.caseToMaterial(cs), player);

				manager.close();
				break;
			case WITHDRAW_HOGOCHI_SALE:
				long whs = 0;

				if(manager.memory.containsKey(2)){
					Claim claim = (Claim) manager.memory.get(2);

					whs = ClaimByebye.getPrice(claim);

					GUIListener.getListener().getGUIManagers().forEach(m -> {
						if(!player.getName().equals(m.getPlayer().getName())){
							m.checkClaim(claim, Message.OWNER_WITHDRAWED_HOGOCHI_SALE);
						}
					});

					ClaimByebye.withdrawSale(claim);
				}else{
					ProtectedRegion region = (ProtectedRegion) manager.memory.get(3);

					whs = RegionByebye.getPrice(region);

					GUIListener.getListener().getGUIManagers().forEach(m -> {
						if(!player.getName().equals(m.getPlayer().getName())){
							m.checkRegion(region, Message.OWNER_WITHDRAWED_HOGOCHI_SALE);
						}
					});

					RegionByebye.withdraw(region);
				}

				Util.success(Message.COMPLETED + Message.caseToString(Case.WITHDRAW_HOGOCHI_SALE), ChatColor.GRAY + "ID: " + ((String) manager.memory.get(4)) + "\n価格: ¥" + whs, Util.caseToMaterial(cs), player);

				manager.close();
				break;
			case FLATTEN_HOGOCHI:
				((Confirmation) manager.getGUI(Type.CONFIRMATION)).changeDisplayNames("更地化する" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "(" + ChatColor.MAGIC + "i" + ChatColor.RESET + "" + ChatColor.DARK_RED + "" + ChatColor.BOLD + "元に戻せません" + ChatColor.MAGIC + "i" + ChatColor.RESET + "" + ChatColor.DARK_RED + "" + ChatColor.BOLD + ")", "破棄する");

				manager.setCase(Case.CONFIRMATION_FLATTEN_HOGOCHI);

				manager.display(Type.CONFIRMATION);
				break;
			case CONFIRMATION_FLATTEN_HOGOCHI:
				ProtectedRegion region = (ProtectedRegion) manager.memory.get(3);

				int returnTickets = RegionByebye.getNeedTickets(region);

				api.addTickets(uuid, returnTickets);

				String oldId = region.getId();

				if(RegionByebye.is50x50(region)){
					ProtectedRegion[] results = RegionByebye.splitLargeRegion(region, true);

					ProtectedRegion[] result1 = RegionByebye.splitSmallRegion(results[0], true);
					ProtectedRegion[] result2 = RegionByebye.splitSmallRegion(results[1], true);

					RegionByebye.flatten(result1[0]);
					RegionByebye.flatten(result1[1]);
					RegionByebye.flatten(result2[0]);
					RegionByebye.flatten(result2[1]);

					/*new BukkitRunnable(){

						@Override
						public void run(){
							RegionByebye.flatten(result1[0]);
							RegionByebye.flatten(result1[1]);
							RegionByebye.flatten(result2[0]);
							RegionByebye.flatten(result2[1]);
						}

					}.runTaskLater(HyperingEconomyGUI.getPlugin(), 20);*/
				}else if(RegionByebye.is50x25(region) || RegionByebye.is25x50(region)){
					ProtectedRegion[] results = RegionByebye.splitSmallRegion(region, true);

					RegionByebye.flatten(results[0]);
					RegionByebye.flatten(results[1]);
				}else{
					RegionByebye.flatten(region);

					Compartment cpm = new Compartment(region);
					Region r = cpm.getRegion(cpm.getDirections(region).get(0));

					amata1219.hogochi.byebye.Util.createProtectedRegion(IdType.ADMIN, new Point(r.getMin().getX(), r.getMin().getZ()), new Point(r.getMax().getX(), r.getMax().getZ()));

					HyperingEconomyGUI.getRegionManager().removeRegion(oldId);
				}

				Util.success(Message.COMPLETED + Message.caseToString(Case.CONFIRMATION_FLATTEN_HOGOCHI), ChatColor.GRAY + "ID: " +  oldId + "\nチケット: " + returnTickets + "枚", Util.caseToMaterial(cs), player);

				manager.close();
				break;
			case COMBINE_HOGOCHIES:
				ProtectedRegion[] regions = ((CombineRegions) manager.getGUI(Type.COMBINE_REGIONS)).getProtectedRegions();

				ProtectedRegion combinedRegion = RegionByebye.combineRegions(regions[0], regions[1]);

				Util.success(Message.COMPLETED + Message.caseToString(Case.COMBINE_HOGOCHIES), ChatColor.GRAY + "ID: " + combinedRegion.getId(), Util.caseToMaterial(cs), player);

				manager.close();
				break;
			case SPLIT_HOGOCHI:
				ProtectedRegion[] regionArgs = null;

				ProtectedRegion region1 = (ProtectedRegion) manager.memory.get(3);

				if(RegionByebye.is50x50(region1))
					regionArgs = RegionByebye.splitLargeRegion(region1, ((SplitRegion) manager.getGUI(Type.COMBINE_REGIONS)).isAlongX());
				else
					regionArgs = RegionByebye.splitSmallRegion(region1, false);

				Util.success(Message.COMPLETED + Message.caseToString(Case.SPLIT_HOGOCHI), ChatColor.GRAY + "ID: " + regionArgs[0].getId() + "\nID: " + regionArgs[1].getId(), Util.caseToMaterial(cs), player);

				manager.close();
			default:
				break;
			}
		}

	}

	public void changeDisplayNames(String enter, String cancel){
		ItemHelper.changeDisplayName(inventory.getItem(5), ChatColor.GOLD + enter);
		ItemHelper.changeDisplayName(inventory.getItem(6), ChatColor.RED + cancel);
	}

	public void changeDisplayNames(String backSpace, String enter, String cancel){
		ItemHelper.changeDisplayName(inventory.getItem(3), backSpace);

		changeDisplayNames(enter, cancel);
	}

	public String getResult(){
		return Util.replaceAll(inventory.getItem(2).getItemMeta().getDisplayName(), "§6", "");
	}

	public void setResult(String result){
		ItemStack item = inventory.getItem(2);

		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(result);

		item.setItemMeta(meta);
	}

}

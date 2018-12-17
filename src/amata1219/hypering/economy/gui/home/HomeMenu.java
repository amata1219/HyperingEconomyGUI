package amata1219.hypering.economy.gui.home;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import amata1219.hypering.economy.Database;
import amata1219.hypering.economy.HyperingEconomyAPI;
import amata1219.hypering.economy.MoneyRanking;
import amata1219.hypering.economy.ServerName;
import amata1219.hypering.economy.gui.GUIListener;
import amata1219.hypering.economy.gui.hogochi.HogochiMenu;
import amata1219.hypering.economy.gui.util.Case;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Meta;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;
import amata1219.hypering.economy.spigot.Electron;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class HomeMenu implements GraphicalUserInterface {

	private GUIManager manager;
	private Inventory inventory;

	private HomeMenu(){

	}

	public static HomeMenu load(GUIManager manager){
		HomeMenu menu = new HomeMenu();

		menu.manager = manager;

		Inventory inventory = ItemHelper.createInventory(18, Type.HOME_MENU);

		Player player = manager.getPlayer();

		ItemStack status = ItemHelper.createSkull(player, ChatColor.GOLD + player.getName(), (String[]) null);

		ItemStack send = ItemHelper.createItem(Material.BOOK_AND_QUILL, ChatColor.GOLD + "お金を送る", ChatColor.GRAY + "・送金額を指定します。 ", ChatColor.GRAY + "・送金相手を指定します。");

		ItemStack buy = ItemHelper.createItem(Material.STORAGE_MINECART, ChatColor.GOLD + "チケットを購入する", ChatColor.GRAY + "・枚数を指定します。");

		ItemStack cash = ItemHelper.createItem(Material.MINECART, ChatColor.GOLD + "チケットを換金する", ChatColor.GRAY + "・枚数を指定します");

		ItemStack ranking = ItemHelper.createItem(Material.BLAZE_POWDER, ChatColor.GOLD + "所持金ランキング", ChatColor.GRAY + "");

		ItemStack auction = ItemHelper.createItem(Material.ANVIL, ChatColor.GOLD + "オークションメニュー", ChatColor.GRAY + "・オークションメニューに移動します。");

		ItemStack economy = ItemHelper.createItem(Electron.isEconomyEnable() ? Material.RAW_FISH : Material.COOKED_FISH, Electron.isEconomyEnable() ? ChatColor.GOLD + "ﾟ｡+━ヾ((○*>∀<*))ﾉﾞ━+｡ﾟ ＜チケットとお金が有効なのん！" : ChatColor.GOLD + "･ﾟ･(｡>ω<｡)･ﾟ･ ＜チケットは有効だけどお金は無効なのん…", ChatColor.GRAY + "");

		ItemStack vote = ItemHelper.createItem(Material.DIAMOND, ChatColor.GOLD + "(● ˃̶͈̀∀˂̶͈́)੭ु⁾⁾ ＜アジ鯖に投票するのん！", ChatColor.GRAY + "");

		ItemStack notification = ItemHelper.createItem(Material.ITEM_FRAME, ChatColor.GOLD + "お知らせ", ChatColor.GRAY + "");

		ItemStack hogochi = ItemHelper.createItem(Material.STICK, ChatColor.GOLD + "保護地メニュー", ChatColor.GRAY + "・保護地メニューに移動します。", ChatColor.GRAY + "・保護地の上にいた場合そこが操作の対象となります。", ChatColor.GRAY + "・そうでない場合は左クリックで土地を選択します。");

		inventory.setItem(1, status);
		inventory.setItem(3, send);
		inventory.setItem(4, buy);
		inventory.setItem(5, cash);
		inventory.setItem(6, ranking);
		inventory.setItem(7, auction);
		inventory.setItem(10, economy);
		inventory.setItem(12, vote);
		inventory.setItem(13, notification);
		inventory.setItem(14, hogochi);

		inventory.setItem(0, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(8, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(9, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(17, ItemHelper.createSeasonalColorWool1());

		menu.inventory = inventory;

		return menu;
	}

	@Override
	public int getId() {
		return 0;
	}

	@Override
	public Inventory getInventory() {
		return inventory;
	}

	@Override
	public void update() {
		ItemStack status = inventory.getItem(1);

		ItemHelper.clearLore(status);

		UUID uuid = manager.getUniqueId();

		HyperingEconomyAPI api = Database.getHyperingEconomyAPI();
		ServerName serverName = Electron.getServerName();

		if(Electron.isEconomyEnable()){
			MoneyRanking ranking = api.getMoneyRanking(serverName);

			ItemHelper.addLore(status, ChatColor.GRAY + "所持金ランキングの順位: " + ranking.getRank(uuid));

			ItemHelper.addLore(status, "");

			ItemHelper.addLore(status, ChatColor.GRAY + "所持金: ¥" + api.getMoney(serverName, uuid));
		}

		ItemHelper.addLore(status, ChatColor.GRAY + "チケット: " + api.getTickets(uuid) + "枚");
	}

	@Override
	public void clear() {
	}

	@Override
	public void push(int slotNumber) {
		if(!Electron.isEconomyEnable()){
			if(slotNumber == 3 || slotNumber == 4 || slotNumber == 5 || slotNumber == 6 || slotNumber == 14){
				Util.error(Message.WARN + "ホームメニュー", Message.CAN_NOT_USE_THIS_FUNCATION, Material.RAW_FISH, manager.getPlayer());
				return;
			}
		}

		switch(slotNumber){
		case 3:
			NumberScanner smNumberScanner = (NumberScanner) manager.getGUI(Type.NUMBER_SCANNER);

			smNumberScanner.setDisplayYenMark(true);

			manager.setCase(Case.SEND_MONEY);
			manager.display(Type.NUMBER_SCANNER);
			break;
		case 4:
			NumberScanner btNumberScanner = (NumberScanner) manager.getGUI(Type.NUMBER_SCANNER);

			btNumberScanner.setDisplayYenMark(false);

			manager.setCase(Case.BUY_TICKET);
			manager.display(Type.NUMBER_SCANNER);
			break;
		case 5:
			NumberScanner ctNumberScanner = (NumberScanner) manager.getGUI(Type.NUMBER_SCANNER);

			ctNumberScanner.setDisplayYenMark(false);

			manager.setCase(Case.CASH_TICKET);
			manager.display(Type.NUMBER_SCANNER);
			break;
		case 6:
			GUIListener.getListener().getPossesionMoneyRanking().update();
			manager.getPlayer().openInventory(GUIListener.getListener().getPossesionMoneyRanking().getInventory());
			break;
		case 7:
			Bukkit.dispatchCommand(manager.getPlayer(), "ca");
			break;
		case 12:
			TextComponent component = new TextComponent(ChatColor.GOLD + "URL: https://minecraft.jp/servers/azisaba.net/vote");

			component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.jp/servers/azisaba.net/vote"));

			manager.getPlayer().spigot().sendMessage(ChatMessageType.CHAT, component);

			Util.success(Message.VOTE_ON_JPMCS, Material.RAW_FISH, manager.getPlayer());

			manager.close();
			break;
		case 13:
			GUIListener.getListener().getNotification().update();
			manager.getPlayer().openInventory(GUIListener.getListener().getNotification().getInventory());
			break;
		case 14:
			HogochiMenu hogochiMenu = (HogochiMenu) manager.getGUI(Type.HOGOCHI_MENU);

			String worldName = manager.getPlayer().getWorld().getName();
			if(worldName.equals("main") || worldName.equals("world_nether") || worldName.equals("world_the_end") || worldName.equals("main_flat")){
				Meta.removeMeta(manager.getPlayer());

				hogochiMenu.update();

				manager.display(Type.HOGOCHI_MENU);
			}else{
				Util.error(Message.WARN + "ホームメニュー", Message.CAN_NOT_USE_THIS_FUNCATION, Material.RAW_FISH, manager.getPlayer());
			}
			break;
		default:
			break;
		}
	}

}

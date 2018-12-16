package amata1219.hypering.economy.gui.home;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.hypering.economy.Database;
import amata1219.hypering.economy.HyperingEconomyAPI;
import amata1219.hypering.economy.ServerName;
import amata1219.hypering.economy.gui.util.Case;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;
import amata1219.hypering.economy.spigot.Electron;

public class NumberScanner implements GraphicalUserInterface {

	private GUIManager manager;
	private Inventory inventory;

	private boolean displayPrefix;
	private String prefix;

	private NumberScanner(){

	}

	public static NumberScanner load(GUIManager manager){
		NumberScanner scanner = new NumberScanner();

		scanner.manager = manager;

		scanner.displayPrefix = true;
		scanner.prefix = "¥";

		Inventory inventory = ItemHelper.createInventory(18, Type.NUMBER_SCANNER);

		ItemStack result = ItemHelper.createItem(Material.BOOK_AND_QUILL, ChatColor.GOLD + "", ChatColor.GRAY + "ここに説明文");

		ItemStack backSpace = ItemHelper.createSkull(ItemHelper.ARROW_LEFT, ChatColor.GOLD + "バックスペース", ChatColor.GRAY + "");

		ItemStack zero = ItemHelper.createSkull(ItemHelper.ZERO, ChatColor.GOLD + "0", ChatColor.GRAY + "");

		ItemStack one = ItemHelper.createSkull(ItemHelper.ONE, ChatColor.GOLD + "1", ChatColor.GRAY + "");

		ItemStack two = ItemHelper.createSkull(ItemHelper.TWO, ChatColor.GOLD + "2", ChatColor.GRAY + "");

		ItemStack three = ItemHelper.createSkull(ItemHelper.THREE, ChatColor.GOLD + "3", ChatColor.GRAY + "");

		ItemStack four = ItemHelper.createSkull(ItemHelper.FOUR, ChatColor.GOLD + "4", ChatColor.GRAY + "");

		ItemStack enter = ItemHelper.createItem(Material.PAPER, ChatColor.GOLD + "エンター", ChatColor.GRAY + "");

		ItemStack cancel = ItemHelper.createItem(Material.INK_SACK, 15, ChatColor.GOLD + "キャンセル", ChatColor.GRAY + "");

		ItemStack five = ItemHelper.createSkull(ItemHelper.FIVE, ChatColor.GOLD + "5", ChatColor.GRAY + "");

		ItemStack six = ItemHelper.createSkull(ItemHelper.SIX, ChatColor.GOLD + "6", ChatColor.GRAY + "");

		ItemStack seven = ItemHelper.createSkull(ItemHelper.SEVEN, ChatColor.GOLD + "7", ChatColor.GRAY + "");

		ItemStack eight = ItemHelper.createSkull(ItemHelper.EIGHT, ChatColor.GOLD + "8", ChatColor.GRAY + "");

		ItemStack nine = ItemHelper.createSkull(ItemHelper.NINE, ChatColor.GOLD + "9", ChatColor.GRAY + "");

		inventory.setItem(0, result);
		inventory.setItem(1, backSpace);
		inventory.setItem(4, zero);
		inventory.setItem(5, one);
		inventory.setItem(6, two);
		inventory.setItem(7, three);
		inventory.setItem(8, four);
		inventory.setItem(9, enter);
		inventory.setItem(10, cancel);
		inventory.setItem(13, five);
		inventory.setItem(14, six);
		inventory.setItem(15, seven);
		inventory.setItem(16, eight);
		inventory.setItem(17, nine);

		scanner.inventory = inventory;

		return scanner;
	}

	@Override
	public int getId() {
		return 1;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

	@Override
	public void update(){
		clear();
	}

	@Override
	public void clear(){
		setResult(0);
	}

	public boolean isPushBackSpace(int slotNumber){
		return slotNumber == 1;
	}

	public boolean isPushEnter(int slotNumber){
		return slotNumber == 9;
	}

	public boolean isPushCancel(int slotNumber){
		return slotNumber == 10;
	}


	@Override
	public void push(int slotNumber){
		Player player = manager.getPlayer();
		Case cs = manager.getCase();

		if(!manager.hasCase()){
			Util.error(Message.CASE_NOT_FOUND_EXCEPTION, Util.caseToMaterial(cs), player);
			manager.close();
			return;
		}

		if(slotNumber == 10){
			manager.close();
			return;
		}

		if(slotNumber == 1){
			if(!backSpace())
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_MIN_VALUE, Util.caseToMaterial(cs), player);

			return;
		}

		if(slotNumber != 9){
			if(!append(getNumber(slotNumber)))
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_MAX_VALUE, Util.caseToMaterial(cs), player);

			return;
		}

		HyperingEconomyAPI api = Database.getHyperingEconomyAPI();
		ServerName serverName = Electron.getServerName();

		UUID uuid = manager.getUniqueId();

		long result = getResult();

		switch(cs){
		case SEND_MONEY:
			if(result == 0){
				Util.warn(Message.WARN + Message.caseToString(cs), Message.NOT_ALLOWED_ZERO, Util.caseToMaterial(cs), player);
				break;
			}

			if(!api.hasMoney(serverName, uuid, result)){
				Util.warn(Message.WARN + Message.caseToString(cs), Message.NOT_ENOUGH_POSSESSION_MONEY, Util.caseToMaterial(cs), player);
				return;
			}

			manager.memory.put(0, result);
			manager.memory.put(0, result);

			manager.display(Type.CHARACTER_SCANNER);
			break;
		case BUY_TICKET:
			if(result == 0){
				Util.warn(Message.WARN + Message.caseToString(cs), Message.NOT_ALLOWED_ZERO, Util.caseToMaterial(cs), player);
				break;
			}

			if(!api.hasMoney(serverName, uuid, result * api.getTicketPrice(serverName))){
				Util.warn(Message.WARN + Message.caseToString(cs), Message.NOT_ENOUGH_POSSESSION_MONEY, Util.caseToMaterial(cs), player);
				return;
			}

			manager.memory.put(0, result);

			Confirmation btConfirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

			btConfirmation.update();

			btConfirmation.setResult(ChatColor.GOLD + "確認 | チケットの購入 - チケット: " + result + "枚");
			btConfirmation.changeDisplayNames("購入する", "破棄する");

			manager.display(Type.CONFIRMATION);
			break;
		case CASH_TICKET:
			if(result == 0){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.NOT_ALLOWED_ZERO, Util.caseToMaterial(cs), player);
				break;
			}

			if(!api.hasTickets(uuid, result)){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.NOT_ENOUGH_POSSESSION_TICKET, Util.caseToMaterial(cs), player);
				return;
			}

			manager.memory.put(0, result);

			Confirmation stConfirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

			stConfirmation.update();

			stConfirmation.setResult(ChatColor.GOLD + "確認 | チケットの換金 - チケット: " + result + "枚");
			stConfirmation.changeDisplayNames("換金する", "破棄する");

			manager.display(Type.CONFIRMATION);
			break;
		case SELL_HOGOCHI:
			manager.memory.put(0, result);

			Confirmation shConfirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

			shConfirmation.update();

			shConfirmation.setResult(ChatColor.GOLD + "確認 | 土地の販売 - ID: " + ((String) manager.memory.get(4)) + " 価格: ¥" + ((Long) manager.memory.get(0)));
			shConfirmation.changeDisplayNames("販売する", "破棄する");

			manager.display(Type.CONFIRMATION);
			break;
		default:
			Util.warn(Message.CASE_NOT_FOUND_EXCEPTION, Util.caseToMaterial(cs), player);
			manager.close();
			break;
		}
	}

	public boolean isDisplayPrefix(){
		return displayPrefix;
	}

	public void setDisplayYenMark(boolean displayPrefix){
		this.displayPrefix = displayPrefix;

		ItemStack result = inventory.getItem(0);

		ItemHelper.changeDisplayName(result, displayPrefix ? ChatColor.GOLD + prefix : ChatColor.GOLD + "");
	}

	public String getPrefix(){
		return prefix;
	}

	public void setPrefix(String prefix){
		this.prefix = prefix;
	}

	public long getResult(){
		String result = Util.replaceAll(inventory.getItem(0).getItemMeta().getDisplayName(), "§6", "");

		if(displayPrefix)
			result = result.substring(prefix.length());

		return result.isEmpty() ? 0 : Long.valueOf(result);
	}

	public void setResult(long result){
		ItemStack item = inventory.getItem(0);

		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.GOLD + (isDisplayPrefix() ? prefix : "") + result);
		item.setItemMeta(meta);
	}

	public void setResult(String result){
		try{
			setResult(Long.valueOf(result));
		}catch(NumberFormatException e){
			e.printStackTrace();
		}
	}

	public boolean append(int number){
		if(number == -1)
			return true;

		long result = getResult();
		if(result == 0 && number == 0)
			return true;

		String append = String.valueOf(result) + number;

		try{
			Long.valueOf(append);
		}catch(Exception e){
			if(result == Long.MAX_VALUE)
				return false;

			setResult(Long.MAX_VALUE);
			return true;
		}

		setResult(append);
		return true;
	}

	public boolean backSpace(){
		long result = getResult();
		if(result == 0)
			return false;

		String strResult = String.valueOf(result);
		String back = strResult.substring(0, strResult.length() - 1);

		if(back.isEmpty())
			setResult(0);
		else
			setResult(back);

		return true;
	}

	public int getNumber(int slotNumber){
		switch(slotNumber){
		case 4:
			return 0;
		case 5:
			return 1;
		case 6:
			return 2;
		case 7:
			return 3;
		case 8:
			return 4;
		case 13:
			return 5;
		case 14:
			return 6;
		case 15:
			return 7;
		case 16:
			return 8;
		case 17:
			return 9;
		default:
			return -1;
		}
	}

}

package amata1219.hypering.economy.gui.home;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import amata1219.hypering.economy.gui.GUIListener;
import amata1219.hypering.economy.gui.util.Case;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;

public class CharacterScanner implements GraphicalUserInterface {

	private GUIManager manager;
	private Inventory inventory;

	private boolean stringInputMode;
	private int maxLength;

	private boolean displayPlayerSkullHead;

	private CharacterScanner(){

	}

	public static CharacterScanner load(GUIManager manager){
		CharacterScanner scanner = new CharacterScanner();

		scanner.manager = manager;

		scanner.maxLength = 16;

		Inventory inventory = ItemHelper.createInventory(45, Type.CHARACTER_SCANNER);

		ItemStack result = ItemHelper.createItem(Material.WRITABLE_BOOK, ChatColor.GOLD + "", ChatColor.GRAY + "ここに説明文");

		ItemStack a = ItemHelper.createSkull(ItemHelper.A, ChatColor.GOLD + "A", ChatColor.GRAY + "");

		ItemStack b = ItemHelper.createSkull(ItemHelper.B ,ChatColor.GOLD + "B", ChatColor.GRAY + "");

		ItemStack c = ItemHelper.createSkull(ItemHelper.C ,ChatColor.GOLD + "C", ChatColor.GRAY + "");

		ItemStack d = ItemHelper.createSkull(ItemHelper.D ,ChatColor.GOLD + "D", ChatColor.GRAY + "");

		ItemStack e = ItemHelper.createSkull(ItemHelper.E ,ChatColor.GOLD + "E", ChatColor.GRAY + "");

		ItemStack f = ItemHelper.createSkull(ItemHelper.F ,ChatColor.GOLD + "F", ChatColor.GRAY + "");

		ItemStack g = ItemHelper.createSkull(ItemHelper.G ,ChatColor.GOLD + "G", ChatColor.GRAY + "");

		ItemStack h = ItemHelper.createSkull(ItemHelper.H ,ChatColor.GOLD + "H", ChatColor.GRAY + "");

		ItemStack modeChanger = ItemHelper.createItem(Material.SALMON, ChatColor.GOLD + "ﾟ｡+━ヾ((○*>∀<*))ﾉﾞ━+｡ﾟ ＜文字列入力機能が有効なのん！", ChatColor.GRAY + "");

		if(!scanner.stringInputMode){
			modeChanger.setType(Material.COOKED_SALMON);
			ItemHelper.changeDisplayName(modeChanger, ChatColor.GOLD + "･ﾟ･(｡>ω<｡)･ﾟ･ ＜文字列入力機能が無効なのん");
		}

		ItemStack i = ItemHelper.createSkull(ItemHelper.I ,ChatColor.GOLD + "I", ChatColor.GRAY + "");

		ItemStack j = ItemHelper.createSkull(ItemHelper.J ,ChatColor.GOLD + "J", ChatColor.GRAY + "");

		ItemStack k = ItemHelper.createSkull(ItemHelper.K ,ChatColor.GOLD + "K", ChatColor.GRAY + "");

		ItemStack l = ItemHelper.createSkull(ItemHelper.L ,ChatColor.GOLD + "L", ChatColor.GRAY + "");

		ItemStack m = ItemHelper.createSkull(ItemHelper.M ,ChatColor.GOLD + "M", ChatColor.GRAY + "");

		ItemStack n = ItemHelper.createSkull(ItemHelper.N ,ChatColor.GOLD + "N", ChatColor.GRAY + "");

		ItemStack o = ItemHelper.createSkull(ItemHelper.O ,ChatColor.GOLD + "O", ChatColor.GRAY + "");

		ItemStack p = ItemHelper.createSkull(ItemHelper.P ,ChatColor.GOLD + "P", ChatColor.GRAY + "");

		ItemStack q = ItemHelper.createSkull(ItemHelper.Q ,ChatColor.GOLD + "Q", ChatColor.GRAY + "");

		ItemStack r = ItemHelper.createSkull(ItemHelper.R ,ChatColor.GOLD + "R", ChatColor.GRAY + "");

		ItemStack s = ItemHelper.createSkull(ItemHelper.S ,ChatColor.GOLD + "S", ChatColor.GRAY + "");

		ItemStack t = ItemHelper.createSkull(ItemHelper.T ,ChatColor.GOLD + "T", ChatColor.GRAY + "");

		ItemStack u = ItemHelper.createSkull(ItemHelper.U ,ChatColor.GOLD + "U", ChatColor.GRAY + "");

		ItemStack v = ItemHelper.createSkull(ItemHelper.V ,ChatColor.GOLD + "V", ChatColor.GRAY + "");

		ItemStack w = ItemHelper.createSkull(ItemHelper.W ,ChatColor.GOLD + "W", ChatColor.GRAY + "");

		ItemStack x = ItemHelper.createSkull(ItemHelper.X ,ChatColor.GOLD + "X", ChatColor.GRAY + "");

		ItemStack enter = ItemHelper.createItem(Material.PAPER, ChatColor.GOLD + "エンター", ChatColor.GRAY + "");

		ItemStack y = ItemHelper.createSkull(ItemHelper.Y ,ChatColor.GOLD + "Y", ChatColor.GRAY + "");

		ItemStack z = ItemHelper.createSkull(ItemHelper.Z ,ChatColor.GOLD + "Z", ChatColor.GRAY + "");

		ItemStack zero = ItemHelper.createSkull(ItemHelper.ZERO ,ChatColor.GOLD + "0", ChatColor.GRAY + "");

		ItemStack one = ItemHelper.createSkull(ItemHelper.ONE ,ChatColor.GOLD + "1", ChatColor.GRAY + "");

		ItemStack two = ItemHelper.createSkull(ItemHelper.TWO ,ChatColor.GOLD + "2", ChatColor.GRAY + "");

		ItemStack three = ItemHelper.createSkull(ItemHelper.THREE ,ChatColor.GOLD + "3", ChatColor.GRAY + "");

		ItemStack four = ItemHelper.createSkull(ItemHelper.FOUR ,ChatColor.GOLD + "4", ChatColor.GRAY + "");

		ItemStack cancel = ItemHelper.createItem(Material.BONE_MEAL, ChatColor.GOLD + "キャンセル", ChatColor.GRAY + "");

		ItemStack five = ItemHelper.createSkull(ItemHelper.FIVE ,ChatColor.GOLD + "5", ChatColor.GRAY + "");

		ItemStack six = ItemHelper.createSkull(ItemHelper.SIX ,ChatColor.GOLD + "6", ChatColor.GRAY + "");

		ItemStack seven = ItemHelper.createSkull(ItemHelper.SEVEN ,ChatColor.GOLD + "7", ChatColor.GRAY + "");

		ItemStack eight = ItemHelper.createSkull(ItemHelper.EIGHT ,ChatColor.GOLD + "8", ChatColor.GRAY + "");

		ItemStack nine = ItemHelper.createSkull(ItemHelper.NINE ,ChatColor.GOLD + "9", ChatColor.GRAY + "");

		ItemStack underBar = ItemHelper.createSkull(ItemHelper.UNDER_BAR ,ChatColor.GOLD + "_", ChatColor.GRAY + "");

		ItemStack backSpace = ItemHelper.createSkull(ItemHelper.ARROW_LEFT, ChatColor.GOLD + "バックスペース", ChatColor.GRAY + "");

		inventory.setItem(0, result);
		inventory.setItem(1, a);
		inventory.setItem(2, b);
		inventory.setItem(3, c);
		inventory.setItem(4, d);
		inventory.setItem(5, e);
		inventory.setItem(6, f);
		inventory.setItem(7, g);
		inventory.setItem(8, h);
		inventory.setItem(9, modeChanger);
		inventory.setItem(10, i);
		inventory.setItem(11, j);
		inventory.setItem(12, k);
		inventory.setItem(13, l);
		inventory.setItem(14, m);
		inventory.setItem(15, n);
		inventory.setItem(16, o);
		inventory.setItem(17, p);
		inventory.setItem(19, q);
		inventory.setItem(20, r);
		inventory.setItem(21, s);
		inventory.setItem(22, t);
		inventory.setItem(23, u);
		inventory.setItem(24, v);
		inventory.setItem(25, w);
		inventory.setItem(26, x);
		inventory.setItem(27, enter);
		inventory.setItem(28, y);
		inventory.setItem(29, z);
		inventory.setItem(30, zero);
		inventory.setItem(31, one);
		inventory.setItem(32, two);
		inventory.setItem(33, three);
		inventory.setItem(34, four);
		inventory.setItem(35, five);
		inventory.setItem(36, cancel);
		inventory.setItem(37, six);
		inventory.setItem(38, seven);
		inventory.setItem(39, eight);
		inventory.setItem(40, nine);
		inventory.setItem(41, underBar);
		inventory.setItem(44, backSpace);

		scanner.inventory = inventory;

		return scanner;
	}

	@Override
	public int getId() {
		return 2;
	}


	@Override
	public Inventory getInventory(){
		return inventory;
	}

	@Override
	public void update(){
		clear();
	}

	@Override
	public void clear(){
		setResult("");
	}

	public boolean isPushBackSpace(int slotNumber){
		return slotNumber == 44;
	}

	public boolean isPushEnter(int slotNumber){
		return slotNumber == 27;
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

		if(slotNumber == 36){
			manager.close();
			return;
		}

		if(slotNumber == 9){
			if(stringInputMode){
				setStringInputMode(false);
				setDisplayPlayerSkullHead(false);
			}else{
				setStringInputMode(true);

				switch(manager.getCase()){
				case SEND_MONEY:
					setDisplayPlayerSkullHead(true);
					updateSkullOwner();
					break;
				default:
					break;
				}
			}
		}

		if(isPushBackSpace(slotNumber)){
			if(!backSpace())
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_MIN_LENGTH, Util.caseToMaterial(cs), player);
			return;
		}

		if(!isPushEnter(slotNumber)){
			char character = getCharacter(slotNumber);

			if(character == '@')
				return;

			if(stringInputMode){
				if(!append(character)){
					Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_MAX_LENGTH, Util.caseToMaterial(cs), player);
					return;
				}

				if(isDisplayPlayerSkullHead())
					updateSkullOwner();
			}else{
				player.openInventory(GUIListener.getListener().getPlayerList().getInventory(character));
			}

			return;
		}

		switch(manager.getCase()){
		case SEND_MONEY:
			String sendTo = getResult();

			OfflinePlayer searchedResult = Util.isPlayerExist(sendTo);

			if(searchedResult == null){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.TRANSMISSION_DESTINATION_IS_NOT_EXIST, Util.caseToMaterial(cs), player);
				break;
			}

			manager.memory.put(1, searchedResult);

			Confirmation confirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

			confirmation.update();

			confirmation.setResult(ChatColor.GOLD + "確認 | 送金 - MCID: " + searchedResult.getName() + " 価格: ¥" + ((long) manager.memory.get(0)));
			confirmation.changeDisplayNames("送金する", "破棄する");

			manager.display(Type.CONFIRMATION);
			break;
		default:
			break;
		}
	}

	public boolean isStringInputMode(){
		return stringInputMode;
	}

	public void setStringInputMode(boolean stringInputMode){
		if(this.stringInputMode == stringInputMode)
			return;

		this.stringInputMode = stringInputMode;

		ItemHelper.changeDisplayName(inventory.getItem(9), stringInputMode ? ChatColor.GOLD + "ﾟ｡+━ヾ((○*>∀<*))ﾉﾞ━+｡ﾟ ＜文字列入力機能が有効なのん！" : ChatColor.GOLD + "･ﾟ･(｡>ω<｡)･ﾟ･ ＜文字列入力機能が無効なのん");
	}

	public boolean isDisplayPlayerSkullHead(){
		return displayPlayerSkullHead;
	}

	public void setDisplayPlayerSkullHead(boolean displayPlayerSkullHead){
		if(this.displayPlayerSkullHead == displayPlayerSkullHead)
			return;

		this.displayPlayerSkullHead = displayPlayerSkullHead;

		inventory.setItem(18, displayPlayerSkullHead ? ItemHelper.createSkull() : ItemHelper.createItem(Material.AIR));
	}

	public void updateSkullOwner(){
		if(!displayPlayerSkullHead)
			return;

		ItemHelper.changeSkullOwner(inventory.getItem(18), getResult());
	}

	public String getResult(){
		return Util.replaceAll(inventory.getItem(0).getItemMeta().getDisplayName(), "§6", "");
	}

	public void setResult(String result){
		ItemStack item = inventory.getItem(0);

		ItemMeta meta = item.getItemMeta();

		meta.setDisplayName(ChatColor.GOLD + result);
		item.setItemMeta(meta);
	}

	public boolean append(char character){
		if(character == '@')
			return true;

		String result = getResult();
		if(result.length() > maxLength)
			return false;

		setResult(result + character);
		return true;
	}

	public boolean backSpace(){
		if(!isStringInputMode())
			return true;

		String result = getResult();
		if(result.isEmpty())
			return false;

		setResult(result.substring(0, result.length() - 1));
		return true;
	}

	public char getCharacter(int slotNumber){
		switch(slotNumber){
		case 1:
			return 'A';
		case 2:
			return 'B';
		case 3:
			return 'C';
		case 4:
			return 'D';
		case 5:
			return 'E';
		case 6:
			return 'F';
		case 7:
			return 'G';
		case 8:
			return 'H';
		case 10:
			return 'I';
		case 11:
			return 'J';
		case 12:
			return 'K';
		case 13:
			return 'L';
		case 14:
			return 'M';
		case 15:
			return 'N';
		case 16:
			return 'O';
		case 17:
			return 'P';
		case 19:
			return 'Q';
		case 20:
			return 'R';
		case 21:
			return 'S';
		case 22:
			return 'T';
		case 23:
			return 'U';
		case 24:
			return 'V';
		case 25:
			return 'W';
		case 26:
			return 'X';
		case 28:
			return 'Y';
		case 29:
			return 'Z';
		case 30:
			return '0';
		case 31:
			return '1';
		case 32:
			return '2';
		case 33:
			return '3';
		case 34:
			return '4';
		case 35:
			return '5';
		case 37:
			return '6';
		case 38:
			return '7';
		case 39:
			return '8';
		case 40:
			return '9';
		case 41:
			return '_';
		default:
			return '@';
		}
	}

}

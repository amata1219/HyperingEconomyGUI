package amata1219.hypering.economy.gui.home;

import java.util.Arrays;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import amata1219.hypering.economy.gui.GUIListener;
import amata1219.hypering.economy.gui.util.Case;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;

public class PlayerList implements GraphicalUserInterface {

	private HashMap<Character, Inventory> guis = new HashMap<>();

	private PlayerList(){

	}

	public static PlayerList load(){
		PlayerList list = new PlayerList();

		Arrays.asList('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S',
				'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '_')
		.forEach(character -> {
			Inventory inventory = ItemHelper.createInventory(54, "PLAYER_LIST_" + character);

			ItemStack returnButton = ItemHelper.createSkull(ItemHelper.ARROW_LEFT, ChatColor.GOLD + "戻る", ChatColor.GRAY + "");

			inventory.setItem(53, returnButton);

			list.guis.put(character, inventory);
		});

		return list;
	}

	@Override
	public int getId() {
		return 4;
	}

	@Override
	public Inventory getInventory() {
		return null;
	}

	public Inventory getInventory(char character){
		return guis.get(character);
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

	public void push(Player player, char character, int slotNumber){
		GUIManager manager = GUIListener.getListener().getGUIManager(player);
		Case cs = manager.getCase();

		if(!guis.containsKey(character)){
			Util.warn(Message.CASE_NOT_FOUND_EXCEPTION, Util.caseToMaterial(cs), player);
			return;
		}

		Inventory gui = guis.get(character);
		if(gui == null)
			return;

		if(!manager.hasCase()){
			Util.warn(Message.CASE_NOT_FOUND_EXCEPTION, Util.caseToMaterial(cs), player);
			return;
		}

		if(slotNumber == 53){
			switch(cs){
			case SEND_MONEY:
				manager.display(Type.CHARACTER_SCANNER);
			default:
				Util.warn(Message.CASE_NOT_FOUND_EXCEPTION, Util.caseToMaterial(cs), player);
			}
			return;
		}

		String name = getName(character, slotNumber);
		if(name == null)
			return;

		switch(manager.getCase()){
		case SEND_MONEY:
			manager.memory.put(1, Bukkit.getPlayer(name));

			Confirmation plConfirmation = (Confirmation) manager.getGUI(Type.CONFIRMATION);

			plConfirmation.update();

			plConfirmation.setResult(ChatColor.GOLD + "確認 | 送金 - MCID: " + name + " 金額: ¥" + ((NumberScanner) manager.getGUI(Type.NUMBER_SCANNER)).getResult());

			plConfirmation.changeDisplayNames("送金する", "破棄する");

			manager.display(Type.CONFIRMATION);
			break;
		default:
			break;
		}
	}

	public void addPlayer(Player player){
		if(!player.isOnline())
			return;

		String name = player.getName();
		char character = name.toUpperCase().charAt(0);

		Inventory inventory = guis.get(character);

		if(inventory.firstEmpty() == -1)
			return;

		ItemStack skullHead = ItemHelper.createSkull(player, ChatColor.GOLD + name, ChatColor.GRAY + "");

		inventory.addItem(skullHead);
	}

	public void removePlayer(Player player){
		String name = player.getName();
		char character = name.toUpperCase().charAt(0);

		Inventory inventory = guis.get(character);

		for(ItemStack item : inventory.getContents()){
			if(item == null || item.getType() == Material.AIR)
				continue;

			if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
				continue;

			if(item.getItemMeta().getDisplayName().equals(ChatColor.GOLD + name)){
				inventory.remove(item);
				break;
			}
		}
	}

	public String getName(char character, int slotNumber){
		if(!guis.containsKey(character))
			return null;

		Inventory inventory = guis.get(character);

		ItemStack item = inventory.getItem(slotNumber);
		if(item == null || item.getType() == Material.AIR)
			return null;

		if(!item.hasItemMeta() || !item.getItemMeta().hasDisplayName())
			return null;

		return Util.replaceAll(item.getItemMeta().getDisplayName(), "§6", "");
	}

}

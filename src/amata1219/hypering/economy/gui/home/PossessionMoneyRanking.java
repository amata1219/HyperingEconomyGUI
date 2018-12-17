package amata1219.hypering.economy.gui.home;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import amata1219.hypering.economy.Database;
import amata1219.hypering.economy.HyperingEconomyAPI;
import amata1219.hypering.economy.MoneyRanking;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.spigot.Electron;

public class PossessionMoneyRanking implements GraphicalUserInterface {

	private Inventory inventory;

	private PossessionMoneyRanking(){

	}

	public static PossessionMoneyRanking load(){
		PossessionMoneyRanking ranking = new PossessionMoneyRanking();

		Inventory inventory = ItemHelper.createInventory(18, Type.POSSESSION_MONEY_RANKING);

		ItemStack info = ItemHelper.createItem(Material.RAW_FISH, ChatColor.GOLD + "", ChatColor.GRAY + "");

		for(int i = 3; i <= 7; i++)
			inventory.setItem(i, ItemHelper.createVanillaSkull());


		for(int i = 12; i <= 16; i++)
			inventory.setItem(i, ItemHelper.createVanillaSkull());

		inventory.setItem(1, info);

		inventory.setItem(0, ItemHelper.createSeasonalColorWool1());
		inventory.setItem(8, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(9, ItemHelper.createSeasonalColorWool2());
		inventory.setItem(17, ItemHelper.createSeasonalColorWool1());

		ranking.inventory = inventory;

		return ranking;
	}

	public Inventory getInventory(){
		return inventory;
	}

	@Override
	public int getId() {
		return 5;
	}

	public void update(){
		HyperingEconomyAPI api = Database.getHyperingEconomyAPI();
		MoneyRanking ranking = api.getMoneyRanking(Electron.getServerName());

		long size = api.existSize();

		ItemHelper.changeDisplayName(inventory.getItem(1), ChatColor.GOLD + "全体のプレイヤー数: " + size + "人");

		int count = 1;

		for(int i = 3; i <= 7; i++){
			if(count >= size)
				break;

			OfflinePlayer player = Bukkit.getOfflinePlayer(ranking.matchedUniqueId(count));

			ItemStack item = inventory.getItem(i);

			ItemHelper.changeDisplayName(item, ChatColor.GOLD + String.valueOf(count) + "位 " + player.getName());
			ItemHelper.changeLore(item, ChatColor.GRAY + "¥" + ranking.getMoney(count));
			ItemHelper.changeSkullOwner(item, player);

			count++;
		}

		for(int i = 12; i <= 16; i++){
			if(count >= size)
				break;

			OfflinePlayer player = Bukkit.getOfflinePlayer(ranking.matchedUniqueId(count));

			ItemStack item = inventory.getItem(i);

			ItemHelper.changeDisplayName(item, ChatColor.GOLD + String.valueOf(count) + "位 " + player.getName());
			ItemHelper.changeLore(item, ChatColor.GRAY + "¥" + ranking.getMoney(count));
			ItemHelper.changeSkullOwner(item, player);

			count++;
		}
	}

	@Override
	public void clear() {

	}

	@Override
	public void push(int slotNumber) {

	}

}

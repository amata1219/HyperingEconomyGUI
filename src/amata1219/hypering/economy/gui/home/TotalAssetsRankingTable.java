package amata1219.hypering.economy.gui.home;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import amata1219.hypering.economy.HyperingEconomyAPI;
import amata1219.hypering.economy.SQL;
import amata1219.hypering.economy.gui.HyperingEconomyGUI;
import amata1219.hypering.economy.gui.util.ItemHelper;
import amata1219.hypering.economy.gui.util.TotalAssetsRanking;
import amata1219.hypering.economy.gui.util.Type;

public class TotalAssetsRankingTable implements GraphicalUserInterface {

	private Inventory inventory;

	private TotalAssetsRankingTable(){

	}

	public static TotalAssetsRankingTable load(){
		TotalAssetsRankingTable ranking = new TotalAssetsRankingTable();

		Inventory inventory = ItemHelper.createInventory(54, Type.TOTAL_ASSETS_RANKING_TABLE);

		for(int i = 0; i <= 52; i++)
			inventory.setItem(i, ItemHelper.createVanillaSkull());

		ItemStack info = ItemHelper.createItem(Material.RAW_FISH, ChatColor.GOLD + "", ChatColor.GRAY + "・スコアは総資産を自動評価したものです。");

		inventory.setItem(53, info);

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

	@Override
	public void update(){
	}

	@Override
	public void clear() {

	}

	@Override
	public void push(int slotNumber) {

	}

	public void apply(){
		HyperingEconomyAPI api = SQL.getSQL().getHyperingEconomyAPI();
		TotalAssetsRanking ranking = HyperingEconomyGUI.getTotalAssetsRanking();

		long size = api.existSize();

		int count = 1;

		while(count <= 53){
			if(count > size)
				break;

			OfflinePlayer player = Bukkit.getOfflinePlayer(ranking.matchedUniqueId(count));

			ItemStack item = inventory.getItem(count - 1);

			ItemHelper.changeDisplayName(item, ChatColor.GOLD + String.valueOf(count) + "位 " + player.getName());
			ItemHelper.changeLore(item, ChatColor.GRAY + "SCORE: " + ranking.getTotalAssets(count));
			ItemHelper.changeSkullOwner(item, player);

			count++;
		}

		ItemHelper.changeDisplayName(inventory.getItem(53), ChatColor.GOLD + "総プレイヤー数: " + size + "人");
	}

}

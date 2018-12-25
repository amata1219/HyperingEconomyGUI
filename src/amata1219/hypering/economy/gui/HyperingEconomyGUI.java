/*
 * 本プラグインの著作権は、amata1219(Twitter@amata1219)に帰属します。
 * また、本プラグインの二次配布、改変使用、自作発言を禁じます。
 */

package amata1219.hypering.economy.gui;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import amata1219.hypering.economy.gui.util.TotalAssetsRanking;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;
import amata1219.hypering.economy.spigot.Electron;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public class HyperingEconomyGUI extends JavaPlugin implements CommandExecutor {

	private static HyperingEconomyGUI plugin;

	private static GriefPrevention griefPrevention;
	private static WorldEditPlugin worldEdit;
	private static WorldGuardPlugin worldGuard;

	private BukkitTask rankingUpdater;
	private TotalAssetsRanking ranking;

	@Override
	public void onEnable(){
		plugin = this;

		saveDefaultConfig();

		griefPrevention = (GriefPrevention) getPlugin("GriefPrevention");
		worldEdit = (WorldEditPlugin) getPlugin("WorldEdit");
		worldGuard = (WorldGuardPlugin) getPlugin("WorldGuard");

		GUIListener.load();

		rankingUpdater = new BukkitRunnable(){

			@Override
			public void run(){
				ranking = TotalAssetsRanking.load(Electron.getServerName());

				GUIListener.getListener().getTotalAssetsRanking().apply();

				Util.broadcast(Sound.ENTITY_PLAYER_LEVELUP);
				Util.broadcast(ChatColor.GOLD + "総資産ランキングが更新されました！");

				for(int i = 1; i <= 5; i++){
					if(!ranking.has(i))
						break;

					int delay = i * 8;
					Util.broadcast(Sound.ENTITY_CHICKEN_EGG, delay);
					Util.broadcast(ChatColor.GOLD + String.valueOf(i) + "位: " + Util.getName(ranking.matchedUniqueId(i)) + "\n" + ChatColor.GRAY + "¥" + ranking.getTotalAssets(i), delay);
				}
			}

		}.runTaskTimer(this, 0, 6000);

		getServer().getPluginManager().registerEvents(GUIListener.getListener(), this);

		getServer().getOnlinePlayers().forEach(player -> GUIListener.getListener().loadPlayerData(player));

		getCommand("g").setExecutor(this);
		getCommand("hegreload").setExecutor(this);
	}

	@Override
	public void onDisable(){
		rankingUpdater.cancel();

		GUIListener.getListener().unload();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
		if(label.equalsIgnoreCase("g")){
			if(!(sender instanceof Player)){
				sender.sendMessage(ChatColor.RED + "ゲーム内から実行して下さい。");
				return true;
			}

			GUIListener.getListener().getGUIManager((Player) sender).display(Type.HOME_MENU);
		}else if(label.equalsIgnoreCase("greload")){
			reloadConfig();
			sender.sendMessage(ChatColor.AQUA + "コンフィグを再読み込みしました。");
		}
		return true;
	}

	private Plugin getPlugin(String pluginName){
		Plugin result = getServer().getPluginManager().getPlugin(pluginName);

		if(result == null)
			throw new NullPointerException("[HyperingEconomyGUI]: " + pluginName + "is not found.");

		return result;
	}

	public static HyperingEconomyGUI getPlugin(){
		return plugin;
	}

	public static GriefPrevention getGriefPrevention(){
		return griefPrevention;
	}

	public static TotalAssetsRanking getTotalAssetsRanking(){
		return plugin.ranking;
	}

	public static boolean isMax(Player player, boolean isMain){
		if(!isMain)
			return getMainFlatRegionCount(player) >= 12;

		String name = player.getWorld().getName();
		return name.equals("main") ? getMainClaimCount(player) >= 3 : getOtherClaimCount(player, name.equals("main_nether")) >= 1;
	}

	public static boolean canBuy(Player player, Claim claim){
		return griefPrevention.dataStore.getPlayerData(player.getUniqueId()).getRemainingClaimBlocks() >= claim.getArea();
	}

	public static int getMainClaimCount(Player player){
		int count = 0;
		for(Claim claim : griefPrevention.dataStore.getPlayerData(player.getUniqueId()).getClaims()){
			if(claim.getGreaterBoundaryCorner().getWorld().getName().equals("main"))
				count++;
		}

		return count;
	}

	public static int getBonusBlocks(UUID uuid){
		return griefPrevention.dataStore.getPlayerData(uuid).getBonusClaimBlocks();
	}

	public static int getOtherClaimCount(Player player, boolean isNether){
		int count = 0;
		for(Claim claim : griefPrevention.dataStore.getPlayerData(player.getUniqueId()).getClaims()){
			String name = claim.getGreaterBoundaryCorner().getWorld().getName();

			if((isNether && name.equals("main_nether")) || (!isNether && name.equals("main_end")))
				count++;
		}

		return count;
	}

	public static int getMainFlatRegionCount(Player player){
		return getMainFlatRegionCount(player.getUniqueId());
	}

	public static int getMainFlatRegionCount(UUID uuid){
		int count = 0;

		for(ProtectedRegion region : getRegionManager().getRegions().values()){
			if(!region.getOwners().contains(uuid))
				continue;

			count += Util.getCost(region);
		}

		return count;
	}

	public static WorldEditPlugin getWorldEdit(){
		return worldEdit;
	}

	public static WorldGuardPlugin getWorldGuard(){
		return worldGuard;
	}

	public static RegionManager getRegionManager(){
		return worldGuard.getRegionManager(Bukkit.getWorld("main_flat"));
	}

}

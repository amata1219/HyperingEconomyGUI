package amata1219.hypering.economy.gui.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import amata1219.hogochi.byebye.RegionByebye;
import amata1219.hypering.economy.gui.GUIListener;
import amata1219.hypering.economy.gui.HyperingEconomyGUI;
import amata1219.hypering.economy.gui.home.GUIManager;
import amata1219.hypering.economy.gui.util.AdvancementAPI.FrameType;

public class Util {

	public static Player getPlayer(UUID uuid){
		return Bukkit.getPlayer(uuid);
	}

	public static boolean isOnline(UUID uuid){
		return Bukkit.getPlayer(uuid) != null;
	}

	public static String getName(UUID uuid){
		return Bukkit.getOfflinePlayer(uuid).getName();
	}

	public static Player getRandomPlayer(){
		return Bukkit.getOnlinePlayers().isEmpty() ? null : Bukkit.getOnlinePlayers().iterator().next();
	}

	public static OfflinePlayer isPlayerExist(String playerName){
		for(OfflinePlayer player : Bukkit.getOfflinePlayers()){
			if(playerName.equals(player.getName().toUpperCase()))
				return player;
		}

		return null;
	}

	public static boolean isEmpty(Inventory inventory){
		for(ItemStack item : inventory.getContents()){
			if(item == null || item.getType() == Material.AIR)
				return true;
		}

		return false;
	}

	public static void success(String title, String description, Material material, Player player){
		success(title + "\n" + ChatColor.RESET + description, material, player);
	}

	public static void success(String message, Material material, Player player){
		display(message, toMaterialData(material), player);
		playSound(Effect.SUCCESS, player);
	}

	public static void normal(String title, String description, Material material, Player player){
		normal(title + "\n" + ChatColor.RESET + description, material, player);
	}

	public static void normal(String message, Material material, Player player){
		display(message, toMaterialData(material), player);
		playSound(Effect.NORMAL, player);
	}

	public static void warn(String title, String description, Material material, Player player){
		warn(title + "\n" + ChatColor.RESET + description, material, player);
	}

	public static void warn(String message, Material material, Player player){
		GUIManager manager = GUIListener.getListener().getGUIManager(player);
		if(manager.isCooldown())
			return;

		display(message, toMaterialData(material), player);
		playSound(Effect.WARN, player);

		manager.setCooldown(50);
	}

	public static void error(String title, String description, Material material, Player player){
		error(title + "\n" + ChatColor.RESET + description, material, player);
	}

	public static void error(String message, Material material, Player player){
		GUIManager manager = GUIListener.getListener().getGUIManager(player);
		if(manager.isCooldown())
			return;

		display(message, toMaterialData(material), player);
		playSound(Effect.ERROR, player);

		manager.setCooldown(50);
	}

	public static void playSound(Effect effect, Player player){
		if(player == null)
			return;

		Sound sound = null;
		float volume = 1.0f, pitch = 1.0f;

		switch(effect){
		case SUCCESS:
			sound = Sound.ENTITY_PLAYER_LEVELUP;
			break;
		case NORMAL:
			sound = Sound.ENTITY_CHICKEN_EGG;
			break;
		case WARN:
			sound = Sound.ENTITY_IRONGOLEM_HURT;
			break;
		case ERROR:
			sound = Sound.BLOCK_ANVIL_PLACE;
			break;
		default:
			sound = Sound.UI_BUTTON_CLICK;
			break;
		}

		player.playSound(player.getLocation(), sound, volume, pitch);
	}

	public static void display(String message, MaterialData material, Player player){
		if(player == null)
			return;

		AdvancementAPI api = new AdvancementAPI(new NamespacedKey(HyperingEconomyGUI.getPlugin(), "story/" + UUID.randomUUID().toString()))
        .withFrame(FrameType.DEFAULT)
        .withTrigger("minecraft:impossible")
        .withIcon(material)
        .withTitle(message)
        .withDescription("")
        .withAnnouncement(false)
		.withBackground("minecraft:textures/blocks/bedrock.png");
		api.loadAdvancement();
		api.sendPlayer(player);

		Bukkit.getScheduler().runTaskLater(HyperingEconomyGUI.getPlugin(), new Runnable() {
			@Override
			public void run() {
				api.delete(player);
				api.delete();
			}
		}, 50);

	}

	public static Material caseToMaterial(Case cs){
		if(cs == null)
			return Material.RAW_FISH;

		switch(cs){
		case SEND_MONEY:
			return Material.BOOK_AND_QUILL;
		case BUY_TICKET:
			return Material.STORAGE_MINECART;
		case CASH_TICKET:
			return Material.MINECART;
		case BUY_HOGOCHI:
			return Material.BOOK_AND_QUILL;
		case SELL_HOGOCHI:
			return Material.MAP;
		case WITHDRAW_HOGOCHI_SALE:
			return Material.PAPER;
		case FLATTEN_HOGOCHI:
			return Material.GRASS;
		case CONFIRMATION_FLATTEN_HOGOCHI:
			return Material.GRASS;
		case COMBINE_HOGOCHIES:
			return Material.IRON_SPADE;
		case SPLIT_HOGOCHI:
			return Material.IRON_AXE;
		default:
			//到達しない
			return Material.RAW_FISH;
		}
	}

	public static MaterialData toMaterialData(Material material){
		return new MaterialData(material);
	}

	public static int getCost(ProtectedRegion region){
		if(RegionByebye.is25x25(region))
			return 1;
		else if(RegionByebye.is50x50(region))
			return 2;
		else
			return 4;
	}

	public static String replaceAll(final String s, final String regex, String replacement){
		StringBuilder replace = new StringBuilder();
		final int sl = s.length();
		final int rl = regex.length();
		replace.append(s);
		boolean m = false;
		for(int i = 0; i < sl; i++){
			int start = replace.indexOf(regex, i);
			if(start == -1){
				if(start == 0){
					return s;
				}
				return replace.toString();
			}
			replace = replace.replace(start, start + rl, replacement);
			m = true;
		}
		if(!m){
			return s;
		}else{
			return replace.toString();
		}
	}

}

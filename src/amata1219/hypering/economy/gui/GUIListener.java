package amata1219.hypering.economy.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import amata1219.hogochi.byebye.ClaimByebye;
import amata1219.hogochi.byebye.ClaimDeletedEvent;
import amata1219.hogochi.byebye.RegionByebye;
import amata1219.hypering.economy.Database;
import amata1219.hypering.economy.gui.hogochi.HogochiMenu;
import amata1219.hypering.economy.gui.home.GUIManager;
import amata1219.hypering.economy.gui.home.Notification;
import amata1219.hypering.economy.gui.home.PlayerList;
import amata1219.hypering.economy.gui.home.TotalAssetsRankingTable;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Meta;
import amata1219.hypering.economy.gui.util.TotalAssetsRanking;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;
import amata1219.hypering.economy.spigot.CollectedEvent;
import amata1219.hypering.economy.spigot.Electron;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GUIListener implements Listener {

	private static GUIListener listener;

	private HashMap<UUID, GUIManager> managers = new HashMap<>();

	private PlayerList playerList;
	private TotalAssetsRankingTable totalAssetsRanking;
	private Notification notification;

	private GUIListener(){

	}

	public static void load(){
		GUIListener.listener = new GUIListener();

		listener.notification = Notification.load();
		listener.playerList = PlayerList.load();

		if(Electron.isEconomyEnable())
			listener.totalAssetsRanking = TotalAssetsRankingTable.load();
	}

	public void unload(){
		HandlerList.unregisterAll(listener);
	}

	public static GUIListener getListener(){
		return listener;
	}

	public PlayerList getPlayerList(){
		return playerList;
	}

	public TotalAssetsRankingTable getTotalAssetsRanking(){
		return totalAssetsRanking;
	}

	public Notification getNotification(){
		return notification;
	}

	public void loadPlayerData(Player player){
		GUIManager manager = GUIManager.load(player);

		managers.put(player.getUniqueId(), manager);

		if(Electron.isEconomyEnable())
			playerList.addPlayer(player);
	}

	public void unloadPlayerData(Player player){
		managers.remove(player.getUniqueId());

		if(Electron.isEconomyEnable())
			playerList.removePlayer(player);
	}

	public Collection<GUIManager> getGUIManagers(){
		return managers.values();
	}

	public GUIManager getGUIManager(Player player){
		return managers.get(player.getUniqueId());
	}

	private boolean isUseWorldGuard(Player player){
		return player.getWorld().getName().equals("main_flat");
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e){
		loadPlayerData(e.getPlayer());

		new BukkitRunnable(){

			@Override
			public void run(){
				e.getPlayer().saveData();
			}

		}.runTaskLater(HyperingEconomyGUI.getPlugin(), 50);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent e){
		unloadPlayerData(e.getPlayer());
	}

	@EventHandler
	public void onCollect(CollectedEvent e){
		HashMap<UUID, Long> map = e.getMap();

		List<UUID> uuids = new ArrayList<>(map.keySet());
		List<Long> money = new ArrayList<>(map.values());

		TotalAssetsRanking.quickSort(uuids, money, 0, uuids.size() - 1, true);

		Util.broadcast(Sound.ENTITY_PLAYER_LEVELUP);
		Util.broadcast(ChatColor.AQUA + "稼ぎ人ランキング！" + ChatColor.GRAY + "(30分間)");

		for(int i = 1; i <= 5; i++){
			if(i > uuids.size())
				break;

			int delay = i * 8;
			Util.broadcast(Sound.ENTITY_CHICKEN_EGG, delay);
			Util.broadcast(ChatColor.AQUA + String.valueOf(i) + "位: " + ChatColor.RED + Util.getName(uuids.get(i - 1)) + " " + ChatColor.GRAY + "¥" + money.get(i - 1), delay);
		}
	}

	@EventHandler
	public void onDelete(ClaimDeletedEvent e){
		managers.values().forEach(m -> m.checkClaim(e.getClaim(), Message.HOGOCHI_WAS_DELETED));
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onVote(VotifierEvent e){
		Vote vote = e.getVote();
		String name = vote.getUsername();

		OfflinePlayer player = Bukkit.getOfflinePlayer(name);
		if(player == null || player.getName() == null){
			System.out.println("Invalid vote: " + name);
			return;
		}

		Database.getHyperingEconomyAPI().addTickets(player.getUniqueId(), 50);
	}

	public void vote(UUID uuid, String name){
		Database.getHyperingEconomyAPI().addTickets(uuid, 50);

		Util.broadcast(Sound.ENTITY_PLAYER_LEVELUP);

		TextComponent component = new TextComponent(ChatColor.LIGHT_PURPLE + name + "さんが投票しました！");
		component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://minecraft.jp/servers/azisaba.net/vote"));
		component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.GRAY + "クリックすると投票ページへ飛びます！").create()));

		for(Player player : Bukkit.getOnlinePlayers())
			player.spigot().sendMessage(component);
	}

	private final List<EntityType> enemies = Collections.unmodifiableList(new ArrayList<>(Arrays.asList(EntityType.ZOMBIE
			, EntityType.SKELETON, EntityType.CREEPER, EntityType.WITCH, EntityType.ENDERMAN, EntityType.HUSK, EntityType.STRAY
			, EntityType.PIG_ZOMBIE, EntityType.SPIDER, EntityType.BLAZE, EntityType.WITHER_SKELETON, EntityType.SQUID
			, EntityType.GUARDIAN, EntityType.MAGMA_CUBE, EntityType.SLIME)));

	@EventHandler
	public void onSpawn(CreatureSpawnEvent e){
		LivingEntity entity = e.getEntity();
		SpawnReason reason = e.getSpawnReason();
		if(reason == SpawnReason.SPAWNER){
			applyMeta(entity);
			return;
		}

		FileConfiguration config = HyperingEconomyGUI.getPlugin().getConfig();
		int count = 0;

		switch(entity.getType()){
		case ZOMBIE:
		case SKELETON:
		case HUSK:
		case STRAY:
		case CREEPER:
		case SPIDER:
		case WITCH:
		case WITHER_SKELETON:
		case SQUID:
		case BLAZE:
		case MAGMA_CUBE:
		case ZOMBIE_VILLAGER:
		case BAT:
		case ENDERMITE:
			for(Entity ent : entity.getNearbyEntities(16, 16, 16)){
				if(enemies.contains(ent.getType()))
					count++;
			}

			if(count > config.getInt("UpperLimit.Normal"))
				applyMeta(entity);
			return;
		case SLIME:
			if(entity.getLocation().getBlockY() > 40)
				return;

			for(Entity ent : entity.getNearbyEntities(12, 48, 12)){
				if(ent.getType() == EntityType.SLIME)
					count++;
			}

			if(count > config.getInt("UpperLimit.Slime"))
				applyMeta(entity);
			return;
		case GUARDIAN:
			for(Entity ent : entity.getNearbyEntities(38, 38, 38)){
				if(ent.getType() == EntityType.SLIME)
					count++;
			}

			if(count > config.getInt("UpperLimit.Guardian"))
				applyMeta(entity);
			return;
		case PIG_ZOMBIE:
			if(reason == SpawnReason.NETHER_PORTAL){
				for(Entity ent : entity.getNearbyEntities(32, 1, 32)){
					if(ent.getType() == EntityType.SLIME)
						count++;
				}

				if(count > config.getInt("UpperLimit.Pigman"))
					applyMeta(entity);
				return;
			}

			for(Entity ent : entity.getNearbyEntities(16, 16, 16)){
				if(enemies.contains(ent.getType()))
					count++;
			}

			if(count > config.getInt("UpperLimit.Normal"))
				applyMeta(entity);
			return;
		default:
			return;
		}
	}

	private void applyMeta(LivingEntity entity){
		entity.setMetadata("HyperingEconomy:MobKill", new FixedMetadataValue(HyperingEconomyGUI.getPlugin(), this));
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent e){
		Action action = e.getAction();

		if(action == Action.PHYSICAL || action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
			return;

		if(e.getHand() == EquipmentSlot.OFF_HAND)
			return;

		Player player = e.getPlayer();

		Meta meta = Meta.getHasMeta(player);
		if(meta == null){
			if(!player.isSneaking())
				return;

			if(!e.hasItem())
				return;

			ItemStack item = e.getItem();
			if(item == null)
			return;

			String name = item.getType().name();
			if(name.indexOf("PICKAXE") == -1 && name.indexOf("SPADE") == -1)
				return;

			HyperingEconomyGUI.getPlugin().antiGhostBlock(player);
			return;
		}

		e.setCancelled(true);

		Meta.removeMeta(player, meta);

		GUIManager manager = managers.get(player.getUniqueId());

		HogochiMenu hogochiMenu = (HogochiMenu) manager.getGUI(Type.HOGOCHI_MENU);

		Location location = e.getClickedBlock() != null ? e.getClickedBlock().getLocation() : player.getLocation();

		if(isUseWorldGuard(player)){
			if(!RegionByebye.isExistProtectedRegion(location.getBlockX(), location.getBlockZ())){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_NOT_EXIST_HOGOCHI, Util.caseToMaterial(manager.getCase()), player);
				return;
			}

			switch(meta){
			case BUY_HOGOCHI:
				hogochiMenu.buyRegion(location);
				return;
			case SELL_HOGOCHI:
				hogochiMenu.sellRegion(location);
				return;
			case WITHDRAW_HOGOCHI_SALE:
				hogochiMenu.withdrawRegionSale(location);
				return;
			case FLATTEN_HOGOCHI:
				hogochiMenu.flattenRegion(location);
				return;
			case COMBINE_HOGOCHIES:
				hogochiMenu.combineRegions(location);
				return;
			case SPLIT_HOGOCHI:
				hogochiMenu.splitRegion(location);
				return;
			default:
				return;
			}
		}else{
			if(!ClaimByebye.isExistClaim(location)){
				Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_NOT_EXIST_HOGOCHI, Util.caseToMaterial(manager.getCase()), player);
				return;
			}

			switch(meta){
			case BUY_HOGOCHI:
				hogochiMenu.buyClaim(location);
				return;
			case SELL_HOGOCHI:
				hogochiMenu.sellClaim(location);
				return;
			case WITHDRAW_HOGOCHI_SALE:
				hogochiMenu.withdrawClaimSale(location);
				return;
			default:
				return;
			}
		}
	}

	@EventHandler
	public void onTeleport(PlayerChangedWorldEvent e){
		Player player = e.getPlayer();

		Meta meta = Meta.getHasMeta(player);
		if(meta == null)
			return;

		Meta.removeMeta(player, meta);

		GUIManager manager = managers.get(player.getUniqueId());
		manager.setCase(null);

		Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.TRY_AGAIN_FROM_HOGOCHI_MENU, Util.caseToMaterial(manager.getCase()), player);
	}

	@EventHandler
	public void onClick(InventoryClickEvent e){
		if(!(e.getWhoClicked() instanceof Player))
			return;

		Player player = (Player) e.getWhoClicked();
		UUID uuid = player.getUniqueId();

		int slotNumber = e.getSlot();
		if(slotNumber < 0 || slotNumber > 53)
			return;

		Inventory inventory = e.getInventory();
		if(inventory == null)
			return;

		String title = inventory.getTitle();
		if(title == null)
			return;

		Type type = Type.toType(title);
		if(type == null)
			return;

		e.setCancelled(true);

		GUIManager manager = managers.get(uuid);
		if(manager == null)
			return;

		switch(type){
		case PLAYER_LIST:
			String[] splitedTitle = title.split("_");
			if(splitedTitle.length != 3)
				break;

			playerList.push(player, splitedTitle[2].charAt(0), slotNumber);
			break;
		case NOTIFICATION:
			notification.action(player, slotNumber);
			break;
		case TOTAL_ASSETS_RANKING_TABLE:
			break;
		default:
			manager.getGUI(type).push(slotNumber);
			break;
		}
	}

}

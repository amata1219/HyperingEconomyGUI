package amata1219.hypering.economy.gui;

import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;

import amata1219.hogochi.byebye.ClaimDeletedEvent;
import amata1219.hogochi.byebye.RegionByebye;
import amata1219.hypering.economy.gui.hogochi.HogochiMenu;
import amata1219.hypering.economy.gui.home.GUIManager;
import amata1219.hypering.economy.gui.home.Notification;
import amata1219.hypering.economy.gui.home.PlayerList;
import amata1219.hypering.economy.gui.home.PossessionMoneyRanking;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Meta;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;
import amata1219.hypering.economy.spigot.Electron;

public class GUIListener implements Listener {

	private static GUIListener listener;

	private HashMap<UUID, GUIManager> managers = new HashMap<>();

	private PlayerList playerList;
	private PossessionMoneyRanking possesionMoneyRanking;
	private Notification notification;

	private GUIListener(){

	}

	public static void load(){
		GUIListener.listener = new GUIListener();

		listener.notification = Notification.load();
		listener.playerList = PlayerList.load();

		if(Electron.isEconomyEnable())
			listener.possesionMoneyRanking = PossessionMoneyRanking.load();
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

	public PossessionMoneyRanking getPossesionMoneyRanking(){
		return possesionMoneyRanking;
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

	private boolean isUseWorldGuard(Player player){
		return player.getWorld().getName().equals("main_flat");
	}

	@EventHandler
	public void onDelete(ClaimDeletedEvent e){
		managers.values().forEach(m -> m.checkClaim(e.getClaim(), Message.HOGOCHI_WAS_DELETED));
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
		if(meta == null)
			return;

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
				break;
			case SELL_HOGOCHI:
				hogochiMenu.sellRegion(location);
				break;
			case WITHDRAW_HOGOCHI_SALE:
				hogochiMenu.withdrawRegionSale(location);
				break;
			case FLATTEN_HOGOCHI:
				hogochiMenu.flattenRegion(location);
				break;
			case COMBINE_HOGOCHIES:
				hogochiMenu.combineRegions(location);
				break;
			case SPLIT_HOGOCHI:
				hogochiMenu.splitRegion(location);
				break;
			default:
				return;
			}
		}else{
			switch(meta){
			case BUY_HOGOCHI:
				hogochiMenu.buyClaim(location);
				break;
			case SELL_HOGOCHI:
				hogochiMenu.sellClaim(location);
				break;
			case WITHDRAW_HOGOCHI_SALE:
				hogochiMenu.withdrawClaimSale(location);
				break;
			default:
				return;
			}
		}

		Util.warn(Message.WARN + Message.caseToString(manager.getCase()), Message.IS_NOT_EXIST_HOGOCHI, Util.caseToMaterial(manager.getCase()), player);
	}

	@EventHandler
	public void onTeleport(PlayerTeleportEvent e){
		if(e.getFrom().getWorld().getName().equals(e.getTo().getWorld().getName()))
			return;

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
		default:
			manager.getGUI(type).push(slotNumber);
			break;
		}
	}

}

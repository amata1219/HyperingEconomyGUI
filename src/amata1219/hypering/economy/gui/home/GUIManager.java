package amata1219.hypering.economy.gui.home;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import amata1219.hypering.economy.gui.HyperingEconomyGUI;
import amata1219.hypering.economy.gui.hogochi.CombineRegions;
import amata1219.hypering.economy.gui.hogochi.HogochiMenu;
import amata1219.hypering.economy.gui.hogochi.SplitRegion;
import amata1219.hypering.economy.gui.util.Case;
import amata1219.hypering.economy.gui.util.Message;
import amata1219.hypering.economy.gui.util.Type;
import amata1219.hypering.economy.gui.util.Util;
import me.ryanhamshire.GriefPrevention.Claim;

public class GUIManager {

	private Player player;
	private HashMap<Integer, GraphicalUserInterface> guis = new HashMap<>();
	public final HashMap<Integer, Object> memory = new HashMap<>();
	/*
	 * 0 - Long number
	 * 1 - OfflinePlayer offline_player
	 * 2 - Claim claim
	 * 3 - ProtectedRegion region
	 * 4 - String hogochi_id
	 * 5 - Long price
	 * 6 - Integer tickets
	 */
	private Case cs;
	private boolean cooldown;

	private GUIManager(){

	}

	public static GUIManager load(Player player){
		GUIManager manager = new GUIManager();

		manager.player = player;

		manager.registerGUI(HomeMenu.load(manager));
		manager.registerGUI(NumberScanner.load(manager));
		manager.registerGUI(CharacterScanner.load(manager));
		manager.registerGUI(Confirmation.load(manager));
		manager.registerGUI(HogochiMenu.load(manager));
		manager.registerGUI(CombineRegions.load(manager));
		manager.registerGUI(SplitRegion.load(manager));

		return manager;
	}

	public void registerGUI(GraphicalUserInterface gui){
		guis.put(gui.getId(), gui);
	}

	public void unregisterGUI(int id){
		guis.remove(id);
	}

	public GraphicalUserInterface getGUI(Type type){
		return guis.get(type.getId());
	}

	public GraphicalUserInterface getGUI(int id){
		return guis.get(id);
	}

	public Player getPlayer(){
		return player;
	}

	public UUID getUniqueId(){
		return player.getUniqueId();
	}

	public void display(Type type){
		if(type == Type.HOME_MENU)
			guis.get(Type.CONFIRMATION.getId()).update();

		GraphicalUserInterface gui = guis.get(type.getId());

		if(type != Type.CONFIRMATION)
			gui.update();

		player.openInventory(gui.getInventory());
	}

	public Case getCase(){
		return cs;
	}

	public void setCase(Case cs){
		this.cs = cs;
	}

	public boolean hasCase(){
		return cs != null;
	}

	public void checkClaim(Claim claim, String reason){
		if(!hasCase() || !memory.containsKey(2))
			return;

		if(!memory.get(2).equals(claim.getID()))
			return;

		close();

		Util.warn(Message.WARN + Message.caseToString(cs) , reason, Util.caseToMaterial(cs), getPlayer());
	}

	public void checkRegion(ProtectedRegion region, String reason){
		if(!hasCase() || !memory.containsKey(3))
			return;

		if(!memory.get(3).equals(region.getId()))
			return;

		close();

		Util.warn(Message.WARN + Message.caseToString(cs) , reason, Util.caseToMaterial(cs), getPlayer());
	}

	public boolean isCooldown(){
		return cooldown;
	}

	public void setCooldown(long time){
		cooldown = true;

		new BukkitRunnable(){

			@Override
			public void run(){
				cooldown = false;
			}

		}.runTaskLater(HyperingEconomyGUI.getPlugin(), time);
	}

	public void close(){
		hide();
		clear();
	}

	public void hide(){
		player.closeInventory();
	}

	public void clear(){
		cs = null;

		memory.clear();
	}

}

package amata1219.hypering.economy.gui.util;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_13_R2.util.CraftNamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import amata1219.hypering.economy.gui.HyperingEconomyGUI;
import net.minecraft.server.v1_13_R2.AdvancementDataWorld;
import net.minecraft.server.v1_13_R2.ChatDeserializer;
import net.minecraft.server.v1_13_R2.IRegistry;
import net.minecraft.server.v1_13_R2.Item;
import net.minecraft.server.v1_13_R2.MinecraftKey;
import net.minecraft.server.v1_13_R2.MinecraftServer;
import net.minecraft.server.v1_13_R2.Advancement.SerializedAdvancement;

public class Advancement {

	private final NamespacedKey id;

	@SuppressWarnings({ "unchecked", "deprecation" })
	public Advancement(Material material, String message){
		id = new NamespacedKey(HyperingEconomyGUI.getPlugin(), "story/" + UUID.randomUUID().toString());

		if(Bukkit.getAdvancement(id) != null)
			return;

		ItemStack item = new ItemStack(material);
		final net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		final int c = Item.getId(nmsItem.getItem());
		final MinecraftKey key = IRegistry.ITEM.keySet().stream().filter(k -> Item.getId(IRegistry.ITEM.get(k)) == c).findFirst().orElse(null);

		JSONObject json = new JSONObject();

		JSONObject icon = new JSONObject();
		icon.put("item", key.toString());
		//icon.put("data", item.getData().getData());

		JSONObject display = new JSONObject();
		display.put("icon", icon);
		display.put("title", message);
		display.put("description", "");
		display.put("background", "minecraft:textures/blocks/bedrock.png");
		display.put("frame", "task");
		display.put("announce_to_chat", false);
		display.put("show_toast", true);

		json.put("parent", null);

		JSONObject criteria = new JSONObject();
		JSONObject conditions = new JSONObject();
		JSONObject elytra = new JSONObject();

		JSONArray itemArray = new JSONArray();

		conditions.put("items", itemArray);
		elytra.put("trigger", "minecraft:impossible");
		elytra.put("conditions", conditions);

		criteria.put("elytra", elytra);

		json.put("criteria", criteria);
		json.put("display", display);


		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String prettyJson = gson.toJson(json);

		//org.bukkit.craftbukkit.v1_13_R2.util.CraftMagicNumbers
		SerializedAdvancement advancement = (SerializedAdvancement) ChatDeserializer.a(AdvancementDataWorld.DESERIALIZER, prettyJson, SerializedAdvancement.class);
		if(advancement == null)
			return;

		AdvancementDataWorld.REGISTRY.a(Maps.newHashMap(Collections.singletonMap(CraftNamespacedKey.toMinecraft(id), advancement)));
		org.bukkit.advancement.Advancement bukkit = Bukkit.getAdvancement(id);
		if(bukkit != null)
			MinecraftServer.getServer().getPlayerList().reload();
	}

	public void send(Player player, boolean award){
		AdvancementProgress progress = player.getAdvancementProgress(Bukkit.getAdvancement(id));
		if(award){
			if(!progress.isDone())
				progress.awardCriteria("elytra");
		}else{
			if(progress.isDone())
				progress.revokeCriteria("elytra");
		}
	}

	public void send(Collection<? extends Player> players, boolean award){
		players.forEach(player -> send(player, award));
	}

	@SuppressWarnings("deprecation")
	public void unload(){
		Bukkit.getUnsafe().removeAdvancement(id);
	}

}

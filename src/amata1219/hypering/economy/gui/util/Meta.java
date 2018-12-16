package amata1219.hypering.economy.gui.util;

import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import amata1219.hypering.economy.gui.HyperingEconomyGUI;

public enum Meta {

	BUY_HOGOCHI("HyperingEconomyGUI:BuyHogochi"),
	SELL_HOGOCHI("HyperingEconomyGUI:SellHogochi"),
	WITHDRAW_HOGOCHI_SALE("HyperingEconomyGUI:WithdrawHogochiSale"),
	FLATTEN_HOGOCHI("HyperingEconomyGUI:FlattenHogochi"),
	COMBINE_HOGOCHIES("HyperingEconomyGUI:CombineHogochies"),
	SPLIT_HOGOCHI("HyperingEconomyGUI:SplitHogochi");

	private final String meta;

	private Meta(String meta){
		this.meta = meta;
	}

	public String getString(){
		return meta;
	}

	public static void setMeta(Player player, Meta meta){
		player.setMetadata(meta.getString(), new FixedMetadataValue(HyperingEconomyGUI.getPlugin(), HyperingEconomyGUI.getPlugin()));
	}

	public static boolean hasMeta(Player player, Meta meta){
		return player.hasMetadata(meta.getString());
	}

	public static Meta getHasMeta(Player player){
		for(Meta meta : values()){
			if(player.hasMetadata(meta.getString()))
				return meta;
		}

		return null;
	}

	public static void removeMeta(Player player, Meta meta){
		player.removeMetadata(meta.getString(), HyperingEconomyGUI.getPlugin());
	}

	public static void removeMeta(Player player){
		for(Meta meta : values())
			player.removeMetadata(meta.getString(), HyperingEconomyGUI.getPlugin());
	}

}

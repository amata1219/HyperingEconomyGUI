package amata1219.hypering.economy.gui.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import amata1219.hypering.economy.Database;
import amata1219.hypering.economy.ServerName;
import amata1219.hypering.economy.gui.HyperingEconomyGUI;

public class TotalAssetsRanking {

	private BiMap<Integer, UUID> ranking = HashBiMap.create();
	private HashMap<UUID, Long> assets = new HashMap<>();

	private TotalAssetsRanking(){

	}

	public static TotalAssetsRanking load(ServerName serverName){
		TotalAssetsRanking ranking = new TotalAssetsRanking();

		String columnIndex = serverName.name().toLowerCase();

		List<UUID> uuids = new ArrayList<>();
		List<Long> assets = new ArrayList<>();
		long price = Database.getHyperingEconomyAPI().getTicketPrice(serverName);
		try(Connection con = Database.getHikariDataSource().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT uuid, " + columnIndex + " FROM " + Database.getDatabaseName() + "." + Database.getPlayerDataTableName())){
			try(ResultSet result = statement.executeQuery()){
				while(result.next()){
					UUID uuid = UUID.fromString(result.getString("uuid"));
					uuids.add(uuid);
					assets.add(result.getLong(columnIndex) + HyperingEconomyGUI.getMainFlatRegionCount(uuid) * 250 * price + HyperingEconomyGUI.getBonusBlocks(uuid) * 100);
				}

				result.close();
			}
		}catch(SQLException e){
			e.printStackTrace();
		}

		quickSort(uuids, assets, 0, uuids.size() - 1, true);

		for(int i = 0; i < uuids.size(); i++){
			ranking.ranking.put(i + 1, uuids.get(i));
			ranking.assets.put(uuids.get(i), assets.get(i));
		}

		return ranking;
	}

	public int size(){
		return ranking.size();
	}

	public boolean has(int i){
		return i <= size();
	}

	public UUID matchedUniqueId(int rank){
		return ranking.get(rank);
	}

	public int getRank(UUID uuid){
		if(!ranking.containsValue(uuid))
			return ranking.size();

		return ranking.inverse().get(uuid);
	}

	public long getTotalAssets(int rank){
		UUID uuid = matchedUniqueId(rank);
		if(uuid == null)
			return 0L;

		return getTotalAssets(uuid);
	}

	public long getTotalAssets(UUID uuid){
		return assets.get(uuid);
	}

	public boolean isInvalid(){
		return ranking.size() != assets.size();
	}

	public static void quickSort(List<UUID> uuids, List<Long> assets, int left, int right, boolean reverse){
		if(left <= right){
			long p = assets.get((left + right) >>> 1);
			int l = left;
			int r = right;
			while(l <= r){
				if(reverse){
					while(assets.get(l) > p)
						l++;

					while(assets.get(r) < p)
						r--;
				}else{
					while(assets.get(l) < p)
						l++;

					while(assets.get(r) > p)
						r--;
				}

				if(l <= r){
					UUID tmp1 = uuids.get(l);
					uuids.set(l, uuids.get(r));
					uuids.set(r, tmp1);

					Long tmp2 = assets.get(l);
					assets.set(l, assets.get(r));
					assets.set(r, tmp2);

					l++;
					r--;
				}
			}
			quickSort(uuids, assets, left, r, reverse);
			quickSort(uuids, assets, l, right, reverse);
		}
	}


}
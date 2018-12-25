package amata1219.hypering.economy.gui.util;

public enum Type {

	HOME_MENU(0),
	NUMBER_SCANNER(1),
	CHARACTER_SCANNER(2),
	CONFIRMATION(3),
	PLAYER_LIST(4),
	TOTAL_ASSETS_RANKING_TABLE(5),
	NOTIFICATION(6),
	HOGOCHI_MENU(7),
	COMBINE_REGIONS(8),
	SPLIT_REGION(9);

	private final int id;

	private Type(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public static Type toType(String inventoryTitle){
		if(inventoryTitle.startsWith("§6PLAYER_LIST_"))
			return PLAYER_LIST;

		switch(inventoryTitle){
		case "§6HOME_MENU":
			return HOME_MENU;
		case "§6NUMBER_SCANNER":
			return NUMBER_SCANNER;
		case "§6CHARACTER_SCANNER":
			return CHARACTER_SCANNER;
		case "§6CONFIRMATION":
			return CONFIRMATION;
		case "§6TOTAL_ASSETS_RANKING_TABLE":
			return TOTAL_ASSETS_RANKING_TABLE;
		case "§6NOTIFICATION":
			return NOTIFICATION;
		case "§6HOGOCHI_MENU":
			return HOGOCHI_MENU;
		case "§6COMBINE_REGIONS":
			return COMBINE_REGIONS;
		case "§6SPLIT_REGION":
			return SPLIT_REGION;
		default:
			return null;
		}
	}

}

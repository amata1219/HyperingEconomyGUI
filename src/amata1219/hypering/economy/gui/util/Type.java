package amata1219.hypering.economy.gui.util;

public enum Type {

	HOME_MENU(0),
	NUMBER_SCANNER(1),
	CHARACTER_SCANNER(2),
	CONFIRMATION(3),
	PLAYER_LIST(4),
	POSSESSION_MONEY_RANKING(5),
	NOTIFICATION(6),
	HOGOCHI_MENU(7),
	COMBINE_HOGOCHIES(8),
	SPLIT_HOGOCHI(9);

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
		case "§6POSSESSION_MONEY_RANKING":
			return POSSESSION_MONEY_RANKING;
		case "§6NOTIFICATION":
			return NOTIFICATION;
		case "§6HOGOCHI_MENU":
			return HOGOCHI_MENU;
		case "§6COMBINE_HOGOCHIES":
			return COMBINE_HOGOCHIES;
		case "§6SPLIT_HOGOCHI":
			return SPLIT_HOGOCHI;
		default:
			return null;
		}
	}

}

package amata1219.hypering.economy.gui.util;

public enum Color {

	WHITE((short) 0),
	ORANGE((short) 1),
	RED_PURPLE((short) 2),
	AQUA((short) 3),
	YELLOW((short) 4),
	YELLOW_GREEN((short) 5),
	PINK((short) 6),
	GRAY((short) 7),
	LIGHT_GRAY((short) 8),
	BLUE_GREEN((short) 9),
	PURPLE((short) 10),
	BLUE((short) 11),
	BROWN((short) 12),
	GREEN((short) 13),
	RED((short) 14),
	BLACK((short) 15);

	private final short data;

	private Color(short data){
		this.data = data;
	}

	public short getData(){
		return data;
	}

	public static Color getColor(int data){
		switch(data){
		case 0:
			return WHITE;
		case 1:
			return  ORANGE;
		case 2:
			return RED_PURPLE;
		case 3:
			return AQUA;
		case 4:
			return YELLOW;
		case 5:
			return YELLOW_GREEN;
		case 6:
			return PINK;
		case 7:
			return GRAY;
		case 8:
			return LIGHT_GRAY;
		case 9:
			return BLUE_GREEN;
		case 10:
			return PURPLE;
		case 11:
			return BLUE;
		case 12:
			return BROWN;
		case 13:
			return GREEN;
		case 14:
			return RED;
		case 15:
			return BLACK;
		default:
			return LIGHT_GRAY;
		}
	}

}

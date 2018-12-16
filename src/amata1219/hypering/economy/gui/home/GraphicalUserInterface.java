package amata1219.hypering.economy.gui.home;

import org.bukkit.inventory.Inventory;

public interface GraphicalUserInterface {

	int getId();

	Inventory getInventory();

	void update();

	void clear();

	void push(int slotNumber);

}

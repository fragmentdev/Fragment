package me.xemu.fragment.menu.guis;

import me.xemu.fragment.menu.Menu;
import me.xemu.fragment.menu.MenuUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MainMenu extends Menu {
	public MainMenu(MenuUtil menuUtil) {
		super(menuUtil);
	}

	@Override
	public String getMenuName() {
		return "Main Menu";
	}

	@Override
	public int getSlots() {
		return 54;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		if (e.getCurrentItem().getType().equals(Material.PAPER)) {

		}
 	}

	@Override
	public void setMenuItems() {
		getInventory().addItem(makeItem(Material.PAPER, "&aGroups"));
		getInventory().addItem(makeItem(Material.PAPER, "&aUsers"));
	}
}

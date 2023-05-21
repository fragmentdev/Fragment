package me.xemu.fragment.menu.menus;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.manager.UserManager;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

import static me.xemu.fragment.utils.Utils.deformat;

public class UsersMenu extends Paged {


	List<Player> players = new ArrayList<>();

	public UsersMenu(MenuUtil menuUtil) {
		super(menuUtil);

		if (!players.isEmpty()) players.clear();
		players.addAll(Bukkit.getOnlinePlayers());
	}

	@Override
	public String getMenuName() {
		return "Fragment > Users:";
	}

	@Override
	public int getSlots() {
		return 54;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		String displayname = deformat(e.getCurrentItem().getItemMeta().getDisplayName());

		if (displayname.equalsIgnoreCase("Users")) {
			new UsersMenu(FragmentPlugin.getMenuUtil(player)).open();
		} else if (displayname.equalsIgnoreCase("Groups")) {
			new GroupsMenu(FragmentPlugin.getMenuUtil(player)).open();
		} else if (displayname.equalsIgnoreCase("Settings")) {
			new SettingsMenu(FragmentPlugin.getMenuUtil(player)).open();
		} else if (displayname.equalsIgnoreCase("Back")) {
			if (page != 0) {
				page = page - 1;
				super.open();
			} else {
				new MainMenu(FragmentPlugin.getMenuUtil(player)).open();
			}
		} else if (displayname.equalsIgnoreCase("Next")) {
			if (!((index + 1) >= Bukkit.getOnlinePlayers().size())) {
				page = page + 1;
				super.open();
			}
		}
	}

	@Override
	public void setMenuItems() {
		applyLayout(true);

		if (!players.isEmpty()) {
			int maxItemsPerPage = 24;
			int startIndex = page * maxItemsPerPage;
			int endIndex = Math.min(startIndex + maxItemsPerPage, players.size());

			for (int i = startIndex; i < endIndex; i++) {
				Player player = players.get(i);
				if (player == null) continue;
				User user = UserManager.load(player.getUniqueId());

				List<String> lore = new ArrayList<>();
				lore.add("&7Groups: (name, weight)");
				for (Group group : user.getGroups()) {
					lore.add(" &8&l> &f&l" + group.getName() + " &7- &e" + group.getWeight());
				}
				getInventory().addItem(makeItem(Material.PLAYER_HEAD, "&e&n" + player.getName(), lore));
			}
		}
	}

}

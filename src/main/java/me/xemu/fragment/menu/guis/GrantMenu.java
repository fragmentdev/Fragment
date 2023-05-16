package me.xemu.fragment.menu.guis;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.MySqlDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.utils.Interaction;
import me.xemu.fragment.utils.Receiver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

import static me.xemu.fragment.utils.Utils.deformat;

public class GrantMenu extends Paged {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private Player target;

	protected List<Group> groups;

	public GrantMenu(MenuUtil menuUtil, Player target) {
		super(menuUtil);
		this.target = target;

		this.groups = plugin.getFragmentDatabase().getGroups();
	}

	@Override
	public String getMenuName() {
		return "Fragment > Grant Player:";
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

			//plugin.loadDatabase();
	}

	@Override
	public void setMenuItems() {
		applyLayout(false);

		if (!groups.isEmpty()) {
			int maxItemsPerPage = 24;
			int startIndex = page * maxItemsPerPage;
			int endIndex = Math.min(startIndex + maxItemsPerPage, groups.size());

			for (int i = startIndex; i < endIndex; i++) {
				Group g = groups.get(i);
				if (group == null) continue;
				Group group = plugin.getFragmentDatabase().loadGroup(g.getName());

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
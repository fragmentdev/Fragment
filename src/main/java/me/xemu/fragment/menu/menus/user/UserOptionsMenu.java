package me.xemu.fragment.menu.menus.user;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.manager.GroupManager;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.menu.menus.GroupsMenu;
import me.xemu.fragment.menu.menus.SettingsMenu;
import me.xemu.fragment.menu.menus.UsersMenu;
import me.xemu.fragment.menu.menus.group.GroupEditMenu;
import me.xemu.fragment.menu.menus.group.GroupPermissionsEditMenu;
import me.xemu.fragment.utils.Interaction;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static me.xemu.fragment.utils.Utils.deformat;

public class UserOptionsMenu extends Paged {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getDatabase();

	private User user;

	public UserOptionsMenu(MenuUtil menuUtil, User user) {
		super(menuUtil);

		if (user != null) {
			this.user = user;
		}
	}

	@Override
	public String getMenuName() {
		return "Fragment > User Options:";
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
				new GroupsMenu(FragmentPlugin.getMenuUtil(player)).open();
			}
		} else if (displayname.equalsIgnoreCase("Next")) {
			e.setCancelled(true);
		} else if (displayname.equalsIgnoreCase("Settings")) {
			new SettingsMenu(FragmentPlugin.getMenuUtil(player)).open();
		} else if (displayname.equalsIgnoreCase("Manage User Permissions")) {
			new UserPermissionsEditMenu(FragmentPlugin.getMenuUtil(Bukkit.getPlayer(user.getUuid()))).open();
		}
	}

	@Override
	public void setMenuItems() {
		applyLayout(true);

		getInventory().setItem(21, makeItem(Material.ANVIL, "&aManage User Permissions", "§7Manage user permissions."));
		getInventory().setItem(22, makeItem(Material.ANVIL, "&aManage Grants", "§7Manage grants using commands."));
	}

	private void reOpenMenu(Player player) {
		new GroupEditMenu(plugin.getMenuUtil(player, menuUtil.getGroup())).open();
	}
}


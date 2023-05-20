package me.xemu.fragment.menu.menus.create;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.menu.menus.GroupsMenu;
import me.xemu.fragment.menu.menus.SettingsMenu;
import me.xemu.fragment.menu.menus.UsersMenu;
import me.xemu.fragment.utils.Interaction;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.List;

import static me.xemu.fragment.utils.Utils.deformat;

public class GroupPermissionsEditMenu extends Paged {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getDatabase();

	List<String> groupPermissions = new ArrayList<>();

	public GroupPermissionsEditMenu(MenuUtil menuUtil) {
		super(menuUtil);

		if (!groupPermissions.isEmpty()) groupPermissions.clear();

		if (database.loadGroup(menuUtil.getGroup()) != null) {
			groupPermissions = database.loadGroup(menuUtil.getGroup()).getPermissions();
		} else {
			groupPermissions = new ArrayList<>();
		}
	}

	@Override
	public String getMenuName() {
		return "Fragment > " + menuUtil.getGroup() + ": Perms";
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
			if (!((index + 1) >= groupPermissions.size())) {
				page = page + 1;
				super.open();
			}
		} else if (displayname.equalsIgnoreCase("Settings")) {
			new SettingsMenu(FragmentPlugin.getMenuUtil(player)).open();
		} else if (displayname.equalsIgnoreCase("Manual Permission Add")) {
			Interaction interaction = new Interaction(plugin);
			player.closeInventory();
			interaction.startInteraction(player, "Permission Node", this, FragmentPlugin.getMenuUtil(player, menuUtil.getGroup())).thenAccept(interactionAccept -> {
				player.closeInventory();
				Group group = database.loadGroup(menuUtil.getGroup());
				if (!group.getPermissions().contains(interactionAccept)) {
					group.getPermissions().add(interactionAccept);
					database.saveGroup(group);
					Bukkit.getScheduler().runTask(FragmentPlugin.getFragment(), () -> {
						reOpenMenu(player);
					});
				} else {
					reOpenMenu(player);
				}
			});
		} else {
			String perm = displayname;

			Group group = database.loadGroup(menuUtil.getGroup());

			if (!group.getPermissions().contains(perm)) {
				group.getPermissions().add(perm);
				super.open();
			} else {
				group.getPermissions().remove(perm);
				super.open();
			}
		}
	}

	@Override
	public void setMenuItems() {
		applyLayout(true);

		if (!getAllPermissions().isEmpty()) {
			int maxItemsPerPage = 24;
			int startIndex = page * maxItemsPerPage;
			int endIndex = Math.min(startIndex + maxItemsPerPage, getAllPermissions().size());

			for (int i = startIndex; i < endIndex; i++) {
				String perm = getAllPermissions().get(i);
				if (perm == null) continue;

				if (groupPermissions.contains(perm)) {
					getInventory().addItem(makeItem(Material.LIME_DYE, "&a" + perm));
				} else {
					getInventory().addItem(makeItem(Material.GRAY_DYE, "&7" + perm));
				}
			}
		}

		getInventory().setItem(53, makeItem(Material.ANVIL, "Manual Permission Add", "§7Add permission manually.", "§aClick me to add."));
	}

	public static List<String> getAllPermissions() {
		List<String> permissions = new ArrayList<>();

		for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
			permissions.addAll(permission.getChildren().keySet());
		}

		return permissions;
	}

	private void reOpenMenu(Player player) {
		new GroupEditMenu(FragmentPlugin.getMenuUtil(player, menuUtil.getGroup())).open();
	}

}
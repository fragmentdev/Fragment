package me.xemu.fragment.menu.menus.user;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.manager.UserManager;
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

public class UserPermissionsEditMenu extends Paged {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getDatabase();

	List<String> userPermissions = new ArrayList<>();

	public UserPermissionsEditMenu(MenuUtil menuUtil) {
		super(menuUtil);

		if (!userPermissions.isEmpty()) userPermissions.clear();

		if (database.loadUser(menuUtil.getOwner().getUniqueId()) != null) {
			userPermissions = database.loadUser(menuUtil.getOwner().getUniqueId()).getPlayerPermissions();
		} else {
			userPermissions = new ArrayList<>();
		}
	}

	@Override
	public String getMenuName() {
		return "Fragment > " + menuUtil.getOwner().getName() + ": User Perms";
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
				new UserOptionsMenu(FragmentPlugin.getMenuUtil(player), UserManager.load(player)).open();
			}
		} else if (displayname.equalsIgnoreCase("Next")) {
			if (!((index + 1) >= userPermissions.size())) {
				page = page + 1;
				super.open();
			}
		} else if (displayname.equalsIgnoreCase("Settings")) {
			new SettingsMenu(FragmentPlugin.getMenuUtil(player)).open();
		} else if (displayname.equalsIgnoreCase("Manual Permission Add")) {
			Interaction interaction = new Interaction(plugin);
			player.closeInventory();
			interaction.startInteraction(player, "Permission Node", this, FragmentPlugin.getMenuUtil(player)).thenAccept(interactionAccept -> {
				player.closeInventory();
				User user = UserManager.load(menuUtil.getOwner());
				if (!user.getPlayerPermissions().contains(interactionAccept)) {
					user.getPlayerPermissions().add(interactionAccept);
					database.saveUser(user);
					Bukkit.getScheduler().runTask(FragmentPlugin.getFragment(), () -> {
						reOpenMenu(player);
					});
				} else {
					reOpenMenu(player);
				}
			});
		} else {
			String perm = displayname;

			User user = UserManager.load(menuUtil.getOwner());

			if (!user.getPlayerPermissions().contains(perm)) {
				user.getPlayerPermissions().add(perm);
				database.saveUser(user);
				super.open();
			} else {
				user.getPlayerPermissions().remove(perm);
				database.saveUser(user);
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

				if (userPermissions.contains(perm)) {
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
		new UserPermissionsEditMenu(FragmentPlugin.getMenuUtil(player)).open();
	}

}
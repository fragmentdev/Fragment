package me.xemu.fragment.menu.menus.group;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.manager.GroupManager;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.menu.menus.GroupsMenu;
import me.xemu.fragment.menu.menus.SettingsMenu;
import me.xemu.fragment.menu.menus.UsersMenu;
import me.xemu.fragment.utils.Interaction;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.xemu.fragment.utils.Utils.deformat;

public class GroupCreateMenu extends Paged {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getDatabase();

	private Map<UUID, String> nameMap = new HashMap<>();
	private Map<UUID, Integer> weightMap = new HashMap<>();

	private Group group;

	public GroupCreateMenu(MenuUtil menuUtil) {
		super(menuUtil);

		if (database.loadGroup(menuUtil.getGroup()) != null) {
			group = GroupManager.load(menuUtil.getGroup());
		}

		nameMap.remove(menuUtil.getOwner().getUniqueId());
		weightMap.remove(menuUtil.getOwner().getUniqueId());
	}

	@Override
	public String getMenuName() {
		return "Fragment > Create Group:";
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
		} else if (displayname.equalsIgnoreCase("Group Name")) {
			Interaction interaction = new Interaction(plugin);
			player.closeInventory();
			interaction.startInteraction(player, "Group Name", this, FragmentPlugin.getMenuUtil(player, group.getName())).thenAccept(interactionAccept -> {
				nameMap.put(player.getUniqueId(), interactionAccept);
				Bukkit.getScheduler().runTask(plugin, () -> {
					// Reopen the GUI
					super.open();
					//reOpenMenu(player);
				});
			});
		} else if (displayname.equalsIgnoreCase("Group Weight")) {
			Interaction interaction = new Interaction(plugin);
			player.closeInventory();
			interaction.startInteraction(player, "Group Weight (Only number)", this, FragmentPlugin.getMenuUtil(player, group.getName())).thenAccept(interactionAccept -> {
				if (Utils.isNumber(interactionAccept)) {
					weightMap.put(player.getUniqueId(), Integer.parseInt(interactionAccept));
				} else {
					player.sendMessage(ChatColor.RED + "You provided a invalid number. Weight did not change.");
					return;
				}
				Bukkit.getScheduler().runTask(plugin, () -> {
					// Reopen the GUI
					super.open();
					 //reOpenMenu(player);
				});
			});
		} else if (displayname.equalsIgnoreCase("Create Group")) {
			if (!nameMap.containsKey(player.getUniqueId()) || !weightMap.containsKey(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "Could not create group. You did not specify name or weight.");
				player.closeInventory();
				return;
			} else if (doesGroupAlreadyExist(nameMap.get(menuUtil.getOwner().getUniqueId()))) {
				player.sendMessage(ChatColor.RED + "Group already exists.");
				player.closeInventory();
				return;
			} else {
				plugin.getDatabase().saveGroup(
						new Group(
								nameMap.get(menuUtil.getOwner().getUniqueId()),
								weightMap.get(menuUtil.getOwner().getUniqueId()),
								null,
								null,
								null,
								null
						)
				);
				player.closeInventory();
				new GroupsMenu(FragmentPlugin.getMenuUtil(player)).open();
			}
		}
	}

	@Override
	public void setMenuItems() {
		applyLayout(true);

		String name = nameMap.get(menuUtil.getOwner().getUniqueId());
		if (name == null) {
			name = "Not set";
		}

		String weight = String.valueOf(weightMap.get(menuUtil.getOwner().getUniqueId()));
		if (weight == null) {
			weight = "Not set";
		}

		// TODO: Improve display on lore
		getInventory().setItem(21, makeItem(Material.ANVIL, "&aGroup Name", "§7Current Name: §f" + name));
		getInventory().setItem(22, makeItem(Material.ANVIL, "&aGroup Weight", "§7Current Weight: §f" + weight));
		getInventory().setItem(30, makeItem(Material.GREEN_STAINED_GLASS_PANE, "&aCreate Group", "§7Click to create group.", "Name: " + name, "Weight: " + weight));
	}

	private void reOpenMenu(Player player) {
		new GroupCreateMenu(plugin.getMenuUtil(player, menuUtil.getGroup())).open();
	}

	private boolean doesGroupAlreadyExist(String name) {
		// TODO: Support MYSQL
		if (plugin.getConfigManager().getJsonDatabase().get("groups." + name.toLowerCase()) != null) {
			return true;
		}
		return false;
	}
}

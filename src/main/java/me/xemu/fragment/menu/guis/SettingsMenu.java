package me.xemu.fragment.menu.guis;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.MySqlDatabase;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.utils.Interaction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static me.xemu.fragment.utils.Utils.deformat;

public class SettingsMenu extends Paged {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();

	public SettingsMenu(MenuUtil menuUtil) {
		super(menuUtil);
	}

	@Override
	public String getMenuName() {
		return "Fragment > Settings:";
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
		} else if (displayname.equalsIgnoreCase("Database Integration")) {
			player.closeInventory();
			Interaction interaction = new Interaction(plugin);
			interaction.startInteraction(player, "Database System - JSON or MySQL?", this, FragmentPlugin.getMenuUtil(player, menuUtil.getGroup())).thenAccept(received -> {
				if (received.equalsIgnoreCase("JSON")) {
					plugin.getConfigManager().getConfig().set("database.integration", "JSON");
					plugin.getConfigManager().getConfig().write();
					new SettingsMenu(FragmentPlugin.getMenuUtil(player)).open();
				} else if (received.equalsIgnoreCase("MySQL")) {
					plugin.getConfigManager().getConfig().set("database.integration", "MySQL");
					plugin.getConfigManager().getConfig().write();
					new SettingsMenu(FragmentPlugin.getMenuUtil(player)).open();

					if (plugin.getFragmentDatabase() instanceof MySqlDatabase) {
						player.sendMessage(ChatColor.GOLD + "Fragment Warning: You just updated your integration to MySQL. Remember to edit your login settings to MySQL in config, or the plugin will crash.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Invalid response type: " + received);
				}
			});

			//plugin.loadDatabase();
		} else if (displayname.equalsIgnoreCase("MySQL Specific")) {
			boolean sqlEnabled = plugin.getConfig().getString("database.integration").equalsIgnoreCase("MySQL") ? true : false;
			if (sqlEnabled) {

			} else {
				e.setCancelled(true);

			}
		}
	}

	@Override
	public void setMenuItems() {
		applyLayout(false);

		getInventory().setItem(21, makeItem(Material.PAPER, "&aDatabase Integration", "§7Integration: §a" + plugin.getFragmentDatabase().getIdentifier()));
		getInventory().setItem(22, makeItem(Material.PAPER, "&a&lMySQL Specific", "§a" + "Requires MySQL Integration."));
		getInventory().setItem(30, makeItem(Material.PAPER, "&aWebhook", "§aSetup the webhook"));
	}
}
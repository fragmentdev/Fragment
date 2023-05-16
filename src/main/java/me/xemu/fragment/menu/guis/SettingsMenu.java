package me.xemu.fragment.menu.guis;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.MySqlDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.utils.Receiver;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
<<<<<<< Updated upstream
			Receiver receiver = new Receiver(plugin);
			receiver.startReceiver(player, "Database System - JSON or MySQL?").thenAccept(received -> {
=======
			Interaction interaction = new Interaction(plugin);
			interaction.startInteraction(player, "Database System - JSON or MySQL?").thenAccept(received -> {
>>>>>>> Stashed changes
				if (received.equalsIgnoreCase("JSON")) {
					plugin.getConfigManager().getConfig().set("database.integration", "JSON");
					plugin.getConfigManager().getConfig().write();
					super.open();
				} else if (received.equalsIgnoreCase("MySQL")) {
					plugin.getConfigManager().getConfig().set("database.integration", "MySQL");
					plugin.getConfigManager().getConfig().write();
					super.open();

					if (plugin.getFragmentDatabase() instanceof MySqlDatabase) {
						player.sendMessage(ChatColor.GOLD + "Fragment Warning: You just updated your integration to MySQL. Remember to edit your login settings to MySQL in config, or the plugin will crash.");
					}
				} else {
					player.sendMessage(ChatColor.RED + "Invalid response type: " + received);
				}
			});

			//plugin.loadDatabase();
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
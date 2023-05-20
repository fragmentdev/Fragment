package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.discord.DiscordWebhook;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.menu.guis.MainMenu;
import me.xemu.fragment.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;

public class FragmentCommand implements CommandExecutor {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;
		String version = plugin.getDescription().getVersion();

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendHelpMessage(player, "Invalid permission.");
		}

		if (args.length == 0) {
			new MainMenu(FragmentPlugin.getMenuUtil(player)).open();
			//player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			//player.sendMessage(Utils.translate("&aFragment v" + version + "&7 by &bXemu&7."));
			//player.sendMessage(Utils.translate("&7Advanced Permission Framework"));
			//player.sendMessage(Utils.translate("&7> &b/fragment reload - Reload the plugin.."));
			//player.sendMessage(Utils.translate("&7> &b/fragment help - View all help."));
			//player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
			if (player.hasPermission("fragment.admin")) {
				player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
				player.sendMessage(Utils.translate("&aFragment v" + version + "&7 by &bXemu & DevScape&7."));
				player.sendMessage(Utils.translate("&7Advanced Permission Framework"));
				player.sendMessage(Utils.translate("&7> &b/fragment reload - Reload the plugin."));
				player.sendMessage(Utils.translate("&7> &b/group create <Name> <Weight> - Create a group."));
				player.sendMessage(Utils.translate("&7> &b/group edit <Name> <Key> <Value> - Edit a group."));
				player.sendMessage(Utils.translate("&7> &b/group delete <Name> - Delete a group."));
				player.sendMessage(Utils.translate("&7> &b/group permission <Add/Remove> <Name> <Permission> - Add a permission."));
				player.sendMessage(Utils.translate("&7> &b/grant <User> <Group> - Add a group to a player."));
				player.sendMessage(Utils.translate("&7> &b/removegrant <User> <Group> - Remove a group to a player."));
				player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
				return true;
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			if (player.hasPermission("fragment.admin")) {
				Utils.sendSuccess(player, "Reloaded all Fragment Files!");

				plugin.getConfigManager().getConfig().forceReload();
				plugin.getConfigManager().getMessages().forceReload();
				plugin.getConfigManager().getDatabase().forceReload();

				return true;
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("webhooktest")) {
			if (FragmentPlugin.WEBHOOK_URL.isEmpty() || FragmentPlugin.WEBHOOK_URL.contains("PLACE-HERE")) {
				Utils.sendError(player, "Could not test webhook. Webhook URL is not set.");
				return true;
			}
			DiscordWebhook webhook = new DiscordWebhook(FragmentPlugin.WEBHOOK_URL);
			webhook.setContent("Webhook is stable!");
			try {
				webhook.execute();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
			Utils.sendSuccess(player, "Dispatched webhook test to the Discord Channel.");
		}

		return true;
	}
}

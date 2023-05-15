package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.discord.DiscordWebhook;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class FragmentCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;
		String version = FragmentPlugin.getFragmentPlugin().getDescription().getVersion();

		if (!player.hasPermission("fragment.admin")) {
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			player.sendMessage(Utils.translate("&aFragment v" + version + "&7 by &bXemu&7."));
			player.sendMessage(Utils.translate("&7Advanced Permission Framework"));
			player.sendMessage(Utils.translate("&cYou do not have the required admin permission to view help."));
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			player.sendMessage(Utils.translate("&aFragment v" + version + "&7 by &bXemu&7."));
			player.sendMessage(Utils.translate("&7Advanced Permission Framework"));
			player.sendMessage(Utils.translate("&7> &b/fragment reload - Reload the plugin.."));
			player.sendMessage(Utils.translate("&7> &b/fragment help - View all help."));
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			return true;
		} else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
			if (player.hasPermission("fragment.admin")) {
				player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
				player.sendMessage(Utils.translate("&aFragment v" + version + "&7 by &bXemu&7."));
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

				FragmentPlugin.getFragmentPlugin().getConfigManager().getConfig().forceReload();
				FragmentPlugin.getFragmentPlugin().getConfigManager().getMessages().forceReload();
				FragmentPlugin.getFragmentPlugin().getConfigManager().getDatabase().forceReload();

				return true;
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("webhooktest")) {
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

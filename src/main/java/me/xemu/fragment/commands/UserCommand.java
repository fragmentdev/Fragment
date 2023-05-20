package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.commands.subcommands.*;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class UserCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		String version = FragmentPlugin.getFragment().getDescription().getVersion();

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, Language.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			player.sendMessage(Utils.translate("&aFragment v" + version + "&7 by &bXemu & DevScape&7."));
			player.sendMessage(Utils.translate("&7Advanced Permission Framework"));
			player.sendMessage(Utils.translate("&7> &b/user <Player> group add <Group> - add a group from the user."));
			player.sendMessage(Utils.translate("&7> &b/user <Player> group remove <Group> - remove a group from the user."));
			player.sendMessage(Utils.translate("&7> &b/user <Player> permission add <Permission> - add a permission from the user."));
			player.sendMessage(Utils.translate("&7> &b/user <Player> permission remove <Permission> - remove a permission from the user."));
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
		}

		if (args.length >= 1) {
			String subCommand = args[1].toLowerCase();
			String target = args[0];

			if (Bukkit.getPlayer(target) == null) {
				Utils.sendSuccess(player, Language.INVALID_TARGET);
				return true;
			}

			// user xemuu[0] group[1] add[2] Owner[3]
			// user xemuu[0] permission[1] add[2] owner.perms[3]

			switch (subCommand) {
				case "permission":
					if (args.length == 4) {
						new UserPermissionCommand().execute(player, args[0], args[2], args[3]);
					} else {
						sendInvalidUsage(player);
					}
					break;
				case "group":
					if (args.length == 4) {
						new UserGroupCommand().execute(player, args[0], args[2], args[3]);
					} else {
						sendInvalidUsage(player);
					}
					break;
				default:
					sendInvalidUsage(player);
					break;
			}
		} else {
			sendInvalidUsage(player);
		}

		return true;
	}

	private void sendInvalidUsage(Player player) {
		Utils.sendError(player, Language.INVALID_USAGE);
	}

}

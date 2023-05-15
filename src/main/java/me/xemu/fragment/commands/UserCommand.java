package me.xemu.fragment.commands;

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

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, Language.NO_PERMISSION);
			return true;
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

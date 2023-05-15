package me.xemu.fragment.commands;

import me.xemu.fragment.commands.subcommands.GroupCreateCommand;
import me.xemu.fragment.commands.subcommands.GroupDeleteCommand;
import me.xemu.fragment.commands.subcommands.GroupEditCommand;
import me.xemu.fragment.commands.subcommands.GroupPermissionCommand;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GroupCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, Language.NO_PERMISSION);
			return true;
		}

		if (args.length >= 1) {
			String subCommand = args[0].toLowerCase();

			switch (subCommand) {
				case "create":
					if (args.length == 3) {
						new GroupCreateCommand().execute(player, args[1], Integer.parseInt(args[2]));
					} else {
						sendInvalidUsage(player);
					}
					break;
				case "edit":
					if (args.length >= 4) {
						new GroupEditCommand().execute(player, args[1], args[2], args);
					} else {
						sendInvalidUsage(player);
					}

					break;
				case "delete":
					if (args.length == 2) {
						new GroupDeleteCommand().execute(player, args[1]);
					} else {
						sendInvalidUsage(player);
					}
					break;
				case "permission":
					if (args.length == 4) {
						if (args[2].equals("add")) {
							new GroupPermissionCommand().execute(player, args[1], args[2], args[3]);
						} else if (args[2].equals("remove")) {
							new GroupPermissionCommand().execute(player, args[1], args[2], args[3]);
						}
					}
					break;
				default:
					sendInvalidUsage(player);
					break;
			}
		}

		return true;
	}

	private void sendInvalidUsage(Player player) {
		Utils.sendError(player, Language.INVALID_USAGE);
	}

}

package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.commands.subcommands.*;
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

		String version = FragmentPlugin.getFragment().getDescription().getVersion();

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, Language.NO_PERMISSION);
			return true;
		}

		if (args.length == 0) {
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			player.sendMessage(Utils.translate("&aFragment v" + version + "&7 by &bXemu & DevScape&7."));
			player.sendMessage(Utils.translate("&7Advanced Permission Framework"));
			player.sendMessage(Utils.translate("&7> &b/group create <Name> <Weight> - Create a group."));
			player.sendMessage(Utils.translate("&7> &b/group edit <Name> <Key> <Value> - Edit a group."));
			player.sendMessage(Utils.translate("&7> &b/group delete <Name> - Delete a group."));
			player.sendMessage(Utils.translate("&7> &b/group permission <Add/Remove> <Name> <Permission> - Add a permission."));
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
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
				case "permission": // group permission add Moderator litebans.ban
					if (args.length == 4) {
						if (args[1].equals("add")) {
							new GroupPermissionCommand().execute(player, args[2], args[1], args[3]);
						} else if (args[1].equals("remove")) {
							new GroupPermissionCommand().execute(player, args[2], args[1], args[3]);
						} else {
							sendInvalidUsage(player);
						}
					}
					break;
				case "self":
					new GroupSelf().execute(player);
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

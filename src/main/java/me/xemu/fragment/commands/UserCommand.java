package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.manager.GroupManager;
import me.xemu.fragment.manager.UserManager;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class UserCommand implements CommandExecutor {
	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getDatabase();
	private HashMap<String, FragmentSubCommand> subCommands = new HashMap<>();
	private UserManager userManager;

	private FragmentSubCommand mainCommand;

	public UserCommand() {
		subCommands.put("gui", new FragmentGuiCommand());

		this.mainCommand = new FragmentGuiCommand();
		this.userManager = new UserManager();
	}


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, "No permission.");
			return true;
		}


		if (args.length == 0) {
			player.sendMessage(ChatColor.RED + "/user <user> group <add/remove> <group>");
			return true;
		} else if (args.length == 4) {
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				player.sendMessage(ChatColor.RED + "Invalid target. /user <user> group <add/remove> <group>");
				return true;
			}

			String action = args[2].toLowerCase();
			String groupName = args[3].toLowerCase();

			Group group = null;
			try {
				group = GroupManager.load(groupName);
			} catch (Exception e) {
				player.sendMessage(ChatColor.RED + "Invalid group. /user <user> group <add/remove> <group>");
				return true;
			}

			if (group == null) {
				player.sendMessage(ChatColor.RED + "Invalid group. /user <user> group <add/remove> <group>");
				return true;
			}

			if (action.equalsIgnoreCase("add")) {
				try {
					userManager.addGroupToUser(UserManager.load(target), group, true);
					player.sendMessage(ChatColor.GREEN + "You granted " + group.getName() + " to " + target.getName());
				} catch (Exception e) {
					e.printStackTrace();
					player.sendMessage(ChatColor.RED + "Could not grant group.");
				}
			} else if (action.equalsIgnoreCase("remove")) {
				try {
					userManager.removeGroupFromUser(UserManager.load(target), group, true);
					player.sendMessage(ChatColor.GREEN + "You removed " + group.getName() + " from " + target.getName());
				} catch (Exception e) {
					player.sendMessage(ChatColor.RED + "Could not grant group.");
				}
			} else {
				player.sendMessage(ChatColor.RED + "Invalid usage. /user <user> group <add/remove> <group>");
			}
;		}

		return true;
	}
}

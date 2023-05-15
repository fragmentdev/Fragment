package me.xemu.fragment.tabcompleter;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class UserTabComplete implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();
		// user xemuu[0] group[1] add[2] Owner[3]
		// user xemuu[0] permission[1] add[2] owner.perms[3]
		if (args.length == 1) {
			completions.addAll(FragmentPlugin.getFragmentPlugin().getServer().getOnlinePlayers().stream().map(m -> m.getName()).toList());
		} else if (args.length == 2) {
			completions.add("permission");
			completions.add("group");
			// Add other subcommands here
		} else if (args.length == 3 && args[1].equalsIgnoreCase("group")) {
			completions.add("add");
			completions.add("remove");
		} else if (args.length == 4 && args[1].equalsIgnoreCase("permission")) {
			completions.add("add");
			completions.add("remove");
		} else if (args.length == 4 && args[1].equalsIgnoreCase("permission")) {
			completions.addAll(getAllPermissions());
		} else if (args.length == 4 && args[1].equalsIgnoreCase("group")) {
			completions.addAll(FragmentPlugin.getFragmentPlugin().getFragmentDatabase().getGroups().stream().map(Group::getName).toList());
		} else if (args.length == 4 && args[0].equalsIgnoreCase("permission")) {
			completions.addAll(getAllPermissions());
		}


		// Optionally, you can include online player names as completions
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 3 && args[0].equalsIgnoreCase("edit")) {
				// Add player names as completions
				for (Player onlinePlayer : player.getServer().getOnlinePlayers()) {
					//completions.add(onlinePlayer.getName());
				}
			}
		}

		// Filter completions based on the current argument being typed
		if (args.length > 0) {
			String currentArg = args[args.length - 1].toLowerCase();
			completions.removeIf(option -> !option.toLowerCase().startsWith(currentArg));
		}

		return completions;
	}

	public static List<String> getAllPermissions() {
		List<String> permissions = new ArrayList<>();

		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			for (Permission permission : plugin.getDescription().getPermissions()) {
				if (permission.getChildren().isEmpty()) {
					permissions.add(permission.getName());
				} else {
					for (String childPermission : permission.getChildren().keySet()) {
						permissions.add(childPermission);
					}
				}
			}
		}

		return permissions;
	}

}

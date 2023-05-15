package me.xemu.fragment.tabcompleter;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import org.bukkit.command.TabCompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GroupTabComplete implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();

		if (args.length == 1) {
			completions.add("create");
			completions.add("edit");
			completions.add("delete");
			completions.add("permission");
			// Add other subcommands here
		} else if (args.length == 2 && args[0].equalsIgnoreCase("edit")) {
			completions.addAll(FragmentPlugin.getFragmentPlugin().getFragmentDatabase().getGroups().stream().map(Group::getName).toList());
		} else if (args.length == 2 && args[0].equalsIgnoreCase("info")) {
			completions.addAll(FragmentPlugin.getFragmentPlugin().getFragmentDatabase().getGroups().stream().map(Group::getName).toList());
		} else if (args.length == 2 && args[0].equalsIgnoreCase("permission")) {
			completions.add("add");
			completions.add("remove");
		} else if (args.length == 2 && args[0].equalsIgnoreCase("removeperm")) {
			completions.addAll(FragmentPlugin.getFragmentPlugin().getFragmentDatabase().getGroups().stream().map(Group::getName).toList());
		} else if (args.length == 3 && args[0].equalsIgnoreCase("edit")) {
			completions.add("prefix");
			completions.add("suffix");
			completions.add("weight");
			completions.add("format");
			// Add other edit options here
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
}

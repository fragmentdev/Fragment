package me.xemu.fragment.tabcomplete;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.manager.GroupManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UserTabCompleter implements TabCompleter {

	@Nullable
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (args.length == 1) {
			return completeUsernames(args[0]);
		} else if (args.length == 2 && args[1].equalsIgnoreCase("group")) {
			return completeGroupAction(args[1]);
		} else if (args.length == 3 && (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("remove"))) {
			return completeGroupNames(args[2]);
		}
		return null;
	}

	private List<String> completeUsernames(String partial) {
		List<String> completions = new ArrayList<>();
		for (Player player : Bukkit.getOnlinePlayers()) {
			String playerName = player.getName();
			if (playerName.toLowerCase().startsWith(partial.toLowerCase())) {
				completions.add(playerName);
			}
		}
		return completions;
	}

	private List<String> completeGroupAction(String partial) {
		List<String> completions = new ArrayList<>();
		if ("add".startsWith(partial.toLowerCase())) {
			completions.add("add");
		}
		if ("remove".startsWith(partial.toLowerCase())) {
			completions.add("remove");
		}
		return completions;
	}

	private List<String> completeGroupNames(String partial) {
		List<String> completions = new ArrayList<>();
		List<String> groupNames = FragmentPlugin.getFragment().getDatabase().loadGroups().stream().map(m -> m.getName()).toList();
		for (String groupName : groupNames) {
			if (groupName.toLowerCase().startsWith(partial.toLowerCase())) {
				completions.add(groupName);
			}
		}
		return completions;
	}
}

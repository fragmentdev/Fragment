package me.xemu.fragment.tabcompleter;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GrantTabComplete implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
		List<String> completions = new ArrayList<>();

		if (args.length == 1) {
			completions.addAll(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
		} else if (args.length == 2) {
			completions.addAll(FragmentPlugin.getFragmentPlugin().getFragmentDatabase().getGroups().stream().map(Group::getName).toList());
		}

		// Filter completions based on the current argument being typed
		if (args.length > 0) {
			String currentArg = args[args.length - 1].toLowerCase();
			completions.removeIf(option -> !option.toLowerCase().startsWith(currentArg));
		}

		return completions;
	}
}

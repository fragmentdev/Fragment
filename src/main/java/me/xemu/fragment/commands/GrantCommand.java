package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.Message;
import me.xemu.fragment.Utils;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GrantCommand implements CommandExecutor {

	private FragmentPlugin plugin = FragmentPlugin.getFragmentPlugin();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			new Message("&cCould not execute Fragment Command.").colorize().console(true);
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("fragment.grant")) {
			sendHelpMessage(player);
			return true;
		}

		if (args.length != 2) {
			Utils.sendError(player, "Invalid command usage.");
			return true;
		}

		String playerName = args[0];
		String groupName = args[1];

		Player target = Bukkit.getPlayer(playerName);

		if (target == null || !target.isOnline()) {
			Utils.sendError(player, "Invalid target.");
			return true;
		}

		Group group = plugin.getFragmentDatabase().loadGroup(groupName);

		if (group == null) {
			Utils.sendError(player, "Invalid group.");
			return true;
		}

		User user = plugin.getFragmentDatabase().loadUser(target);
		user.getGroups().add(group);
		plugin.getFragmentDatabase().saveUser(user);

		Map<String, String> successMessageData = new HashMap<>();
		successMessageData.put("Target", target.getName());
		successMessageData.put("Group", group.getName());

		Utils.sendSuccess(player, "You granted a group.", successMessageData);

		return true;
	}

	private void sendHelpMessage(Player player) {
		if (player.hasPermission("fragment.grant")) {
			new Message("&8&m------------------------------").colorize().send(player);
			new Message("&7>> &f/grant <Player> <Group> &e&o(Grant a group name)").colorize().send(player);
			new Message("&8&m------------------------------").colorize().send(player);
		} else {
			new Message("&cYou are missing the required 'fragment.grant' command.").colorize().send(player);
		}
	}

	private boolean isValidKey(String key) {
		String[] supportedKeys = {"prefix", "suffix", "weight", "format"};
		for (String supportedKey : supportedKeys) {
			if (supportedKey.equalsIgnoreCase(key)) {
				return true;
			}
		}
		return false;
	}

	private String joinArguments(String[] args, int startIndex) {
		StringBuilder sb = new StringBuilder();
		for (int i = startIndex; i < args.length; i++) {
			sb.append(args[i]);
			if (i < args.length - 1) {
				sb.append(" ");
			}
		}
		return sb.toString();
	}

	private void performEdit(Group group, String key, String value) {
		switch(key) {
			case "weight":
				group.setWeight(Integer.parseInt(value));
				plugin.getFragmentDatabase().saveGroup(group);
				break;
			case "prefix":
				group.setPrefix(value);
				plugin.getFragmentDatabase().saveGroup(group);
				break;
			case "suffix":
				group.setSuffix(value);
				plugin.getFragmentDatabase().saveGroup(group);
				break;
			case "format":
				group.setFormat(value);
				plugin.getFragmentDatabase().saveGroup(group);
				break;
		}
	}
}

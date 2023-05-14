package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.utils.Message;
import me.xemu.fragment.utils.Utils;
import me.xemu.fragment.entity.Group;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupCommand implements CommandExecutor {

	private FragmentPlugin plugin = FragmentPlugin.getFragmentPlugin();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			new Message("&cCould not execute Fragment Command.").colorize().console(true);
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("fragment.admin")) {
			sendHelpMessage(player);
			return true;
		}

		if (args.length == 0) {
			sendHelpMessage(player);
			return true;
		} else if (args.length > 1) {
			if (args[0].equalsIgnoreCase("create")) {
				String groupName = args[1];
				String weight = args[2];

				if (args.length != 3) {
					Utils.sendError(player, "Invalid usage.");
					return true;
				}

				if (groupName == null || groupName == "") {
					Utils.sendError(player, "&cInvalid argument 'groupName' as it's blank.");
					return true;
				} else if (!Utils.isNumber(weight)) {
					Utils.sendError(player, "&cInvalid argument 'weight' as the value is not a integer.");
					return true;
				} else if (weight == "" || weight == null) {
					Utils.sendError(player, "&cInvalid argument 'weight' as it's blank.");
					return true;
				}

				Group group = new Group(groupName, Integer.parseInt(weight), null, null, null, null);

				if (plugin.getFragmentDatabase().loadGroup(groupName) != null) {
					Utils.sendError(player, "&cThis group already exists.");
					return true;
				}

				plugin.getFragmentDatabase().saveGroup(group);



				new Message("&8&m--------------------------").colorize().send(player);
				new Message("&a&l✓ &7You created a group.").colorize().send(player);
				new Message("&7Name: &f" + group.getName()).colorize().send(player);
				new Message("§7Prefix: §r" + group.getPrefix()).send(player);
				new Message("§7Suffix: §r" + group.getSuffix()).send(player);
				new Message("§7Format: §r" + group.getFormat()).send(player);
				new Message("&8&m--------------------------").colorize().send(player);

				plugin.getDiscordManager().sendCreateGroupLog(player.getName(), group.getName(), String.valueOf(group.getWeight()));

				return true;
			} else if (args[0].equalsIgnoreCase("edit")) {
				if (args.length < 3) {
					new Message("&cInsufficient arguments for 'edit' command.").colorize().send(player);
					return true;
				}

				String groupName = args[1];
				String key = args[2];
				String value = joinArguments(args, 3);

				Group group = plugin.getFragmentDatabase().loadGroup(groupName);

				if (group == null) {
					new Message("&cInvalid group: " + groupName).colorize().send(player);
					return true;
				}

				if (!isValidKey(key)) {
					new Message("&cInvalid key. Supported keys: prefix, suffix, weight, format").colorize().send(player);
					return true;
				}

				if (value.equals("")) {
					new Message("Invalid value.").colorize().send(player);
					return true;
				}

				performEdit(group, key, value);

				new Message("&8&m--------------------------").colorize().send(player);
				new Message("&a&l✓ &7You edited a group.").colorize().send(player);
				new Message("&7Name: &f" + group.getName()).colorize().send(player);
				new Message("§7Key: §r" + key).send(player);
				new Message("§7Value: §r" + value).send(player);
				new Message("&8&m--------------------------").colorize().send(player);

				plugin.getDiscordManager().editGroupLog(player.getName(), group.getName(), key, value);

				return true;
			} else if (args[0].equalsIgnoreCase("info")) {
				String groupName = args[1];
				Group group = plugin.getFragmentDatabase().loadGroup(groupName);

				if (group == null) {
					Utils.sendError(player, "Group doesn't exist.");
					return true;
				}

				new Message("&8&m--------------------------").colorize().send(player);
				new Message("&a&l✓ &7Group Information").colorize().send(player);
				new Message("&7Name: &f" + group.getName()).colorize().send(player);
				new Message("§7Prefix: §r" + group.getPrefix()).send(player);
				new Message("§7Suffix: §r" + group.getSuffix()).send(player);
				new Message("§7Format: §r" + group.getFormat()).send(player);
				new Message("§7Weight: §r" + group.getWeight()).send(player);
				new Message("&8&m--------------------------").colorize().send(player);
			} else if (args[0].equalsIgnoreCase("delete")) {
				String groupName = args[1];
				Group group = plugin.getFragmentDatabase().loadGroup(groupName);

				if (group == null) {
					Utils.sendError(player, "Group doesn't exist.");
					return true;
				}

 				plugin.getFragmentDatabase().getUsers().forEach(user -> {
					user.getGroups().remove(groupName);
				});
				plugin.getConfigManager().getDatabase().removeAll("groups." + groupName);

				Map<String, String> successMessageData = new HashMap<>();
				successMessageData.put("Group", group.getName());

				Utils.sendSuccess(player, "You deleted a group.", successMessageData);
			} else if (args[0].equalsIgnoreCase("addperm")) {
				String groupName = args[1];
				Group group = plugin.getFragmentDatabase().loadGroup(groupName);

				if (group == null) {
					Utils.sendError(player, "Group doesn't exist.");
					return true;
				}

				if (group.getPermissions().contains(args[2])) {
					Utils.sendError(player, "Group already has this permission.");
					return true;
				}

				group.getPermissions().add(args[2]);
				plugin.getFragmentDatabase().saveGroup(group);

				Map<String, String> data = new HashMap<>();
				data.put("Group", group.getName());
				data.put("Permission", args[2]);

				Utils.sendSuccess(player, "You granted a permission to a group.", data);

				plugin.getDiscordManager().editGroupLog(player.getName(), group.getName(), "add-permission", args[2]);

				return true;
			} else if (args[0].equalsIgnoreCase("removeperm")) {
				String groupName = args[1];
				Group group = plugin.getFragmentDatabase().loadGroup(groupName);

				if (group == null) {
					Utils.sendError(player, "Group doesn't exist.");
					return true;
				}

				if (!group.getPermissions().contains(args[2])) {
					Utils.sendError(player, "Group doesn't have that permission.");
					return true;
				}

				group.getPermissions().remove(args[2]);
				plugin.getFragmentDatabase().saveGroup(group);

				Map<String, String> data = new HashMap<>();
				data.put("Group", group.getName());
				data.put("Permission", args[2]);

				Utils.sendSuccess(player, "You removed a grant from a permission to a group.", data);

				plugin.getDiscordManager().editGroupLog(player.getName(), group.getName(), "remove-permission", args[2]);

			} else {
				Utils.sendError(player, "Invalid usage: /group");
			}
		} else if (args[0].equalsIgnoreCase("self")) {
			Group heaviestGroup = Utils.getHeaviestGroup(plugin.getFragmentDatabase().loadUser(player.getUniqueId()).getGroups());
			List<Group> otherGroups = new ArrayList<>();

			new Message("&8&m------------------------------").colorize().send(player);
			new Message("&7>> <heaviest> &e(Heaviest Group)").colorize()
					.placeholder("<heaviest>", heaviestGroup.getName())
					.send(player);

			for (Group group : plugin.getFragmentDatabase().loadUser(player.getUniqueId()).getGroups()) {
				if (!group.equals(heaviestGroup)) {
					otherGroups.add(group);
				}
			}

			if (!otherGroups.isEmpty()) {
				new Message("&7>> Other Groups:").colorize().send(player);
				for (Group otherGroup : otherGroups) {
					new Message("   <group>").placeholder("<group>", otherGroup.getName()).colorize().send(player);
				}
			} else {
				// If there are no other groups, display a message indicating so
				new Message("&7>> No other groups found.").colorize().send(player);
			}

			new Message("&8&m------------------------------").colorize().send(player);
			return true;

		} else {
			Utils.sendError(player, "Invalid usage: /group");
		}

		return true;
	}

	private void sendHelpMessage(Player player) {
		if (player.hasPermission("fragment.admin")) {
			new Message("&8&m------------------------------").colorize().send(player);
			new Message("&7>> &f/group create <Group-Name> <Weight> &e&o(Create a new group)").colorize().send(player);
			new Message("&7>> &f/group edit <Group-Name> <Key> <Value> &e&o(Edit a group)").colorize().send(player);
			new Message("&7>> &f/group delete <Group-Name> &e&o(Delete a group, DANGER ZONE)").colorize().send(player);
			new Message("&7>> &f/group info <Group-Name> &e&o(View group information)").colorize().send(player);
			new Message("&7>> &f/group self &e&o(View your own groups)").colorize().send(player);
			new Message("&8&m------------------------------").colorize().send(player);
		} else {
			new Message("&cYou are missing the required 'fragment.admin' command.").colorize().send(player);
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

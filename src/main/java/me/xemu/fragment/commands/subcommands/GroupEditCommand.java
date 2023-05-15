package me.xemu.fragment.commands.subcommands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class GroupEditCommand {

	private FragmentPlugin plugin = FragmentPlugin.getFragmentPlugin();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	public void execute(Player player, String groupName, String key, String[] args) {
		if (database.loadGroup(groupName) == null) {
			Utils.sendError(player, Language.GROUP_DOESNT_EXIST);
			return;
		}

		if (!isValidKey(key)) {
			Utils.sendError(player, Language.GROUP_EDITED_INVALID_KEY);
			return;
		}

		String value = joinArguments(args, 3);

		Group group = database.loadGroup(groupName);

		performEdit(group, key, value);

		Utils.sendSuccess(player, Language.GROUP_EDITED.replaceAll("<group>".replaceAll("<key>", key).replaceAll("<value>", value), group.getName()));

		plugin.getDiscordManager().editGroupLog(player.getName(), group.getName(), key, value);
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

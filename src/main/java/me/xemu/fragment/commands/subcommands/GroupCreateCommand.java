package me.xemu.fragment.commands.subcommands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class GroupCreateCommand {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	public void execute(Player player, String groupName, int weight) {
		if (database.loadGroup(groupName) != null) {
			Utils.sendError(player, Language.GROUP_ALREADY_EXISTS);
			return;
		}

		Group group = new Group(groupName, weight, null, null, null, null);
		database.saveGroup(group);

		Map<String, String> strings = new HashMap<>();
		strings.put("Group", group.getName());
		strings.put("Weight", String.valueOf(group.getWeight()));

		String message = Language.GROUP_CREATED.replaceAll("<group>", group.getName());
		Utils.sendSuccess(player, message, strings);

		plugin.getDiscordManager().sendCreateGroupLog(player.getName(), group.getName(), String.valueOf(group.getWeight()));
	}

}

package me.xemu.fragment.commands.subcommands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.handler.GroupHandler;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupCreateCommand {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private FragmentDatabase database = plugin.getFragmentDatabase();
	private GroupHandler groupHandler = plugin.getGroupHandler();

	public void execute(Player player, String groupName, int weight) {
		if (groupHandler.getGroupByName(groupName) != null) {
			Utils.sendError(player, Language.GROUP_ALREADY_EXISTS);
			return;
		}

		// Prevent a error throwing
		if (weight >= Integer.MAX_VALUE) {
			Utils.sendError(player, "Group weight increases the max integer value supported by Java. Make it under " + Integer.MAX_VALUE);
			return;
		}

		Group group = new Group(groupName, weight, null, null, null, null);

		try {
			List<Group> groupsWithSameWeight = database.getGroups().stream().filter(g -> g.getWeight() == weight).toList();
			if (!groupsWithSameWeight.isEmpty()) {
				Utils.sendError(player, "You cannot have two groups with the same weight.");
				return;
			}
		}  catch (NullPointerException nullPointerException) {
			Utils.sendError(player, Language.AN_ERROR_OCCURRED);
			return;
		}

		database.saveGroup(group);

		Map<String, String> strings = new HashMap<>();
		strings.put("Group", group.getName());
		strings.put("Weight", String.valueOf(group.getWeight()));

		String message = Language.GROUP_CREATED.replaceAll("<group>", group.getName());
		Utils.sendSuccess(player, message, strings);

		plugin.getDiscordManager().sendCreateGroupLog(player.getName(), group.getName(), String.valueOf(group.getWeight()));
	}

}

package me.xemu.fragment.commands.subcommands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.entity.Player;

public class GroupDeleteCommand {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	public void execute(Player player, String groupName) {
		if (database.loadGroup(groupName) == null) {
			Utils.sendError(player, Language.GROUP_DOESNT_EXIST);
			return;
		}

		plugin.getConfigManager().getDatabase().remove("groups." + groupName);

		String message = Language.GROUP_DELETED.replaceAll("<group>", groupName);
		Utils.sendSuccess(player, message);

		plugin.getDiscordManager().deleteGroupLog(player.getName(), groupName);
	}

}

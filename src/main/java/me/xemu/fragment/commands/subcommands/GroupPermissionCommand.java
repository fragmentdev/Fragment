package me.xemu.fragment.commands.subcommands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

// group permission <type> <permission>
public class GroupPermissionCommand {

	private FragmentPlugin plugin = FragmentPlugin.getFragmentPlugin();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	public void execute(Player player, String groupName, String type, String permission) {
		if (database.loadGroup(groupName) == null) {
			Utils.sendError(player, Language.GROUP_DOESNT_EXIST);
			return;
		}

		Group group = database.loadGroup(groupName);

		if (type.equalsIgnoreCase("add")) {
			group.getPermissions().add(permission);
			database.saveGroup(group);
			String message = Language.GROUP_ADD_PERMISSION
					.replaceAll("<permission>", permission)
					.replaceAll("<group>", group.getName());
			Utils.sendSuccess(player, message);
			plugin.getDiscordManager().editGroupLog(player.getName(), group.getName(), "group-permission-"+type, permission);
			return;
		} else if (type.equalsIgnoreCase("remove")) {
			group.getPermissions().remove(permission);
			database.saveGroup(group);
			String message = Language.GROUP_ADD_PERMISSION
					.replaceAll("<permission>", permission)
					.replaceAll("<group>", group.getName());
			Utils.sendSuccess(player, message);
			plugin.getDiscordManager().editGroupLog(player.getName(), group.getName(), "group-permission-"+type, permission);

			return;
		}
	}

}

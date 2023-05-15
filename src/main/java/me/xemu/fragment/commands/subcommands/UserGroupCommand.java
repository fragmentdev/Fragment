package me.xemu.fragment.commands.subcommands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Message;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// group group <add/remove> <player> <group>
public class UserGroupCommand {

	private FragmentPlugin plugin = FragmentPlugin.getFragmentPlugin();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	public void execute(Player player, String targetString, String type, String groupName) {
		User user = database.loadUser(player.getUniqueId());
		if (user == null) {
			player.sendMessage(Language.INVALID_USAGE);
			return;
		}

		Group group = database.loadGroup(groupName);
		if (group == null) {
			Utils.sendError(player, Language.GROUP_DOESNT_EXIST);
			return;
		}

		Player target = Bukkit.getPlayer(targetString);
		User targetUser = database.loadUser(target.getUniqueId());
		if (target == null) {
			Utils.sendError(player, Language.INVALID_TARGET);
			return;
		}

		if (type.equals("add")) {
			targetUser.getGroups().add(group);
			database.saveUser(targetUser);
			Utils.sendSuccess(player, Language.GROUP_GRANT_TO_PLAYER.replaceAll("<player>", target.getName()).replaceAll("<group>", group.getName()));
		} else if (type.equals("remove")) {
			targetUser.getGroups().remove(group);
			database.saveUser(targetUser);
			Utils.sendSuccess(player, Language.GROUP_GRANT_REMOVED_FROM_PLAYER.replaceAll("<player>", target.getName()).replaceAll("<group>", group.getName()));
		}
	}

}

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

// group permission <add/remove> <player> <group>
public class UserPermissionCommand {

	private FragmentPlugin plugin = FragmentPlugin.getFragmentPlugin();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	public void execute(Player player, String targetString, String type, String permission) {
		User user = database.loadUser(player.getUniqueId());
		if (user == null) {
			player.sendMessage(Language.INVALID_USAGE);
			return;
		}

		Player target = Bukkit.getPlayer(targetString);
		User targetUser = database.loadUser(target.getUniqueId());
		if (target == null) {
			Utils.sendError(player, Language.INVALID_TARGET);
			return;
		}

		if (type.equals("add")) {
			targetUser.getPlayerPermissions().add(permission);
			database.saveUser(targetUser);
			Utils.sendSuccess(player, Language.PLAYER_ADD_PERMISSION.replaceAll("<player>", target.getName()).replaceAll("<permission>", permission));
		} else if (type.equals("remove")) {
			targetUser.getPlayerPermissions().remove(permission);
			database.saveUser(targetUser);
			Utils.sendSuccess(player, Language.PLAYER_REMOVE_PERMISSION.replaceAll("<player>", target.getName()).replaceAll("<permission>", permission));
		}
	}

}

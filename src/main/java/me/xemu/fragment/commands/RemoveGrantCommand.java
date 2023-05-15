package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RemoveGrantCommand implements CommandExecutor {

	private FragmentPlugin plugin = FragmentPlugin.getFragmentPlugin();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, Language.NO_PERMISSION);
			return true;
		}

		// grant <user> <group>

		Player target = Bukkit.getPlayer(args[0]);
		String groupName = args[1];

		if (target == null) {
			Utils.sendError(player, Language.INVALID_TARGET);
			return true;
		}

		if (args.length != 2) {
			Utils.sendError(player, Language.INVALID_USAGE);
			return true;
		}

		User user = database.loadUser(target);

		if (!user.getGroups().contains(database.loadGroup(groupName))) {
			Utils.sendError(player, Language.GROUP_NOT_GRANTED);
			return true;
		}

		user.getGroups().remove(database.loadGroup(groupName));
		database.saveUser(user);

		Utils.sendSuccess(player, Language.GROUP_GRANT_REMOVED_FROM_PLAYER.replaceAll("<group>", groupName).replaceAll("<player>", target.getName()));

		return true;
	}

}


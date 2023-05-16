package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.menu.guis.grant.GrantsMenu;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GrantsCommand implements CommandExecutor {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		String version = FragmentPlugin.getInstance().getDescription().getVersion();

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, Language.NO_PERMISSION);
			return true;
		}

		// grant <user> <group>

		if (args.length == 0) {
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			player.sendMessage(Utils.translate("&aFragment v" + version + "&7 by &bXemu & DevScape&7."));
			player.sendMessage(Utils.translate("&7Advanced Permission Framework"));
			player.sendMessage(Utils.translate("&7> &b/grants <User> - View all grants to a player."));
			player.sendMessage(Utils.translate("&8&m--------------------------------------------------"));
			return true;
		}

		Player target = Bukkit.getPlayer(args[0]);

		if (target == null) {
			Utils.sendError(player, Language.INVALID_TARGET);
			return true;
		}

		if (args.length != 1) {
			Utils.sendError(player, Language.INVALID_USAGE);
			return true;
		}

		User user = database.loadUser(target);

		new GrantsMenu(FragmentPlugin.getMenuUtil(player), target).open();

		return true;
	}

}


package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.utils.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


public class FragmentCommand implements CommandExecutor {
	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getDatabase();
	private HashMap<String, FragmentSubCommand> subCommands = new HashMap<>();

	public FragmentCommand() {
		subCommands.put("reload", new FragmentReloadCommand());
		subCommands.put("gui", new FragmentGuiCommand());
	}


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, "No permission.");
			return true;
		}


		if (args.length >= 1) {
			String subCommand = args[0].toLowerCase();

			if (subCommands.containsKey(subCommand)) {
				if (subCommands.get(subCommand).getRequiredPermission() != "") {
					if (player.hasPermission(subCommands.get(subCommand).getRequiredPermission())) {
						subCommands.get(subCommand).execute(player);
					} else {
						Utils.sendError(player, "No permission for sub-command: " + subCommand);
					}
				}
			} else {
				Utils.sendError(player, "Invalid usage for Fragment: /fragment <reload/gui>");
				return true;
			}

		} else {
			Utils.sendError(player, "Invalid usage for Fragment: /fragment <reload>");
			return true;
		}

		return true;
	}
}

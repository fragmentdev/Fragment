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

	private FragmentSubCommand mainCommand;

	public FragmentCommand() {
		subCommands.put("gui", new FragmentGuiCommand());

		this.mainCommand = new FragmentGuiCommand();
	}


	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		Player player = (Player) sender;

		if (!player.hasPermission("fragment.admin")) {
			Utils.sendError(player, "No permission.");
			return true;
		}


		if (args.length == 0) {
			mainCommand.execute(player);
		}

		return true;
	}

	private String getInvalidUsageMessage() {
		return "Invalid usage for Fragment: /fragment <reload/gui>";
	}

}

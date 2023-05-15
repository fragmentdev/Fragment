package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.commands.subcommands.UserGroupCommand;
import me.xemu.fragment.commands.subcommands.UserPermissionCommand;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.menu.guis.MainMenu;
import me.xemu.fragment.utils.Receiver;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class TestCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return true;

		Player player = (Player) sender;

		Receiver receiver = new Receiver(FragmentPlugin.getFragmentPlugin());
		receiver.startReceiver(player, "Group Name").thenAccept(a -> {
			player.sendMessage("Creating group " + a);
		});

		return true;
	}

	private void sendInvalidUsage(Player player) {
		Utils.sendError(player, Language.INVALID_USAGE);
	}

}

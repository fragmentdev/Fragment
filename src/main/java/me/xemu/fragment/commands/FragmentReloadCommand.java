package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.manager.ConfigManager;
import me.xemu.fragment.utils.Utils;
import org.bukkit.entity.Player;

public class FragmentReloadCommand implements FragmentSubCommand {
	private ConfigManager configManager = FragmentPlugin.getFragment().getConfigManager();

	@Override
	public void execute(Player player) {
		configManager.getConfig().forceReload();
		configManager.getMessages().forceReload();
		Utils.sendSuccess(player, "Successfully reloaded configs!");
	}

	@Override
	public String getRequiredPermission() {
		return "fragment.admin";
	}
}

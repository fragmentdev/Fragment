package me.xemu.fragment.commands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.menu.menus.MainMenu;
import org.bukkit.entity.Player;

public class FragmentGuiCommand implements FragmentSubCommand {

	@Override
	public void execute(Player player) {
		new MainMenu(FragmentPlugin.getMenuUtil(player)).open();
	}

	@Override
	public String getRequiredPermission() {
		return "fragment.admin";
	}
}

package me.xemu.fragment.commands;

import org.bukkit.entity.Player;

public interface FragmentSubCommand {
	void execute(Player player);
	String getRequiredPermission();
}

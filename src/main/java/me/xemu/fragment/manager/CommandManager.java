package me.xemu.fragment.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.commands.FragmentCommand;

@Getter
@AllArgsConstructor
public class CommandManager {

	private FragmentPlugin plugin;

	public void registerCommands() {
		plugin.getCommand("fragment").setExecutor(new FragmentCommand());
	}

}

package me.xemu.fragment.manager;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.builder.ConsoleMessage;
import me.xemu.fragment.commands.FragmentCommand;
import me.xemu.fragment.commands.UserCommand;
import me.xemu.fragment.tabcomplete.UserTabCompleter;
import org.bukkit.command.PluginCommand;

@Getter
@AllArgsConstructor
public class CommandManager {

	private FragmentPlugin plugin;

	public void registerCommands() {
		new ConsoleMessage("Loading commands...").consoleWarning(true);
		plugin.getCommand("fragment").setExecutor(new FragmentCommand());

		PluginCommand command = plugin.getCommand("user");
		command.setExecutor(new UserCommand());
		command.setTabCompleter(new UserTabCompleter());
		new ConsoleMessage("Loaded all commands.").consoleSuccess(true);
	}

}

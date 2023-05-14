package me.xemu.fragment;

import lombok.Getter;
import me.xemu.fragment.commands.GrantCommand;
import me.xemu.fragment.commands.GroupCommand;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.database.JsonDatabase;
import me.xemu.fragment.listener.ChatListener;
import me.xemu.fragment.listener.JoinListener;
import me.xemu.fragment.manager.ConfigManager;
import me.xemu.fragment.tabcompleter.GrantTabComplete;
import me.xemu.fragment.tabcompleter.GroupTabComplete;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class FragmentPlugin extends JavaPlugin {

	private static FragmentPlugin fragmentPlugin;

	private ConfigManager configManager;

	private FragmentDatabase fragmentDatabase;

	@Override
	public void onEnable() {
		FragmentPlugin.fragmentPlugin = this;

		this.configManager = new ConfigManager();

		loadConfigurations();
		this.fragmentDatabase = new JsonDatabase();
		fragmentDatabase.load();
		setConstants();

		loadCommands();
		loadEvents();

	}

	@Override
	public void onDisable() {
		FragmentPlugin.fragmentPlugin = null;
	}

	public static FragmentPlugin getFragmentPlugin() {
		return fragmentPlugin;
	}

	private void loadConfigurations() {
		configManager.createConfigFile();
		configManager.createMessagesFile();
		configManager.createDatabaseFile();
	}

	private void loadCommands() {
		PluginCommand groupCommand = getCommand("group");
		groupCommand.setExecutor(new GroupCommand());
		groupCommand.setTabCompleter(new GroupTabComplete());

		PluginCommand grantCommand = getCommand("grant");
		grantCommand.setExecutor(new GrantCommand());
		grantCommand.setTabCompleter(new GrantTabComplete());
	}

	private void loadEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
	}

	public static String DEFAULT_FORMAT;

	public static String DEFAULT_GROUP;

	public static boolean FORCE_DEFAULT_GROUP;

	public void setConstants() {
		DEFAULT_FORMAT = getFragmentPlugin().getConfigManager()
				.getConfig()
				.getOrSetDefault("default-format", "{Player} &8» &r{Message}");
		DEFAULT_GROUP = getFragmentPlugin().getConfigManager()
				.getConfig()
				.getOrSetDefault("default-group", "Default");
		FORCE_DEFAULT_GROUP = getFragmentPlugin().getConfigManager()
				.getConfig()
				.getOrSetDefault("force-default-group", Boolean.TRUE);
	}
}

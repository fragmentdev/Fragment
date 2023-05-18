package me.xemu.fragment;

import lombok.Getter;
import me.xemu.fragment.commands.*;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.database.JsonDatabase;
import me.xemu.fragment.handler.GroupHandler;
import me.xemu.fragment.hooks.PapiHook;
import me.xemu.fragment.listener.ChatListener;
import me.xemu.fragment.listener.JoinListener;
import me.xemu.fragment.manager.ConfigManager;
import me.xemu.fragment.manager.DiscordManager;
import me.xemu.fragment.menu.MenuListener;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.tabcompleter.GrantTabComplete;
import me.xemu.fragment.tabcompleter.GroupTabComplete;
import me.xemu.fragment.tabcompleter.UserTabComplete;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@Getter
public class FragmentPlugin extends JavaPlugin {

	private static FragmentPlugin instance;

	private ConfigManager configManager;
	private DiscordManager discordManager;

	private FragmentDatabase fragmentDatabase;

	private GroupHandler groupHandler;

	public static String DEFAULT_FORMAT;
	public static boolean FORMAT_ENABLED;

	public static String DEFAULT_GROUP;

	public static boolean FORCE_DEFAULT_GROUP;

	public static boolean WEBHOOK_ENABLED;

	public static String WEBHOOK_URL;

	private static final HashMap<Player, MenuUtil> menuUtilMap = new HashMap<>();

	final int pluginId = 18489;

	@Override
	public void onEnable() {
		init();
	}

	private void init() {
		instance = this;

		configManager = new ConfigManager();
		configManager.load();

		setConstants();
		fragmentDatabase = new JsonDatabase();
		fragmentDatabase.load();

		discordManager = new DiscordManager();
		discordManager.init();

		groupHandler = new GroupHandler();

		loadCommands();
		loadEvents();

		new Metrics(this, pluginId);
	}

	public static FragmentPlugin getInstance() {
		return instance;
	}

	private void loadCommands() {
		PluginCommand groupCommand = getCommand("group");
		groupCommand.setExecutor(new GroupCommand());
		groupCommand.setTabCompleter(new GroupTabComplete());

		PluginCommand grantCommand = getCommand("grant");
		grantCommand.setExecutor(new GrantCommand());
		grantCommand.setTabCompleter(new GrantTabComplete());

		PluginCommand removeCommand = getCommand("removegrant");
		removeCommand.setExecutor(new RemoveGrantCommand());

		PluginCommand userCommand = getCommand("user");
		userCommand.setExecutor(new UserCommand());
		userCommand.setTabCompleter(new UserTabComplete());

		getCommand("fragment").setExecutor(new FragmentCommand());
		getCommand("grants").setExecutor(new GrantsCommand());
	}

	private void loadEvents() {
		Bukkit.getServer().getPluginManager().registerEvents(new ChatListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
	}

	private void loadPlaceholderApi() {
		if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
			new PapiHook().register();
			Bukkit.getLogger().info("Hooked into PlaceholderAPI.");
		} else {
			Bukkit.getLogger().info("Couldn't find PlaceholderAPI.");
		}
	}

	public void setConstants() {
		DEFAULT_FORMAT = getInstance().getConfigManager()
				.getConfig()
				.getOrSetDefault("default-format", "{Player} &8» &r{Message}");
		DEFAULT_GROUP = getInstance().getConfigManager()
				.getConfig()
				.getOrSetDefault("default-group", "Default");
		FORMAT_ENABLED = getInstance().getConfigManager()
				.getConfig()
				.getOrSetDefault("format-enabled", true);
		FORCE_DEFAULT_GROUP = getInstance().getConfigManager()
				.getConfig()
				.getOrSetDefault("force-default-group", Boolean.TRUE);
		WEBHOOK_ENABLED = getInstance().getConfigManager()
				.getConfig()
				.getOrSetDefault("webhook-enabled", false);
		WEBHOOK_URL = getInstance().getConfigManager()
				.getConfig()
				.getOrSetDefault("discord-webhook", "PLACE-HERE");


	}

	public HashMap<Player, MenuUtil> getMenuUtil() {
		return menuUtilMap;
	}

	public static MenuUtil getMenuUtil(Player player) {
		MenuUtil menuUtil;

		if (menuUtilMap.containsKey(player)) {
			return menuUtilMap.get(player);
		} else {
			menuUtil = new MenuUtil(player);
			menuUtilMap.put(player, menuUtil);
		}

		return menuUtil;
	}

	public static MenuUtil getMenuUtil(Player player, String group) {
		MenuUtil menuUtil;

		if (menuUtilMap.containsKey(player)) {
			return menuUtilMap.get(player);
		} else {
			menuUtil = new MenuUtil(player, group);
			menuUtilMap.put(player, menuUtil);
		}

		return menuUtil;
	}
}

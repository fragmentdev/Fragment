package me.xemu.fragment;

import lombok.Getter;
import me.xemu.fragment.commands.*;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.database.JsonDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.handler.GroupHandler;
import me.xemu.fragment.hooks.FragmentHook;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	public static boolean FORCE_CREATE_DEFAULT_GROUP;

	public static boolean WEBHOOK_ENABLED;

	public static String WEBHOOK_URL;

	private static final HashMap<Player, MenuUtil> menuUtilMap = new HashMap<>();

	final int pluginId = 18489;

	private List<FragmentHook> hooks;

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

		this.hooks = new ArrayList<>();
		hooks.add(new PapiHook());

		loadCommands();
		loadEvents();
		loadHooks(); // Attempt to load Placeholder API

		new Metrics(this, pluginId);

		if (FORCE_CREATE_DEFAULT_GROUP && getFragmentDatabase().loadGroup(DEFAULT_GROUP) == null) {
			getFragmentDatabase().saveGroup(new Group("Default", 1, "", "", "&a[Default] {Player]&8: &r{Message}", null));
		}
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

	private void loadHooks() {
		hooks.forEach(hook -> {
			if (hook.requiresPlugin()) {
				if(Bukkit.getServer().getPluginManager().getPlugin(hook.getPluginName()) == null) {
					Bukkit.getLogger().info("Could not load hook: <hookClass>".replaceAll("<hookClass>", hook.getClass().getName()));
					Bukkit.getLogger().info("Required plugin " + hook.getPluginName() + " doesn't exist.");
					Bukkit.getLogger().info("Fragment is being disabled.");

					getServer().getPluginManager().disablePlugin(this);
				} else {
					Bukkit.getLogger().info("Loaded hook: " + hook.getClass().getName());
				}
			}
		});
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

		FORCE_CREATE_DEFAULT_GROUP = getInstance().getConfigManager()
				.getConfig()
				.getOrSetDefault("force-create-default-group", Boolean.TRUE);


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

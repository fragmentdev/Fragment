package me.xemu.fragment;

import lombok.Getter;
import me.xemu.fragment.builder.ConsoleMessage;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.listeners.ChatListener;
import me.xemu.fragment.listeners.JoinListener;
import me.xemu.fragment.manager.*;
import me.xemu.fragment.menu.MenuListener;
import me.xemu.fragment.menu.MenuUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@Getter
public class FragmentPlugin extends JavaPlugin {

	private static FragmentPlugin fragment;

	private ConfigManager configManager;
	private DatabaseManager databaseManager;
	private CommandManager commandManager;
	private GroupManager groupManager;
	private UserManager userManager;
	private HookManager hookManager;

	private FragmentDatabase database;

	private final int pluginId = 18489;

	private static final HashMap<Player, MenuUtil> menuUtilMap = new HashMap<>();

	@Override
	public void onEnable() {
		init();
	}

	private void init() {
		fragment = this;

		this.configManager = new ConfigManager();
		this.hookManager = new HookManager();

		this.databaseManager = new DatabaseManager();
		this.commandManager = new CommandManager(this);
		this.groupManager = new GroupManager();
		this.userManager = new UserManager();
		this.database = databaseManager.select();

		new ConsoleMessage("Loading database integration.").consoleNormal(true);
		database.load();
		new ConsoleMessage("Loaded database integration: " + database.getClass().getName()).consoleSuccess(true);

		hookManager.load();
		groupManager.createDefaultGroup();

		commandManager.registerCommands();
		loadEvents();

		new Metrics(this, pluginId);
	}

	public static FragmentPlugin getFragment() {
		return fragment;
	}


	private void loadEvents() {
		getServer().getPluginManager().registerEvents(new JoinListener(), this);
		new ConsoleMessage("Registered JoinListener.")
				.consoleNormal(true);
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		new ConsoleMessage("Registered MenuListener.")
				.consoleNormal(true);

		if (ConfigManager.FORMAT_MODULE_ENABLED) {
			getServer().getPluginManager().registerEvents(new ChatListener(), this);
			new ConsoleMessage("Registered ChatListener..")
					.consoleNormal(true);
		} else {
			new ConsoleMessage("Skipped ChatListener initialization due to the format module being disabled.")
					.consoleWarning(true);
		}

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

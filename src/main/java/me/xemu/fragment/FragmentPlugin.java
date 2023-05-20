package me.xemu.fragment;

import lombok.Getter;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.manager.CommandManager;
import me.xemu.fragment.manager.ConfigManager;
import me.xemu.fragment.manager.DatabaseManager;
import me.xemu.fragment.menu.MenuListener;
import me.xemu.fragment.menu.MenuUtil;
import org.bstats.bukkit.Metrics;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

@Getter
public class FragmentPlugin extends JavaPlugin {


	private static FragmentPlugin fragment;

	private ConfigManager configManager;
	private DatabaseManager databaseManager;
	private CommandManager commandManager;

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
		this.databaseManager = new DatabaseManager();
		this.commandManager = new CommandManager(this);
		this.database = databaseManager.select();

		commandManager.registerCommands();

		loadEvents();

		new Metrics(this, pluginId);
	}

	public static FragmentPlugin getFragment() {
		return fragment;
	}


	private void loadEvents() {
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
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

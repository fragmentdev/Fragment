package me.xemu.fragment.manager;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.database.implementations.JsonDatabase;
import me.xemu.fragment.builder.ConsoleMessage;

public class DatabaseManager {
	private FragmentPlugin plugin = FragmentPlugin.getFragment();

	private FragmentDatabase database;

	public DatabaseManager() {
		if (select() == null) {
			new ConsoleMessage("Could not find a database integration.").consoleError(true);
		}
	}

	public FragmentDatabase select() {
		if (plugin.getConfig().getString("Database.System").equalsIgnoreCase("json")) {
			this.database = new JsonDatabase();
		}
		return null;
	}

	public FragmentDatabase getDatabase() {
		return database;
	}
}
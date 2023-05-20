package me.xemu.fragment.manager;

import de.leonhard.storage.Json;
import de.leonhard.storage.Yaml;
import lombok.Getter;
import lombok.Setter;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.implementations.JsonDatabase;

@Getter
@Setter
public class ConfigManager {
	private FragmentPlugin plugin = FragmentPlugin.getFragment();

	private Yaml config;
	private Yaml messages;
	private Json jsonDatabase;

	public ConfigManager() {
		this.config = new Yaml("config", "plugins/Fragment");
		this.messages = new Yaml("messages", "plugins/Fragment");

		if (plugin.getDatabase() instanceof JsonDatabase) {
			this.jsonDatabase = new Json("database", "plugins/Fragment");
		}

		config.setDefault("Database.System", "json");
	}
}
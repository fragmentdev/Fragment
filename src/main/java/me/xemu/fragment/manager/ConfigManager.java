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

	public static String DATABASE_SYSTEM;
	public static boolean DISCORD_WEBHOOK_ENABLED;
	public static String DISCORD_WEBHOOK_URL;
	public static boolean AUTO_CREATE_DEFAULT_GROUP;
	public static String DEFAULT_GROUP;
	public static boolean FORMAT_MODULE_ENABLED;
	public static String DEFAULT_FORMAT;

	public ConfigManager() {
		this.config = new Yaml("config", "plugins/Fragment");
		this.messages = new Yaml("messages", "plugins/Fragment");

		if (plugin.getDatabase() instanceof JsonDatabase) {
			this.jsonDatabase = new Json("database", "plugins/Fragment");
		}

		setConstants();
	}

	public void setConstants() {
		DATABASE_SYSTEM = config.getOrSetDefault("Database.Integration", "json");
		DISCORD_WEBHOOK_ENABLED = config.getOrSetDefault("Discord.Enabled", false);
		DISCORD_WEBHOOK_URL = config.getOrSetDefault("Discord.URL", "");
		AUTO_CREATE_DEFAULT_GROUP = config.getOrSetDefault("Settings.AutoCreateDefaultGroup", true);
		DEFAULT_GROUP = config.getOrSetDefault("Settings.DefaultGroup", "Default");
		FORMAT_MODULE_ENABLED = config.getOrSetDefault("ChatFormat.Enabled", true);
		DEFAULT_FORMAT = config.getOrSetDefault("ChatFormat.DefaultFormat", "{Prefix}{Player}&8: &r{Suffix}{Message}");
	}

}
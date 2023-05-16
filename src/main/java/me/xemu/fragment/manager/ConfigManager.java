package me.xemu.fragment.manager;

import de.leonhard.storage.Json;
import de.leonhard.storage.Yaml;
import lombok.Getter;

public class ConfigManager {

	private Yaml config;
	private Yaml messages;
	private Json database;

	public ConfigManager() {}

	public void load() {
		createConfigFile();
		createMessagesFile();
		createDatabaseFile();

		getConfig().setDefault("database.integration", "Json");
	}

	public void createConfigFile() {
		this.config = new Yaml("config", "plugins/Fragment");
	}

	public void createMessagesFile() {
		this.messages = new Yaml("messages", "plugins/Fragment");
	}

	public void createDatabaseFile() {
		this.database = new Json("database", "plugins/Fragment");
	}

	public Yaml getConfig() {
		if (config == null) throw new NullPointerException("Could not get config.");
		return config;
	}

	public Yaml getMessages() {
		if (messages == null) throw new NullPointerException("Could not get messages.");
		return messages;
	}

	public Json getDatabase() {
		if (database == null) throw new NullPointerException("Could not get database.");
		return database;
	}
}
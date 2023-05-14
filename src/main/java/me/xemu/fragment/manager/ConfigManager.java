package me.xemu.fragment.manager;

import de.leonhard.storage.Json;
import de.leonhard.storage.Yaml;
import lombok.Getter;

@Getter
public class ConfigManager {

	private Yaml config;
	private Yaml messages;
	private Json database;

	public void createConfigFile() {
		this.config = new Yaml("config", "plugins/Fragment");
	}

	public void createMessagesFile() {
		this.messages = new Yaml("messages", "plugins/Fragment");
	}

	public void createDatabaseFile() {
		this.database = new Json("database", "plugins/Fragment");
	}

}
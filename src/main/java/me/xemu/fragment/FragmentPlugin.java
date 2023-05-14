package me.xemu.fragment;

import lombok.Getter;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.manager.ConfigManager;
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
}

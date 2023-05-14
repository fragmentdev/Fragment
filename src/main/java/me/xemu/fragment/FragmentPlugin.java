package me.xemu.fragment;

import org.bukkit.plugin.java.JavaPlugin;

public class FragmentPlugin extends JavaPlugin {

	private static FragmentPlugin fragmentPlugin;

	@Override
	public void onEnable() {
		FragmentPlugin.fragmentPlugin = this;
	}

	@Override
	public void onDisable() {
		FragmentPlugin.fragmentPlugin = null;
	}

}

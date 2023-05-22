package me.xemu.fragment.manager;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.builder.ConsoleMessage;
import me.xemu.fragment.hook.FragmentHook;
import me.xemu.fragment.hook.FragmentHookData;
import me.xemu.fragment.hook.implementations.PapiHook;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class HookManager {

	private List<FragmentHook> hooks = new ArrayList<>();

	public HookManager() {
		registerToList();
		new ConsoleMessage("Hooks loaded into Manager.").consoleWarning(true);
	}

	public void registerToList() {
		hooks.add(new PapiHook());
	}

	public void load() {
		hooks.forEach(hook -> {
			if (hook.getClass().isAnnotationPresent(FragmentHookData.class)) {
				new ConsoleMessage("Hook located. Loading...").consoleWarning(true);
				FragmentHookData data = hook.getClass().getAnnotation(FragmentHookData.class);

				if (data.required()) {
					if (Bukkit.getPluginManager().getPlugin(data.name()) != null) {
						new ConsoleMessage("Hook loaded: " + hook.getClass().getName()).consoleSuccess(true);
					} else {
						new ConsoleMessage("Could not load hook! Could not locate required plugin " + data.name() + ". Plugin is disabling!")
								.consoleError(true);
						Bukkit.getPluginManager().disablePlugin(FragmentPlugin.getFragment());
					}
 				} else {
					new ConsoleMessage("Hook loaded: " + hook.getClass().getName()).consoleSuccess(true);
				}
			} else {
				new ConsoleMessage("Could not load hook! Method is not properly annotated with the FragmentHookData.class annotation!")
						.consoleError(true);
				Bukkit.getPluginManager().disablePlugin(FragmentPlugin.getFragment());
			}
		});
	}

}

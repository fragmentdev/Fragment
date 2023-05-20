package me.xemu.fragment.builder;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

@Setter
@Getter
public class ConsoleMessage {

	private String message;

	public ConsoleMessage(String message) {
		this.message = message;
	}

	public ConsoleMessage consoleNormal(boolean prefix) {
		if (prefix) {
			Bukkit.getLogger().info("[Fragment] " + getMessage());
		} else {
			Bukkit.getLogger().info(getMessage());
		}
		return this;
	}

	public ConsoleMessage consoleWarning(boolean prefix) {
		if (prefix) {
			Bukkit.getLogger().info(ChatColor.GOLD + "[Fragment] " + getMessage());
		} else {
			Bukkit.getLogger().info(ChatColor.GOLD + getMessage());
		}
		return this;
	}

	public ConsoleMessage consoleError(boolean prefix) {
		if (prefix) {
			Bukkit.getLogger().info(ChatColor.RED + "[Fragment] " + getMessage());
		} else {
			Bukkit.getLogger().info(ChatColor.RED + getMessage());
		}
		return this;
	}

}
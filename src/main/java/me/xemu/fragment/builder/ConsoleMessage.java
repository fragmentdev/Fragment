package me.xemu.fragment.builder;

import lombok.Getter;
import lombok.Setter;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

@Setter
@Getter
public class ConsoleMessage {

	private String message;

	public ConsoleMessage(String message) {
		this.message = message;
	}

	public ConsoleMessage consoleNormal(boolean prefix) {
		if (prefix) {
			sendConsoleMessage("&7[&bFragment&7] &r" + getMessage());
		} else {
			sendConsoleMessage("&r" + getMessage());
		}
		return this;
	}

	public ConsoleMessage consoleWarning(boolean prefix) {
		if (prefix) {
			sendConsoleMessage("&7[&bFragment&7] &6" + getMessage());
		} else {
			sendConsoleMessage("&6" + getMessage());
		}
		return this;
	}

	public ConsoleMessage consoleError(boolean prefix) {
		if (prefix) {
			sendConsoleMessage("&7[&bFragment&7] &c" + getMessage());
		} else {
			sendConsoleMessage("&c" + getMessage());
		}
		return this;
	}

	public ConsoleMessage consoleSuccess(boolean prefix) {
		if (prefix) {
			sendConsoleMessage("&7[&bFragment&7] &a" + getMessage());
		} else {
			sendConsoleMessage("&a" + getMessage());
		}
		return this;
	}

	public void sendConsoleMessage(String message) {
		ConsoleCommandSender sender = Bukkit.getConsoleSender();
		sender.sendMessage(Utils.translate(message));
	}



}
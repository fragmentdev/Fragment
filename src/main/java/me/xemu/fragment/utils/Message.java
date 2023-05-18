package me.xemu.fragment.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
@Setter
@AllArgsConstructor
public class Message {

	private String message;

	public Message colorize() {
		this.message = ChatColor.translateAlternateColorCodes('&', getMessage());
		return this;
	}

	public Message placeholder(String placeholder, String replacement) {
		this.message = message.replaceAll(placeholder, replacement);
		return this;
	}

	public void send(Player player) {
		player.sendMessage(getMessage());
	}

	public void send(CommandSender sender) {
		sender.sendMessage(getMessage());
	}

	public void console(boolean prefix) {
		if (prefix) {
			Bukkit.getLogger().info("[Fragment] " + getMessage());
		} else {
			Bukkit.getLogger().info(getMessage());
		}
	}

	public Message placeholderApi(Player player) {
		setMessage(PlaceholderAPI.setPlaceholders(player, message));
		return this;
	}

}

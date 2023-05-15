package me.xemu.fragment.utils;

import me.xemu.fragment.entity.Group;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Utils {

	public static String translate(String str) {
		return ChatColor.translateAlternateColorCodes('&', str);
	}

	public static boolean isNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void sendError(Player player, String string) {
		new Message("&8&m-----------------------------").colorize().send(player);
		new Message("&c&l✗ &r&c" + string).colorize().send(player);
		new Message("&8&m-----------------------------").colorize().send(player);
	}

	public static void sendError(Player player, String string, Map<String, String> strings) {
		List<Message> toSend = new ArrayList<>();

		toSend.add(new Message("&8&m-----------------------------").colorize());
		toSend.add(new Message("&c&l✗ &r&c" + string).colorize());

		strings.keySet().forEach(str -> {
			toSend.add(new Message("&7" + str + ": &f" + strings.get(str)).colorize());
		});

		toSend.add(new Message("&8&m-----------------------------").colorize());
		toSend.forEach(send -> {
			send.colorize().send(player);
		});
	}

	public static void sendSuccess(Player player, String string, Map<String, String> strings) {
		List<Message> toSend = new ArrayList<>();

		toSend.add(new Message("&8&m-----------------------------").colorize());
		toSend.add(new Message("&a&l✓ &r" + string).colorize());

		strings.keySet().forEach(str -> {
			toSend.add(new Message("&7" + str + ": &f" + strings.get(str)).colorize());
		});

		toSend.add(new Message("&8&m-----------------------------").colorize());
		toSend.forEach(send -> {
			send.colorize().send(player);
		});
	}

	public static void sendSuccess(Player player, String string) {
		List<Message> toSend = new ArrayList<>();

		toSend.add(new Message("&8&m-----------------------------").colorize());
		toSend.add(new Message("&a&l✓ &r" + string).colorize());
		toSend.add(new Message("&8&m-----------------------------").colorize());
		toSend.forEach(send -> {
			send.colorize().send(player);
		});
	}

	public static Group getHeaviestGroup(List<Group> groups) {
		if (groups.isEmpty()) {
			return null;
		}

		groups.sort(Comparator.comparingInt(Group::getWeight).reversed());
		return groups.get(0);
	}


}

package me.xemu.fragment.utils;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.builder.Message;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Utils {

	private static Pattern p2 = Pattern.compile("&#([A-Fa-f0-9]){6}");

	public static String translate(String message) {
		message = message.replace(">>", "").replace("<<", "");
		Matcher matcher = p2.matcher(message);
		while (matcher.find()) {
			ChatColor hexColor = ChatColor.of(matcher.group().substring(1));
			String before = message.substring(0, matcher.start());
			String after = message.substring(matcher.end());
			message = before + hexColor + after;
			matcher = p2.matcher(message);
		}
		return ChatColor.translateAlternateColorCodes('&', message);
	}

	public static String deformat(String str) {
		return ChatColor.stripColor(translate(str));
	}

	public static List<String> color(List<String> lore){
		return lore.stream().map(Utils::translate).collect(Collectors.toList());
	}

	public static boolean isNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void sendHelpMessage(Player player, String[] message) {
		new Message("&8&m-------------------------------").colorize().send(player);
		new Message("&aFragment v" + FragmentPlugin.getFragment().getDescription().getVersion() + "&7 by &bXemu&7.").colorize().send(player);
		new Message("&7Advanced Permission Framework").colorize().send(player);
		for (String string : message) {
			new Message("&7> &b<message>").colorize().placeholder("<message>", string).send(player);
		}
		new Message("&8&m-------------------------------").colorize().send(player);
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
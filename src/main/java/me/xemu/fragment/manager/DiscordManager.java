package me.xemu.fragment.manager;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.discord.DiscordWebhook;

import java.awt.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DiscordManager {

	private Color color = Color.decode("#2a729c");
	private String image = "https://images-ext-1.discordapp.net/external/IMWjIRCedV6QP0azGhJ0WKS0bii_qdmlH3mUGc3h77k/%3F1661178087/https/www.spigotmc.org/data/avatars/l/545/545825.jpg?width=240&height=240";

	private String url = FragmentPlugin.WEBHOOK_URL;
	private boolean enabled = FragmentPlugin.WEBHOOK_ENABLED;

	public void sendGrantLog(String whoGranted, String granted, String grantName) {
		DiscordWebhook webhook = new DiscordWebhook(url);
		DiscordWebhook.EmbedObject e = new DiscordWebhook.EmbedObject();

		e.setTitle("Fragment | Grant Log");
		e.setDescription("A grant action has been executed.");
		e.addField("Executor", whoGranted, true);
		e.addField("Receiver", granted, true);
		e.addField("Group", grantName, false);
		e.addField("Timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), false);
		e.setColor(color);
		e.setFooter("Fragment by Xemu", image);

		webhook.addEmbed(e);
		try {
			webhook.execute();
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void sendCreateGroupLog(String user, String groupCreated, String groupWeight) {
		DiscordWebhook webhook = new DiscordWebhook(url);
		DiscordWebhook.EmbedObject e = new DiscordWebhook.EmbedObject();

		e.setTitle("Fragment | Create Group Log");
		e.setDescription("A create group action has been executed.");
		e.addField("Executor", user, true);
		e.addField("Group", groupCreated, true);
		e.addField("Weight", groupWeight, false);
		e.addField("Timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), false);
		e.setColor(color);
		e.setFooter("Fragment by Xemu", image);

		webhook.addEmbed(e);
		try {
			if (enabled) {
				webhook.execute();
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void editGroupLog(String user, String group, String key, String value) {
		DiscordWebhook webhook = new DiscordWebhook(url);
		DiscordWebhook.EmbedObject e = new DiscordWebhook.EmbedObject();

		e.setTitle("Fragment | Edit Group Log");
		e.setDescription("A edit group action has been executed.");
		e.addField("Executor", user, true);
		e.addField("Group", group, false);
		e.addField("Key", key, false);
		e.addField("Value", value, false);
		e.addField("Timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), false);
		e.setColor(color);
		e.setFooter("Fragment by Xemu", image);

		webhook.addEmbed(e);
		try {
			if (enabled) {
				webhook.execute();
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void deleteGroupLog(String user, String group) {
		DiscordWebhook webhook = new DiscordWebhook(url);
		DiscordWebhook.EmbedObject e = new DiscordWebhook.EmbedObject();

		e.setTitle("Fragment | Delete Group Log");
		e.setDescription("A delete group action has been executed.");
		e.addField("Executor", user, true);
		e.addField("Group", group, false);
		e.addField("Timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), false);
		e.setColor(color);
		e.setFooter("Fragment by Xemu", image);

		webhook.addEmbed(e);
		try {
			if (enabled) {
				webhook.execute();
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

}

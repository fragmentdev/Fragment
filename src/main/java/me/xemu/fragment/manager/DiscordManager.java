package me.xemu.fragment.manager;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.discord.DiscordWebhook;

import java.awt.*;
import java.io.IOException;

public class DiscordManager {

	private String url;
	private boolean enabled;
	private Color color;
	private String image;
	private String username;
	private String avatarUrl;

	public void init() {
		this.url = FragmentPlugin.WEBHOOK_URL;
		this.enabled = !url.equals("PLACE-HERE");
		this.color = Color.decode("#2a729c");
		this.image = "https://www.spigotmc.org/data/avatars/l/545/545825.jpg";
		this.username = "Fragment Logs";
		this.avatarUrl = "https://www.spigotmc.org/data/avatars/l/545/545825.jpg";
	}

	private void sendLog(String title, String description, String executor, String content) {
		DiscordWebhook webhook = new DiscordWebhook(url);
		DiscordWebhook.EmbedObject embed = new DiscordWebhook.EmbedObject();

		embed.setTitle(title);
		embed.setDescription(description);
		embed.addField("Executor", executor, true);
		embed.setColor(color);
		embed.setFooter("Fragment by Xemu", image);

		webhook.setUsername(username);
		webhook.setAvatarUrl(avatarUrl);
		webhook.addEmbed(embed);
		webhook.setContent(content);

		try {
			if (enabled) {
				webhook.execute();
			}
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	public void sendGrantLog(String whoGranted, String granted, String grantName) {
		String title = "Fragment | Grant Log";
		String description = "A grant action has been executed.";
		String content = "Fragment provided a Grant log.";

		sendLog(title, description, whoGranted, content);
	}

	public void sendCreateGroupLog(String user, String groupCreated, String groupWeight) {
		String title = "Fragment | Create Group Log";
		String description = "A create group action has been executed.";
		String content = "Fragment provided a create Group log.";

		sendLog(title, description, user, content);
	}

	public void editGroupLog(String user, String group, String key, String value) {
		String title = "Fragment | Edit Group Log";
		String description = "An edit group action has been executed.";
		String content = "Fragment provided an edit Group log.";

		sendLog(title, description, user, content);
	}

	public void deleteGroupLog(String user, String group) {
		String title = "Fragment | Delete Group Log";
		String description = "A delete group action has been executed.";
		String content = "Fragment provided a delete Group log.";

		sendLog(title, description, user, content);
	}
}

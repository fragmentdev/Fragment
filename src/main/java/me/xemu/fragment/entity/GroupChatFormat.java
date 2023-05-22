package me.xemu.fragment.entity;

import me.xemu.fragment.builder.Message;
import me.xemu.fragment.manager.UserManager;
import me.xemu.fragment.utils.Utils;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class GroupChatFormat {

	private String raw;
	private String formatted;

	public GroupChatFormat(String raw, AsyncPlayerChatEvent event) {
		this.raw = raw;

		User user = UserManager.load(event.getPlayer().getUniqueId());
		Group group = Utils.getHeaviestGroup(user.getGroups());
		String prefix = group.getPrefix();
		String suffix = group.getSuffix();

		if (group == null) {
			prefix = "";
			suffix = "";
		}

		if (group == null || group.getPrefix().isEmpty()) {
			prefix = "";
		} else if (group == null || group.getSuffix().isEmpty()) {
			suffix = "";
		}

		String format = new Message(raw)
				.placeholder("{Player}", event.getPlayer().getName())
				.placeholder("{Message}", event.getMessage())
				.placeholder("{Prefix}", prefix)
				.placeholder("{Suffix}", suffix)
				.placeholderApi(event.getPlayer())
				.colorize()
				.getMessage();

		formatted = format;
	}

	public String getRaw() {
		return raw;
	}

	public String getFormatted() {
		return formatted != null ? formatted : "";
	}
}

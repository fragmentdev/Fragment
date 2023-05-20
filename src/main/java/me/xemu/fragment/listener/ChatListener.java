package me.xemu.fragment.listener;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.utils.Message;
import me.xemu.fragment.utils.Utils;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		if (FragmentPlugin.FORMAT_ENABLED) {
			Player player = event.getPlayer();
			Group playerGroup = Utils.getHeaviestGroup(database.loadUser(player.getUniqueId()).getGroups());

			if (playerGroup == null) {
				return;
			}

			Message prefix = new Message(playerGroup.getPrefix())
					.colorize()
					.placeholderApi(player);

			Message suffix = new Message(playerGroup.getSuffix())
					.colorize()
					.placeholderApi(player);

			Message playerName = new Message(player.getName())
					.colorize()
					.placeholderApi(player);

			Message message = new Message(event.getMessage())
					.colorize()
					.placeholderApi(player);

			String format = new Message(playerGroup.getFormat()).colorize().placeholderApi(player).getMessage();
			if (playerGroup.getFormat().equals("")) {
				format = new Message(FragmentPlugin.DEFAULT_FORMAT).colorize().placeholderApi(player).getMessage();
			}
			format = format.replace("{Prefix}", prefix.getMessage())
					.replace("{Player}", playerName.getMessage())
					.replace("{Suffix}", suffix.getMessage())
					.replace("{Message}", message.getMessage());

			event.setFormat(format);
		}
	}

}

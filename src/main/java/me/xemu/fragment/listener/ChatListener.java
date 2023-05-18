package me.xemu.fragment.listener;

import me.clip.placeholderapi.PlaceholderAPI;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.utils.Utils;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		Group playerGroup = Utils.getHeaviestGroup(plugin.getFragmentDatabase().loadUser(player.getUniqueId())
				.getGroups());

		if (playerGroup == null) {
			return;
		}

		String prefix = PlaceholderAPI.setPlaceholders(player, Utils.translate(playerGroup.getPrefix()));
		String suffix = PlaceholderAPI.setPlaceholders(player, Utils.translate(playerGroup.getSuffix()));
		String playerName = PlaceholderAPI.setPlaceholders(player, player.getDisplayName());
		String message = event.getMessage();

		String format = PlaceholderAPI.setPlaceholders(player, Utils.translate(playerGroup.getFormat()));
		if (playerGroup.getFormat().equals("")) {
			format = PlaceholderAPI.setPlaceholders(player, Utils.translate(FragmentPlugin.DEFAULT_FORMAT));
		}
		format = format.replace("{Prefix}", prefix)
				.replace("{Player}", playerName)
				.replace("{Suffix}", suffix)
				.replace("{Message}", message);

		event.setFormat(format);
	}

}

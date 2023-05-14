package me.xemu.fragment.listener;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.Message;
import me.xemu.fragment.Utils;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

	private FragmentPlugin plugin = FragmentPlugin.getFragmentPlugin();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		Group playerGroup = Utils.getHeaviestGroup(plugin.getFragmentDatabase().loadUser(player.getUniqueId())
				.getGroups());

		if (playerGroup == null) {
			return;
		}

		String prefix = Utils.translate(playerGroup.getPrefix());
		String suffix = Utils.translate(playerGroup.getSuffix());
		String playerName = player.getDisplayName();
		String message = event.getMessage();

		String format = Utils.translate(playerGroup.getFormat());
		if (playerGroup.getFormat().equals("")) {
			format = Utils.translate(FragmentPlugin.DEFAULT_FORMAT);
		}
		format = format.replace("{Prefix}", prefix)
				.replace("{Player}", playerName)
				.replace("{Suffix}", suffix)
				.replace("{Message}", message);

		event.setFormat(format);
	}

}

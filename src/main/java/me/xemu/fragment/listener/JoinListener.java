package me.xemu.fragment.listener;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		User user = database.loadUser(player.getUniqueId());
		Group group = database.loadGroup(FragmentPlugin.DEFAULT_GROUP);

		if (user.getGroups().isEmpty() && FragmentPlugin.FORCE_DEFAULT_GROUP) {
			user.getGroups().add(group);
			database.saveUser(user);
		}

		user.loadPermissions();
	}

}

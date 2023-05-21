package me.xemu.fragment.listeners;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.manager.GroupManager;
import me.xemu.fragment.manager.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private FragmentPlugin plugin = FragmentPlugin.getFragment();
    private GroupManager groupManager = plugin.getGroupManager();
    private UserManager userManager = plugin.getUserManager();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = UserManager.load(player);


    }

}

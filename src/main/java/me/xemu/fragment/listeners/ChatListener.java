package me.xemu.fragment.listeners;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.GroupChatFormat;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.manager.ConfigManager;
import me.xemu.fragment.manager.UserManager;
import me.xemu.fragment.utils.Utils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    private FragmentPlugin plugin = FragmentPlugin.getFragment();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        User user = UserManager.load(event.getPlayer());
        Group topGroup = Utils.getHeaviestGroup(user.getGroups());

        String formatString;
        if (topGroup != null && topGroup.getFormat() != null && !topGroup.getFormat().isEmpty()) {
            formatString = topGroup.getFormat();
        } else {
            formatString = ConfigManager.DEFAULT_FORMAT;
        }

        if (formatString != null) {
            GroupChatFormat format = new GroupChatFormat(formatString, event);
            String formatted = format.getFormatted();
            event.setFormat(formatted);
        }
    }

}
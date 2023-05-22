package me.xemu.fragment.listeners;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.GroupChatFormat;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.manager.ConfigManager;
import me.xemu.fragment.manager.UserManager;
import me.xemu.fragment.utils.Utils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        User user = UserManager.load(event.getPlayer());

        if (!user.getGroups().isEmpty()) {
            handleGroupChatFormat(user, event);
        } else {
            handleNoGroupChatFormat(user, event);
        }
    }

    public void handleGroupChatFormat(User user, AsyncPlayerChatEvent event) {
        Group group = Utils.getHeaviestGroup(user.getGroups());
        String groupRawString = ConfigManager.DEFAULT_FORMAT;
        if (!group.getFormat().isBlank()) {
            groupRawString = group.getFormat();
        }
        GroupChatFormat groupChatFormat = new GroupChatFormat(groupRawString, event);
        event.setFormat(groupChatFormat.getFormatted());
    }

    public void handleNoGroupChatFormat(User user, AsyncPlayerChatEvent event) {
        String groupRawString = ConfigManager.DEFAULT_FORMAT;
        GroupChatFormat groupChatFormat = new GroupChatFormat(groupRawString, event);
        event.setFormat(groupChatFormat.getFormatted());
    }

}
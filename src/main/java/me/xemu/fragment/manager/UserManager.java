package me.xemu.fragment.manager;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Player;

public class UserManager {

    private FragmentPlugin plugin = FragmentPlugin.getFragment();
    private FragmentDatabase database = plugin.getDatabase();

    public void addGroupToUser(User user, Group group, boolean save) {
        if (!user.getGroups().contains(group)) {
            user.getGroups().add(group);
            if (save) {
                database.saveUser(user);
            }
        }
    }

    public void addGroupToPlayer(Player player, Group group, boolean save) {
        User user = database.loadUser(player.getUniqueId());
        if (!user.getGroups().contains(group)) {
            user.getGroups().add(group);
            if (save) {
                database.saveUser(user);
            }
        }
    }

    public void removeGroupFromUser(User user, Group group, boolean save) {
        user.getGroups().remove(group);
        if (save) {
            database.saveUser(user);
        }
    }

    public void removeGroupFromPlayer(Player player, Group group, boolean save) {
        User user = database.loadUser(player.getUniqueId());
        user.getGroups().remove(group);
        if (save) {
            database.saveUser(user);
        }
    }

}
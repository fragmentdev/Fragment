package me.xemu.fragment.manager;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

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

    public List<Group> getGroups(User user) {
        return user.getGroups();
    }

    public boolean hasGroup(User user, Group group) {
        if (user.getGroups().contains(group)) return true;
        return false;
    }

    public boolean hasGroup(Player player, Group group) {
        return hasGroup(UserManager.load(player), group);
    }

    public static User load(UUID uuid) {
        return FragmentPlugin.getFragment().getDatabase().loadUser(uuid);
    }

    public static User load(Player player) {
        return load(player.getUniqueId());
    }

}
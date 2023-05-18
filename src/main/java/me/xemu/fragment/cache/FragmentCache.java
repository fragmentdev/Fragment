package me.xemu.fragment.cache;

import lombok.Getter;
import lombok.Setter;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
@Setter
public class FragmentCache {
	private Cache<Group> groupCache;
	private Cache<User> userCache;

	public FragmentCache() {
		this.groupCache = new Cache<>();
		this.userCache = new Cache<>();
	}

	public User loadUser(Player player) {
		if (userCache.contains(asString(player.getUniqueId()))) {
			User user = userCache.get(asString(player.getUniqueId()));
			return user;
		} else {
			User user = FragmentPlugin.getInstance().getFragmentDatabase().loadUser(player);
			userCache.put(asString(player.getUniqueId()), user);
			return user;
		}
	}

	public void updateUser(Player player) {
		if (userCache.contains(asString(player.getUniqueId()))) {
			userCache.remove(asString(player.getUniqueId()));
			userCache.put(asString(player.getUniqueId()), loadUser(player));
		} else {
			User user = FragmentPlugin.getInstance().getFragmentDatabase().loadUser(player);
			userCache.put(asString(player.getUniqueId()), user);
		}
	}

	public void removeUser(Player player) {
		if (userCache.contains(asString(player.getUniqueId()))) {
			userCache.remove(asString(player.getUniqueId()));
		}
	}

	public Group loadGroup(String name) {
		if (groupCache.contains(name)) {
			Group group = groupCache.get(name);
			return group;
		} else {
			Group group = FragmentPlugin.getInstance().getFragmentDatabase().loadGroup(name);
			groupCache.put(group.getName(), group);
			return group;
		}
	}

	public void updateGroup(Group group) {
		if (groupCache.contains(group.getName())) {
			groupCache.remove(group.getName());
			groupCache.put(group.getName(), group);
		} else {
			FragmentPlugin.getInstance().getFragmentDatabase().saveGroup(group);
			groupCache.put(group.getName(), group);
		}
	}

	public void removeGroup(Group group) {
		groupCache.remove(group.getName());
	}

	public String asString(UUID uuid) {
		return uuid.toString();
	}
}

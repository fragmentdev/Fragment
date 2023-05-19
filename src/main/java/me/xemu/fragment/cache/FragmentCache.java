package me.xemu.fragment.cache;

import lombok.Getter;
import lombok.Setter;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
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

	public User loadUserOrAdd(Player player) {
		if (userCache.contains(asString(player.getUniqueId()))) {
			User user = userCache.get(asString(player.getUniqueId()));
			return user;
		} else {
			User user = FragmentPlugin.getInstance().getFragmentDatabase().loadUser(player);
			userCache.put(asString(player.getUniqueId()), user);
			return user;
		}
	}

	public User loadUserOrAdd(UUID uuid) {
		if (userCache.contains(asString(uuid))) {
			User user = userCache.get(asString(uuid));
			return user;
		} else {
			User user = FragmentPlugin.getInstance().getFragmentDatabase().loadUser(uuid);
			userCache.put(asString(uuid), user);
			return user;
		}
	}

	public void updateUser(Player player) {
		if (userCache.contains(asString(player.getUniqueId()))) {
			userCache.remove(asString(player.getUniqueId()));
			userCache.put(asString(player.getUniqueId()), loadUserOrAdd(player));
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

	public List<Group> getCachedGroups() {
		ArrayList<Group> cachedGroups = new ArrayList<>();
		groupCache.getKeys().forEach(key -> {
			cachedGroups.add(loadGroup(key));
		});
		return cachedGroups;
	}

	public List<User> getCachedUsers() {
		ArrayList<User> cachedUsers = new ArrayList<>();
		userCache.getKeys().forEach(key -> {
			cachedUsers.add(loadUserOrAdd(UUID.fromString(key)));
		});
		return cachedUsers;
	}

	public void removeGroup(Group group) {
		groupCache.remove(group.getName());
	}

	public String asString(UUID uuid) {
		return uuid.toString();
	}
}

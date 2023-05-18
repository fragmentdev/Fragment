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

	public String asString(UUID uuid) {
		return uuid.toString();
	}
}

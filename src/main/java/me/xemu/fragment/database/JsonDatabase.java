package me.xemu.fragment.database;

import de.leonhard.storage.Json;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JsonDatabase implements FragmentDatabase {
	private Json db;

	@Override
	public String getIdentifier() {
		return "json";
	}

	@Override
	public void load() {
		this.db = FragmentPlugin.getFragmentPlugin().getConfigManager().getDatabase();
	}

	@Override
	public User loadUser(UUID uuid) {
		List<String> groupStrings = db.getStringList("users." + uuid + ".groups");
		ArrayList<Group> groups = new ArrayList<>();

		groupStrings.forEach(groupName -> {
			groups.add(loadGroup(groupName));
		});

		return new User(uuid, groups);
	}

	@Override
	public User loadUser(Player player) {
		return loadUser(player.getUniqueId());
	}

	@Override
	public void saveUser(User user) {
		List<String> groups = user.getGroups().stream().map(Group::getName).toList();
		db.set("users." + user.getUuid() + ".groups", groups);
		db.write();
	}

	@Override
	public List<User> getUsers() {
		List<UUID> players = FragmentPlugin.getFragmentPlugin().getServer().getOnlinePlayers().stream()
				.map(Entity::getUniqueId)
				.toList();

		List<User> users = new ArrayList<>();
		players.forEach(player -> {
			users.add(loadUser(player));
		});

		return users;
	}

	@Override
	public Group loadGroup(String name) {
		return null;
	}

	@Override
	public void saveGroup(Group group) {

	}

	@Override
	public List<Group> getGroups() {
		return null;
	}
}

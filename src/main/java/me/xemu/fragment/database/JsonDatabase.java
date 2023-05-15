package me.xemu.fragment.database;

import de.leonhard.storage.Json;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class JsonDatabase implements FragmentDatabase {
	private FragmentPlugin plugin;
	private Json db;

	@Override
	public String getIdentifier() {
		return "json";
	}

	@Override
	public void load() {
		this.plugin = FragmentPlugin.getFragmentPlugin();
		this.db = plugin.getConfigManager().getDatabase();

	}

	@Override
	public User loadUser(UUID uuid) {
		List<String> groupStrings = db.getStringList("users." + uuid + ".groups");
		List<Group> groups = groupStrings.stream()
				.map(this::loadGroup)
				.filter(group -> group != null)
				.collect(Collectors.toList());

		return new User(uuid, groups);
	}

	@Override
	public User loadUser(Player player) {
		return loadUser(player.getUniqueId());
	}

	@Override
	public void saveUser(User user) {
		List<String> groups = user.getGroups().stream()
				.map(Group::getName)
				.collect(Collectors.toList());
		db.set("users." + user.getUuid() + ".groups", groups);
		db.write();
	}

	@Override
	public List<User> getUsers() {
		List<UUID> playerUUIDs = plugin.getServer().getOnlinePlayers().stream()
				.map(Entity::getUniqueId)
				.collect(Collectors.toList());

		List<User> users = new ArrayList<>();
		playerUUIDs.forEach(uuid -> users.add(loadUser(uuid)));

		return users;
	}

	@Override
	public Group loadGroup(String name) {
		if (!db.contains("groups." + name)) {
			return null;
		}

		int weight = db.getInt("groups." + name + ".weight");
		String prefix = db.getString("groups." + name + ".prefix");
		String suffix = db.getString("groups." + name + ".suffix");
		String format = db.getString("groups." + name + ".format");
		List<String> permissions = db.getStringList("groups." + name + ".permissions");

		return new Group(name, weight, prefix, suffix, format, permissions);
	}

	@Override
	public void saveGroup(Group group) {
		String name = group.getName();

		db.set("groups." + name + ".name", name);
		db.set("groups." + name + ".weight", group.getWeight());
		db.set("groups." + name + ".prefix", group.getPrefix());
		db.set("groups." + name + ".suffix", group.getSuffix());
		db.set("groups." + name + ".format", group.getFormat());
		db.set("groups." + name + ".permissions", group.getPermissions());

		db.write();
	}

	@Override
	public List<Group> getGroups() {
		Set<String> groupNames = db.getSection("groups").singleLayerKeySet();
		List<Group> groups = new ArrayList<>();
		groupNames.forEach(groupName -> {
			Group group = loadGroup(groupName);
			if (group != null) {
				groups.add(group);
			}
		});

		return groups;
	}

	@Override
	public boolean exists(User user) {
		return db.contains("users." + user.getUuid());
	}

	@Override
	public boolean exists(Group group) {
		return db.contains("groups." + group.getName());
	}
}
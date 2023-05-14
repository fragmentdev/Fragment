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
	private Json db;
	private FragmentPlugin plugin;

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
		return new Group(
				name,
				db.getInt("groups." + name + ".weight"),
				db.getString("groups." + name + ".prefix"),
				db.getString("groups." + name + ".suffix"),
				db.getString("groups." + name + ".format"),
				db.getStringList("groups." + name + ".permissions")
		);
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
		List<Group> groups = new ArrayList<>();
		Set<String> groupNames = db.singleLayerKeySet("groups");
		groupNames.forEach(group -> {
			groups.add(loadGroup(group));
		});

		return groups;
	}
}

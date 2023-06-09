package me.xemu.fragment.database.implementations;

import de.leonhard.storage.Json;
import lombok.Getter;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class JsonDatabase implements FragmentDatabase {
	private final FragmentPlugin plugin = FragmentPlugin.getFragment();
	public Json db;

	@Override
	public void load() {
		this.db = plugin.getConfigManager().getJsonDatabase();
	}

	@Override
	public User loadUser(UUID uuid) {
		List<Group> groups = db.getStringList("users." + uuid + ".groups")
				.stream()
				.map(this::loadGroup)
				.toList();

		List<String> permissions = db.getStringList("users." + uuid + ".permissions");

		ArrayList<Group> groupArrayList = new ArrayList<>(groups);
		ArrayList<String> permissionsArrayList = new ArrayList<>(permissions);

		return new User(uuid, groupArrayList, permissionsArrayList);
	}

	@Override
	public void saveUser(User user) {
		db.set("users." + user.getUuid() + ".groups", user.getGroups().stream().map(Group::getName).toList());
		db.set("users." + user.getUuid() + ".permissions", user.getPlayerPermissions());
		db.write();
	}

	@Override
	public Group loadGroup(String name) {
		int weight = db.getInt("groups." + name + ".weight");
		String prefix = db.getString("groups." + name + ".prefix");
		String suffix = db.getString("groups." + name + ".suffix");
		String format = db.getString("groups." + name + ".format");
		List<String> permissions = db.getStringList("groups." + name + ".permissions");

		return new Group(name, weight, prefix, suffix, format, permissions);
	}

	@Override
	public void saveGroup(Group group) {
		String name = group.getName().toLowerCase();
		db.set("groups." + name + ".weight", group.getWeight());
		db.set("groups." + name + ".prefix", group.getPrefix());
		db.set("groups." + name + ".suffix", group.getSuffix());
		db.set("groups." + name + ".format", group.getFormat());
		db.set("groups." + name + ".permissions", group.getPermissions());
		db.write();
	}

	@Override
	public List<User> loadUsers() {
		return db.getSection("users").singleLayerKeySet().stream().map(m -> loadUser(UUID.fromString(m))).toList();
	}

	@Override
	public List<Group> loadGroups() {
		return db.getSection("groups").singleLayerKeySet().stream().map(this::loadGroup).toList();
	}

}
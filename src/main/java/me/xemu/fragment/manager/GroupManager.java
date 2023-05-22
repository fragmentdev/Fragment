package me.xemu.fragment.manager;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.builder.ConsoleMessage;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.database.implementations.JsonDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.utils.Utils;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class GroupManager {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getDatabase();

	public void createGroup(String name, int weight, String prefix, String suffix, String format, List<String> permissions) {
		if (database.loadGroup(name.toLowerCase()) == null) {
			database.saveGroup(new Group(name.toLowerCase(), weight, prefix, suffix, format, permissions));
			new ConsoleMessage("Group Created: " + name + " / " + weight + " - " + permissions.size() + " permissions.")
					.consoleNormal(true);
		}
	}

	public void editGroup(String name, String key, Object value) {
		Group group = database.loadGroup(name);

		switch (key) {
			case "prefix":
				group.setPrefix((String) value);
				database.saveGroup(group);
				break;
			case "suffix":
				group.setSuffix((String) value);
				database.saveGroup(group);
				break;
			case "format":
				group.setFormat((String) value);
				database.saveGroup(group);
				break;
			case "weight":
				if (Utils.isNumber((String) value)) {
					group.setWeight(Integer.valueOf((String) value));
					database.saveGroup(group);
				}
				break;
		}
	}

	public void deleteGroup(String name) {
		if (plugin.getDatabase() instanceof JsonDatabase) {
			plugin.getConfigManager().getJsonDatabase().removeAll("groups." + name);
		}

		// TODO: Bring MySQL Support to this method
	}

	public List<Group> getAllGroups() {
		return database.loadGroups();
	}

	public List<Group> filterGroups(Predicate<Group> predicate) {
		return getAllGroups().stream().filter(predicate).toList();
	}

	// TODO: Customizable properties for Default Group
	public void createDefaultGroup() {
		if (ConfigManager.AUTO_CREATE_DEFAULT_GROUP) {
			if (GroupManager.load(ConfigManager.DEFAULT_GROUP) == null) {
				createGroup(ConfigManager.DEFAULT_GROUP, 10, "&9[Default]", "&f", "", null);
			}
		}
	}

	public static Group load(String name) {
		return FragmentPlugin.getFragment().getDatabase().loadGroup(name);
	}

	public static Group getDefaultGroup() {
		return load(ConfigManager.DEFAULT_GROUP);
	}

	public static boolean hasDefaultGroup() {
		if (Objects.equals(ConfigManager.DEFAULT_GROUP, "")) {
			return false;
		}
		return true;
	}

}

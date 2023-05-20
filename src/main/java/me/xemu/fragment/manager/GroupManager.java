package me.xemu.fragment.manager;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.utils.EditOptions;

import java.util.List;

public class GroupManager {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getDatabase();

	public void createGroup(String name, int weight, String prefix, String suffix, String format, List<String> permissions) {
		if (database.loadGroup(name) == null) {
			database.saveGroup(new Group(name, weight, prefix, suffix, format, permissions));
		}
	}

	public void editGroup(String name, String key, Object value) {
		Group group = database.loadGroup(name);
		EditOptions option = EditOptions.getOptionByKey(key);


	}

}

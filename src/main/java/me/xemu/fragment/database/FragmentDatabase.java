package me.xemu.fragment.database;

import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;

import java.util.List;
import java.util.UUID;

public interface FragmentDatabase {
	void load();

	User loadUser(UUID uuid);
	void saveUser(User user);

	Group loadGroup(String name);
	void saveGroup(Group group);

	List<User> loadUsers();
	List<Group> loadGroups();
}
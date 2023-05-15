package me.xemu.fragment.database;

import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface FragmentDatabase {
	String getIdentifier();
	void load();

	User loadUser(UUID uuid);
	User loadUser(Player player);
	void saveUser(User user);
	List<User> getUsers();

	Group loadGroup(String name);
	void saveGroup(Group group);
	List<Group> getGroups();

	boolean exists(User user);
	boolean exists(Group group);
}
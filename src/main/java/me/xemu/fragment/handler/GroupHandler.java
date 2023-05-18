package me.xemu.fragment.handler;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class GroupHandler {
	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private FragmentDatabase db = plugin.getFragmentDatabase();

	public void addGroupToPlayerId(UUID uuid, Group group, boolean save) {
		User user = db.loadUser(uuid);
		user.getGroups().add(group);
		if (save) {
			db.saveUser(user);
		}
	}

	public void addGroupToPlayer(Player player, Group group, boolean save) {
		addGroupToPlayerId(player.getUniqueId(), group, save);
	}

	public void addGroupToPlayers(List<Player> players, Group group, boolean save) {
		players.forEach(p -> addGroupToPlayer(p, group, save));
	}

	public List<Group> getGroupsByPlayerId(UUID uuid) {
		return db.loadUser(uuid).getGroups();
	}

	public List<Group> getGroupsByPlayer(Player player) {
		return getGroupsByPlayerId(player.getUniqueId());
	}

	public void removeGroupFromPlayerId(UUID uuid, Group group, boolean save) {
		User user = db.loadUser(uuid);
		user.getGroups().remove(group);
		if (save) {
			db.saveUser(user);
		}
	}

	public void removeGroupFromPlayer(Player player, Group group, boolean save) {
		removeGroupFromPlayerId(player.getUniqueId(), group, save);
	}

	public Group getGroupByName(String name) {
		if (db.loadGroup(name) == null) return null;
		return db.loadGroup(name);
	}

	public boolean hasGroup(UUID uuid, Group group) {
		return getGroupsByPlayerId(uuid).contains(group);
	}

	public boolean hasGroup(Player player, Group group) {
		return getGroupsByPlayer(player).contains(group);
	}

	public List<Group> getGroupsByPredicate(Predicate<Group> groupPredicate) {
		return db.getGroups().stream().filter(groupPredicate).toList();
	}

	public List<Group> getGroupsByWeight(int weight) {
		return getGroupsByPredicate(g -> g.getWeight() == weight);
	}
}
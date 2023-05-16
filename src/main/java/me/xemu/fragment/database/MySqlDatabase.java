package me.xemu.fragment.database;

import lombok.SneakyThrows;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MySqlDatabase implements FragmentDatabase {

	private final String host;
	private final int port;
	private final String database;
	private final String username;
	private final String password;

	public MySqlDatabase(String host, int port, String database, String username, String password) {
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	@Override
	public String getIdentifier() {
		return "mysql";
	}

	@Override
	public void load() {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			Statement statement = connection.createStatement();
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS users (uuid VARCHAR(36) PRIMARY KEY, groups TEXT, permissions TEXT)");
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS groups (name VARCHAR(100) PRIMARY KEY, weight INT, prefix VARCHAR(100), suffix VARCHAR(100), format VARCHAR(100), permissions TEXT)");
		} catch (SQLException ex) {
			throw new RuntimeException("Failed to connect to MySQL database", ex);
		}
	}

	@Override
	public User loadUser(UUID uuid) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE uuid = ?");
			statement.setString(1, uuid.toString());
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				String groupsString = result.getString("groups");
				String permissionsString = result.getString("permissions");
				List<Group> groups = deserializeGroups(groupsString);
				return new User(uuid, groups, deserializePermissions(permissionsString));
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return null;
	}

	@Override
	public User loadUser(Player player) {
		return loadUser(player.getUniqueId());
	}

	@Override
	public void saveUser(User user) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			String groupsString = serializeGroups(user.getGroups());
			String permissionsString = serializePermissions(user.getPlayerPermissions());
			PreparedStatement statement = connection.prepareStatement("INSERT INTO users (uuid, groups, permissions) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE groups = ?, permissions = ?");
			statement.setString(1, user.getUuid().toString());
			statement.setString(2, groupsString);
			statement.setString(3, permissionsString);
			statement.setString(4, groupsString);
			statement.setString(5, permissionsString);
			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public List<User> getUsers() {
		List<User> users = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM users");
			ResultSet result = statement.executeQuery();

			while (result.next()) {
				UUID uuid = UUID.fromString(result.getString("uuid"));
				String groupsString = result.getString("groups");
				String permissionsString = result.getString("permissions");
				List<Group> groups = deserializeGroups(groupsString);
				users.add(new User(uuid, groups, deserializePermissions(permissionsString)));
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return users;
	}

	@Override
	public Group loadGroup(String name) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			PreparedStatement statement = connection.prepareStatement("SELECT * FROM groups WHERE name = ?");
			statement.setString(1, name);
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				int weight = result.getInt("weight");
				String prefix = result.getString("prefix");
				String suffix = result.getString("suffix");
				String format = result.getString("format");
				String permissionsString = result.getString("permissions");
				List<String> permissions = deserializePermissions(permissionsString);
				return new Group(name, weight, prefix, suffix, format, permissions);
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return null;
	}

	@Override
	public void saveGroup(Group group) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			String permissionsString = serializePermissions(group.getPermissions());
			PreparedStatement statement = connection.prepareStatement("INSERT INTO groups (name, weight, prefix, suffix, format, permissions) VALUES (?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE weight = ?, prefix = ?, suffix = ?, format = ?, permissions = ?");
			statement.setString(1, group.getName());
			statement.setInt(2, group.getWeight());
			statement.setString(3, group.getPrefix());
			statement.setString(4, group.getSuffix());
			statement.setString(5, group.getFormat());
			statement.setString(6, permissionsString);
			statement.setInt(7, group.getWeight());
			statement.setString(8, group.getPrefix());
			statement.setString(9, group.getSuffix());
			statement.setString(10, group.getFormat());
			statement.setString(11, permissionsString);
			statement.executeUpdate();
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public List<Group> getGroups() {
		List<Group> groups = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery("SELECT * FROM groups");

			while (result.next()) {
				String name = result.getString("name");
				int weight = result.getInt("weight");
				String prefix = result.getString("prefix");
				String suffix = result.getString("suffix");
				String format = result.getString("format");
				String permissionsString = result.getString("permissions");
				List<String> permissions = deserializePermissions(permissionsString);
				groups.add(new Group(name, weight, prefix, suffix, format, permissions));
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return groups;
	}

	@Override
	public boolean exists(User user) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM users WHERE uuid = ?");
			statement.setString(1, user.getUuid().toString());
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				int count = result.getInt(1);
				return count > 0;
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return false;
	}

	@Override
	public boolean exists(Group group) {
		try (Connection connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database, username, password)) {
			PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM groups WHERE name = ?");
			statement.setString(1, group.getName());
			ResultSet result = statement.executeQuery();

			if (result.next()) {
				int count = result.getInt(1);
				return count > 0;
			}
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}

		return false;
	}

	private String serializeGroups(List<Group> groups) {
		StringBuilder sb = new StringBuilder();
		for (Group group : groups) {
			sb.append(group.getName()).append(",");
		}
		return sb.toString();
	}

	private List<Group> deserializeGroups(String groupsString) {
		List<Group> groups = new ArrayList<>();
		String[] groupNames = groupsString.split(",");
		for (String groupName : groupNames) {
			Group group = loadGroup(groupName);
			if (group != null) {
				groups.add(group);
			}
		}
		return groups;
	}

	private String serializePermissions(List<String> permissions) {
		StringBuilder sb = new StringBuilder();
		for (String permission : permissions) {
			sb.append(permission).append(",");
		}
		return sb.toString();
	}

	private List<String> deserializePermissions(String permissionsString) {
		List<String> permissions = new ArrayList<>();
		String[] permissionNames = permissionsString.split(",");
		for (String permissionName : permissionNames) {
			permissions.add(permissionName);
		}
		return permissions;
	}
}
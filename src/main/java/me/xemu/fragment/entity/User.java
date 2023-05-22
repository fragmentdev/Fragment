package me.xemu.fragment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.xemu.fragment.FragmentPlugin;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class User {
	private UUID uuid;
	private ArrayList<Group> groups;
	private ArrayList<String> playerPermissions;

	public void loadPermissions() {
		List<String> permissions = groups.stream().flatMap(group -> group.getPermissions().stream()).toList();
		permissions.addAll(playerPermissions); // Add all players permission
		permissions.forEach(permission -> {
			Bukkit.getPlayer(uuid).addAttachment(FragmentPlugin.getFragment(), permission, true);
			Bukkit.getLogger().info(permission);
		});
	}
}

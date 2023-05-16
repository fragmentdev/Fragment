package me.xemu.fragment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.xemu.fragment.FragmentPlugin;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class User {
	private UUID uuid;
	private List<Group> groups;
	private List<String> playerPermissions;

	public void loadPermissions() {
		List<String> permissions = getGroups().stream().flatMap(group -> group.getPermissions().stream()).toList();
		permissions.addAll(playerPermissions); // Add all players permission
		permissions.forEach(permission -> {
			Bukkit.getPlayer(uuid).addAttachment(FragmentPlugin.getInstance(), permission, true);
			Bukkit.getLogger().info(permission);
		});
	}
}

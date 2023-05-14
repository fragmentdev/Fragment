package me.xemu.fragment.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.xemu.fragment.FragmentPlugin;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.PermissionAttachment;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
public class User {
	private UUID uuid;
	private List<Group> groups;

	public void loadPermissions() {
		List<String> permissions = getGroups().stream().flatMap(group -> group.getPermissions().stream()).toList();
		permissions.forEach(permission -> {
			Bukkit.getPlayer(uuid).addAttachment(FragmentPlugin.getFragmentPlugin(), permission, true);
			Bukkit.getLogger().info(permission);
		});
	}
}

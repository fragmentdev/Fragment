package me.xemu.fragment.language;

import de.leonhard.storage.Yaml;
import me.xemu.fragment.FragmentPlugin;
import org.bukkit.ChatColor;

public class Language {

	private static Yaml messages = FragmentPlugin.getInstance().getConfigManager().getMessages();

	public static String NO_PERMISSION = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("NO_PERMISSION", "&cYou do not have the right permissions to do this.")
	);

	public static String INVALID_USAGE = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("INVALID_USAGE", "&cInvalid usage. Check out our help page: /fragment")
	);

	public static String INVALID_TARGET = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("INVALID_TARGET", "&cInvalid target.")
	);

	public static String NO_GROUPS = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("NO_GROUPS", "&cYou do not have any groups..")
	);

	public static String GROUP_ALREADY_EXISTS = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_ALREADY_EXISTS", "&cThis group already exists.")
	);

	public static String GROUP_DOESNT_EXIST = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_DOESNT_EXIST", "&cThis group doesn't exist.")
	);

	public static String GROUP_CREATED = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_CREATED", "&aYou created a new group: <group>")
	);

	public static String GROUP_DELETED = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_DELETED", "&aYou deleted a group: <group>")
	);

	public static String GROUP_EDITED = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_EDITED", "&aYou edited a group: <group> (<key> - <value>)")
	);

	public static String GROUP_EDITED_INVALID_KEY = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_EDITED", "&cCould not edit group. Invalid key provided.")
	);

	public static String GROUP_GRANT_TO_PLAYER = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_GRANT_TO_PLAYER", "&aYou granted the group <group> to the player <player>")
	);

	public static String GROUP_GRANT_REMOVED_FROM_PLAYER = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_GRANT_REMOVED_FROM_PLAYER", "&aYou removed the group <group> from the player <player>")
	);

	public static String GROUP_ALREADY_GRANTED = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_ALREADY_GRANTED", "&cThis group is already granted.")
	);

	public static String GROUP_NOT_GRANTED = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_NOT_GRANTED", "&cThis group is not granted.")
	);

	public static String PERMISSION_ALREADY_GRANTED = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("PERMISSION_ALREADY_GRANTED", "&cThis permission is already granted.")
	);

	public static String PERMISSION_NOT_GRANTED = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("PERMISSION_NOT_GRANTED", "&cThis permission is not granted.")
	);



	public static String GROUP_ADD_PERMISSION = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_ADD_PERMISSION", "&aYou added the permission <permission> to the group <group>")
	);

	public static String GROUP_REMOVE_PERMISSION = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("GROUP_REMOVE_PERMISSION", "&aYou removed the permission <permission> from the group <group>")
	);

	public static String PLAYER_ADD_PERMISSION = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("PLAYER_ADD_PERMISSION", "&aYou added the permission <permission> to the player <player>")
	);

	public static String PLAYER_REMOVE_PERMISSION = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("PLAYER_REMOVE_PERMISSION", "&aYou removed the permission <permission> from the player <player>")
	);

	public static String AN_ERROR_OCCURRED = ChatColor.translateAlternateColorCodes('&',
			messages.getOrSetDefault("AN_ERROR_OCCURRED", "&cAn error occurred while executing the command.")
	);
}
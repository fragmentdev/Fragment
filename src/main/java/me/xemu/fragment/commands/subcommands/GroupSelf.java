package me.xemu.fragment.commands.subcommands;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.utils.Utils;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

// group permission <type> <permission>
public class GroupSelf {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private FragmentDatabase database = plugin.getFragmentDatabase();

	public void execute(Player player) {
		User user = database.loadUser(player.getUniqueId());
		if (user == null) {
			player.sendMessage(Language.INVALID_USAGE);
			return;
		}

		List<Group> groups = user.getGroups();
		if (groups.isEmpty()) {
			player.sendMessage(Language.NO_GROUPS);
			return;
		}

		Optional<Group> highestGroup = groups.stream()
				.max(Comparator.comparingInt(Group::getWeight));

		player.sendMessage(Utils.translate("&7&oYour groups:"));
		if (highestGroup.isPresent()) {
			Group highest = highestGroup.get();
			player.sendMessage(Utils.translate("&7 - &e" + highest.getName() + " (Highest Group)"));
		}

		for (Group group : groups) {
			if (group != highestGroup.orElse(null)) {
				player.sendMessage(Utils.translate("&7 - &f" + group.getName()));
			}
		}
	}

}

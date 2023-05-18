package me.xemu.fragment.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PapiHook extends PlaceholderExpansion {
	@Override
	public @NotNull String getIdentifier() {
		return "fragment";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Xemu and DevScape";
	}

	@Override
	public @NotNull String getVersion() {
		return FragmentPlugin.getInstance().getDescription().getVersion();
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
		User user = FragmentPlugin.getInstance().getFragmentDatabase().loadUser(player.getUniqueId());
		List<Group> groups = user.getGroups();

		if (params.equalsIgnoreCase("groupname")) {
			Group group = Utils.getHeaviestGroup(groups);
			if (group == null) {
				return "No group.";
			}

			return group.getName();
		} else if (params.equalsIgnoreCase("groupprefix")) {
			Group group = Utils.getHeaviestGroup(groups);
			if (group == null) {
				return "No group.";
			}

			return group.getPrefix();
		} else if (params.equalsIgnoreCase("groupsuffix")) {
			Group group = Utils.getHeaviestGroup(groups);
			if (group == null) {
				return "No group.";
			}

			return group.getSuffix();
		} else if (params.equalsIgnoreCase("groupweight")) {
			Group group = Utils.getHeaviestGroup(groups);
			if (group == null) {
				return "No group.";
			}

			return String.valueOf(group.getWeight());
		} else if (params.equalsIgnoreCase("groupformat")) {
			Group group = Utils.getHeaviestGroup(groups);
			if (group == null) {
				return "No group.";
			}

			return group.getFormat();
		}
		return null;
	}
}

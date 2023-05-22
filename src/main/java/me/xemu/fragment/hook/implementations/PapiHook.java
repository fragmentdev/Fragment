package me.xemu.fragment.hook.implementations;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.hook.FragmentHook;
import me.xemu.fragment.hook.FragmentHookData;
import me.xemu.fragment.manager.UserManager;
import me.xemu.fragment.utils.Utils;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@FragmentHookData(required = true, name = "PlaceholderAPI")
public class PapiHook extends PlaceholderExpansion implements FragmentHook {
	@Override
	public String getHookName() {
		return "PlaceholderAPI-Hook";
	}

	@Override
	public void load() {
		this.register();
	}

	@Override
	public @NotNull String getIdentifier() {
		return "fragment";
	}

	@Override
	public @NotNull String getAuthor() {
		return "Xemu";
	}

	@Override
	public @NotNull String getVersion() {
		return FragmentPlugin.getFragment().getDescription().getVersion() + "-papi";
	}

	@Override
	public boolean persist() {
		return true;
	}

	@Override
	public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
		User user = UserManager.load(player.getUniqueId());
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

package me.xemu.fragment.menu.guis.grant;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.entity.User;
import me.xemu.fragment.language.Language;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.menu.guis.GroupsMenu;
import me.xemu.fragment.menu.guis.MainMenu;
import me.xemu.fragment.menu.guis.UsersMenu;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.ArrayList;
import java.util.List;

import static me.xemu.fragment.utils.Utils.deformat;

public class GrantsMenu extends Paged {

	private FragmentPlugin plugin = FragmentPlugin.getFragment();
	private Player target;
	private User targetUser;

	protected List<Group> groups;

	public GrantsMenu(MenuUtil menuUtil, Player target) {
		super(menuUtil);
		this.target = target;
		this.targetUser = plugin.getFragmentDatabase().loadUser(target);
		this.groups = targetUser.getGroups().stream().filter(
				group -> targetUser.getGroups().contains(group)
		).toList();
	}

	@Override
	public String getMenuName() {
		return "Fragment > Grants:";
	}

	@Override
	public int getSlots() {
		return 54;
	}

	@Override
	public void handleMenu(InventoryClickEvent e) {
		Player player = (Player) e.getWhoClicked();
		String displayname = deformat(e.getCurrentItem().getItemMeta().getDisplayName());

		if (displayname.equalsIgnoreCase("Users")) {
			new UsersMenu(FragmentPlugin.getMenuUtil(player)).open();
		} else if (displayname.equalsIgnoreCase("Groups")) {
			new GroupsMenu(FragmentPlugin.getMenuUtil(player)).open();
		} else if (displayname.equalsIgnoreCase("Back")) {
			if (page != 0) {
				page = page - 1;
				super.open();
			} else {
				new MainMenu(FragmentPlugin.getMenuUtil(player)).open();
			}
		} else if (displayname.equalsIgnoreCase("Next")) {
			if (!((index + 1) >= Bukkit.getOnlinePlayers().size())) {
				page = page + 1;
				super.open();
			}
		} else if (displayname.equalsIgnoreCase("Grant Group")) {
			player.closeInventory();
			new GrantMenu(FragmentPlugin.getMenuUtil(player), target).open();
		} else if (groups.stream().map(m -> m.getName()).toList().contains(displayname)) {
			Group group = plugin.getFragmentDatabase().loadGroup(displayname);
			plugin.getGroupHandler().removeGroupFromPlayer(target, group, true);
			player.closeInventory();

			String message = Language.GROUP_GRANT_REMOVED_FROM_PLAYER;
			message = message.replaceAll("<group>", group.getName()).replaceAll("<player>", target.getName());

			player.sendMessage(message);
		}

		groups.forEach(g -> {
			if (displayname.equalsIgnoreCase(g.getName())) {
				User t = plugin.getFragmentDatabase().loadUser(target);
				t.getGroups().removeIf(predicate -> predicate.getName().equalsIgnoreCase(g.getName()));
				plugin.getFragmentDatabase().saveUser(t);

				player.closeInventory();
				String message = Language.GROUP_GRANT_REMOVED_FROM_PLAYER
						.replace("<group>", g.getName())
						.replace("<player>", target.getName());

				player.sendMessage(message);
			}
		});

			//plugin.loadDatabase();
	}

	@Override
	public void setMenuItems() {
		applyLayout(false);

		if (!groups.isEmpty()) {
			int maxItemsPerPage = 24;
			int startIndex = page * maxItemsPerPage;
			int endIndex = Math.min(startIndex + maxItemsPerPage, groups.size());

			for (int i = startIndex; i < endIndex; i++) {
				Group g = groups.get(i);
				if (g == null) continue;

				List<String> lore = new ArrayList<>();
				lore.add(Utils.translate("&7Weight: &f" + g.getWeight()));
				lore.add(Utils.translate("&7Permissions: &f" + g.getPermissions().size()));
				lore.add(Utils.translate("&7Prefix: &f" + g.getPrefix()));
				lore.add(Utils.translate("&7Suffix: &f" + g.getSuffix()));
				lore.add(Utils.translate("&7Format: &f" + g.getFormat()));
				lore.add(Utils.translate("&aClick to remove grant from " + target.getName()));
				getInventory().addItem(makeItem(Material.CHEST, "&e&n" + g.getName(), lore));

				getInventory().setItem(53, makeItem(Material.GREEN_BANNER, "&aGrant Group"));
			}
		} else {
			getInventory().setItem(24, makeItem(Material.BARRIER, "&cNo grantable groups."));
		}
	}
}
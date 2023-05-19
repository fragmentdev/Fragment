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

public class GrantMenu extends Paged {

	private FragmentPlugin plugin = FragmentPlugin.getInstance();
	private Player target;
	private User targetUser;

	protected List<Group> groups;

	public GrantMenu(MenuUtil menuUtil, Player target) {
		super(menuUtil);
		this.target = target;
		this.targetUser = plugin.getFragmentDatabase().loadUser(target);

		this.groups = plugin.getFragmentDatabase().getGroups().stream().filter(group -> !targetUser.getGroups().contains(group)).toList();
	}

	@Override
	public String getMenuName() {
		return "Fragment > Grant Player:";
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
		}

		groups.forEach(g -> {
			if (displayname.equalsIgnoreCase(g.getName())) {
				User t = plugin.getFragmentDatabase().loadUser(target);
				if (t.getGroups().contains(g)) {
					player.closeInventory();
					player.sendMessage(Language.GROUP_ALREADY_GRANTED);
				}
				t.getGroups().add(g);
				plugin.getFragmentDatabase().saveUser(t);

				player.closeInventory();
				player.sendMessage(Language.GROUP_GRANT_TO_PLAYER.replace("<group>", g.getName().replace("<player>", target.getName())));
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
				lore.add(Utils.translate("&aClick to grant to " + target.getName()));
				getInventory().addItem(makeItem(Material.CHEST, "&e&n" + g.getName(), lore));
			}
		} else {
			getInventory().setItem(21, makeItem(Material.BARRIER, "&cNo available groups.", "§7The user most likely has all groups granted already."));
		}
	}
}
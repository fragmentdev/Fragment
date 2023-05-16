package me.xemu.fragment.menu.guis.editgroup;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.menu.guis.GroupsMenu;
import me.xemu.fragment.menu.guis.MainMenu;
import me.xemu.fragment.menu.guis.UsersMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

import static me.xemu.fragment.utils.Utils.deformat;

public class PermissionsEditMenu extends Paged {

    List<String> groupPermissions = new ArrayList<>();

    public PermissionsEditMenu(MenuUtil menuUtil) {
        super(menuUtil);

        if (!groupPermissions.isEmpty()) groupPermissions.clear();

        if (FragmentPlugin.getInstance().getFragmentDatabase().loadGroup(menuUtil.getGroup()) != null) {
            groupPermissions = FragmentPlugin.getInstance().getFragmentDatabase().loadGroup(menuUtil.getGroup()).getPermissions();
        } else {
            groupPermissions = new ArrayList<>();
        }
    }

    @Override
    public String getMenuName() {
        return "Fragment > " + menuUtil.getGroup() + ": Perms";
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
                new GroupsMenu(FragmentPlugin.getMenuUtil(player)).open();
            }
        } else if (displayname.equalsIgnoreCase("Next")) {
            if (!((index + 1) >= groupPermissions.size())) {
                page = page + 1;
                super.open();
            }
        } else if (displayname.equalsIgnoreCase("Settings")) {
            e.setCancelled(true);
        } else if (displayname.equalsIgnoreCase("About Fragment")) {
            e.setCancelled(true);
        } else {
            String perm = displayname;

            Group group = FragmentPlugin.getInstance().getFragmentDatabase().loadGroup(menuUtil.getGroup());

            if (!group.getPermissions().contains(perm)) {
                group.getPermissions().add(perm);
                super.open();
            } else {
                group.getPermissions().remove(perm);
                super.open();
            }
        }
    }

    @Override
    public void setMenuItems() {
        applyLayout(true);

        if (!getAllPermissions().isEmpty()) {
            int maxItemsPerPage = 24;
            int startIndex = page * maxItemsPerPage;
            int endIndex = Math.min(startIndex + maxItemsPerPage, getAllPermissions().size());

            for (int i = startIndex; i < endIndex; i++) {
                String perm = getAllPermissions().get(i);
                if (perm == null) continue;

                if (groupPermissions.contains(perm)) {
                    getInventory().addItem(makeItem(Material.LIME_DYE, "&a" + perm));
                } else {
                    getInventory().addItem(makeItem(Material.GRAY_DYE, "&7" + perm));
                }
            }
        }
    }

    public static List<String> getAllPermissions() {
        List<String> permissions = new ArrayList<>();

        for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
            permissions.addAll(permission.getChildren().keySet());
        }

        return permissions;
    }
}
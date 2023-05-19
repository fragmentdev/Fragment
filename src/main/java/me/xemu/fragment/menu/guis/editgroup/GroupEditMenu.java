package me.xemu.fragment.menu.guis.editgroup;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.menu.guis.GroupsMenu;
import me.xemu.fragment.menu.guis.SettingsMenu;
import me.xemu.fragment.menu.guis.UsersMenu;
import me.xemu.fragment.utils.Interaction;
import me.xemu.fragment.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.xemu.fragment.utils.Utils.deformat;

public class GroupEditMenu extends Paged {

    private FragmentPlugin plugin = FragmentPlugin.getInstance();
    private FragmentDatabase database = plugin.getFragmentDatabase();

    private Group group;
    

    public GroupEditMenu(MenuUtil menuUtil) {
        super(menuUtil);

        if (database.loadGroup(menuUtil.getGroup()) != null) {
            group = database.loadGroup(menuUtil.getGroup());
        }

    }

    @Override
    public String getMenuName() {
        return "Fragment > " + menuUtil.getGroup() + ": Edit";
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
            e.setCancelled(true);
        } else if (displayname.equalsIgnoreCase("Settings")) {
            new SettingsMenu(FragmentPlugin.getMenuUtil(player)).open();
        } else if (displayname.equalsIgnoreCase("Group Prefix")) {
            Interaction interaction = new Interaction(plugin);
            player.closeInventory();
            interaction.startInteraction(player, "Group Prefix", this, FragmentPlugin.getMenuUtil(player, group.getName())).thenAccept(interactionAccept -> {
                Group group = database.loadGroup(menuUtil.getGroup());
                group.setPrefix(interactionAccept);
                database.saveGroup(group);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    // Reopen the GUI
                    reOpenMenu(player);
                });
            });
        } else if (displayname.equalsIgnoreCase("Group Suffix")) {
            Interaction interaction = new Interaction(plugin);
            player.closeInventory();
            interaction.startInteraction(player, "Group Suffix", this, FragmentPlugin.getMenuUtil(player, group.getName())).thenAccept(interactionAccept -> {
                Group group = database.loadGroup(menuUtil.getGroup());
                group.setSuffix(interactionAccept);
                database.saveGroup(group);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    // Reopen the GUI
                    reOpenMenu(player);
                });
            });
        } else if (displayname.equalsIgnoreCase("Group Format")) {
            Interaction interaction = new Interaction(plugin);
            player.closeInventory();
            interaction.startInteraction(player, "Group Format", this, FragmentPlugin.getMenuUtil(player, group.getName())).thenAccept(interactionAccept -> {
                Group group = database.loadGroup(menuUtil.getGroup());
                group.setFormat(interactionAccept);
                database.saveGroup(group);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    // Reopen the GUI
                    reOpenMenu(player);
                });
            });
        } else if (displayname.equalsIgnoreCase("Group Weight")) {
            Interaction interaction = new Interaction(plugin);
            player.closeInventory();
            interaction.startInteraction(player, "Group Weight (Only number)", this, FragmentPlugin.getMenuUtil(player, group.getName())).thenAccept(interactionAccept -> {
                Group group = database.loadGroup(menuUtil.getGroup());
                if (Utils.isNumber(interactionAccept)) {
                    group.setWeight(Integer.parseInt(interactionAccept));
                } else {
                    player.sendMessage(ChatColor.RED + "You provided a String instead of an number. Weight did not change.");
                    return;
                }
                database.saveGroup(group);
                Bukkit.getScheduler().runTask(plugin, () -> {
                    // Reopen the GUI
                    reOpenMenu(player);
                });
            });
        } else if (displayname.equalsIgnoreCase("Manage Permissions")) {
            new PermissionsEditMenu(FragmentPlugin.getMenuUtil(player, group.getName())).open();
        }
    }

    @Override
    public void setMenuItems() {
        applyLayout(false);

        getInventory().setItem(21, makeItem(Material.ANVIL, "&aGroup Prefix", "§7Current Prefix: §f" + group.getPrefix()));
        getInventory().setItem(22, makeItem(Material.ANVIL, "&aGroup Suffix", "§7Current Suffix: §f" + group.getSuffix()));
        getInventory().setItem(23, makeItem(Material.ANVIL, "&aGroup Format", "§7Current Format: §f" + group.getFormat()));
        getInventory().setItem(24, makeItem(Material.ANVIL, "&aGroup Weight", "§7Current Weight: §f" + group.getWeight()));
        getInventory().setItem(30, makeItem(Material.BARRIER, "&aManage Permissions", "§7Click to manage group permissions."));
    }
    
    private void reOpenMenu(Player player) {
        new GroupEditMenu(plugin.getMenuUtil(player, menuUtil.getGroup())).open();
    }

}
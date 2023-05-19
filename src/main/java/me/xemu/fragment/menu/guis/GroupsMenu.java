package me.xemu.fragment.menu.guis;

import de.tr7zw.nbtapi.NBTItem;
import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.Group;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import me.xemu.fragment.menu.guis.editgroup.GroupEditMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

import static me.xemu.fragment.utils.Utils.*;

public class GroupsMenu extends Paged {

    List<Group> groups = new ArrayList<>();

    public GroupsMenu(MenuUtil menuUtil) {
        super(menuUtil);

        if (!groups.isEmpty()) groups.clear();
        groups = FragmentPlugin.getInstance().getFragmentDatabase().getGroups();
    }

    @Override
    public String getMenuName() {
        return "Fragment > Groups:";
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        String displayname = deformat(e.getCurrentItem().getItemMeta().getDisplayName());

        NBTItem item = new NBTItem(e.getCurrentItem());

        if (item.hasNBTData() && e.getCurrentItem().getType() == Material.CHEST) {
            String group = item.getString("group");

            new GroupEditMenu(FragmentPlugin.getMenuUtil(player, group)).open();
            menuUtil.setGroup(group);
        }

        if (displayname.equalsIgnoreCase("Users")) {
            new UsersMenu(FragmentPlugin.getMenuUtil(player)).open();
        } else if (displayname.equalsIgnoreCase("Groups")) {
            new GroupsMenu(FragmentPlugin.getMenuUtil(player)).open();
        } else if (displayname.equalsIgnoreCase("Settings")) {
            new SettingsMenu(FragmentPlugin.getMenuUtil(player)).open();
        } else if (displayname.equalsIgnoreCase("Back")) {
            if (page != 0) {
                page = page - 1;
                super.open();
            } else {
                new MainMenu(FragmentPlugin.getMenuUtil(player)).open();
            }
        } else if (displayname.equalsIgnoreCase("Next")) {
            if (!((index + 1) >= groups.size())) {
                page = page + 1;
                super.open();
            }
        } else {
            e.setCancelled(true);
        }
    }

    @Override
    public void setMenuItems() {
        applyLayout(true);

        if (!groups.isEmpty()) {
            int maxItemsPerPage = 24;
            int startIndex = page * maxItemsPerPage;
            int endIndex = Math.min(startIndex + maxItemsPerPage, groups.size());

            for (int i = startIndex; i < endIndex; i++) {
                Group group = groups.get(i);
                if (group == null) continue;

                ItemStack item = new ItemStack(Material.CHEST, 1);
                ItemMeta meta = item.getItemMeta();
                assert meta != null;

                NBTItem nbt = new NBTItem(item);

                List<String> lore = new ArrayList<>();
                lore.add("&7Weight: &f" + group.getWeight());
                lore.add("&7Prefix: " + group.getPrefix());
                lore.add("&7Suffix: " + group.getSuffix());
                lore.add("&7Permissions: (" + group.getPermissions().size() + ")");
                lore.add("");
                lore.add("&eClick to manage group!");

                nbt.setString("group", group.getName());

                meta.setDisplayName(translate("&e&n" + group.getName()));
                meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                meta.addItemFlags(ItemFlag.HIDE_DYE);
                meta.addItemFlags(ItemFlag.HIDE_DESTROYS);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                meta.setLore(color(lore));

                nbt.getItem().setItemMeta(meta);

                nbt.setString("group", group.getName());
                inventory.addItem(nbt.getItem());
            }
        }
    }
}
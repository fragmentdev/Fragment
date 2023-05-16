package me.xemu.fragment.menu;

import org.bukkit.Material;

public abstract class Paged extends Menu {

    protected int page = 0;
    protected int maxItems = 35;
    protected int index = 0;

    public Paged(MenuUtil menuUtil) {
        super(menuUtil);
    }

    protected int getPage() {
        return page + 1;
    }

    public int getMaxItems() {
        return maxItems;
    }

    public void applyLayout(boolean page) {
        // layout function

        // NEXT, BACK
        if (page) {
            getInventory().setItem(48, makeItem(Material.ARROW, "&f&lBack"));
            getInventory().setItem(50, makeItem(Material.ARROW, "&f&lNext"));
        } else {
            getInventory().setItem(48, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
            getInventory().setItem(50, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        }

        // CATEGORY ITEMS
        getInventory().setItem(9, makeItem(Material.PLAYER_HEAD, "&f&lUsers"));
        getInventory().setItem(18, makeItem(Material.CHEST, "&f&lGroups"));
        getInventory().setItem(27, makeItem(Material.REPEATER, "&f&lSettings"));
        getInventory().setItem(36, makeItem(Material.BOOK, "&f&lAbout Fragment"));

        /// GLASS ITEMS
        getInventory().setItem(0, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(2, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(3, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(4, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(5, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(6, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(7, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(8, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));

        getInventory().setItem(17, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(26, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(35, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(44, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(45, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(47, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(49, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(51, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(52, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));
        getInventory().setItem(53, makeItem(Material.GRAY_STAINED_GLASS_PANE, ""));

        getInventory().setItem(1, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        getInventory().setItem(10, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        getInventory().setItem(19, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        getInventory().setItem(28, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        getInventory().setItem(37, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));
        getInventory().setItem(46, makeItem(Material.WHITE_STAINED_GLASS_PANE, ""));


    }
}
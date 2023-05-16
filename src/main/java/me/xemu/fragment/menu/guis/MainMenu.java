package me.xemu.fragment.menu.guis;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.menu.MenuUtil;
import me.xemu.fragment.menu.Paged;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import static me.xemu.fragment.utils.Utils.deformat;

public class MainMenu extends Paged {

    private FragmentPlugin plugin = FragmentPlugin.getInstance();

    public MainMenu(MenuUtil menuUtil) {
        super(menuUtil);
    }

    @Override
    public String getMenuName() {
        return "Fragment > Main Menu:";
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
            if (!((index + 1) >= Bukkit.getOnlinePlayers().size())) {
                page = page + 1;
                super.open();
            }
        } else {
            e.setCancelled(true);
        }
    }

    @Override
    public void setMenuItems() {
        applyLayout(false);

        getInventory().setItem(21, makeItem(Material.PAPER, "&aPlugin Version", "§7Version: §a" + plugin.getDescription().getVersion()));
        getInventory().setItem(22, makeItem(Material.PAPER, "&aDatabase Integration", "§7Integration: §a" + plugin.getFragmentDatabase().getIdentifier()));
        getInventory().setItem(23, makeItem(Material.PAPER, "&aAuthors", "§a" + plugin.getDescription().getAuthors()));
        getInventory().setItem(24, makeItem(Material.PAPER, "&aWebhook Enabled", "§a" + plugin.WEBHOOK_ENABLED));
        getInventory().setItem(30, makeItem(Material.PAPER, "&aLanguage", "§a" + "English / Custom"));
        getInventory().setItem(31, makeItem(Material.PAPER, "&aConfigs", "§a" + "Created"));
    }
}
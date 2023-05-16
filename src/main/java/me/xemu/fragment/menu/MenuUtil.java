package me.xemu.fragment.menu;

import org.bukkit.entity.Player;

public class MenuUtil {

    private Player owner;
    private String group;

    public MenuUtil(Player owner) {
        this.owner = owner;
    }

    // for group editor area.
    public MenuUtil(Player owner, String group) {
        this.owner = owner;
        this.group = group;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
}
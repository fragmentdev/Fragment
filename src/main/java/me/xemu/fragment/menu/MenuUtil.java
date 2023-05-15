package me.xemu.fragment.menu;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.entity.User;
import org.bukkit.entity.Player;

public class MenuUtil {

    private Player owner;
    private User user;

    public MenuUtil(Player owner) {
        this.owner = owner;
        this.user = FragmentPlugin.getFragmentPlugin().getFragmentDatabase().loadUser(owner.getUniqueId());
    }

    public Player getOwner() {
        return owner;
    }

    public User getUser() {
        return user;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

}
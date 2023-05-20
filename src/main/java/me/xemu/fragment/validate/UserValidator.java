package me.xemu.fragment.validate;

import me.xemu.fragment.FragmentPlugin;
import me.xemu.fragment.database.FragmentDatabase;

public class UserValidator implements Runnable {
    private FragmentDatabase db = FragmentPlugin.getFragment().getFragmentDatabase();

    @Override
    public void run() {
        db.getUsers().forEach(user -> {
            user.getGroups().forEach(group -> {

            });
        });
    }
}

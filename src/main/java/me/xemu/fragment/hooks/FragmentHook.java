package me.xemu.fragment.hooks;

public interface FragmentHook {

    String getPluginName();

    boolean requiresPlugin();

    void registerHook();

}


package com.haze47.mergeit;

import org.bukkit.plugin.java.JavaPlugin;

public class MergeItPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("MergeIt Plugin Enabled!");
        getServer().getPluginManager().registerEvents(new MergeListener(), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("MergeIt Plugin Disabled!");
    }
}

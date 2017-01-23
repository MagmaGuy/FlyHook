package com.magmaguy.flyhook;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public final class FlyHook extends JavaPlugin implements Listener {

    //Determine behaviour on startup
    @Override
    public void onEnable() {

        getLogger().info("FlyHook - enabled!");

        //Load settings from config
        loadConfiguration();

        //Hook up all listeners, some depending on config
        this.getServer().getPluginManager().registerEvents(this, this);

        //Essential listeners
        this.getServer().getPluginManager().registerEvents(new HookHandler(this), this);

        //Config-based listeners
        if (getConfig().getBoolean("Flying 2x critical hits") == true) {
            this.getServer().getPluginManager().registerEvents(new FlyingCrits(this), this);
        }
        if (getConfig().getBoolean("Dragon hijacking") == true) {
            this.getServer().getPluginManager().registerEvents(new DragonHijack(this), this);
        }
        if (getConfig().getBoolean("Slow elytra flight by sneaking")) {
            this.getServer().getPluginManager().registerEvents(new SlowRide(this), this);
        }


    }

    //Determine behaviour on shutdown
    @Override
    public void onDisable() {

        getLogger().info("FlyHook - shutting down!");

    }

    public void FlyHook() {

    }

    public void loadConfiguration() {

        //check defaults
        getConfig().addDefault("Flying 2x critical hits", true);
        getConfig().addDefault("Dragon hijacking", true);
        getConfig().addDefault("Slow elytra flight by sneaking", true);
        getConfig().options().copyDefaults(true);
        saveConfig();

        getLogger().info("FlyHook config loaded!");

    }

    public void reloadConfiguration() {

        reloadConfig();

        getLogger().info("FlyHook config reloaded!");

    }

}

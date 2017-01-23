package com.magmaguy.flyhook;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getLogger;

/**
 * Created by MagmaGuy on 23/01/2017.
 */
public class SlowRide implements Listener{

    private FlyHook plugin;

    public SlowRide (Plugin plugin)
    {

        this.plugin = (FlyHook) plugin;

    }

    private int processID;

    @EventHandler
    public void slowRide (PlayerToggleSneakEvent event)
    {

        Player player = event.getPlayer();

        if (player.isGliding())
        {

            new BukkitRunnable()
            {

                int counter;

                @Override
                public void run() {

                    if (player.isValid())
                    {

                        player.setVelocity(player.getVelocity().multiply(0.5));

                        counter++;

                        getLogger().info("" + counter);

                        if (counter == 20 * 2)
                        {

                            this.cancel();
                            getLogger().info("cancelling" + counter);

                        }

                    } else {

                        this.cancel();

                    }

                }

            }.runTaskTimer(plugin, 0L, 1L);

        }

    }

}

package com.magmaguy.flyhook;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by MagmaGuy on 23/01/2017.
 */
public class FlyingCrits implements Listener{

    private FlyHook plugin;

    public FlyingCrits (Plugin plugin)
    {

        this.plugin = (FlyHook) plugin;

    }

    @EventHandler
    public void doubleDamage (EntityDamageByEntityEvent event)
    {

        Entity entity = event.getDamager();

        if (entity instanceof Player)
        {

            Player player = (Player) entity;

            if (player.isGliding())
            {

                event.setDamage(event.getDamage() * 2);

                player.sendTitle("", "§c2x Damage!");

            }

        }

        if (entity instanceof Arrow)
        {

            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player)
            {

                Player player = (Player) arrow.getShooter();

                if (player.isGliding())
                {

                    event.setDamage(event.getDamage() * 2);

                    player.sendTitle("", "§c2x Damage!");

                }

            }

        }

    }

}

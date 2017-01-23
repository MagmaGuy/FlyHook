package com.magmaguy.flyhook;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

/**
 * Created by MagmaGuy on 23/01/2017.
 */
public class FlyingCrits implements Listener {

    private FlyHook plugin;

    public FlyingCrits(Plugin plugin) {

        this.plugin = (FlyHook) plugin;

    }

    @EventHandler
    public void doubleDamage(EntityDamageByEntityEvent event) {

        Entity entity = event.getDamager();

        if (entity instanceof Player) {

            Player player = (Player) entity;

            if (player.isGliding() && event.getEntity() instanceof LivingEntity) {

                LivingEntity livingEntity = (LivingEntity) event.getEntity();

                double currentHealth = (livingEntity.getHealth());
                double critBonus = event.getFinalDamage() * 2;

                if (currentHealth - critBonus >= 0)
                {

                    event.setCancelled(true);
                    livingEntity.setHealth(livingEntity.getHealth() - critBonus);

                } else {

                    event.setCancelled(true);
                    livingEntity.setHealth(0);

                }

                player.sendTitle("", "§c2x Damage!");

            }

        }

        if (entity instanceof Arrow) {

            Arrow arrow = (Arrow) event.getDamager();

            if (arrow.getShooter() instanceof Player) {

                Player player = (Player) arrow.getShooter();

                if (player.isGliding()) {

                    event.setDamage(event.getDamage() * 2);

                    player.sendTitle("", "§c2x Damage!");

                }

            }

        }

    }

}

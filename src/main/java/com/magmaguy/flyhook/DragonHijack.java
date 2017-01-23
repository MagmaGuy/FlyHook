package com.magmaguy.flyhook;

import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;
import static org.bukkit.Material.FISHING_ROD;

/**
 * Created by MagmaGuy on 23/01/2017.
 */
public class DragonHijack implements Listener {

    private FlyHook plugin;

    public DragonHijack(Plugin plugin) {

        this.plugin = (FlyHook) plugin;

    }


    @EventHandler
    public void dragonHijack(EntityDamageByEntityEvent event) {


        //apply critical damage and dismount
        if (event.getEntityType().equals(EntityType.ENDER_DRAGON) && event.getDamager() instanceof Player
                || event.getEntityType().equals(EntityType.ENDER_DRAGON) && event.getDamager() instanceof Arrow) {

            LivingEntity dragonLivingEntity = (LivingEntity) event.getEntity();

            if (event.getDamager() instanceof Player || ((Arrow) event.getDamager()).getShooter() instanceof Player) {

                if (dragonLivingEntity.getPassengers().size() > 0) {

                    double critBonus = event.getFinalDamage() * 8;

                    double currentHealth = dragonLivingEntity.getHealth();

                    if (currentHealth - critBonus >= 0)
                    {

                        event.setCancelled(true);
                        dragonLivingEntity.setHealth(dragonLivingEntity.getHealth() - critBonus);

                    } else {

                        event.setCancelled(true);
                        dragonLivingEntity.setHealth(0);

                    }

                    Player player = (Player) event.getEntity().getPassengers().get(0);

                    String subtitle = "§4Hijack crit, " + critBonus + " damage!";

                    player.sendTitle("", subtitle);

                    dragonLivingEntity.removePassenger(player);
                    player.setGliding(true);

                    if (dragonLivingEntity.getHealth() == 0) {

                        getServer().broadcastMessage("§5" + player.getDisplayName() + " §6has hooked unto the §cEnder Dragon§6, " +
                                "latched to its back and §4struck it down from the skies§6!");

                    }

                }

            }

        }

        //mount the dragon
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();

            if (event.getEntityType().equals(EntityType.ENDER_DRAGON)) {
                Entity dragonEntity = event.getEntity();

                if (player.getEquipment().getItemInMainHand().getType().equals(FISHING_ROD) ||
                        player.getEquipment().getItemInOffHand().getType().equals(FISHING_ROD)) {

                    //no way currently of doing it without deprecation
                    dragonEntity.setPassenger(player);

                }

            }

        }

    }

}

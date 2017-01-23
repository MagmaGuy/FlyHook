package com.magmaguy.flyhook;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Material.FISHING_ROD;

/**
 * Created by MagmaGuy on 23/01/2017.
 */
public class DragonHijack implements Listener{

    private FlyHook plugin;

    public DragonHijack (Plugin plugin)
    {

        this.plugin = (FlyHook) plugin;

    }

    //Allow players to mount an enderdragon if they have a fishing hook
    @EventHandler
    public void dragonHijack(EntityDamageByEntityEvent event){

        if(event.getDamager() instanceof Player)
        {
            Player player = (Player) event.getDamager();

            if(event.getEntityType().equals(EntityType.ENDER_DRAGON))
            {
                Entity dragonEntity = event.getEntity();

                if(player.getEquipment().getItemInMainHand().getType().equals(FISHING_ROD)||
                        player.getEquipment().getItemInOffHand().getType().equals(FISHING_ROD))
                {

                    //no way currently of doing it without deprecation
                    dragonEntity.setPassenger(player);

                }

            }

        }

    }

    @EventHandler
    public void damageCrits(EntityDamageEvent event){

        Entity entity  = event.getEntity();

        if((event.getEntityType().equals(EntityType.ENDER_DRAGON)))
        {

            LivingEntity dragonLivingEntity = (LivingEntity) entity;


            if(dragonLivingEntity.getPassengers() != null)
            {

                double critBonus = event.getDamage() * 4;
                dragonLivingEntity.damage(critBonus);

                Player player = (Player) entity.getPassengers();

                String subtitle = "§4Dragon crit! " + critBonus + " damage!";

                player.sendTitle("", subtitle);

                dragonLivingEntity.removePassenger(player);
                player.setGliding(true);

            }

        }

    }

}

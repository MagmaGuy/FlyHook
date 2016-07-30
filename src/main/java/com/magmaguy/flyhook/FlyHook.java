package com.magmaguy.flyhook;

import java.util.Set;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import static org.bukkit.event.player.PlayerFishEvent.State.FISHING;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.logging.Level;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.player.PlayerQuitEvent;
import static java.lang.Math.abs;
import static org.bukkit.Material.FISHING_ROD;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;


public final class FlyHook  extends JavaPlugin implements Listener{
    
    Location targettedBlock;
    Material blockMaterial;
    ArrayList<String> playerList = new ArrayList(175);
    ArrayList<Vector> vectorBlock = new ArrayList(175);
    
    //Determine behaviour on startup
    @Override
    public void onEnable(){
        
        getLogger().info("FlyHook - enabled!");
        this.getServer().getPluginManager().registerEvents(this, this);
        
    }
    
    //Determine behaviour on shutdown
    @Override
    public void onDisable(){
        
        getLogger().info("FlyHook - shutting down!");
        
    }
    
    public void FlyHook(){

    }
    
    ItemStack playerItem;
    
    @EventHandler
    public void Grapple(PlayerFishEvent event)
    {      
        //Select player and hook
        Player player = event.getPlayer();
        FishHook hook = event.getHook();
        
        //Check what the player is holding
        ItemStack playerItemRight = player.getEquipment().getItemInMainHand();
        ItemStack playerItemLeft = player.getEquipment().getItemInOffHand();
        
        if(playerItemRight.getType().equals(FISHING_ROD))
        {
            
            playerItem = playerItemRight;
            
        } else if (playerItemLeft.getType().equals(FISHING_ROD))
        {
            
            playerItem = playerItemLeft;
            
        }
        
        //Block detection
        //Check if the item is a grapppling hook
        if (playerItem.getItemMeta().hasDisplayName())
        {
            
            //Find if player is gliding
            boolean flyingBoolean = player.isGliding();
            
            //If they are, proceed
            if (flyingBoolean)
            {
                //If the player already has hook data, use that
                if (targettedBlock != null && blockMaterial != Material.AIR && playerList.contains(player.getName()))
                {
                        int index = playerList.indexOf(player.getName());
                        
                        //Vector math
                        //Applies if the block detection occurred correctly
                        Vector toHookVector = vectorBlock.get(index).subtract(player.getLocation().toVector());
                        Vector toHookVectorCopy = toHookVector;

                        toHookVector = toHookVector.normalize();

                        toHookVector.multiply(new Vector(1.0, 1.0, 1.0));

                        double newXVector = abs(toHookVector.getX()) * toHookVectorCopy.getX() + player.getVelocity().getX();
                        double newYVector = abs(toHookVector.getY()) * toHookVectorCopy.getY() + player.getVelocity().getY();
                        double newZVector = abs(toHookVector.getZ()) * toHookVectorCopy.getZ() + player.getVelocity().getZ();

                        toHookVectorCopy.setX(newXVector);
                        toHookVectorCopy.setY(newYVector);
                        toHookVectorCopy.setZ(newZVector);

                        player.setVelocity(toHookVectorCopy);

                        targettedBlock = null;
                        
                        //Scrub used data
                        playerList.remove(index);
                        vectorBlock.remove(index);
                        
                        getLogger().log(Level.INFO, "New userlist length: {0}", playerList.size());
                        
                        
                }
                
                //This is the first throw of the fishing line
                if (event.getState() == FISHING)
                {
                    //Check which block the player has targetted
                    targettedBlock = player.getTargetBlock((Set<Material>) null, 32).getLocation();
                    blockMaterial = player.getTargetBlock((Set<Material>) null, 32).getType();
                    
                    //Teleport hook to the targetted location
                    hook.teleport(targettedBlock);
                
                }
                
                //This logs relevant data about the throw for the second part which is first in the code
                if (targettedBlock != null && blockMaterial != Material.AIR)
                {
                    //Visual cue to let people know they landed the hit
                    player.getWorld().playEffect(player.getLocation(), Effect.FIREWORKS_SPARK, null);
                    
                    //Checks if there are existing entries for the player casting the line, deletes them
                    if(playerList.contains(player.getName()))
                    {
                        int index = playerList.indexOf(player.getName());
                        
                        playerList.remove(index);
                        vectorBlock.remove(index);
                    }
                    
                    //logs the relevant info for use in the very first part
                    playerList.add(player.getName());
                    vectorBlock.add(targettedBlock.toVector());
                }
                
            }
                
        }
        
    }
    
    //Make sure players that quit don't accidentally leave info stored in the lists
    @EventHandler
    public void Quit(PlayerQuitEvent event){
        
        Player player = event.getPlayer();
        
        if (playerList != null)
        {
            if (playerList.contains(player.getName()))
            {
                int index = playerList.indexOf(player.getName());
                
                playerList.remove(index);
                vectorBlock.remove(index);
                
            }
        }
        
    }
    
    //Allow players to mount an enderdragon if they have a fishing hook, to be replaced with drop
    @EventHandler
    public void DragonHijack(EntityDamageByEntityEvent event){
        
        if(event.getDamager() instanceof Player)
        {
            Player player = (Player) event.getDamager();
            
             if(event.getEntityType().equals(EntityType.ENDER_DRAGON))
             {
                 Entity dragonEntity = event.getEntity();
                 
                 if(player.getEquipment().getItemInMainHand().getType().equals(FISHING_ROD)||
                         player.getEquipment().getItemInOffHand().getType().equals(FISHING_ROD))
                 {
                     
                     dragonEntity.setPassenger(player);
                     
                 }
                 
             }
             
        }
        
    }
    
    @EventHandler
    public void damageCrits(EntityDamageEvent event){
        
        Entity potentialDragon  = event.getEntity();
        
        if((event.getEntityType().equals(EntityType.ENDER_DRAGON)))
        {
            
            LivingEntity dragonLivingEntity = (LivingEntity) potentialDragon;

            
            if(dragonLivingEntity.getPassenger() != null)
            {
                
                double critBonus = dragonLivingEntity.getLastDamage() * 4;
                dragonLivingEntity.damage(critBonus);
                
                Player player = (Player) potentialDragon.getPassenger();
                
                player.sendRawMessage("Dragon crit! " + critBonus + " damage!");
                
                //You don't want to know.
                player.setInvulnerable(false);
                
            }
            
        }
        
    }
    
}

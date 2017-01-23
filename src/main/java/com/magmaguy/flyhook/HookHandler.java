package com.magmaguy.flyhook;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Set;

import static java.lang.Math.abs;
import static org.bukkit.Material.FISHING_ROD;
import static org.bukkit.event.player.PlayerFishEvent.State.FISHING;

/**
 * Created by MagmaGuy on 23/01/2017.
 */
public class HookHandler implements Listener {

    private FlyHook plugin;

    public HookHandler(Plugin plugin) {

        this.plugin = (FlyHook) plugin;

    }

    Location targettedBlock;
    Material blockMaterial;
    ArrayList<String> playerList = new ArrayList(175);
    ArrayList<Vector> vectorBlock = new ArrayList(175);

    private ItemStack playerItem;

    @EventHandler
    public void Grapple(PlayerFishEvent event) {
        //Select player and hook
        Player player = event.getPlayer();
        FishHook hook = event.getHook();

        //Check what the player is holding
        ItemStack playerItemRight = player.getEquipment().getItemInMainHand();
        ItemStack playerItemLeft = player.getEquipment().getItemInOffHand();

        if (playerItemRight.getType().equals(FISHING_ROD)) {

            playerItem = playerItemRight;

        } else if (playerItemLeft.getType().equals(FISHING_ROD)) {

            playerItem = playerItemLeft;

        }

        //Block detection
        //Check if the item is a grappling hook


        if (player.isGliding()) {
            //If the player already has hook data, use that
            if (targettedBlock != null && blockMaterial != Material.AIR && playerList.contains(player.getName())) {
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

            }

            //This is the first throw of the fishing line
            if (event.getState() == FISHING) {
                //Check which block the player has targetted
                targettedBlock = player.getTargetBlock((Set<Material>) null, 32).getLocation();
                blockMaterial = player.getTargetBlock((Set<Material>) null, 32).getType();

                //Teleport hook to the targetted location
                hook.teleport(targettedBlock);

            }

            //This logs relevant data about the throw for the second part which is first in the code
            if (targettedBlock != null && blockMaterial != Material.AIR) {
                //Visual cue to let people know they landed the hit
                player.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, player.getLocation(), 5, 0, 0, 0);

                //Checks if there are existing entries for the player casting the line, deletes them
                if (playerList.contains(player.getName())) {
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

    //Make sure players that quit don't accidentally leave info stored in the lists
    @EventHandler
    public void Quit(PlayerQuitEvent event) {

        Player player = event.getPlayer();

        if (playerList != null) {
            if (playerList.contains(player.getName())) {
                int index = playerList.indexOf(player.getName());

                playerList.remove(index);
                vectorBlock.remove(index);

            }
        }

    }
}

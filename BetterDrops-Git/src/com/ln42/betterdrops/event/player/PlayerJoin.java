/*
 * This class handles the details if a player logged off while wearing a pair of special boots.
 */
package com.ln42.betterdrops.event.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.event.inventory.BootsEquipEvent;

public class PlayerJoin implements Listener {
	private Main plugin;

	public PlayerJoin(Main pl) {
		plugin = pl;
	}

	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final String name = event.getPlayer().getDisplayName();
		if (BootsEquipEvent.offlinePlayers.containsKey(name)) {
			if (BootsEquipEvent.offlinePlayers.get(name).equals("fire")) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Player player = event.getPlayer();
						player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
						player.removePotionEffect(PotionEffectType.SPEED);
						player.removePotionEffect(PotionEffectType.JUMP);
						if (BootsEquipEvent.oldEffects.containsKey(player.getDisplayName())){
							if (BootsEquipEvent.oldEffects.get(player.getDisplayName()).containsKey(PotionEffectType.FIRE_RESISTANCE)){
								PotionEffect pe = BootsEquipEvent.oldEffects.get(player.getDisplayName()).get(PotionEffectType.FIRE_RESISTANCE);
								player.addPotionEffect(pe);
							}
							if (BootsEquipEvent.oldEffects.get(player.getDisplayName()).containsKey(PotionEffectType.SPEED)){
								PotionEffect pe = BootsEquipEvent.oldEffects.get(player.getDisplayName()).get(PotionEffectType.SPEED);
								player.addPotionEffect(pe);
							}
							if (BootsEquipEvent.oldEffects.get(player.getDisplayName()).containsKey(PotionEffectType.JUMP)){
								PotionEffect pe = BootsEquipEvent.oldEffects.get(player.getDisplayName()).get(PotionEffectType.JUMP);
								player.addPotionEffect(pe);
							}
							BootsEquipEvent.oldEffects.remove(player.getDisplayName());
						}
						Location l = player.getLocation();
						l.setX(l.getBlockX() + .5);
						l.setZ(l.getBlockZ() + .5);
						player.teleport(l);
						if (player.getLocation().getBlock().getType().equals(Material.FIRE)) {
							player.getLocation().getBlock().setType(Material.AIR);
						}
						player.setFireTicks(0);
						BootsEquipEvent.offlinePlayers.remove(name);
					}
				}.runTaskLater(plugin, 5);
			} else if (BootsEquipEvent.offlinePlayers.get(name).equals("sky")){
				new BukkitRunnable(){
					@Override
					public void run(){
						Player player = event.getPlayer();
						player.removePotionEffect(PotionEffectType.SLOW);
						if (BootsEquipEvent.oldEffects.containsKey(player.getDisplayName())){
							if (BootsEquipEvent.oldEffects.get(player.getDisplayName()).containsKey(PotionEffectType.SLOW)){
								PotionEffect pe = BootsEquipEvent.oldEffects.get(player.getDisplayName()).get(PotionEffectType.SLOW);
								player.addPotionEffect(pe);
							}
							BootsEquipEvent.oldEffects.remove(player.getDisplayName());
						}
						BootsEquipEvent.offlinePlayers.remove(name);
					}
				}.runTaskLater(plugin, 5);
			}
		}
	}
}

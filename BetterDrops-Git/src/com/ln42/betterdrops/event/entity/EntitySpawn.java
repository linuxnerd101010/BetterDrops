package com.ln42.betterdrops.event.entity;

import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.scheduler.BukkitRunnable;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;

public class EntitySpawn implements Listener {
	private Main plugin;

	public EntitySpawn(Main pl) {
		plugin = pl;
	}

	// @SuppressWarnings("deprecation")
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onEntitySpawn(final CreatureSpawnEvent event) {
		if (event.getEntity() instanceof Skeleton) {
			if (Tools.Odds(Main.oddsConfig.getInt("PoweredSkeletonSpawn"), 0)) {
				new BukkitRunnable() {
					@Override
					public void run() {
						Skeleton skeleton = (Skeleton) event.getEntity();
						if (skeleton.getSkeletonType().equals(SkeletonType.WITHER)) {
							return;
						}
						if (plugin.getConfig().getBoolean("PreventPoweredSpawnFromSpawners")) {
							if (event.getSpawnReason().equals(SpawnReason.SPAWNER)) {
								return;
							}
						}
						if (plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility") != 0) {
							skeleton.setCustomName("Powered Skeleton");
							if (plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility") == 2) {
								skeleton.setCustomNameVisible(true);
							} else {
								skeleton.setCustomNameVisible(false);
							}
							skeleton.getEquipment().setItemInHand(null);
							skeleton.getEquipment().setItemInHand(Tools.bowSelect());
							skeleton.getEquipment().setItemInHandDropChance((float) 0);
						} else {
							skeleton.getEquipment().setItemInHand(null);
							skeleton.getEquipment().setItemInHand(Tools.bowSelect());
							skeleton.getEquipment().setItemInHandDropChance((float) 1);
						}
						
					}
				}.runTaskLater(this.plugin, 10);
			}
		}
	}
}

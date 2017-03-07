package com.ln42.betterdrops.event.entity;

import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.projectiles.ProjectileSource;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.event.player.PlayerClick;

public class ProjectileLaunch implements Listener {
	public static HashMap<Entity, Integer> thrownSpecialItems = new HashMap<Entity, Integer>();
	private Main plugin;

	public ProjectileLaunch(Main pl) {
		plugin = pl;
	}

	@EventHandler
	public void onProjectileLaunch(ProjectileLaunchEvent event) {
		Projectile entity = event.getEntity();
		ProjectileSource s = entity.getShooter();
		if (!(s instanceof Player)) {
			return;
		}
		Player player = (Player) s;
		if (entity.getType().equals(EntityType.EGG)) {
			if (plugin.getConfig().getBoolean("LightningStrikeEgg")) {
				if (PlayerClick.strikeEggThrown.containsKey(s)) {
					thrownSpecialItems.put(entity, 0);
					PlayerClick.strikeEggThrown.remove(player);
				}
			}
			if (PlayerClick.vexBombThrown.containsKey(s)) {
				thrownSpecialItems.put(entity, 1);
				PlayerClick.vexBombThrown.remove(player);
			}
		} else if (entity.getType().equals(EntityType.THROWN_EXP_BOTTLE)) {
			if (plugin.getConfig().getBoolean("XPStorageDrop")) {
				if (PlayerClick.expStorageThrown.containsKey(player)) {
					thrownSpecialItems.put(entity, PlayerClick.expStorageThrown.get(player));
					PlayerClick.expStorageThrown.remove(player);
				}
			}
		}
	}
}

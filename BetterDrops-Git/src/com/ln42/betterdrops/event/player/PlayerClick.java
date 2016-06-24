package com.ln42.betterdrops.event.player;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.DragonFireball;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LargeFireball;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ShulkerBullet;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;

@SuppressWarnings("unused")
public class PlayerClick implements Listener {
	// public static boolean cooldown = false;
	public static HashMap<Player, Boolean> cooldown = new HashMap<Player, Boolean>();
	public static HashMap<Player, Boolean> anvilEggThrown = new HashMap<Player, Boolean>();
	private com.ln42.betterdrops.Main plugin;

	public PlayerClick(com.ln42.betterdrops.Main pl) {
		plugin = pl;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerClick(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		ItemStack item = player.getItemInHand();
		if (item.getType().equals(Material.STICK)) {
			if (plugin.getConfig().getBoolean("ShulkerLauncherDrop")) {
				if (Tools.isSpecialItem(item, "shulkerBL")) {
					// Block targetBlock = player.getTargetBlock((Set<Material>)
					// null, 100);
					int range = plugin.getConfig().getInt("ShulkerBLSightRange");
					BlockIterator blockit = new BlockIterator(player.getEyeLocation(), 0.0, range);
					Entity[] earr = null;
					Boolean entityFound = false;
					while (blockit.hasNext()) {
						earr = getNearbyEntities(blockit.next().getLocation(), 2, player);
						int nullCount = 0;
						if (earr.length != 0) {
							for (int i = 0; i <= earr.length - 1; i++) {
								if (earr[i] == null) {
									nullCount += 1;
								}
							}
							if (earr.length > nullCount) {
								entityFound = true;
								break;
							}
						}
					}
					if (entityFound == false) {
						return;
					}
					if (cooldown.containsKey(player)) {
						if (cooldown.get(player)) {
							return;
						}
					}
					for (int i = 0; i <= earr.length - 1; i++) {
						if (earr[i] != null) {
							Location l = player.getLocation();
							ShulkerBullet bullet = player.getWorld().spawn(l, ShulkerBullet.class);
							bullet.getWorld().playSound(l, Sound.ENTITY_SHULKER_SHOOT, .4F, 1.5F);
							bullet.setShooter(player);
							bullet.setTarget(earr[i]);
							setCooldown(player);
							randomBreak(player, item);
							return;
						}
					}
				}
			}
			//if (Tools.isSpecialItem(item, "fireballWand")) {
		//		player.launchProjectile(DragonFireball.class);
			//}
		}
		if (plugin.getConfig().getBoolean("LightningStrikeEgg")) {
			if (item.getType().equals(Material.EGG)) {
				if (Tools.isSpecialItem(item, "strikeEgg")) {
					anvilEggThrown.put(player, true);
				}
			}
		}
	}

	public static Entity[] getNearbyEntities(Location l, int radius, Player player) {
		int chunkRadius = radius < 16 ? 1 : (radius - (radius % 16)) / 16;
		HashSet<Entity> radiusEntities = new HashSet<Entity>();
		for (int chX = 0 - chunkRadius; chX <= chunkRadius; chX++) {
			for (int chZ = 0 - chunkRadius; chZ <= chunkRadius; chZ++) {
				int x = (int) l.getX(), y = (int) l.getY(), z = (int) l.getZ();
				for (Entity e : new Location(l.getWorld(), x + (chX * 16), y, z + (chZ * 16)).getChunk()
						.getEntities()) {
					if (e.getLocation().distance(l) <= radius && e.getLocation().getBlock() != l.getBlock())
						radiusEntities.add(e);
				}
			}
		}
		Entity[] ent = radiusEntities.toArray(new Entity[radiusEntities.size()]);
		for (int i = 0; i <= ent.length - 1; i++) {
			if (ent[i].equals(player)) {
				ent[i] = null;
			}
			if (!(ent[i] instanceof LivingEntity)) {
				ent[i] = null;
			}
		}
		return ent;
	}

	public void setCooldown(final Player player) {
		int time = plugin.getConfig().getInt("ShulkerBLCooldown");
		if (time <= 0) {
			return;
		}
		cooldown.put(player, true);
		new BukkitRunnable() {
			@Override
			public void run() {
				cooldown.remove(player);
			}
		}.runTaskLater(this.plugin, time);
	}

	@SuppressWarnings("deprecation")
	public void randomBreak(Player player, ItemStack item) {
		int luck = 0;
		luck += Tools.potionEffectLevel(player, PotionEffectType.UNLUCK);
		luck -= Tools.potionEffectLevel(player, PotionEffectType.LUCK);
		if (Tools.Odds(Main.oddsConfig.getInt("ShulkerLauncherBreak"), luck)) {
			player.getWorld().playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1.5F);
			item.setAmount(item.getAmount() - 1);
			if (item.getAmount() == 0) {
				player.setItemInHand(null);
			}
		}
	}
}

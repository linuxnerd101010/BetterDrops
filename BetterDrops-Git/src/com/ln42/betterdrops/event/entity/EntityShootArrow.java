/*
 * This class handles details from all of the special bows.
 */
package com.ln42.betterdrops.event.entity;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
//§6Space Bow
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;

public class EntityShootArrow implements Listener {
	private Main plugin;
	public static HashMap<Arrow, UUID> spaceArrows = new HashMap<Arrow, UUID>();
	public static HashMap<Arrow, UUID> iceArrows = new HashMap<Arrow, UUID>();
	public static HashMap<Arrow, UUID> bazookaArrows = new HashMap<Arrow, UUID>();
	// public static Location explodeLoc = null;
	public static HashMap<LivingEntity, Location> explodeLoc = new HashMap<LivingEntity, Location>();

	public EntityShootArrow(Main pl) {
		plugin = pl;
	}

	// @SuppressWarnings("deprecation")
	@SuppressWarnings("unused")
	@EventHandler
	public void onShootBow(final EntityShootBowEvent e) {
		ItemStack bow = e.getBow();
		ItemMeta meta = bow.getItemMeta();
		final LivingEntity shooter = e.getEntity();
		if (Tools.isSpecialItem(bow, "spaceBow")) {
			Arrow arrow = (Arrow) e.getProjectile();
			if (shooter instanceof Player) {
				e.getProjectile().setVelocity(e.getProjectile().getVelocity().multiply(5));
				UUID id = arrow.getUniqueId();
				arrow.setKnockbackStrength(plugin.getConfig().getInt("PlayerShootSpaceBowKnockbackValue"));
				spaceArrows.put(arrow, id);
				new BukkitRunnable() {
					@Override
					public void run() {
						(e.getProjectile()).remove();
					}
				}.runTaskLater(this.plugin, 1000);
			}
			if (shooter instanceof Skeleton) {
				arrow.setKnockbackStrength(plugin.getConfig().getInt("SkeletonShootSpaceBowKnockbackValue"));
			}
		}
		if (Tools.isSpecialItem(bow, "shotgunBow")) {
			if (e.getProjectile() instanceof Arrow) {
				Arrow shotArrow = (Arrow) e.getProjectile();
				int fire = shotArrow.getFireTicks();
				World world = shotArrow.getWorld();
				if (shooter instanceof Player) {
					Player player = (Player) e.getEntity();
					PlayerInventory inv = player.getInventory();
					Vector dir = player.getEyeLocation().getDirection();
					Location playerLoc = player.getLocation();
					playerLoc.add(dir);
					for (int i = 0; i < 10; i++) {
						double factor = (double) plugin.getConfig().get("ShotgunBowSpread");
						double xDiff = factor * Math.random();
						double yDiff = factor * Math.random();
						double zDiff = factor * Math.random();
						// xDiff /= 10;
						// yDiff /= 10;
						// zDiff /= 10;
						double xNeg = Math.random();
						double yNeg = Math.random();
						double zNeg = Math.random();
						if (xNeg >= .5) {
							xDiff *= -1;
						}
						if (yNeg >= .5) {
							yDiff *= -1;
						}
						if (zNeg >= .5) {
							zDiff *= -1;
						}
						float fall = shotArrow.getFallDistance();
						fall *= 0;
						float f = 12;
						xDiff += dir.getX();
						yDiff += dir.getY();
						zDiff += dir.getZ();
						Vector newArrowVector = new Vector(xDiff, yDiff, zDiff);
						newArrowVector.multiply(3);
						if (plugin.getConfig().getBoolean("ShotgunArrowsComeFromInventory")) {
							if (!(player.getGameMode().equals(GameMode.CREATIVE))) {
								if (!(inv.contains(Material.ARROW, 2))) {
									return;
								}
							}
							player.launchProjectile(Arrow.class, newArrowVector);
							if (!(player.getGameMode().equals(GameMode.CREATIVE))) {
								inv.removeItem(new ItemStack(Material.ARROW));
							}
						} else {
							new BukkitRunnable(){
								@Override
								public void run(){
									Arrow newArrow = world.spawnArrow(shotArrow.getLocation(), newArrowVector, 0, 0);
									newArrow.setVelocity(newArrowVector);
									newArrow.setFireTicks(fire);
									newArrow.setShooter(player);
								}
							}.runTaskLater(plugin, 2);
						}
						// world.spawnArrow(shotArrow.getLocation(),
						// shotArrow.getVelocity(),
						// shotArrow.getFallDistance(), f);
					}
				}
				if (shooter instanceof Skeleton) {
					Skeleton sk = (Skeleton) e.getEntity();
					Vector dir = sk.getEyeLocation().getDirection();
					Location skLoc = sk.getLocation();
					skLoc.add(dir);
					for (int i = 0; i < 10; i++) {
						double factor = (double) plugin.getConfig().get("ShotgunBowSpread");
						double xDiff = factor * Math.random();
						double yDiff = factor * Math.random();
						double zDiff = factor * Math.random();
						// xDiff /= 10;
						// yDiff /= 10;
						// zDiff /= 10;
						double xNeg = Math.random();
						double yNeg = Math.random();
						double zNeg = Math.random();
						if (xNeg >= .5) {
							xDiff *= -1;
						}
						if (yNeg >= .5) {
							yDiff *= -1;
						}
						if (zNeg >= .5) {
							zDiff *= -1;
						}
						float fall = shotArrow.getFallDistance();
						fall *= 0;
						float f = 12;
						xDiff += dir.getX();
						yDiff += dir.getY();
						zDiff += dir.getZ();
						Vector newArrowVector = new Vector(xDiff, yDiff, zDiff);
						newArrowVector.multiply(1.1);
						sk.launchProjectile(Arrow.class, newArrowVector);
					}
				}
			}
		}
		if (Tools.isSpecialItem(bow, "iceBow")) {
			if (e.getProjectile() instanceof Arrow) {
				Arrow shotArrow = (Arrow) e.getProjectile();
				iceArrows.put(shotArrow, shotArrow.getUniqueId());
			}
		}
		if (Tools.isSpecialItem(bow, "bazookaBow")) {
			final Arrow arrow = (Arrow) e.getProjectile();
			if (e.getEntity() instanceof Player) {
				arrow.setVelocity(e.getProjectile().getVelocity().multiply(2));
			}
			bazookaArrows.put(arrow, arrow.getUniqueId());
			final World world = arrow.getWorld();
			arrow.setKnockbackStrength(0);
			// final Vector fvec = arrow.getVelocity();
			new BukkitRunnable() {
				@Override
				public void run() {
					Location arrowLoc = arrow.getLocation();
					if (!(explodeLoc.containsKey(shooter))) {
						explodeLoc.put(shooter, arrowLoc);
					}
					/*
					 * Vector vec = fvec; vec.multiply(.5); if
					 * (arrow.getVelocity().getX() < vec.getX()){ if
					 * (arrow.getVelocity().getZ() < vec.getZ()){
					 * arrowLoc.setY(arrowLoc.getY() + .3);
					 * arrowLoc.setX(arrowLoc.getX() + .3);
					 * arrowLoc.setX(arrowLoc.getX() + .3); } }
					 * System.out.println("Explosion Loc: " +
					 * arrowLoc.toString());
					 */
					world.createExplosion(explodeLoc.get(shooter), 3);
					explodeLoc.remove(shooter);
					arrow.remove();
					EntityShootArrow.bazookaArrows.remove(arrow);
				}
			}.runTaskLater(this.plugin, 20);
		}
	}
}
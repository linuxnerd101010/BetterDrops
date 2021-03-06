/*
 * This class handles most code for the Lightning Strike egg.
 */
package com.ln42.betterdrops.event.player;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vex;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.ln42.betterdrops.Tools;
import com.ln42.betterdrops.event.entity.ProjectileLaunch;

public class PlayerThrowEgg implements Listener {
	private com.ln42.betterdrops.Main plugin;
	public static HashMap<LivingEntity, Boolean> strikeActive = new HashMap<LivingEntity, Boolean>();
	private HashMap<LivingEntity, Integer> id = new HashMap<LivingEntity, Integer>();
	private HashMap<LivingEntity, Integer> count = new HashMap<LivingEntity, Integer>();
	public static HashMap<Player, LivingEntity> targetEntity = new HashMap<Player, LivingEntity>();
	public static HashMap<Egg, Player> thrower = new HashMap<Egg, Player>();

	public PlayerThrowEgg(com.ln42.betterdrops.Main pl) {
		plugin = pl;
	}

	@EventHandler
	public void onEggThrow(final PlayerEggThrowEvent event) {
		if (ProjectileLaunch.thrownSpecialItems.containsKey(event.getEgg())) {
			if (ProjectileLaunch.thrownSpecialItems.get(event.getEgg()) == 0) {
				thrower.put(event.getEgg(), event.getPlayer());
				new BukkitRunnable() {
					@Override
					public void run() {
						ProjectileLaunch.thrownSpecialItems.remove(event.getEgg());
						Location eggLoc = event.getEgg().getLocation();
						if (!(targetEntity.isEmpty())) {
							if (targetEntity.containsKey(event.getPlayer())) {
								lightningStrike(targetEntity.get(event.getPlayer()));
								targetEntity.remove(event.getPlayer());
								return;
							}
						}
						Entity[] earr = getNearbyEntities(eggLoc, 5, event.getPlayer());
						if (!(targetEntity.isEmpty())) {
							if (targetEntity.containsKey(event.getPlayer())) {
								lightningStrike(targetEntity.get(event.getPlayer()));
								targetEntity.remove(event.getPlayer());
								return;
							}
						}
						for (int i = 0; i <= earr.length - 1; i++) {
							if (earr[i] != null) {
								lightningStrike((LivingEntity) earr[i]);
								return;
							}
						}
					}
				}.runTaskLater(plugin, 2);
			} else if (ProjectileLaunch.thrownSpecialItems.get(event.getEgg()) == 1){
				Location eggLoc = event.getEgg().getLocation();
				for (int v = 0; v <= 4; v++){
					event.getEgg().getWorld().spawn(Tools.randomLocationVariance(eggLoc, 1, 0, true), Vex.class);
				}
			}
		}
		/*
		 * NOTES: Put a static boolean in PlayerClick that detects when the
		 * player uses a strike egg, and sets it to true for 2 clicks. Only
		 * execute this if that boolean is true. Also, register this event.
		 */
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
			if (!(ent[i] instanceof LivingEntity)) {
				ent[i] = null;
			}
			if (ent[i] instanceof Player) {
				if (!(ent[i].equals(player))) {
					Player pl = (Player) ent[i];
					if (pl.getGameMode().equals(GameMode.CREATIVE)) {
						ent[i] = null;
					} else {
						targetEntity.put(player, pl);
					}
				} else {
					ent[i] = null;
				}
			}
			if (ent[i] instanceof Chicken) {
				ent[i] = null;
			}
		}
		return ent;
	}

	public void lightningStrike(final LivingEntity target) {
		// final Stack<Block> anvilStack = new Stack<Block>();
		if (strikeActive.containsKey(target)) {
			if (strikeActive.get(target)) {
				return;
			}
		}
		final Location l = target.getLocation();
		final World world = target.getWorld();
		strikeActive.put(target, true);
		l.setX(l.getX() + 7);
		world.strikeLightningEffect(l);
		l.setX(l.getX() - 14);
		world.strikeLightningEffect(l);
		l.setX(l.getX() + 7);
		l.setZ(l.getZ() + 7);
		world.strikeLightningEffect(l);
		l.setZ(l.getZ() - 14);
		world.strikeLightningEffect(l);
		l.setZ(l.getZ() + 7);
		count.put(target, 0);
		final BukkitScheduler scheduler = target.getServer().getScheduler();
		int mId = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				// if (isDecaying == false) {
				// anvilDecay(anvilStack);
				// }
				Location newL = target.getLocation();
				newL = Tools.randomLocationVariance(newL, 3, 0, false);
				newL.getWorld().strikeLightning(newL);
				// newL.getBlock().setType(Material.ANVIL);
				// scheduleDecay(newL.getBlock());
				// anvilStack.add(newL.getBlock());
				// world.playSound(newL, Sound.BLOCK_ANVIL_PLACE, .4F, 2);
				if (target.isDead()) {
					count.remove(target);
					new BukkitRunnable() {
						@Override
						public void run() {
							strikeActive.remove(target);
						}
					}.runTaskLater(plugin, 4);
					scheduler.cancelTask(id.get(target));
					return;
				}
				if (count.containsKey(target)) {
					if (count.get(target) >= plugin.getConfig().getInt("LSEMaxStrikeCount")) {
						strikeActive.remove(target);
						count.remove(target);
						scheduler.cancelTask(id.get(target));
					}
					int i = count.get(target);
					count.remove(target);
					count.put(target, i + 1);
				}
			}
		}, 10L, 7L);
		id.put(target, mId);
	}

	public void scheduleDecay(final Block block) {
		new BukkitRunnable() {
			@Override
			public void run() {
				Location l = block.getLocation();
				boolean isAnvil = false;
				Block blockN = block;
				while (!(isAnvil)) {
					if (blockN.getType().equals(Material.ANVIL)) {
						isAnvil = true;
						blockN.setType(Material.AIR);
					} else {
						l.setY(l.getY() - 1);
						blockN = l.getBlock();
					}
					if (l.getBlockY() <= 0) {
						isAnvil = true;
					}
				}
			}
		}.runTaskLater(plugin, 40);
	}
}

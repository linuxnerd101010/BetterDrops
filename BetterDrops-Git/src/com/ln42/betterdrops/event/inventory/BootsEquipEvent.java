/*
 * This class handles the detection of when a player puts on special boots, and most of the handling code regarding the special boots.
 */
package com.ln42.betterdrops.event.inventory;

import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;
import com.ln42.betterdrops.codingforcookies.ArmorEquipEvent;
import com.ln42.betterdrops.codingforcookies.ArmorType;

public class BootsEquipEvent implements Listener {
	private Main plugin;
	// private int id = 0;
	// private boolean waterTask = false;
	private int tempId = 0;
	private HashMap<Player, Boolean> taskRunning = new HashMap<Player, Boolean>();
	private HashMap<Player, Boolean> skyTaskRunning = new HashMap<Player, Boolean>();
	private HashMap<Player, Boolean> waterTaskRunning = new HashMap<Player, Boolean>();
	private HashMap<Player, Boolean> fireTaskRunning = new HashMap<Player, Boolean>();
	public static HashMap<Player, Integer> id = new HashMap<Player, Integer>();
	private HashMap<Player, Boolean> waterTask = new HashMap<Player, Boolean>();
	private HashMap<Player, Block> waterBlock = new HashMap<Player, Block>();
	public static HashMap<Block, Integer> bridgeBlock = new HashMap<Block, Integer>();
	public static HashMap<String, String> offlinePlayers = new HashMap<String, String>();
	public static HashMap<String, HashMap<PotionEffectType, PotionEffect>> oldEffects = new HashMap<String, HashMap<PotionEffectType, PotionEffect>>();
	private HashMap<Player, Double> skyYDiff = new HashMap<Player, Double>();
	// public static Stack<Block> bridgeBlock = new Stack<Block>();

	public BootsEquipEvent(Main pl) {
		plugin = pl;
	}

	@EventHandler
	public void onBootsEquip(ArmorEquipEvent event) {
		if (event.getType() == null) {
			return;
		}
		if (event.getType().equals(ArmorType.BOOTS)) {
			ItemStack boots = event.getNewArmorPiece();
			Player player = event.getPlayer();
			if (Tools.isSpecialItem(boots, "fireBoots")) {
				if (taskRunning.containsKey(player)) {
					if (taskRunning.get(player)) {
						delayTask("fire", player);
					} else {
						fireWalk(player);
					}
				} else {
					fireWalk(player);
				}
			} else if (Tools.isSpecialItem(boots, "skywalkerBoots")) {
				if (taskRunning.containsKey(player)) {
					if (taskRunning.get(player)) {
						delayTask("sky", player);
					} else {
						skyWalk(player);
					}
				} else {
					skyWalk(player);
				}
			} else if (Tools.isSpecialItem(boots, "levitationBoots")) {
				if (taskRunning.containsKey(player)) {
					if (taskRunning.get(player)) {
						delayTask("water", player);
					} else {
						safeWalk(player);
					}
				} else {
					safeWalk(player);
				}
			}
		}
	}

	public void fireWalk(final Player player) {
		taskRunning.put(player, true);
		fireTaskRunning.put(player, true);
		if (player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)) {
			HashMap<PotionEffectType, PotionEffect> temp = new HashMap<PotionEffectType, PotionEffect>();
			temp.put(PotionEffectType.FIRE_RESISTANCE, Tools.getPotionEffect(player, PotionEffectType.FIRE_RESISTANCE));
			oldEffects.put(player.getDisplayName(), temp);
			player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
		}
		if (player.hasPotionEffect(PotionEffectType.SPEED)) {
			HashMap<PotionEffectType, PotionEffect> temp = new HashMap<PotionEffectType, PotionEffect>();
			temp.put(PotionEffectType.SPEED, Tools.getPotionEffect(player, PotionEffectType.SPEED));
			oldEffects.put(player.getDisplayName(), temp);
			player.removePotionEffect(PotionEffectType.SPEED);
		}
		if (player.hasPotionEffect(PotionEffectType.JUMP)) {
			HashMap<PotionEffectType, PotionEffect> temp = new HashMap<PotionEffectType, PotionEffect>();
			temp.put(PotionEffectType.JUMP, Tools.getPotionEffect(player, PotionEffectType.JUMP));
			oldEffects.put(player.getDisplayName(), temp);
			player.removePotionEffect(PotionEffectType.JUMP);
		}
		final BukkitScheduler scheduler = player.getServer().getScheduler();
		tempId = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				Location playerLoc = player.getLocation();
				Location blockLoc = playerLoc;
				blockLoc.setY(playerLoc.getY());
				final Block block = blockLoc.getBlock();
				// if
				// (!(player.hasPotionEffect(PotionEffectType.FIRE_RESISTANCE)))
				// {
				player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100000, 0));
				// }
				// if (!(player.hasPotionEffect(PotionEffectType.SPEED))) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100000, 2));
				// }
				// if (!(player.hasPotionEffect(PotionEffectType.JUMP))) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100000, 2));
				// }
				if ((block.getType().equals(Material.AIR))) {
					block.setType(Material.FIRE);
				}
				if (!(player.isOnline())) {
					int nId = id.get(player);
					id.remove(player);
					offlinePlayers.put(player.getDisplayName(), "fire");
					taskRunning.remove(player);
					fireTaskRunning.remove(player);
					scheduler.cancelTask(nId);
					return;
				}
				if (!(Tools.isSpecialItem(player.getEquipment().getBoots(), "fireBoots"))) {
					if (id.containsKey(player)) {
						int nId = id.get(player);
						id.remove(player);
						player.removePotionEffect(PotionEffectType.FIRE_RESISTANCE);
						player.removePotionEffect(PotionEffectType.SPEED);
						player.removePotionEffect(PotionEffectType.JUMP);
						if (oldEffects.containsKey(player.getDisplayName())) {
							if (oldEffects.get(player.getDisplayName()).containsKey(PotionEffectType.FIRE_RESISTANCE)) {
								PotionEffect pe = oldEffects.get(player.getDisplayName())
										.get(PotionEffectType.FIRE_RESISTANCE);
								player.addPotionEffect(pe);
							}
							if (oldEffects.get(player.getDisplayName()).containsKey(PotionEffectType.SPEED)) {
								PotionEffect pe = oldEffects.get(player.getDisplayName()).get(PotionEffectType.SPEED);
								player.addPotionEffect(pe);
							}
							if (oldEffects.get(player.getDisplayName()).containsKey(PotionEffectType.JUMP)) {
								PotionEffect pe = oldEffects.get(player.getDisplayName()).get(PotionEffectType.JUMP);
								player.addPotionEffect(pe);
							}
							oldEffects.remove(player.getDisplayName());
						}
						Location l = player.getLocation();
						l.setX(l.getBlockX() + .5);
						l.setZ(l.getBlockZ() + .5);
						player.teleport(l);
						if (player.getLocation().getBlock().getType().equals(Material.FIRE)) {
							player.getLocation().getBlock().setType(Material.AIR);
						}
						player.setFireTicks(0);
						fireTaskRunning.remove(player);
						taskRunning.remove(player);
						scheduler.cancelTask(nId);
					}
				}
			}
		}, 5L, 2L);
		id.put(player, tempId);
	}

	public void safeWalk(final Player player) {
		taskRunning.put(player, true);
		waterTaskRunning.put(player, true);
		final BukkitScheduler scheduler = player.getServer().getScheduler();
		tempId = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@Override
			public void run() {
				Location playerLoc = player.getLocation();
				final Location blockLoc = playerLoc;
				final Block block = blockLoc.getBlock();
				if (waterBlock.containsKey(player)) {
					Block wblock = waterBlock.get(player);
					wblock.setType(Material.AIR);
					waterTask.remove(player);
					waterTask.put(player, false);
					waterBlock.remove(player);
				}
				if (!(player.isOnline())) {
					int nId = id.get(player);
					id.remove(player);
					taskRunning.remove(player);
					waterTaskRunning.remove(player);
					scheduler.cancelTask(nId);
					return;
				}
				if (!(Tools.isSpecialItem(player.getEquipment().getBoots(), "levitationBoots"))) {
					if (id.containsKey(player)) {
						int nId = id.get(player);
						id.remove(player);
						taskRunning.remove(player);
						waterTaskRunning.remove(player);
						scheduler.cancelTask(nId);
						waterTask.clear();
						return;
					}
				}
				if (waterTask.containsKey(player)) {
					if (waterTask.get(player)) {
						waterTask.remove(player);
						return;
					}
					waterTask.remove(player);
				}
				if (block.getType().equals(Material.AIR)) {
					block.setType(Material.STATIONARY_WATER);
					waterTask.put(player, true);
					waterBlock.put(player, block);
				}
			}
		}, 5L, 4L);
		id.put(player, tempId);
	}

	public void skyWalk(final Player player) {
		taskRunning.put(player, true);
		skyTaskRunning.put(player, true);
		if (player.hasPotionEffect(PotionEffectType.SLOW)) {
			HashMap<PotionEffectType, PotionEffect> temp = new HashMap<PotionEffectType, PotionEffect>();
			temp.put(PotionEffectType.SLOW, Tools.getPotionEffect(player, PotionEffectType.SLOW));
			oldEffects.put(player.getDisplayName(), temp);
			player.removePotionEffect(PotionEffectType.SLOW);
		}
		final BukkitScheduler scheduler = player.getServer().getScheduler();
		tempId = scheduler.scheduleSyncRepeatingTask(plugin, new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (!(player.isOnline())) {
					int nId = id.get(player);
					id.remove(player);
					offlinePlayers.put(player.getDisplayName(), "sky");
					taskRunning.remove(player);
					skyTaskRunning.remove(player);
					scheduler.cancelTask(nId);
					if (skyYDiff.containsKey(player)){
						skyYDiff.remove(player);
					}
					return;
				}
				if (!(Tools.isSpecialItem(player.getEquipment().getBoots(), "skywalkerBoots"))) {
					if (id.containsKey(player)) {
						int nId = id.get(player);
						id.remove(player);
						player.removePotionEffect(PotionEffectType.SLOW);
						if (oldEffects.containsKey(player.getDisplayName())) {
							if (oldEffects.get(player.getDisplayName()).containsKey(PotionEffectType.SLOW)) {
								PotionEffect pe = oldEffects.get(player.getDisplayName()).get(PotionEffectType.SLOW);
								player.addPotionEffect(pe);
							}
							oldEffects.remove(player.getDisplayName());
						}
						taskRunning.remove(player);
						skyTaskRunning.remove(player);
						if (skyYDiff.containsKey(player)){
							skyYDiff.remove(player);
						}
						scheduler.cancelTask(nId);
						return;
					}
				}
				int decayDelay = plugin.getConfig().getInt("SkywalkBootsBlockDecayDelay");
				// PotionEffect[] pearr = (PotionEffect[])
				// player.getActivePotionEffects().toArray();
				// if (!(player.hasPotionEffect(PotionEffectType.SLOW))) {
				player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100000, 1));
				// }
				// player.removePotionEffect(PotionEffectType.SLOW);
				// player.addPotionEffect(new
				// PotionEffect(PotionEffectType.SLOW, 100, 1));
				Location playerLoc = player.getLocation();
				if (!(skyYDiff.containsKey(player))) {
					skyYDiff.put(player, playerLoc.getY());
				} else {
					double oldY = skyYDiff.get(player);
					double newY = playerLoc.getY();
					double diff = oldY - newY;
					if (diff > 1) {
						playerLoc.setY(oldY);
						player.teleport(playerLoc);
						player.removePotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 10, 5, true));
					}
					skyYDiff.remove(player);
					skyYDiff.put(player, newY);
				}
				final Location blockLoc = player.getLocation();
				blockLoc.setY(playerLoc.getY() - 1);
				final Block block = blockLoc.getBlock();
				if (block.getType().equals(Material.GLASS))
					return;
				int blockBakmid = blockLoc.getBlock().getType().getId();
				int blockBak = blockBakmid;
				bridgeBlock.put(block, blockBak);
				block.setType(Material.GLASS);
				if (decayDelay > 0) {
					// bridgeBlock.push(block);
					new BukkitRunnable() {
						@Override
						public void run() {
							/*
							 * if (!(bridgeBlock.search(block) < 0)) {
							 * bridgeBlock.remove(bridgeBlock.search(block)); }
							 */
							if (bridgeBlock.containsKey(block)) {
								block.setTypeId(bridgeBlock.get(block));
								bridgeBlock.remove(block);
							}
						}
					}.runTaskLater(plugin, decayDelay);

				}
			}
		}, 5L, 2L);
		id.put(player, tempId);
	}

	public void delayTask(String type, final Player player) {
		if (type.equals("sky")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (taskRunning.containsKey(player)) {
						if (skyTaskRunning.containsKey(player)) {
							if (skyTaskRunning.get(player)) {
								return;
							}
						}
						delayTask("sky", player);
						return;
					}
					skyWalk(player);
				}
			}.runTaskLater(plugin, 10);
		} else if (type.equals("water")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (taskRunning.containsKey(player)) {
						if (waterTaskRunning.containsKey(player)) {
							if (waterTaskRunning.get(player)) {
								return;
							}
						}
						delayTask("water", player);
						return;
					}
					safeWalk(player);
				}
			}.runTaskLater(plugin, 10);
		} else if (type.equals("fire")) {
			new BukkitRunnable() {
				@Override
				public void run() {
					if (taskRunning.containsKey(player)) {
						if (fireTaskRunning.containsKey(player)) {
							if (fireTaskRunning.get(player)) {
								return;
							}
						}
						delayTask("water", player);
						return;
					}
					fireWalk(player);
				}
			}.runTaskLater(plugin, 10);
		}
	}
}

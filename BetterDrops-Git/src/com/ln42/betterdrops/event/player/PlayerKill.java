/*
 * This class handles all drops.
 */
package com.ln42.betterdrops.event.player;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.BlockProjectileSource;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;

import org.bukkit.enchantments.Enchantment;

public class PlayerKill implements Listener {
	// @SuppressWarnings("unused")
	private com.ln42.betterdrops.Main plugin;

	public PlayerKill(com.ln42.betterdrops.Main pl) {
		plugin = pl;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerKillPlayer(EntityDeathEvent event) {
		Entity killedE = event.getEntity();
		Player killer = event.getEntity().getKiller();
		Object[] items = event.getDrops().toArray();
		boolean nP = true;
		if (killer == null) {
			EntityDamageEvent damageE = killedE.getLastDamageCause();
			if (damageE.getCause().equals(DamageCause.PROJECTILE)) {
				if (plugin.getConfig().getBoolean("DispenserKillsHeadDrop")) {
					EntityDamageByEntityEvent eDamageE = (EntityDamageByEntityEvent) damageE;
					Projectile p = (Projectile) eDamageE.getDamager();
					ProjectileSource pS = p.getShooter();
					if (pS instanceof BlockProjectileSource) {
						BlockProjectileSource blockS = (BlockProjectileSource) pS;
						if (blockS.getBlock().getType().equals(Material.DISPENSER)) {
							nP = false;
						}
					}
				}
			} else if (damageE.getCause().equals(DamageCause.LIGHTNING)) {
				if (plugin.getConfig().getBoolean("LightningStrikeEgg")) {
					if (killedE instanceof Player) {
						Player killedPlayer = (Player) killedE;
						if (PlayerThrowEgg.strikeActive.containsKey(killedPlayer)) {
							if (killedPlayer.getWorld().getGameRuleValue("keepInventory").equals("false")) {
								Location l = killedPlayer.getLocation();
								new BukkitRunnable() {
									@Override
									public void run() {
										for (int i = 0; i != items.length - 1; i++) {
											killedPlayer.getWorld().dropItem(l, (ItemStack) items[i]);
										}
									}
								}.runTaskLater(plugin, 5);
								nP = false;
							}
						}
					}
				}
			}
			if (nP) {
				return;
			}
		}
		if (killedE instanceof Player) {
			final Player killedPlayer = (Player) killedE;
			if (plugin.getConfig().getBoolean("PlayerHeadsDrop")) {
				int looting = 0;
				if (nP) {
					ItemStack weapon = killer.getItemInHand();
					looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
					looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
					looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
				}
				if (Tools.Odds(Main.oddsConfig.getInt("PlayerHeadDrop"), looting)) {
					String skullOwner = killedPlayer.getName();
					// String skullOwner = killer.getName();
					short damage = 0;
					byte data = 3;
					ItemStack drop = new ItemStack(Material.SKULL_ITEM, 1, damage, data);
					SkullMeta skullMeta = (SkullMeta) drop.getItemMeta();
					// skullMeta.setDisplayName(skullOwner + "'s Head");
					skullMeta.setOwner(skullOwner);
					drop.setItemMeta(skullMeta);
					killer.getWorld().dropItem(killedE.getLocation(), drop);
				}
			}
			EntityDamageEvent damageE = killedPlayer.getLastDamageCause();
			if (damageE.getCause().equals(DamageCause.LIGHTNING)) {
				if (plugin.getConfig().getBoolean("LightningStrikeEgg")) {
					if (killedE instanceof Player) {
						if (PlayerThrowEgg.strikeActive.containsKey(killedPlayer)) {
							if (killedPlayer.getWorld().getGameRuleValue("keepInventory").equals("false")) {
								Location l = killedPlayer.getLocation();
								new BukkitRunnable() {
									@Override
									public void run() {
										for (int i = -1; i != items.length - 1; i++) {
											if (i < 0) {
												i++;
											}
											killedPlayer.getWorld().dropItem(l, (ItemStack) items[i]);
										}
									}
								}.runTaskLater(plugin, 20);
								nP = false;
							}
						}
					}
				}
			}
		}
		if (killedE instanceof Creeper) {
			if (plugin.getConfig().getBoolean("MobHeadsDrop")) {
				int looting = 0;
				if (nP) {
					ItemStack weapon = killer.getItemInHand();
					looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
					looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
					looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
				}
				if (Tools.Odds(Main.oddsConfig.getInt("CreeperHeadDrop"), looting)) {
					short damage = 0;
					byte data = 4;
					ItemStack drop = new ItemStack(Material.SKULL_ITEM, 1, damage, data);
					killedE.getWorld().dropItem(killedE.getLocation(), drop);
				}
			}
		}
		if (killedE instanceof Zombie) {
			if (plugin.getConfig().getBoolean("MobHeadsDrop")) {
				if (!(killedE instanceof PigZombie)) {
					int looting = 0;
					if (nP) {
						ItemStack weapon = killer.getItemInHand();
						looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
						looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
						looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
					}
					if (Tools.Odds(Main.oddsConfig.getInt("ZombieHeadDrop"), looting)) {
						short damage = 0;
						byte data = 2;
						ItemStack drop = new ItemStack(Material.SKULL_ITEM, 1, damage, data);
						killedE.getWorld().dropItem(killedE.getLocation(), drop);
					}
				}
			}
		}
		if (killedE instanceof Skeleton) {
			int looting = 0;
			if (nP) {
				ItemStack weapon = killer.getItemInHand();
				looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
				looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
				looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
			}
			if (plugin.getConfig().getBoolean("MobHeadsDrop")) {
				Skeleton sk = (Skeleton) killedE;
				if (!(sk.getSkeletonType().equals(SkeletonType.WITHER))) {
					if (Tools.Odds(Main.oddsConfig.getInt("SkeletonHeadDrop"), looting)) {
						short damage = 0;
						byte data = 0;
						ItemStack drop = new ItemStack(Material.SKULL_ITEM, 1, damage, data);
						killedE.getWorld().dropItem(killedE.getLocation(), drop);
					}
				}
			}
			if (plugin.getConfig().getBoolean("PoweredSkeletons")) {
				if (killedE.getCustomName() == null) {
					Object[] drops = event.getDrops().toArray();
					for (int i = 0; i <= drops.length - 1; i++) {
						ItemStack item = (ItemStack) drops[i];
						if (Tools.isSpecialItem(item)) {
							if (item.getType().equals(Material.BOW)) {
								item.setDurability(Tools.randomDurability(385, looting));
								killedE.getWorld().dropItem(killedE.getLocation(), item);
								event.getDrops().clear();
								return;
							}
						}
					}
					return;
				}
				if (((Skeleton) killedE).getEquipment().getItemInHand().getType().equals(Material.AIR)) {
					return;
				}
				if (killedE.getCustomName().equals("Powered Skeleton")) {
					ItemStack bow = ((Skeleton) killedE).getEquipment().getItemInHand();
					if (bow.getType().equals(Material.BOW)) {
						bow.setDurability(Tools.randomDurability(385, looting));
					}
					killedE.getWorld().dropItem(killedE.getLocation(), bow);
				}
			}
		}
		if (killedE.getServer().getVersion().contains("MC: 1.9")) {
			if (killedE instanceof Shulker) {
				if (plugin.getConfig().getBoolean("ShulkerLauncherDrop")) {
					ItemStack weapon = killer.getItemInHand();
					int looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
					looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
					looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
					if (Tools.Odds(Main.oddsConfig.getInt("ShulkerLauncherDrop"), looting)) {
						ItemStack drop = Tools.getSpecialItem("shulkerBL");
						killedE.getWorld().dropItem(killedE.getLocation(), drop);
					}
				}
			}
		} else if (killedE.getServer().getVersion().contains("MC: 1.10")) {
			if (killedE instanceof Shulker) {
				if (plugin.getConfig().getBoolean("ShulkerLauncherDrop")) {
					ItemStack weapon = killer.getItemInHand();
					int looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
					looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
					looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
					if (Tools.Odds(Main.oddsConfig.getInt("ShulkerLauncherDrop"), looting)) {
						ItemStack drop = Tools.getSpecialItem("shulkerBL");
						killedE.getWorld().dropItem(killedE.getLocation(), drop);
					}
				}
			}
		}
		if (killedE instanceof PigZombie) {
			if (plugin.getConfig().getBoolean("SpecialBootsDrop")) {
				ItemStack weapon = killer.getItemInHand();
				int looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
				looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
				looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
				if (Tools.Odds(Main.oddsConfig.getInt("SpecialBootsDrop"), looting)) {
					ItemStack drop = Tools.bootsSelect();
					killedE.getWorld().dropItem(killedE.getLocation(), drop);
				}
			}
		}
		if (killedE instanceof Witch) {
			ItemStack weapon = killer.getItemInHand();
			int looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
			looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
			looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
			if (plugin.getConfig().getBoolean("FlightPotionDrop")) {
				if (Tools.Odds(Main.oddsConfig.getInt("FlightPotionDrop"), looting)) {
					killedE.getWorld().dropItem(killedE.getLocation(), Tools.getSpecialItem("flightPotion"));
				}
			}
			if (plugin.getConfig().getBoolean("TheftWandDrop")) {
				if (Tools.Odds(Main.oddsConfig.getInt("TheftWandDropChance"), looting)) {
					killedE.getWorld().dropItem(killedE.getLocation(), Tools.getSpecialItem("theftWand"));
				}
			}
		}
		if (killedE instanceof Bat) {
			if (plugin.getConfig().getBoolean("LightningStrikeEgg")) {
				killedE.getWorld().dropItem(killedE.getLocation(), Tools.getSpecialItem("strikeEgg"));
			}
		}
	}
}

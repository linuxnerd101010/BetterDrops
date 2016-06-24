/*
 * This class handles all drops.
 */
package com.ln42.betterdrops.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Shulker;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Witch;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;

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
		if (killer == null) {
			return;
		}
		if (killedE instanceof Player) {
			Player killedPlayer = (Player) killedE;
			if (plugin.getConfig().getBoolean("PlayerHeadsDrop")) {
				ItemStack weapon = killer.getItemInHand();
				int looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
				looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
				looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
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
		}
		if (killedE instanceof Creeper) {
			if (plugin.getConfig().getBoolean("MobHeadsDrop")) {
				ItemStack weapon = killer.getItemInHand();
				int looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
				looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
				looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
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
					ItemStack weapon = killer.getItemInHand();
					int looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
					looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
					looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
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
			ItemStack weapon = killer.getItemInHand();
			int looting = weapon.getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS);
			looting += Tools.potionEffectLevel(killer, PotionEffectType.LUCK);
			looting -= Tools.potionEffectLevel(killer, PotionEffectType.UNLUCK);
			if (plugin.getConfig().getBoolean("MobHeadsDrop")) {
				if (Tools.Odds(Main.oddsConfig.getInt("SkeletonHeadDrop"), looting)) {
					short damage = 0;
					byte data = 0;
					ItemStack drop = new ItemStack(Material.SKULL_ITEM, 1, damage, data);
					killedE.getWorld().dropItem(killedE.getLocation(), drop);
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

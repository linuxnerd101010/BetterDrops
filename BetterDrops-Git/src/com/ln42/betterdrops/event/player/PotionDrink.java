/*
 * This class handles all code related to the Flight Potion.
 */
package com.ln42.betterdrops.event.player;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;

public class PotionDrink implements Listener {
	private Main plugin;

	public PotionDrink(Main pl) {
		plugin = pl;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onDrinkEvent(PlayerItemConsumeEvent event) {
		final Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (Tools.isSpecialItem(item, "flightPotion")) {
			if (plugin.getConfig().getBoolean("FlightPotionDrop")) {
				player.setAllowFlight(true);
				player.setFlying(true);
				player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 600, 0));
				new BukkitRunnable() {
					@Override
					public void run() {
						player.setAllowFlight(false);
						player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 6));
					}
				}.runTaskLater(this.plugin, 600);
			}
		} else if (Tools.isSpecialItem(item, "xpBottle")) {
			if (plugin.getConfig().getBoolean("XPStorageDrop")) {
				int xp = Tools.getXPForLevel(player.getLevel());
				for (int i = 0; i <= 40; i++){
					System.out.println("Ingoing: " + Integer.toString(i));
					System.out.println("Outcome: " + Integer.toString(Tools.getXPForLevel(i)));
					System.out.println("Reversed: " + Integer.toString(Tools.getLevelForXP(Tools.getXPForLevel(i))[0]));
					System.out.println("Remainder: " + Integer.toString(Tools.getLevelForXP(Tools.getXPForLevel(i))[1]));
				}
				xp += player.getTotalExperience();
				ItemStack fullBottle = null;
				if (xp >= 500) {
					fullBottle = Tools.getFullXpStorageBottle(500);
					int[] arr = Tools.getLevelForXP(xp - 500);
					int lvl = arr[0];
					if (player.getTotalExperience() >= arr[1]) {
						arr[1] *= -1;
						player.giveExp(arr[1]);
						player.setLevel(lvl);
					} else {
						int xpChange = Tools.getXPForLevel(lvl);
						lvl -= 1;
						xpChange -= Tools.getXPForLevel(lvl);
						xpChange -= arr[1] - player.getTotalExperience();
						player.setLevel(0);
						player.setTotalExperience(0);
						player.setLevel(lvl);
						player.giveExp(xpChange);
					}
				} else if (xp != 0) {
					fullBottle = Tools.getFullXpStorageBottle(xp);
					player.setLevel(0);
				} else {
					event.setCancelled(true);
					return;
				}
				final ItemStack fFullBottle = fullBottle;
				new BukkitRunnable() {
					@Override
					public void run() {
						if (player.getItemInHand().getType().equals(Material.GLASS_BOTTLE)) {
							player.setItemInHand(fFullBottle);
						} else {
							if (player.getInventory().contains(Material.GLASS_BOTTLE)) {
								player.getInventory().remove(Material.GLASS_BOTTLE);
								player.getWorld().dropItem(player.getLocation(), fFullBottle);
							} else {
								player.getWorld().dropItem(player.getLocation(), fFullBottle);
							}
						}
					}
				}.runTaskLater(plugin, 10);
			}
		}
	}
}

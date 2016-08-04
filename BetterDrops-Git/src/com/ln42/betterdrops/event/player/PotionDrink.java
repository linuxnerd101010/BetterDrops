/*
 * This class handles all code related to the Flight Potion.
 */
package com.ln42.betterdrops.event.player;

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
				int xp = player.getExpToLevel();
				ItemStack fullBottle = null;
				if (xp >= 10) {
					fullBottle = Tools.getFullXpStorageBottle(10);
					player.giveExpLevels(-10);
				} else {
					fullBottle = Tools.getFullXpStorageBottle(xp);
					xp *= -1;
					player.giveExpLevels(xp);
				}
				final ItemStack fFullBottle = fullBottle;
				new BukkitRunnable(){
					@Override
					public void run(){
						player.setItemInHand(fFullBottle);
					}
				}.runTaskLater(plugin, 10);
			}
		}
	}
}

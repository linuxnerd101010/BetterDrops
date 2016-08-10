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
				player.sendMessage(Integer.toString(player.getLevel()));
				int xp = Tools.getXPForLevel(player.getLevel());
				//player.sendMessage(Integer.toString(Tools.getXPForLevel(player.getLevel())));
				//player.sendMessage(Integer.toString(player.getTotalExperience()));
				player.sendMessage(Integer.toString(xp));
				ItemStack fullBottle = null;
				if (xp >= 500) {
					fullBottle = Tools.getFullXpStorageBottle(500);
					int lvl = Tools.getLevelForXP(xp - 500);
					lvl -= 1;
					player.setLevel(lvl);
				} else if (xp != 0){
					fullBottle = Tools.getFullXpStorageBottle(xp);
					player.setLevel(0);
				} else {
					event.setCancelled(true);
					return;
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

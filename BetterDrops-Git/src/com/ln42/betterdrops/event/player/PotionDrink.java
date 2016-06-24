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
	@EventHandler
	public void onDrinkEvent(PlayerItemConsumeEvent event){
		final Player player = event.getPlayer();
		ItemStack item = event.getItem();
		if (Tools.isSpecialItem(item, "flightPotion")){
			player.setAllowFlight(true);
			player.setFlying(true);
			player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 600, 0));
			new BukkitRunnable(){
				@Override
				public void run(){
					player.setAllowFlight(false);
					player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 40, 6));
				}
			}.runTaskLater(this.plugin, 600);
		}
	}
}

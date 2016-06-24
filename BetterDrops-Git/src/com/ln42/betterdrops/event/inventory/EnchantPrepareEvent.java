package com.ln42.betterdrops.event.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.inventory.ItemStack;

import com.ln42.betterdrops.Tools;

public class EnchantPrepareEvent implements Listener {
	@EventHandler
	public void onEnchant(PrepareItemEnchantEvent event){
		ItemStack item = event.getItem();
		if (Tools.isSpecialItem(item)){
			event.setCancelled(true);
		}
	}
}

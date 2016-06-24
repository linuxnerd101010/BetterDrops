package com.ln42.betterdrops.event.block;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.ln42.betterdrops.event.inventory.BootsEquipEvent;

public class BridgeBreak implements Listener {

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (BootsEquipEvent.bridgeBlock.containsKey(event.getBlock())) {
			event.setCancelled(true);
			event.getBlock().setTypeId(BootsEquipEvent.bridgeBlock.get(event.getBlock()));
			BootsEquipEvent.bridgeBlock.remove(event.getBlock());
		}
	}
}

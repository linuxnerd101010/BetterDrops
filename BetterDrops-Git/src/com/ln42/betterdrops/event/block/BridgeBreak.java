/*
 * This class is neccesary to prevent a player with silk touch from mining the blocks produced by the Skywalker boots.
 * It also prevents players from using the skywalker boots to travel through surfaces.
 */
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

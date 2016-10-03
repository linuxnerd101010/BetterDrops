/*
 * This class is neccesary to prevent a player with silk touch from mining the blocks produced by the Skywalker boots.
 * It also prevents players from using the skywalker boots to travel through surfaces.
 */
package com.ln42.betterdrops.event.block;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;
import com.ln42.betterdrops.event.inventory.BootsEquipEvent;

public class BlockBreak implements Listener {
	private Main plugin;

	public BlockBreak(Main pl) {
		plugin = pl;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		if (plugin.getConfig().getBoolean("SpecialBootsDrop")) {
			if (BootsEquipEvent.bridgeBlock.containsKey(event.getBlock())) {
				event.setCancelled(true);
				event.getBlock().setType(BootsEquipEvent.bridgeBlock.get(event.getBlock()));
				event.getBlock().setData(BootsEquipEvent.bridgeData.get(event.getBlock()));
				BootsEquipEvent.bridgeData.remove(event.getBlock());
				BootsEquipEvent.bridgeBlock.remove(event.getBlock());
				BootsEquipEvent.bridgeBlockDecay.remove(event.getBlock());
			}
		}
		if (plugin.getConfig().getBoolean("XPStorageDrop")) {
			if (event.getBlock().getType().equals(Material.EMERALD_ORE)) {
				if (event.getExpToDrop() >= 3) {
					Player player = event.getPlayer();
					ItemStack item = player.getItemInHand();
					int looting = 0;
					if (item != null) {
						looting = item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS);
						looting += Tools.potionEffectLevel(player, PotionEffectType.LUCK);
						looting -= Tools.potionEffectLevel(player, PotionEffectType.UNLUCK);
					}
					if (Tools.Odds(Main.oddsConfig.getInt("XPStorageDrop"), looting)) {
						event.getBlock().getWorld().dropItem(event.getBlock().getLocation(), Tools.getSpecialItem("xpBottle"));
					}
				}
			}
		}
	}
}

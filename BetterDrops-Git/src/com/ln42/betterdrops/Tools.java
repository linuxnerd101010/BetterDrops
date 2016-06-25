/*
 * Various methods used elsewhere.
 */
package com.ln42.betterdrops;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Tools {
	@SuppressWarnings("deprecation")
	public static ItemStack getSpecialItem(String name) {
		if (name.equals("fireBoots")) {
			ItemStack item = new ItemStack(Material.GOLD_BOOTS);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.RED + "Fire boots");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Feet o' Flames!");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		if (name.equals("levitationBoots")) {
			ItemStack item = new ItemStack(Material.GOLD_BOOTS);
			item.addUnsafeEnchantment(Enchantment.DEPTH_STRIDER, 10);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.AQUA + "Levitation boots");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Floating away...");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		if (name.equals("skywalkerBoots")) {
			ItemStack item = new ItemStack(Material.GOLD_BOOTS);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.GREEN + "Skywalker Boots");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Of no relation to Anakin");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		if (name.equals("spaceBow")) {
			ItemStack item = new ItemStack(Material.BOW);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.GOLD + "Space Bow");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Down with Newton!");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		if (name.equals("shotgunBow")) {
			ItemStack item = new ItemStack(Material.BOW);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.GRAY + "Shotgun");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Redneck Style!");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		if (name.equals("iceBow")) {
			ItemStack item = new ItemStack(Material.BOW);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.AQUA + "Ice Bow");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Freeze 'em!");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		if (name.equals("bazookaBow")) {
			ItemStack item = new ItemStack(Material.BOW);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.DARK_GREEN + "Bazooka");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Explosions!");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		if (name.equals("flightPotion")) {
			ItemStack flightPotion = new ItemStack(Material.BOW);
			flightPotion.setTypeId(373);
			ItemMeta flightPotionMeta = flightPotion.getItemMeta();
			flightPotionMeta.setDisplayName(ChatColor.RESET + "Flight Potion");
			ArrayList<String> flightPotionLore = new ArrayList<String>();
			flightPotionLore.add("0:30");
			flightPotionMeta.setLore(flightPotionLore);
			flightPotion.setItemMeta(flightPotionMeta);
			return flightPotion;
		}
		if (name.equals("shulkerBL")) {
			ItemStack launcher = new ItemStack(Material.STICK);
			ItemMeta meta = launcher.getItemMeta();
			meta.setDisplayName(ChatColor.LIGHT_PURPLE + "Shulker Bullet Shooter");
			ArrayList<String> launcherLore = new ArrayList<String>();
			launcherLore.add("Aim and Click to use.");
			meta.setLore(launcherLore);
			launcher.setItemMeta(meta);
			return launcher;
		}
		if (name.equals("theftWand")) {
			ItemStack item = new ItemStack(Material.STICK);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.DARK_GRAY + "Wand of Theft");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Thou shalt not get caught.");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		if (name.equals("strikeEgg")) {
			ItemStack item = new ItemStack(Material.EGG);
			ItemMeta itemMeta = item.getItemMeta();
			itemMeta.setDisplayName(ChatColor.YELLOW + "Lightning Strike");
			ArrayList<String> itemLore = new ArrayList<String>();
			itemLore.add("Kill (almost) guaranteed.");
			itemMeta.setLore(itemLore);
			item.setItemMeta(itemMeta);
			return item;
		}
		/*
		 * if (name.equals("fireballWand")) { ItemStack item = new
		 * ItemStack(Material.STICK); ItemMeta itemMeta = item.getItemMeta();
		 * itemMeta.setDisplayName(ChatColor.RED + "Fireball Wand");
		 * ArrayList<String> itemLore = new ArrayList<String>();
		 * itemLore.add("DEBUG_LORE"); itemMeta.setLore(itemLore);
		 * item.setItemMeta(itemMeta); return item; }
		 */else {
			return null;
		}
	}

	public static boolean Odds(int chance, int looting) {
		if (chance <= 1) {
			return true;
		}
		boolean isNegative = false;
		if (looting < 0) {
			isNegative = true;
			looting *= -1;
		}
		double ddlooting = looting;
		double dlooting = ddlooting / 2;
		dlooting += 1;
		double dchance = 0;
		if (isNegative) {
			dchance = chance * dlooting;
		} else {
			dchance = chance / dlooting;
		}
		if (dchance < 2) {
			return true;
		}
		int ichance = (int) dchance;
		Random rand = new Random();
		int randomNum = rand.nextInt((ichance - 1) + 1) + 1;
		int num = ichance / 2;
		// if (ichance * 2 != num) {
		// num += 1;
		// }
		// System.out.println(Integer.toString(randomNum));
		// System.out.println(Integer.toString(num));
		if (randomNum == num) {
			return true;
		} else {
			return false;
		}
	}

	public static ItemStack bootsSelect() {
		double rand = Math.random();
		rand *= 3;
		int irand = (int) rand;
		if (irand == 0) {
			if (JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("FireBoots")) {
				return getSpecialItem("fireBoots");
			} else {
				return bootsSelect();
			}
		}
		if (irand == 1) {
			if (JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("SkywalkerBoots")) {
				return getSpecialItem("skywalkerBoots");
			} else {
				return bootsSelect();
			}
		}
		if (irand == 2) {
			if (JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("LevitationBoots")) {
				return getSpecialItem("levitationBoots");
			} else {
				return bootsSelect();
			}
		} else {
			return null;
		}
	}

	public static ItemStack bowSelect() {
		double rand = Math.random();
		rand *= 4;
		int irand = (int) rand;
		if (irand == 0) {
			if (JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("BazookaBow")) {
				return getSpecialItem("bazookaBow");
			} else {
				return bowSelect();
			}
		}
		if (irand == 1) {
			if (JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("IceBow")) {
				return getSpecialItem("iceBow");
			} else {
				return bowSelect();
			}
		}
		if (irand == 2) {
			if (JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("SpaceBow")) {
				return getSpecialItem("spaceBow");
			} else {
				return bowSelect();
			}
		}
		if (irand == 3) {
			if (JavaPlugin.getPlugin(Main.class).getConfig().getBoolean("ShotgunBow")) {
				return getSpecialItem("shotgunBow");
			} else {
				return bowSelect();
			}
		} else {
			return null;
		}
	}

	public static short randomDurability(int max, int looting) {
		boolean isNegative = false;
		int realMax = max;
		if (looting < 0) {
			isNegative = true;
			looting *= -1;
		}
		double dlooting = looting;
		dlooting /= 2;
		dlooting += 1;
		double dmax = max;
		if (isNegative) {
			dmax *= dlooting;
		} else {
			dmax /= dlooting;
		}
		max = (int) dmax;
		double rand = Math.random();
		if (rand > .5) {
			rand *= Math.random() * 2;
		}
		rand *= max;
		if (rand > realMax){
			return (short) realMax;
		}
		return (short) rand;
	}

	public static boolean isSpecialItem(ItemStack item) {
		if (item == null) {
			return false;
		}
		if (item.getType().equals(Material.AIR)) {
			return false;
		}
		if (!(item.getItemMeta().hasLore())) {
			return false;
		}
		List<String> lore = item.getItemMeta().getLore();
		if (lore.contains("Feet o' Flames!")) {
			return true;
		} else if (lore.contains("Floating away...")) {
			return true;
		} else if (lore.contains("Of no relation to Anakin")) {
			return true;
		} else if (lore.contains("Down with Newton!")) {
			return true;
		} else if (lore.contains("Redneck Style!")) {
			return true;
		} else if (lore.contains("Explosions!")) {
			return true;
		} else if (lore.contains("0:30")) {
			return true;
		} else if (lore.contains("Aim and Click to use.")) {
			return true;
		} else if (lore.contains("Freeze 'em!")) {
			return true;
		} else if (lore.contains("Thou shalt not get caught.")) {
			return true;
		} else if (lore.contains("Kill (almost) guaranteed.")) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSpecialItem(ItemStack item, String name) {
		if (isSpecialItem(item)) {
			if (item.getItemMeta().getLore().equals(getSpecialItem(name).getItemMeta().getLore())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public static Location randomLocationVariance(Location l, int amount, int increaseYAmount, boolean enableRandomY) {
		Random rand = new Random();
		int randomNum = rand.nextInt((amount - 0) + 1) + 0;
		double randD = Math.random();
		if (randD <= .5) {
			randomNum *= -1;
		}
		l.setX(l.getX() + randomNum);
		randomNum = rand.nextInt((amount - 0) + 1) + 0;
		randD = Math.random();
		if (randD <= .5) {
			randomNum *= -1;
		}
		l.setZ(l.getZ() + randomNum);
		if (increaseYAmount != 0) {
			l.setY(l.getY() + increaseYAmount);
		}
		if (enableRandomY) {
			randomNum = rand.nextInt((amount - 0) + 1) + 0;
			randD = Math.random();
			if (randD <= .5) {
				randomNum *= -1;
			}
			l.setY(l.getY() + randomNum);
		}
		return l;
	}

	public static int potionEffectLevel(Player player, PotionEffectType type) {
		Object[] parr = player.getActivePotionEffects().toArray();
		for (int i = 0; i <= parr.length - 1; i++) {
			PotionEffect effect = (PotionEffect) parr[i];
			if (effect.getType().equals(type)) {
				return effect.getAmplifier() + 1;
			}
		}
		return 0;
	}
	public static PotionEffect getPotionEffect(Player player, PotionEffectType type){
		Object[] parr = player.getActivePotionEffects().toArray();
		for (int i = 0; i <= parr.length - 1; i++) {
			PotionEffect effect = (PotionEffect) parr[i];
			if (effect.getType().equals(type)) {
				return effect;
			}
		}
		return null;
	}
}

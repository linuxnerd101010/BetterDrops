package com.ln42.betterdrops.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Skeleton.SkeletonType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;

public class PluginCommand implements CommandExecutor {
	// @SuppressWarnings("deprecation")
	private Main plugin;

	public PluginCommand(Main pl) {
		plugin = pl;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		boolean permission = true;
		boolean oddsPerm = true;
		boolean configPerm = true;
		// Checks if the player has elevated privileges.
		if (!(sender.isOp())) {
			if (!(sender.hasPermission("bd.Commands"))) {
				permission = false;
			}
		}
		if (!(sender.isOp())) {
			if (!(sender.hasPermission("bd.viewOdds"))) {
				if (!(sender.hasPermission("bd.Commands"))) {
					oddsPerm = false;
				}
			}
		}
		if (!(sender.isOp())) {
			if (!(sender.hasPermission("bd.viewConfig"))) {
				if (!(sender.hasPermission("bd.Commands"))) {
					configPerm = false;
				}
			}
		}
		if (args.length == 0) {
			sender.sendMessage(ChatColor.RED + "Invalid Arguments. Type /bd help for more info.");
			return false;
		}
		String firstArg = args[0];
		if (args.length == 1) {
			// If the player typed /bd help
			if (firstArg.equals("help")) {
				sender.sendMessage("Usage:");
				sender.sendMessage(ChatColor.BOLD + "/bd help");
				sender.sendMessage("Displays this help.");
				sender.sendMessage(ChatColor.BOLD + "/bd give <item> [player]");
				sender.sendMessage(
						"Gives the specified special item to the specified player. If no player is specified, it gives the item to the player entering the command.");
				sender.sendMessage(ChatColor.BOLD + "/bd spawn <item> [player] OR /bd spawn <item> <x> <y> <z>");
				sender.sendMessage(
						"Spawns a skeleton holding the specified special item at the specified player's location. If no player is specified, it spawns the skeleton at the location of the player entering the command. If a coordinate is given, it spawns the skeleton there.");
				sender.sendMessage(ChatColor.BOLD + "/bd list");
				sender.sendMessage("Lists all the special items in BetterDrops1.9, and what creatures drop them.");
				sender.sendMessage(ChatColor.BOLD + "/bd odds list");
				sender.sendMessage("Lists all of the entries in the odds file and their current values.");
				sender.sendMessage(ChatColor.BOLD + "/bd odds set <entry> <value>");
				sender.sendMessage("Set the given odds entry to the given value.");
				sender.sendMessage(ChatColor.BOLD + "/bd config list");
				sender.sendMessage("Lists all of the entries in the config file and their current values.");
				sender.sendMessage(ChatColor.BOLD + "/bd config set <entry> <value>");
				sender.sendMessage("Set the given config entry to the given value.");
				sender.sendMessage(ChatColor.BOLD + "/bd version");
				sender.sendMessage("Displays the current version of BetterDrops.");
				return true;
			} else if (firstArg.equals("list")) {
				// If the player typed /bd list
				if (plugin.getConfig().getBoolean("ShulkerLauncherDrop")) {
					sender.sendMessage(ChatColor.LIGHT_PURPLE + "shulkerBL" + ChatColor.RESET
							+ "-A wand that shoots a shulker bullet at the creature/player you are looking at when used. Dropped by Shulkers.");
				} else {
					sender.sendMessage(ChatColor.RED + "Shulker Bullet Launcher is disabled.");
				}
				if (plugin.getConfig().getBoolean("TheftWandDrop")) {
					sender.sendMessage(ChatColor.DARK_GRAY + "theftWand" + ChatColor.RESET
							+ "-A wand that steals the item from whatever you hit with it, or moves it out of the target's hand. Dropped by Witches.");
				} else {
					sender.sendMessage(ChatColor.RED + "The Wand of Theft is disabled.");
				}
				if (plugin.getConfig().getBoolean("FlightPotionDrop")) {
					sender.sendMessage(ChatColor.ITALIC + "flightPotion" + ChatColor.RESET
							+ "-A potion that allows you to fly for 30 seconds. Dropped by Witches.");
				} else {
					sender.sendMessage(ChatColor.RED + "Flight Potion is disabled.");
				}
				if (plugin.getConfig().getBoolean("PoweredSkeletons")) {
					sender.sendMessage(ChatColor.AQUA + "iceBow" + ChatColor.RESET
							+ "-A bow that creates an ice layer around the creature/player hit. Wielded/Dropped by Powered Skeletons.");
					sender.sendMessage(ChatColor.GOLD + "spaceBow" + ChatColor.RESET
							+ "-A bow that is not affected by gravity and knocks the target creature/player farther than a normal bow. Wielded/Dropped by Powered Skeletons.");
					sender.sendMessage(ChatColor.DARK_GREEN + "bazookaBow" + ChatColor.RESET
							+ "-A bow that shoots an exploding arrow. Wielded/Dropped by Powered Skeletons.");
					sender.sendMessage(ChatColor.GRAY + "shotgunBow" + ChatColor.RESET
							+ "-A bow that shoots 11 arrows from your inventory in a spread-like a shotgun. Wielded/Dropped by Powered Skeletons.");
				} else {
					sender.sendMessage(ChatColor.RED + "Special Bows are disabled.");
				}
				if (plugin.getConfig().getBoolean("SpecialBootsDrop")) {
					sender.sendMessage(ChatColor.RED + "fireBoots" + ChatColor.RESET
							+ "-Boots that give you swiftness, jump boost, fire resistance-and set the block you are standing on on fire. Excellent for deforestation. Dropped by Zombie Pigmen.");
					sender.sendMessage(ChatColor.AQUA + "levitationBoots" + ChatColor.RESET
							+ "-Boots that create a water cushion at your feet, so you do not take fall damage-and can climb walls. Dropped by Zombie Pigmen.");
					sender.sendMessage(ChatColor.GREEN + "skywalkerBoots" + ChatColor.RESET
							+ "-Boots that create a glass block at your feet that decays after a short time. Dropped by Zombie Pigmen.");
				} else {
					sender.sendMessage(ChatColor.RED + "Special Boots are disabled.");
				}
				if (plugin.getConfig().getBoolean("LightningStrikeEgg")) {
					sender.sendMessage(ChatColor.YELLOW + "strikeEgg" + ChatColor.RESET
							+ "-An egg that when thrown targets the nearest player/creature and zaps them with lightning, usually until dead. Dropped by Bats.");
				} else {
					sender.sendMessage(ChatColor.RED + "Lightning Strike Egg is disabled.");
				}
				return true;
			} else if (firstArg.equals("version")) {
				sender.sendMessage("BetterDrops version " + plugin.getDescription().getVersion());
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid Arguments. Type /bd help for more info.");
				return false;
			}
		}
		String secondArg = args[1];
		if (secondArg == null) {
			sender.sendMessage(ChatColor.RED + "Invalid Arguments. Type /bd help for more info.");
			return false;
		}
		if (firstArg.equals("give")) {
			// If the player typed /bd give
			if (permission) {
				Inventory inv = Bukkit.createInventory(null, 9, "SpecialInventory");
				ItemStack item = Tools.getSpecialItem(secondArg);
				if (item == null) {
					sender.sendMessage(ChatColor.RED + "That item does not exist");
					return false;
				}
				inv.addItem(item);
				if (args.length == 3) {
					String thirdArg = args[2];
					Player targetPlayer = sender.getServer().getPlayer(thirdArg);
					if (targetPlayer == null) {
						sender.sendMessage(ChatColor.RED + "That player does not exist.");
						return false;
					} else {
						sender.sendMessage("Item given.");
						targetPlayer.openInventory(inv);
						return true;
					}
				} else if (args.length == 2) {
					if (!(sender instanceof Player)) {
						sender.sendMessage(ChatColor.RED + "You must specify a player!");
						return false;
					}
					Player player = (Player) sender;
					player.openInventory(inv);
					sender.sendMessage("Item given.");
					return true;
				}

			} else {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
				return false;
			}
		} else if (firstArg.equals("spawn")) {
			if (!(permission)) {
				sender.sendMessage(ChatColor.DARK_RED + "You do not have permission to use this command.");
				return false;
			}
			final ItemStack item = Tools.getSpecialItem(secondArg);
			if (item == null) {
				sender.sendMessage(ChatColor.RED + "That bow does not exist.");
				return false;
			}
			if (args.length == 3) {
				String thirdArg = args[2];
				final Player targetPlayer = sender.getServer().getPlayer(thirdArg);
				if (targetPlayer == null) {
					sender.sendMessage(ChatColor.RED + "That player does not exist.");
					return false;
				}
				final Skeleton sk = targetPlayer.getWorld().spawn(targetPlayer.getLocation(), Skeleton.class);
				sender.sendMessage("Skeleton spawned.");
				new BukkitRunnable() {
					@Override
					public void run() {
						sk.setSkeletonType(SkeletonType.NORMAL);
						if (plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility") != 0) {
							sk.setCustomName("Powered Skeleton");
							if (plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility") == 2) {
								sk.setCustomNameVisible(true);
							} else {
								sk.setCustomNameVisible(false);
							}
						}
						sk.getEquipment().setItemInHand(null);
						sk.getEquipment().setItemInHand(item);
						sk.getEquipment().setItemInHandDropChance((float) 0);
					}
				}.runTaskLater(this.plugin, 15);
				return true;
			} else if (args.length == 5) {
				double x = 0;
				double y = 0;
				double z = 0;
				try {
					x = Double.parseDouble(args[2]);
				} catch (java.lang.NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "That is not a valid coordinate.");
					return false;
				}
				try {
					y = Double.parseDouble(args[3]);
				} catch (java.lang.NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "That is not a valid coordinate.");
					return false;
				}
				try {
					z = Double.parseDouble(args[4]);
				} catch (java.lang.NumberFormatException e) {
					sender.sendMessage(ChatColor.RED + "That is not a valid coordinate.");
					return false;
				}
				World world = null;
				if (sender instanceof Player) {
					Player player = (Player) sender;
					world = player.getWorld();
				} else if (sender instanceof BlockCommandSender) {
					BlockCommandSender bcs = (BlockCommandSender) sender;
					world = bcs.getBlock().getWorld();
				} else {
					world = plugin.getServer().getWorld("world");
				}
				Location targetL = new Location(world, x, y, z);
				final Skeleton sk = world.spawn(targetL, Skeleton.class);
				sender.sendMessage("Skeleton spawned.");
				new BukkitRunnable() {
					@Override
					public void run() {
						sk.setSkeletonType(SkeletonType.NORMAL);
						if (plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility") != 0) {
							sk.setCustomName("Powered Skeleton");
							if (plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility") == 2) {
								sk.setCustomNameVisible(true);
							} else {
								sk.setCustomNameVisible(false);
							}
						}
						sk.getEquipment().setItemInHand(null);
						sk.getEquipment().setItemInHand(item);
						sk.getEquipment().setItemInHandDropChance((float) 0);
					}
				}.runTaskLater(this.plugin, 15);
				return true;
			} else {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "You must specify a player!");
					return false;
				}
				Player player = (Player) sender;
				final Skeleton sk = player.getWorld().spawn(player.getLocation(), Skeleton.class);
				sender.sendMessage("Skeleton spawned.");
				new BukkitRunnable() {
					@Override
					public void run() {
						sk.setSkeletonType(SkeletonType.NORMAL);
						if (plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility") != 0) {
							sk.setCustomName("Powered Skeleton");
							if (plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility") == 2) {
								sk.setCustomNameVisible(true);
							} else {
								sk.setCustomNameVisible(false);
							}
						}
						sk.getEquipment().setItemInHand(null);
						sk.getEquipment().setItemInHand(item);
						sk.getEquipment().setItemInHandDropChance((float) 0);
					}
				}.runTaskLater(this.plugin, 15);
				return true;
			}
		} else if (firstArg.equals("odds")) {
			if (secondArg.equals("list")) {
				if (!(oddsPerm)) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return false;
				}
				sender.sendMessage("All odds are 1 divided by the given value, with 1 being 100% chance.");
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "CreeperHeadDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Integer.toString(Main.oddsConfig.getInt("CreeperHeadDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "ZombieHeadDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Integer.toString(Main.oddsConfig.getInt("ZombieHeadDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "SkeletonHeadDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Integer.toString(Main.oddsConfig.getInt("SkeletonHeadDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PlayerHeadDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Integer.toString(Main.oddsConfig.getInt("PlayerHeadDrop")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "ShulkerLauncherDrop " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Integer.toString(Main.oddsConfig.getInt("ShulkerLauncherDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PoweredSkeletonSpawn " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET
						+ Integer.toString(Main.oddsConfig.getInt("PoweredSkeletonSpawn")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "SpecialBootsDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Integer.toString(Main.oddsConfig.getInt("SpecialBootsDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "FlightPotionDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Integer.toString(Main.oddsConfig.getInt("FlightPotionDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "ShulkerLauncherBreak " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET
						+ Integer.toString(Main.oddsConfig.getInt("ShulkerLauncherBreak")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "TheftWandDropChance " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Integer.toString(Main.oddsConfig.getInt("TheftWandDropChance")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "TheftWandSuccessChance "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Integer.toString(Main.oddsConfig.getInt("TheftWandSuccessChance")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "TheftWandMobSuccessChance "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Integer.toString(Main.oddsConfig.getInt("TheftWandMobSuccessChance")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "TheftWandBreakChance " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET
						+ Integer.toString(Main.oddsConfig.getInt("TheftWandBreakChance")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "TheftWandMobBreakChance "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Integer.toString(Main.oddsConfig.getInt("TheftWandMobBreakChance")));
			} else if (secondArg.equals("set")) {
				if (!(permission)) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return false;
				}
				if (args.length != 4) {
					sender.sendMessage(ChatColor.RED + "Invalid Arguments. Type /bd help for more info.");
					return false;
				}
				String key = args[2];
				if (!(Main.oddsConfig.contains(key))) {
					sender.sendMessage(
							ChatColor.RED + "That entry does not exist. Type /bd odds list for a list of entries.");
					return false;
				}
				int value = 0;
				try {
					value = Integer.parseInt(args[3]);
				} catch (java.lang.NumberFormatException e) {
					sender.sendMessage("Invalid value. Values must be an integer.");
					return false;
				}
				Main.oddsConfig.set(key, value);
				sender.sendMessage("Odds updated. Reload/Restart to save to disk.");
				return true;
			} else {
				sender.sendMessage(ChatColor.RED + "Invalid arguments. Type /bd help for more info.");
				return false;
			}
		} else if (firstArg.equals("config")) {
			if (secondArg.equals("list")) {
				if (!(configPerm)) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return false;
				}
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "MobHeadsDrop " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("MobHeadsDrop")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "PlayerHeadsDrop " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("PlayerHeadsDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PoweredSkeletons " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("PoweredSkeletons")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PreventPoweredSpawnFromSpawners "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("PreventPoweredSpawnFromSpawners")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PreventPoweredSpawnFromSpawners "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("PreventPoweredSpawnFromSpawners")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PoweredSkeletonNameTagVisibility "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Integer.toString(plugin.getConfig().getInt("PoweredSkeletonNameTagVisibility")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "SpaceBow " + ChatColor.BOLD + "Value: "
						+ ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("SpaceBow")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "IceBow " + ChatColor.BOLD + "Value: "
						+ ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("IceBow")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "BazookaBow " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("BazookaBow")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "ShotgunBow " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("ShotgunBow")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "ShotgunBowSpread " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Double.toString(plugin.getConfig().getDouble("ShotgunBowSpread")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "SkeletonShootSpaceBowKnockbackValue "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Integer.toString(plugin.getConfig().getInt("SkeletonShootSpaceBowKnockbackValue")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PlayerShootSpaceBowKnockbackValue "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Integer.toString(plugin.getConfig().getInt("PlayerShootSpaceBowKnockbackValue")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "SpecialBootsDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("SpecialBootsDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "FireBoots " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("FireBoots")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "SkywalkerBoots " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("SkywalkerBoots")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "LevitationBoots " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("LevitationBoots")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "SkywalkBootsBlockDecayDelay "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Integer.toString(plugin.getConfig().getInt("SkywalkBootsBlockDecayDelay")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "ShulkerLauncherDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("ShulkerLauncherDrop")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "ShulkerBLCooldown " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Integer.toString(plugin.getConfig().getInt("ShulkerBLCooldown")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "ShulkerBLSightRange " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Integer.toString(plugin.getConfig().getInt("ShulkerBLSightRange")));
				sender.sendMessage(
						ChatColor.BOLD + "Key: " + ChatColor.RESET + "TheftWandDrop " + ChatColor.BOLD + "Value: "
								+ ChatColor.RESET + Boolean.toString(plugin.getConfig().getBoolean("TheftWandDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "LightningStrikeEgg " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("LightningStrikeEgg")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "FlightPotionDrop " + ChatColor.BOLD
						+ "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("FlightPotionDrop")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PreventSpecialItemRepair "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("PreventSpecialItemRepair")));
				sender.sendMessage(ChatColor.BOLD + "Key: " + ChatColor.RESET + "PreventSpecialItemEnchant "
						+ ChatColor.BOLD + "Value: " + ChatColor.RESET
						+ Boolean.toString(plugin.getConfig().getBoolean("PreventSpecialItemEnchant")));
				return true;
			} else if (secondArg.equals("set")) {
				if (!(permission)) {
					sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
					return false;
				}
				if (args.length != 4) {
					sender.sendMessage(ChatColor.RED + "Invalid Arguments. Type /bd help for more info.");
					return false;
				}
				String key = args[2];
				if (!(plugin.getConfig().contains(key))) {
					sender.sendMessage(
							ChatColor.RED + "That entry does not exist. Type /bd config list for a list of entries.");
					return false;
				}
				if (Main.configEntryType.get(key).equals(Boolean.class)) {
					boolean value = true;
					try {
						value = Boolean.parseBoolean(args[3]);
					} catch (NumberFormatException e) {
						sender.sendMessage("Invalid value. Values must be of the same type as the key.");
						return false;
					}
					plugin.getConfig().set(key, value);
					sender.sendMessage("Config updated. Reload/Restart to save to disk.");
					return true;
				}
				if (Main.configEntryType.get(key).equals(Integer.class)) {
					int value = 0;
					try {
						value = Integer.parseInt(args[3]);
					} catch (java.lang.NumberFormatException e) {
						sender.sendMessage("Invalid value. Values must be of the same type as the key.");
						return false;
					}
					plugin.getConfig().set(key, value);
					sender.sendMessage("Config updated. Reload/Restart to save to disk.");
					return true;
				}
				if (Main.configEntryType.get(key).equals(Double.class)) {
					double value = 0;
					try {
						value = Double.parseDouble(args[3]);
					} catch (java.lang.NumberFormatException e) {
						sender.sendMessage("Invalid value. Values must be of the same type as the key.");
						return false;
					}
					plugin.getConfig().set(key, value);
					sender.sendMessage("Config updated. Reload/Restart to save to disk.");
					return true;
				}
				/*
				 * try { value = Integer.parseInt(args[3]); } catch
				 * (java.lang.NumberFormatException e) { sender.sendMessage(
				 * "Invalid value. Values must be an integer."); return false; }
				 * Main.oddsConfig.set(key, value); sender.sendMessage(
				 * "Odds updated. Reload/Restart to save to disk."); return
				 * true;
				 */
			}
		}
		return false;
	}
}

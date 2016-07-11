/*
 * This class handles code involving most of the special bows, the Theft Wand, and the Lightning Strike egg.
 */
package com.ln42.betterdrops.event.entity;

import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.ln42.betterdrops.Main;
import com.ln42.betterdrops.Tools;
import com.ln42.betterdrops.event.player.PlayerClick;
import com.ln42.betterdrops.event.player.PlayerThrowEgg;

public class EntityDamageEntity implements Listener {
	private Main plugin;

	public EntityDamageEntity(Main pl) {
		plugin = pl;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onArrowHit(final EntityDamageByEntityEvent event) {
		if (plugin.getConfig().getBoolean("PoweredSkeletons")) {
			if (event.getDamager() instanceof Arrow) {
				final Arrow arrow = (Arrow) event.getDamager();
				if (EntityShootArrow.spaceArrows.containsKey(arrow)) {
					UUID id = arrow.getUniqueId();
					if (EntityShootArrow.spaceArrows.get(arrow).equals(id)) {
						if (event.getEntity() instanceof LivingEntity) {
							double damage = event.getDamage();
							damage /= 3;
							event.setDamage(damage);
							EntityShootArrow.spaceArrows.remove(arrow);
						}
					}
				}
				if (EntityShootArrow.iceArrows.containsKey(arrow)) {
					UUID id = arrow.getUniqueId();
					if (EntityShootArrow.iceArrows.get(arrow).equals(id)) {
						if (event.getEntity() instanceof LivingEntity) {
							EntityShootArrow.iceArrows.remove(arrow);
							LivingEntity target = (LivingEntity) event.getEntity();
							Location targetLocation = roundLoc(target.getLocation());
							target.teleport(targetLocation);
							event.setDamage(4);
							if (target.getType().equals(EntityType.SPIDER)) {
								spiderIceForm(targetLocation);
								return;
							}
							if (target.getType().equals(EntityType.SHEEP)) {
								largeIceForm(targetLocation);
								return;
							}
							if (target.getType().equals(EntityType.COW)) {
								largeIceForm(targetLocation);
								return;
							}
							if (target.getType().equals(EntityType.MAGMA_CUBE)) {
								MagmaCube mc = (MagmaCube) target;
								int size = mc.getSize();
								if (size == 1) {
									smallIceForm(targetLocation);
									return;
								}
								if (size == 2) {
									largeIceForm(targetLocation);
									return;
								}
								if (size <= 4) {
									megaIceForm(targetLocation);
								}
								return;
							}
							if (target.getType().equals(EntityType.SLIME)) {
								Slime slime = (Slime) target;
								int size = slime.getSize();
								if (size == 1) {
									smallIceForm(targetLocation);
									return;
								}
								if (size == 2) {
									largeIceForm(targetLocation);
									return;
								}
								if (size <= 4) {
									megaIceForm(targetLocation);
								}
								return;

							}
							if (target.getType().equals(EntityType.GHAST)) {
								target.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 1, 1));
								return;
							}
							if (target.getType().equals(EntityType.HORSE)) {
								megaIceForm(targetLocation);
								return;
							}
							if (target.getType().equals(EntityType.SHULKER)){
								microIceForm(targetLocation);
								return;
							}
							if (target.getType().equals(EntityType.SILVERFISH)){
								microIceForm(targetLocation);
								return;
							}
							if (target.getType().equals(EntityType.ENDERMITE)){
								microIceForm(targetLocation);
								return;
							}
							if (target.getType().equals(EntityType.PIG)) {
								largeIceForm(targetLocation);
								return;
							} else {
								smallIceForm(targetLocation);
								return;
							}
						}
					}
				}
				if (EntityShootArrow.bazookaArrows.containsKey(arrow)) {
					if (EntityShootArrow.bazookaArrows.get(arrow).equals(arrow.getUniqueId())) {
						if (arrow.getShooter() instanceof LivingEntity){
						//EntityShootArrow.explodeLoc = event.getEntity().getLocation();
						EntityShootArrow.explodeLoc.put((LivingEntity) arrow.getShooter(), event.getEntity().getLocation());
						//event.setCancelled(true);
						event.setDamage(0);
						arrow.setVelocity(arrow.getVelocity().multiply(0));
						}
					}
				}
			}
		}
		if (plugin.getConfig().getBoolean("TheftWandDrop")) {
			if (event.getDamager() instanceof Player) {
				Player attacker = (Player) event.getDamager();
				if (Tools.isSpecialItem(attacker.getItemInHand(), "theftWand")) {
					int sLuck = 0;
					int bLuck = 0;
					sLuck += Tools.potionEffectLevel(attacker, PotionEffectType.LUCK);
					sLuck -= Tools.potionEffectLevel(attacker, PotionEffectType.UNLUCK);
					bLuck -= Tools.potionEffectLevel(attacker, PotionEffectType.LUCK);
					bLuck += Tools.potionEffectLevel(attacker, PotionEffectType.UNLUCK);
					if (event.getEntity() instanceof Player) {
						Player defender = (Player) event.getEntity();
						if (Tools.Odds(Main.oddsConfig.getInt("TheftWandSuccessChance"), sLuck)) {
							defender.getWorld().dropItem(defender.getLocation(), defender.getItemInHand());
							defender.setItemInHand(null);
							if (Tools.Odds(Main.oddsConfig.getInt("TheftWandBreakChance"), bLuck)) {
								attacker.setItemInHand(null);
								attacker.getWorld().playSound(attacker.getLocation(), Sound.ITEM_SHIELD_BREAK, 1, 1.5F);
							}
						} else {
							PlayerInventory playerinv = defender.getInventory();
							int slot = playerinv.firstEmpty();
							playerinv.setItem(slot, playerinv.getItemInHand());
							playerinv.setItemInHand(null);
						}

					} else {
						if (event.getEntity() instanceof LivingEntity) {
							if (!(event.getEntity() instanceof Player)) {
								LivingEntity e = (LivingEntity) event.getEntity();
								if (Tools.Odds(Main.oddsConfig.getInt("TheftWandMobSuccessChance"), sLuck)) {
									if (e.getEquipment().getItemInHand() != null) {
										if (!(e.getEquipment().getItemInHand().getType().equals(Material.AIR))) {
											e.getWorld().dropItem(e.getLocation(), e.getEquipment().getItemInHand());
											e.getEquipment().setItemInHand(null);
											e.setCanPickupItems(true);
											if (Tools.Odds(Main.oddsConfig.getInt("TheftWandMobBreakChance"), bLuck)) {
												attacker.setItemInHand(null);
												attacker.getWorld().playSound(attacker.getLocation(),
														Sound.ITEM_SHIELD_BREAK, 1, 1.5F);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (plugin.getConfig().getBoolean("LightningStrikeEgg")) {
			if (event.getDamager() instanceof Egg) {
				final Egg egg = (Egg) event.getDamager();
				new BukkitRunnable() {
					@Override
					public void run() {
						if (PlayerThrowEgg.thrower.containsKey(egg)) {
							if (PlayerClick.anvilEggThrown.get(PlayerThrowEgg.thrower.get(egg))) {
								PlayerThrowEgg.targetEntity.put(PlayerThrowEgg.thrower.get(egg), (LivingEntity) event.getEntity());
								PlayerThrowEgg.thrower.remove(egg);
							}
						}
					}
				}.runTaskLater(plugin, 1);
			}
		}
	}
	public void microIceForm(Location targetLocation){
		Location activeLocation = new Location(targetLocation.getWorld(), targetLocation.getX(), targetLocation.getY(),
				targetLocation.getZ());
		// Block below
		activeLocation.setY(targetLocation.getY() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// First Level
		activeLocation.setY(targetLocation.getY());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		//Second level
		activeLocation.setY(targetLocation.getY() + 1);
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
	}
	public void smallIceForm(Location targetLocation) {
		Location activeLocation = new Location(targetLocation.getWorld(), targetLocation.getX(), targetLocation.getY(),
				targetLocation.getZ());
		// Block below
		activeLocation.setY(targetLocation.getY() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// First Level
		activeLocation.setY(targetLocation.getY());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// Second Level
		activeLocation.setY(targetLocation.getY() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// Third Level
		activeLocation.setY(targetLocation.getY() + 2);
		activeLocation.setX(targetLocation.getX());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
	}

	public void largeIceForm(Location targetLocation) {
		Location activeLocation = new Location(targetLocation.getWorld(), targetLocation.getX(), targetLocation.getY(),
				targetLocation.getZ());
		// Below
		activeLocation.setY(targetLocation.getY() - 1);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// First Level
		activeLocation.setY(targetLocation.getY());
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() + 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 2);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// Second Layer
		activeLocation.setY(targetLocation.getY() + 1);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() + 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 2);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// Third layer
		activeLocation.setY(targetLocation.getY() + 2);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
	}

	public void spiderIceForm(Location targetLocation) {
		Location activeLocation = new Location(targetLocation.getWorld(), targetLocation.getX(), targetLocation.getY(),
				targetLocation.getZ());
		// Below
		activeLocation.setY(targetLocation.getY() - 1);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// First Layer
		activeLocation.setX(targetLocation.getX() + 2);
		activeLocation.setY(targetLocation.getY());
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() + 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 2);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// Second Layer
		activeLocation.setY(targetLocation.getY() + 1);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
	}

	public void megaIceForm(Location targetLocation) {
		Location activeLocation = new Location(targetLocation.getWorld(), targetLocation.getX(), targetLocation.getY(),
				targetLocation.getZ());
		// Below
		activeLocation.setY(targetLocation.getY() - 1);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// First Layer
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setY(targetLocation.getY());
		activeLocation.setX(targetLocation.getX() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() + 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 2);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// Second Layer
		activeLocation.setY(targetLocation.getY() + 1);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() + 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 2);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// Third Layer
		activeLocation.setY(targetLocation.getY() + 2);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() + 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ() + 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 2);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 2);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		// Fourth layer
		activeLocation.setY(targetLocation.getY() + 3);
		activeLocation.setZ(targetLocation.getZ());
		activeLocation.setX(targetLocation.getX() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX());
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setX(targetLocation.getX() - 1);
		activeLocation.setZ(targetLocation.getZ());
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() + 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
		activeLocation.setZ(targetLocation.getZ() - 1);
		if (isAirCheck(activeLocation)) {
			activeLocation.getBlock().setType(Material.ICE);
		}
	}

	public Location roundLoc(Location loc) {
		double x = loc.getBlockX();
		double z = loc.getBlockZ();
		double y = loc.getBlockY();
		x += .5;
		z += .5;
		loc.setX(x);
		loc.setY(y);
		loc.setZ(z);
		return loc;
	}

	public boolean isAirCheck(Location loc) {
		if (!(loc.getBlock().getType().equals(Material.AIR))) {
			if (!(loc.getBlock().getType().equals(Material.STATIONARY_WATER))) {
				if (!(loc.getBlock().getType().equals(Material.STATIONARY_LAVA))) {
					if (!(loc.getBlock().getType().equals(Material.WATER))) {
						if (!(loc.getBlock().getType().equals(Material.LAVA))) {
							return false;
						} else {
							return true;
						}
					} else {
						return true;
					}
				} else {
					return true;
				}
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
}

//This is the primary class of the plugin.
package com.ln42.betterdrops;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.ln42.betterdrops.codingforcookies.ArmorListsener;
import com.ln42.betterdrops.commands.PluginCommand;
import com.ln42.betterdrops.event.block.BlockBreak;
import com.ln42.betterdrops.event.entity.EntityDamageEntity;
import com.ln42.betterdrops.event.entity.EntitySpawn;
import com.ln42.betterdrops.event.entity.XPBottleHandler;
import com.ln42.betterdrops.event.entity.EntityShootArrow;
import com.ln42.betterdrops.event.inventory.AnvilEvent;
import com.ln42.betterdrops.event.inventory.BootsEquipEvent;
import com.ln42.betterdrops.event.inventory.EnchantPrepareEvent;
import com.ln42.betterdrops.event.player.PlayerClick;
import com.ln42.betterdrops.event.player.PlayerJoin;
import com.ln42.betterdrops.event.player.PlayerKill;
import com.ln42.betterdrops.event.player.PlayerThrowEgg;
import com.ln42.betterdrops.event.player.PotionDrink;
import com.ln42.betterdrops.event.player.ProjectileLaunch;

public class Main extends JavaPlugin implements Listener {
	public static FileConfiguration oddsConfig = null;
	@SuppressWarnings("rawtypes")
	public static HashMap<String, Class> configEntryType = new HashMap<String, Class>();

	@Override
	public void onEnable() {
		PluginDescriptionFile pdfFile = getDescription();
		registerConfig();
		registerCommands();
		registerEvents();
		registerRecipes();
		Logger logger = getLogger();
		logger.info(pdfFile.getName() + " has been enabled (V." + pdfFile.getVersion() + ")");

	}

	@Override
	public void onDisable() {
		PluginDescriptionFile pdfFile = getDescription();
		Logger logger = getLogger();
		logger.info(pdfFile.getName() + " has been disabled (V." + pdfFile.getVersion() + ")");
		
	}

	public void registerCommands() {
		getCommand("bd").setExecutor(new PluginCommand(this));
	}

	public void registerEvents() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerKill(this), this);
		if (this.getConfig().getBoolean("SpecialBootsDrop")) {
			if (this.getConfig().getBoolean("PreventSpecialItemRepair")) {
				pm.registerEvents(new AnvilEvent(), this);
			}
			if (this.getConfig().getBoolean("PreventSpecialItemEnchant")) {
				pm.registerEvents(new EnchantPrepareEvent(), this);
			}
			pm.registerEvents(new ArmorListsener(getConfig().getStringList("blocked")), this);
		} else if (this.getConfig().getBoolean("PoweredSkeletons")) {
			if (this.getConfig().getBoolean("PreventSpecialItemRepair")) {
				pm.registerEvents(new AnvilEvent(), this);
			}
			if (this.getConfig().getBoolean("PreventSpecialItemEnchant")) {
				pm.registerEvents(new EnchantPrepareEvent(), this);
			}
		}
		if (this.getConfig().getBoolean("PoweredSkeletons")) {
			pm.registerEvents(new EntityShootArrow(this), this);
		}
		if (this.getConfig().getBoolean("SpecialBootsDrop")) {
			// pm.registerEvents(new PlayerWalk(this), this);
			pm.registerEvents(new BootsEquipEvent(this), this);
			pm.registerEvents(new PlayerJoin(this), this);
		}
		if (this.getConfig().getBoolean("SpecialBootsDrop")) {
			pm.registerEvents(new BlockBreak(this), this);
		} else if (this.getConfig().getBoolean("XPStorageDrop")) {
			pm.registerEvents(new BlockBreak(this), this);
		}
		if (this.getConfig().getBoolean("PoweredSkeletons")) {
			pm.registerEvents(new EntityDamageEntity(this), this);
		} else if (this.getConfig().getBoolean("TheftWandDrop")) {
			pm.registerEvents(new EntityDamageEntity(this), this);
		} else if (this.getConfig().getBoolean("LightningStrikeEgg")) {
			pm.registerEvents(new EntityDamageEntity(this), this);
		} else if (this.getConfig().getBoolean("XPStorageDrop")) {
			pm.registerEvents(new EntityDamageEntity(this), this);
		}
		if (this.getConfig().getBoolean("FlightPotionDrop")) {
			pm.registerEvents(new PotionDrink(this), this);
		}
		if (this.getConfig().getBoolean("ShulkerLauncherDrop")) {
			pm.registerEvents(new PlayerClick(this), this);
		} else if (this.getConfig().getBoolean("LightningStrikeEgg")) {
			pm.registerEvents(new PlayerClick(this), this);
		}
		if (this.getConfig().getBoolean("PoweredSkeletons")) {
			pm.registerEvents(new EntitySpawn(this), this);
		}
		if (this.getConfig().getBoolean("LightningStrikeEgg")) {
			pm.registerEvents(new PlayerThrowEgg(this), this);
		}
		if (this.getConfig().getBoolean("XPStorageDrop")) {
			pm.registerEvents(new XPBottleHandler(), this);
		}
		if (this.getConfig().getBoolean("XPStorageDrop")) {
			pm.registerEvents(new ProjectileLaunch(this), this);
		} else if (this.getConfig().getBoolean("LightningStrikeEgg")){
			pm.registerEvents(new ProjectileLaunch(this), this);
		}
	}

	public void registerConfig() {
		getConfig().options().copyDefaults(true);
		oddsConfig = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "odds.yml"));
		saveConfig();
		configEntryType.put("MobHeadsDrop", Boolean.class);
		configEntryType.put("PlayerHeadsDrop", Boolean.class);
		configEntryType.put("PoweredSkeletons", Boolean.class);
		configEntryType.put("PreventPoweredSpawnFromSpawners", Boolean.class);
		configEntryType.put("PoweredSkeletonNameTagVisibility", Integer.class);
		configEntryType.put("SpaceBow", Boolean.class);
		configEntryType.put("IceBow", Boolean.class);
		configEntryType.put("BazookaBow", Boolean.class);
		configEntryType.put("ShotgunBow", Boolean.class);
		configEntryType.put("ShotgunBowSpread", Double.class);
		configEntryType.put("ShotgunArrowsComeFromInventory", Boolean.class);
		configEntryType.put("SkeletonShootSpaceBowKnockbackValue", Integer.class);
		configEntryType.put("PlayerShootSpaceBowKnockbackValue", Integer.class);
		configEntryType.put("SpecialBootsDrop", Boolean.class);
		configEntryType.put("FireBoots", Boolean.class);
		configEntryType.put("SkywalkerBoots", Boolean.class);
		configEntryType.put("LevitationBoots", Boolean.class);
		configEntryType.put("SkywalkBootsBlockDecayDelay", Integer.class);
		configEntryType.put("ShulkerLauncherDrop", Boolean.class);
		configEntryType.put("ShulkerBLCooldown", Integer.class);
		configEntryType.put("ShulkerBLSightRange", Integer.class);
		configEntryType.put("TheftWandDrop", Boolean.class);
		configEntryType.put("FlightPotionDrop", Boolean.class);
		configEntryType.put("LightningStrikeEgg", Boolean.class);
		configEntryType.put("XPStorageDrop", Boolean.class);
		configEntryType.put("PreventSpecialItemRepair", Boolean.class);
		configEntryType.put("PreventSpecialItemEnchant", Boolean.class);
	}

	public void registerRecipes() {
	}
}
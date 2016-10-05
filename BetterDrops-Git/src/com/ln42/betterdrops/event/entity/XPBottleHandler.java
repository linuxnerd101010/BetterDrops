package com.ln42.betterdrops.event.entity;

import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExpBottleEvent;

public class XPBottleHandler implements Listener {
	@EventHandler
	public void onExpBottleThrow(ExpBottleEvent event){
		Entity entity = event.getEntity();
		if (ProjectileLaunch.thrownSpecialItems.containsKey(entity)){
			int amount = ProjectileLaunch.thrownSpecialItems.get(entity);	
			ProjectileLaunch.thrownSpecialItems.remove(entity);
			entity.getServer().broadcastMessage(Integer.toString(amount));
			event.setExperience(amount);
		}
	}
}

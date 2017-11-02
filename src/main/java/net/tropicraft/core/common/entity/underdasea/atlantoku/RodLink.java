package net.tropicraft.core.common.entity.underdasea.atlantoku;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

public class RodLink {
	
	public static HashMap<UUID, EntityHook> floats = new HashMap<UUID, EntityHook>();
	
	
	public static void createLink(EntityHook h, EntityPlayer p){
		if(!floats.containsKey(p.getUniqueID())){
			floats.put(p.getUniqueID(), h);
		}
	}
	
	public static void destroyLink(EntityHook k, EntityPlayer p){
		if(floats.containsKey(p.getUniqueID())){
			floats.get(p.getUniqueID()).setAngler(null);
			floats.remove(p.getUniqueID());
		}
	}
	
	public static EntityPlayer getLinkedPlayer(EntityHook h){
		for(UUID id : floats.keySet()){
			if(floats.get(id).getUniqueID().equals(h.getUniqueID())){
				return h.world.getPlayerEntityByUUID(id);
			}
		}
		return null;
	}
	
	public static boolean playerHasFloat(EntityPlayer p){
		if(floats.containsKey(p.getUniqueID())){
			return true;
		}
		return false;
	}
	
	public static EntityHook getLinkedHook(EntityPlayer p){
		EntityHook h = floats.get(p.getUniqueID());
		return h;
	}

}
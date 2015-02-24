package net.tropicraft.fishing;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tropicraft.entity.EntityHook;

public class LinkedRods {
	
	public HashMap<Integer, EntityHook> floats = new HashMap<Integer, EntityHook>();
	
	
	public void createLink(EntityHook h, EntityPlayer p){
		if(floats.get(p.getEntityId()) == null){
			floats.put(p.getEntityId(), h);
		//	System.out.println("Created Float link!");
		}
	}
	
	public void destroyLink(EntityHook k, EntityPlayer p){
		if(floats.get(p.getEntityId()) != null){
			floats.remove(p.getEntityId());
		//	System.out.println("Removed Float link!");
		}
	}
	
	private Entity getEntById(World w, int i){
		List<Entity> ents = w.loadedEntityList;
		for(Entity e : ents){
			if(e.getEntityId() == i){
				return e;
			}
		}
		return null;
	}
	
	public EntityPlayer getLinkedPlayer(EntityHook h){
		EntityPlayer p = null;
		for(int a : floats.keySet()){
			if(floats.get(a).getEntityId() == h.getEntityId()){
			//	System.out.println("Got linked player!");
				return (EntityPlayer) getEntById(h.worldObj, a);
			}
		}
		return p;
	}
	
	public boolean playerHasFloat(EntityPlayer p){
		boolean b = false;
		if(floats.get(p.getEntityId()) != null){
			b = true;
		}
		return b;
	}
	
	public EntityHook getLinkedHook(EntityPlayer p){
		EntityHook h = null;
		for(int a : floats.keySet()){
			if(a == p.getEntityId()){
			//	System.out.println("Got linked hook!");
				return floats.get(a);
			}
		}
		return h;
	}

}

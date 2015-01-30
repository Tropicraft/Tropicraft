package tropicraft.fishing;

import java.util.HashMap;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class LinkedRods {
	
	public HashMap<Integer, EntityHook> floats = new HashMap<Integer, EntityHook>();
	
	
	public void createLink(EntityHook h, EntityPlayer p){
		if(floats.get(p.entityId) == null){
			floats.put(p.entityId, h);
		//	System.out.println("Created Float link!");
		}
	}
	
	public void destroyLink(EntityHook k, EntityPlayer p){
		if(floats.get(p.entityId) != null){
			floats.remove(p.entityId);
		//	System.out.println("Removed Float link!");
		}
	}
	
	private Entity getEntById(World w, int i){
		List<Entity> ents = w.loadedEntityList;
		for(Entity e : ents){
			if(e.entityId == i){
				return e;
			}
		}
		return null;
	}
	
	public EntityPlayer getLinkedPlayer(EntityHook h){
		EntityPlayer p = null;
		for(int a : floats.keySet()){
			if(floats.get(a).entityId == h.entityId){
			//	System.out.println("Got linked player!");
				return (EntityPlayer) getEntById(h.worldObj, a);
			}
		}
		return p;
	}
	
	public boolean playerHasFloat(EntityPlayer p){
		boolean b = false;
		if(floats.get(p.entityId) != null){
			b = true;
		}
		return b;
	}
	
	public EntityHook getLinkedHook(EntityPlayer p){
		EntityHook h = null;
		for(int a : floats.keySet()){
			if(a == p.entityId){
			//	System.out.println("Got linked hook!");
				return floats.get(a);
			}
		}
		return h;
	}

}

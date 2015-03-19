package net.tropicraft.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.EntityLivingBase;

public class EffectHelper {

	public static List<EffectEntry> listEntities = new ArrayList<EffectEntry>();
	
	public static void addEntry(EntityLivingBase entity) {
		EffectEntry entry = new EffectEntry(entity);
		listEntities.add(entry);
	}
	
	public static void tick() {
		Iterator<EffectEntry> it = listEntities.iterator();
		while (it.hasNext()) {
			EffectEntry entry = it.next();
			if (entry.getEntity() == null || entry.getEntity().isDead || entry.isFinished()) {
				entry.cleanup();
				it.remove();
			} else {
				entry.tick();
			}
		}
	}
	
}

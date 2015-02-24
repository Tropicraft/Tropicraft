package net.tropicraft.fishing;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;

public class FishingManager {
	
	private static ArrayList<FishingEvent> fishingEvents = new ArrayList<FishingEvent>();
	public static boolean debug = false;
	
	public static void debug(String s){
		if(debug)
		System.out.println(s);
	}
	
	public static void initEvent(FishingEvent evt){
		fishingEvents.add(evt);
	}
	
	public static ArrayList<FishingEvent> getEventsForPlayer(EntityPlayer p){
		ArrayList<FishingEvent> match = new ArrayList<FishingEvent>();
		for(FishingEvent evt : fishingEvents){
			if(evt.thePlayer.getEntityId() == p.getEntityId()){
				match.add(evt);
			}
		}
		return match;
	}

	public static void onServerTick(){
		ArrayList<FishingEvent> dirty = new ArrayList<FishingEvent>();
		for(FishingEvent evt : fishingEvents){
			if(evt.currentTick < evt.totalTicks){
				if(evt.currentTick == 0){
					debug(evt.getClass().getSimpleName()+" event started!");
				}
				evt.onUpdate();
				evt.currentTick++;
			}else{
				debug(evt.getClass().getSimpleName()+" event complete!");
				dirty.add(evt);
			}
		}
		for(FishingEvent evt : dirty){
			fishingEvents.remove(evt);
		}
	}

}

package CoroUtil.util;

import java.util.WeakHashMap;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;

public class CoroUtilPlayer {

	public static WeakHashMap<PlayerEntity, Vec3> lookupPlayerToLastPos = new WeakHashMap<>();
	public static WeakHashMap<PlayerEntity, Vec3> lookupPlayerToLastSpeed = new WeakHashMap<>();

	/**
	 * Currently used for tracking player SERVER SIDE speed
	 *
	 * @param player
	 */
	public static void trackPlayerForSpeed(PlayerEntity player) {
		
		//TODO: edge cases like teleporting and respawning, reconnecting, etc
		//cap max speed, we dont need too much accuracy for now
		
		Vec3 vecPos = new Vec3(player.posX, player.posY, player.posZ);
		if (!lookupPlayerToLastPos.containsKey(player)) {
			lookupPlayerToLastPos.put(player, vecPos);
			return;
		} else {
			Vec3 vecLastPos = lookupPlayerToLastPos.get(player);
			
			Vec3 vecDiff = vecPos.subtract(vecLastPos);
			lookupPlayerToLastSpeed.put(player, vecDiff);
			
			lookupPlayerToLastPos.put(player, vecPos);
		}
	}

	/**
	 * Currently used for player SERVER SIDE speed
	 *
	 * @param player
	 * @param max
	 * @return
	 */
	public static Vec3 getPlayerSpeedCapped(PlayerEntity player, float max) {
		if (lookupPlayerToLastSpeed.containsKey(player)) {
			Vec3 vec = lookupPlayerToLastSpeed.get(player);
			if (vec.xCoord > max) vec.xCoord = max;
			if (vec.yCoord > max) vec.yCoord = max;
			if (vec.zCoord > max) vec.zCoord = max;
			return vec;
		} else {
			return new Vec3(0, 0, 0);
		}
	}

}

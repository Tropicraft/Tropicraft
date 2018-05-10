package net.tropicraft.core.common.event;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.core.common.network.MessagePlayerSwimData.PlayerSwimData;
import net.tropicraft.core.registry.ItemRegistry;

public class ScubaHandlerCommon {
	
	public static HashMap<UUID, PlayerSwimData> rotationMap = new HashMap<UUID, PlayerSwimData>();

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			if(isInWater((EntityPlayer)event.getEntity()))
			if(event.getSource().equals(DamageSource.IN_WALL)) {
				event.setCanceled(true);
			}
		}
	}
	
	public float lerp(float x1, float x2, float t) {
		float f = MathHelper.wrapDegrees(x2 - x1);
		if (f > t)
			f = t;
		if (f < -t)
			f = -t;
		return x1 + f;
	}

	public float rangeMap(float input, float inpMin, float inpMax, float outMin, float outMax) {
	    if (Math.abs(inpMax - inpMin) < 1e-12) {
	        return 0;
	    }
	    double ratio = (outMax - outMin) / (inpMax - inpMin);
	    return (float)(ratio * (input - inpMin) + outMin);
	}
	
	public boolean isPlayerWearingFlippers(EntityPlayer p) {
		ItemStack bootSlot = p.getArmorInventoryList().iterator().next();
		if (bootSlot != null)
			return bootSlot.getItem().equals(ItemRegistry.pinkFlippers)
					|| bootSlot.getItem().equals(ItemRegistry.yellowFlippers);
		return false;
	}
	
	public boolean isInWater(EntityPlayer p, double offsetX, double offsetY, double offsetZ) {
		return p.world.isMaterialInBB(p.getEntityBoundingBox().offset(offsetX, offsetY, offsetZ), Material.WATER);
	}
	
	public boolean isInWater(EntityPlayer p) {
		return isInWater(p, 0, 0, 0);
	}
	
	
	private PlayerSwimData getData(EntityPlayer p) {
		if (!rotationMap.containsKey(p.getUniqueID())) {
			rotationMap.put(p.getUniqueID(), new PlayerSwimData(p.getUniqueID()));
		}
		return rotationMap.get(p.getUniqueID());
	}
}

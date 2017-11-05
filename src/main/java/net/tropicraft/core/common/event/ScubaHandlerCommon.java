package net.tropicraft.core.common.event;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.tropicraft.core.common.network.MessagePlayerSwimData.PlayerSwimData;
import net.tropicraft.core.registry.ItemRegistry;

public class ScubaHandlerCommon {
	
	public static HashMap<UUID, PlayerSwimData> rotationMap = new HashMap<UUID, PlayerSwimData>();

	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			if(isInWater((EntityPlayer)event.getEntity()))
			if(event.getSource().equals(DamageSource.inWall)) {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onTickPlayer(PlayerTickEvent event) {
		if (!event.type.equals(TickEvent.Type.PLAYER))
			return;
		EntityPlayer p = event.player;
		
		PlayerSwimData d = getData(p);
		
		boolean inLiquid = isInWater(p);

		if(event.phase.equals(Phase.END)) {
			if(!inLiquid) {
				if(d.targetHeight == d.currentHeight) {
					float f;
			        float f1;

			        if (p.isElytraFlying()) {
			            f = 0.6F;
			            f1 = 0.6F;
			        }
			        else if (p.isPlayerSleeping()) {
			            f = 0.2F;
			            f1 = 0.2F;
			        }
			        else if (p.isSneaking()) {
			            f = 0.6F;
			            f1 = 1.65F;
			        }
			        else {
			            f = 0.6F;
			            f1 = 1.8F;
			        }

			        if (f != 0.3f || f1 != p.height) {
			            AxisAlignedBB axisalignedbb = p.getEntityBoundingBox();
			            axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)f, axisalignedbb.minY + (double)f1, axisalignedbb.minZ + (double)f);

			            if (!p.world.collidesWithAnyBlock(axisalignedbb)) {
			                this.setPlayerSize(p, f, f1, 0f, f1);
			                d.currentHeight = f1;
			               d.targetHeight = f1;
			            }
			        }
			   
			      return;
				}
			}
				
			if(!isInWater(p, 0, -3.2, 0)) { // || p.world.getBlockState(bp).getMaterial().isLiquid()) {
				if(!isPlayerWearingFlippers(p)) {
					d.targetHeight = 1.8f;
				}
			}else {
				d.targetHeight = 1.1f;
			}
			
			if(!isInWater(p, 0, +1.8D, 0)) {
				d.targetHeight = 1.8f;
			}
			
			if(d.currentHeight < d.targetHeight) {
				d.currentHeight += 0.1f;
				if(d.currentHeight > d.targetHeight) {
					d.currentHeight = d.targetHeight;
				}
			}
			if(d.currentHeight > d.targetHeight) {
				d.currentHeight -= 0.1f;

				if(d.currentHeight < d.targetHeight) {
					d.currentHeight = d.targetHeight;
				}
			}
			setPlayerSize(p, 0.6f, d.currentHeight, rangeMap(d.currentHeight, 0.6f, 1.8f, 1.25f, 0f), 1f);
		}
	}
	
	public void setPlayerSize(EntityPlayer p, float x, float y, float offset, float height) {
		AxisAlignedBB axisalignedbb = p.getEntityBoundingBox();
		p.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)x, axisalignedbb.minY +(double)y, axisalignedbb.minZ + (double)x));
		p.posY -= offset;
		p.height = height;
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
		return p.world.isAABBInMaterial(p.getEntityBoundingBox().offset(offsetX, offsetY, offsetZ), Material.WATER);
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

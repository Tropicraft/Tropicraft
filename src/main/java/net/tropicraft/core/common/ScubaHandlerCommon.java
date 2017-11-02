package net.tropicraft.core.common;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.tropicraft.core.client.ScubaHandler;
import net.tropicraft.core.common.network.MessagePlayerSwimData.PlayerSwimData;

public class ScubaHandlerCommon {

	
	public void setPlayerSize(EntityPlayer p, float x, float y, float offset, float height) {
		/*try {
			MethodHandle m = MethodHandles.lookup().unreflect(ReflectionHelper.findMethod(Entity.class, p, new String[] {"func_70105_a", "setSize"}, float.class, float.class));
			m.invoke(p, x, y);
		}catch(Throwable t) {
			t.printStackTrace();
		}*/
		AxisAlignedBB axisalignedbb = p.getEntityBoundingBox();
		p.setEntityBoundingBox(new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)x, axisalignedbb.minY +(double)y, axisalignedbb.minZ + (double)x));
		p.posY -= offset;
		p.isCollidedVertically = false;
		//p.isCollidedVertically = false;
        //p.height = 1f+(float)Math.sin(p.ticksExisted/Math.PI)/2;
		p.height = height;
     //   System.out.println(p.getEyeHeight());
	}
	
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
		//	System.out.println("ow "+event.isCancelable()+" "+event.getSource().damageType);
			event.setCanceled(true);
			if(event.getSource().equals(DamageSource.inWall)) {
				event.setCanceled(true);
			}
			
		//	System.out.println(event.isCanceled());

		}
	}
	
	@SubscribeEvent
	public void onLivingAttack(LivingAttackEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
		//	System.out.println("ow "+event.isCancelable()+" "+event.getSource().damageType);
			event.setCanceled(true);
			if(event.getSource().equals(DamageSource.inWall)) {
				event.setCanceled(true);
			}
			
		//	System.out.println(event.isCanceled());

		}
	}
	
	@SubscribeEvent
	public void onTickPlayer(PlayerTickEvent event) {
		if (!event.type.equals(TickEvent.Type.PLAYER))
			return;
		EntityPlayer p = event.player;
		
		PlayerSwimData d = ScubaHandler.getData(p);
		BlockPos bpd = p.getPosition().down(1);
		BlockPos bpu = p.getPosition().up(1);
		BlockPos bp = p.getPosition();

		if(event.phase.equals(Phase.END)) 
		{
			if(!p.world.getBlockState(bpu).getMaterial().isLiquid()) 
			{
				if(d.targetHeight == d.currentHeight) {
					float f;
			        float f1;

			        if (p.isElytraFlying())
			        {
			            f = 0.6F;
			            f1 = 0.6F;
			        }
			        else if (p.isPlayerSleeping())
			        {
			            f = 0.2F;
			            f1 = 0.2F;
			        }
			        else if (p.isSneaking())
			        {
			            f = 0.6F;
			            f1 = 1.65F;
			        }
			        else
			        {
			            f = 0.6F;
			            f1 = 1.8F;
			        }

			        if (f != 0.3f || f1 != p.height)
			        {
			            AxisAlignedBB axisalignedbb = p.getEntityBoundingBox();
			            axisalignedbb = new AxisAlignedBB(axisalignedbb.minX, axisalignedbb.minY, axisalignedbb.minZ, axisalignedbb.minX + (double)f, axisalignedbb.minY + (double)f1, axisalignedbb.minZ + (double)f);

			            if (!p.world.collidesWithAnyBlock(axisalignedbb))
			            {
			                this.setPlayerSize(p, f, f1, 0f, f1);
			                d.currentHeight = f1;
			               d.targetHeight = f1;
			            }
			        }
			   
			      return;
				}
			}
				
				if(!p.world.getBlockState(bpd).getMaterial().isLiquid()) { // || p.world.getBlockState(bp).getMaterial().isLiquid()) {
					//setPlayerSize(p, 0.6f, 1.8f, 0f, 1f);
					if(!ScubaHandler.isPlayerWearingFlippers(p))
						d.targetHeight = 1.8f;
				}else {
					//setPlayerSize(p, 0.6f, 0.6f, 1f, 1f);
					d.targetHeight = 1.1f;
				}
				
				if(!p.world.getBlockState(bpd).getMaterial().isLiquid() && !p.world.getBlockState(bpu).getMaterial().isLiquid()) {
					d.targetHeight = 1.1f;
				}
				
				if(d.currentHeight < d.targetHeight) {
					d.currentHeight += 0.1f;
					if(d.currentHeight > d.targetHeight) {
						d.currentHeight = d.targetHeight;
					}
				}
				if(d.currentHeight > d.targetHeight) {
					d.currentHeight -= 0.04f;
					if(d.currentHeight < d.targetHeight) {
						d.currentHeight = d.targetHeight;
					}
				}
				//if(d.currentHeight != d.targetHeight)
				setPlayerSize(p, 0.6f, d.currentHeight, rangeMap(d.currentHeight, 0.6f, 1.8f, 1.25f, 0f), 1f);
			/*}
			else {
				
				 
			}*/
		}
	}
	
	public static float rangeMap(float input, float inpMin, float inpMax, float outMin, float outMax) {

	    if (Math.abs(inpMax - inpMin) < 1e-12) {
	        return 0;
	    }
 
	    double ratio = (outMax - outMin) / (inpMax - inpMin);
	    return (float)(ratio * (input - inpMin) + outMin);
	}
}

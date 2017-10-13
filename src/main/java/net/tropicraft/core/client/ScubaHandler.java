package net.tropicraft.core.client;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class ScubaHandler {

	private static ScubaHandler instance;

	public static ScubaHandler getInstance() {
		return instance;
	}

	public ScubaHandler() {
		instance = this;
	}
	
public HashMap<UUID, PlayerSwimData> rotationMap = new HashMap<UUID, PlayerSwimData>();
	
	public PlayerSwimData getData(EntityPlayer p){
		if(!rotationMap.containsKey(p.getUniqueID())) {
			rotationMap.put(p.getUniqueID(), new PlayerSwimData());
		}
		return rotationMap.get(p.getUniqueID());
	}
	
	public static class PlayerSwimData{
		public float rotationYawHead = 0f;
		public float prevRotationYawHead = 0f;
		public float rotationYaw = 0f;
		public float prevRotationYaw = 0f;
		public float renderYawOffset = 0f;
		public float prevRenderYawOffset = 0f;
		public float rotationPitch = 0f;
		public float prevRotationPitch = 0f;
	
		public float targetRotationPitch = 0f;
		public float targetRotationYaw = 0f;
		public float targetRotationRoll = 0f;
		public float currentRotationPitch = 0f;
		public float currentRotationYaw = 0f;
		public float currentRotationRoll = 0f;
		public float pitchSpeed = 3f;
		public float yawSpeed = 2f;
		public float rollSpeed = 2f;
		
		public float targetHeadPitchOffset = 0f;
		public float currentHeadPitchOffset = 0f;
		
		public float currentSwimSpeed = 1f;
		public float targetSwimSpeed = 0f;
		public float swimSpeedAccel = 0.1f;
	}

	@SubscribeEvent
	public void onRenderTick(RenderWorldLastEvent event) {
		if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0) {
			EntityPlayer p = Minecraft.getMinecraft().player;
			PlayerSwimData d = getData(p);
				d.currentRotationPitch = p.rotationPitch;
				d.currentRotationYaw = p.rotationYaw;
				d.currentHeadPitchOffset = 0;
				d.yawSpeed = 5f;
		}
	}

	@SubscribeEvent
	public void onTickPlayer(PlayerTickEvent event) {
		EntityPlayer p = event.player;
		PlayerSwimData d = this.getData(p);

		BlockPos bp = new BlockPos((int)p.posX, (int)p.posY+1, (int)p.posZ);

		if (p.isInWater() && event.player.world.getBlockState(bp).getMaterial().isLiquid()) {
			p.setNoGravity(true);
			d.targetSwimSpeed = p.moveForward;	
			
			d.currentSwimSpeed = lerp(d.currentSwimSpeed, d.targetSwimSpeed, d.swimSpeedAccel*10);
			
			float currentSpeed = d.currentSwimSpeed * 0.1f;
			
			p.motionX = currentSpeed * Math.sin(-p.rotationYawHead* (Math.PI / 180.0));
			p.motionZ = currentSpeed * Math.cos(-p.rotationYawHead * (Math.PI / 180.0));
			p.motionY = (currentSpeed) * Math.sin((-p.rotationPitch) * (Math.PI / 180.0));
	
			if (p.isSneaking()) {
				p.setSneaking(false);
				if (p.motionY > -0.2f) {
					p.motionY -= 0.02f;
				} else {
					p.motionY = -0.2f;
				}
			}

		} else {
			p.setNoGravity(false);
		}
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event) {

		EntityPlayer p = event.getEntityPlayer();
		PlayerSwimData d = this.getData(p);

		if (p.isInWater()) {
			d.targetRotationPitch = 0;
			d.targetRotationYaw = p.rotationYaw;
			d.targetHeadPitchOffset = 45f;
			d.targetRotationRoll = (float)p.moveStrafing * 5;
			
			if(p.moveForward == 0f) {
				d.targetRotationPitch = 0f;
			}
			
			if(p.moveForward < 0f) {
				d.targetRotationPitch = -65f;
				d.targetHeadPitchOffset = -45f;
			}
			
			if(p.moveForward > 0f) {
				d.targetRotationPitch = p.rotationPitch+90f;
				d.targetHeadPitchOffset = p.rotationPitch+90f;

				if(d.targetHeadPitchOffset > 90f) {
					d.targetHeadPitchOffset = 90f;
					
				}
			}
			
			d.currentRotationPitch = lerp(d.currentRotationPitch, d.targetRotationPitch, d.pitchSpeed);
			d.currentHeadPitchOffset = lerp(d.currentHeadPitchOffset, d.targetHeadPitchOffset, d.pitchSpeed);

			d.currentRotationYaw = lerp(d.currentRotationYaw, d.targetRotationYaw, d.yawSpeed);
			d.currentRotationRoll = lerp(d.currentRotationRoll, d.targetRotationRoll, d.rollSpeed);

			GlStateManager.translate(0, 1.5f, 0f);

			GlStateManager.rotate(d.currentRotationYaw, 0f, -1f, 0f);

			GlStateManager.rotate(d.currentRotationPitch, 1f, 0f, 0f);
			
			GlStateManager.rotate(d.currentRotationRoll, 0f, 0f, -1f);
	
			GlStateManager.translate(0, -1.5f, 0f);

			d.rotationYawHead = p.rotationYawHead;
			d.prevRotationYawHead = p.prevRotationYawHead;
			d.rotationYaw = p.rotationYaw;
			d.prevRotationYaw = p.prevRotationYaw;
			d.renderYawOffset = p.renderYawOffset;
			d.prevRenderYawOffset = p.prevRenderYawOffset;
			d.rotationPitch = p.rotationPitch;
			d.prevRotationPitch = p.prevRotationPitch;

			p.rotationYawHead = 0f;
			p.prevRotationYawHead = 0f;
			p.rotationYaw = 0f;
			p.prevRotationYaw = 0f;

			p.renderYawOffset = 0f;
			p.prevRenderYawOffset = 0f;

			p.rotationPitch -= d.currentHeadPitchOffset;
			p.prevRotationPitch -= d.currentHeadPitchOffset;

			event.getRenderer().getMainModel().isSneak = false;
			p.setSneaking(false);
		}
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Post event) {
		EntityPlayer p = event.getEntityPlayer();
		PlayerSwimData d = this.getData(p);
		if (event.getEntityPlayer().isInWater()) {
			event.getEntityPlayer().rotationYawHead = MathHelper.wrapDegrees(d.rotationYawHead);
			event.getEntityPlayer().prevRotationYawHead = MathHelper.wrapDegrees(d.prevRotationYawHead);
			event.getEntityPlayer().rotationYaw = MathHelper.wrapDegrees(d.rotationYaw);
			event.getEntityPlayer().prevRotationYaw = MathHelper.wrapDegrees(d.prevRotationYaw);
			event.getEntityPlayer().renderYawOffset = MathHelper.wrapDegrees(d.renderYawOffset);
			event.getEntityPlayer().prevRenderYawOffset = MathHelper.wrapDegrees(d.prevRenderYawOffset);
			event.getEntityPlayer().rotationPitch = MathHelper.wrapDegrees(d.rotationPitch);
			event.getEntityPlayer().prevRotationPitch = MathHelper.wrapDegrees(d.prevRotationPitch);
		}

	}
	
	public float lerp(float x1, float x2, float t) {
		float f = MathHelper.wrapDegrees(x2 - x1);
		if (f > t) f = t;
		if (f < -t) f = -t;	
		return x1 + f;
	}
}

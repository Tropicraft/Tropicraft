package net.tropicraft.core.client;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.tropicraft.core.common.entity.placeable.EntityBeachFloat;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;
import net.tropicraft.core.common.network.MessagePlayerSwimData;
import net.tropicraft.core.common.network.MessagePlayerSwimData.PlayerSwimData;
import net.tropicraft.core.common.network.TCPacketHandler;
import net.tropicraft.core.registry.ItemRegistry;

public class ScubaHandler {

	public final float SWIM_SPEED_ACCEL = 0.008f;
	public final float SWIM_SPEED_ROTATE_YAW = 2f;
	public final float SWIM_SPEED_ROTATE_PITCH = 1f;
	public final float SWIM_SPEED_ROTATE_ROLL = 0.5f;

	public static HashMap<UUID, PlayerSwimData> rotationMap = new HashMap<UUID, PlayerSwimData>();
	private HashMap<Item, Float> flipperSpeedMap = new HashMap<Item, Float>();

	public ScubaHandler() {
		flipperSpeedMap.put(ItemRegistry.pinkFlippers, 1.5f);
		flipperSpeedMap.put(ItemRegistry.yellowFlippers, 1.5f);
	}

	@SubscribeEvent
	public void onRenderViewTick(EntityViewRenderEvent.CameraSetup event) {
		if (event.getEntity().equals(Minecraft.getMinecraft().player)) {
			if (Minecraft.getMinecraft().player != null && !Minecraft.getMinecraft().player.isDead) {
				EntityPlayer p = Minecraft.getMinecraft().player;
				PlayerSwimData d = getData(p);
				event.setRoll(-d.currentRotationRoll * 0.25f);
			}
		}
	}

	@SubscribeEvent
	public void onRenderTick(RenderWorldLastEvent event) {
		EntityPlayer p = Minecraft.getMinecraft().player;
		if (p != null && !p.isDead) {
			if (Minecraft.getMinecraft().gameSettings.thirdPersonView == 0
					|| Minecraft.getMinecraft().currentScreen != null) {
				if (!isInWater(p)) {
					updateSwimDataAngles(p);
				}
				updateSwimRenderAngles(p);
			}
		}
	}

	@SubscribeEvent
	public void onTickPlayer(PlayerTickEvent event) {
		if (!event.type.equals(TickEvent.Type.PLAYER))
			return;
		if (event.player != Minecraft.getMinecraft().player) {
			return;
		}

		EntityPlayer p = event.player;

		if(!p.capabilities.isFlying) {
			PlayerSwimData d = getData(p);
			double above = 1.5D;
			if (d.currentRotationPitch != 0) {
				above = 2D;
			}
			boolean liquidAbove = isInWater(p, 0, above, 0);
			boolean inLiquid = isInWater(p) && isInWater(p, 0, -0.5D, 0);
	
			if (inLiquid && !liquidAbove && d.currentRotationPitch < 45f && d.currentRotationPitch > -15f
					&& (p.posY - (int) p.posY < 0.5f) && isPlayerWearingFlippers(p)) {
				// p.motionY += 0.02f;
	
				d.targetRotationPitch = 0f;
				if (!inLiquid) {
					p.motionY -= 4f;
				}
			}
			if (inLiquid && liquidAbove && isPlayerWearingFlippers(p)) {
	
				p.setNoGravity(true);
				d.targetSwimSpeed = 0f;
	
				if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindForward)) {
					d.targetSwimSpeed = getFlipperSpeed(p);
				}
				if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindBack)) {
					d.targetSwimSpeed = -getFlipperSpeed(p);
				}
				if (p.moveStrafing != 0) {
					d.targetSwimSpeed = getFlipperSpeed(p) / 2;
				}
	
				if (d.currentSwimSpeed < d.targetSwimSpeed) {
					d.currentSwimSpeed += SWIM_SPEED_ACCEL;
	
					if (d.currentSwimSpeed > d.targetSwimSpeed) {
						d.currentSwimSpeed = d.targetSwimSpeed;
					}
				} else if (d.currentSwimSpeed > d.targetSwimSpeed) {
					d.currentSwimSpeed -= SWIM_SPEED_ACCEL;
	
					if (d.currentSwimSpeed < d.targetSwimSpeed) {
						d.currentSwimSpeed = d.targetSwimSpeed;
					}
				}
	
				float currentSpeed = d.currentSwimSpeed * 0.1f;
				float offset = 0f;
	
				p.motionX = currentSpeed * Math.sin(-(d.currentRotationYaw + offset) * (Math.PI / 180.0));
				p.motionZ = currentSpeed * Math.cos(-(d.currentRotationYaw + offset) * (Math.PI / 180.0));
				p.motionY = (currentSpeed)
						* Math.sin((d.currentRotationPitch + d.currentHeadPitchOffset) * (Math.PI / 180.0));
	
				if (p.isSneaking()) {
					p.setSneaking(false);
					if (p.motionY > -0.2f) {
						p.motionY -= 0.02f;
					} else {
						p.motionY = -0.2f;
					}
				}
	
			} else {
				d.targetSwimSpeed = 0f;
				p.setNoGravity(false);
			}
		}
		TCPacketHandler.sendToServer(new MessagePlayerSwimData(getData(p)));
	}

	private boolean inGUI = false;

	@SubscribeEvent
	public void onDrawScreenPre(DrawScreenEvent.Pre event) {
		inGUI = true;
	}

	@SubscribeEvent
	public void onDrawScreenPos(DrawScreenEvent.Post event) {
		inGUI = false;
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Pre event) {
		if (inGUI) {
			return;
		}

		EntityPlayer p = event.getEntityPlayer();
		PlayerSwimData d = getData(p);

		if (p.isElytraFlying() || p.getRidingEntity() instanceof EntityBeachFloat) {
			return;
		}

		updateSwimRenderAngles(p);

		boolean inLiquid = isInWater(p) && isInWater(p, 0, 0.4D, 0);

		if (inLiquid) {
			if (!p.onGround && isPlayerWearingFlippers(p)) {
				p.limbSwingAmount = 0.2f + (d.currentSwimSpeed / 20);
			}
		} else {
			if (d.currentRotationPitch == 0f && d.currentRotationRoll == 0f) {
				return;
			}
		}

		GlStateManager.pushMatrix();

		boolean isSelf = d.playerUUID.equals(Minecraft.getMinecraft().player.getUniqueID());
		GlStateManager.translate(0, 1.5f, 0f);
		if(p.isRiding() && p.getRidingEntity() instanceof EntityTropicraftWaterBase) {
			d.currentRotationYaw = 0f;
			d.currentRotationRoll = 0f;
			d.currentHeadPitchOffset = 0f;
			d.currentRotationPitch = 0f;
			d.rotationYawHead = 90f;
		}
		if (isSelf) {
			GlStateManager.rotate(d.currentRotationYaw, 0f, -1f, 0f);
			GlStateManager.rotate(d.currentRotationPitch, 1f, 0f, 0f);
			GlStateManager.rotate(d.currentRotationRoll, 0f, 0f, -1f);
		} else {
			GlStateManager.translate(event.getX(), event.getY(), event.getZ());
			GlStateManager.rotate(d.currentRotationYaw, 0f, -1f, 0f);
			GlStateManager.rotate(d.currentRotationPitch, 1f, 0f, 0f);
			GlStateManager.rotate(d.currentRotationRoll, 0f, 0f, -1f);
			GlStateManager.translate(-event.getX(), -event.getY(), -event.getZ());
		}
		GlStateManager.translate(0, -1.5f, 0f);

		updateSwimDataAngles(p);
		clearPlayerRenderAngles(p);
	}

	@SubscribeEvent
	public void onRenderPlayer(RenderPlayerEvent.Post event) {
		if (inGUI) {
			return;
		}
		if (event.getEntityPlayer().isElytraFlying() || event.getEntityPlayer().getRidingEntity() instanceof EntityBeachFloat) {
			return;
		}

		EntityPlayer p = event.getEntityPlayer();

		boolean inLiquid = isInWater(p) && isInWater(p, 0, 0.4D, 0);

		if (!inLiquid) {
			if (getData(p).currentRotationPitch == 0f
					&& getData(p).currentRotationRoll == 0f) {
				return;
			}
		}

		restorePlayerRenderAngles(p);

		GlStateManager.popMatrix();
	}

	public boolean isPlayerWearingFlippers(EntityPlayer p) {
		ItemStack bootSlot = p.inventory.armorItemInSlot(0);
		if (bootSlot != null)
			return bootSlot.getItem().equals(ItemRegistry.pinkFlippers)
					|| bootSlot.getItem().equals(ItemRegistry.yellowFlippers);
		return false;
	}

	public float getFlipperSpeed(EntityPlayer p) {
		ItemStack bootSlot = p.inventory.armorItemInSlot(0);
		if (bootSlot != null) {
			if (flipperSpeedMap.containsKey(bootSlot.getItem())) {
				return flipperSpeedMap.get(bootSlot.getItem());
			}
		}
		return 0f;
	}

	public void updateSwimRenderAngles(EntityPlayer p) {
		PlayerSwimData d = getData(p);
		float ps = SWIM_SPEED_ROTATE_PITCH;
		float ys = SWIM_SPEED_ROTATE_YAW;
		float rs = SWIM_SPEED_ROTATE_ROLL;

		boolean liquidAbove = isInWater(p, 0, 1.4D, 0);
		boolean inLiquid = isInWater(p);

		if (inLiquid && liquidAbove) {
			d.targetRotationYaw = p.rotationYaw;
			d.targetHeadPitchOffset = 45f;

			float t = (float) p.moveStrafing * 90;

			if (p.moveStrafing != 0) {
				if (p.moveForward != 0f) {
					t = (float) p.moveStrafing * 45;
				}
				d.targetRotationYaw -= t;
			}
			// Not moving, level out
			if (!isPlayerWearingFlippers(p)) {
				if (p.moveForward == 0f && d.targetSwimSpeed == 0f) {
					d.targetRotationPitch = p.rotationPitch + 90f;
					d.targetHeadPitchOffset = d.targetRotationPitch;
					if (d.targetHeadPitchOffset > 55) {
						d.targetHeadPitchOffset = 55f;
					}
				}
			}

			if (d.targetRotationRoll != 0) {
				d.targetRotationPitch -= 180f;
			}

			if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindJump)
					&& p.equals(Minecraft.getMinecraft().player)) {
				d.targetSwimSpeed = getFlipperSpeed(p);
				d.targetRotationPitch = 0f;
				d.targetHeadPitchOffset = 45f;
			}

			if (GameSettings.isKeyDown(Minecraft.getMinecraft().gameSettings.keyBindSneak)
					&& p.equals(Minecraft.getMinecraft().player)) {
				d.targetSwimSpeed = getFlipperSpeed(p);
				d.targetRotationPitch = 180f;
				d.targetHeadPitchOffset = 45f;
			}
			d.targetRotationPitch += d.targetRotationRoll * 2f;

			// Backpaddle body and head adjustments
			if (p.moveForward < 0f) {
				d.targetRotationPitch = p.rotationPitch - 65f;
				d.targetHeadPitchOffset = p.rotationPitch - 45f;
			}

			// Full speed ahead cap'n
			if (p.moveForward > 0f) {
				d.targetRotationPitch = p.rotationPitch + 90f;
				d.targetHeadPitchOffset = p.rotationPitch + 90f;

				if (d.targetHeadPitchOffset > 90f) {
					d.targetHeadPitchOffset = 90f;

				}
			}

			// If moving sideways, look ahead
			if (p.moveStrafing != 0) {
				if (p.moveForward == 0f) {
					d.targetHeadPitchOffset = p.rotationPitch + 55f;
				} else {
					d.targetHeadPitchOffset = (p.rotationPitch + 55f);

				}
			}

			// Above a floor and not attempting to move
			if ((!isInWater(p, 0, -1.8D, 0) && d.targetSwimSpeed == 0f
					&& !isPlayerWearingFlippers(p))) {
				d.targetRotationPitch = 0f;
				d.targetHeadPitchOffset = 0f;
				d.targetRotationRoll = 0f;
				ps = ps * 4;
				rs = rs * 4;
				ys = ys * 4;
			}
		} else {
			d.targetRotationPitch = 0f;
			d.targetRotationRoll = 0f;
			d.targetHeadPitchOffset = d.targetHeadPitchOffset * 0.8f;
			d.targetRotationYaw = p.rotationYaw;
			ps = ps * 4;
			rs = rs * 4;
			ys = ys * 4;
		}

		d.currentRotationPitch = lerp(MathHelper.wrapDegrees(d.currentRotationPitch),
				MathHelper.wrapDegrees(d.targetRotationPitch), ps);
		d.currentHeadPitchOffset = lerp(MathHelper.wrapDegrees(d.currentHeadPitchOffset),
				MathHelper.wrapDegrees(d.targetHeadPitchOffset), ps);

		d.currentRotationYaw = lerp(d.currentRotationYaw, d.targetRotationYaw, ys);
		d.currentRotationRoll = lerp(d.currentRotationRoll, d.targetRotationRoll, rs);
	}

	public void updateSwimDataAngles(EntityPlayer p) {
		PlayerSwimData d = getData(p);

		d.rotationYawHead = p.rotationYawHead;
		d.prevRotationYawHead = p.prevRotationYawHead;

		d.rotationYaw = p.rotationYaw;
		d.prevRotationYaw = p.prevRotationYaw;
		d.renderYawOffset = p.renderYawOffset;
		d.prevRenderYawOffset = p.prevRenderYawOffset;
		d.rotationPitch = p.rotationPitch;
		d.prevRotationPitch = p.prevRotationPitch;
		
	}

	public void clearPlayerRenderAngles(EntityPlayer p) {
		PlayerSwimData d = getData(p);

		if(p.isRiding() && p.getRidingEntity() instanceof EntityTropicraftWaterBase) {
			EntityTropicraftWaterBase fish = (EntityTropicraftWaterBase)p.getRidingEntity();
			p.rotationYawHead = fish.rotationYawHead;
			p.prevRotationYawHead = fish.prevRotationYawHead;
		}else {
			p.rotationYawHead = 0f;
			p.prevRotationYawHead = 0f;
			
			p.rotationYaw = 0f;
			p.prevRotationYaw = 0f;
		}
		p.renderYawOffset = 0f;
		p.prevRenderYawOffset = 0f;
		p.rotationPitch = -MathHelper.wrapDegrees(d.currentHeadPitchOffset);
		p.prevRotationPitch = -MathHelper.wrapDegrees(d.currentHeadPitchOffset);
	}

	public void restorePlayerRenderAngles(EntityPlayer p) {
		PlayerSwimData d = getData(p);

		p.rotationYawHead = d.rotationYawHead;
		p.prevRotationYawHead = d.prevRotationYawHead;

		p.rotationYaw = d.rotationYaw;
		p.prevRotationYaw = d.prevRotationYaw;
		p.renderYawOffset = d.renderYawOffset;
		p.prevRenderYawOffset = d.prevRenderYawOffset;
		p.rotationPitch = d.rotationPitch;
		p.prevRotationPitch = d.prevRotationPitch;
	}
	
	public boolean isInWater(EntityPlayer p, double offsetX, double offsetY, double offsetZ) {
		return p.world.isMaterialInBB(p.getEntityBoundingBox().offset(offsetX, offsetY, offsetZ), Material.WATER);
	}
	
	public boolean isInWater(EntityPlayer p) {
		return isInWater(p, 0, 0, 0);
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

	private PlayerSwimData getData(EntityPlayer p) {
		if (!rotationMap.containsKey(p.getUniqueID())) {
			rotationMap.put(p.getUniqueID(), new PlayerSwimData(p.getUniqueID()));
		}
		return rotationMap.get(p.getUniqueID());
	}
}

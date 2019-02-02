package net.tropicraft.core.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.common.network.MessagePlayerSwimData;
import net.tropicraft.core.common.network.MessagePlayerSwimData.PlayerSwimData;

@SideOnly(Side.CLIENT)
public class PlayerSwimDataClientHandler implements IMessageHandler<MessagePlayerSwimData, IMessage> {

	@Override
	public IMessage onMessage(MessagePlayerSwimData message, MessageContext ctx) {
		// We received this on the client, update other player position info
		if(ctx.side.equals(Side.CLIENT)) {
			PlayerSwimData d = message.data;

			if (!ScubaHandler.rotationMap.containsKey(d.playerUUID)) {
				ScubaHandler.rotationMap.put(d.playerUUID, new PlayerSwimData(d.playerUUID));
			}
			PlayerSwimData localData = ScubaHandler.rotationMap.get(d.playerUUID);
			
			if(Minecraft.getMinecraft().player == null || d.playerUUID.equals(Minecraft.getMinecraft().player.getUniqueID())) {
				// We don't want to imprint our own movements from the server onto ourselves
				return null;
			}
			
			localData.rotationYawHead = d.rotationYawHead;
			localData.prevRotationYawHead = d.prevRotationYawHead;
			localData.rotationYaw = d.rotationYaw;
			localData.prevRotationYaw = d.prevRotationYaw;
			localData.renderYawOffset = d.renderYawOffset;
			localData.prevRenderYawOffset = d.prevRenderYawOffset;
			localData.rotationPitch = d.rotationPitch;
			localData.prevRotationPitch = d.prevRotationPitch;

			localData.targetRotationPitch = d.targetRotationPitch;
			localData.targetRotationYaw = d.targetRotationYaw;
			localData.targetRotationRoll = d.targetRotationRoll;
			
			localData.currentRotationPitch = d.currentRotationPitch;
			localData.currentRotationYaw = d.currentRotationYaw;
			localData.currentRotationRoll = d.currentRotationRoll;
			
			localData.targetHeadPitchOffset = d.targetHeadPitchOffset;
			localData.currentHeadPitchOffset = d.currentHeadPitchOffset;

			localData.currentSwimSpeed = d.currentSwimSpeed;
			localData.targetSwimSpeed = d.targetSwimSpeed;
		}
		return null;
	}
}
package net.tropicraft.core.common.network;

import java.nio.charset.Charset;
import java.util.UUID;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.tropicraft.core.common.event.ScubaHandlerCommon;

public class MessagePlayerSwimData implements IMessage{
	
	public PlayerSwimData data;
	
	public MessagePlayerSwimData() { }
	public MessagePlayerSwimData(PlayerSwimData d) {

		data = d;
	}
	
	@Override
	public void fromBytes(ByteBuf buf) {
		int len = buf.readInt();
		UUID uuid = UUID.fromString(new String(buf.readBytes(len).toString(Charset.defaultCharset())));
		if(data == null) {
			data = new PlayerSwimData(uuid);
		}
		data.rotationYawHead = buf.readFloat();
		data.prevRotationYawHead = buf.readFloat();
		data.rotationYaw = buf.readFloat();
		data.prevRotationYaw = buf.readFloat();
		data.renderYawOffset = buf.readFloat();
		data.prevRenderYawOffset = buf.readFloat();
		data.rotationPitch = buf.readFloat();
		data.prevRotationPitch = buf.readFloat();

		data.targetRotationPitch = MathHelper.wrapDegrees(buf.readFloat());
		data.targetRotationYaw = MathHelper.wrapDegrees(buf.readFloat());
		data.targetRotationRoll = MathHelper.wrapDegrees(buf.readFloat());
		
		data.currentRotationPitch = buf.readFloat();
		data.currentRotationYaw = buf.readFloat();
		data.currentRotationRoll = buf.readFloat();
		
		data.targetHeadPitchOffset = buf.readFloat();
		data.currentHeadPitchOffset = buf.readFloat();

		data.currentSwimSpeed = buf.readFloat();
		data.targetSwimSpeed = buf.readFloat();
		
		data.currentHeight = buf.readFloat();
		data.targetHeight = buf.readFloat();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		
		buf.writeInt(data.playerUUID.toString().getBytes().length);
		buf.writeBytes(data.playerUUID.toString().getBytes());
		buf.writeFloat(data.rotationYawHead);
		buf.writeFloat(data.prevRotationYawHead);
		buf.writeFloat(data.rotationYaw);
		buf.writeFloat(data.prevRotationYawHead);
		buf.writeFloat(data.renderYawOffset);
		buf.writeFloat(data.prevRenderYawOffset);
		buf.writeFloat(data.rotationPitch);
		buf.writeFloat(data.prevRotationYawHead);

		buf.writeFloat(data.targetRotationPitch);
		buf.writeFloat(data.targetRotationYaw);
		buf.writeFloat(data.targetRotationRoll);
		
		buf.writeFloat(data.currentRotationPitch);
		buf.writeFloat(data.currentRotationYaw);
		buf.writeFloat(data.currentRotationRoll);

		buf.writeFloat(data.targetHeadPitchOffset);
		buf.writeFloat(data.currentHeadPitchOffset);

		buf.writeFloat(data.currentSwimSpeed);
		buf.writeFloat(data.targetSwimSpeed);

		buf.writeFloat(data.currentHeight);
		buf.writeFloat(data.targetHeight);

	}

	public static class PlayerSwimData {
		
		public PlayerSwimData(UUID associatedPlayer) {
			this.playerUUID = associatedPlayer;
		}
		
		public UUID playerUUID;
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
		
		public float targetHeadPitchOffset = 0f;
		public float currentHeadPitchOffset = 0f;

		public float currentSwimSpeed = 0f;
		public float targetSwimSpeed = 0f;
		
		public float currentHeight = 1.8f;
		public float targetHeight = 1.8f;
	}
	
	
	public static final class Handler implements IMessageHandler<MessagePlayerSwimData, IMessage> {
		@Override
		public IMessage onMessage(MessagePlayerSwimData message, MessageContext ctx) {
			// We received this on the server, send to all other players
			if(ctx.side.equals(Side.SERVER)) {
			
				EntityPlayerMP player = ctx.getServerHandler().playerEntity;
				if (!ScubaHandlerCommon.rotationMap.containsKey(player.getUniqueID())) {
					ScubaHandlerCommon.rotationMap.put(player.getUniqueID(), message.data);
				}else {
					
					// Update out server-side instance
					PlayerSwimData localData = ScubaHandlerCommon.rotationMap.get(player.getUniqueID());
					PlayerSwimData d = message.data;
					
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
				}
				BlockPos p = player.getPosition();
				TCPacketHandler.INSTANCE.sendToAllAround(message, new TargetPoint(player.world.provider.getDimension(), p.getX(), p.getY(), p.getZ(), 32D));
			}
			return null;
		}
	}
}

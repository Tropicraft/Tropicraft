package net.tropicraft.core.common.network.message;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;

import java.util.function.Supplier;

public class MessageMixerStart extends MessageTileEntity<DrinkMixerTileEntity> {

	public MessageMixerStart() {
		super();
	}

	public MessageMixerStart(DrinkMixerTileEntity sifter) {
		super(sifter);
	}

	public static void encode(final MessageMixerStart message, final PacketBuffer buf) {
		MessageTileEntity.encode(message, buf);
	}

	public static MessageMixerStart decode(final PacketBuffer buf) {
		final MessageMixerStart message = new MessageMixerStart();
		MessageTileEntity.decode(message, buf);
		return message;
	}

	public static void handle(final MessageMixerStart message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final DrinkMixerTileEntity te = message.getClientTileEntity();
			if (te != null) {
				te.startMixing();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}

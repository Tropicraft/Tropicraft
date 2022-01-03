package net.tropicraft.core.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;

import java.util.function.Supplier;

public class MessageMixerStart extends MessageTileEntity<DrinkMixerBlockEntity> {

	public MessageMixerStart() {
		super();
	}

	public MessageMixerStart(DrinkMixerBlockEntity sifter) {
		super(sifter);
	}

	public static void encode(final MessageMixerStart message, final FriendlyByteBuf buf) {
		MessageTileEntity.encode(message, buf);
	}

	public static MessageMixerStart decode(final FriendlyByteBuf buf) {
		final MessageMixerStart message = new MessageMixerStart();
		MessageTileEntity.decode(message, buf);
		return message;
	}

	public static void handle(final MessageMixerStart message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final DrinkMixerBlockEntity te = message.getClientTileEntity();
			if (te != null) {
				te.startMixing();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}

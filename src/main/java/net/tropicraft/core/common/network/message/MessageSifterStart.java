package net.tropicraft.core.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

import java.util.function.Supplier;

public class MessageSifterStart extends MessageTileEntity<SifterTileEntity> {

	public MessageSifterStart() {
		super();
	}

	public MessageSifterStart(SifterTileEntity sifter) {
		super(sifter);
	}

	public static void encode(final MessageSifterStart message, final FriendlyByteBuf buf) {
		MessageTileEntity.encode(message, buf);
	}

	public static MessageSifterStart decode(final FriendlyByteBuf buf) {
		final MessageSifterStart message = new MessageSifterStart();
		MessageTileEntity.decode(message, buf);
		return message;
	}

	public static void handle(final MessageSifterStart message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final SifterTileEntity sifter = message.getClientTileEntity();
			if (sifter != null) {
				sifter.startSifting();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}

package net.tropicraft.core.common.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

import java.util.function.Supplier;

public class MessageSifterStart extends MessageTileEntity<SifterBlockEntity> {

	public MessageSifterStart() {
		super();
	}

	public MessageSifterStart(SifterBlockEntity sifter) {
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
			final SifterBlockEntity sifter = message.getClientTileEntity();
			if (sifter != null) {
				sifter.startSifting();
			}
		});
		ctx.get().setPacketHandled(true);
	}
}

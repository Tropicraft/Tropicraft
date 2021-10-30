package net.tropicraft.core.common.network.message;

import net.minecraft.world.item.ItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.SifterTileEntity;

import java.util.function.Supplier;

public class MessageSifterInventory extends MessageTileEntity<SifterTileEntity> {

	private ItemStack siftItem;

	public MessageSifterInventory() {
		super();
	}

	public MessageSifterInventory(SifterTileEntity sifter) {
		super(sifter);
		siftItem = sifter.getSiftItem();
	}

	public static void encode(final MessageSifterInventory message, final FriendlyByteBuf buf) {
		MessageTileEntity.encode(message, buf);
		buf.writeItem(message.siftItem);
	}

	public static MessageSifterInventory decode(final FriendlyByteBuf buf) {
		final MessageSifterInventory message = new MessageSifterInventory();
		MessageTileEntity.decode(message, buf);
		message.siftItem = buf.readItem();
		return message;
	}

	public static void handle(final MessageSifterInventory message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			SifterTileEntity sifter = message.getClientTileEntity();
			if (sifter != null) {
				sifter.setSiftItem(message.siftItem);
			}
		});
		ctx.get().setPacketHandled(true);
	}
}

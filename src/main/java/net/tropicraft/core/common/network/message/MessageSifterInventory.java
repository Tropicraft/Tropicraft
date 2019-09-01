package net.tropicraft.core.common.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
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

	public static void encode(final MessageSifterInventory message, final PacketBuffer buf) {
		MessageTileEntity.encode(message, buf);
		buf.writeItemStack(message.siftItem);
	}

	public static MessageSifterInventory decode(final PacketBuffer buf) {
		final MessageSifterInventory message = new MessageSifterInventory();
		MessageTileEntity.decode(message, buf);
		message.siftItem = buf.readItemStack();
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

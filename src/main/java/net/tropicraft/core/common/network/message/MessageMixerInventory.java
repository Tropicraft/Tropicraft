package net.tropicraft.core.common.network.message;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;

import java.util.function.Supplier;

public class MessageMixerInventory extends MessageTileEntity<DrinkMixerTileEntity> {
	private NonNullList<ItemStack> inventory;
	private ItemStack result = ItemStack.EMPTY;

	public MessageMixerInventory() {
		super();
	}

	public MessageMixerInventory(final DrinkMixerTileEntity mixer) {
		super(mixer);
		inventory = mixer.ingredients;
		result = mixer.result;
	}

	public static void encode(final MessageMixerInventory message, final PacketBuffer buf) {
		MessageTileEntity.encode(message, buf);

		buf.writeByte(message.inventory.size());
		for (final ItemStack i : message.inventory) {
			buf.writeItemStack(i);
		}

		buf.writeItemStack(message.result);
	}

	public static MessageMixerInventory decode(final PacketBuffer buf) {
		final MessageMixerInventory message = new MessageMixerInventory();
		MessageTileEntity.decode(message, buf);
		message.inventory = NonNullList.withSize(buf.readByte(), ItemStack.EMPTY);
		for (int i = 0; i < message.inventory.size(); i++) {
			message.inventory.set(i, buf.readItemStack());
		}

		message.result = buf.readItemStack();

		return message;
	}

	public static void handle(final MessageMixerInventory message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final DrinkMixerTileEntity mixer = message.getClientTileEntity();
			if (mixer != null) {
				mixer.ingredients = message.inventory;
				mixer.result = message.result;
			}
		});
		ctx.get().setPacketHandled(true);
	}
}

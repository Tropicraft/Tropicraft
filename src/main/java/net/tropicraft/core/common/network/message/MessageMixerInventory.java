package net.tropicraft.core.common.network.message;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;

import java.util.function.Supplier;

public class MessageMixerInventory extends MessageTileEntity<DrinkMixerBlockEntity> {
	private NonNullList<ItemStack> inventory;
	private ItemStack result = ItemStack.EMPTY;

	public MessageMixerInventory() {
		super();
	}

	public MessageMixerInventory(final DrinkMixerBlockEntity mixer) {
		super(mixer);
		inventory = mixer.ingredients;
		result = mixer.result;
	}

	public static void encode(final MessageMixerInventory message, final FriendlyByteBuf buf) {
		MessageTileEntity.encode(message, buf);

		buf.writeByte(message.inventory.size());
		for (final ItemStack i : message.inventory) {
			buf.writeItem(i);
		}

		buf.writeItem(message.result);
	}

	public static MessageMixerInventory decode(final FriendlyByteBuf buf) {
		final MessageMixerInventory message = new MessageMixerInventory();
		MessageTileEntity.decode(message, buf);
		message.inventory = NonNullList.withSize(buf.readByte(), ItemStack.EMPTY);
		for (int i = 0; i < message.inventory.size(); i++) {
			message.inventory.set(i, buf.readItem());
		}

		message.result = buf.readItem();

		return message;
	}

	public static void handle(final MessageMixerInventory message, Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final DrinkMixerBlockEntity mixer = message.getClientTileEntity();
			if (mixer != null) {
				mixer.ingredients = message.inventory;
				mixer.result = message.result;
			}
		});
		ctx.get().setPacketHandled(true);
	}
}

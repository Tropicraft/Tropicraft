package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;

import java.util.List;
import java.util.function.Supplier;

public record MessageMixerInventory(BlockPos pos, NonNullList<ItemStack> inventory, ItemStack result) {
    public MessageMixerInventory(final DrinkMixerBlockEntity mixer) {
        this(mixer.getBlockPos(), mixer.ingredients, mixer.result);
    }

    public static MessageMixerInventory decode(final FriendlyByteBuf buf) {
        BlockPos pos = buf.readBlockPos();
        List<ItemStack> items = buf.readList(FriendlyByteBuf::readItem);
        NonNullList<ItemStack> inventory = NonNullList.withSize(items.size(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.size(); i++) {
            inventory.set(i, items.get(i));
        }
        ItemStack result = buf.readItem();
        return new MessageMixerInventory(pos, inventory, result);
    }

    public void encode(final FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeCollection(inventory, FriendlyByteBuf::writeItem);
        buf.writeItem(result);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getExistingBlockEntity(pos) instanceof DrinkMixerBlockEntity mixer) {
            mixer.ingredients = inventory;
            mixer.result = result;
        }
    }
}

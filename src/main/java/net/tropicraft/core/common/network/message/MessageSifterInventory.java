package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

import java.util.function.Supplier;

public record MessageSifterInventory(BlockPos pos, ItemStack siftItem) {
    public MessageSifterInventory(SifterBlockEntity sifter) {
        this(sifter.getBlockPos(), sifter.getSiftItem());
    }

    public MessageSifterInventory(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readItem());
    }

    public void encode(final FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeItem(siftItem);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getExistingBlockEntity(pos) instanceof SifterBlockEntity sifter) {
            sifter.setSiftItem(siftItem);
        }
    }
}
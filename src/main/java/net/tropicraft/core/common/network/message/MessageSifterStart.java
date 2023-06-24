package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

import java.util.function.Supplier;

public record MessageSifterStart(BlockPos pos) {
    public void encode(final FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public MessageSifterStart(final FriendlyByteBuf buf) {
        this(buf.readBlockPos());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getExistingBlockEntity(pos) instanceof SifterBlockEntity sifter) {
            sifter.startSifting();
        }
    }
}

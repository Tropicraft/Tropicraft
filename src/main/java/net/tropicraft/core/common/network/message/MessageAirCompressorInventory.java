package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;

import java.util.function.Supplier;

public record MessageAirCompressorInventory(BlockPos pos, ItemStack tank) {
    public MessageAirCompressorInventory(AirCompressorBlockEntity airCompressor) {
        this(airCompressor.getBlockPos(), airCompressor.getTankStack());
    }

    public MessageAirCompressorInventory(FriendlyByteBuf buf) {
        this(buf.readBlockPos(), buf.readItem());
    }

    public void encode(final FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeItem(tank);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getExistingBlockEntity(pos) instanceof AirCompressorBlockEntity compressor) {
            if (!tank.isEmpty()) {
                compressor.addTank(tank);
            } else {
                compressor.ejectTank();
            }
        }
    }
}
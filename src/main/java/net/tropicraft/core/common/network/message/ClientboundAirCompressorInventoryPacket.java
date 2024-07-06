package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;

public record ClientboundAirCompressorInventoryPacket(BlockPos pos, ItemStack tank) implements CustomPacketPayload {
    public static final Type<ClientboundAirCompressorInventoryPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "air_compressor_inventory"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAirCompressorInventoryPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientboundAirCompressorInventoryPacket::pos,
            ItemStack.OPTIONAL_STREAM_CODEC, ClientboundAirCompressorInventoryPacket::tank,
            ClientboundAirCompressorInventoryPacket::new
    );

    public ClientboundAirCompressorInventoryPacket(AirCompressorBlockEntity airCompressor) {
        this(airCompressor.getBlockPos(), airCompressor.getTankStack());
    }

    public static void handle(ClientboundAirCompressorInventoryPacket packet, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getBlockEntity(packet.pos) instanceof AirCompressorBlockEntity compressor) {
            if (!packet.tank.isEmpty()) {
                compressor.addTank(packet.tank);
            } else {
                compressor.ejectTank();
            }
        }
    }

    @Override
    public Type<ClientboundAirCompressorInventoryPacket> type() {
        return TYPE;
    }
}

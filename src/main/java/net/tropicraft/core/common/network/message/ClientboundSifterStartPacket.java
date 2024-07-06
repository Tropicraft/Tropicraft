package net.tropicraft.core.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

public record ClientboundSifterStartPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ClientboundSifterStartPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "sifter_start"));

    public static final StreamCodec<ByteBuf, ClientboundSifterStartPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientboundSifterStartPacket::pos,
            ClientboundSifterStartPacket::new
    );

    public static void handle(ClientboundSifterStartPacket packet, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getBlockEntity(packet.pos) instanceof SifterBlockEntity sifter) {
            sifter.startSifting();
        }
    }

    @Override
    public Type<ClientboundSifterStartPacket> type() {
        return TYPE;
    }
}

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
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;

public record ClientboundMixerStartPacket(BlockPos pos) implements CustomPacketPayload {
    public static final Type<ClientboundMixerStartPacket> TYPE = new Type<>(Tropicraft.location("mixer_start"));

    public static final StreamCodec<ByteBuf, ClientboundMixerStartPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientboundMixerStartPacket::pos,
            ClientboundMixerStartPacket::new
    );

    public static void handle(ClientboundMixerStartPacket packet, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getBlockEntity(packet.pos) instanceof DrinkMixerBlockEntity drinkMixer) {
            drinkMixer.startMixing();
        }
    }

    @Override
    public Type<ClientboundMixerStartPacket> type() {
        return TYPE;
    }
}

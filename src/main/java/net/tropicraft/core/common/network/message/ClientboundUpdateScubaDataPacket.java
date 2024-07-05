package net.tropicraft.core.common.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tropicraft.Constants;
import net.tropicraft.core.common.item.scuba.ScubaData;

public record ClientboundUpdateScubaDataPacket(ScubaData data) implements CustomPacketPayload {
    public static final Type<ClientboundUpdateScubaDataPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Constants.MODID, "update_scuba_data"));

    public static final StreamCodec<ByteBuf, ClientboundUpdateScubaDataPacket> STREAM_CODEC = StreamCodec.composite(
            ScubaData.STREAM_CODEC, ClientboundUpdateScubaDataPacket::data,
            ClientboundUpdateScubaDataPacket::new
    );

    public static void handle(ClientboundUpdateScubaDataPacket packet, IPayloadContext ctx) {
        ScubaData data = Minecraft.getInstance().player.getData(ScubaData.ATTACHMENT);
        data.copyFrom(packet.data);
    }

    @Override
    public Type<ClientboundUpdateScubaDataPacket> type() {
        return TYPE;
    }
}

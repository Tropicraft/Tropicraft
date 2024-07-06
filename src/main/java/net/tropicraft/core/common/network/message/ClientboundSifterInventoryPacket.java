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
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;

public record ClientboundSifterInventoryPacket(BlockPos pos, ItemStack siftItem) implements CustomPacketPayload {
    public static final Type<ClientboundSifterInventoryPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Tropicraft.ID, "sifter_inventory"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSifterInventoryPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientboundSifterInventoryPacket::pos,
            ItemStack.OPTIONAL_STREAM_CODEC, ClientboundSifterInventoryPacket::siftItem,
            ClientboundSifterInventoryPacket::new
    );

    public ClientboundSifterInventoryPacket(SifterBlockEntity sifter) {
        this(sifter.getBlockPos(), sifter.getSiftItem());
    }

    public static void handle(ClientboundSifterInventoryPacket packet, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getBlockEntity(packet.pos) instanceof SifterBlockEntity sifter) {
            sifter.setSiftItem(packet.siftItem);
        }
    }

    @Override
    public Type<ClientboundSifterInventoryPacket> type() {
        return TYPE;
    }
}

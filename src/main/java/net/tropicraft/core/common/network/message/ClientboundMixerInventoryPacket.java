package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;
import net.tropicraft.core.common.drinks.DrinkIngredient;

import java.util.List;

public record ClientboundMixerInventoryPacket(BlockPos pos, List<Holder<DrinkIngredient>> inventory, ItemStack result) implements CustomPacketPayload {
    public static final Type<ClientboundMixerInventoryPacket> TYPE = new Type<>(Tropicraft.location("mixer_inventory"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundMixerInventoryPacket> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC, ClientboundMixerInventoryPacket::pos,
            DrinkIngredient.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundMixerInventoryPacket::inventory,
            ItemStack.OPTIONAL_STREAM_CODEC, ClientboundMixerInventoryPacket::result,
            ClientboundMixerInventoryPacket::new
    );

    public ClientboundMixerInventoryPacket(DrinkMixerBlockEntity mixer) {
        this(mixer.getBlockPos(), mixer.getDrinkIngredients(), mixer.result);
    }

    public static void handle(ClientboundMixerInventoryPacket packet, IPayloadContext ctx) {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null && level.getBlockEntity(packet.pos) instanceof DrinkMixerBlockEntity mixer) {
            mixer.setDrinkIngredients(List.copyOf(packet.inventory));
            mixer.result = packet.result;
        }
    }

    @Override
    public Type<ClientboundMixerInventoryPacket> type() {
        return TYPE;
    }
}

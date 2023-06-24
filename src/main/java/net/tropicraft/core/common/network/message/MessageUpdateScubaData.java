package net.tropicraft.core.common.network.message;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.network.NetworkEvent;
import net.tropicraft.core.common.item.scuba.ScubaData;

import java.util.function.Supplier;

public record MessageUpdateScubaData(ScubaData data) {

    public static MessageUpdateScubaData decode(final FriendlyByteBuf buf) {
        ScubaData data = new ScubaData();
        data.deserializeBuffer(buf);
        return new MessageUpdateScubaData(data);
    }

    public void encode(final FriendlyByteBuf buf) {
        data.serializeBuffer(buf);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        LazyOptional<ScubaData> data = Minecraft.getInstance().player.getCapability(ScubaData.CAPABILITY);
        data.ifPresent(d -> d.copyFrom(this.data));
    }
}

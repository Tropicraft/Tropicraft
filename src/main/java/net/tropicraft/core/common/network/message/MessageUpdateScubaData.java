package net.tropicraft.core.common.network.message;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkEvent;
import net.tropicraft.core.common.item.scuba.ScubaData;
import net.tropicraft.core.common.network.TropicraftMessage;


public class MessageUpdateScubaData implements TropicraftMessage {
    
    private final ScubaData data;

    public MessageUpdateScubaData(ScubaData data) {
        this.data = data;
    }

    public static void encode(final MessageUpdateScubaData message, final PacketBuffer buf) {
        message.data.serializeBuffer(buf);
    }

    public static MessageUpdateScubaData decode(final PacketBuffer buf) {
        ScubaData data = new ScubaData();
        data.deserializeBuffer(buf);
        return new MessageUpdateScubaData(data);
    }

    public static void handle(final MessageUpdateScubaData message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            LazyOptional<ScubaData> data = Minecraft.getInstance().player.getCapability(ScubaData.CAPABILITY);
            data.ifPresent(d -> d.copyFrom(message.data));
        });
        ctx.get().setPacketHandled(true);
    }
}

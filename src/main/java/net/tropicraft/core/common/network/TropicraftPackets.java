package net.tropicraft.core.common.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.network.message.ClientboundAirCompressorInventoryPacket;
import net.tropicraft.core.common.network.message.ClientboundMixerInventoryPacket;
import net.tropicraft.core.common.network.message.ClientboundMixerStartPacket;
import net.tropicraft.core.common.network.message.ClientboundSifterInventoryPacket;
import net.tropicraft.core.common.network.message.ClientboundSifterStartPacket;
import net.tropicraft.core.common.network.message.ClientboundUpdateScubaDataPacket;

@EventBusSubscriber(modid = Constants.MODID, bus = EventBusSubscriber.Bus.MOD)
public class TropicraftPackets {
    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(Tropicraft.getCompatVersion());
        registrar.playToClient(ClientboundSifterInventoryPacket.TYPE, ClientboundSifterInventoryPacket.STREAM_CODEC, ClientboundSifterInventoryPacket::handle);
        registrar.playToClient(ClientboundSifterStartPacket.TYPE, ClientboundSifterStartPacket.STREAM_CODEC, ClientboundSifterStartPacket::handle);
        registrar.playToClient(ClientboundMixerInventoryPacket.TYPE, ClientboundMixerInventoryPacket.STREAM_CODEC, ClientboundMixerInventoryPacket::handle);
        registrar.playToClient(ClientboundMixerStartPacket.TYPE, ClientboundMixerStartPacket.STREAM_CODEC, ClientboundMixerStartPacket::handle);
        registrar.playToClient(ClientboundAirCompressorInventoryPacket.TYPE, ClientboundAirCompressorInventoryPacket.STREAM_CODEC, ClientboundAirCompressorInventoryPacket::handle);
        registrar.playToClient(ClientboundUpdateScubaDataPacket.TYPE, ClientboundUpdateScubaDataPacket.STREAM_CODEC, ClientboundUpdateScubaDataPacket::handle);
    }
}

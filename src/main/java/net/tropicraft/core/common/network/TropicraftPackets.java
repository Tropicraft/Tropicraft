package net.tropicraft.core.common.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.network.message.*;

public class TropicraftPackets {
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MODID, "main"),
            Tropicraft::getCompatVersion,
            Tropicraft::isCompatibleVersion,
            Tropicraft::isCompatibleVersion
    );

    private static int nextMessageId = 0;

    private static int nextMessageId() {
        return nextMessageId++;
    }

    public static void init() {
        CHANNEL.messageBuilder(MessageSifterInventory.class, nextMessageId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MessageSifterInventory::encode)
                .decoder(MessageSifterInventory::new)
                .consumerMainThread(MessageSifterInventory::handle)
                .add();
        CHANNEL.messageBuilder(MessageSifterStart.class, nextMessageId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MessageSifterStart::encode)
                .decoder(MessageSifterStart::new)
                .consumerMainThread(MessageSifterStart::handle)
                .add();
        CHANNEL.messageBuilder(MessageMixerInventory.class, nextMessageId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MessageMixerInventory::encode)
                .decoder(MessageMixerInventory::decode)
                .consumerMainThread(MessageMixerInventory::handle)
                .add();
        CHANNEL.messageBuilder(MessageMixerStart.class, nextMessageId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MessageMixerStart::encode)
                .decoder(MessageMixerStart::new)
                .consumerMainThread(MessageMixerStart::handle)
                .add();
        CHANNEL.messageBuilder(MessageAirCompressorInventory.class, nextMessageId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MessageAirCompressorInventory::encode)
                .decoder(MessageAirCompressorInventory::new)
                .consumerMainThread(MessageAirCompressorInventory::handle)
                .add();
        CHANNEL.messageBuilder(MessageUpdateScubaData.class, nextMessageId(), NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MessageUpdateScubaData::encode)
                .decoder(MessageUpdateScubaData::decode)
                .consumerMainThread(MessageUpdateScubaData::handle)
                .add();
    }
}

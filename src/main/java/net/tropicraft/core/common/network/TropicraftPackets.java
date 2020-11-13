package net.tropicraft.core.common.network;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.tropicraft.Constants;
import net.tropicraft.core.common.network.message.MessageAirCompressorInventory;
import net.tropicraft.core.common.network.message.MessageMixerInventory;
import net.tropicraft.core.common.network.message.MessageMixerStart;
import net.tropicraft.core.common.network.message.MessageSifterInventory;
import net.tropicraft.core.common.network.message.MessageSifterStart;
import net.tropicraft.core.common.network.message.MessageUpdateScubaData;

public class TropicraftPackets {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MODID, "main"),
            () -> Constants.PROTOCOL_VERSION,
            Constants.PROTOCOL_VERSION::equals,
            Constants.PROTOCOL_VERSION::equals
    );

    private static int messageID = 0;

    private static int getUniqueId() {
        return messageID++;
    }

    public static void init() {
        INSTANCE.registerMessage(getUniqueId(), MessageSifterInventory.class, MessageSifterInventory::encode, MessageSifterInventory::decode, MessageSifterInventory::handle);
        INSTANCE.registerMessage(getUniqueId(), MessageSifterStart.class, MessageSifterStart::encode, MessageSifterStart::decode, MessageSifterStart::handle);
        INSTANCE.registerMessage(getUniqueId(), MessageMixerInventory.class, MessageMixerInventory::encode, MessageMixerInventory::decode, MessageMixerInventory::handle);
        INSTANCE.registerMessage(getUniqueId(), MessageMixerStart.class, MessageMixerStart::encode, MessageMixerStart::decode, MessageMixerStart::handle);
        INSTANCE.registerMessage(getUniqueId(), MessageAirCompressorInventory.class, MessageAirCompressorInventory::encode, MessageAirCompressorInventory::decode, MessageAirCompressorInventory::handle);
        INSTANCE.registerMessage(getUniqueId(), MessageUpdateScubaData.class, MessageUpdateScubaData::encode, MessageUpdateScubaData::decode, MessageUpdateScubaData::handle);
    }

    public static void sendToDimension(final TropicraftMessage msg, final DimensionType dimType) {
        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimType), msg);
    }
}

package net.tropicraft.core.common.network;

import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.network.message.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TropicraftPackets {
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(Constants.MODID, "main"),
            () -> Tropicraft.getCompatVersion(),
            Tropicraft::isCompatibleVersion,
            Tropicraft::isCompatibleVersion
    );

    private static final Logger LOGGER = LogManager.getLogger(TropicraftPackets.class);

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

    public static void sendToDimension(final TropicraftMessage msg, final World world) {
        RegistryKey<World> dimension = world.dimension();
        if (world.isClientSide()) {
            LOGGER.warn("Attempted to send packet to dimension on client world", new RuntimeException());
            return;
        }

        INSTANCE.send(PacketDistributor.DIMENSION.with(() -> dimension), msg);
    }
}

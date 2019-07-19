package net.tropicraft;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.sound.Sounds;
import net.tropicraft.core.proxy.ClientProxy;
import net.tropicraft.core.proxy.CommonProxy;
import net.tropicraft.core.proxy.ServerProxy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Constants.MODID)
public class Tropicraft
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup TROPICRAFT_ITEM_GROUP = (new ItemGroup(ItemGroup.getGroupCountSafe(), "tropicraft") {
        @OnlyIn(Dist.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(TropicraftBlocks.RED_ANTHURIUM);
        }
    });

    public static CommonProxy PROXY;

    public Tropicraft() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the enqueueIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
//        // Register the processIMC method for modloading
//        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
        Sounds.init();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }
//
//    private void enqueueIMC(final InterModEnqueueEvent event) {
//        // some example code to dispatch IMC to another mod
//        InterModComms.sendTo(Constants.MODID, "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
//    }
//
//    private void processIMC(final InterModProcessEvent event) {
//        // some example code to receive and process InterModComms from other mods
//        LOGGER.info("Got IMC {}", event.getIMCStream().
//                map(m->m.getMessageSupplier().get()).
//                collect(Collectors.toList()));
//    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(final FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}

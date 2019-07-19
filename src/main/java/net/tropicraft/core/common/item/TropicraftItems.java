package net.tropicraft.core.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;

public class TropicraftItems {



    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {

        }

        private static void registerItem(final RegistryEvent.Register<Item> event, final String name, final Item item) {
            event.getRegistry().register(item.setRegistryName(new ResourceLocation(Constants.MODID, name)));
        }
    }
}

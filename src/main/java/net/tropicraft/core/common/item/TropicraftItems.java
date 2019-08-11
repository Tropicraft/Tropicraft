package net.tropicraft.core.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.core.common.ColorHelper;

public class TropicraftItems {

    public static UmbrellaItem WHITE_UMBRELLA = Builder.umbrella(ColorHelper.Color.WHITE);
    public static UmbrellaItem ORANGE_UMBRELLA = Builder.umbrella(ColorHelper.Color.ORANGE);
    public static UmbrellaItem MAGENTA_UMBRELLA = Builder.umbrella(ColorHelper.Color.MAGENTA);
    public static UmbrellaItem LIGHT_BLUE_UMBRELLA = Builder.umbrella(ColorHelper.Color.LIGHT_BLUE);
    public static UmbrellaItem YELLOW_UMBRELLA = Builder.umbrella(ColorHelper.Color.YELLOW);
    public static UmbrellaItem LIME_UMBRELLA = Builder.umbrella(ColorHelper.Color.LIME);
    public static UmbrellaItem PINK_UMBRELLA = Builder.umbrella(ColorHelper.Color.PINK);
    public static UmbrellaItem GRAY_UMBRELLA = Builder.umbrella(ColorHelper.Color.GRAY);
    public static UmbrellaItem LIGHT_GRAY_UMBRELLA = Builder.umbrella(ColorHelper.Color.LIGHT_GRAY);
    public static UmbrellaItem CYAN_UMBRELLA = Builder.umbrella(ColorHelper.Color.CYAN);
    public static UmbrellaItem PURPLE_UMBRELLA = Builder.umbrella(ColorHelper.Color.PURPLE);
    public static UmbrellaItem BLUE_UMBRELLA = Builder.umbrella(ColorHelper.Color.BLUE);
    public static UmbrellaItem BROWN_UMBRELLA = Builder.umbrella(ColorHelper.Color.BROWN);
    public static UmbrellaItem GREEN_UMBRELLA = Builder.umbrella(ColorHelper.Color.GREEN);
    public static UmbrellaItem RED_UMBRELLA = Builder.umbrella(ColorHelper.Color.RED);
    public static UmbrellaItem BLACK_UMBRELLA = Builder.umbrella(ColorHelper.Color.BLACK);

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
            register(event, "white_umbrella", WHITE_UMBRELLA);
            register(event, "orange_umbrella", ORANGE_UMBRELLA);
            register(event, "magenta_umbrella", MAGENTA_UMBRELLA);
            register(event, "light_blue_umbrella", LIGHT_BLUE_UMBRELLA);
            register(event, "yellow_umbrella", YELLOW_UMBRELLA);
            register(event, "lime_umbrella", LIME_UMBRELLA);
            register(event, "pink_umbrella", PINK_UMBRELLA);
            register(event, "gray_umbrella", GRAY_UMBRELLA);
            register(event, "light_gray_umbrella", LIGHT_GRAY_UMBRELLA);
            register(event, "cyan_umbrella", CYAN_UMBRELLA);
            register(event, "purple_umbrella", PURPLE_UMBRELLA);
            register(event, "blue_umbrella", BLUE_UMBRELLA);
            register(event, "brown_umbrella", BROWN_UMBRELLA);
            register(event, "green_umbrella", GREEN_UMBRELLA);
            register(event, "red_umbrella", RED_UMBRELLA);
            register(event, "black_umbrella", BLACK_UMBRELLA);
        }

        private static void register(final RegistryEvent.Register<Item> event, final String name, final Item item) {
            event.getRegistry().register(item.setRegistryName(new ResourceLocation(Constants.MODID, name)));
        }
    }
}

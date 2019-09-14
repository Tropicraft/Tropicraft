package net.tropicraft.core.common.item;

import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.Foods;
import net.tropicraft.core.common.drinks.Drink;

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

    public static Item BAMBOO_SPEAR = new SwordItem(TropicraftToolTiers.BAMBOO, 3, -2.4F, new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP));
    public static Item SOLONOX_SHELL = Builder.shell();
    public static Item FROX_CONCH = Builder.shell();
    public static Item PAB_SHELL = Builder.shell();
    public static Item RUBE_NAUTILUS = Builder.shell();
    public static Item STARFISH = Builder.shell();
    public static Item TURTLE_SHELL = Builder.shell();
    public static final Item[] SHELLS = new Item[] {
            SOLONOX_SHELL, FROX_CONCH, PAB_SHELL, RUBE_NAUTILUS, STARFISH, TURTLE_SHELL
    };

    public static LoveTropicsShellItem LOVE_TROPICS_SHELL = new LoveTropicsShellItem(new Item.Properties());
    public static Item LEMON = Builder.food(Foods.LEMON);
    public static Item LIME = Builder.food(Foods.LIME);
    public static Item GRAPEFRUIT = Builder.food(Foods.GRAPEFRUIT);
    public static Item ORANGE = Builder.food(Foods.ORANGE);
    public static Item PINEAPPLE_CUBES = Builder.food(Foods.PINEAPPLE_CUBES);
    public static Item COCONUT_CHUNK = Builder.food(Foods.COCONUT_CHUNK);
    public static Item RAW_COFFEE_BEAN = Builder.item();
    public static Item ROASTED_COFFEE_BEAN = Builder.item();
    public static Item COFFEE_BERRY = Builder.item();
    public static Item BAMBOO_MUG = Builder.item();

    // Cocktails
    // TODO - add item jsons
    public static Item LEMONADE = Builder.cocktail(Drink.LEMONADE);
    public static Item LIMEADE = Builder.cocktail(Drink.LIMEADE);
    public static Item ORANGEADE = Builder.cocktail(Drink.ORANGEADE);
    public static Item CAIPIRINHA = Builder.cocktail(Drink.CAIPIRINHA);
    public static Item PINA_COLADA = Builder.cocktail(Drink.PINA_COLADA);
    public static Item COCONUT_WATER = Builder.cocktail(Drink.COCONUT_WATER);
    public static Item MAI_TAI = Builder.cocktail(Drink.MAI_TAI);
    public static Item BLACK_COFFEE = Builder.cocktail(Drink.BLACK_COFFEE);
    // wildcard
    public static Item COCKTAIL = Builder.cocktail(Drink.COCKTAIL);
    // TODO replace with ItemTags
    public static Item[] COCKTAILS = new Item[] {
        LEMONADE, LIMEADE, ORANGEADE, CAIPIRINHA, PINA_COLADA, COCONUT_WATER, MAI_TAI, BLACK_COFFEE
    };

    public static Item WHITE_PEARL = Builder.item();
    public static Item BLACK_PEARL = Builder.item();
    public static Item SCALE = Builder.item();
    public static Item NIGEL_STACHE = new NigelStacheItem(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP));
    public static Item maskSquareZord = Builder.mask(0);
    public static Item maskHornMonkey = Builder.mask(1);
    public static Item maskOblongatron = Builder.mask(2);
    public static Item maskHeadinator = Builder.mask(3);
    public static Item maskSquareHorn = Builder.mask(4);
    public static Item maskScrewAttack = Builder.mask(5);
    public static Item maskTheBrain = Builder.mask(6);
    public static Item maskBatBoy = Builder.mask(7);
    public static Item mask1 = Builder.mask(8);
    public static Item mask2 = Builder.mask(9);
    public static Item mask3 = Builder.mask(10);
    public static Item mask4 = Builder.mask(11);
    public static Item mask5 = Builder.mask(12);

    public static Item[] ASHEN_MASKS = new Item[] {
            maskSquareZord, maskHornMonkey, maskOblongatron, maskHeadinator, maskSquareHorn, maskScrewAttack,
            maskTheBrain, maskBatBoy, mask1, mask2, mask3, mask4, mask5
    };

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

            register(event, "bamboo_spear", BAMBOO_SPEAR);
            register(event, "love_tropics_shell", LOVE_TROPICS_SHELL);
            register(event, "solonox_shell", SOLONOX_SHELL);
            register(event, "frox_conch", FROX_CONCH);
            register(event, "pab_shell", PAB_SHELL);
            register(event, "rube_nautilus", RUBE_NAUTILUS);
            register(event, "starfish", STARFISH);
            register(event, "turtle_shell", TURTLE_SHELL);
            register(event, "lemon", LEMON);
            register(event, "lime", LIME);
            register(event, "grapefruit", GRAPEFRUIT);
            register(event, "orange", ORANGE);
            register(event, "pineapple_cubes", PINEAPPLE_CUBES);
            register(event, "coconut_chunk", COCONUT_CHUNK);
            register(event, "raw_coffee_bean", RAW_COFFEE_BEAN);
            register(event, "roasted_coffee_bean", ROASTED_COFFEE_BEAN);
            register(event, "coffee_berry", COFFEE_BERRY);
            register(event, "bamboo_mug", BAMBOO_MUG);
            register(event, Drink.LEMONADE.name, LEMONADE);
            register(event, Drink.LIMEADE.name, LIMEADE);
            register(event, Drink.ORANGEADE.name, ORANGEADE);
            register(event, Drink.CAIPIRINHA.name, CAIPIRINHA);
            register(event, Drink.PINA_COLADA.name, PINA_COLADA);
            register(event, Drink.COCONUT_WATER.name, COCONUT_WATER);
            register(event, Drink.MAI_TAI.name, MAI_TAI);
            register(event, Drink.BLACK_COFFEE.name, BLACK_COFFEE);
            register(event, Drink.COCKTAIL.name, COCKTAIL);
            register(event, "white_pearl", WHITE_PEARL);
            register(event, "black_pearl", BLACK_PEARL);
            register(event, "scale", SCALE);
        }

        private static void register(final RegistryEvent.Register<Item> event, final String name, final Item item) {
            event.getRegistry().register(item.setRegistryName(new ResourceLocation(Constants.MODID, name)));
        }
    }
}

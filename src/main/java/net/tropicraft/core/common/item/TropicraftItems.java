package net.tropicraft.core.common.item;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableMap;

import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.Foods;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.drinks.Drink;

@EventBusSubscriber(modid = Info.MODID, bus = Bus.MOD)
public class TropicraftItems {
    
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Info.MODID);
    
    public static final RegistryObject<Item> AZURITE = register("azurite_gem", Builder.item());
    public static final RegistryObject<Item> EUDIALYTE = register("eudialyte_gem", Builder.item());
    public static final RegistryObject<Item> ZIRCON = register("zircon_gem", Builder.item());
    public static final RegistryObject<Item> SHAKA = register("shaka_ingot", Builder.item());
    public static final RegistryObject<Item> MANGANESE = register("manganese_ingot", Builder.item());
    
    public static final Map<DyeColor, RegistryObject<UmbrellaItem>> UMBRELLAS = ImmutableMap.copyOf(Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), c -> register(c.getName() + "_umbrella", Builder.umbrella(c)))));
    
    public static final RegistryObject<Item> BAMBOO_STICK = register("bamboo_stick", Builder.item());

    public static final RegistryObject<Item> BAMBOO_SPEAR = register(
            "bamboo_spear", () -> new SwordItem(TropicraftToolTiers.BAMBOO, 3, -2.4F, new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP)));
    public static final RegistryObject<Item> SOLONOX_SHELL = register("solonox_shell", Builder.shell());
    public static final RegistryObject<Item> FROX_CONCH = register("frox_conch", Builder.shell());
    public static final RegistryObject<Item> PAB_SHELL = register("pab_shell", Builder.shell());
    public static final RegistryObject<Item> RUBE_NAUTILUS = register("rube_nautilus", Builder.shell());
    public static final RegistryObject<Item> STARFISH = register("starfish", Builder.shell());
    public static final RegistryObject<Item> TURTLE_SHELL = register("turtle_shell", Builder.shell());
    @SuppressWarnings("unchecked")
    public static final RegistryObject<Item>[] SHELLS = new RegistryObject[] {
            SOLONOX_SHELL, FROX_CONCH, PAB_SHELL, RUBE_NAUTILUS, STARFISH, TURTLE_SHELL
    };

    public static final RegistryObject<LoveTropicsShellItem> LOVE_TROPICS_SHELL = register(
            "love_tropics_shell", () -> new LoveTropicsShellItem(new Item.Properties()));
    public static final RegistryObject<Item> LEMON = register("lemon", Builder.food(Foods.LEMON));
    public static final RegistryObject<Item> LIME = register("lime", Builder.food(Foods.LIME));
    public static final RegistryObject<Item> GRAPEFRUIT = register("grapefruit", Builder.food(Foods.GRAPEFRUIT));
    public static final RegistryObject<Item> ORANGE = register("orange", Builder.food(Foods.ORANGE));
    public static final RegistryObject<Item> PINEAPPLE_CUBES = register("pineapple_cubes", Builder.food(Foods.PINEAPPLE_CUBES));
    public static final RegistryObject<Item> COCONUT_CHUNK = register("coconut_chunk", Builder.food(Foods.COCONUT_CHUNK));
    public static final RegistryObject<Item> RAW_COFFEE_BEAN = register("raw_coffee_bean", Builder.item());
    public static final RegistryObject<Item> ROASTED_COFFEE_BEAN = register("roasted_coffee_bean", Builder.item());
    public static final RegistryObject<Item> COFFEE_BERRY = register("coffee_berry", Builder.item());
    public static final RegistryObject<Item> BAMBOO_MUG = register("bamboo_mug", Builder.item());

    // Cocktails
    // TODO - add item jsons

    // TODO replace with ItemTags
    public static final ImmutableMap<Drink, RegistryObject<Item>> COCKTAILS = ImmutableMap.copyOf(
            Drink.DRINKS.values().stream()
                .collect(Collectors.toMap(Function.identity(), d -> register(d.name, Builder.cocktail(d)))));

    public static final RegistryObject<Item> WHITE_PEARL = register("white_pearl", Builder.item());
    public static final RegistryObject<Item> BLACK_PEARL = register("black_pearl", Builder.item());
    public static final RegistryObject<Item> SCALE = register("scale", Builder.item());
//    public static final RegistryObject<Item> NIGEL_STACHE = register(
//            "", () -> new NigelStacheItem(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP)));
//    public static final RegistryObject<Item> maskSquareZord = register("", Builder.mask(0));
//    public static final RegistryObject<Item> maskHornMonkey = register("", Builder.mask(1));
//    public static final RegistryObject<Item> maskOblongatron = register("", Builder.mask(2));
//    public static final RegistryObject<Item> maskHeadinator = register("", Builder.mask(3));
//    public static final RegistryObject<Item> maskSquareHorn = register("", Builder.mask(4));
//    public static final RegistryObject<Item> maskScrewAttack = register("", Builder.mask(5));
//    public static final RegistryObject<Item> maskTheBrain = register("", Builder.mask(6));
//    public static final RegistryObject<Item> maskBatBoy = register("", Builder.mask(7));
//    public static final RegistryObject<Item> mask1 = register("", Builder.mask(8));
//    public static final RegistryObject<Item> mask2 = register("", Builder.mask(9));
//    public static final RegistryObject<Item> mask3 = register("", Builder.mask(10));
//    public static final RegistryObject<Item> mask4 = register("", Builder.mask(11));
//    public static final RegistryObject<Item> mask5 = register("", Builder.mask(12));
//
//    @SuppressWarnings("unchecked")
//    public static final RegistryObject<Item>[] ASHEN_MASKS = new RegistryObject[] {
//            maskSquareZord, maskHornMonkey, maskOblongatron, maskHeadinator, maskSquareHorn, maskScrewAttack,
//            maskTheBrain, maskBatBoy, mask1, mask2, mask3, mask4, mask5
//    };

    public static final RegistryObject<Item> FRESH_MARLIN = register("fresh_marlin", Builder.food(Foods.FRESH_MARLIN));
    public static final RegistryObject<Item> SEARED_MARLIN = register("seared_marlin", Builder.food(Foods.SEARED_MARLIN));
    public static final RegistryObject<Item> RAW_RAY = register("raw_ray", Builder.item());
    public static final RegistryObject<Item> COOKED_RAY = register("cooked_ray", Builder.food(Foods.COOKED_RAY));
    public static final RegistryObject<Item> FROG_LEG = register("frog_leg", Builder.item());
    public static final RegistryObject<Item> COOKED_FROG_LEG = register("cooked_frog_leg", Builder.food(Foods.COOKED_FROG_LEG));
    public static final RegistryObject<Item> SEA_URCHIN_ROE = register("sea_urchin_roe", Builder.food(Foods.SEA_URCHIN_ROE));
    public static final RegistryObject<Item> TOASTED_NORI = register("toasted_nori", Builder.food(Foods.TOASTED_NORI));
    public static final RegistryObject<Item> RAW_FISH = register("raw_fish", Builder.food(Foods.RAW_FISH));
    public static final RegistryObject<Item> COOKED_FISH = register("cooked_fish", Builder.food(Foods.COOKED_FISH));
    public static final RegistryObject<Item> POISON_FROG_SKIN = register("poison_frog_skin", Builder.item());

    public static final RegistryObject<Item> BAMBOO_ITEM_FRAME = register(
            "bamboo_item_frame", () -> new BambooItemFrameItem(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP)));
 
    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> sup) {
        return ITEMS.register(name, sup);
    }
    
    @SubscribeEvent
    public static void onItemRegister(RegistryEvent.Register<Item> event) {
        ForgeRegistries.BLOCKS.getValues().stream()
            .filter(b -> b instanceof FlowerPotBlock)
            .map(b -> (FlowerPotBlock) b)
            .forEach(b -> {
                if (b.getEmptyPot().getRegistryName().equals(TropicraftBlocks.BAMBOO_FLOWER_POT.getId()) && b.getEmptyPot() != b) {
                    addPlant(TropicraftBlocks.BAMBOO_FLOWER_POT.get(), b);
                } else if (b.func_220276_d().getRegistryName().getNamespace().equals(Info.MODID)) {
                    addPlant((FlowerPotBlock) Blocks.FLOWER_POT, b);
                }
            });
    }
    
    private static void addPlant(FlowerPotBlock empty, FlowerPotBlock full) {
        empty.addPlant(full.func_220276_d().getRegistryName(), full.delegate);
    }
}

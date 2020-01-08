package net.tropicraft.core.common.item;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.DyeColor;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.Foods;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.MOD)
public class TropicraftItems {
    
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, Constants.MODID);
    
    public static final RegistryObject<Item> AZURITE = register("azurite_gem", Builder.item());
    public static final RegistryObject<Item> EUDIALYTE = register("eudialyte_gem", Builder.item());
    public static final RegistryObject<Item> ZIRCON = register("zircon_gem", Builder.item());
    public static final RegistryObject<Item> SHAKA = register("shaka_ingot", Builder.item());
    public static final RegistryObject<Item> MANGANESE = register("manganese_ingot", Builder.item());
    
    public static final Map<DyeColor, RegistryObject<FurnitureItem<UmbrellaEntity>>> UMBRELLAS = ImmutableMap.copyOf(Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), c -> register(c.getName() + "_umbrella", Builder.umbrella(c)))));
    public static final Map<DyeColor, RegistryObject<FurnitureItem<ChairEntity>>> CHAIRS = ImmutableMap.copyOf(Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), c -> register(c.getName() + "_chair", Builder.chair(c)))));
    public static final Map<DyeColor, RegistryObject<FurnitureItem<BeachFloatEntity>>> BEACH_FLOATS = ImmutableMap.copyOf(Arrays.stream(DyeColor.values())
            .collect(Collectors.toMap(Function.identity(), c -> register(c.getName() + "_beach_float", Builder.beachFloat(c)))));
    
    public static final RegistryObject<Item> BAMBOO_STICK = register("bamboo_stick", Builder.item());

    public static final RegistryObject<Item> BAMBOO_SPEAR = register(
            "bamboo_spear", () -> new SpearItem(TropicraftToolTiers.BAMBOO, 3, -2.4F, new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP)));
    public static final RegistryObject<Item> SOLONOX_SHELL = register("solonox_shell", Builder.shell());
    public static final RegistryObject<Item> FROX_CONCH = register("frox_conch", Builder.shell());
    public static final RegistryObject<Item> PAB_SHELL = register("pab_shell", Builder.shell());
    public static final RegistryObject<Item> RUBE_NAUTILUS = register("rube_nautilus", Builder.shell());
    public static final RegistryObject<Item> STARFISH = register("starfish", Builder.shell());
    public static final RegistryObject<Item> TURTLE_SHELL = register("turtle_shell", Builder.shell());

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
    public static final ImmutableMap<Drink, RegistryObject<CocktailItem>> COCKTAILS = ImmutableMap.copyOf(
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
    
    public static final RegistryObject<Item> IGUANA_LEATHER = register("iguana_leather", Builder.item());
    public static final RegistryObject<Item> TROPICAL_FERTILIZER = register("tropical_fertilizer", Builder.item(TropicalFertilizerItem::new));

    public static final RegistryObject<Item> BAMBOO_ITEM_FRAME = register(
            "bamboo_item_frame", () -> new BambooItemFrameItem(new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP)));
    
    public static final ImmutableMap<RecordMusic, RegistryObject<TropicalMusicDiscItem>> MUSIC_DISCS = ImmutableMap.copyOf(Arrays.stream(RecordMusic.values())
            .collect(Collectors.toMap(Function.identity(), type -> register("music_disc_" + type.name().toLowerCase(Locale.ROOT), Builder.musicDisc(type)))));

    public static final RegistryObject<Item> TROPICAL_FISH_BUCKET = register("tropical_fish_bucket", Builder.fishBucket(TropicraftEntities.TROPICAL_FISH));
    public static final RegistryObject<Item> SARDINE_BUCKET = register("sardine_bucket", Builder.fishBucket(TropicraftEntities.RIVER_SARDINE));
    public static final RegistryObject<Item> PIRANHA_BUCKET = register("piranha_bucket", Builder.fishBucket(TropicraftEntities.PIRANHA));

    public static final RegistryObject<Item> KOA_SPAWN_EGG = register("koa_spawn_egg", Builder.spawnEgg(TropicraftEntities.KOA_HUNTER));
    public static final RegistryObject<Item> TROPICREEPER_SPAWN_EGG = register("tropicreeper_spawn_egg", Builder.spawnEgg(TropicraftEntities.TROPI_CREEPER));
    public static final RegistryObject<Item> IGUANA_SPAWN_EGG = register("iguana_spawn_egg", Builder.spawnEgg(TropicraftEntities.IGUANA));
    public static final RegistryObject<Item> TROPISKELLY_SPAWN_EGG = register("tropiskelly_spawn_egg", Builder.spawnEgg(TropicraftEntities.TROPI_SKELLY));
    public static final RegistryObject<Item> EIH_SPAWN_EGG = register("eih_spawn_egg", Builder.spawnEgg(TropicraftEntities.EIH));
    public static final RegistryObject<Item> SEA_TURTLE_SPAWN_EGG = register("sea_turtle_spawn_egg", Builder.spawnEgg(TropicraftEntities.SEA_TURTLE));
    public static final RegistryObject<Item> MARLIN_SPAWN_EGG = register("marlin_spawn_egg", Builder.spawnEgg(TropicraftEntities.MARLIN));
    public static final RegistryObject<Item> FAILGULL_SPAWN_EGG = register("failgull_spawn_egg", Builder.spawnEgg(TropicraftEntities.FAILGULL));
    public static final RegistryObject<Item> DOLPHIN_SPAWN_EGG = register("dolphin_spawn_egg", Builder.spawnEgg(TropicraftEntities.DOLPHIN));
    public static final RegistryObject<Item> SEAHORSE_SPAWN_EGG = register("seahorse_spawn_egg", Builder.spawnEgg(TropicraftEntities.SEAHORSE));
    public static final RegistryObject<Item> TREE_FROG_SPAWN_EGG = register("tree_frog_spawn_egg", Builder.spawnEgg(TropicraftEntities.TREE_FROG));
    public static final RegistryObject<Item> SEA_URCHIN_SPAWN_EGG = register("sea_urchin_spawn_egg", Builder.spawnEgg(TropicraftEntities.SEA_URCHIN));
    public static final RegistryObject<Item> V_MONKEY_SPAWN_EGG = register("v_monkey_spawn_egg", Builder.spawnEgg(TropicraftEntities.V_MONKEY));
    public static final RegistryObject<Item> PIRANHA_SPAWN_EGG = register("piranha_spawn_egg", Builder.spawnEgg(TropicraftEntities.PIRANHA));
    public static final RegistryObject<Item> SARDINE_SPAWN_EGG = register("sardine_spawn_egg", Builder.spawnEgg(TropicraftEntities.RIVER_SARDINE));
    public static final RegistryObject<Item> TROPICAL_FISH_SPAWN_EGG = register("tropical_fish_spawn_egg", Builder.spawnEgg(TropicraftEntities.TROPICAL_FISH));
    public static final RegistryObject<Item> EAGLE_RAY_SPAWN_EGG = register("eagle_ray_spawn_egg", Builder.spawnEgg(TropicraftEntities.EAGLE_RAY));
    public static final RegistryObject<Item> TROPI_SPIDER_SPAWN_EGG = register("tropi_spider_spawn_egg", Builder.spawnEgg(TropicraftEntities.TROPI_SPIDER));

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
                } else if (b.func_220276_d().getRegistryName().getNamespace().equals(Constants.MODID)) {
                    addPlant((FlowerPotBlock) Blocks.FLOWER_POT, b);
                }
            });
    }
    
    private static void addPlant(FlowerPotBlock empty, FlowerPotBlock full) {
        empty.addPlant(full.func_220276_d().getRegistryName(), full.delegate);
    }
}

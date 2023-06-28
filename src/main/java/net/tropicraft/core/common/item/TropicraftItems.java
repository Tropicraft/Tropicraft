package net.tropicraft.core.common.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.Tags;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.common.Foods;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.block.TropicraftWoodTypes;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.MixerRecipes;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;
import net.tropicraft.core.common.item.scuba.*;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static net.tropicraft.core.common.block.TropicraftBlocks.CHUNK;

@EventBusSubscriber(modid = Constants.MODID, bus = Bus.MOD)
public class TropicraftItems {
    public static final Registrate REGISTRATE = Tropicraft.registrate();

    static {
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
            prov.addTag(Tags.Items.GEMS).addTags(TropicraftTags.Items.AZURITE_GEM, TropicraftTags.Items.EUDIALYTE_GEM, TropicraftTags.Items.ZIRCON_GEM, TropicraftTags.Items.ZIRCONIUM_GEM);
            prov.addTag(Tags.Items.INGOTS).addTags(TropicraftTags.Items.MANGANESE_INGOT, TropicraftTags.Items.SHAKA_INGOT);

            prov.addTag(TropicraftTags.Items.FRUITS).add(Items.APPLE);
            prov.addTag(TropicraftTags.Items.MEATS).add(Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.RABBIT, Items.MUTTON);
        });
    }

    public static final ItemEntry<Item> AZURITE = simpleItem("azurite_gem")
            .tag(TropicraftTags.Items.AZURITE_GEM)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(TropicraftTags.Items.AZURITE_ORE), RecipeCategory.MISC, ctx, 0.3f))
            .register();
    public static final ItemEntry<Item> EUDIALYTE = simpleItem("eudialyte_gem")
            .tag(TropicraftTags.Items.EUDIALYTE_GEM)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(TropicraftTags.Items.EUDIALYTE_ORE), RecipeCategory.MISC, ctx, 0.5f))
            .register();
    public static final ItemEntry<Item> ZIRCON = simpleItem("zircon_gem")
            .tag(TropicraftTags.Items.ZIRCON_GEM)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(TropicraftTags.Items.ZIRCON_ORE), RecipeCategory.MISC, ctx, 0.5f))
            .register();
    public static final ItemEntry<Item> SHAKA = simpleItem("shaka_ingot")
            .tag(TropicraftTags.Items.SHAKA_INGOT)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(TropicraftTags.Items.SHAKA_ORE), RecipeCategory.MISC, ctx, 0.5f))
            .register();
    public static final ItemEntry<Item> MANGANESE = simpleItem("manganese_ingot")
            .tag(TropicraftTags.Items.MANGANESE_INGOT)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(TropicraftTags.Items.MANGANESE_ORE), RecipeCategory.MISC, ctx, 0.5f))
            .register();
    public static final ItemEntry<Item> ZIRCONIUM = simpleItem("zirconium_gem")
            .tag(TropicraftTags.Items.ZIRCONIUM_GEM)
            .lang("Zirconium")
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get())
                    .requires(AZURITE.get(), 2)
                    .requires(ZIRCON.get(), 2)
                    .unlockedBy("has_zircon", has(ZIRCON.get()))
                    .unlockedBy("has_azurite", has(AZURITE.get()))
                    .save(prov))
            .register();

    public static final Map<DyeColor, ItemEntry<FurnitureItem<UmbrellaEntity>>> UMBRELLAS = Arrays.stream(DyeColor.values())
            .collect(Maps.<DyeColor, DyeColor, ItemEntry<FurnitureItem<UmbrellaEntity>>>toImmutableEnumMap(Function.identity(), color ->
                    furniture(color.getSerializedName() + "_umbrella", TropicraftEntities.UMBRELLA, color)
                            .recipe((ctx, prov) -> {
                                ItemLike wool = Sheep.ITEM_BY_DYE.get(color);
                                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                                        .pattern("WWW").pattern(" B ").pattern(" B ")
                                        .group(Constants.MODID + ":umbrellas")
                                        .define('W', wool)
                                        .define('B', TropicraftItems.BAMBOO_STICK.get())
                                        .unlockedBy("has_" + color.getSerializedName() + "_wool", has(wool))
                                        .save(prov);
                            })
                            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/umbrella"), prov.modLoc("item/umbrella_inverted")))
                            .register()
            ));
    public static final Map<DyeColor, ItemEntry<FurnitureItem<ChairEntity>>> CHAIRS = Arrays.stream(DyeColor.values())
            .collect(Maps.<DyeColor, DyeColor, ItemEntry<FurnitureItem<ChairEntity>>>toImmutableEnumMap(Function.identity(), color ->
                    furniture(color.getSerializedName() + "_chair", TropicraftEntities.CHAIR, color)
                            .recipe((ctx, prov) -> {
                                ItemLike wool = Sheep.ITEM_BY_DYE.get(color);
                                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                                        .pattern("BWB").pattern("BWB").pattern("BWB")
                                        .group(Constants.MODID + ":chairs")
                                        .define('W', wool)
                                        .define('B', TropicraftItems.BAMBOO_STICK.get())
                                        .unlockedBy("has_" + color.getSerializedName() + "_wool", has(wool))
                                        .save(prov);
                            })
                            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/chair"), prov.modLoc("item/chair_inverted")))
                            .register()
            ));
    public static final Map<DyeColor, ItemEntry<FurnitureItem<BeachFloatEntity>>> BEACH_FLOATS = Arrays.stream(DyeColor.values())
            .collect(Maps.<DyeColor, DyeColor, ItemEntry<FurnitureItem<BeachFloatEntity>>>toImmutableEnumMap(Function.identity(), color ->
                    furniture(color.getSerializedName() + "_beach_float", TropicraftEntities.BEACH_FLOAT, color)
                            .recipe((ctx, prov) -> {
                                ItemLike wool = Sheep.ITEM_BY_DYE.get(color);
                                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                                        .pattern("WWW").pattern("BBB")
                                        .group(Constants.MODID + ":beach_floats")
                                        .define('W', wool)
                                        .define('B', Blocks.BAMBOO)
                                        .unlockedBy("has_" + color.getSerializedName() + "_wool", has(wool))
                                        .save(prov);
                            })
                            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/beach_float"), prov.modLoc("item/beach_float_inverted")))
                            .register()
            ));

    private static <T extends FurnitureEntity> ItemBuilder<FurnitureItem<T>, Registrate> furniture(String name, Supplier<EntityType<T>> type, DyeColor color) {
        return REGISTRATE.item(name, p -> new FurnitureItem<>(p, type, color))
                .color(() -> () -> (stack, tintIndex) -> tintIndex == 0 ? 0xffffff : color.getTextColor());
    }

    public static final ItemEntry<Item> BAMBOO_STICK = simpleItem("bamboo_stick")
            .tag(Tags.Items.RODS_WOODEN)
            .recipe((ctx, prov) -> {
                // Override the vanilla recipe to output ours, it's tagged so it will behave the same
                ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                        .pattern("X").pattern("X")
                        .define('X', Items.BAMBOO)
                        .unlockedBy("has_bamboo", has(Items.BAMBOO))
                        .save(prov, new ResourceLocation("stick_from_bamboo_item"));
            })
            .model((ctx, prov) -> prov.handheld(ctx))
            .register();

    public static final ItemEntry<SpearItem> BAMBOO_SPEAR = REGISTRATE.item("bamboo_spear", p -> new SpearItem(TropicraftToolTiers.BAMBOO, 3, -2.4F, p))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ctx.get())
                    .pattern("X ").pattern(" X")
                    .define('X', BAMBOO_STICK.get())
                    .unlockedBy("has_bamboo_stick", has(BAMBOO_STICK.get()))
                    .save(prov))
            .model((ctx, prov) -> {
                ItemModelBuilder throwing = prov.withExistingParent(ctx.getName() + "_throwing", prov.modLoc("spear_throwing"))
                        .texture("layer0", prov.itemTexture(ctx));
                prov.withExistingParent(ctx.getName(), prov.modLoc("spear"))
                        .texture("layer0", prov.itemTexture(ctx))
                        .override()
                        .predicate(prov.mcLoc("throwing"), 1)
                        .model(throwing);
            })
            .register();
    public static final ItemEntry<ShellItem> SOLONOX_SHELL = shell("solonox_shell").register();
    public static final ItemEntry<ShellItem> FROX_CONCH = shell("frox_conch").register();
    public static final ItemEntry<ShellItem> PAB_SHELL = shell("pab_shell").register();
    public static final ItemEntry<ShellItem> RUBE_NAUTILUS = shell("rube_nautilus").register();
    public static final ItemEntry<ShellItem> STARFISH = shell("starfish").register();
    public static final ItemEntry<ShellItem> TURTLE_SHELL = shell("turtle_shell").register();

    private static ItemBuilder<ShellItem, Registrate> shell(String name) {
        return REGISTRATE.item(name, ShellItem::new)
                .tag(TropicraftTags.Items.SHELLS);
    }

    public static final ItemEntry<LoveTropicsShellItem> LOVE_TROPICS_SHELL = REGISTRATE.item("love_tropics_shell", LoveTropicsShellItem::new)
            .initialProperties(Item.Properties::new)
            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/ltshell"), prov.modLoc("item/ltshell_inverted")))
            .color(() -> () -> LoveTropicsShellItem::getColor)
            .addMiscData(ProviderType.LANG, prov -> {
                prov.add("item.tropicraft.shell.owned.normal", "%s's Shell");
                prov.add("item.tropicraft.shell.owned.with_s", "%s' Shell");
            })
            .register();

    public static final ItemEntry<Item> LEMON = food("lemon", Foods.LEMON)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> LIME = food("lime", Foods.LIME)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> GRAPEFRUIT = food("grapefruit", Foods.GRAPEFRUIT)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> ORANGE = food("orange", Foods.ORANGE)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> PINEAPPLE_CUBES = food("pineapple_cubes", Foods.PINEAPPLE_CUBES).register();
    public static final ItemEntry<Item> COCONUT_CHUNK = food("coconut_chunk", Foods.COCONUT_CHUNK).register();

    public static final ItemEntry<ItemNameBlockItem> RAW_COFFEE_BEAN = REGISTRATE.item("raw_coffee_bean", p -> new ItemNameBlockItem(TropicraftBlocks.COFFEE_BUSH.get(), p))
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ctx.get())
                    .requires(TropicraftItems.COFFEE_BERRY.get())
                    .unlockedBy("has_coffee_bean", has(TropicraftItems.COFFEE_BERRY.get()))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> ROASTED_COFFEE_BEAN = simpleItem("roasted_coffee_bean")
            .recipe((ctx, prov) -> prov.food(ingredient(RAW_COFFEE_BEAN), RecipeCategory.FOOD, ctx, 0.1f))
            .register();
    public static final ItemEntry<Item> COFFEE_BERRY = simpleItem("coffee_berry").register();
    public static final ItemEntry<Item> BAMBOO_MUG = simpleItem("bamboo_mug")
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                    .pattern("X X").pattern("X X").pattern("XXX")
                    .define('X', Items.BAMBOO)
                    .unlockedBy("has_bamboo", has(Items.BAMBOO))
                    .save(prov))
            .register();

    // Cocktails
    public static final ImmutableMap<Drink, ItemEntry<CocktailItem>> COCKTAILS = Drink.DRINKS.values().stream()
            .collect(ImmutableMap.toImmutableMap(Function.identity(), drink ->
                    REGISTRATE.item(drink.name, p -> {
                                CocktailItem item = new CocktailItem(drink, p);
                                MixerRecipes.setDrinkItem(drink, item);
                                return item;
                            })
                            .properties(p -> p.durability(0).stacksTo(1).craftRemainder(BAMBOO_MUG.get()))
                            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/cocktail"), prov.modLoc("item/cocktail_contents")))
                            .color(() -> () -> (ItemColor) (stack, tintIndex) -> (tintIndex == 0 ? 0xffffff : drink.color))
                            .lang(drink.getName())
                            .register()
            ));

    static {
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, prov -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, COCKTAILS.get(Drink.PINA_COLADA).get())
                    .requires(BAMBOO_MUG.get())
                    .requires(COCONUT_CHUNK.get())
                    .requires(PINEAPPLE_CUBES.get())
                    .unlockedBy("has_bamboo_mug", has(BAMBOO_MUG.get()))
                    .save(prov);
        });
    }

    public static final ItemEntry<Item> WHITE_PEARL = simpleItem("white_pearl").register();
    public static final ItemEntry<Item> BLACK_PEARL = simpleItem("black_pearl").register();
    public static final ItemEntry<Item> SCALE = simpleItem("scale").register();
    public static final ItemEntry<NigelStacheItem> NIGEL_STACHE = REGISTRATE.item("nigel_stache", NigelStacheItem::new)
            .lang("Nigel's Moustache")
            .register();

    public static final ItemEntry<Item> FRESH_MARLIN = food("fresh_marlin", Foods.FRESH_MARLIN).register();
    public static final ItemEntry<Item> SEARED_MARLIN = food("seared_marlin", Foods.SEARED_MARLIN)
            .recipe((ctx, prov) -> prov.food(ingredient(FRESH_MARLIN), RecipeCategory.FOOD, ctx, 0.15f))
            .register();

    public static final ItemEntry<Item> RAW_RAY = food("raw_ray", Foods.RAW_RAY).register();
    public static final ItemEntry<Item> COOKED_RAY = food("cooked_ray", Foods.COOKED_RAY)
            .recipe((ctx, prov) -> prov.food(ingredient(RAW_RAY), RecipeCategory.FOOD, ctx, 0.15f))
            .register();
    public static final ItemEntry<Item> FROG_LEG = food("frog_leg", Foods.RAW_FROG_LEG).register();
    public static final ItemEntry<Item> COOKED_FROG_LEG = food("cooked_frog_leg", Foods.COOKED_FROG_LEG)
            .recipe((ctx, prov) -> prov.food(ingredient(FROG_LEG), RecipeCategory.FOOD, ctx, 0.1f))
            .register();
    public static final ItemEntry<Item> SEA_URCHIN_ROE = food("sea_urchin_roe", Foods.SEA_URCHIN_ROE).register();
    public static final ItemEntry<Item> TOASTED_NORI = food("toasted_nori", Foods.TOASTED_NORI)
            .recipe((ctx, prov) -> prov.food(DataIngredient.items(Items.SEAGRASS), RecipeCategory.FOOD, ctx, 0.1f))
            .register();
    public static final ItemEntry<Item> RAW_FISH = food("raw_fish", Foods.RAW_FISH)
            .tag(ItemTags.FISHES)
            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/smolfish")))
            .register();
    public static final ItemEntry<Item> COOKED_FISH = food("cooked_fish", Foods.COOKED_FISH)
            .tag(ItemTags.FISHES)
            .recipe((ctx, prov) -> prov.food(ingredient(RAW_FISH), RecipeCategory.FOOD, ctx, 0.1f))
            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/cooked_smolfish")))
            .register();
    public static final ItemEntry<Item> POISON_FROG_SKIN = simpleItem("poison_frog_skin").register();

    public static final ItemEntry<Item> IGUANA_LEATHER = simpleItem("iguana_leather")
            .tag(Tags.Items.LEATHER)
            .register();
    public static final ItemEntry<TropicalFertilizerItem> TROPICAL_FERTILIZER = REGISTRATE.item("tropical_fertilizer", TropicalFertilizerItem::new)
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ctx.get())
                    .requires(TropicraftFlower.MAGIC_MUSHROOM.get())
                    .requires(TropicraftFlower.CROTON.get())
                    .unlockedBy("has_magic_mushroom", has(TropicraftFlower.MAGIC_MUSHROOM.get()))
                    .save(prov))
            .register();

    public static final ItemEntry<BambooItemFrameItem> BAMBOO_ITEM_FRAME = REGISTRATE.item("bamboo_item_frame", BambooItemFrameItem::new)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 1)
                    .pattern("XXX")
                    .pattern("XYX")
                    .pattern("XXX")
                    .define('X', Items.BAMBOO)
                    .define('Y', Items.LEATHER)
                    .unlockedBy("has_bamboo", has(Items.BAMBOO))
                    .unlockedBy("has_leather", has(Items.LEATHER))
                    .save(prov))
            .register();

    public static final ImmutableMap<RecordMusic, ItemEntry<TropicalMusicDiscItem>> MUSIC_DISCS = Arrays.stream(RecordMusic.values())
            .collect(Maps.<RecordMusic, RecordMusic, ItemEntry<TropicalMusicDiscItem>>toImmutableEnumMap(Function.identity(), type ->
                    REGISTRATE.item("music_disc_" + type.name().toLowerCase(Locale.ROOT), p -> new TropicalMusicDiscItem(type, p))
                            .properties(p -> p.rarity(Rarity.RARE))
                            .tag(ItemTags.MUSIC_DISCS)
                            .setData(ProviderType.LANG, (ctx, prov) -> prov.addItemWithTooltip(ctx, "Music Disc", type.getTooltip()))
                            .register()
            ));

    public static final ItemEntry<Item> TROPICAL_FISH_BUCKET = fishBucket("tropical_fish_bucket", TropicraftEntities.TROPICAL_FISH).register();
    public static final ItemEntry<Item> SARDINE_BUCKET = fishBucket("sardine_bucket", TropicraftEntities.RIVER_SARDINE).register();
    public static final ItemEntry<Item> PIRANHA_BUCKET = fishBucket("piranha_bucket", TropicraftEntities.PIRANHA).register();

    private static <T extends AbstractFish> ItemBuilder<Item, Registrate> fishBucket(final String name, final Supplier<? extends EntityType<T>> entity) {
        return REGISTRATE.item(name, p -> (Item) new TropicraftFishBucketItem<>(entity, Fluids.WATER, p))
                .properties(p -> p.stacksTo(1));
    }

    public static final ItemEntry<Item> KOA_SPAWN_EGG = spawnEgg("koa_spawn_egg", TropicraftEntities.KOA).lang("Koa Headband").register();
    public static final ItemEntry<Item> TROPICREEPER_SPAWN_EGG = spawnEgg("tropicreeper_spawn_egg", TropicraftEntities.TROPICREEPER).lang("TropiCreeper Hat").register();
    public static final ItemEntry<Item> IGUANA_SPAWN_EGG = spawnEgg("iguana_spawn_egg", TropicraftEntities.IGUANA).register();
    public static final ItemEntry<Item> TROPISKELLY_SPAWN_EGG = spawnEgg("tropiskelly_spawn_egg", TropicraftEntities.TROPISKELLY).lang("TropiSkelly Skirt").register();
    public static final ItemEntry<Item> EIH_SPAWN_EGG = spawnEgg("eih_spawn_egg", TropicraftEntities.EIH).lang("Eye of Head").register();
    public static final ItemEntry<Item> SEA_TURTLE_SPAWN_EGG = spawnEgg("sea_turtle_spawn_egg", TropicraftEntities.SEA_TURTLE).register();
    public static final ItemEntry<Item> MARLIN_SPAWN_EGG = spawnEgg("marlin_spawn_egg", TropicraftEntities.MARLIN).register();
    public static final ItemEntry<Item> FAILGULL_SPAWN_EGG = spawnEgg("failgull_spawn_egg", TropicraftEntities.FAILGULL).register();
    public static final ItemEntry<Item> DOLPHIN_SPAWN_EGG = spawnEgg("dolphin_spawn_egg", TropicraftEntities.DOLPHIN).register();
    public static final ItemEntry<Item> SEAHORSE_SPAWN_EGG = spawnEgg("seahorse_spawn_egg", TropicraftEntities.SEAHORSE).register();
    public static final ItemEntry<Item> TREE_FROG_SPAWN_EGG = spawnEgg("tree_frog_spawn_egg", TropicraftEntities.TREE_FROG).register();
    public static final ItemEntry<Item> SEA_URCHIN_SPAWN_EGG = spawnEgg("sea_urchin_spawn_egg", TropicraftEntities.SEA_URCHIN).register();
    public static final ItemEntry<Item> V_MONKEY_SPAWN_EGG = spawnEgg("v_monkey_spawn_egg", TropicraftEntities.V_MONKEY).lang("Vervet Monkey Spawn Egg").register();
    public static final ItemEntry<Item> PIRANHA_SPAWN_EGG = spawnEgg("piranha_spawn_egg", TropicraftEntities.PIRANHA).register();
    public static final ItemEntry<Item> SARDINE_SPAWN_EGG = spawnEgg("sardine_spawn_egg", TropicraftEntities.RIVER_SARDINE).register();
    public static final ItemEntry<Item> TROPICAL_FISH_SPAWN_EGG = spawnEgg("tropical_fish_spawn_egg", TropicraftEntities.TROPICAL_FISH).register();
    public static final ItemEntry<Item> EAGLE_RAY_SPAWN_EGG = spawnEgg("eagle_ray_spawn_egg", TropicraftEntities.EAGLE_RAY).register();
    public static final ItemEntry<Item> TROPI_SPIDER_SPAWN_EGG = spawnEgg("tropi_spider_spawn_egg", TropicraftEntities.TROPI_SPIDER).register();
    public static final ItemEntry<Item> ASHEN_SPAWN_EGG = spawnEgg("ashen_spawn_egg", TropicraftEntities.ASHEN).lang("Ashen Ash").register();
    public static final ItemEntry<Item> HAMMERHEAD_SPAWN_EGG = spawnEgg("hammerhead_spawn_egg", TropicraftEntities.HAMMERHEAD).register();
    public static final ItemEntry<Item> COWKTAIL_SPAWN_EGG = spawnEgg("cowktail_spawn_egg", TropicraftEntities.COWKTAIL).register();
    public static final ItemEntry<Item> MAN_O_WAR_SPAWN_EGG = spawnEgg("man_o_war_spawn_egg", TropicraftEntities.MAN_O_WAR).register();
    public static final ItemEntry<Item> TROPIBEE_SPAWN_EGG = spawnEgg("tropibee_spawn_egg", TropicraftEntities.TROPI_BEE).register();
    public static final ItemEntry<Item> TAPIR_SPAWN_EGG = spawnEgg("tapir_spawn_egg", TropicraftEntities.TAPIR).register();
    public static final ItemEntry<Item> JAGUAR_SPAWN_EGG = spawnEgg("jaguar_spawn_egg", TropicraftEntities.JAGUAR).register();
    public static final ItemEntry<Item> BROWN_BASILISK_LIZARD_SPAWN_EGG = spawnEgg("brown_basilisk_lizard_spawn_egg", TropicraftEntities.BROWN_BASILISK_LIZARD).register();
    public static final ItemEntry<Item> GREEN_BASILISK_LIZARD_SPAWN_EGG = spawnEgg("green_basilisk_lizard_spawn_egg", TropicraftEntities.GREEN_BASILISK_LIZARD).register();
    public static final ItemEntry<Item> HUMMINGBIRD_SPAWN_EGG = spawnEgg("hummingbird_spawn_egg", TropicraftEntities.HUMMINGBIRD).register();
    public static final ItemEntry<Item> FIDDLER_CRAB_SPAWN_EGG = spawnEgg("fiddler_crab_spawn_egg", TropicraftEntities.FIDDLER_CRAB).register();
    public static final ItemEntry<Item> SPIDER_MONKEY_SPAWN_EGG = spawnEgg("spider_monkey_spawn_egg", TropicraftEntities.SPIDER_MONKEY).register();
    public static final ItemEntry<Item> WHITE_LIPPED_PECCARY_SPAWN_EGG = spawnEgg("white_lipped_peccary_spawn_egg", TropicraftEntities.WHITE_LIPPED_PECCARY).register();
    public static final ItemEntry<Item> CUBERA_SPAWN_EGG = spawnEgg("cubera_spawn_egg", TropicraftEntities.CUBERA).register();

    private static <T extends Mob> ItemBuilder<Item, Registrate> spawnEgg(final String name, final RegistryEntry<EntityType<T>> entity) {
        return REGISTRATE.item(name, p -> new ForgeSpawnEggItem(entity, 0xffffff, 0xffffff, p));
    }

    public static final ImmutableMap<AshenMasks, ItemEntry<AshenMaskItem>> ASHEN_MASKS = Arrays.stream(AshenMasks.values())
            .collect(Maps.<AshenMasks, AshenMasks, ItemEntry<AshenMaskItem>>toImmutableEnumMap(Function.identity(), type ->
                    REGISTRATE.item("ashen_mask_" + type.name().toLowerCase(Locale.ROOT), p -> new AshenMaskItem(ArmorMaterials.ASHEN_MASK, type, p))
                            .tag(TropicraftTags.Items.ASHEN_MASKS)
                            .lang(type.getName())
                            .register()
            ));

    public static final ItemEntry<DaggerItem> DAGGER = REGISTRATE.item("dagger", p -> new DaggerItem(TropicraftToolTiers.ZIRCON, p))
            .properties(p -> p.stacksTo(1))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ctx.get())
                    .pattern("X")
                    .pattern("I")
                    .define('X', CHUNK.get())
                    .define('I', BAMBOO_STICK.get())
                    .unlockedBy("has_" + prov.safeName(CHUNK.get()), has(CHUNK.get()))
                    .unlockedBy("has_bamboo", has(Items.BAMBOO))
                    .save(prov))
            .register();

    public static final ItemEntry<BlowGunItem> BLOW_GUN = REGISTRATE.item("blow_gun", BlowGunItem::new)
            .properties(p -> p.stacksTo(1))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ctx.get())
                    .pattern("X  ")
                    .pattern(" I ")
                    .pattern("  X")
                    .define('X', BAMBOO_STICK.get())
                    .define('I', ZIRCON.get())
                    .unlockedBy("has_" + prov.safeName(ZIRCON.get()), has(ZIRCON.get()))
                    .unlockedBy("has_" + prov.safeName(BAMBOO_STICK.get()), has(BAMBOO_STICK.get()))
                    .save(prov))
            .register();

    // TODO add zirconium tools

    public static final ItemEntry<Item> ZIRCON_HOE = hoe("zircon_hoe", TropicraftToolTiers.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_HOE = hoe("zirconium_hoe", TropicraftToolTiers.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_HOE = hoe("eudialyte_hoe", TropicraftToolTiers.EUDIALYTE, EUDIALYTE).register();

    public static final ItemEntry<Item> ZIRCON_AXE = axe("zircon_axe", TropicraftToolTiers.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_AXE = axe("zirconium_axe", TropicraftToolTiers.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_AXE = axe("eudialyte_axe", TropicraftToolTiers.EUDIALYTE, EUDIALYTE).register();

    public static final ItemEntry<Item> ZIRCON_PICKAXE = pickaxe("zircon_pickaxe", TropicraftToolTiers.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_PICKAXE = pickaxe("zirconium_pickaxe", TropicraftToolTiers.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_PICKAXE = pickaxe("eudialyte_pickaxe", TropicraftToolTiers.EUDIALYTE, EUDIALYTE).register();

    public static final ItemEntry<Item> ZIRCON_SHOVEL = shovel("zircon_shovel", TropicraftToolTiers.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_SHOVEL = shovel("zirconium_shovel", TropicraftToolTiers.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_SHOVEL = shovel("eudialyte_shovel", TropicraftToolTiers.EUDIALYTE, EUDIALYTE).register();

    public static final ItemEntry<Item> ZIRCON_SWORD = sword("zircon_sword", TropicraftToolTiers.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_SWORD = sword("zirconium_sword", TropicraftToolTiers.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_SWORD = sword("eudialyte_sword", TropicraftToolTiers.EUDIALYTE, EUDIALYTE).register();

    private static ItemBuilder<Item, Registrate> hoe(final String name, final TropicraftToolTiers tier, final Supplier<? extends Item> input) {
        return REGISTRATE.item(name, p -> (Item) new HoeItem(tier, 0, -2.0f, p))
                .tag(ItemTags.HOES)
                .model((ctx, prov) -> prov.handheld(ctx))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                        .pattern("XX")
                        .pattern(" B")
                        .pattern(" B")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), has(Items.BAMBOO))
                        .save(prov)
                );
    }

    private static ItemBuilder<Item, Registrate> shovel(final String name, final Tier tier, final Supplier<? extends Item> input) {
        return REGISTRATE.item(name, p -> (Item) new ShovelItem(tier, 2.0f, -3.0f, p))
                .tag(ItemTags.SHOVELS)
                .model((ctx, prov) -> prov.handheld(ctx))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                        .pattern("X")
                        .pattern("B")
                        .pattern("B")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), has(Items.BAMBOO))
                        .save(prov)
                );
    }

    private static ItemBuilder<Item, Registrate> pickaxe(final String name, final Tier tier, final Supplier<? extends Item> input) {
        return REGISTRATE.item(name, p -> (Item) new PickaxeItem(tier, 2, -2.0f, p))
                .tag(ItemTags.PICKAXES)
                .model((ctx, prov) -> prov.handheld(ctx))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                        .pattern("XXX")
                        .pattern(" B ")
                        .pattern(" B ")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), has(Items.BAMBOO))
                        .save(prov)
                );
    }

    private static ItemBuilder<Item, Registrate> axe(final String name, final Tier tier, final Supplier<? extends Item> input) {
        return REGISTRATE.item(name, p -> (Item) new AxeItem(tier, 5.0f, -2.0f, p))
                .tag(ItemTags.AXES)
                .model((ctx, prov) -> prov.handheld(ctx))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                        .pattern("XX")
                        .pattern("XB")
                        .pattern(" B")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), has(Items.BAMBOO))
                        .save(prov)
                );
    }

    private static ItemBuilder<Item, Registrate> sword(final String name, final Tier tier, final Supplier<? extends Item> input) {
        return REGISTRATE.item(name, p -> (Item) new SwordItem(tier, 3, -3.0f, p))
                .tag(ItemTags.SWORDS)
                .model((ctx, prov) -> prov.handheld(ctx))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ctx.get())
                        .pattern("X")
                        .pattern("X")
                        .pattern("B")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), has(Items.BAMBOO))
                        .save(prov)
                );
    }

    public static final ItemEntry<Item> FIRE_BOOTS = fireArmor("fire_boots", ArmorItem.Type.BOOTS).register();
    public static final ItemEntry<Item> FIRE_LEGGINGS = fireArmor("fire_leggings", ArmorItem.Type.LEGGINGS).register();
    public static final ItemEntry<Item> FIRE_CHESTPLATE = fireArmor("fire_chestplate", ArmorItem.Type.CHESTPLATE).register();
    public static final ItemEntry<Item> FIRE_HELMET = fireArmor("fire_helmet", ArmorItem.Type.HELMET).register();

    public static final ItemEntry<Item> SCALE_BOOTS = scaleArmor("scale_boots", ArmorItem.Type.BOOTS)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                    .pattern("X X")
                    .pattern("X X")
                    .define('X', SCALE.get())
                    .unlockedBy("has_" + prov.safeName(SCALE.get()), has(SCALE.get()))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> SCALE_LEGGINGS = scaleArmor("scale_leggings", ArmorItem.Type.LEGGINGS)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                    .pattern("XXX")
                    .pattern("X X")
                    .pattern("X X")
                    .define('X', SCALE.get())
                    .unlockedBy("has_" + prov.safeName(SCALE.get()), has(SCALE.get()))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> SCALE_CHESTPLATE = scaleArmor("scale_chestplate", ArmorItem.Type.CHESTPLATE)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                    .pattern("X X")
                    .pattern("XXX")
                    .pattern("XXX")
                    .define('X', SCALE.get())
                    .unlockedBy("has_" + prov.safeName(SCALE.get()), has(SCALE.get()))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> SCALE_HELMET = scaleArmor("scale_helmet", ArmorItem.Type.HELMET)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get())
                    .pattern("XXX")
                    .pattern("X X")
                    .define('X', SCALE.get())
                    .unlockedBy("has_" + prov.safeName(SCALE.get()), has(SCALE.get()))
                    .save(prov))
            .register();

    public static final ItemEntry<ScubaGogglesItem> YELLOW_SCUBA_GOGGLES = scubaGoggles("yellow_scuba_goggles", ScubaType.YELLOW, () -> Items.YELLOW_DYE).register();
    public static final ItemEntry<ScubaHarnessItem> YELLOW_SCUBA_HARNESS = scubaHarness("yellow_scuba_harness", ScubaType.YELLOW, () -> Items.YELLOW_DYE).register();
    public static final ItemEntry<ScubaFlippersItem> YELLOW_SCUBA_FLIPPERS = scubaFlippers("yellow_scuba_flippers", ScubaType.YELLOW, () -> Items.YELLOW_DYE).register();
    public static final ItemEntry<ScubaGogglesItem> PINK_SCUBA_GOGGLES = scubaGoggles("pink_scuba_goggles", ScubaType.PINK, () -> Items.PINK_DYE).register();
    public static final ItemEntry<ScubaHarnessItem> PINK_SCUBA_HARNESS = scubaHarness("pink_scuba_harness", ScubaType.PINK, () -> Items.PINK_DYE).register();
    public static final ItemEntry<ScubaFlippersItem> PINK_SCUBA_FLIPPERS = scubaFlippers("pink_scuba_flippers", ScubaType.PINK, () -> Items.PINK_DYE).register();

    private static ItemBuilder<Item, Registrate> fireArmor(final String name, final ArmorItem.Type slotType) {
        return REGISTRATE.item(name, p -> (Item) new FireArmorItem(slotType, p))
                .properties(p -> p.stacksTo(1).durability(300));
    }

    private static ItemBuilder<Item, Registrate> scaleArmor(final String name, final ArmorItem.Type slotType) {
        return REGISTRATE.item(name, p -> (Item) new ScaleArmorItem(slotType, p))
                .properties(p -> p.stacksTo(1));
    }

    private static ItemBuilder<ScubaGogglesItem, Registrate> scubaGoggles(final String name, final ScubaType type, Supplier<? extends Item> source) {
        return REGISTRATE.item(name, p -> new ScubaGogglesItem(type, p))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get(), 1)
                        .pattern("YYY")
                        .pattern("X X")
                        .pattern(" Z ")
                        .define('X', Blocks.GLASS_PANE)
                        .define('Y', ZIRCON.get())
                        .define('Z', source.get())
                        .unlockedBy("has_" + prov.safeName(source.get()), has(source.get()))
                        .unlockedBy("has_" + prov.safeName(ZIRCON.get()), has(ZIRCON.get()))
                        .save(prov));
    }

    private static ItemBuilder<ScubaHarnessItem, Registrate> scubaHarness(final String name, final ScubaType type, Supplier<? extends Item> source) {
        return REGISTRATE.item(name, p -> new ScubaHarnessItem(type, p))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get(), 1)
                        .pattern("Y Y")
                        .pattern("YXY")
                        .pattern("YZY")
                        .define('X', source.get())
                        .define('Y', Tags.Items.LEATHER)
                        .define('Z', AZURITE.get())
                        .unlockedBy("has_" + prov.safeName(AZURITE.get()), has(AZURITE.get()))
                        .save(prov));
    }

    private static ItemBuilder<ScubaFlippersItem, Registrate> scubaFlippers(final String name, final ScubaType type, Supplier<? extends Item> source) {
        return REGISTRATE.item(name, p -> new ScubaFlippersItem(type, p))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get(), 1)
                        .pattern("XX")
                        .pattern("YY")
                        .pattern("XX")
                        .define('X', source.get())
                        .define('Y', ZIRCON.get())
                        .unlockedBy("has_" + prov.safeName(source.get()), has(source.get()))
                        .unlockedBy("has_" + prov.safeName(ZIRCON.get()), has(ZIRCON.get()))
                        .save(prov));
    }

    public static final ItemEntry<PonyBottleItem> YELLOW_PONY_BOTTLE = ponyBottle("yellow_pony_bottle", Blocks.YELLOW_STAINED_GLASS_PANE);
    public static final ItemEntry<PonyBottleItem> PINK_PONY_BOTTLE = ponyBottle("pink_pony_bottle", Blocks.PINK_STAINED_GLASS_PANE);

    private static ItemEntry<PonyBottleItem> ponyBottle(String name, Block glassPane) {
        return REGISTRATE.item(name, PonyBottleItem::new)
                .properties(p -> p.stacksTo(1).durability(32))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ctx.get(), 1)
                        .pattern("Y")
                        .pattern("X")
                        .define('X', glassPane)
                        .define('Y', Blocks.LEVER)
                        .unlockedBy("has_" + prov.safeName(glassPane), has(glassPane))
                        .save(prov))
                .register();
    }

    public static final ItemEntry<WaterWandItem> WATER_WAND = REGISTRATE.item("water_wand", WaterWandItem::new)
            .properties(p -> p.stacksTo(1).durability(2000))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get(), 1)
                    .pattern("  X")
                    .pattern(" Y ")
                    .pattern("Y  ")
                    .define('X', AZURITE.get())
                    .define('Y', Items.GOLD_INGOT)
                    .unlockedBy("has_" + prov.safeName(AZURITE.get()), has(AZURITE.get()))
                    .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                    .save(prov))
            .register();

    public static final ItemEntry<ExplodingCoconutItem> EXPLODING_COCONUT = REGISTRATE.item("exploding_coconut", ExplodingCoconutItem::new).register();

    public static final ItemEntry<Item> FISHING_NET = simpleItem("fishing_net")
            .properties(p -> p.stacksTo(1))
            .register();

    public static final ItemEntry<Item> PIANGUAS = simpleItem("pianguas").register();

    public static final ItemEntry<SignItem> MAHOGANY_SIGN = sign(TropicraftWoodTypes.MAHOGANY, TropicraftBlocks.MAHOGANY_PLANKS, TropicraftBlocks.MAHOGANY_SIGN, TropicraftBlocks.MAHOGANY_WALL_SIGN).register();
    public static final ItemEntry<SignItem> PALM_SIGN = sign(TropicraftWoodTypes.PALM, TropicraftBlocks.PALM_PLANKS, TropicraftBlocks.PALM_SIGN, TropicraftBlocks.PALM_WALL_SIGN).register();
    public static final ItemEntry<SignItem> BAMBOO_SIGN = sign(TropicraftWoodTypes.BAMBOO, TropicraftBlocks.BAMBOO_BUNDLE, TropicraftBlocks.BAMBOO_SIGN, TropicraftBlocks.BAMBOO_WALL_SIGN).register();
    public static final ItemEntry<SignItem> THATCH_SIGN = sign(TropicraftWoodTypes.THATCH, TropicraftBlocks.THATCH_BUNDLE, TropicraftBlocks.THATCH_SIGN, TropicraftBlocks.THATCH_WALL_SIGN).register();
    public static final ItemEntry<SignItem> MANGROVE_SIGN = sign(TropicraftWoodTypes.MANGROVE, TropicraftBlocks.MANGROVE_PLANKS, TropicraftBlocks.MANGROVE_SIGN, TropicraftBlocks.MANGROVE_WALL_SIGN).register();

    private static ItemBuilder<Item, Registrate> simpleItem(String name) {
        return REGISTRATE.item(name, Item::new);
    }

    private static ItemBuilder<Item, Registrate> food(final String name, final FoodProperties food) {
        return simpleItem(name).properties(p -> p.food(food));
    }

    private static ItemBuilder<SignItem, Registrate> sign(final WoodType woodType, final Supplier<? extends Block> planks, final Supplier<? extends StandingSignBlock> standingSign, final Supplier<? extends WallSignBlock> wallSign) {
        String woodName = new ResourceLocation(woodType.name()).getPath();
        return REGISTRATE.item(woodName + "_sign", p -> new SignItem(p, standingSign.get(), wallSign.get()))
                .properties(p -> p.stacksTo(16))
                .tag(ItemTags.SIGNS)
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
                        .pattern("###")
                        .pattern("###")
                        .pattern(" | ")
                        .define('#', planks.get())
                        .define('|', Tags.Items.RODS_WOODEN)
                        .unlockedBy("has_" + woodName, has(planks.get()))
                        .group("wooden_sign")
                        .save(prov));
    }

    @SubscribeEvent
    public static void onItemRegister(RegisterEvent event) {
        if (!ForgeRegistries.ITEMS.equals(event.getForgeRegistry())) {
            return;
        }
        ForgeRegistries.BLOCKS.getEntries().forEach(entry -> {
            if (entry.getValue() instanceof FlowerPotBlock flowerPot) {
                FlowerPotBlock emptyPot = flowerPot.getEmptyPot();
                Block content = flowerPot.getContent();
                if (emptyPot.builtInRegistryHolder().is(TropicraftBlocks.BAMBOO_FLOWER_POT.getId()) && emptyPot != flowerPot) {
                    addPlant(TropicraftBlocks.BAMBOO_FLOWER_POT.get(), flowerPot);
                } else if (content.builtInRegistryHolder().key().location().getNamespace().equals(Constants.MODID)) {
                    addPlant((FlowerPotBlock) Blocks.FLOWER_POT, flowerPot);
                }
            }
        });
    }

    private static void addPlant(FlowerPotBlock empty, FlowerPotBlock full) {
        empty.addPlant(full.getContent().builtInRegistryHolder().key().location(), full.builtInRegistryHolder());
    }

    private static DataIngredient ingredient(NonNullSupplier<? extends ItemLike> item) {
        return DataIngredient.items(item);
    }
}

package net.tropicraft.core.common.item;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.generators.RegistrateItemModelGenerator;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.advancements.critereon.DamageSourcePredicate;
import net.minecraft.advancements.critereon.TagPredicate;
import net.minecraft.client.color.item.Constant;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.properties.select.DisplayContext;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftEquipmentAssets;
import net.tropicraft.core.client.TropicraftItemTintSources;
import net.tropicraft.core.common.Foods;
import net.tropicraft.core.common.TropicraftRegistries;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.attribute.TropicraftAttributes;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicraftFlower;
import net.tropicraft.core.common.block.TropicraftWoodTypes;
import net.tropicraft.core.common.drinks.Cocktail;
import net.tropicraft.core.common.drinks.Drink;
import net.tropicraft.core.common.drinks.TropicraftDrinks;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;
import net.tropicraft.core.common.entity.placeable.ChairEntity;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;
import net.tropicraft.core.common.item.component.DamageModifier;
import net.tropicraft.core.common.item.component.TropicraftDataComponents;
import net.tropicraft.core.common.item.scuba.PonyBottleItem;
import net.tropicraft.core.common.item.scuba.ScubaArmorItem;
import net.tropicraft.core.common.item.scuba.ScubaHarnessItem;
import net.tropicraft.core.common.item.scuba.ScubaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.minecraft.client.data.models.model.ItemModelUtils.*;

public class TropicraftItems {
    public static final Registrate REGISTRATE = Tropicraft.registrate();

    private static final Map<DyeColor, Item> WOOL_BY_DYE = Map.ofEntries(
            Map.entry(DyeColor.BLACK, Items.BLACK_WOOL),
            Map.entry(DyeColor.BLUE, Items.BLUE_WOOL),
            Map.entry(DyeColor.BROWN, Items.BROWN_WOOL),
            Map.entry(DyeColor.CYAN, Items.CYAN_WOOL),
            Map.entry(DyeColor.GRAY, Items.GRAY_WOOL),
            Map.entry(DyeColor.GREEN, Items.GREEN_WOOL),
            Map.entry(DyeColor.LIGHT_BLUE, Items.LIGHT_BLUE_WOOL),
            Map.entry(DyeColor.LIGHT_GRAY, Items.LIGHT_GRAY_WOOL),
            Map.entry(DyeColor.LIME, Items.LIME_WOOL),
            Map.entry(DyeColor.MAGENTA, Items.MAGENTA_WOOL),
            Map.entry(DyeColor.ORANGE, Items.ORANGE_WOOL),
            Map.entry(DyeColor.PINK, Items.PINK_WOOL),
            Map.entry(DyeColor.PURPLE, Items.PURPLE_WOOL),
            Map.entry(DyeColor.RED, Items.RED_WOOL),
            Map.entry(DyeColor.YELLOW, Items.YELLOW_WOOL),
            Map.entry(DyeColor.WHITE, Items.WHITE_WOOL)
    );

    static {
        REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, prov -> {
            prov.tag(Tags.Items.GEMS).addTags(TropicraftTags.Items.AZURITE_GEM, TropicraftTags.Items.EUDIALYTE_GEM, TropicraftTags.Items.ZIRCON_GEM, TropicraftTags.Items.ZIRCONIUM_GEM);
            prov.tag(Tags.Items.INGOTS).addTags(TropicraftTags.Items.MANGANESE_INGOT, TropicraftTags.Items.SHAKA_INGOT);

            prov.tag(TropicraftTags.Items.FRUITS).add(Items.APPLE);
            prov.tag(TropicraftTags.Items.MEATS).add(Items.BEEF, Items.PORKCHOP, Items.CHICKEN, Items.RABBIT, Items.MUTTON);

            prov.tag(TropicraftTags.Items.BAMBOO_TOOL_MATERIALS).add(Items.BAMBOO);
        });
    }

    public static final ItemEntry<Item> AZURITE = simpleItem("azurite_gem")
            .tag(TropicraftTags.Items.AZURITE_GEM)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(prov.itemLookup().getOrThrow(TropicraftTags.Items.AZURITE_ORE)), RecipeCategory.MISC, ctx, 0.3f))
            .register();
    public static final ItemEntry<Item> EUDIALYTE = simpleItem("eudialyte_gem")
            .tag(TropicraftTags.Items.EUDIALYTE_GEM)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(prov.itemLookup().getOrThrow(TropicraftTags.Items.EUDIALYTE_ORE)), RecipeCategory.MISC, ctx, 0.5f))
            .register();
    public static final ItemEntry<Item> ZIRCON = simpleItem("zircon_gem")
            .tag(TropicraftTags.Items.ZIRCON_GEM)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(prov.itemLookup().getOrThrow(TropicraftTags.Items.ZIRCON_ORE)), RecipeCategory.MISC, ctx, 0.5f))
            .register();
    public static final ItemEntry<Item> SHAKA = simpleItem("shaka_ingot")
            .tag(TropicraftTags.Items.SHAKA_INGOT)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(prov.itemLookup().getOrThrow(TropicraftTags.Items.SHAKA_ORE)), RecipeCategory.MISC, ctx, 0.5f))
            .register();
    public static final ItemEntry<Item> MANGANESE = simpleItem("manganese_ingot")
            .tag(TropicraftTags.Items.MANGANESE_INGOT)
            .recipe((ctx, prov) -> prov.smeltingAndBlasting(DataIngredient.tag(prov.itemLookup().getOrThrow(TropicraftTags.Items.MANGANESE_ORE)), RecipeCategory.MISC, ctx, 0.5f))
            .register();
    public static final ItemEntry<Item> ZIRCONIUM = simpleItem("zirconium_gem")
            .tag(TropicraftTags.Items.ZIRCONIUM_GEM)
            .lang("Zirconium")
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                    .requires(AZURITE.get(), 2)
                    .requires(ZIRCON.get(), 2)
                    .unlockedBy("has_zircon", prov.has(ZIRCON.get()))
                    .unlockedBy("has_azurite", prov.has(AZURITE.get()))
                    .save(prov))
            .register();

    public static final Map<DyeColor, ItemEntry<FurnitureItem<UmbrellaEntity>>> UMBRELLAS = Arrays.stream(DyeColor.values())
            .collect(Maps.<DyeColor, DyeColor, ItemEntry<FurnitureItem<UmbrellaEntity>>>toImmutableEnumMap(Function.identity(), color ->
                    furniture("umbrella", TropicraftEntities.UMBRELLA, color)
                            .recipe((ctx, prov) -> {
                                Item wool = WOOL_BY_DYE.get(color);
                                ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                                        .pattern("WWW").pattern(" B ").pattern(" B ")
                                        .group(Tropicraft.ID + ":umbrellas")
                                        .define('W', wool)
                                        .define('B', TropicraftItems.BAMBOO_STICK.get())
                                        .unlockedBy("has_" + color.getSerializedName() + "_wool", prov.has(wool))
                                        .save(prov);
                            })
                            .register()
            ));
    public static final Map<DyeColor, ItemEntry<FurnitureItem<ChairEntity>>> CHAIRS = Arrays.stream(DyeColor.values())
            .collect(Maps.<DyeColor, DyeColor, ItemEntry<FurnitureItem<ChairEntity>>>toImmutableEnumMap(Function.identity(), color ->
                    furniture("chair", TropicraftEntities.CHAIR, color)
                            .recipe((ctx, prov) -> {
                                Item wool = WOOL_BY_DYE.get(color);
                                ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                                        .pattern("BWB").pattern("BWB").pattern("BWB")
                                        .group(Tropicraft.ID + ":chairs")
                                        .define('W', wool)
                                        .define('B', TropicraftItems.BAMBOO_STICK.get())
                                        .unlockedBy("has_" + color.getSerializedName() + "_wool", prov.has(wool))
                                        .save(prov);
                            })
                            .register()
            ));
    public static final Map<DyeColor, ItemEntry<FurnitureItem<BeachFloatEntity>>> BEACH_FLOATS = Arrays.stream(DyeColor.values())
            .collect(Maps.<DyeColor, DyeColor, ItemEntry<FurnitureItem<BeachFloatEntity>>>toImmutableEnumMap(Function.identity(), color ->
                    furniture("beach_float", TropicraftEntities.BEACH_FLOAT, color)
                            .recipe((ctx, prov) -> {
                                Item wool = WOOL_BY_DYE.get(color);
                                ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                                        .pattern("WWW").pattern("BBB")
                                        .group(Tropicraft.ID + ":beach_floats")
                                        .define('W', wool)
                                        .define('B', Blocks.BAMBOO)
                                        .unlockedBy("has_" + color.getSerializedName() + "_wool", prov.has(wool))
                                        .save(prov);
                            })
                            .register()
            ));

    private static <T extends FurnitureEntity> ItemBuilder<FurnitureItem<T>, Registrate> furniture(String baseName, Supplier<EntityType<T>> type, DyeColor color) {
        return REGISTRATE.item(color.getSerializedName() + "_" + baseName, p -> new FurnitureItem<>(p, type, color))
                .model(() -> (ctx, prov) -> Models.generateFurniture(ctx, prov, baseName, color));
    }

    public static final ItemEntry<Item> BAMBOO_STICK = simpleItem("bamboo_stick")
            .tag(Tags.Items.RODS_WOODEN)
            .recipe((ctx, prov) -> {
                // Override the vanilla recipe to output ours, it's tagged so it will behave the same
                ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                        .pattern("X").pattern("X")
                        .define('X', Items.BAMBOO)
                        .unlockedBy("has_bamboo", prov.has(Items.BAMBOO))
                        .save(prov, ResourceKey.create(Registries.RECIPE, ResourceLocation.withDefaultNamespace("stick_from_bamboo_item")));
            })
            .model(() -> Models::generateHandheld)
            .register();

    public static final ItemEntry<SpearItem> BAMBOO_SPEAR = REGISTRATE.item("bamboo_spear", p -> new SpearItem(TropicraftToolMaterials.BAMBOO, 3, -2.4f, p))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.COMBAT, ctx.get())
                    .pattern("X ").pattern(" X")
                    .define('X', BAMBOO_STICK.get())
                    .unlockedBy("has_bamboo_stick", prov.has(BAMBOO_STICK.get()))
                    .save(prov))
            .tag(ItemTags.TRIDENT_ENCHANTABLE)
            .model(() -> Models::generateBambooSpear)
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
            .model(() -> Models::generateLoveTropicsShell)
            .addMiscData(ProviderType.LANG, prov -> {
                prov.add("item.tropicraft.shell.owned.normal", "%s's Shell");
                prov.add("item.tropicraft.shell.owned.with_s", "%s' Shell");
            })
            .register();

    private static final Consumable FAST_FOOD = Consumables.defaultFood().consumeSeconds(0.8f).build();
    public static final ItemEntry<Item> LEMON = food("lemon", Foods.LEMON, FAST_FOOD)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> LIME = food("lime", Foods.LIME, FAST_FOOD)
            .tag(TropicraftTags.Items.FRUITS)
            .tag(TropicraftTags.Items.LIME)
            .register();
    public static final ItemEntry<Item> GRAPEFRUIT = food("grapefruit", Foods.GRAPEFRUIT, FAST_FOOD)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> ORANGE = food("orange", Foods.ORANGE, FAST_FOOD)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> PASSIONFRUIT = food("passionfruit", Foods.PASSIONFRUIT, FAST_FOOD)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> JOCOTE = food("jocote", Foods.JOCOTE, FAST_FOOD)
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<BlockItem> PAPAYA = REGISTRATE.item("papaya", p -> new BlockItem(TropicraftBlocks.PAPAYA.get(), p))
            .properties(p -> p.food(Foods.PAPAYA))
            .tag(TropicraftTags.Items.FRUITS)
            .register();
    public static final ItemEntry<Item> PINEAPPLE_CUBES = food("pineapple_cubes", Foods.PINEAPPLE_CUBES, FAST_FOOD)
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(prov.itemLookup(), RecipeCategory.FOOD, ctx.get(), 2)
                    .requires(TropicraftBlocks.PINEAPPLE)
                    .unlockedBy("has_pineapple", prov.has(TropicraftBlocks.PINEAPPLE))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> COCONUT_CHUNK = food("coconut_chunk", Foods.COCONUT_CHUNK, FAST_FOOD).register();

    public static final ItemEntry<BlockItem> RAW_COFFEE_BEAN = REGISTRATE.item("raw_coffee_bean", p -> new BlockItem(TropicraftBlocks.COFFEE_BUSH.get(), p))
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(prov.itemLookup(), RecipeCategory.FOOD, ctx.get())
                    .requires(TropicraftItems.COFFEE_BERRY.get())
                    .unlockedBy("has_coffee_bean", prov.has(TropicraftItems.COFFEE_BERRY.get()))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> ROASTED_COFFEE_BEAN = simpleItem("roasted_coffee_bean")
            .recipe((ctx, prov) -> prov.food(ingredient(RAW_COFFEE_BEAN), RecipeCategory.FOOD, ctx, 0.1f))
            .register();
    public static final ItemEntry<Item> COFFEE_BERRY = simpleItem("coffee_berry").register();
    public static final ItemEntry<Item> BAMBOO_MUG = simpleItem("bamboo_mug")
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                    .pattern("X X").pattern("X X").pattern("XXX")
                    .define('X', Items.BAMBOO)
                    .unlockedBy("has_bamboo", prov.has(Items.BAMBOO))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> BAMBOO_BOWL = simpleItem("bamboo_bowl")
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                    .pattern("X X").pattern(" X ")
                    .define('X', Items.BAMBOO)
                    .unlockedBy("has_bamboo", prov.has(Items.BAMBOO))
                    .save(prov))
            .register();

    public static final ItemEntry<Item> GREEN_PLANTAIN = food("green_plantain", Foods.PLANTAIN).tag(TropicraftTags.Items.PLANTAIN).register();
    public static final ItemEntry<Item> YELLOW_PLANTAIN = food("yellow_plantain", Foods.PLANTAIN).tag(TropicraftTags.Items.PLANTAIN).register();
    public static final ItemEntry<Item> DRIED_PLANTAINS = food("dried_plantains", Foods.DRIED_PLANTAINS)
            .recipe((ctx, prov) -> prov.food(DataIngredient.tag(prov.itemLookup().getOrThrow(TropicraftTags.Items.PLANTAIN)), RecipeCategory.FOOD, ctx, 0.1f))
            .register();

    public static final ItemEntry<Item> MOFONGO = REGISTRATE.item("mofongo", p -> new Item(p))
            .properties(p -> p.food(Foods.MOFONGO).craftRemainder(BAMBOO_BOWL.get()).usingConvertsTo(BAMBOO_BOWL.get()))
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(prov.itemLookup(), RecipeCategory.FOOD, ctx.get())
                    .requires(DRIED_PLANTAINS, 2)
                    .requires(BAMBOO_BOWL)
                    .requires(Items.COOKED_PORKCHOP)
                    .unlockedBy("has_plantains", prov.has(DRIED_PLANTAINS))
                    .save(prov))
            .register();

    public static final ItemEntry<CocktailItem> COCKTAIL = REGISTRATE.item("cocktail", CocktailItem::new)
            .properties(p -> p.durability(0).stacksTo(1).craftRemainder(BAMBOO_MUG.get()))
            .model(() -> Models::generateCocktail)
            .tab(Tropicraft.CREATIVE_TAB, (ctx, modifier) -> {
                HolderLookup.RegistryLookup<Drink> drinks = modifier.getParameters().holders().lookupOrThrow(TropicraftRegistries.DRINK);
                drinks.listElements().forEach(drink -> {
                    ItemStack stack = new ItemStack(ctx.get());
                    stack.set(TropicraftDataComponents.COCKTAIL, Cocktail.ofDrink(drink));
                    modifier.accept(stack);
                });
            })
            .register();

    static {
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, prov -> {
            Holder<Drink> pinaColada = prov.registries().lookupOrThrow(TropicraftRegistries.DRINK).getOrThrow(TropicraftDrinks.PINA_COLADA);
            ShapelessRecipeBuilder.shapeless(prov.itemLookup(), RecipeCategory.FOOD, CocktailItem.makeDrink(pinaColada))
                    .requires(BAMBOO_MUG.get())
                    .requires(COCONUT_CHUNK.get())
                    .requires(PINEAPPLE_CUBES.get())
                    .unlockedBy("has_bamboo_mug", prov.has(BAMBOO_MUG.get()))
                    .save(prov, Tropicraft.resourceKey(Registries.RECIPE, "pina_colada"));
        });
    }

    public static final ItemEntry<Item> WHITE_PEARL = simpleItem("white_pearl").register();
    public static final ItemEntry<Item> BLACK_PEARL = simpleItem("black_pearl").register();
    public static final ItemEntry<Item> SCALE = simpleItem("scale")
            .tag(TropicraftTags.Items.REPAIRS_SCALE_ARMOR)
            .register();
    public static final ItemEntry<Item> NIGEL_STACHE = simpleItem("nigel_stache")
            .properties(p -> TropicraftArmorMaterials.applySafe(p, TropicraftArmorMaterials.NIGEL_STACHE, ArmorType.HELMET))
            .lang("Nigel's Moustache")
            .tag(TropicraftTags.Items.REPAIRS_NIGEL_STACHE)
            .model(() -> Models::generateNigelStache)
            .register();

    public static final ItemEntry<Item> COOL_SHADES = REGISTRATE.item("cool_shades", Item::new)
            .properties(p -> p.stacksTo(1).equippable(EquipmentSlot.HEAD))
            .model(() -> Models::generateCoolShades)
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
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), ModelTemplates.FLAT_ITEM, prov.modLoc("item/smolfish")))
            .register();
    public static final ItemEntry<Item> COOKED_FISH = food("cooked_fish", Foods.COOKED_FISH)
            .tag(ItemTags.FISHES)
            .recipe((ctx, prov) -> prov.food(ingredient(RAW_FISH), RecipeCategory.FOOD, ctx, 0.1f))
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), ModelTemplates.FLAT_ITEM, prov.modLoc("item/cooked_smolfish")))
            .register();
    public static final ItemEntry<Item> POISON_FROG_SKIN = simpleItem("poison_frog_skin").register();

    public static final ItemEntry<Item> IGUANA_LEATHER = simpleItem("iguana_leather")
            .tag(Tags.Items.LEATHERS)
            .register();
    public static final ItemEntry<TropicalFertilizerItem> TROPICAL_FERTILIZER = REGISTRATE.item("tropical_fertilizer", TropicalFertilizerItem::new)
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                    .requires(TropicraftFlower.MAGIC_MUSHROOM.get())
                    .requires(TropicraftFlower.CROTON.get())
                    .unlockedBy("has_magic_mushroom", prov.has(TropicraftFlower.MAGIC_MUSHROOM.get()))
                    .save(prov))
            .register();

    public static final ItemEntry<BambooItemFrameItem> BAMBOO_ITEM_FRAME = REGISTRATE.item("bamboo_item_frame", BambooItemFrameItem::new)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get(), 1)
                    .pattern("XXX")
                    .pattern("XYX")
                    .pattern("XXX")
                    .define('X', Items.BAMBOO)
                    .define('Y', Items.LEATHER)
                    .unlockedBy("has_bamboo", prov.has(Items.BAMBOO))
                    .unlockedBy("has_leather", prov.has(Items.LEATHER))
                    .save(prov))
            .register();

    public static final ItemEntry<Item> MUSIC_DISC_BURIED_TREASURE = musicDisc(TropicraftJukeboxSongs.BURIED_TREASURE).register();
    public static final ItemEntry<Item> MUSIC_DISC_EASTERN_ISLES = musicDisc(TropicraftJukeboxSongs.EASTERN_ISLES).register();
    public static final ItemEntry<Item> MUSIC_DISC_THE_TRIBE = musicDisc(TropicraftJukeboxSongs.THE_TRIBE).register();
    public static final ItemEntry<Item> MUSIC_DISC_LOW_TIDE = musicDisc(TropicraftJukeboxSongs.LOW_TIDE).register();
    public static final ItemEntry<Item> MUSIC_DISC_TRADE_WINDS = musicDisc(TropicraftJukeboxSongs.TRADE_WINDS).register();
    public static final ItemEntry<Item> MUSIC_DISC_SUMMERING = musicDisc(TropicraftJukeboxSongs.SUMMERING).register();

    private static ItemBuilder<Item, Registrate> musicDisc(ResourceKey<JukeboxSong> song) {
        return REGISTRATE.item("music_disc_" + song.location().getPath(), Item::new)
                .properties(p -> p.rarity(Rarity.RARE).component(DataComponents.JUKEBOX_PLAYABLE, new JukeboxPlayable(new EitherHolder<>(song))))
                .lang("Music Disc")
                .tag(Tags.Items.MUSIC_DISCS);
    }

    public static final ItemEntry<Item> TROPICAL_FISH_BUCKET = fishBucket("tropical_fish_bucket", TropicraftEntities.TROPICAL_FISH).register();
    public static final ItemEntry<Item> SARDINE_BUCKET = fishBucket("sardine_bucket", TropicraftEntities.RIVER_SARDINE).register();
    public static final ItemEntry<Item> PIRANHA_BUCKET = fishBucket("piranha_bucket", TropicraftEntities.PIRANHA).register();

    private static <T extends AbstractFish> ItemBuilder<Item, Registrate> fishBucket(String name, Supplier<? extends EntityType<T>> entity) {
        return REGISTRATE.item(name, p -> (Item) new TropicraftFishBucketItem<>(entity.get(), Fluids.WATER, p))
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
    public static final ItemEntry<Item> GIBNUT_SPAWN_EGG = spawnEgg("gibnut_spawn_egg", TropicraftEntities.GIBNUT).register();
    public static final ItemEntry<Item> MANATEE_SPAWN_EGG = spawnEgg("manatee_spawn_egg", TropicraftEntities.MANATEE).register();
    public static final ItemEntry<Item> SLENDER_HARVEST_MOUSE_SPAWN_EGG = spawnEgg("slender_harvest_mouse_spawn_egg", TropicraftEntities.SLENDER_HARVEST_MOUSE).register();
    public static final ItemEntry<Item> TOUCAN_SPAWN_EGG = spawnEgg("toucan_spawn_egg", TropicraftEntities.TOUCAN).register();
    public static final ItemEntry<Item> PAPYRUS_CANARY_SPAWN_EGG = spawnEgg("papyrus_canary_spawn_egg", TropicraftEntities.PAPYRUS_CANARY).register();
    public static final ItemEntry<Item> PAPYRUS_GONOLEK_SPAWN_EGG = spawnEgg("papyrus_gonolek_spawn_egg", TropicraftEntities.PAPYRUS_GONOLEK).register();
    public static final ItemEntry<Item> SHOEBILL_STORK_SPAWN_EGG = spawnEgg("shoebill_stork_spawn_egg", TropicraftEntities.SHOEBILL_STORK).register();
    public static final ItemEntry<Item> WHITE_COLLARED_OLIVEBACK_SPAWN_EGG = spawnEgg("white_collared_oliveback_spawn_egg", TropicraftEntities.WHITE_COLLARED_OLIVEBACK).register();
    public static final ItemEntry<Item> WHITE_WINGED_WARBLER_SPAWN_EGG = spawnEgg("white_winged_warbler_spawn_egg", TropicraftEntities.WHITE_WINGED_WARBLER).register();
    public static final ItemEntry<Item> STARFISH_SPAWN_EGG = spawnEgg("starfish_spawn_egg", TropicraftEntities.STARFISH).register();

    private static <T extends Mob> ItemBuilder<Item, Registrate> spawnEgg(String name, RegistryEntry<EntityType<?>, EntityType<T>> entity) {
        return REGISTRATE.item(name, p -> new SpawnEggItem(entity.get(), p));
    }

    public static final ImmutableMap<AshenMasks, ItemEntry<AshenMaskItem>> ASHEN_MASKS = Arrays.stream(AshenMasks.values())
            .collect(Maps.<AshenMasks, AshenMasks, ItemEntry<AshenMaskItem>>toImmutableEnumMap(Function.identity(), type ->
                    REGISTRATE.item("ashen_mask_" + type.id(), AshenMaskItem::new)
                            .tag(TropicraftTags.Items.ASHEN_MASKS)
                            .lang(type.getName())
                            .model(() -> (ctx, prov) -> Models.generateAshenMask(ctx, prov, type))
                            .register()
            ));

    public static final ItemEntry<Item> DAGGER = simpleItem("dagger")
            .properties(p -> DaggerItem.applyProperties(TropicraftToolMaterials.ZIRCON, p.stacksTo(1)))
            .model(() -> Models::generateHandheld)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.COMBAT, ctx.get())
                    .pattern("X")
                    .pattern("I")
                    .define('X', TropicraftBlocks.CHUNK.get())
                    .define('I', BAMBOO_STICK.get())
                    .unlockedBy("has_" + prov.safeName(TropicraftBlocks.CHUNK.get()), prov.has(TropicraftBlocks.CHUNK.get()))
                    .unlockedBy("has_bamboo", prov.has(Items.BAMBOO))
                    .save(prov))
            .register();

    public static final ItemEntry<BlowGunItem> BLOW_GUN = REGISTRATE.item("blow_gun", BlowGunItem::new)
            .properties(p -> p.stacksTo(1))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.COMBAT, ctx.get())
                    .pattern("X  ")
                    .pattern(" I ")
                    .pattern("  X")
                    .define('X', BAMBOO_STICK.get())
                    .define('I', ZIRCON.get())
                    .unlockedBy("has_" + prov.safeName(ZIRCON.get()), prov.has(ZIRCON.get()))
                    .unlockedBy("has_" + prov.safeName(BAMBOO_STICK.get()), prov.has(BAMBOO_STICK.get()))
                    .save(prov))
            .register();

    // TODO add zirconium tools

    public static final ItemEntry<Item> ZIRCON_HOE = hoe("zircon_hoe", TropicraftToolMaterials.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_HOE = hoe("zirconium_hoe", TropicraftToolMaterials.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_HOE = hoe("eudialyte_hoe", TropicraftToolMaterials.EUDIALYTE, EUDIALYTE).register();

    public static final ItemEntry<Item> ZIRCON_AXE = axe("zircon_axe", TropicraftToolMaterials.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_AXE = axe("zirconium_axe", TropicraftToolMaterials.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_AXE = axe("eudialyte_axe", TropicraftToolMaterials.EUDIALYTE, EUDIALYTE).register();

    public static final ItemEntry<Item> ZIRCON_PICKAXE = pickaxe("zircon_pickaxe", TropicraftToolMaterials.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_PICKAXE = pickaxe("zirconium_pickaxe", TropicraftToolMaterials.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_PICKAXE = pickaxe("eudialyte_pickaxe", TropicraftToolMaterials.EUDIALYTE, EUDIALYTE).register();

    public static final ItemEntry<Item> ZIRCON_SHOVEL = shovel("zircon_shovel", TropicraftToolMaterials.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_SHOVEL = shovel("zirconium_shovel", TropicraftToolMaterials.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_SHOVEL = shovel("eudialyte_shovel", TropicraftToolMaterials.EUDIALYTE, EUDIALYTE).register();

    public static final ItemEntry<Item> ZIRCON_SWORD = sword("zircon_sword", TropicraftToolMaterials.ZIRCON, ZIRCON).register();
    public static final ItemEntry<Item> ZIRCONIUM_SWORD = sword("zirconium_sword", TropicraftToolMaterials.ZIRCONIUM, ZIRCONIUM).register();
    public static final ItemEntry<Item> EUDIALYTE_SWORD = sword("eudialyte_sword", TropicraftToolMaterials.EUDIALYTE, EUDIALYTE).register();

    private static ItemBuilder<Item, Registrate> hoe(String name, ToolMaterial material, Supplier<? extends Item> input) {
        return REGISTRATE.item(name, p -> (Item) new HoeItem(material, 3.0f, -2.4f, p))
                .tag(ItemTags.HOES)
                .model(() -> Models::generateHandheld)
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.TOOLS, ctx.get())
                        .pattern("XX")
                        .pattern(" B")
                        .pattern(" B")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), prov.has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), prov.has(Items.BAMBOO))
                        .save(prov)
                );
    }

    private static ItemBuilder<Item, Registrate> shovel(String name, ToolMaterial material, Supplier<? extends Item> input) {
        return REGISTRATE.item(name, p -> (Item) new ShovelItem(material, 1.5f, -3.0f, p))
                .tag(ItemTags.SHOVELS)
                .model(() -> Models::generateHandheld)
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.TOOLS, ctx.get())
                        .pattern("X")
                        .pattern("B")
                        .pattern("B")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), prov.has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), prov.has(Items.BAMBOO))
                        .save(prov)
                );
    }

    private static ItemBuilder<Item, Registrate> pickaxe(String name, ToolMaterial material, Supplier<? extends Item> input) {
        return REGISTRATE.item(name, Item::new)
                .properties(p -> p.pickaxe(material, 1.0f, -2.8f))
                .tag(ItemTags.PICKAXES)
                .model(() -> Models::generateHandheld)
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.TOOLS, ctx.get())
                        .pattern("XXX")
                        .pattern(" B ")
                        .pattern(" B ")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), prov.has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), prov.has(Items.BAMBOO))
                        .save(prov)
                );
    }

    private static ItemBuilder<Item, Registrate> axe(String name, ToolMaterial material, Supplier<? extends Item> input) {
        return REGISTRATE.item(name, p -> (Item) new AxeItem(material, 6.0f, -3.0f, p))
                .tag(ItemTags.AXES)
                .model(() -> Models::generateHandheld)
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.TOOLS, ctx.get())
                        .pattern("XX")
                        .pattern("XB")
                        .pattern(" B")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), prov.has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), prov.has(Items.BAMBOO))
                        .save(prov)
                );
    }

    private static ItemBuilder<Item, Registrate> sword(String name, ToolMaterial material, Supplier<? extends Item> input) {
        return REGISTRATE.item(name, Item::new)
                .properties(p -> p.sword(material, 3.0f, -2.4f))
                .tag(ItemTags.SWORDS)
                .model(() -> Models::generateHandheld)
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.COMBAT, ctx.get())
                        .pattern("X")
                        .pattern("X")
                        .pattern("B")
                        .define('X', input.get())
                        .define('B', BAMBOO_STICK.get())
                        .unlockedBy("has_" + prov.safeName(input.get()), prov.has(input.get()))
                        .unlockedBy("has_" + prov.safeName(Items.BAMBOO), prov.has(Items.BAMBOO))
                        .save(prov)
                );
    }

    public static final ItemEntry<Item> FIRE_BOOTS = fireArmor("fire_boots", ArmorType.BOOTS).register();
    public static final ItemEntry<Item> FIRE_LEGGINGS = fireArmor("fire_leggings", ArmorType.LEGGINGS).register();
    public static final ItemEntry<Item> FIRE_CHESTPLATE = fireArmor("fire_chestplate", ArmorType.CHESTPLATE).register();
    public static final ItemEntry<Item> FIRE_HELMET = fireArmor("fire_helmet", ArmorType.HELMET).register();

    public static final ItemEntry<Item> SCALE_BOOTS = scaleArmor("scale_boots", ArmorType.BOOTS)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                    .pattern("X X")
                    .pattern("X X")
                    .define('X', SCALE.get())
                    .unlockedBy("has_" + prov.safeName(SCALE.get()), prov.has(SCALE.get()))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> SCALE_LEGGINGS = scaleArmor("scale_leggings", ArmorType.LEGGINGS)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                    .pattern("XXX")
                    .pattern("X X")
                    .pattern("X X")
                    .define('X', SCALE.get())
                    .unlockedBy("has_" + prov.safeName(SCALE.get()), prov.has(SCALE.get()))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> SCALE_CHESTPLATE = scaleArmor("scale_chestplate", ArmorType.CHESTPLATE)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                    .pattern("X X")
                    .pattern("XXX")
                    .pattern("XXX")
                    .define('X', SCALE.get())
                    .unlockedBy("has_" + prov.safeName(SCALE.get()), prov.has(SCALE.get()))
                    .save(prov))
            .register();
    public static final ItemEntry<Item> SCALE_HELMET = scaleArmor("scale_helmet", ArmorType.HELMET)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get())
                    .pattern("XXX")
                    .pattern("X X")
                    .define('X', SCALE.get())
                    .unlockedBy("has_" + prov.safeName(SCALE.get()), prov.has(SCALE.get()))
                    .save(prov))
            .register();

    public static final ItemEntry<Item> YELLOW_SCUBA_GOGGLES = scubaGoggles("yellow_scuba_goggles", ScubaType.YELLOW, () -> Items.YELLOW_DYE).register();
    public static final ItemEntry<ScubaHarnessItem> YELLOW_SCUBA_HARNESS = scubaHarness("yellow_scuba_harness", ScubaType.YELLOW, () -> Items.YELLOW_DYE).register();
    public static final ItemEntry<Item> YELLOW_SCUBA_FLIPPERS = scubaFlippers("yellow_scuba_flippers", ScubaType.YELLOW, () -> Items.YELLOW_DYE).register();
    public static final ItemEntry<Item> PINK_SCUBA_GOGGLES = scubaGoggles("pink_scuba_goggles", ScubaType.PINK, () -> Items.PINK_DYE).register();
    public static final ItemEntry<ScubaHarnessItem> PINK_SCUBA_HARNESS = scubaHarness("pink_scuba_harness", ScubaType.PINK, () -> Items.PINK_DYE).register();
    public static final ItemEntry<Item> PINK_SCUBA_FLIPPERS = scubaFlippers("pink_scuba_flippers", ScubaType.PINK, () -> Items.PINK_DYE).register();

    private static ItemBuilder<Item, Registrate> fireArmor(String name, ArmorType slotType) {
        return simpleItem(name)
                .properties(p -> TropicraftArmorMaterials.applySafe(p, TropicraftArmorMaterials.FIRE_ARMOR, slotType))
                .tag(ItemTags.TRIMMABLE_ARMOR)
                .model(() -> (ctx, prov) ->
                        Models.generateTrimmedArmor(ctx, prov, slotType, TropicraftEquipmentAssets.FIRE)
                );
    }

    private static ItemBuilder<Item, Registrate> scaleArmor(String name, ArmorType slotType) {
        return simpleItem(name)
                .properties(p -> TropicraftArmorMaterials.applySafe(p, TropicraftArmorMaterials.SCALE_ARMOR, slotType).component(TropicraftDataComponents.INCOMING_DAMAGE_MODIFIER, new DamageModifier(List.of(
                        new DamageModifier.Rule(
                                DamageSourcePredicate.Builder.damageType()
                                        .tag(TagPredicate.is(DamageTypeTags.IS_FIRE))
                                        .build(),
                                0.0f
                        )
                ))))
                .tag(ItemTags.TRIMMABLE_ARMOR)
                .model(() -> (ctx, prov) ->
                        Models.generateTrimmedArmor(ctx, prov, slotType, TropicraftEquipmentAssets.SCALE)
                );
    }

    private static ItemBuilder<Item, Registrate> scubaGoggles(String name, ScubaType type, Supplier<? extends Item> source) {
        return REGISTRATE.item(name, p -> new Item(scubaGoggles(p, type)))
                .clientExtension(() -> ScubaArmorItem.ClientExtensions::new)
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get(), 1)
                        .pattern("YYY")
                        .pattern("X X")
                        .pattern(" Z ")
                        .define('X', Blocks.GLASS_PANE)
                        .define('Y', ZIRCON.get())
                        .define('Z', source.get())
                        .unlockedBy("has_" + prov.safeName(source.get()), prov.has(source.get()))
                        .unlockedBy("has_" + prov.safeName(ZIRCON.get()), prov.has(ZIRCON.get()))
                        .save(prov));
    }

    private static Item.Properties scubaGoggles(Item.Properties properties, ScubaType scubaType) {
        ArmorType armorType = ArmorType.HELMET;
        ArmorMaterial material = scubaType.material();
        AttributeModifier visibilityBoost = new AttributeModifier(Tropicraft.location("underwater.visibility"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE);
        return properties
                .durability(armorType.getDurability(material.durability()))
                .attributes(material.createAttributes(armorType).withModifierAdded(
                        TropicraftAttributes.UNDERWATER_VISIBILITY, visibilityBoost, EquipmentSlotGroup.HEAD
                ))
                .component(
                        DataComponents.EQUIPPABLE,
                        Equippable.builder(armorType.getSlot())
                                .setEquipSound(material.equipSound())
                                .setAsset(material.assetId())
                                .setCameraOverlay(Tropicraft.location("gui/goggles"))
                                .build()
                )
                .repairable(material.repairIngredient());
    }

    private static ItemBuilder<ScubaHarnessItem, Registrate> scubaHarness(String name, ScubaType type, Supplier<? extends Item> source) {
        return REGISTRATE.item(name, p -> new ScubaHarnessItem(type, p))
                .properties(p -> p.component(TropicraftDataComponents.SCUBA_AIR, 0))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get(), 1)
                        .pattern("Y Y")
                        .pattern("YXY")
                        .pattern("YZY")
                        .define('X', source.get())
                        .define('Y', Tags.Items.LEATHERS)
                        .define('Z', AZURITE.get())
                        .unlockedBy("has_" + prov.safeName(AZURITE.get()), prov.has(AZURITE.get()))
                        .save(prov))
                .clientExtension(() -> ScubaArmorItem.ClientExtensions::new);
    }

    private static ItemBuilder<Item, Registrate> scubaFlippers(String name, ScubaType type, Supplier<? extends Item> source) {
        return simpleItem(name)
                .properties(p -> {
                    p = TropicraftArmorMaterials.applySafe(p, type.material(), ArmorType.BOOTS);
                    return p.component(DataComponents.ATTRIBUTE_MODIFIERS, type.material().createAttributes(ArmorType.BOOTS).withModifierAdded(
                            NeoForgeMod.SWIM_SPEED, new AttributeModifier(Tropicraft.location("scuba"), 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL), EquipmentSlotGroup.FEET
                    ));
                })
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get(), 1)
                        .pattern("XX")
                        .pattern("YY")
                        .pattern("XX")
                        .define('X', source.get())
                        .define('Y', ZIRCON.get())
                        .unlockedBy("has_" + prov.safeName(source.get()), prov.has(source.get()))
                        .unlockedBy("has_" + prov.safeName(ZIRCON.get()), prov.has(ZIRCON.get()))
                        .save(prov))
                .clientExtension(() -> ScubaArmorItem.ClientExtensions::new);
    }

    public static final ItemEntry<PonyBottleItem> YELLOW_PONY_BOTTLE = ponyBottle("yellow_pony_bottle", Blocks.YELLOW_STAINED_GLASS_PANE);
    public static final ItemEntry<PonyBottleItem> PINK_PONY_BOTTLE = ponyBottle("pink_pony_bottle", Blocks.PINK_STAINED_GLASS_PANE);

    private static ItemEntry<PonyBottleItem> ponyBottle(String name, Block glassPane) {
        return REGISTRATE.item(name, PonyBottleItem::new)
                .properties(p -> p.stacksTo(1).durability(32))
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.MISC, ctx.get(), 1)
                        .pattern("Y")
                        .pattern("X")
                        .define('X', glassPane)
                        .define('Y', Blocks.LEVER)
                        .unlockedBy("has_" + prov.safeName(glassPane), prov.has(glassPane))
                        .save(prov))
                .register();
    }

    public static final ItemEntry<WaterWandItem> WATER_WAND = REGISTRATE.item("water_wand", WaterWandItem::new)
            .properties(p -> p.stacksTo(1).durability(2000))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.TOOLS, ctx.get(), 1)
                    .pattern("  X")
                    .pattern(" Y ")
                    .pattern("Y  ")
                    .define('X', AZURITE.get())
                    .define('Y', Items.GOLD_INGOT)
                    .unlockedBy("has_" + prov.safeName(AZURITE.get()), prov.has(AZURITE.get()))
                    .unlockedBy("has_gold_ingot", prov.has(Items.GOLD_INGOT))
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

    private static ItemBuilder<Item, Registrate> food(String name, FoodProperties food) {
        return food(name, food, Consumables.DEFAULT_FOOD);
    }

    private static ItemBuilder<Item, Registrate> food(String name, FoodProperties food, Consumable consumable) {
        return simpleItem(name).properties(p -> p.food(food, consumable));
    }

    private static ItemBuilder<SignItem, Registrate> sign(WoodType woodType, Supplier<? extends Block> planks, Supplier<? extends StandingSignBlock> standingSign, Supplier<? extends WallSignBlock> wallSign) {
        String woodName = ResourceLocation.parse(woodType.name()).getPath();
        return REGISTRATE.item(woodName + "_sign", p -> new SignItem(standingSign.get(), wallSign.get(), p))
                .properties(p -> p.stacksTo(16).useBlockDescriptionPrefix())
                .tag(ItemTags.SIGNS)
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get())
                        .pattern("###")
                        .pattern("###")
                        .pattern(" | ")
                        .define('#', planks.get())
                        .define('|', Tags.Items.RODS_WOODEN)
                        .unlockedBy("has_" + woodName, prov.has(planks.get()))
                        .group("wooden_sign")
                        .save(prov));
    }

    public static void onItemRegister(RegisterEvent event) {
        if (event.getRegistryKey() != Registries.BLOCK) {
            return;
        }
        event.getRegistry().listElements().forEach(holder -> {
            if (holder.value() instanceof FlowerPotBlock flowerPot) {
                FlowerPotBlock emptyPot = flowerPot.getEmptyPot();
                Block content = flowerPot.getPotted();
                if (emptyPot.builtInRegistryHolder().is(TropicraftBlocks.BAMBOO_FLOWER_POT.getId()) && emptyPot != flowerPot) {
                    addPlant(TropicraftBlocks.BAMBOO_FLOWER_POT.get(), flowerPot);
                } else if (content.builtInRegistryHolder().key().location().getNamespace().equals(Tropicraft.ID)) {
                    addPlant((FlowerPotBlock) Blocks.FLOWER_POT, flowerPot);
                }
            }
        });
    }

    private static void addPlant(FlowerPotBlock empty, FlowerPotBlock full) {
        empty.addPlant(full.getPotted().builtInRegistryHolder().key().location(), () -> full);
    }

    private static DataIngredient ingredient(NonNullSupplier<? extends ItemLike> item) {
        return DataIngredient.items(item);
    }

    private static class Models {
        public static final ModelTemplate FACE_ITEM_TEMPLATE = ModelTemplates.createItem(Tropicraft.location("template_face_item").toString(), TextureSlot.LAYER0);
        public static final ModelTemplate EQUIPPED_ASHEN_MASK_TEMPLATE = ModelTemplates.createItem(Tropicraft.location("equipped_ashen_mask").toString(), "_equipped", TextureSlot.LAYER0, TextureSlot.BACK);

        private static <T extends FurnitureEntity> void generateFurniture(DataGenContext<Item, FurnitureItem<T>> ctx, RegistrateItemModelGenerator prov, String baseName, DyeColor color) {
            ResourceLocation model = prov.generateLayeredItem(ctx.get(),
                    prov.modLoc("item/" + baseName),
                    prov.modLoc("item/" + baseName + "_inverted")
            );
            prov.itemModelOutput.accept(ctx.get(), tintedModel(model, ItemModelGenerators.BLANK_LAYER, new Constant(color.getTextColor())));
        }

        private static void generateBambooSpear(DataGenContext<Item, SpearItem> ctx, RegistrateItemModelGenerator prov) {
            ModelTemplate template = ModelTemplates.createItem(Tropicraft.location("spear").toString(), TextureSlot.LAYER0);
            ModelTemplate throwingTemplate = ModelTemplates.createItem(Tropicraft.location("spear_throwing").toString(), "_throwing", TextureSlot.LAYER0);

            TextureMapping textures = TextureMapping.layer0(ctx.get());
            ItemModel.Unbaked normalModel = plainModel(prov.createFlatItemModel(ctx.get(), template));
            ItemModel.Unbaked throwingModel = plainModel(throwingTemplate.create(ctx.get(), textures, prov.modelOutput));

            prov.itemModelOutput.accept(ctx.get(), conditional(isUsingItem(), throwingModel, normalModel));
        }

        private static void generateLoveTropicsShell(DataGenContext<Item, LoveTropicsShellItem> ctx, RegistrateItemModelGenerator prov) {
            prov.generateItemWithTintedOverlay(ctx.get(), "_inverted", new TropicraftItemTintSources.LoveTropicsShell());
        }

        private static void generateCocktail(DataGenContext<Item, CocktailItem> ctx, RegistrateItemModelGenerator prov) {
            prov.generateItemWithTintedOverlay(ctx.get(), "_contents", new TropicraftItemTintSources.Cocktail());
        }

        private static void generateNigelStache(DataGenContext<Item, Item> ctx, RegistrateItemModelGenerator prov) {
            generateHeadEquippable(ctx, prov,
                    ModelTemplates.FLAT_ITEM.create(ctx.get(), TextureMapping.layer0(ctx.get()), prov.modelOutput),
                    FACE_ITEM_TEMPLATE.create(
                            ModelLocationUtils.getModelLocation(ctx.get(), "_equipped"),
                            TextureMapping.layer0(TextureMapping.getItemTexture(ctx.get(), "_equipped")),
                            prov.modelOutput
                    )
            );
        }

        private static void generateCoolShades(DataGenContext<Item, Item> ctx, RegistrateItemModelGenerator prov) {
            generateHeadEquippable(ctx, prov,
                    ModelTemplates.FLAT_ITEM.create(ctx.get(), TextureMapping.layer0(ctx.get()), prov.modelOutput),
                    ModelLocationUtils.getModelLocation(ctx.get(), "_equipped")
            );
        }

        private static void generateHeadEquippable(DataGenContext<Item, Item> ctx, RegistrateItemModelGenerator prov, ResourceLocation model, ResourceLocation equippedModel) {
            prov.itemModelOutput.accept(ctx.get(), select(
                    new DisplayContext(),
                    plainModel(model),
                    when(List.of(ItemDisplayContext.HEAD), plainModel(equippedModel))
            ));
        }

        private static void generateAshenMask(DataGenContext<Item, AshenMaskItem> ctx, RegistrateItemModelGenerator prov, AshenMasks type) {
            ResourceLocation equippedTexture = prov.modLoc("item/ashen_mask/equipped/" + type.id());
            TextureMapping equippedTextures = TextureMapping.layer0(equippedTexture)
                    .put(TextureSlot.BACK, equippedTexture.withSuffix("_back"));
            ItemModel.Unbaked model = plainModel(ModelTemplates.FLAT_ITEM.create(ctx.get(), TextureMapping.layer0(ctx.get()), prov.modelOutput));
            ItemModel.Unbaked equippedModel = plainModel(EQUIPPED_ASHEN_MASK_TEMPLATE.create(ctx.get(), equippedTextures, prov.modelOutput));
            prov.itemModelOutput.accept(ctx.get(), select(
                    new DisplayContext(),
                    model,
                    when(List.of(ItemDisplayContext.HEAD), equippedModel)
            ));
        }

        private static void generateTrimmedArmor(DataGenContext<Item, Item> ctx, RegistrateItemModelGenerator prov, ArmorType slotType, ResourceKey<EquipmentAsset> asset) {
            ResourceLocation prefix = ItemModelGenerators.prefixForSlotTrim(slotType.getSerializedName());
            prov.generateTrimmableItem(ctx.get(), asset, prefix, false);
        }

        private static void generateHandheld(DataGenContext<Item, Item> ctx, RegistrateItemModelGenerator prov) {
            prov.generateFlatItem(ctx.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        }
    }
}

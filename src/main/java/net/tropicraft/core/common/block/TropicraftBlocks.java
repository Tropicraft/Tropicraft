package net.tropicraft.core.common.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.builders.ItemBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.generators.RegistrateBlockModelGenerator;
import com.tterrag.registrate.providers.generators.RegistrateItemModelGenerator;
import com.tterrag.registrate.providers.generators.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.advancements.criterion.DataComponentMatchers;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.client.color.block.BlockTintSources;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.ConditionBuilder;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.data.models.model.TexturedModel;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.renderer.special.ChestSpecialRenderer;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.Unit;
import net.minecraft.util.Util;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.LilyPadBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RedstoneWallTorchBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SeagrassBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.StandingSignBlock;
import net.minecraft.world.level.block.TallFlowerBlock;
import net.minecraft.world.level.block.TallSeagrassBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.UntintedParticleLeavesBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.Tags;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.renderer.special.AirCompressorSpecialRenderer;
import net.tropicraft.core.client.renderer.special.DrinkMixerSpecialRenderer;
import net.tropicraft.core.client.renderer.tileentity.AirCompressorBlockEntityRenderer;
import net.tropicraft.core.client.renderer.tileentity.BambooChestRenderer;
import net.tropicraft.core.client.renderer.tileentity.DrinkMixerBlockEntityRenderer;
import net.tropicraft.core.client.renderer.tileentity.SifterBlockEntityRenderer;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TikiTorchBlock.TorchSection;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlock;
import net.tropicraft.core.common.block.jigarbov.JigarbovTorchType;
import net.tropicraft.core.common.block.tileentity.AirCompressorBlockEntity;
import net.tropicraft.core.common.block.tileentity.BambooChestBlockEntity;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;
import net.tropicraft.core.common.block.tileentity.SifterBlockEntity;
import net.tropicraft.core.common.block.tileentity.VolcanoBlockEntity;
import net.tropicraft.core.common.item.TropicraftItems;
import net.tropicraft.core.common.item.component.TropicraftDataComponents;
import net.tropicraft.core.mixin.BlockEntityTypeAccessor;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static net.minecraft.advancements.criterion.StatePropertiesPredicate.Builder.properties;
import static net.minecraft.client.data.models.BlockModelGenerators.*;
import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition.hasBlockStateProperties;
import static net.tropicraft.core.common.item.TropicraftItems.*;

public class TropicraftBlocks {
    private static final Registrate REGISTRATE = Tropicraft.registrate();

    private static LootItemCondition.Builder hasShears(RegistrateBlockLootTables loot) {
        return MatchTool.toolMatches(ItemPredicate.Builder.item().of(loot.itemLookup(), Items.SHEARS));
    }

    private static LootItemCondition.Builder hasSilkTouch(RegistrateBlockLootTables loot) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = loot.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
        return MatchTool.toolMatches(
                ItemPredicate.Builder.item().withComponents(DataComponentMatchers.Builder.components().partial(
                        DataComponentPredicates.ENCHANTMENTS,
                        EnchantmentsPredicate.enchantments(
                                List.of(new EnchantmentPredicate(
                                        enchantments.getOrThrow(Enchantments.SILK_TOUCH),
                                        MinMaxBounds.Ints.atLeast(1)
                                ))
                        )
                ).build())
        );
    }

    private static LootItemCondition.Builder hasNoSilkTouch(RegistrateBlockLootTables loot) {
        return hasSilkTouch(loot).invert();
    }

    private static LootItemCondition.Builder hasShearsOrSilkTouch(RegistrateBlockLootTables loot) {
        return hasShears(loot).or(hasSilkTouch(loot));
    }

    private static LootItemCondition.Builder hasNoShearsOrSilkTouch(RegistrateBlockLootTables loot) {
        return hasShearsOrSilkTouch(loot).invert();
    }

    private static Holder.Reference<Enchantment> fortune(RegistrateBlockLootTables loot) {
        return loot.getRegistries().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.FORTUNE);
    }

    private static final float[] FRUIT_SAPLING_RATES = new float[]{1 / 10.0f, 1 / 8.0f, 1 / 6.0f, 1 / 5.0f};
    private static final float[] SAPLING_RATES = new float[]{1.0f / 20.0f, 1.0f / 16.0f, 1.0f / 12.0f, 1.0f / 10.0f};
    private static final float[] RARE_SAPLING_RATES = new float[]{1.0f / 40.0f, 1.0f / 36.0f, 1.0f / 32.0f, 1.0f / 24.0f, 1.0f / 10.0f};

    private static final ColorParticleOption TROPICS_LEAF_PARTICLE = ColorParticleOption.create(ParticleTypes.TINTED_LEAVES, 0xff199d10);

    static {
        if (FMLEnvironment.getDist() == Dist.CLIENT) {
            //noinspection Convert2MethodRef
            REGISTRATE.addDataGenerator(ProviderType.BLOCKSTATE, prov -> Models.generateBlockStates(prov));
        }

        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
            prov.tag(TropicraftTags.Blocks.CARVER_REPLACEABLES).addTags(BlockTags.OVERWORLD_CARVER_REPLACEABLES);
            prov.tag(TropicraftTags.Blocks.BIRDS_LIKE_TO_STAND_ON).addTags(BlockTags.LOGS).addTags(BlockTags.LEAVES).addTags(TropicraftTags.Blocks.BRANCHES);
        });

        // Misc recipes
        REGISTRATE.addDataGenerator(ProviderType.RECIPE, prov -> {
            registerPlankRecipe(prov, () -> TropicraftBlocks.JOCOTE_LOG, Blocks.JUNGLE_PLANKS);
            registerPlankRecipe(prov, () -> TropicraftBlocks.PAPAYA_LOG, Blocks.JUNGLE_PLANKS);
            registerPlankRecipe(prov, () -> TropicraftBlocks.PAPAYA_WOOD, Blocks.JUNGLE_PLANKS);
            registerPlankRecipe(prov, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG, TropicraftBlocks.MANGROVE_PLANKS.get());
            registerPlankRecipe(prov, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD, TropicraftBlocks.MANGROVE_PLANKS.get());
        });
    }

    private static void registerPlankRecipe(RegistrateRecipeProvider prov, Supplier<ItemLike> logOrWood, Block output) {
        prov.singleItemUnfinished(
                DataIngredient.items(logOrWood.get()),
                RecipeCategory.BUILDING_BLOCKS,
                () -> output, 1, 4).group("planks").save(prov, prov.safeId(output) + "_from_" + prov.safeName(logOrWood.get())
        );
    }

    public static final BlockEntry<PortalWaterBlock> TELEPORT_WATER = REGISTRATE.block("teleport_water", PortalWaterBlock::new)
            .initialProperties(() -> Blocks.WATER)
            .blockstate(() -> (ctx, prov) -> prov.create(ctx.get(), prov.mcLoc("block/water")))
            .register();

    public static final BlockEntry<LiquidBlock> PORTAL_WATER = REGISTRATE.block("portal_water", p -> new LiquidBlock(Fluids.WATER, p))
            .initialProperties(() -> Blocks.WATER)
            .blockstate(() -> (ctx, prov) -> prov.create(ctx.get(), prov.mcLoc("block/water")))
            .register();

    public static final BlockEntry<Block> CHUNK = REGISTRATE.block("chunk", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).strength(6.0f).explosionResistance(30.0f))
            .blockstate(() -> Models::generateAllRotationVariantBlock)
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .lang("Chunk O' Head")
            .simpleItem()
            .register();

    public static final BlockEntry<DropExperienceBlock> AZURITE_ORE = ore("azurite_ore", TropicraftItems.AZURITE, MapColor.STONE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .item()
            .tag(TropicraftTags.Items.AZURITE_ORE, Tags.Items.ORES)
            .build()
            .register();
    public static final BlockEntry<DropExperienceBlock> EUDIALYTE_ORE = ore("eudialyte_ore", TropicraftItems.EUDIALYTE, MapColor.STONE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .item()
            .tag(TropicraftTags.Items.EUDIALYTE_ORE, Tags.Items.ORES)
            .build()
            .register();
    public static final BlockEntry<DropExperienceBlock> MANGANESE_ORE = ore("manganese_ore", MapColor.STONE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .item()
            .tag(TropicraftTags.Items.MANGANESE_ORE, Tags.Items.ORES)
            .build()
            .register();
    public static final BlockEntry<DropExperienceBlock> SHAKA_ORE = ore("shaka_ore", MapColor.COLOR_BLACK)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .item()
            .tag(TropicraftTags.Items.SHAKA_ORE, Tags.Items.ORES)
            .build()
            .register();
    public static final BlockEntry<DropExperienceBlock> ZIRCON_ORE = ore("zircon_ore", TropicraftItems.ZIRCON, MapColor.STONE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .item()
            .tag(TropicraftTags.Items.ZIRCON_ORE, Tags.Items.ORES)
            .build()
            .register();

    private static BlockBuilder<DropExperienceBlock, Registrate> ore(String name, MapColor color) {
        return REGISTRATE.block(name, p -> new DropExperienceBlock(UniformInt.of(0, 2), p))
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.strength(3.0f).mapColor(color))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE, Tags.Blocks.ORES)
                .item()
                .tag(Tags.Items.ORES)
                .build();
    }

    private static BlockBuilder<DropExperienceBlock, Registrate> ore(String name, Supplier<Item> gem, MapColor color) {
        return REGISTRATE.block(name, p -> new DropExperienceBlock(UniformInt.of(0, 2), p))
                .initialProperties(() -> Blocks.STONE)
                .properties(p -> p.strength(3.0f).mapColor(color))
                .loot((loot, block) -> loot.add(block,
                        loot.createSilkTouchDispatchTable(block,
                                loot.applyExplosionDecay(block, lootTableItem(gem.get())
                                        .apply(ApplyBonusCount.addOreBonusCount(fortune(loot))))
                        )
                ))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE, Tags.Blocks.ORES)
                .simpleItem();
    }

    public static final BlockEntry<Block> AZURITE_BLOCK = oreStorageBlock("azurite_block", MapColor.COLOR_LIGHT_BLUE, TropicraftItems.AZURITE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .register();
    public static final BlockEntry<Block> EUDIALYTE_BLOCK = oreStorageBlock("eudialyte_block", MapColor.COLOR_PINK, TropicraftItems.EUDIALYTE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .register();
    public static final BlockEntry<Block> MANGANESE_BLOCK = oreStorageBlock("manganese_block", MapColor.COLOR_PURPLE, TropicraftItems.MANGANESE)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .register();
    public static final BlockEntry<Block> SHAKA_BLOCK = oreStorageBlock("shaka_block", MapColor.COLOR_BLUE, TropicraftItems.SHAKA)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .register();
    public static final BlockEntry<Block> ZIRCON_BLOCK = oreStorageBlock("zircon_block", MapColor.COLOR_RED, TropicraftItems.ZIRCON)
            .tag(BlockTags.NEEDS_IRON_TOOL)
            .register();
    public static final BlockEntry<Block> ZIRCONIUM_BLOCK = oreStorageBlock("zirconium_block", MapColor.COLOR_PINK, TropicraftItems.ZIRCONIUM).register();

    private static BlockBuilder<Block, Registrate> oreStorageBlock(String name, MapColor color, ItemEntry<Item> ingredient) {
        return REGISTRATE.block(name, Block::new)
                .properties(p -> p.requiresCorrectToolForDrops().mapColor(color).sound(SoundType.METAL).destroyTime(5.0f).explosionResistance(6.0f))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .recipe((ctx, prov) -> prov.storage(ingredient, RecipeCategory.BUILDING_BLOCKS, ctx))
                .simpleItem();
    }

    public static final Map<TropicraftFlower, BlockEntry<TropicsFlowerBlock>> FLOWERS = Arrays.stream(TropicraftFlower.values())
            .collect(ImmutableMap.toImmutableMap(Function.identity(), flower -> {
                BlockBuilder<TropicsFlowerBlock, Registrate> builder = REGISTRATE
                        .block(flower.getId(), p -> new TropicsFlowerBlock(flower, flower.getEffect(), flower.getEffectDuration(), flower.getShape(), p))
                        .initialProperties(() -> Blocks.POPPY)
                        .tag(flower.getTags())
                        .blockstate(() -> Models::generateFlower)
                        .item()
                        .tag(ItemTags.FLOWERS)
                        .model(() -> (ctx, prov) ->
                                prov.generateFlatItem(ctx.get(), prov.modBlockTexture("flower/" + ctx.getName()))
                        )
                        .build();
                Item dye = flower.getDye();
                if (dye != null) {
                    builder = builder.recipe((ctx, prov) -> {
                        prov.singleItemUnfinished(DataIngredient.items(ctx.get()), RecipeCategory.MISC, () -> dye, 1, 2).save(prov, Tropicraft.resourceKey(Registries.RECIPE, name(dye)));
                    });
                }
                return builder.register();
            }));

    public static final BlockEntry<BlockTropicraftSand> PURIFIED_SAND = REGISTRATE.block("purified_sand", BlockTropicraftSand::new)
            .initialProperties(() -> Blocks.SAND)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .blockstate(() -> Models::generatePurifiedSand)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .recipe((ctx, prov) -> {
                prov.smelting(DataIngredient.items(ctx), RecipeCategory.MISC, CookingBookCategory.BLOCKS, () -> Blocks.GLASS, 0.3f);
                prov.singleItem(DataIngredient.items(ctx), RecipeCategory.BUILDING_BLOCKS, () -> Blocks.SAND, 1, 1);
            })
            .register();

    public static final BlockEntry<Block> PACKED_PURIFIED_SAND = REGISTRATE.block("packed_purified_sand", Block::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.mapColor(MapColor.STONE).strength(0.8f).requiresCorrectToolForDrops())
            .blockstate(() -> Models::generateAllRotationVariantBlock)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.BUILDING_BLOCKS, ctx.get())
                    .pattern("XX").pattern("XX")
                    .define('X', PURIFIED_SAND.get())
                    .unlockedBy("has_purified_sand", prov.has(PURIFIED_SAND.get()))
                    .save(prov))
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<BlockTropicraftSand> CORAL_SAND = REGISTRATE.block("coral_sand", BlockTropicraftSand::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.mapColor(MapColor.COLOR_PINK))
            .blockstate(() -> Models::generateAllRotationVariantBlock)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<BlockTropicraftSand> FOAMY_SAND = REGISTRATE.block("foamy_sand", BlockTropicraftSand::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.mapColor(MapColor.COLOR_GREEN))
            .blockstate(() -> Models::generateAllRotationVariantBlock)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<VolcanicSandBlock> VOLCANIC_SAND = REGISTRATE.block("volcanic_sand", VolcanicSandBlock::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY))
            .blockstate(() -> Models::generateAllRotationVariantBlock)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<BlockTropicraftSand> MINERAL_SAND = REGISTRATE.block("mineral_sand", BlockTropicraftSand::new)
            .initialProperties(() -> Blocks.SAND)
            .blockstate(() -> Models::generateAllRotationVariantBlock)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<MudBlock> MUD = REGISTRATE.block("mud", MudBlock::new)
            .initialProperties(() -> Blocks.DIRT)
            .properties(p -> p.speedFactor(0.5f).isValidSpawn((s, w, pa, e) -> true).isRedstoneConductor((s, w, pa) -> true).isViewBlocking((s, w, pa) -> true).isSuffocating((s, w, pa) -> true))
            .tag(TropicraftTags.Blocks.MUD, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES, BlockTags.SUPPORTS_BAMBOO, BlockTags.MUD, BlockTags.SUPPORT_OVERRIDE_SNOW_LAYER)
            .blockstate(() -> Models::generateMud)
            .simpleItem()
            .register();

    public static final BlockEntry<Block> MUD_WITH_PIANGUAS = REGISTRATE.block("mud_with_pianguas", Block::new)
            .initialProperties(MUD)
            .loot((loot, block) -> loot.add(block, loot.applyExplosionDecay(TropicraftBlocks.MUD_WITH_PIANGUAS.get(),
                    lootTable()
                            .withPool(lootPool()
                                    .add(lootTableItem(TropicraftBlocks.MUD_WITH_PIANGUAS.get())
                                            .when(hasSilkTouch(loot))
                                            .otherwise(lootTableItem(TropicraftBlocks.MUD.get()))
                                    )
                            )
                            .withPool(lootPool()
                                    .when(hasNoSilkTouch(loot))
                                    .add(lootTableItem(TropicraftItems.PIANGUAS.get())
                                            .apply(ApplyBonusCount.addOreBonusCount(fortune(loot)))
                                    )
                            )
            )))
            .tag(TropicraftTags.Blocks.MUD, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES, BlockTags.SUPPORTS_BAMBOO, BlockTags.MUD, BlockTags.SUPPORT_OVERRIDE_SNOW_LAYER)
            .blockstate(() -> (ctx, prov) -> prov.createRotatedVariantBlock(ctx.get()))
            .simpleItem()
            .register();

    public static final BlockEntry<RotatedPillarBlock> BAMBOO_BUNDLE = REGISTRATE.block("bamboo_bundle", RotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).sound(SoundType.BAMBOO).strength(0.2f, 5.0f))
            .blockstate(() -> (ctx, prov) -> prov.generateAxisBlock(ctx.get(), prov.modBlockTexture("bamboo_side"), prov.modBlockTexture("bamboo_end")))
            .recipe((ctx, prov) -> prov.singleItem(DataIngredient.items((NonNullSupplier<? extends ItemLike>) BAMBOO_STICK), RecipeCategory.BUILDING_BLOCKS, ctx, 9, 2))
            .simpleItem()
            .register();

    public static final BlockEntry<RotatedPillarBlock> THATCH_BUNDLE = REGISTRATE.block("thatch_bundle", RotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).sound(SoundType.BAMBOO).strength(0.2f, 5.0f).ignitedByLava().pushReaction(PushReaction.DESTROY))
            .blockstate(() -> (ctx, prov) -> prov.generateAxisBlock(ctx.get(), prov.modBlockTexture("thatch_side"), prov.modBlockTexture("thatch_end")))
            .recipe((ctx, prov) -> prov.singleItem(DataIngredient.items(Items.SUGAR_CANE), RecipeCategory.BUILDING_BLOCKS, ctx, 4, 1))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> MAHOGANY_PLANKS = planks("mahogany_planks", MapColor.COLOR_BROWN, () -> DataIngredient.items(TropicraftBlocks.MAHOGANY_LOG.get(), TropicraftBlocks.MAHOGANY_WOOD.get())).register();
    public static final BlockEntry<Block> PALM_PLANKS = planks("palm_planks", MapColor.COLOR_BROWN, () -> DataIngredient.items(TropicraftBlocks.PALM_LOG.get(), TropicraftBlocks.PALM_WOOD.get())).register();

    public static final BlockEntry<RotatedPillarBlock> MAHOGANY_LOG = log("mahogany_log", MapColor.WOOD, MapColor.COLOR_BROWN).register();
    public static final BlockEntry<RotatedPillarBlock> PALM_LOG = log("palm_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN).register();

    // TODO: fix this typo
    public static final BlockEntry<RotatedPillarBlock> MAHOGANY_WOOD = wood("mohogany_wood", MapColor.WOOD, MAHOGANY_LOG).lang("Mahogany Wood").register();
    public static final BlockEntry<RotatedPillarBlock> PALM_WOOD = wood("palm_wood", MapColor.COLOR_GRAY, PALM_LOG).register();

    public static final BlockEntry<StairBlock> PALM_STAIRS = woodenStairs("palm_stairs", PALM_PLANKS).register();
    public static final BlockEntry<StairBlock> MAHOGANY_STAIRS = woodenStairs("mahogany_stairs", MAHOGANY_PLANKS).register();
    public static final BlockEntry<StairBlock> THATCH_STAIRS = woodenStairs("thatch_stairs", THATCH_BUNDLE)
            .blockstate(() -> Models::generateThatchStairs)
            .register();

    public static final BlockEntry<StairBlock> THATCH_STAIRS_FUZZY = woodenStairs("thatch_stairs_fuzzy", THATCH_BUNDLE)
            .blockstate(() -> Models::generateThatchRoof)
            .lang("Thatch Roof")
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get(), 4)
                    .pattern("C  ").pattern("XC ").pattern("XXC")
                    .define('X', THATCH_BUNDLE.get())
                    .define('C', Items.SUGAR_CANE)
                    .unlockedBy("has_thatch_bundle", prov.has(THATCH_BUNDLE.get()))
                    .save(prov))
            .register();
    public static final BlockEntry<StairBlock> BAMBOO_STAIRS = woodenStairs("bamboo_stairs", BAMBOO_BUNDLE)
            .blockstate(() -> (ctx, prov) -> {
                Material side = prov.modBlockTexture("bamboo_side");
                Material end = prov.modBlockTexture("bamboo_end");
                prov.generateStairsBlock(ctx.get(), side, end, end);
            })
            .register();
    public static final BlockEntry<StairBlock> CHUNK_STAIRS = stoneStairs("chunk_stairs", CHUNK).register();

    public static final BlockEntry<CoconutBlock> COCONUT = REGISTRATE.block("coconut", CoconutBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).strength(2.0f).sound(SoundType.STONE).pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, droppingChunks(loot, block, TropicraftItems.COCONUT_CHUNK)))
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> (ctx, prov) ->
                    prov.createCrossBlock(ctx.get(), PlantType.NOT_TINTED)
            )
            .item()
            .model(() -> Models::generateFlatBlockItem)
            .build()
            .register();

    public static final BlockEntry<SlabBlock> BAMBOO_SLAB = woodenSlab("bamboo_slab", BAMBOO_BUNDLE)
            .blockstate(() -> Models::generateBambooSlab)
            .register();
    public static final BlockEntry<SlabBlock> THATCH_SLAB = woodenSlab("thatch_slab", THATCH_BUNDLE)
            .blockstate(() -> Models::generateThatchSlab)
            .register();
    public static final BlockEntry<SlabBlock> CHUNK_SLAB = stoneSlab("chunk_slab", CHUNK).register();
    public static final BlockEntry<SlabBlock> PALM_SLAB = woodenSlab("palm_slab", PALM_PLANKS).register();
    public static final BlockEntry<SlabBlock> MAHOGANY_SLAB = woodenSlab("mahogany_slab", MAHOGANY_PLANKS).register();

    public static final BlockEntry<SaplingBlock> GRAPEFRUIT_SAPLING = sapling("grapefruit_sapling", TropicraftTreeGrowers.GRAPEFRUIT).register();
    public static final BlockEntry<SaplingBlock> LEMON_SAPLING = sapling("lemon_sapling", TropicraftTreeGrowers.LEMON).register();
    public static final BlockEntry<SaplingBlock> LIME_SAPLING = sapling("lime_sapling", TropicraftTreeGrowers.LIME).register();
    public static final BlockEntry<SaplingBlock> ORANGE_SAPLING = sapling("orange_sapling", TropicraftTreeGrowers.ORANGE).register();
    public static final BlockEntry<SaplingBlock> PAPAYA_SAPLING = sapling("papaya_sapling", TropicraftTreeGrowers.PAPAYA).register();
    public static final BlockEntry<SaplingBlock> MAHOGANY_SAPLING = sapling("mahogany_sapling", TropicraftTreeGrowers.RAINFOREST).register();
    public static final BlockEntry<SaplingBlock> PALM_SAPLING = sapling("palm_sapling", TropicraftTreeGrowers.PALM, () -> Blocks.SAND, CORAL_SAND, FOAMY_SAND, VOLCANIC_SAND, PURIFIED_SAND, MINERAL_SAND).register();

    public static final BlockEntry<LeavesBlock> MAHOGANY_LEAVES = leaves("mahogany_leaves", MAHOGANY_SAPLING, RARE_SAPLING_RATES, false).register();
    public static final BlockEntry<LeavesBlock> PALM_LEAVES = leaves("palm_leaves", PALM_SAPLING, SAPLING_RATES, false).register();
    public static final BlockEntry<LeavesBlock> KAPOK_LEAVES = leaves("kapok_leaves", false).register();
    public static final BlockEntry<LeavesBlock> FRUIT_LEAVES = leaves("fruit_leaves", true).register();
    public static final BlockEntry<UntintedParticleLeavesBlock> GRAPEFRUIT_LEAVES = fruitLeaves("grapefruit_leaves", GRAPEFRUIT_SAPLING, TropicraftItems.GRAPEFRUIT).register();
    public static final BlockEntry<UntintedParticleLeavesBlock> LEMON_LEAVES = fruitLeaves("lemon_leaves", LEMON_SAPLING, TropicraftItems.LEMON).register();
    public static final BlockEntry<UntintedParticleLeavesBlock> LIME_LEAVES = fruitLeaves("lime_leaves", LIME_SAPLING, TropicraftItems.LIME).register();
    public static final BlockEntry<UntintedParticleLeavesBlock> ORANGE_LEAVES = fruitLeaves("orange_leaves", ORANGE_SAPLING, TropicraftItems.ORANGE).register();
    public static final BlockEntry<LeavesBlock> PAPAYA_LEAVES = leaves("papaya_leaves", PAPAYA_SAPLING, SAPLING_RATES, true).register();
    public static final BlockEntry<LeavesBlock> WHITE_FLOWERING_LEAVES = leaves("white_flowering_leaves", true).register();
    public static final BlockEntry<LeavesBlock> RED_FLOWERING_LEAVES = leaves("red_flowering_leaves", true).register();
    public static final BlockEntry<LeavesBlock> BLUE_FLOWERING_LEAVES = leaves("blue_flowering_leaves", true).register();
    public static final BlockEntry<LeavesBlock> PURPLE_FLOWERING_LEAVES = leaves("purple_flowering_leaves", true).register();
    public static final BlockEntry<LeavesBlock> YELLOW_FLOWERING_LEAVES = leaves("yellow_flowering_leaves", true).register();

    public static final BlockEntry<FruitingVineBlock> PASSIONFRUIT_VINE = REGISTRATE.block("passionfruit_vine", FruitingVineBlock::new)
            .properties(p -> p.mapColor(MapColor.GRASS).replaceable().noCollision().strength(0.2f).sound(SoundType.VINE).ignitedByLava().pushReaction(PushReaction.DESTROY))
            .blockstate(() -> Models::generatePassionfruitVine)
            .loot((loot, block) -> loot.add(block, loot.createSilkTouchOrShearsDispatchTable(block, loot.applyExplosionCondition(block, lootTableItem(PASSIONFRUIT)
                    .when(hasBlockStateProperties(block).setProperties(properties().hasProperty(FruitingVineBlock.AGE, FruitingVineBlock.MAX_AGE)))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
            ))))
            .item()
            .model(() -> (ctx, prov) ->
                    prov.generateFlatItem(ctx.get(), prov.modBlockTexture("passionfruit_vine_1"))
            )
            .build()
            .register();

    public static final BlockEntry<RotatedPillarBlock> PAPAYA_LOG = log("papaya_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN).register();
    public static final BlockEntry<RotatedPillarBlock> PAPAYA_WOOD = wood("papaya_wood", MapColor.COLOR_GRAY, PAPAYA_LOG).register();

    public static final BlockEntry<SaplingBlock> PLANTAIN_SAPLING = sapling("plantain_sapling", TropicraftTreeGrowers.PLANTAIN).register();
    public static final BlockEntry<RotatedPillarBlock> PLANTAIN_STEM = log("plantain_stem", MapColor.COLOR_LIGHT_GREEN, MapColor.COLOR_BROWN).register();
    public static final BlockEntry<LeavesBlock> PLANTAIN_LEAVES = leaves("plantain_leaves", PLANTAIN_SAPLING, SAPLING_RATES, true).register();
    public static final BlockEntry<Block> GREEN_PLANTAIN_BUNCH = plantainBunch("green_plantain_bunch", () -> TropicraftItems.GREEN_PLANTAIN);
    public static final BlockEntry<Block> YELLOW_PLANTAIN_BUNCH = plantainBunch("yellow_plantain_bunch", () -> TropicraftItems.YELLOW_PLANTAIN);

    private static BlockEntry<Block> plantainBunch(String name, Supplier<ItemEntry<Item>> item) {
        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.MELON)
                .properties(p -> p.sound(SoundType.WART_BLOCK).strength(0.5f))
                .tag(BlockTags.MINEABLE_WITH_HOE)
                .loot((tables, block) -> tables.add(block, tables.createSingleItemTableWithSilkTouch(block, item.get().get(), UniformGenerator.between(3, 5))))
                .simpleItem()
                .register();
    }

    public static final BlockEntry<SaplingBlock> JOCOTE_SAPLING = sapling("jocote_sapling", TropicraftTreeGrowers.JOCOTE).register();
    public static final BlockEntry<RotatedPillarBlock> JOCOTE_LOG = log("jocote_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN).register();
    public static final BlockEntry<LeavesBlock> JOCOTE_LEAVES = leaves("jocote_leaves", JOCOTE_SAPLING, SAPLING_RATES, true).register();
    public static final BlockEntry<FruitingBranchBlock> JOCOTE_BRANCH = REGISTRATE.block("jocote_branch", FruitingBranchBlock::new)
            .properties(p -> p.sound(SoundType.AZALEA).noOcclusion().instabreak().randomTicks().pushReaction(PushReaction.DESTROY))
            .blockstate(() -> Models::generateJocoteBranch)
            .tag(TropicraftTags.Blocks.BRANCHES)
            .loot((loot, block) -> loot.add(block, lootTable().withPool(loot.applyExplosionCondition(block, lootPool().setRolls(ConstantValue.exactly(1))
                    .add(lootTableItem(block))
                    .add(lootTableItem(JOCOTE)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                            .when(hasBlockStateProperties(block).setProperties(properties().hasProperty(FruitingBranchBlock.AGE, FruitingBranchBlock.MAX_AGE)))
                    )
            ))))
            .item()
            .model(() -> (ctx, prov) ->
                    prov.generateFlatItem(ctx.get(), prov.modBlockTexture("jocote_branch_horizontal_0"))
            )
            .build()
            .register();

    public static final BlockEntry<MahoganyNutBlock> MAHOGANY_NUT = REGISTRATE.block("mahogany_nut", MahoganyNutBlock::new)
            .initialProperties(() -> Blocks.COCOA)
            .blockstate(() -> Models::generateMahoganyNut)
            .loot((loot, block) -> loot.add(block, lootTable().withPool(loot.applyExplosionCondition(block, lootPool().setRolls(ConstantValue.exactly(1))
                    .add(lootTableItem(block).when(hasBlockStateProperties(block).setProperties(properties().hasProperty(MahoganyNutBlock.AGE, MahoganyNutBlock.MAX_AGE))))
            ))))
            .item()
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), ModelTemplates.FLAT_ITEM))
            .build()
            .register();

    public static final BlockEntry<RotatedPillarBlock> RED_MANGROVE_LOG = mangroveLog("red_mangrove_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG.get()).register();
    public static final BlockEntry<RotatedPillarBlock> RED_MANGROVE_WOOD = wood("red_mangrove_wood", MapColor.COLOR_GRAY, RED_MANGROVE_LOG, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD.get()).register();
    public static final BlockEntry<MangroveRootsBlock> RED_MANGROVE_ROOTS = mangroveRoots("red_mangrove_roots").register();

    public static final BlockEntry<RotatedPillarBlock> LIGHT_MANGROVE_LOG = mangroveLog("light_mangrove_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG.get()).register();
    public static final BlockEntry<RotatedPillarBlock> LIGHT_MANGROVE_WOOD = wood("light_mangrove_wood", MapColor.COLOR_GRAY, LIGHT_MANGROVE_LOG, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD.get()).register();
    public static final BlockEntry<MangroveRootsBlock> LIGHT_MANGROVE_ROOTS = mangroveRoots("light_mangrove_roots").register();

    public static final BlockEntry<RotatedPillarBlock> BLACK_MANGROVE_LOG = mangroveLog("black_mangrove_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG.get()).register();
    public static final BlockEntry<RotatedPillarBlock> BLACK_MANGROVE_WOOD = wood("black_mangrove_wood", MapColor.COLOR_GRAY, BLACK_MANGROVE_LOG, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD.get()).register();
    public static final BlockEntry<MangroveRootsBlock> BLACK_MANGROVE_ROOTS = mangroveRoots("black_mangrove_roots").register();

    public static final BlockEntry<MangroveLeavesBlock> RED_MANGROVE_LEAVES = mangroveLeaves("red_mangrove_leaves", () -> TropicraftBlocks.RED_MANGROVE_PROPAGULE.get()).register();
    public static final BlockEntry<MangroveLeavesBlock> TALL_MANGROVE_LEAVES = mangroveLeaves("tall_mangrove_leaves", () -> TropicraftBlocks.TALL_MANGROVE_PROPAGULE.get()).register();
    public static final BlockEntry<MangroveLeavesBlock> TEA_MANGROVE_LEAVES = mangroveLeaves("tea_mangrove_leaves", () -> TropicraftBlocks.TEA_MANGROVE_PROPAGULE.get()).register();
    public static final BlockEntry<MangroveLeavesBlock> BLACK_MANGROVE_LEAVES = mangroveLeaves("black_mangrove_leaves", () -> TropicraftBlocks.BLACK_MANGROVE_PROPAGULE.get()).register();

    public static final BlockEntry<PropaguleBlock> RED_MANGROVE_PROPAGULE = propagule("red_mangrove_propagule", TropicraftTreeGrowers.RED_MANGROVE, "Rhizophora mangle").register();
    public static final BlockEntry<PropaguleBlock> TALL_MANGROVE_PROPAGULE = propagule("tall_mangrove_propagule", TropicraftTreeGrowers.TALL_MANGROVE, "Rhizophora racemosa").register();
    public static final BlockEntry<PropaguleBlock> TEA_MANGROVE_PROPAGULE = propagule("tea_mangrove_propagule", TropicraftTreeGrowers.TEA_MANGROVE, "Pelliciera rhizophorae").register();
    public static final BlockEntry<PropaguleBlock> BLACK_MANGROVE_PROPAGULE = propagule("black_mangrove_propagule", TropicraftTreeGrowers.BLACK_MANGROVE, "Avicennia germinans").register();

    public static final BlockEntry<RotatedPillarBlock> STRIPPED_MANGROVE_LOG = mangroveLog("stripped_mangrove_log", MapColor.COLOR_RED, MapColor.COLOR_RED, null).register();
    public static final BlockEntry<RotatedPillarBlock> STRIPPED_MANGROVE_WOOD = wood("stripped_mangrove_wood", MapColor.COLOR_RED, STRIPPED_MANGROVE_LOG).register();

    public static final BlockEntry<Block> MANGROVE_PLANKS = planks("mangrove_planks", MapColor.COLOR_BROWN, () -> DataIngredient.items((NonNullSupplier<? extends Block>) TropicraftBlocks.LIGHT_MANGROVE_LOG, TropicraftBlocks.RED_MANGROVE_LOG, TropicraftBlocks.BLACK_MANGROVE_LOG, TropicraftBlocks.LIGHT_MANGROVE_WOOD, TropicraftBlocks.RED_MANGROVE_WOOD, TropicraftBlocks.BLACK_MANGROVE_WOOD)).register();
    public static final BlockEntry<StairBlock> MANGROVE_STAIRS = woodenStairs("mangrove_stairs", MANGROVE_PLANKS).register();
    public static final BlockEntry<SlabBlock> MANGROVE_SLAB = woodenSlab("mangrove_slab", MANGROVE_PLANKS).register();
    public static final BlockEntry<FenceBlock> MANGROVE_FENCE = woodenFence("mangrove_fence", MANGROVE_PLANKS, i -> i).register();
    public static final BlockEntry<FenceGateBlock> MANGROVE_FENCE_GATE = fenceGate("mangrove_fence_gate", MANGROVE_PLANKS).register();
    public static final BlockEntry<DoorBlock> MANGROVE_DOOR = woodenDoor("mangrove_door", MANGROVE_PLANKS).register();
    public static final BlockEntry<TrapDoorBlock> MANGROVE_TRAPDOOR = trapdoor("mangrove_trapdoor", MANGROVE_PLANKS).register();

    public static final BlockEntry<ButtonBlock> MANGROVE_BUTTON = woodButton("mangrove_button", MANGROVE_PLANKS, "mangrove_planks").register();
    public static final BlockEntry<ButtonBlock> MAHOGANY_BUTTON = woodButton("mahogany_button", MAHOGANY_PLANKS, "mahogany_planks").register();
    public static final BlockEntry<ButtonBlock> PALM_BUTTON = woodButton("palm_button", PALM_PLANKS, "palm_planks").register();
    public static final BlockEntry<ButtonBlock> BAMBOO_BUTTON = woodButton("bamboo_button", BAMBOO_BUNDLE, "bamboo_end").register();
    public static final BlockEntry<ButtonBlock> THATCH_BUTTON = woodButton("thatch_button", THATCH_BUNDLE, "thatch_end").register();

    public static final BlockEntry<PressurePlateBlock> MANGROVE_PRESSURE_PLATE = pressurePlate("mangrove_pressure_plate", MANGROVE_PLANKS, "mangrove_planks").register();
    public static final BlockEntry<PressurePlateBlock> MAHOGANY_PRESSURE_PLATE = pressurePlate("mahogany_pressure_plate", MAHOGANY_PLANKS, "mahogany_planks").register();
    public static final BlockEntry<PressurePlateBlock> PALM_PRESSURE_PLATE = pressurePlate("palm_pressure_plate", PALM_PLANKS, "palm_planks").register();
    public static final BlockEntry<PressurePlateBlock> BAMBOO_PRESSURE_PLATE = pressurePlate("bamboo_pressure_plate", BAMBOO_BUNDLE, "bamboo_end").register();
    public static final BlockEntry<PressurePlateBlock> THATCH_PRESSURE_PLATE = pressurePlate("thatch_pressure_plate", THATCH_BUNDLE, "thatch_end").register();

    public static final BlockEntry<StandingSignBlock> MAHOGANY_SIGN = standingSign(TropicraftWoodTypes.MAHOGANY, () -> TropicraftItems.MAHOGANY_SIGN.get(), "mahogany_planks").register();
    public static final BlockEntry<StandingSignBlock> PALM_SIGN = standingSign(TropicraftWoodTypes.PALM, () -> TropicraftItems.PALM_SIGN.get(), "palm_planks").register();
    public static final BlockEntry<StandingSignBlock> BAMBOO_SIGN = standingSign(TropicraftWoodTypes.BAMBOO, () -> TropicraftItems.BAMBOO_SIGN.get(), "bamboo_end").register();
    public static final BlockEntry<StandingSignBlock> THATCH_SIGN = standingSign(TropicraftWoodTypes.THATCH, () -> TropicraftItems.THATCH_SIGN.get(), "thatch_end").register();
    public static final BlockEntry<StandingSignBlock> MANGROVE_SIGN = standingSign(TropicraftWoodTypes.MANGROVE, () -> TropicraftItems.MANGROVE_SIGN.get(), "mangrove_planks").register();

    public static final BlockEntry<WallSignBlock> MAHOGANY_WALL_SIGN = wallSign(TropicraftWoodTypes.MAHOGANY, () -> TropicraftItems.MAHOGANY_SIGN.get(), "mahogany_planks").register();
    public static final BlockEntry<WallSignBlock> PALM_WALL_SIGN = wallSign(TropicraftWoodTypes.PALM, () -> TropicraftItems.PALM_SIGN.get(), "palm_planks").register();
    public static final BlockEntry<WallSignBlock> BAMBOO_WALL_SIGN = wallSign(TropicraftWoodTypes.BAMBOO, () -> TropicraftItems.BAMBOO_SIGN.get(), "bamboo_end").register();
    public static final BlockEntry<WallSignBlock> THATCH_WALL_SIGN = wallSign(TropicraftWoodTypes.THATCH, () -> TropicraftItems.THATCH_SIGN.get(), "thatch_end").register();
    public static final BlockEntry<WallSignBlock> MANGROVE_WALL_SIGN = wallSign(TropicraftWoodTypes.MANGROVE, () -> TropicraftItems.MANGROVE_SIGN.get(), "mangrove_planks").register();

    public static final BlockEntry<ReedsBlock> REEDS = REGISTRATE.block("reeds", ReedsBlock::new)
            .initialProperties(() -> Blocks.SUGAR_CANE)
            .blockstate(() -> Models::generateReeds)
            .item()
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), prov.modBlockTexture(ctx.getName() + "_top_tall")))
            .build()
            .register();

    public static final BlockEntry<PapayaBlock> PAPAYA = REGISTRATE.block("papaya", PapayaBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(loot.applyExplosionDecay(block, LootItem.lootTableItem(TropicraftItems.PAPAYA).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                            .when(hasBlockStateProperties(block).setProperties(properties().hasProperty(PapayaBlock.AGE, 1)))))))))
            .blockstate(() -> Models::generatePapaya)
            .register();

    public static final BlockEntry<FenceBlock> BAMBOO_FENCE = woodenFence("bamboo_fence", BAMBOO_BUNDLE, item -> item
            .tag(ItemTags.WOODEN_FENCES)
            .model(() -> (ctx, prov) -> prov.generateWithTemplate(ctx.get(), ModelTemplates.FENCE_INVENTORY, TextureMapping.defaultTexture(prov.modBlockTexture("bamboo_side"))))
    )
            .blockstate(() -> (ctx, prov) -> prov.generateFenceBlock(ctx.get(), prov.modBlockTexture("bamboo_side")))
            .register();
    public static final BlockEntry<FenceBlock> THATCH_FENCE = woodenFence("thatch_fence", THATCH_BUNDLE, item -> item
            .tag(ItemTags.WOODEN_FENCES)
            .model(() -> (ctx, prov) -> prov.generateWithTemplate(ctx.get(), ModelTemplates.FENCE_INVENTORY, TextureMapping.defaultTexture(prov.modBlockTexture("thatch_side"))))
    )
            .blockstate(() -> (ctx, prov) -> prov.generateFenceBlock(ctx.get(), prov.modBlockTexture("thatch_side")))
            .register();
    public static final BlockEntry<FenceBlock> CHUNK_FENCE = woodenFence("chunk_fence", CHUNK, i -> i).register();
    public static final BlockEntry<FenceBlock> PALM_FENCE = woodenFence("palm_fence", PALM_PLANKS, i -> i).register();
    public static final BlockEntry<FenceBlock> MAHOGANY_FENCE = woodenFence("mahogany_fence", MAHOGANY_PLANKS, i -> i).register();

    public static final BlockEntry<FenceGateBlock> BAMBOO_FENCE_GATE = fenceGate("bamboo_fence_gate", BAMBOO_BUNDLE)
            .blockstate(() -> (ctx, prov) -> prov.generateFenceGateBlock(ctx.get(), prov.modBlockTexture("bamboo_side")))
            .register();
    public static final BlockEntry<FenceGateBlock> THATCH_FENCE_GATE = fenceGate("thatch_fence_gate", THATCH_BUNDLE)
            .blockstate(() -> (ctx, prov) -> prov.generateFenceGateBlock(ctx.get(), prov.modBlockTexture("thatch_side")))
            .register();
    public static final BlockEntry<FenceGateBlock> CHUNK_FENCE_GATE = fenceGate("chunk_fence_gate", CHUNK).register();
    public static final BlockEntry<FenceGateBlock> PALM_FENCE_GATE = fenceGate("palm_fence_gate", PALM_PLANKS).register();
    public static final BlockEntry<FenceGateBlock> MAHOGANY_FENCE_GATE = fenceGate("mahogany_fence_gate", MAHOGANY_PLANKS).register();

    public static final BlockEntry<WallBlock> CHUNK_WALL = REGISTRATE.block("chunk_wall", WallBlock::new)
            .initialProperties(CHUNK)
            .tag(BlockTags.WALLS)
            .blockstate(() -> (ctx, prov) -> prov.generateWallBlock(ctx.get(), prov.blockTexture(CHUNK.get())))
            .recipe((ctx, prov) -> prov.wall(DataIngredient.items(CHUNK.get()), RecipeCategory.DECORATIONS, ctx))
            .item()
            .tag(ItemTags.WALLS)
            .model(() -> (ctx, prov) -> prov.generateWithTemplate(ctx.get(), ModelTemplates.WALL_INVENTORY, new TextureMapping()
                    .put(TextureSlot.WALL, prov.modBlockTexture(CHUNK.getId().getPath()))))
            .build()
            .register();

    public static final BlockEntry<DoorBlock> BAMBOO_DOOR = woodenDoor("bamboo_door", BAMBOO_BUNDLE).register();
    public static final BlockEntry<DoorBlock> PALM_DOOR = woodenDoor("palm_door", PALM_PLANKS).register();
    public static final BlockEntry<DoorBlock> MAHOGANY_DOOR = woodenDoor("mahogany_door", MAHOGANY_PLANKS).register();
    public static final BlockEntry<DoorBlock> THATCH_DOOR = woodenDoor("thatch_door", THATCH_BUNDLE).register();

    public static final BlockEntry<TrapDoorBlock> BAMBOO_TRAPDOOR = trapdoor("bamboo_trapdoor", BAMBOO_BUNDLE).register();
    public static final BlockEntry<TrapDoorBlock> PALM_TRAPDOOR = trapdoor("palm_trapdoor", PALM_PLANKS).register();
    public static final BlockEntry<TrapDoorBlock> MAHOGANY_TRAPDOOR = trapdoor("mahogany_trapdoor", MAHOGANY_PLANKS).register();
    public static final BlockEntry<TrapDoorBlock> THATCH_TRAPDOOR = trapdoor("thatch_trapdoor", THATCH_BUNDLE).register();

    public static final BlockEntry<TallFlowerBlock> IRIS = REGISTRATE.block("iris", TallFlowerBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).noCollision().instabreak().sound(SoundType.GRASS).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, createSinglePropConditionTable(loot, block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
            .tag(BlockTags.FLOWERS)
            .blockstate(() -> Models::generateDoublePlant)
            .recipe((ctx, prov) -> prov.singleItemUnfinished(DataIngredient.items(ctx.get()), RecipeCategory.MISC, () -> Items.PURPLE_DYE, 1, 4).save(prov, Tropicraft.resourceKey(Registries.RECIPE, name(Items.PURPLE_DYE))))
            .item()
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), prov.modBlockTexture("iris_top")))
            .build()
            .register();

    public static final BlockEntry<PineappleBlock> PINEAPPLE = REGISTRATE.block("pineapple", PineappleBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).randomTicks().noCollision().instabreak().sound(SoundType.GRASS).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, droppingChunks(loot, block, TropicraftItems.PINEAPPLE_CUBES,
                    hasBlockStateProperties(block).setProperties(
                            properties().hasProperty(
                                    DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)))))
            .blockstate(() -> Models::generateDoublePlant)
            .item()
            .model(() -> (ctx, prov) ->
                    prov.generateFlatItem(ctx.get(), prov.modBlockTexture("pineapple_top"))
            )
            .build()
            .register();

    public static final BlockEntry<BongoDrumBlock> SMALL_BONGO_DRUM = bongoDrum("small_bongo_drum", BongoDrumBlock.Size.SMALL).register();
    public static final BlockEntry<BongoDrumBlock> MEDIUM_BONGO_DRUM = bongoDrum("medium_bongo_drum", BongoDrumBlock.Size.MEDIUM).register();
    public static final BlockEntry<BongoDrumBlock> LARGE_BONGO_DRUM = bongoDrum("large_bongo_drum", BongoDrumBlock.Size.LARGE).register();

    private static BlockBuilder<BongoDrumBlock, Registrate> bongoDrum(String name, BongoDrumBlock.Size size) {
        return REGISTRATE.block(name, p -> new BongoDrumBlock(size, p))
                .properties(p -> p.mapColor(MapColor.TERRACOTTA_WHITE).strength(2.0f).sound(SoundType.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS))
                .tag(TropicraftTags.Blocks.BONGOS)
                .blockstate(() -> (ctx, prov) ->
                        prov.createNonTemplateModelBlock(ctx.get())
                )
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get())
                        .pattern(StringUtils.repeat('T', size.recipeColumns))
                        .pattern(StringUtils.repeat('B', size.recipeColumns))
                        .pattern(StringUtils.repeat('B', size.recipeColumns))
                        .define('T', IGUANA_LEATHER.get())
                        .define('B', MAHOGANY_PLANKS.get())
                        .group("tropicraft:bongos")
                        .unlockedBy("has_" + prov.safeName(IGUANA_LEATHER.get()), prov.has(IGUANA_LEATHER.get()))
                        .save(prov))
                .simpleItem();
    }

    public static final BlockEntry<LadderBlock> BAMBOO_LADDER = REGISTRATE.block("bamboo_ladder", LadderBlock::new)
            .initialProperties(() -> Blocks.LADDER)
            .properties(p -> p.sound(SoundType.BAMBOO))
            .tag(BlockTags.CLIMBABLE, BlockTags.MINEABLE_WITH_AXE)
            .blockstate(() -> Models::generateLadder)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get(), 4)
                    .pattern("S S").pattern("BSB").pattern("S S")
                    .define('S', BAMBOO_STICK.get())
                    .define('B', Items.BAMBOO)
                    .unlockedBy("has_bamboo", prov.has(Items.BAMBOO))
                    .save(prov))
            .item()
            .model(() -> Models::generateFlatBlockItem)
            .build()
            .register();

    public static final BlockEntry<BoardwalkBlock> BAMBOO_BOARDWALK = boardwalk("bamboo_boardwalk", BAMBOO_SLAB, Either.right(Tropicraft.id("block/bamboo_side"))).register();
    public static final BlockEntry<BoardwalkBlock> PALM_BOARDWALK = boardwalk("palm_boardwalk", PALM_SLAB, Either.left(PALM_PLANKS)).register();
    public static final BlockEntry<BoardwalkBlock> MAHOGANY_BOARDWALK = boardwalk("mahogany_boardwalk", MAHOGANY_SLAB, Either.left(MAHOGANY_PLANKS)).register();
    public static final BlockEntry<BoardwalkBlock> MANGROVE_BOARDWALK = boardwalk("mangrove_boardwalk", MANGROVE_SLAB, Either.left(MANGROVE_PLANKS)).register();

    public static final BlockEntry<BambooChestBlock> BAMBOO_CHEST = REGISTRATE.block("bamboo_chest", BambooChestBlock::new)
            .initialProperties(BAMBOO_BUNDLE)
            .properties(p -> p.strength(1.0f))
            .blockstate(() -> (ctx, prov) -> prov.createAirLikeBlock(ctx.get(), prov.modBlockTexture("bamboo_side")))
            .blockEntity(BambooChestBlockEntity::new)
            .renderer(() -> BambooChestRenderer::new)
            .build()
            .item()
            .model(() -> (ctx, prov) ->
                    Models.generateChestItem(ctx, prov, Tropicraft.id("bamboo"), prov.modBlockTexture("bamboo_side"))
            )
            .build()
            .addMiscData(ProviderType.LANG, prov -> {
                prov.add(Tropicraft.ID + ".container.bambooChest", "Bamboo Chest");
                prov.add(Tropicraft.ID + ".container.bambooChestDouble", "Large Bamboo Chest");
            })
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get())
                    .pattern("BBB").pattern("B B").pattern("BBB")
                    .define('B', Items.BAMBOO)
                    .unlockedBy("has_bamboo", prov.has(Items.BAMBOO))
                    .save(prov))
            .register();

    public static final BlockEntityEntry<BambooChestBlockEntity> BAMBOO_CHEST_ENTITY = BlockEntityEntry.cast(BAMBOO_CHEST.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<SifterBlock> SIFTER = REGISTRATE.block("sifter", SifterBlock::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .properties(Properties::noOcclusion)
            .blockEntity(SifterBlockEntity::new)
            .renderer(() -> SifterBlockEntityRenderer::new)
            .build()
            .setData(ProviderType.LANG, (ctx, prov) -> prov.addBlockWithTooltip(ctx, "Place any type of tropics or regular sand in the sifter. What treasures are hidden inside?"))
            .recipe((ctx, prov) -> {
                ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get())
                        .pattern("XXX").pattern("XIX").pattern("XXX")
                        .define('X', ItemTags.PLANKS)
                        .define('I', Tags.Items.GLASS_BLOCKS)
                        .group("tropicraft:sifter")
                        .unlockedBy("has_glass", prov.has(Tags.Items.GLASS_BLOCKS))
                        .save(prov);
                ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get())
                        .pattern("XXX").pattern("XIX").pattern("XXX")
                        .define('X', ItemTags.PLANKS)
                        .define('I', Tags.Items.GLASS_PANES)
                        .group("tropicraft:sifter")
                        .unlockedBy("has_glass_pane", prov.has(Tags.Items.GLASS_PANES))
                        .save(prov, Tropicraft.resourceKey(Registries.RECIPE, "sifter_with_glass_pane"));
            })
            .simpleItem()
            .register();

    public static final BlockEntityEntry<SifterBlockEntity> SIFTER_ENTITY = BlockEntityEntry.cast(SIFTER.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<DrinkMixerBlock> DRINK_MIXER = REGISTRATE.block("drink_mixer", DrinkMixerBlock::new)
            .properties(p -> p.mapColor(MapColor.STONE).strength(2.0f, 30.0f).noOcclusion().instrument(NoteBlockInstrument.BASEDRUM))
            .blockstate(() -> (ctx, prov) -> prov.createAirLikeBlock(ctx.get(), prov.modBlockTexture("chunk")))
            .blockEntity(DrinkMixerBlockEntity::new)
            .renderer(() -> DrinkMixerBlockEntityRenderer::new)
            .build()
            .item()
            .properties(p -> p.component(TropicraftDataComponents.HAS_DESCRIPTION, Unit.INSTANCE))
            .model(() -> Models::generateDrinkMixerItem)
            .build()
            .setData(ProviderType.LANG, (ctx, prov) -> prov.addBlockWithTooltip(ctx, "Place two drink ingredients on the mixer, then place an empty mug on the base, then ???, then enjoy!"))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get())
                    .pattern("XXX").pattern("XYX").pattern("XXX")
                    .define('X', CHUNK.get())
                    .define('Y', BAMBOO_MUG.get())
                    .unlockedBy("has_bamboo_mug", prov.has(BAMBOO_MUG.get()))
                    .save(prov))
            .register();

    public static final BlockEntityEntry<DrinkMixerBlockEntity> DRINK_MIXER_ENTITY = BlockEntityEntry.cast(DRINK_MIXER.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<AirCompressorBlock> AIR_COMPRESSOR = REGISTRATE.block("air_compressor", AirCompressorBlock::new)
            .properties(p -> p.mapColor(MapColor.STONE).strength(2.0f, 30.0f).noOcclusion().instrument(NoteBlockInstrument.BASEDRUM))
            .blockstate(() -> (ctx, prov) -> prov.createAirLikeBlock(ctx.get(), prov.modBlockTexture("chunk")))
            .blockEntity(AirCompressorBlockEntity::new)
            .renderer(() -> AirCompressorBlockEntityRenderer::new)
            .build()
            .item()
            .properties(p -> p.component(TropicraftDataComponents.HAS_DESCRIPTION, Unit.INSTANCE))
            .model(() -> Models::generateAirCompressorItem)
            .build()
            .setData(ProviderType.LANG, (ctx, prov) -> prov.addBlockWithTooltip(ctx, "Place an empty scuba harness in the compressor to fill it with air!"))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get(), 1)
                    .pattern("XXX")
                    .pattern("XYX")
                    .pattern("XXX")
                    .define('X', CHUNK.get())
                    .define('Y', AZURITE.get())
                    .unlockedBy("has_" + prov.safeName(CHUNK.get()), prov.has(CHUNK.get()))
                    .unlockedBy("has_" + prov.safeName(AZURITE.get()), prov.has(AZURITE.get()))
                    .save(prov))
            .register();

    public static final BlockEntityEntry<AirCompressorBlockEntity> AIR_COMPRESSOR_ENTITY = BlockEntityEntry.cast(AIR_COMPRESSOR.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<VolcanoBlock> VOLCANO = REGISTRATE.block("volcano", VolcanoBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(Properties::noLootTable)
            .blockstate(() -> Models::generateVolcanoBlock)
            .simpleBlockEntity(VolcanoBlockEntity::new)
            .register();

    public static final BlockEntityEntry<VolcanoBlockEntity> VOLCANO_ENTITY = BlockEntityEntry.cast(VOLCANO.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<TikiTorchBlock> TIKI_TORCH = REGISTRATE.block("tiki_torch", TikiTorchBlock::new)
            .initialProperties(() -> Blocks.TORCH)
            .properties(p -> p.sound(SoundType.WOOD).lightLevel(state -> state.getValue(TikiTorchBlock.SECTION) == TorchSection.UPPER ? 15 : 0))
            .loot((loot, block) -> loot.add(block, createSinglePropConditionTable(loot, block, TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.UPPER)))
            .blockstate(() -> Models::generateTikiTorch)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get())
                    .pattern("Y").pattern("X").pattern("X")
                    .define('X', BAMBOO_STICK.get())
                    .define('Y', ItemTags.COALS)
                    .unlockedBy("has_bamboo_stick", prov.has(BAMBOO_STICK.get()))
                    .save(prov))
            .item().defaultModel().build()
            .register();

    public static final BlockEntry<FlowerPotBlock> BAMBOO_FLOWER_POT = REGISTRATE.block("bamboo_flower_pot", p -> new FlowerPotBlock(null, () -> Blocks.AIR, p))
            .properties(p -> p.strength(0.2f, 5.0f).sound(SoundType.BAMBOO).pushReaction(PushReaction.DESTROY))
            .blockstate(() -> (ctx, prov) -> Models.generateFlowerPot(ctx, prov, ctx, prov.modBlockTexture("bamboo_side")))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get())
                    .pattern("#D#").pattern(" # ")
                    .define('#', Items.BAMBOO)
                    .define('D', Items.DIRT)
                    .unlockedBy("has_bamboo", prov.has(Items.BAMBOO))
                    .save(prov))
            .item().defaultModel().build()
            .register();

    public static final BlockEntry<CoffeeBushBlock> COFFEE_BUSH = REGISTRATE.block("coffee_bush", CoffeeBushBlock::new)
            .properties(p -> p.mapColor(MapColor.GRASS).strength(0.15f).sound(SoundType.GRASS).noOcclusion().pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, coffee(loot, TropicraftBlocks.COFFEE_BUSH.get(), TropicraftItems.RAW_COFFEE_BEAN)))
            .blockstate(() -> Models::generateCoffeeBush)
            .register();

    public static final BlockEntry<GrowableSinglePlantBlock> GOLDEN_LEATHER_FERN = REGISTRATE.block("small_golden_leather_fern", p -> new GrowableSinglePlantBlock(p, () -> TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN))
            .initialProperties(() -> Blocks.FERN)
            .properties(p -> p.offsetType(BlockBehaviour.OffsetType.XZ))
            .blockstate(() -> (ctx, prov) -> prov.createCrossBlock(ctx.get(), PlantType.NOT_TINTED, TextureMapping.cross(prov.modBlockTexture("small_golden_leather_fern"))))
            .lang("Golden Leather Fern")
            .item()
            .model(() -> (ctx, prov) -> prov.generateFlatItem(ctx.get(), prov.modItemTexture("golden_leather_fern")))
            .build()
            .register();

    public static final BlockEntry<GrowableDoublePlantBlock> TALL_GOLDEN_LEATHER_FERN = REGISTRATE.block("tall_golden_leather_fern", p -> new GrowableDoublePlantBlock(p, () -> TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN).setPickItem(() -> TropicraftBlocks.GOLDEN_LEATHER_FERN))
            .initialProperties(() -> Blocks.LARGE_FERN)
            .loot((loot, block) -> loot.dropOther(block, GOLDEN_LEATHER_FERN.get()))
            .blockstate(() -> Models::generateDoublePlant)
            .register();

    public static final BlockEntry<HugePlantBlock> LARGE_GOLDEN_LEATHER_FERN = REGISTRATE.block("golden_leather_fern", p -> new HugePlantBlock(p).setPickItem(() -> TropicraftBlocks.GOLDEN_LEATHER_FERN))
            .properties(p -> p.mapColor(MapColor.PLANT).noOcclusion().noCollision().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.dropOther(block, GOLDEN_LEATHER_FERN.get()))
            .blockstate(() -> Models::generateLargeGoldenLeatherFern)
            .lang("Large Golden Leather Fern")
            .register();

    public static final BlockEntry<LilyPadBlock> FLOWERING_LILY_PAD = REGISTRATE.block("flowering_lily_pad", LilyPadBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).noOcclusion().instabreak().sound(SoundType.LILY_PAD).pushReaction(PushReaction.DESTROY))
            .blockstate(() -> Models::generateFloweringLilyPad)
            .tag(BlockTags.SWORD_EFFICIENT, BlockTags.INSIDE_STEP_SOUND_BLOCKS, BlockTags.FROG_PREFER_JUMP_TO)
            .color(NonNullSupplier.of(() -> () -> List.of(BlockTintSources.constant(0xff71c35c, 0xff208030))))
            .item(PlaceOnWaterBlockItem::new)
            .model(() -> Models::generateFlatBlockItem)
            .build()
            .register();

    public static final BlockEntry<DuckweedBlock> DUCKWEED = REGISTRATE.block("duckweed", DuckweedBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).noCollision().noOcclusion().instabreak().sound(SoundType.LILY_PAD).pushReaction(PushReaction.DESTROY))
            .blockstate(() -> Models::generateDuckweed)
            .tag(BlockTags.SWORD_EFFICIENT)
            .item(PlaceOnWaterBlockItem::new)
            .model(() -> Models::generateFlatBlockItem)
            .build()
            .register();

    // Short and tall seagrass
    public static final BlockEntry<CustomSeagrassBlock> EEL_GRASS = seagrass("eel_grass", "Enhalus acoroides", () -> TropicraftBlocks.TALL_EEL_GRASS).register();
    public static final BlockEntry<CustomTallSeagrassBlock> TALL_EEL_GRASS = tallSeagrass("tall_eel_grass", EEL_GRASS).register();
    public static final BlockEntry<CustomSeagrassBlock> FLOWERING_EEL_GRASS = seagrass("flowering_eel_grass", "Enhalus acoroides", () -> TropicraftBlocks.FLOWERING_TALL_EEL_GRASS).register();
    public static final BlockEntry<CustomTallSeagrassBlock> FLOWERING_TALL_EEL_GRASS = tallSeagrass("flowering_tall_eel_grass", FLOWERING_EEL_GRASS).register();
    public static final BlockEntry<Block> MATTED_EEL_GRASS = mattedSeagrassBlock("matted_eel_grass", "Enhalus acoroides").register();
    public static final BlockEntry<Block> EEL_GRASS_BLOCK = seagrassBlock("eel_grass", "Enhalus acoroides").register();

    public static final BlockEntry<CustomSeagrassBlock> FERN_SEAGRASS = seagrass("fern_seagrass", "Halophila spinulosa", () -> TropicraftBlocks.TALL_FERN_SEAGRASS).register();
    public static final BlockEntry<CustomTallSeagrassBlock> TALL_FERN_SEAGRASS = tallSeagrass("tall_fern_seagrass", FERN_SEAGRASS).register();
    public static final BlockEntry<Block> MATTED_FERN_SEAGRASS = mattedSeagrassBlock("matted_fern_seagrass", "Halophila spinulosa").register();
    public static final BlockEntry<Block> FERN_SEAGRASS_BLOCK = seagrassBlock("fern_seagrass", "Halophila spinulosa").register();

    public static final BlockEntry<CustomSeagrassBlock> SICKLE_SEAGRASS = seagrass("sickle_seagrass", "Thalassodendron ciliatum", () -> TropicraftBlocks.TALL_SICKLE_SEAGRASS).register();
    public static final BlockEntry<CustomTallSeagrassBlock> TALL_SICKLE_SEAGRASS = tallSeagrass("tall_sickle_seagrass", SICKLE_SEAGRASS).register();
    public static final BlockEntry<Block> MATTED_SICKLE_SEAGRASS = mattedSeagrassBlock("matted_sickle_seagrass", "Thalassodendron ciliatum").register();
    public static final BlockEntry<Block> SICKLE_SEAGRASS_BLOCK = seagrassBlock("sickle_seagrass", "Thalassodendron ciliatum").register();

    // Short only seagrass
    public static final BlockEntry<CustomSeagrassBlock> NOODLE_SEAGRASS = seagrass("noodle_seagrass", "Syringodium isoetifolium", null).register();
    public static final BlockEntry<Block> MATTED_NOODLE_SEAGRASS = mattedSeagrassBlock("matted_noodle_seagrass", "Syringodium isoetifolium").register();
    public static final BlockEntry<Block> NOODLE_SEAGRASS_BLOCK = seagrassBlock("noodle_seagrass", "Syringodium isoetifolium").register();

    private static BlockBuilder<CustomSeagrassBlock, Registrate> seagrass(String name, String scientificName, @Nullable Supplier<BlockEntry<? extends TallSeagrassBlock>> tall) {
        return REGISTRATE.block(name, p -> new CustomSeagrassBlock(p, () -> tall.get().get()))
                .initialProperties(() -> Blocks.SEAGRASS)
                .loot((loot, block) -> loot.add(block, onlyWithSilkTouchOrShears(loot, block)))
                .blockstate(() -> Models::generateSeagrass)
                .item()
                .properties(p -> p.component(TropicraftDataComponents.SCIENTIFIC_NAME, scientificName))
                .model(() -> Models::generateFlatBlockItem)
                .build();
    }

    private static BlockBuilder<CustomTallSeagrassBlock, Registrate> tallSeagrass(String name, BlockEntry<? extends SeagrassBlock> normal) {
        return REGISTRATE.block(name, p -> new CustomTallSeagrassBlock(p, normal::get))
                .initialProperties(() -> Blocks.SEAGRASS)
                .loot((loot, block) -> loot.add(block, onlyWithSilkTouchOrShears(loot, normal.get())))
                .blockstate(() -> Models::generateTallSeagrass);
    }

    private static BlockBuilder<Block, Registrate> seagrassBlock(String name, String scientificName) {
        return REGISTRATE.block(name + "_block", Block::new)
                .initialProperties(() -> Blocks.SAND)
                .blockstate(() -> (ctx, prov) ->
                        Models.generateSeagrassBlock(name, ctx, prov)
                )
                .item()
                .properties(p -> p.component(TropicraftDataComponents.SCIENTIFIC_NAME, scientificName))
                .build();
    }

    private static BlockBuilder<Block, Registrate> mattedSeagrassBlock(String name, String scientificName) {
        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.SAND)
                .blockstate(() -> Models::generateMattedSeagrassBlock)
                .item()
                .properties(p -> p.component(TropicraftDataComponents.SCIENTIFIC_NAME, scientificName))
                .build();
    }

    @SuppressWarnings("unchecked")
    private static final Set<BlockEntry<? extends Block>> POTTABLE_PLANTS = ImmutableSet.<BlockEntry<? extends Block>>builder()
            .add(PALM_SAPLING, MAHOGANY_SAPLING, GRAPEFRUIT_SAPLING, LEMON_SAPLING, LIME_SAPLING, ORANGE_SAPLING)
            .add(IRIS)
            .addAll(FLOWERS.values())
            .build();

    public static final List<BlockEntry<FlowerPotBlock>> BAMBOO_POTTED_TROPICS_PLANTS = POTTABLE_PLANTS.stream()
            .map(plant -> bambooPot("bamboo_potted_" + plant.getId().getPath(), plant))
            .toList();

    public static final List<BlockEntry<FlowerPotBlock>> VANILLA_POTTED_TROPICS_PLANTS = POTTABLE_PLANTS.stream()
            .map(plant -> vanillaPot("potted_" + plant.getId().getPath(), plant))
            .toList();

    public static final List<BlockEntry<FlowerPotBlock>> BAMBOO_POTTED_VANILLA_PLANTS = Stream.of(
                    Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING,
                    Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.FERN, Blocks.DANDELION, Blocks.POPPY,
                    Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP,
                    Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY,
                    Blocks.WITHER_ROSE, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.DEAD_BUSH, Blocks.CACTUS
            )
            .map(plant -> bambooPot("bamboo_potted_" + name(plant), () -> plant))
            .toList();

    private static BlockEntry<FlowerPotBlock> bambooPot(String name, Supplier<? extends Block> plant) {
        return REGISTRATE.block(name, p -> new FlowerPotBlock(BAMBOO_FLOWER_POT, plant, p))
                .properties(p -> p.strength(0.2f, 5.0f).sound(SoundType.BAMBOO).pushReaction(PushReaction.DESTROY))
                .loot((loot, block) -> loot.add(block, pottedPlantLoot(loot, block)))
                .tag(BlockTags.FLOWER_POTS)
                .blockstate(() -> (ctx, prov) -> Models.generateFlowerPot(ctx, prov, BAMBOO_FLOWER_POT, prov.modBlockTexture("bamboo_side")))
                .register();
    }

    private static BlockEntry<FlowerPotBlock> vanillaPot(String name, Supplier<? extends Block> plant) {
        return REGISTRATE.block(name, p -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, plant, p))
                .initialProperties(() -> Blocks.FLOWER_POT)
                .loot((loot, block) -> loot.add(block, pottedPlantLoot(loot, block)))
                .tag(BlockTags.FLOWER_POTS)
                .blockstate(() -> (ctx, prov) -> Models.generateFlowerPot(ctx, prov, () -> Blocks.FLOWER_POT, prov.mcBlockTexture("flower_pot")))
                .register();
    }

    private static LootTable.Builder pottedPlantLoot(RegistrateBlockLootTables loot, FlowerPotBlock fullPot) {
        return lootTable().withPool(loot.applyExplosionCondition(fullPot.getEmptyPot(), LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(fullPot.getEmptyPot()))))
                .withPool(loot.applyExplosionCondition(fullPot.getPotted(), LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(fullPot.getPotted()))));
    }

    public static final List<BlockEntry<FlowerPotBlock>> ALL_POTTED_PLANTS = ImmutableList.<BlockEntry<FlowerPotBlock>>builder()
            .addAll(BAMBOO_POTTED_TROPICS_PLANTS)
            .addAll(VANILLA_POTTED_TROPICS_PLANTS)
            .addAll(BAMBOO_POTTED_VANILLA_PLANTS)
            .build();

    public static final Map<JigarbovTorchType, BlockEntry<? extends RedstoneWallTorchBlock>> JIGARBOV_WALL_TORCHES = Arrays.stream(JigarbovTorchType.values()).collect(ImmutableMap.toImmutableMap(Function.identity(),
            type -> REGISTRATE
                    .block("jigarbov_" + type.getName() + "_wall_torch", p -> new RedstoneWallTorchBlock(p) {
                        @Override
                        public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
                            return new ItemStack(Items.REDSTONE_TORCH);
                        }
                    })
                    .initialProperties(() -> Blocks.REDSTONE_TORCH)
                    .setData(ProviderType.LANG, (ctx, prov) -> {
                    })
                    .blockstate(() -> (ctx, prov) ->
                            Models.generateJigarbovTorch(ctx, prov, type)
                    )
                    // TODO: Ideally we should set this on the block properties, but it tries to re-generate the minecraft loot table
                    .loot((loot, block) -> loot.dropOther(block, Items.REDSTONE_TORCH))
                    .register()
    ));

    private static BlockBuilder<StairBlock, Registrate> stoneStairs(String name, BlockEntry<? extends Block> block) {
        return stairs(name, block, BlockTags.STAIRS, ItemTags.STAIRS)
                .recipe((ctx, prov) -> prov.stairs(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    private static BlockBuilder<StairBlock, Registrate> woodenStairs(String name, BlockEntry<? extends Block> block) {
        return stairs(name, block, BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS)
                .recipe((ctx, prov) -> prov.stairs(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, "wooden_stairs", false))
                .tag(BlockTags.MINEABLE_WITH_AXE);
    }

    private static BlockBuilder<StairBlock, Registrate> stairs(String name, BlockEntry<? extends Block> block, TagKey<Block> blockTag, TagKey<Item> itemTag) {
        return REGISTRATE.block(name, p -> new StairBlock(block.get().defaultBlockState(), p))
                .initialProperties(block)
                .tag(blockTag)
                .blockstate(() -> (ctx, prov) -> prov.generateStairsBlock(ctx.get(), prov.blockTexture(block.get())))
                .item()
                .tag(itemTag)
                .build();
    }

    private static BlockBuilder<SlabBlock, Registrate> stoneSlab(String name, BlockEntry<? extends Block> block) {
        return slab(name, block, BlockTags.SLABS, ItemTags.SLABS)
                .recipe((ctx, prov) -> prov.slab(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, null, true))
                .tag(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    private static BlockBuilder<SlabBlock, Registrate> woodenSlab(String name, BlockEntry<? extends Block> block) {
        return slab(name, block, BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS)
                .recipe((ctx, prov) -> prov.slab(DataIngredient.items(block.get()), RecipeCategory.BUILDING_BLOCKS, ctx, "wooden_slab", false))
                .tag(BlockTags.MINEABLE_WITH_AXE);
    }

    private static BlockBuilder<SlabBlock, Registrate> slab(String name, BlockEntry<? extends Block> block, TagKey<Block> blockTag, TagKey<Item> itemTag) {
        return REGISTRATE.block(name, SlabBlock::new)
                .initialProperties(block)
                .loot((loot, slab) -> loot.add(slab, loot.createSlabItemTable(slab)))
                .tag(blockTag)
                .blockstate(() -> (ctx, prov) -> Models.generateSlabBlock(ctx, prov, block))
                .item()
                .tag(itemTag)
                .build();
    }

    private static BlockBuilder<ButtonBlock, Registrate> woodButton(String name, BlockEntry<? extends Block> block, String texture) {
        return REGISTRATE.block(name, p -> new ButtonBlock(BlockSetType.OAK, 30, p))
                .initialProperties(block)
                .properties(p -> p.noCollision().strength(0.5f).pushReaction(PushReaction.DESTROY))
                .tag(BlockTags.WOODEN_BUTTONS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) -> prov.generateButtonBlock(ctx.get(), prov.modBlockTexture(texture)))
                .recipe((ctx, prov) -> prov.buttonBuilder(ctx.get(), Ingredient.of(block.asItem()))
                        .unlockedBy("has_" + prov.safeName(block.get()), prov.has(block.get()))
                        .group("wooden_button")
                        .save(prov))
                .item()
                .model(() -> (ctx, prov) ->
                        prov.generateWithTemplate(ctx.get(), ModelTemplates.BUTTON_INVENTORY, TextureMapping.defaultTexture(prov.modBlockTexture(texture)))
                )
                .tag(ItemTags.WOODEN_BUTTONS)
                .build();
    }

    private static BlockBuilder<PressurePlateBlock, Registrate> pressurePlate(String name, BlockEntry<? extends Block> block, String texture) {
        return REGISTRATE.block(name, p -> new PressurePlateBlock(BlockSetType.OAK, p))
                .initialProperties(block)
                .properties(p -> p.forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollision().strength(0.5f).ignitedByLava().pushReaction(PushReaction.DESTROY))
                .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_AXE)
                .recipe((ctx, prov) -> prov.pressurePlateBuilder(RecipeCategory.REDSTONE, ctx.get(), Ingredient.of(block.asItem()))
                        .unlockedBy("has_" + prov.safeName(block.get()), prov.has(block.get()))
                        .group("wooden_pressure_plate")
                        .save(prov))
                .blockstate(() -> (ctx, prov) -> prov.generatePressurePlateBlock(ctx.get(), prov.modBlockTexture(texture)))
                .simpleItem();
    }

    private static BlockBuilder<StandingSignBlock, Registrate> standingSign(WoodType woodType, Supplier<? extends Item> item, String texture) {
        String woodName = Identifier.parse(woodType.name()).getPath();
        return REGISTRATE.block(woodName + "_sign", p -> new StandingSignBlock(woodType, p))
                .initialProperties(() -> Blocks.OAK_SIGN)
                .tag(BlockTags.STANDING_SIGNS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) ->
                        prov.createAirLikeBlock(ctx.get(), prov.modBlockTexture(texture))
                )
                .loot((loot, b) -> loot.dropOther(b, item.get()))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .onRegisterAfter(Registries.BLOCK_ENTITY_TYPE, b -> extendBlockEntity(BlockEntityType.SIGN, b));
    }

    private static BlockBuilder<WallSignBlock, Registrate> wallSign(WoodType woodType, Supplier<? extends Item> item, String texture) {
        String woodName = Identifier.parse(woodType.name()).getPath();
        return REGISTRATE.block(woodName + "_wall_sign", p -> new WallSignBlock(woodType, p))
                .initialProperties(() -> Blocks.OAK_SIGN)
                .tag(BlockTags.WALL_SIGNS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) ->
                        prov.createAirLikeBlock(ctx.get(), prov.modBlockTexture(texture))
                )
                .loot((loot, b) -> loot.dropOther(b, item.get()))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .onRegisterAfter(Registries.BLOCK_ENTITY_TYPE, b -> extendBlockEntity(BlockEntityType.SIGN, b));
    }

    @SafeVarargs
    private static BlockBuilder<SaplingBlock, Registrate> sapling(String name, TreeGrower tree, Supplier<? extends Block>... validPlantBlocks) {
        return REGISTRATE
                .block(name, p -> (SaplingBlock) new SaplingBlock(tree, p) {
                    @Override
                    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
                        if (super.canSurvive(state, level, pos)) {
                            return true;
                        }
                        BlockPos ground = pos.below();
                        return mayPlaceOn(level.getBlockState(ground), level, ground);
                    }

                    @Override
                    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
                        Block block = state.getBlock();
                        if (super.mayPlaceOn(state, level, pos)) {
                            return true;
                        }
                        return Arrays.stream(validPlantBlocks).map(Supplier::get).anyMatch(b -> b == block);
                    }
                })
                .initialProperties(() -> Blocks.OAK_SAPLING)
                .tag(BlockTags.SAPLINGS)
                .blockstate(() -> Models::generateSapling)
                .item()
                .model(() -> Models::generateFlatBlockItem)
                .tag(ItemTags.SAPLINGS)
                .build();
    }

    private static BlockBuilder<LeavesBlock, Registrate> leaves(String name, BlockEntry<SaplingBlock> sapling, float[] saplingRates, boolean normalDecay) {
        return REGISTRATE.block(name, properties -> normalDecay ? new UntintedParticleLeavesBlock(0.01f, TROPICS_LEAF_PARTICLE, properties) : new TropicraftLeavesBlock(0.01f, TROPICS_LEAF_PARTICLE, properties))
                .initialProperties(() -> Blocks.OAK_LEAVES)
                .loot((loot, block) -> loot.add(block, loot.createLeavesDrops(block, sapling.get(), saplingRates)))
                .tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
                .item()
                .tag(ItemTags.LEAVES)
                .build();
    }

    private static BlockBuilder<LeavesBlock, Registrate> leaves(String name, boolean normalDecay) {
        return REGISTRATE.block(name, properties -> normalDecay ? new UntintedParticleLeavesBlock(0.01f, TROPICS_LEAF_PARTICLE, properties) : new TropicraftLeavesBlock(0.01f, TROPICS_LEAF_PARTICLE, properties))
                .initialProperties(() -> Blocks.OAK_LEAVES)
                .loot((loot, block) -> loot.add(block, onlyWithSilkTouchOrShears(loot, block).withPool(lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(hasNoShearsOrSilkTouch(loot))
                        .add(loot.applyExplosionDecay(block, lootTableItem(Items.STICK)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(fortune(loot), 0.02f, 0.022222223f, 0.025f, 0.033333335f, 0.1f))))))
                .tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
                .item()
                .tag(ItemTags.LEAVES)
                .build();
    }

    private static BlockBuilder<UntintedParticleLeavesBlock, Registrate> fruitLeaves(String name, Supplier<SaplingBlock> sapling, Supplier<? extends Item> fruit) {
        return REGISTRATE.block(name, properties -> new UntintedParticleLeavesBlock(0.01f, TROPICS_LEAF_PARTICLE, properties))
                .initialProperties(() -> Blocks.OAK_LEAVES)
                .loot((loot, block) -> loot.add(block, loot.createLeavesDrops(block, sapling.get(), FRUIT_SAPLING_RATES)
                        .withPool(lootPool().setRolls(ConstantValue.exactly(1))
                                .when(hasNoShearsOrSilkTouch(loot))
                                .add(loot.applyExplosionDecay(block, lootTableItem(fruit.get()))))
                ))
                .tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
                .item()
                .tag(ItemTags.LEAVES)
                .build();
    }

    private static BlockBuilder<RotatedPillarBlock, Registrate> log(String name, MapColor topColor, MapColor sideColor) {
        return log(name, topColor, sideColor, null);
    }

    private static BlockBuilder<RotatedPillarBlock, Registrate> mangroveLog(String name, MapColor topColor, MapColor sideColor, @Nullable Supplier<? extends RotatedPillarBlock> strippedLog) {
        return REGISTRATE.block(name, p -> strippedLog != null ? new TropicraftLogBlock(p, strippedLog) : new RotatedPillarBlock(p))
                .initialProperties(() -> Blocks.OAK_LOG)
                .properties(p -> rotatedPillarProperties(p, topColor, sideColor))
                .tag(BlockTags.LOGS, BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) -> prov.generateLogBlock(ctx.get()))
                .item()
                .tag(ItemTags.LOGS, ItemTags.LOGS_THAT_BURN, TropicraftTags.Items.MANGROVE_LOGS)
                .build();
    }

    private static BlockBuilder<RotatedPillarBlock, Registrate> log(String name, MapColor topColor, MapColor sideColor, @Nullable Supplier<? extends RotatedPillarBlock> strippedLog) {
        return REGISTRATE.block(name, p -> strippedLog != null ? new TropicraftLogBlock(p, strippedLog) : new RotatedPillarBlock(p))
                .initialProperties(() -> Blocks.OAK_LOG)
                .properties(p -> rotatedPillarProperties(p, topColor, sideColor))
                .tag(BlockTags.LOGS, BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) -> prov.generateLogBlock(ctx.get()))
                .item()
                .tag(ItemTags.LOGS, ItemTags.LOGS_THAT_BURN)
                .build();
    }

    private static BlockBuilder<RotatedPillarBlock, Registrate> wood(String name, MapColor color, BlockEntry<? extends RotatedPillarBlock> bark) {
        return wood(name, color, bark, null);
    }

    private static BlockBuilder<RotatedPillarBlock, Registrate> wood(String name, MapColor color, BlockEntry<? extends RotatedPillarBlock> bark, @Nullable Supplier<? extends RotatedPillarBlock> stripped) {
        return REGISTRATE.block(name, p -> stripped != null ? new TropicraftLogBlock(p, stripped) : new RotatedPillarBlock(p))
                .properties(p -> p.sound(SoundType.WOOD).mapColor(color).strength(2.0f).ignitedByLava().instrument(NoteBlockInstrument.BASS))
                .tag(BlockTags.LOGS, BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) -> {
                    Material barkTexture = prov.blockTexture(bark.get());
                    prov.generateAxisBlock(ctx.get(), barkTexture, barkTexture);
                })
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.BUILDING_BLOCKS, ctx.get(), 3)
                        .pattern("##").pattern("##")
                        .define('#', bark.get())
                        .group("bark")
                        .unlockedBy("has_log", prov.has(Blocks.ACACIA_LOG)) // TODO: What? Why?
                        .save(prov))
                .item()
                .tag(ItemTags.LOGS, ItemTags.LOGS_THAT_BURN)
                .build();
    }

    private static BlockBuilder<MangroveRootsBlock, Registrate> mangroveRoots(String name) {
        return REGISTRATE.block(name, MangroveRootsBlock::new)
                .properties(p ->
                        p.mapColor(MapColor.WOOD)
                                .sound(SoundType.WOOD)
                                .ignitedByLava()
                                .instrument(NoteBlockInstrument.BASS)
                                .strength(2.0f)
                                .noOcclusion()
                                .isRedstoneConductor((state, world, pos) -> false)
                                .postProcess((state, level, pos) -> pos)
                )
                .tag(TropicraftTags.Blocks.ROOTS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) ->
                        Models.generateMangroveRoots(ctx, prov, name)
                )
                .item()
                .model(() -> (ctx, prov) -> prov.generateBlockItem(ctx.get(), "_stem"))
                .build();
    }

    private static BlockBuilder<MangroveLeavesBlock, Registrate> mangroveLeaves(String name, Supplier<PropaguleBlock> propagule) {
        return REGISTRATE.block(name, p -> new MangroveLeavesBlock(0.01f, TROPICS_LEAF_PARTICLE, propagule, p))
                .initialProperties(() -> Blocks.OAK_LEAVES)
                .properties(Properties::randomTicks)
                .loot((loot, block) -> loot.add(block, onlyWithSilkTouchOrShears(loot, block).withPool(lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(hasNoShearsOrSilkTouch(loot))
                        .add(loot.applyExplosionDecay(block, lootTableItem(Items.STICK)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 2.0f))))
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(fortune(loot), 0.02f, 0.022222223f, 0.025f, 0.033333335f, 0.1f))))))
                .tag(BlockTags.LEAVES)
                .item()
                .tag(ItemTags.LEAVES)
                .build();
    }

    private static BlockBuilder<PropaguleBlock, Registrate> propagule(String name, TreeGrower tree, String scientificName) {
        return REGISTRATE.block(name, p -> new PropaguleBlock(tree, p))
                .initialProperties(() -> Blocks.OAK_SAPLING)
                .tag(BlockTags.SAPLINGS)
                .blockstate(() -> Models::generatePropagule)
                .setData(ProviderType.LANG, (ctx, prov) -> prov.addBlockWithTooltip(ctx, scientificName))
                .item()
                .properties(p -> p.component(TropicraftDataComponents.HAS_DESCRIPTION, Unit.INSTANCE))
                .model(() -> Models::generateFlatBlockItem)
                .tag(ItemTags.SAPLINGS)
                .build();
    }

    private static BlockBuilder<Block, Registrate> planks(String name, MapColor color, Supplier<DataIngredient> log) {
        return REGISTRATE.block(name, Block::new)
                .initialProperties(() -> Blocks.OAK_PLANKS)
                .properties(p -> p.mapColor(color))
                .tag(BlockTags.PLANKS, BlockTags.MINEABLE_WITH_AXE)
                .recipe((ctx, prov) -> prov.planks(log.get(), RecipeCategory.BUILDING_BLOCKS, ctx))
                .item()
                .tag(ItemTags.PLANKS)
                .build();
    }

    private static BlockBuilder<FenceBlock, Registrate> woodenFence(String name, BlockEntry<? extends Block> block, UnaryOperator<ItemBuilder<BlockItem, BlockBuilder<FenceBlock, Registrate>>> itemFunction) {
        BlockBuilder<FenceBlock, Registrate> builder = REGISTRATE.block(name, FenceBlock::new)
                .initialProperties(block)
                .tag(BlockTags.WOODEN_FENCES, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) -> prov.generateFenceBlock(ctx.get(), prov.blockTexture(block.get())))
                .recipe((ctx, prov) -> prov.fence(DataIngredient.items(block.get()), RecipeCategory.DECORATIONS, ctx, "wooden_fence"));
        return itemFunction.apply(builder.item()
                .tag(ItemTags.WOODEN_FENCES)
                .model(() -> (ctx, prov) ->
                        prov.generateWithTemplate(ctx.get(), ModelTemplates.FENCE_INVENTORY, TextureMapping.defaultTexture(block.get()))
                )
        ).build();
    }

    private static BlockBuilder<FenceGateBlock, Registrate> fenceGate(String name, BlockEntry<? extends Block> block) {
        return REGISTRATE.block(name, p -> new FenceGateBlock(p, SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE))
                .initialProperties(block)
                .tag(BlockTags.FENCE_GATES, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) -> prov.generateFenceGateBlock(ctx.get(), prov.blockTexture(block.get())))
                .recipe((ctx, prov) -> prov.fenceGate(DataIngredient.items(block.get()), RecipeCategory.DECORATIONS, ctx, "wooden_fence_gate"))
                .simpleItem();
    }

    private static BlockBuilder<DoorBlock, Registrate> woodenDoor(String name, BlockEntry<? extends Block> material) {
        return REGISTRATE.block(name, p -> new DoorBlock(BlockSetType.OAK, p))
                .initialProperties(() -> Blocks.OAK_DOOR)
                .loot((loot, block) -> loot.add(block, createSinglePropConditionTable(loot, block, DoorBlock.HALF, DoubleBlockHalf.LOWER)))
                .tag(BlockTags.WOODEN_DOORS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) -> prov.generateDoorBlock(ctx.get(), prov.modBlockTexture(ctx.getName() + "_bottom"), prov.modBlockTexture(ctx.getName() + "_top")))
                .recipe((ctx, prov) -> prov.door(DataIngredient.items(material.get()), RecipeCategory.DECORATIONS, ctx, "wooden_door"))
                .item()
                .defaultModel()
                .tag(ItemTags.WOODEN_DOORS)
                .build();
    }

    private static BlockBuilder<TrapDoorBlock, Registrate> trapdoor(String name, BlockEntry<? extends Block> material) {
        return REGISTRATE.block(name, p -> new TrapDoorBlock(BlockSetType.OAK, p))
                .initialProperties(() -> Blocks.OAK_TRAPDOOR)
                .tag(BlockTags.WOODEN_TRAPDOORS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) -> prov.generateTrapdoorBlock(ctx.get(), prov.blockTexture(ctx.get()), true))
                .recipe((ctx, prov) -> prov.trapDoor(DataIngredient.items(material.get()), RecipeCategory.DECORATIONS, ctx, "wooden_trapdoor"))
                .item()
                .tag(ItemTags.WOODEN_TRAPDOORS)
                .model(() -> (ctx, prov) -> prov.generateBlockItem(ctx.get(), "_bottom"))
                .build();
    }

    private static BlockBuilder<BoardwalkBlock, Registrate> boardwalk(String name, BlockEntry<SlabBlock> slab, Either<Supplier<Block>, Identifier> texture) {
        return REGISTRATE.block(name, BoardwalkBlock::new)
                .initialProperties(slab)
                .properties(Properties::noOcclusion)
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .blockstate(() -> (ctx, prov) ->
                        Models.generateBoardwalk(ctx, prov, texture)
                )
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(prov.itemLookup(), RecipeCategory.DECORATIONS, ctx.get(), 3)
                        .pattern("XXX")
                        .pattern("S S")
                        .define('X', slab.get())
                        .define('S', Tags.Items.RODS_WOODEN)
                        .group("tropicraft:boardwalk")
                        .unlockedBy("has_" + prov.safeName(slab.get()), prov.has(slab.get()))
                        .save(prov))
                .item()
                .model(() -> (ctx, prov) -> prov.generateBlockItem(ctx.get(), "_short"))
                .build();
    }

    private static Properties rotatedPillarProperties(Properties properties, MapColor topColor, MapColor sideColor) {
        return properties.mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? topColor : sideColor);
    }

    protected static <T extends Comparable<T> & StringRepresentable> LootTable.Builder createSinglePropConditionTable(RegistrateBlockLootTables loot, Block block, Property<T> property, T value) {
        return LootTable.lootTable().withPool(loot.applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f)).add(LootItem.lootTableItem(block).when(hasBlockStateProperties(block).setProperties(properties().hasProperty(property, value))))));
    }

    private static LootPool.Builder droppingChunksPool(RegistrateBlockLootTables loot, Block block, Supplier<? extends ItemLike> chunk) {
        return LootPool.lootPool().add(LootItem.lootTableItem(chunk.get())
                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(loot.itemLookup(), ItemTags.SWORDS)))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0f, 4.0f)))
                .otherwise(loot.applyExplosionCondition(block, LootItem.lootTableItem(block))));
    }

    protected static LootTable.Builder droppingChunks(RegistrateBlockLootTables loot, Block block, Supplier<? extends ItemLike> chunk) {
        return LootTable.lootTable().withPool(droppingChunksPool(loot, block, chunk));
    }

    protected static LootTable.Builder droppingChunks(RegistrateBlockLootTables loot, Block block, Supplier<? extends ItemLike> chunk, LootItemCondition.Builder condition) {
        return LootTable.lootTable().withPool(droppingChunksPool(loot, block, chunk).when(condition));
    }

    private static LootTable.Builder dropNumberOfItems(RegistrateBlockLootTables loot, Block block, Supplier<? extends ItemLike> drop, int minDrops, int maxDrops) {
        return LootTable.lootTable().withPool(loot.applyExplosionCondition(block, LootPool.lootPool()
                .add(LootItem.lootTableItem(drop.get()))
                .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))));
    }

    private static LootTable.Builder onlyWithSilkTouchOrShears(RegistrateBlockLootTables loot, Block block) {
        return lootTable().withPool(lootPool()
                .when(hasShearsOrSilkTouch(loot))
                .setRolls(ConstantValue.exactly(1))
                .add(lootTableItem(block))
        );
    }

    private static LootTable.Builder coffee(RegistrateBlockLootTables loot, Block block, Supplier<? extends ItemLike> drop) {
        return LootTable.lootTable()
                .withPool(loot.applyExplosionCondition(block, LootPool.lootPool()
                        .add(LootItem.lootTableItem(drop.get()))
                        .apply(SetItemCountFunction.setCount(new ConstantValue(1)))))
                .withPool(loot.applyExplosionCondition(block, LootPool.lootPool()
                        .add(LootItem.lootTableItem(drop.get()))
                        .when(hasBlockStateProperties(block).setProperties(properties().hasProperty(CoffeeBushBlock.AGE, 6)))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))));
    }

    private static String name(ItemLike item) {
        return item.asItem().builtInRegistryHolder().key().identifier().getPath();
    }

    private static void extendBlockEntity(BlockEntityType<?> type, Block block) {
        ((BlockEntityTypeAccessor) type).tropicraft$setValidBlocks(ImmutableSet.<Block>builder()
                .addAll(((BlockEntityTypeAccessor) type).tropicraft$getValidBlocks())
                .add(block)
                .build()
        );
    }

    private static class Models {
        public static final TextureSlot WOOD_SLOT = TextureSlot.create("wood");
        public static final TextureSlot HORIZONTAL_SLOT = TextureSlot.create("horizontal");
        public static final TextureSlot VERTICAL_SLOT = TextureSlot.create("vertical");
        public static final TextureSlot BUSH_SLOT = TextureSlot.create("bush");
        public static final TextureSlot JIGARBOV_SLOT = TextureSlot.create("jigarbov");
        public static final TextureSlot PLANKS_SLOT = TextureSlot.create("planks");
        public static final TextureSlot VINE_SLOT = TextureSlot.create("vine");
        public static final TextureSlot PAPAYA_SLOT = TextureSlot.create("papaya");

        public static final ModelTemplate ITEM_FRAME_TEMPLATE = ModelTemplates.create("item_frame", WOOD_SLOT);
        public static final ModelTemplate ITEM_FRAME_MAP_TEMPLATE = ModelTemplates.create("item_frame_map", WOOD_SLOT);

        public static final ModelTemplate FUZZY_STAIRS_TEMPLATE = ModelTemplates.create(Tropicraft.id("stairs_fuzzy").toString(), TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.CROSS);
        public static final ModelTemplate FUZZY_STAIRS_OUTER_TEMPLATE = ModelTemplates.create(Tropicraft.id("stairs_fuzzy_outer").toString(), TextureSlot.SIDE, TextureSlot.BOTTOM, TextureSlot.TOP, TextureSlot.CROSS);

        public static final ModelTemplate FRUITING_BRANCH = ModelTemplates.create(Tropicraft.id("fruiting_branch").toString(), HORIZONTAL_SLOT, VERTICAL_SLOT);
        public static final ModelTemplate COFFEE_BUSH = ModelTemplates.create(Tropicraft.id("coffee_bush").toString(), BUSH_SLOT);
        public static final ModelTemplate HUGE_CROSS = ModelTemplates.create(Tropicraft.id("huge_cross").toString(), TextureSlot.CROSS, TextureSlot.PARTICLE);
        public static final ModelTemplate JIGARBOV_TORCH = ModelTemplates.create(Tropicraft.id("jigarbov_wall_torch").toString(), TextureSlot.TORCH, JIGARBOV_SLOT);
        public static final ModelTemplate JIGARBOV_TORCH_UNLIT = ModelTemplates.create(Tropicraft.id("jigarbov_wall_torch_unlit").toString(), TextureSlot.TORCH, JIGARBOV_SLOT);
        public static final ModelTemplate TALL_MACHINE = ModelTemplates.createItem(Tropicraft.id("tall_machine").toString(), TextureSlot.PARTICLE);
        public static final ModelTemplate TALL_TORCH_TEMPLATE = ModelTemplates.create(Tropicraft.id("template_tall_torch").toString(), TextureSlot.TORCH);

        public static final ModelTemplate LADDER_TEMPLATE = ModelTemplates.create("ladder", TextureSlot.TEXTURE, TextureSlot.PARTICLE);
        public static final ModelTemplate VINE_TEMPLATE = ModelTemplates.create("vine", VINE_SLOT, TextureSlot.PARTICLE);

        public static final ModelTemplate PAPAYA_TEMPLATE = ModelTemplates.create(Tropicraft.id("papaya").toString(), PAPAYA_SLOT);

        private static final List<VariantMutator> ALL_Y_ROTATIONS = List.of(NOP, Y_ROT_90, Y_ROT_180, Y_ROT_270);
        private static final List<VariantMutator> ALL_ROTATIONS = List.of(
                NOP,
                X_ROT_90,
                X_ROT_180,
                X_ROT_270,
                Y_ROT_90,
                Y_ROT_90.then(X_ROT_90),
                Y_ROT_90.then(X_ROT_180),
                Y_ROT_90.then(X_ROT_270),
                Y_ROT_180,
                Y_ROT_180.then(X_ROT_90),
                Y_ROT_180.then(X_ROT_180),
                Y_ROT_180.then(X_ROT_270),
                Y_ROT_270,
                Y_ROT_270.then(X_ROT_90),
                Y_ROT_270.then(X_ROT_180),
                Y_ROT_270.then(X_ROT_270)
        );

        public static void generateBlockStates(RegistrateBlockModelGenerator prov) {
            TextureMapping bambooItemFrameTextures = new TextureMapping()
                    .put(TextureSlot.PARTICLE, prov.modBlockTexture("bamboo_side"))
                    .put(WOOD_SLOT, prov.modBlockTexture("bamboo_side"));
            ITEM_FRAME_TEMPLATE.create(
                    prov.modLoc("block/bamboo_item_frame"),
                    bambooItemFrameTextures,
                    prov.modelOutput
            );
            ITEM_FRAME_MAP_TEMPLATE.create(
                    prov.modLoc("block/bamboo_item_frame_map"),
                    bambooItemFrameTextures,
                    prov.modelOutput
            );
        }

        public static Identifier fuzzyStairs(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockModelGenerator prov, String suffix, ModelTemplate template, String side, String end, String cross) {
            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.SIDE, prov.modBlockTexture(side))
                    .put(TextureSlot.BOTTOM, prov.modBlockTexture(end))
                    .put(TextureSlot.TOP, prov.modBlockTexture(end))
                    .put(TextureSlot.CROSS, prov.modBlockTexture(cross));
            return template.createWithSuffix(ctx.get(), suffix, textures, prov.modelOutput);
        }

        public static void generateDoublePlant(DataGenContext<Block, ? extends DoublePlantBlock> ctx, RegistrateBlockModelGenerator prov) {
            MultiVariant topVariant = plainVariant(prov.createSuffixedVariant(ctx.get(), "_top", ModelTemplates.CROSS, TextureMapping::cross));
            MultiVariant bottomVariant = plainVariant(prov.createSuffixedVariant(ctx.get(), "_bottom", ModelTemplates.CROSS, TextureMapping::cross));
            prov.createDoubleBlock(ctx.get(), topVariant, bottomVariant);
        }

        public static void generateFlatBlockItem(DataGenContext<Item, ? extends BlockItem> ctx, RegistrateItemModelGenerator prov) {
            prov.generateFlatBlockItem(ctx.get());
        }

        public static Identifier cubeTop(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockModelGenerator prov, String suffix) {
            return ModelTemplates.CUBE_TOP.createWithSuffix(
                    ctx.get(),
                    suffix,
                    new TextureMapping()
                            .put(TextureSlot.SIDE, TextureMapping.getBlockTexture(ctx.get()))
                            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(ctx.get(), suffix)),
                    prov.modelOutput
            );
        }

        public static List<Weighted<Variant>> allYRotations(Variant variant, int weight) {
            return ALL_Y_ROTATIONS.stream().map(rotation -> new Weighted<>(variant.with(rotation), weight)).toList();
        }

        public static List<Weighted<Variant>> allRotations(Variant variant, int weight) {
            return ALL_ROTATIONS.stream().map(rotation -> new Weighted<>(variant.with(rotation), weight)).toList();
        }

        public static void generateLadder(DataGenContext<Block, LadderBlock> ctx, RegistrateBlockModelGenerator prov) {
            Identifier model = LADDER_TEMPLATE.create(ctx.get(), TextureMapping.defaultTexture(ctx.get()), prov.modelOutput);
            prov.blockStateOutput.accept(
                    MultiVariantGenerator.dispatch(ctx.get(), plainVariant(model)).with(ROTATION_HORIZONTAL_FACING)
            );
        }

        private static void generateChestItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelGenerator prov, Identifier texture, Material particle) {
            Identifier baseModel = ModelTemplates.CHEST_INVENTORY.create(ctx.get(), TextureMapping.particle(particle), prov.modelOutput);
            prov.itemModelOutput.accept(ctx.get(), ItemModelUtils.specialModel(baseModel, new ChestSpecialRenderer.Unbaked(texture)));
        }

        public static void generateAllRotationVariantBlock(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockModelGenerator prov) {
            Variant variant = plainModel(TexturedModel.CUBE.create(ctx.get(), prov.modelOutput));
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get(), createAllRotationVariants(variant)));
        }

        public static MultiVariant createAllRotationVariants(Variant variant) {
            return variants(ALL_ROTATIONS.stream().map(variant::with).toArray(Variant[]::new));
        }

        private static void generateFlower(DataGenContext<Block, TropicsFlowerBlock> ctx, RegistrateBlockModelGenerator prov) {
            TextureMapping textures = TextureMapping.cross(prov.modBlockTexture("flower/" + ctx.getName()));
            prov.createCrossBlock(ctx.get(), PlantType.NOT_TINTED, textures);
        }

        private static void generatePurifiedSand(DataGenContext<Block, BlockTropicraftSand> ctx, RegistrateBlockModelGenerator prov) {
            Variant normal = plainModel(TexturedModel.CUBE.create(ctx.get(), prov.modelOutput));
            Variant calcified = plainModel(cubeTop(ctx, prov, "_calcified"));
            Variant dune1 = plainModel(cubeTop(ctx, prov, "_dune1"));
            Variant dune2 = plainModel(cubeTop(ctx, prov, "_dune2"));
            Variant starfish = plainModel(cubeTop(ctx, prov, "_starfish"));
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get()).with(createBooleanModelDispatch(
                    BlockTropicraftSand.UNDERWATER,
                    new MultiVariant(WeightedList.<Variant>builder()
                            .addAll(allRotations(normal, 50))
                            .addAll(allYRotations(dune1, 10))
                            .addAll(allYRotations(dune2, 10))
                            .addAll(allYRotations(starfish, 1))
                            .build()
                    ),
                    new MultiVariant(WeightedList.<Variant>builder()
                            .addAll(allRotations(normal, 50))
                            .addAll(allYRotations(calcified, 5))
                            .build()
                    )
            )));
        }

        private static void generateMud(DataGenContext<Block, MudBlock> ctx, RegistrateBlockModelGenerator prov) {
            Variant normal = plainModel(TexturedModel.CUBE.create(ctx.get(), prov.modelOutput));
            Variant withStones = plainModel(ModelTemplates.CUBE_ALL.createWithSuffix(ctx.get(), "_with_stones", TextureMapping.cube(TextureMapping.getBlockTexture(ctx.get(), "_with_stones")), prov.modelOutput));
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get(), new MultiVariant(WeightedList.<Variant>builder()
                    .addAll(allYRotations(normal, 5))
                    .addAll(allYRotations(withStones, 1))
                    .build()
            )));
        }

        private static void generateThatchRoof(DataGenContext<Block, StairBlock> ctx, RegistrateBlockModelGenerator prov) {
            Identifier straightModel = fuzzyStairs(ctx, prov, "", FUZZY_STAIRS_TEMPLATE, "thatch_side", "thatch_end", "thatch_grass");
            Identifier innerModel = prov.modLoc("block/thatch_stairs_inner");
            Identifier outerModel = fuzzyStairs(ctx, prov, "_outer", FUZZY_STAIRS_OUTER_TEMPLATE, "thatch_side", "thatch_end", "thatch_grass");
            prov.blockStateOutput.accept(createStairs(ctx.get(),
                    plainVariant(innerModel),
                    plainVariant(straightModel),
                    plainVariant(outerModel)
            ));
        }

        private static void generateThatchStairs(DataGenContext<Block, StairBlock> ctx, RegistrateBlockModelGenerator prov) {
            Material side = prov.modBlockTexture("thatch_side");
            Material end = prov.modBlockTexture("thatch_end");
            prov.generateStairsBlock(ctx.get(), side, end, end);
        }

        private static void generateBambooSlab(DataGenContext<Block, SlabBlock> ctx, RegistrateBlockModelGenerator prov) {
            Material side = prov.modBlockTexture("bamboo_side");
            Material end = prov.modBlockTexture("bamboo_end");
            prov.generateSlabBlock(ctx.get(), plainVariant(ModelLocationUtils.getModelLocation(BAMBOO_BUNDLE.get())), side, end, end);
        }

        private static void generateThatchSlab(DataGenContext<Block, SlabBlock> ctx, RegistrateBlockModelGenerator prov) {
            Material side = prov.modBlockTexture("thatch_side");
            Material end = prov.modBlockTexture("thatch_end");
            prov.generateSlabBlock(ctx.get(), plainVariant(ModelLocationUtils.getModelLocation(THATCH_BUNDLE.get())), side, end, end);
        }

        private static void generatePassionfruitVine(DataGenContext<Block, FruitingVineBlock> ctx, RegistrateBlockModelGenerator prov) {
            Map<Property<Boolean>, VariantMutator> multifaceProperties = selectMultifaceProperties(ctx.get().defaultBlockState(), MultifaceBlock::getFaceProperty);
            MultiPartGenerator builder = MultiPartGenerator.multiPart(ctx.get());
            for (int age : FruitingVineBlock.AGE.getPossibleValues()) {
                Material texture = prov.modBlockTexture(ctx.getName() + "_" + age);
                TextureMapping textures = new TextureMapping().put(VINE_SLOT, texture).put(TextureSlot.PARTICLE, texture);
                MultiVariant model = plainVariant(VINE_TEMPLATE.createWithSuffix(ctx.get(), "_" + age, textures, prov.modelOutput));
                for (Map.Entry<Property<Boolean>, VariantMutator> entry : multifaceProperties.entrySet()) {
                    Property<Boolean> faceProperty = entry.getKey();
                    VariantMutator faceMutator = entry.getValue();
                    ConditionBuilder condition = condition().term(FruitingVineBlock.AGE, age).term(faceProperty, true);
                    builder.with(condition, model.with(faceMutator));
                }
            }
            prov.blockStateOutput.accept(builder);
        }

        private static void generateJocoteBranch(DataGenContext<Block, FruitingBranchBlock> ctx, RegistrateBlockModelGenerator prov) {
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get())
                    .with(PropertyDispatch.initial(FruitingBranchBlock.AGE).generate(age ->
                            plainVariant(FRUITING_BRANCH.createWithSuffix(ctx.get(),
                                    "_age" + age,
                                    new TextureMapping()
                                            .put(HORIZONTAL_SLOT, prov.modBlockTexture("jocote_branch_horizontal_" + age))
                                            .put(VERTICAL_SLOT, prov.modBlockTexture("jocote_branch_vertical")),
                                    prov.modelOutput
                            ))
                    ))
                    .with(ROTATION_HORIZONTAL_FACING)
            );
        }

        private static void generateMahoganyNut(DataGenContext<Block, MahoganyNutBlock> ctx, RegistrateBlockModelGenerator prov) {
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get())
                    .with(PropertyDispatch.initial(BlockStateProperties.AGE_2)
                            .select(0, plainVariant(ModelLocationUtils.getModelLocation(ctx.get(), "_0")))
                            .select(1, plainVariant(ModelLocationUtils.getModelLocation(ctx.get(), "_1")))
                            .select(2, plainVariant(ModelLocationUtils.getModelLocation(ctx.get(), "_2")))
                    ));
        }

        private static void generateReeds(DataGenContext<Block, ReedsBlock> ctx, RegistrateBlockModelGenerator prov) {
            Function<String, Variant> models = Util.memoize(texture -> {
                Identifier location = prov.modLoc("block/" + texture);
                Material material = prov.modBlockTexture(texture);
                return plainModel(ModelTemplates.CROP.create(location, TextureMapping.singleSlot(TextureSlot.CROP, material), prov.modelOutput));
            });
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get()).with(PropertyDispatch.initial(ReedsBlock.TYPE)
                    .generate(type -> variants(Arrays.stream(type.getTextures()).map(models).toArray(Variant[]::new)))
            ));
        }

        private static void generatePapaya(DataGenContext<Block, PapayaBlock> ctx, RegistrateBlockModelGenerator prov) {
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get())
                    .with(PropertyDispatch.initial(BlockStateProperties.AGE_1)
                            .select(0, plainVariant(PAPAYA_TEMPLATE.createWithSuffix(ctx.get(), "_stage0",
                                    TextureMapping.singleSlot(PAPAYA_SLOT, prov.modBlockTexture("papaya_stage0")),
                                    prov.modelOutput
                            )))
                            .select(1, plainVariant(PAPAYA_TEMPLATE.createWithSuffix(ctx.get(), "_stage1",
                                    TextureMapping.singleSlot(PAPAYA_SLOT, prov.modBlockTexture("papaya_stage1")),
                                    prov.modelOutput
                            )))
                    )
                    .with(ROTATION_HORIZONTAL_FACING_ALT));
        }

        private static void generateVolcanoBlock(DataGenContext<Block, VolcanoBlock> ctx, RegistrateBlockModelGenerator prov) {
            MultiVariant variant = plainVariant(ModelLocationUtils.getModelLocation(Blocks.BEDROCK));
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get(), variant));
        }

        private static void generateTikiTorch(DataGenContext<Block, TikiTorchBlock> ctx, RegistrateBlockModelGenerator prov) {
            MultiVariant tikiLower = plainVariant(TALL_TORCH_TEMPLATE.createWithSuffix(ctx.get(), "_lower", TextureMapping.torch(prov.modBlockTexture("tiki_torch_lower")), prov.modelOutput));
            MultiVariant tikiUpper = plainVariant(ModelTemplates.TORCH.createWithSuffix(ctx.get(), "_upper", TextureMapping.torch(prov.modBlockTexture("tiki_torch_upper")), prov.modelOutput));
            Block block = ctx.get();
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(block).with(
                    PropertyDispatch.initial(TikiTorchBlock.SECTION)
                            .select(TorchSection.LOWER, tikiLower)
                            .select(TorchSection.MIDDLE, tikiLower)
                            .select(TorchSection.UPPER, tikiUpper)
            ));
        }

        private static void generateFlowerPot(DataGenContext<Block, ? extends FlowerPotBlock> ctx, RegistrateBlockModelGenerator prov, Supplier<? extends Block> empty, Material particle) {
            Block flower = ctx.get().getPotted();
            boolean isVanilla = flower.builtInRegistryHolder().key().identifier().getNamespace().equals("minecraft");
            String flowerName = name(flower);
            TextureSlot flowerpotSlot = TextureSlot.create("flowerpot");
            ModelTemplate template;
            if (flower == Blocks.AIR) {
                template = ModelTemplates.create("flower_pot", flowerpotSlot, TextureSlot.PARTICLE);
            } else if (!isVanilla) {
                template = ModelTemplates.create("flower_pot_cross", flowerpotSlot, TextureSlot.PLANT, TextureSlot.PARTICLE);
            } else {
                template = ModelTemplates.create("potted_" + flowerName, flowerpotSlot, TextureSlot.PARTICLE);
            }
            TextureMapping textures = new TextureMapping()
                    .put(flowerpotSlot, prov.blockTexture(empty.get()))
                    .put(TextureSlot.PARTICLE, prov.modBlockTexture("bamboo_side"));
            if (!isVanilla) {
                if (flower instanceof TropicsFlowerBlock) {
                    textures.put(TextureSlot.PLANT, prov.modBlockTexture("flower/" + flowerName));
                } else if (flower instanceof TallFlowerBlock) {
                    textures.put(TextureSlot.PLANT, prov.modBlockTexture(flowerName + "_top"));
                } else {
                    textures.put(TextureSlot.PLANT, prov.blockTexture(flower));
                }
            }
            prov.generateWithTemplate(ctx.get(), template, textures);
        }

        private static void generateCoffeeBush(DataGenContext<Block, CoffeeBushBlock> ctx, RegistrateBlockModelGenerator prov) {
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get()).with(PropertyDispatch.initial(CoffeeBushBlock.AGE).generate(age -> {
                TextureMapping textures = TextureMapping.singleSlot(BUSH_SLOT, prov.modBlockTexture(ctx.getName() + "_stage" + age));
                return plainVariant(COFFEE_BUSH.createWithSuffix(ctx.get(), "_stage_" + age, textures, prov.modelOutput));
            })));
        }

        private static void generateLargeGoldenLeatherFern(DataGenContext<Block, HugePlantBlock> ctx, RegistrateBlockModelGenerator prov) {
            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.CROSS, prov.modBlockTexture("large_golden_leather_fern"))
                    .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(GOLDEN_LEATHER_FERN.get()));
            MultiVariant model = plainVariant(HUGE_CROSS.create(ctx.get(), textures, prov.modelOutput));
            prov.blockStateOutput.accept(MultiPartGenerator.multiPart(ctx.get())
                    .with(condition().term(HugePlantBlock.TYPE, HugePlantBlock.Type.CENTER), model)
            );
        }

        private static void generateFloweringLilyPad(DataGenContext<Block, LilyPadBlock> ctx, RegistrateBlockModelGenerator prov) {
            ModelTemplate template = ModelTemplates.create(Tropicraft.id("water_lily_with_flower").toString(), TextureSlot.TEXTURE, TextureSlot.PLANT);
            TextureMapping textures = new TextureMapping()
                    .put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(Blocks.LILY_PAD))
                    .put(TextureSlot.PLANT, TextureMapping.getBlockTexture(ctx.get()));
            MultiVariant variant = createRotatedVariants(plainModel(template.create(ctx.get(), textures, prov.modelOutput)));
            prov.blockStateOutput.accept(createSimpleBlock(ctx.get(), variant));
        }

        private static void generateDuckweed(DataGenContext<Block, DuckweedBlock> ctx, RegistrateBlockModelGenerator prov) {
            Material texture = prov.modBlockTexture("duckweed");
            TextureMapping textures = new TextureMapping().put(TextureSlot.WOOL, texture).put(TextureSlot.PARTICLE, texture);
            prov.generateWithTemplate(ctx.get(), ModelTemplates.CARPET, textures);
        }

        private static void generateSeagrass(DataGenContext<Block, CustomSeagrassBlock> ctx, RegistrateBlockModelGenerator prov) {
            prov.createTrivialBlock(ctx.get(), TexturedModel.SEAGRASS);
        }

        private static void generateTallSeagrass(DataGenContext<Block, CustomTallSeagrassBlock> ctx, RegistrateBlockModelGenerator prov) {
            prov.createDoubleBlock(ctx.get(),
                    plainVariant(ModelTemplates.SEAGRASS.createWithSuffix(ctx.get(), "_top", TextureMapping.defaultTexture(TextureMapping.getBlockTexture(ctx.get(), "_top")), prov.modelOutput)),
                    plainVariant(ModelTemplates.SEAGRASS.createWithSuffix(ctx.get(), "_bottom", TextureMapping.defaultTexture(TextureMapping.getBlockTexture(ctx.get(), "_bottom")), prov.modelOutput))
            );
        }

        private static void generateSeagrassBlock(String name, DataGenContext<Block, Block> ctx, RegistrateBlockModelGenerator prov) {
            prov.generateWithTemplate(ctx.get(), ModelTemplates.CUBE_ALL, TextureMapping.cube(prov.modBlockTexture("matted_" + name + "_top")));
        }

        private static void generateMattedSeagrassBlock(DataGenContext<Block, Block> ctx, RegistrateBlockModelGenerator prov) {
            Material bottomTexture = TextureMapping.getBlockTexture(TropicraftBlocks.PURIFIED_SAND.get());
            prov.createTrivialBlock(ctx.get(), TexturedModel.CUBE_TOP_BOTTOM.updateTexture(textures -> textures.put(TextureSlot.BOTTOM, bottomTexture)));
        }

        private static void generateJigarbovTorch(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockModelGenerator prov, JigarbovTorchType type) {
            Material etchTexture = prov.modBlockTexture("jigarbov/" + type.getName());

            TextureMapping litTextures = new TextureMapping()
                    .put(TextureSlot.TORCH, prov.mcBlockTexture("redstone_torch"))
                    .put(JIGARBOV_SLOT, etchTexture);
            MultiVariant litVariant = plainVariant(JIGARBOV_TORCH.create(ctx.get(), litTextures, prov.modelOutput));

            TextureMapping unlitTextures = new TextureMapping()
                    .put(TextureSlot.TORCH, prov.mcBlockTexture("redstone_torch_off"))
                    .put(JIGARBOV_SLOT, etchTexture);
            MultiVariant unlitVariant = plainVariant(JIGARBOV_TORCH_UNLIT.createWithSuffix(ctx.get(), "_off", unlitTextures, prov.modelOutput));

            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get())
                    .with(createBooleanModelDispatch(BlockStateProperties.LIT, litVariant, unlitVariant))
                    .with(ROTATION_TORCH)
            );
        }

        private static void generateSlabBlock(DataGenContext<Block, SlabBlock> ctx, RegistrateBlockModelGenerator prov, BlockEntry<? extends Block> block) {
            prov.generateSlabBlock(ctx.get(), plainVariant(ModelLocationUtils.getModelLocation(block.get())), prov.blockTexture(block.get()));
        }

        private static void generateSapling(DataGenContext<Block, SaplingBlock> ctx, RegistrateBlockModelGenerator prov) {
            prov.createCrossBlock(ctx.get(), PlantType.NOT_TINTED);
        }

        private static void generateMangroveRoots(DataGenContext<Block, MangroveRootsBlock> ctx, RegistrateBlockModelGenerator prov, String name) {
            Material roots = prov.modBlockTexture(name);

            TextureSlot rootsSlot = TextureSlot.create("roots");
            TextureSlot appendagesSlot = TextureSlot.create("appendages");
            ModelTemplate stemTemplate = ModelTemplates.create(Tropicraft.id("mangrove_roots/stem").toString(), "_stem", rootsSlot);
            ModelTemplate stemShortTemplate = ModelTemplates.create(Tropicraft.id("mangrove_roots/stem_short").toString(), "_stem_short", rootsSlot);
            ModelTemplate connectionLowTemplate = ModelTemplates.create(Tropicraft.id("mangrove_roots/connection_low").toString(), "_connection_low", rootsSlot);
            ModelTemplate connectionHighTemplate = ModelTemplates.create(Tropicraft.id("mangrove_roots/connection_high").toString(), "_connection_high", rootsSlot);

            ModelTemplate appendagesTemplate = ModelTemplates.create(Tropicraft.id("mangrove_roots/appendages").toString(), appendagesSlot);

            TextureMapping rootsTextures = TextureMapping.singleSlot(rootsSlot, roots);

            MultiVariant stem = plainVariant(stemTemplate.create(ctx.get(), rootsTextures, prov.modelOutput));
            MultiVariant stemShort = plainVariant(stemShortTemplate.create(ctx.get(), rootsTextures, prov.modelOutput));
            MultiVariant connectionLow = plainVariant(connectionLowTemplate.create(ctx.get(), rootsTextures, prov.modelOutput));
            MultiVariant connectionHigh = plainVariant(connectionHighTemplate.create(ctx.get(), rootsTextures, prov.modelOutput));

            MultiVariant appendagesHigh = plainVariant(appendagesTemplate.createWithSuffix(ctx.get(), "_appendages_high", TextureMapping.singleSlot(appendagesSlot, prov.modBlockTexture(name + "_appendages_high")), prov.modelOutput));
            MultiVariant appendagesHighShort = plainVariant(appendagesTemplate.createWithSuffix(ctx.get(), "_appendages_high_short", TextureMapping.singleSlot(appendagesSlot, prov.modBlockTexture(name + "_appendages_high_short")), prov.modelOutput));
            MultiVariant appendagesGrounded = plainVariant(appendagesTemplate.createWithSuffix(ctx.get(), "_appendages_ground", TextureMapping.singleSlot(appendagesSlot, prov.modBlockTexture(name + "_appendages_ground")), prov.modelOutput));
            MultiVariant appendagesGroundedShort = plainVariant(appendagesTemplate.createWithSuffix(ctx.get(), "_appendages_ground_short", TextureMapping.singleSlot(appendagesSlot, prov.modBlockTexture(name + "_appendages_ground_short")), prov.modelOutput));

            MultiPartGenerator builder = MultiPartGenerator.multiPart(ctx.get());

            builder.with(condition().term(MangroveRootsBlock.TALL, true), stem);
            builder.with(condition().term(MangroveRootsBlock.TALL, false), stemShort);

            builder.with(
                    condition().term(MangroveRootsBlock.TALL, true).term(MangroveRootsBlock.GROUNDED, true),
                    appendagesGrounded
            );
            builder.with(
                    condition().term(MangroveRootsBlock.TALL, false).term(MangroveRootsBlock.GROUNDED, true),
                    appendagesGroundedShort
            );
            builder.with(
                    condition().term(MangroveRootsBlock.TALL, true).term(MangroveRootsBlock.GROUNDED, false),
                    appendagesHigh
            );
            builder.with(
                    condition().term(MangroveRootsBlock.TALL, false).term(MangroveRootsBlock.GROUNDED, false),
                    appendagesHighShort
            );

            for (int i = 0; i < 4; i++) {
                EnumProperty<MangroveRootsBlock.Connection> connection = MangroveRootsBlock.CONNECTIONS[i];
                VariantMutator rotation = switch (i) {
                    case 0 -> Y_ROT_270;
                    case 1 -> NOP;
                    case 2 -> Y_ROT_90;
                    default -> Y_ROT_180;
                };

                builder.with(condition().term(connection, MangroveRootsBlock.Connection.HIGH), connectionHigh.with(rotation.then(UV_LOCK)));
                builder.with(condition().term(connection, MangroveRootsBlock.Connection.LOW), connectionLow.with(rotation.then(UV_LOCK)));
            }

            prov.blockStateOutput.accept(builder);
        }

        private static void generatePropagule(DataGenContext<Block, PropaguleBlock> ctx, RegistrateBlockModelGenerator prov) {
            prov.blockStateOutput.accept(MultiVariantGenerator.dispatch(ctx.get()).with(createBooleanModelDispatch(PropaguleBlock.PLANTED,
                    plainVariant(ModelTemplates.CROSS.createWithSuffix(ctx.get(), "_planted", TextureMapping.cross(prov.modBlockTexture(ctx.getName() + "_planted")), prov.modelOutput)),
                    plainVariant(ModelTemplates.CROSS.createWithSuffix(ctx.get(), "_hanging", TextureMapping.cross(ctx.get()), prov.modelOutput))
            )));
        }

        private static void generateBoardwalk(DataGenContext<Block, BoardwalkBlock> ctx, RegistrateBlockModelGenerator prov, Either<Supplier<Block>, Identifier> texture) {
            TextureMapping textures = TextureMapping.singleSlot(PLANKS_SLOT, texture.map(b -> TextureMapping.getBlockTexture(b.get()), Material::new));

            ModelTemplate shortTemplate = ModelTemplates.create(Tropicraft.id("boardwalk/short").toString(), "_short", PLANKS_SLOT);
            ModelTemplate shortPostTemplate = ModelTemplates.create(Tropicraft.id("boardwalk/short_post").toString(), "_short_post", PLANKS_SLOT);
            ModelTemplate tallTemplate = ModelTemplates.create(Tropicraft.id("boardwalk/tall").toString(), "_tall", PLANKS_SLOT);
            ModelTemplate tallPostTemplate = ModelTemplates.create(Tropicraft.id("boardwalk/tall_post").toString(), "_tall_post", PLANKS_SLOT);
            ModelTemplate tallConnectionTemplate = ModelTemplates.create(Tropicraft.id("boardwalk/tall_connection").toString(), "_tall_connection", PLANKS_SLOT);

            MultiVariant shortModel = plainVariant(shortTemplate.create(ctx.get(), textures, prov.modelOutput));
            MultiVariant shortPostModel = plainVariant(shortPostTemplate.create(ctx.get(), textures, prov.modelOutput));
            MultiVariant tallModel = plainVariant(tallTemplate.create(ctx.get(), textures, prov.modelOutput));
            MultiVariant tallPostModel = plainVariant(tallPostTemplate.create(ctx.get(), textures, prov.modelOutput));
            MultiVariant tallConnectionModel = plainVariant(tallConnectionTemplate.create(ctx.get(), textures, prov.modelOutput));

            MultiPartGenerator builder = MultiPartGenerator.multiPart(ctx.get());

            Direction.Axis[] horizontals = new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z};
            for (Direction.Axis axis : horizontals) {
                VariantMutator rotation = axis == Direction.Axis.X ? Y_ROT_270 : NOP;
                VariantMutator reverseRotation = axis == Direction.Axis.X ? Y_ROT_90 : Y_ROT_180;

                builder.with(
                        condition().term(BoardwalkBlock.TYPE, BoardwalkBlock.Type.SHORT, BoardwalkBlock.Type.SHORT_POST)
                                .term(BoardwalkBlock.AXIS, axis),
                        shortModel.with(rotation).with(UV_LOCK)
                );

                builder.with(
                        condition().term(BoardwalkBlock.TYPE, BoardwalkBlock.Type.TALL, BoardwalkBlock.Type.TALL_POST, BoardwalkBlock.Type.TALL_POST_FRONT, BoardwalkBlock.Type.TALL_POST_BACK, BoardwalkBlock.Type.TALL_POST_FRONT_BACK)
                                .term(BoardwalkBlock.AXIS, axis),
                        tallModel.with(rotation).with(UV_LOCK)
                );

                builder.with(
                        condition().term(BoardwalkBlock.TYPE, BoardwalkBlock.Type.TALL_POST_BACK, BoardwalkBlock.Type.TALL_POST_FRONT_BACK)
                                .term(BoardwalkBlock.AXIS, axis),
                        tallConnectionModel.with(rotation).with(UV_LOCK)
                );

                builder.with(
                        condition().term(BoardwalkBlock.TYPE, BoardwalkBlock.Type.TALL_POST_FRONT, BoardwalkBlock.Type.TALL_POST_FRONT_BACK)
                                .term(BoardwalkBlock.AXIS, axis),
                        tallConnectionModel.with(reverseRotation).with(UV_LOCK)
                );
            }

            builder.with(
                    condition().term(BoardwalkBlock.TYPE, BoardwalkBlock.Type.SHORT_POST),
                    shortPostModel
            );
            builder.with(
                    condition().term(BoardwalkBlock.TYPE, BoardwalkBlock.Type.TALL_POST, BoardwalkBlock.Type.TALL_POST_FRONT, BoardwalkBlock.Type.TALL_POST_BACK, BoardwalkBlock.Type.TALL_POST_FRONT_BACK),
                    tallPostModel
            );

            prov.blockStateOutput.accept(builder);
        }

        private static void generateDrinkMixerItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelGenerator prov) {
            Identifier baseModel = TALL_MACHINE.create(ctx.get(), TextureMapping.particle(CHUNK.get()), prov.modelOutput);
            prov.itemModelOutput.accept(ctx.get(), ItemModelUtils.specialModel(baseModel, new DrinkMixerSpecialRenderer.Unbaked()));
        }

        private static void generateAirCompressorItem(DataGenContext<Item, BlockItem> ctx, RegistrateItemModelGenerator prov) {
            Identifier baseModel = TALL_MACHINE.create(ctx.get(), TextureMapping.particle(CHUNK.get()), prov.modelOutput);
            prov.itemModelOutput.accept(ctx.get(), ItemModelUtils.specialModel(baseModel, new AirCompressorSpecialRenderer.Unbaked()));
        }
    }
}

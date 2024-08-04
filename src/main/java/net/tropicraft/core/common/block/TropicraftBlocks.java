package net.tropicraft.core.common.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Either;
import com.tterrag.registrate.Registrate;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.providers.DataGenContext;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateBlockstateProvider;
import com.tterrag.registrate.providers.RegistrateItemModelProvider;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.providers.loot.RegistrateBlockLootTables;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.tterrag.registrate.util.nullness.NonNullBiConsumer;
import com.tterrag.registrate.util.nullness.NonNullBiFunction;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemEnchantmentsPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.ItemSubPredicates;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RedstoneTorchBlock;
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
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.ModelProvider;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.Tags;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftItemRenderers;
import net.tropicraft.core.client.tileentity.AirCompressorRenderer;
import net.tropicraft.core.client.tileentity.BambooChestRenderer;
import net.tropicraft.core.client.tileentity.DrinkMixerRenderer;
import net.tropicraft.core.client.tileentity.SifterRenderer;
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
import net.tropicraft.core.mixin.BlockEntityTypeAccessor;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.tterrag.registrate.providers.RegistrateRecipeProvider.has;
import static net.minecraft.advancements.critereon.StatePropertiesPredicate.Builder.properties;
import static net.minecraft.world.level.storage.loot.LootPool.lootPool;
import static net.minecraft.world.level.storage.loot.LootTable.lootTable;
import static net.minecraft.world.level.storage.loot.entries.LootItem.lootTableItem;
import static net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition.hasBlockStateProperties;
import static net.neoforged.neoforge.client.model.generators.ConfiguredModel.allRotations;
import static net.neoforged.neoforge.client.model.generators.ConfiguredModel.allYRotations;
import static net.tropicraft.core.common.item.TropicraftItems.*;

public class TropicraftBlocks {
    private static final Registrate REGISTRATE = Tropicraft.registrate();

    private static final LootItemCondition.Builder HAS_SHEARS = MatchTool.toolMatches(ItemPredicate.Builder.item().of(Items.SHEARS));

    private static LootItemCondition.Builder hasSilkTouch(RegistrateBlockLootTables loot) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = loot.getRegistries().lookupOrThrow(Registries.ENCHANTMENT);
        return MatchTool.toolMatches(
                ItemPredicate.Builder.item()
                        .withSubPredicate(
                                ItemSubPredicates.ENCHANTMENTS,
                                ItemEnchantmentsPredicate.enchantments(
                                        List.of(new EnchantmentPredicate(enchantments.getOrThrow(Enchantments.SILK_TOUCH), MinMaxBounds.Ints.atLeast(1)))
                                )
                        )
        );
    }

    private static LootItemCondition.Builder hasNoSilkTouch(RegistrateBlockLootTables loot) {
        return hasSilkTouch(loot).invert();
    }

    private static LootItemCondition.Builder hasShearsOrSilkTouch(RegistrateBlockLootTables loot) {
        return HAS_SHEARS.or(hasSilkTouch(loot));
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

    static {
        REGISTRATE.addDataGenerator(ProviderType.BLOCKSTATE, prov -> {
            prov.models().withExistingParent("bamboo_item_frame", "item_frame")
                    .texture("particle", prov.modLoc("block/bamboo_side"))
                    .texture("wood", prov.modLoc("block/bamboo_side"));

            prov.models().withExistingParent("bamboo_item_frame_map", "item_frame_map")
                    .texture("particle", prov.modLoc("block/bamboo_side"))
                    .texture("wood", prov.modLoc("block/bamboo_side"));
        });

        REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, prov -> {
            prov.addTag(TropicraftTags.Blocks.CARVER_REPLACEABLES).addTags(BlockTags.OVERWORLD_CARVER_REPLACEABLES);
        });
    }

    public static final BlockEntry<PortalWaterBlock> TELEPORT_WATER = REGISTRATE.block("teleport_water", PortalWaterBlock::new)
            .initialProperties(() -> Blocks.WATER)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().getExistingFile(prov.mcLoc("block/water"))))
            .register();

    public static final BlockEntry<LiquidBlock> PORTAL_WATER = REGISTRATE.block("portal_water", p -> new LiquidBlock(Fluids.WATER, p))
            .initialProperties(() -> Blocks.WATER)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().getExistingFile(prov.mcLoc("block/water"))))
            .register();

    public static final BlockEntry<Block> CHUNK = REGISTRATE.block("chunk", Block::new)
            .initialProperties(() -> Blocks.STONE)
            .properties(p -> p.mapColor(MapColor.COLOR_BLACK).strength(6.0f).explosionResistance(30.0f))
            .blockstate(TropicraftBlocks::simpleBlockAllRotations)
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
                        .block(flower.getId(), p -> new TropicsFlowerBlock(flower.getEffect(), flower.getEffectDuration(), flower.getShape(), p))
                        .initialProperties(() -> Blocks.POPPY)
                        .addLayer(() -> RenderType::cutout)
                        .tag(flower.getTags())
                        .blockstate((ctx, prov) -> {
                            prov.simpleBlock(ctx.get(), prov.models().withExistingParent(ctx.getName(), "block/cross")
                                    .texture("cross", "tropicraft:block/flower/" + ctx.getName()));
                        })
                        .item()
                        .tag(ItemTags.FLOWERS)
                        .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("block/flower/" + ctx.getName())))
                        .build();
                Item dye = flower.getDye();
                if (dye != null) {
                    builder = builder.recipe((ctx, prov) -> {
                        prov.singleItemUnfinished(DataIngredient.items(ctx.get()), RecipeCategory.MISC, () -> dye, 1, 2).save(prov, Tropicraft.location(name(dye)));
                    });
                }
                return builder.register();
            }));

    public static final BlockEntry<BlockTropicraftSand> PURIFIED_SAND = REGISTRATE.block("purified_sand", BlockTropicraftSand::new)
            .initialProperties(() -> Blocks.SAND)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .blockstate((ctx, prov) -> {
                ModelFile normal = prov.cubeAll(ctx.get());
                ModelFile calcified = cubeTop(ctx, prov, "calcified");
                ModelFile dune1 = cubeTop(ctx, prov, "dune1");
                ModelFile dune2 = cubeTop(ctx, prov, "dune2");
                ModelFile starfish = cubeTop(ctx, prov, "starfish");
                prov.getVariantBuilder(ctx.get())
                        .partialState().with(BlockTropicraftSand.UNDERWATER, false)
                        .addModels(allRotations(normal, false, 50))
                        .addModels(allYRotations(calcified, 0, false, 5))
                        .partialState().with(BlockTropicraftSand.UNDERWATER, true)
                        .addModels(allRotations(normal, false, 50))
                        .addModels(allYRotations(dune1, 0, false, 10))
                        .addModels(allYRotations(dune2, 0, false, 10))
                        .addModels(allYRotations(starfish, 0, false));
            })
            .item()
            .tag(ItemTags.SAND)
            .build()
            .recipe((ctx, prov) -> {
                prov.smelting(DataIngredient.items(ctx), RecipeCategory.MISC, () -> Blocks.GLASS, 0.3f);
                prov.singleItem(DataIngredient.items(ctx), RecipeCategory.BUILDING_BLOCKS, () -> Blocks.SAND, 1, 1);
            })
            .register();

    public static final BlockEntry<Block> PACKED_PURIFIED_SAND = REGISTRATE.block("packed_purified_sand", Block::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.mapColor(MapColor.STONE).strength(0.8f).requiresCorrectToolForDrops())
            .blockstate(TropicraftBlocks::simpleBlockAllRotations)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get())
                    .pattern("XX").pattern("XX")
                    .define('X', PURIFIED_SAND.get())
                    .unlockedBy("has_purified_sand", has(PURIFIED_SAND.get()))
                    .save(prov))
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<BlockTropicraftSand> CORAL_SAND = REGISTRATE.block("coral_sand", BlockTropicraftSand::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.mapColor(MapColor.COLOR_PINK))
            .blockstate(TropicraftBlocks::simpleBlockAllRotations)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<BlockTropicraftSand> FOAMY_SAND = REGISTRATE.block("foamy_sand", BlockTropicraftSand::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.mapColor(MapColor.COLOR_GREEN))
            .blockstate(TropicraftBlocks::simpleBlockAllRotations)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<VolcanicSandBlock> VOLCANIC_SAND = REGISTRATE.block("volcanic_sand", VolcanicSandBlock::new)
            .initialProperties(() -> Blocks.SAND)
            .properties(p -> p.mapColor(MapColor.COLOR_LIGHT_GRAY))
            .blockstate(TropicraftBlocks::simpleBlockAllRotations)
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<BlockTropicraftSand> MINERAL_SAND = REGISTRATE.block("mineral_sand", BlockTropicraftSand::new)
            .initialProperties(() -> Blocks.SAND)
            .blockstate((ctx, prov) -> prov.simpleBlock(
                    ctx.get(), ConfiguredModel.allRotations(prov.cubeAll(ctx.get()), false)
            ))
            .tag(BlockTags.SAND, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES)
            .item()
            .tag(ItemTags.SAND)
            .build()
            .register();

    public static final BlockEntry<MudBlock> MUD = REGISTRATE.block("mud", MudBlock::new)
            .initialProperties(() -> Blocks.DIRT)
            .properties(p -> p.speedFactor(0.5f).isValidSpawn((s, w, pa, e) -> true).isRedstoneConductor((s, w, pa) -> true).isViewBlocking((s, w, pa) -> true).isSuffocating((s, w, pa) -> true))
            .tag(TropicraftTags.Blocks.MUD, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES, BlockTags.BAMBOO_PLANTABLE_ON, BlockTags.DIRT, BlockTags.SNOW_LAYER_CAN_SURVIVE_ON, BlockTags.SNIFFER_DIGGABLE_BLOCK)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(),
                    ArrayUtils.addAll(
                            allYRotations(prov.models().cubeAll("mud", prov.modLoc("block/mud")), 0, false, 5),
                            allYRotations(prov.models().cubeAll("mud_with_stones", prov.modLoc("block/mud_with_stones")), 0, false, 1)
                    )
            ))
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
            .tag(TropicraftTags.Blocks.MUD, BlockTags.MINEABLE_WITH_SHOVEL, TropicraftTags.Blocks.CARVER_REPLACEABLES, BlockTags.BAMBOO_PLANTABLE_ON, BlockTags.DIRT, BlockTags.SNOW_LAYER_CAN_SURVIVE_ON, BlockTags.SNIFFER_DIGGABLE_BLOCK)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), model -> ConfiguredModel.allYRotations(model, 0, false)))
            .simpleItem()
            .register();

    public static final BlockEntry<RotatedPillarBlock> BAMBOO_BUNDLE = REGISTRATE.block("bamboo_bundle", RotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).sound(SoundType.BAMBOO).strength(0.2f, 5.0f))
            .blockstate((ctx, prov) -> prov.axisBlock(ctx.get(), prov.modLoc("block/bamboo")))
            .recipe((ctx, prov) -> prov.singleItem(DataIngredient.items((NonNullSupplier<? extends ItemLike>) BAMBOO_STICK), RecipeCategory.BUILDING_BLOCKS, ctx, 9, 2))
            .simpleItem()
            .register();

    public static final BlockEntry<RotatedPillarBlock> THATCH_BUNDLE = REGISTRATE.block("thatch_bundle", RotatedPillarBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).sound(SoundType.BAMBOO).strength(0.2f, 5.0f).ignitedByLava().pushReaction(PushReaction.DESTROY))
            .blockstate((ctx, prov) -> prov.axisBlock(ctx.get(), prov.modLoc("block/thatch")))
            .recipe((ctx, prov) -> prov.singleItem(DataIngredient.items(Items.SUGAR_CANE), RecipeCategory.BUILDING_BLOCKS, ctx, 4, 1))
            .simpleItem()
            .register();

    public static final BlockEntry<Block> MAHOGANY_PLANKS = planks("mahogany_planks", MapColor.COLOR_BROWN, () -> DataIngredient.items(TropicraftBlocks.MAHOGANY_LOG.get())).register();
    public static final BlockEntry<Block> PALM_PLANKS = planks("palm_planks", MapColor.COLOR_BROWN, () -> DataIngredient.items(TropicraftBlocks.PALM_LOG.get())).register();

    public static final BlockEntry<RotatedPillarBlock> MAHOGANY_LOG = log("mahogany_log", MapColor.WOOD, MapColor.COLOR_BROWN).register();
    public static final BlockEntry<RotatedPillarBlock> PALM_LOG = log("palm_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN).register();

    // TODO: fix this typo
    public static final BlockEntry<RotatedPillarBlock> MAHOGANY_WOOD = wood("mohogany_wood", MapColor.WOOD, MAHOGANY_LOG).lang("Mahogany Wood").register();
    public static final BlockEntry<RotatedPillarBlock> PALM_WOOD = wood("palm_wood", MapColor.COLOR_GRAY, PALM_LOG).register();

    public static final BlockEntry<StairBlock> PALM_STAIRS = woodenStairs("palm_stairs", PALM_PLANKS).register();
    public static final BlockEntry<StairBlock> MAHOGANY_STAIRS = woodenStairs("mahogany_stairs", MAHOGANY_PLANKS).register();
    public static final BlockEntry<StairBlock> THATCH_STAIRS = woodenStairs("thatch_stairs", THATCH_BUNDLE)
            .blockstate((ctx, prov) -> {
                ResourceLocation side = prov.modLoc("block/thatch_side");
                ResourceLocation end = prov.modLoc("block/thatch_end");
                prov.stairsBlock(ctx.get(), side, end, end);
            })
            .register();
    public static final BlockEntry<StairBlock> THATCH_STAIRS_FUZZY = woodenStairs("thatch_stairs_fuzzy", THATCH_BUNDLE)
            .addLayer(() -> RenderType::cutoutMipped)
            .blockstate((ctx, prov) -> {
                ModelFile fuzzyThatch = fuzzyStairs(prov, "thatch_stairs_fuzzy", "thatch_side", "thatch_end", "thatch_grass");
                ModelFile fuzzyThatchOuter = fuzzyStairsOuter(prov, "thatch_stairs_fuzzy_outer", "thatch_side", "thatch_end", "thatch_grass");
                prov.stairsBlock(ctx.get(), fuzzyThatch, prov.models().getExistingFile(prov.modLoc("thatch_stairs_inner")), fuzzyThatchOuter);
            })
            .lang("Thatch Roof")
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 4)
                    .pattern("C  ").pattern("XC ").pattern("XXC")
                    .define('X', THATCH_BUNDLE.get())
                    .define('C', Items.SUGAR_CANE)
                    .unlockedBy("has_thatch_bundle", has(THATCH_BUNDLE.get()))
                    .save(prov))
            .register();
    public static final BlockEntry<StairBlock> BAMBOO_STAIRS = woodenStairs("bamboo_stairs", BAMBOO_BUNDLE)
            .blockstate((ctx, prov) -> {
                ResourceLocation side = prov.modLoc("block/bamboo_side");
                ResourceLocation end = prov.modLoc("block/bamboo_end");
                prov.stairsBlock(ctx.get(), side, end, end);
            })
            .register();
    public static final BlockEntry<StairBlock> CHUNK_STAIRS = stoneStairs("chunk_stairs", CHUNK).register();

    public static final BlockEntry<CoconutBlock> COCONUT = REGISTRATE.block("coconut", CoconutBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).strength(2.0f).sound(SoundType.STONE).pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, droppingChunks(loot, block, TropicraftItems.COCONUT_CHUNK)))
            .tag(BlockTags.MINEABLE_WITH_AXE)
            .addLayer(() -> RenderType::cutout)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cross("coconut", prov.modLoc("block/coconut"))))
            .item()
            .model(TropicraftBlocks::blockSprite)
            .build()
            .register();

    public static final BlockEntry<SlabBlock> BAMBOO_SLAB = woodenSlab("bamboo_slab", BAMBOO_BUNDLE)
            .blockstate((ctx, prov) -> {
                ResourceLocation side = prov.modLoc("block/bamboo_side");
                ResourceLocation end = prov.modLoc("block/bamboo_end");
                prov.slabBlock(ctx.get(), BAMBOO_BUNDLE.getId(), side, end, end);
            })
            .register();
    public static final BlockEntry<SlabBlock> THATCH_SLAB = woodenSlab("thatch_slab", THATCH_BUNDLE)
            .blockstate((ctx, prov) -> {
                ResourceLocation side = prov.modLoc("block/thatch_side");
                ResourceLocation end = prov.modLoc("block/thatch_end");
                prov.slabBlock(ctx.get(), THATCH_BUNDLE.getId(), side, end, end);
            })
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
    public static final BlockEntry<LeavesBlock> GRAPEFRUIT_LEAVES = fruitLeaves("grapefruit_leaves", GRAPEFRUIT_SAPLING, TropicraftItems.GRAPEFRUIT).register();
    public static final BlockEntry<LeavesBlock> LEMON_LEAVES = fruitLeaves("lemon_leaves", LEMON_SAPLING, TropicraftItems.LEMON).register();
    public static final BlockEntry<LeavesBlock> LIME_LEAVES = fruitLeaves("lime_leaves", LIME_SAPLING, TropicraftItems.LIME).register();
    public static final BlockEntry<LeavesBlock> ORANGE_LEAVES = fruitLeaves("orange_leaves", ORANGE_SAPLING, TropicraftItems.ORANGE).register();
    public static final BlockEntry<LeavesBlock> PAPAYA_LEAVES = leaves("papaya_leaves", PAPAYA_SAPLING, SAPLING_RATES, false).register();
    public static final BlockEntry<LeavesBlock> WHITE_FLOWERING_LEAVES = leaves("white_flowering_leaves", true).register();
    public static final BlockEntry<LeavesBlock> RED_FLOWERING_LEAVES = leaves("red_flowering_leaves", true).register();
    public static final BlockEntry<LeavesBlock> BLUE_FLOWERING_LEAVES = leaves("blue_flowering_leaves", true).register();
    public static final BlockEntry<LeavesBlock> PURPLE_FLOWERING_LEAVES = leaves("purple_flowering_leaves", true).register();
    public static final BlockEntry<LeavesBlock> YELLOW_FLOWERING_LEAVES = leaves("yellow_flowering_leaves", true).register();

    public static final BlockEntry<FruitingVineBlock> PASSIONFRUIT_VINE = REGISTRATE.block("passionfruit_vine", FruitingVineBlock::new)
            .properties(p -> p.mapColor(MapColor.GRASS).replaceable().noCollission().strength(0.2f).sound(SoundType.VINE).ignitedByLava().pushReaction(PushReaction.DESTROY))
            .blockstate((ctx, prov) -> {
                MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());
                for (int age : FruitingVineBlock.AGE.getPossibleValues()) {
                    BlockModelBuilder model = prov.models().withExistingParent(ctx.getName() + "_" + age, prov.modLoc("block/vines"))
                            .texture("texture", prov.modLoc("block/" + ctx.getName() + "_" + age));
                    builder.part().modelFile(model).addModel().condition(FruitingVineBlock.AGE, age).condition(BlockStateProperties.NORTH, true);
                    builder.part().modelFile(model).rotationY(270).addModel().condition(FruitingVineBlock.AGE, age).condition(BlockStateProperties.WEST, true);
                    builder.part().modelFile(model).rotationY(90).addModel().condition(FruitingVineBlock.AGE, age).condition(BlockStateProperties.EAST, true);
                    builder.part().modelFile(model).rotationY(180).addModel().condition(FruitingVineBlock.AGE, age).condition(BlockStateProperties.SOUTH, true);
                    builder.part().modelFile(model).rotationX(90).addModel().condition(FruitingVineBlock.AGE, age).condition(BlockStateProperties.DOWN, true);
                    builder.part().modelFile(model).rotationX(270).addModel().condition(FruitingVineBlock.AGE, age).condition(BlockStateProperties.UP, true);
                }
            })
            .addLayer(() -> RenderType::cutoutMipped)
            .loot((loot, block) -> loot.add(block, loot.createSilkTouchOrShearsDispatchTable(block, loot.applyExplosionCondition(block, lootTableItem(TropicraftItems.PASSIONFRUIT)
                    .when(hasBlockStateProperties(block).setProperties(properties().hasProperty(FruitingVineBlock.AGE, FruitingVineBlock.MAX_AGE)))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
            ))))
            .item()
            .model((ctx, prov) -> prov.blockSprite(ctx, prov.modLoc("block/passionfruit_vine_1")))
            .build()
            .register();

    public static final BlockEntry<RotatedPillarBlock> PAPAYA_LOG = log("papaya_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN)
            .recipe((ctx, prov) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, Blocks.JUNGLE_LOG)
                    .requires(ctx.get())
                    .unlockedBy("has_papaya_log", has(ctx.get()))
                    .save(prov, Tropicraft.location("papaya_log_to_jungle_log")))
            .register();
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
            .properties(p -> p.sound(SoundType.AZALEA).noOcclusion().noCollission().instabreak().randomTicks().pushReaction(PushReaction.DESTROY))
            .blockstate((ctx, prov) -> {
                ResourceLocation fruitingBranch = prov.modLoc("block/fruiting_branch");
                List<BlockModelBuilder> models = IntStream.rangeClosed(0, FruitingBranchBlock.MAX_AGE).mapToObj(age ->
                        prov.models().withExistingParent(ctx.getName() + "_age" + age, fruitingBranch)
                                .texture("horizontal", prov.modLoc("block/jocote_branch_horizontal_" + age))
                                .texture("vertical", prov.modLoc("block/jocote_branch_vertical"))
                ).toList();
                prov.horizontalBlock(ctx.get(), state -> models.get(state.getValue(FruitingBranchBlock.AGE)));
            })
            .addLayer(() -> RenderType::cutoutMipped)
            .loot((loot, block) -> loot.add(block, lootTable().withPool(loot.applyExplosionCondition(block, lootPool().setRolls(ConstantValue.exactly(1))
                    .add(lootTableItem(block))
                    .add(lootTableItem(JOCOTE)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                            .when(hasBlockStateProperties(block).setProperties(properties().hasProperty(FruitingBranchBlock.AGE, FruitingBranchBlock.MAX_AGE)))
                    )
            ))))
            .item()
            .model((ctx, prov) -> prov.blockSprite(ctx, prov.modLoc("block/jocote_branch_horizontal_0")))
            .build()
            .register();

    public static final BlockEntry<MahoganyNutBlock> MAHOGANY_NUT = REGISTRATE.block("mahogany_nut", MahoganyNutBlock::new)
            .initialProperties(() -> Blocks.COCOA)
            .blockstate((ctx, prov) -> prov.getVariantBuilder(ctx.get()).forAllStates(state ->
                    ConfiguredModel.builder()
                            .modelFile(prov.models().getExistingFile(prov.modLoc("block/mahogany_nut_" + state.getValue(MahoganyNutBlock.AGE))))
                            .build()
            ))
            .loot((loot, block) -> loot.add(block, lootTable().withPool(loot.applyExplosionCondition(block, lootPool().setRolls(ConstantValue.exactly(1))
                    .add(lootTableItem(block).when(hasBlockStateProperties(block).setProperties(properties().hasProperty(MahoganyNutBlock.AGE, MahoganyNutBlock.MAX_AGE))))
            ))))
            .addLayer(() -> RenderType::cutoutMipped)
            .item()
            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/mahogany_nut")))
            .build()
            .register();

    public static final BlockEntry<RotatedPillarBlock> RED_MANGROVE_LOG = log("red_mangrove_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG.get())
            .item().tag(TropicraftTags.Items.MANGROVE_LOGS).build()
            .register();
    public static final BlockEntry<RotatedPillarBlock> RED_MANGROVE_WOOD = wood("red_mangrove_wood", MapColor.COLOR_GRAY, RED_MANGROVE_LOG, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD.get()).register();
    public static final BlockEntry<MangroveRootsBlock> RED_MANGROVE_ROOTS = mangroveRoots("red_mangrove_roots").register();

    public static final BlockEntry<RotatedPillarBlock> LIGHT_MANGROVE_LOG = log("light_mangrove_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG.get())
            .item().tag(TropicraftTags.Items.MANGROVE_LOGS).build()
            .register();
    public static final BlockEntry<RotatedPillarBlock> LIGHT_MANGROVE_WOOD = wood("light_mangrove_wood", MapColor.COLOR_GRAY, LIGHT_MANGROVE_LOG, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD.get()).register();
    public static final BlockEntry<MangroveRootsBlock> LIGHT_MANGROVE_ROOTS = mangroveRoots("light_mangrove_roots").register();

    public static final BlockEntry<RotatedPillarBlock> BLACK_MANGROVE_LOG = log("black_mangrove_log", MapColor.COLOR_GRAY, MapColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG.get())
            .item().tag(TropicraftTags.Items.MANGROVE_LOGS).build()
            .register();
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

    public static final BlockEntry<RotatedPillarBlock> STRIPPED_MANGROVE_LOG = log("stripped_mangrove_log", MapColor.COLOR_RED, MapColor.COLOR_RED).register();
    public static final BlockEntry<RotatedPillarBlock> STRIPPED_MANGROVE_WOOD = wood("stripped_mangrove_wood", MapColor.COLOR_RED, STRIPPED_MANGROVE_LOG).register();

    public static final BlockEntry<Block> MANGROVE_PLANKS = planks("mangrove_planks", MapColor.COLOR_BROWN, () -> DataIngredient.items((NonNullSupplier<? extends Block>) TropicraftBlocks.LIGHT_MANGROVE_LOG, TropicraftBlocks.RED_MANGROVE_LOG, TropicraftBlocks.BLACK_MANGROVE_LOG)).register();
    public static final BlockEntry<StairBlock> MANGROVE_STAIRS = woodenStairs("mangrove_stairs", MANGROVE_PLANKS).register();
    public static final BlockEntry<SlabBlock> MANGROVE_SLAB = woodenSlab("mangrove_slab", MANGROVE_PLANKS).register();
    public static final BlockEntry<FenceBlock> MANGROVE_FENCE = woodenFence("mangrove_fence", MANGROVE_PLANKS).register();
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
            .addLayer(() -> RenderType::cutout)
            .blockstate((ctx, prov) -> {
                VariantBlockStateBuilder builder = prov.getVariantBuilder(ctx.get());
                for (ReedsBlock.Type type : ReedsBlock.Type.values()) {
                    VariantBlockStateBuilder.PartialBlockstate partialState = builder.partialState().with(ReedsBlock.TYPE, type);
                    for (String texture : type.getTextures()) {
                        partialState.addModels(new ConfiguredModel(prov.models().crop(texture, prov.modLoc("block/" + texture))));
                    }
                }
            })
            .item()
            .model((ctx, prov) -> prov.blockSprite(ctx, prov.modLoc("block/" + ctx.getName() + "_top_tall")))
            .build()
            .register();

    // TODO: register with food
    public static final BlockEntry<PapayaBlock> PAPAYA = REGISTRATE.block("papaya", PapayaBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).randomTicks().strength(0.2f, 3.0f).sound(SoundType.WOOD).noOcclusion().pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(loot.applyExplosionDecay(block, LootItem.lootTableItem(block.asItem()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2))
                            .when(hasBlockStateProperties(block).setProperties(properties().hasProperty(PapayaBlock.AGE, 1)))))))))
            .addLayer(() -> RenderType::cutout)
            .blockstate((ctx, prov) -> {
                prov.models().withExistingParent("papaya_stage0", "cocoa_stage2")
                        .texture("particle", prov.modLoc("block/papaya_stage0"))
                        .texture("cocoa", prov.modLoc("block/papaya_stage0"));
                prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
                    int age = state.getValue(PapayaBlock.AGE);
                    Direction facing = state.getValue(PapayaBlock.FACING);
                    ModelFile.ExistingModelFile modelFile = prov.models().getExistingFile(prov.modLoc("block/papaya_stage" + age));
                    return new ConfiguredModel[]{new ConfiguredModel(modelFile, 0, facing.get2DDataValue() * 90, false)};
                });
            })
            .item().defaultModel().build()
            .register();

    public static final BlockEntry<FenceBlock> BAMBOO_FENCE = woodenFence("bamboo_fence", BAMBOO_BUNDLE)
            .blockstate((ctx, prov) -> prov.fenceBlock(ctx.get(), prov.modLoc("block/bamboo_side")))
            .item()
            .tag(ItemTags.WOODEN_FENCES)
            .model((ctx, prov) -> prov.fenceInventory(ctx.getName(), prov.modLoc("block/bamboo_side")))
            .build()
            .register();
    public static final BlockEntry<FenceBlock> THATCH_FENCE = woodenFence("thatch_fence", THATCH_BUNDLE)
            .blockstate((ctx, prov) -> prov.fenceBlock(ctx.get(), prov.modLoc("block/thatch_side")))
            .item()
            .tag(ItemTags.WOODEN_FENCES)
            .model((ctx, prov) -> prov.fenceInventory(ctx.getName(), prov.modLoc("block/thatch_side")))
            .build()
            .register();
    public static final BlockEntry<FenceBlock> CHUNK_FENCE = woodenFence("chunk_fence", CHUNK).register();
    public static final BlockEntry<FenceBlock> PALM_FENCE = woodenFence("palm_fence", PALM_PLANKS).register();
    public static final BlockEntry<FenceBlock> MAHOGANY_FENCE = woodenFence("mahogany_fence", MAHOGANY_PLANKS).register();

    public static final BlockEntry<FenceGateBlock> BAMBOO_FENCE_GATE = fenceGate("bamboo_fence_gate", BAMBOO_BUNDLE)
            .blockstate((ctx, prov) -> prov.fenceGateBlock(ctx.get(), prov.modLoc("block/bamboo_side")))
            .register();
    public static final BlockEntry<FenceGateBlock> THATCH_FENCE_GATE = fenceGate("thatch_fence_gate", THATCH_BUNDLE)
            .blockstate((ctx, prov) -> prov.fenceGateBlock(ctx.get(), prov.modLoc("block/thatch_side")))
            .register();
    public static final BlockEntry<FenceGateBlock> CHUNK_FENCE_GATE = fenceGate("chunk_fence_gate", CHUNK).register();
    public static final BlockEntry<FenceGateBlock> PALM_FENCE_GATE = fenceGate("palm_fence_gate", PALM_PLANKS).register();
    public static final BlockEntry<FenceGateBlock> MAHOGANY_FENCE_GATE = fenceGate("mahogany_fence_gate", MAHOGANY_PLANKS).register();

    public static final BlockEntry<WallBlock> CHUNK_WALL = REGISTRATE.block("chunk_wall", WallBlock::new)
            .initialProperties(CHUNK)
            .tag(BlockTags.WALLS)
            .blockstate((ctx, prov) -> prov.wallBlock(ctx.get(), prov.blockTexture(CHUNK.get())))
            .recipe((ctx, prov) -> prov.wall(DataIngredient.items(CHUNK.get()), RecipeCategory.DECORATIONS, ctx))
            .item()
            .tag(ItemTags.WALLS)
            .model((ctx, prov) -> prov.wallInventory(ctx.getName(), prov.modLoc("block/" + CHUNK.getId().getPath())))
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
            .properties(p -> p.mapColor(MapColor.PLANT).noCollission().instabreak().sound(SoundType.GRASS).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, createSinglePropConditionTable(loot, block, DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)))
            .tag(BlockTags.TALL_FLOWERS)
            .addLayer(() -> RenderType::cutout)
            .blockstate(TropicraftBlocks::doublePlant)
            .recipe((ctx, prov) -> prov.singleItemUnfinished(DataIngredient.items(ctx.get()), RecipeCategory.MISC, () -> Items.PURPLE_DYE, 1, 4).save(prov, Tropicraft.location(name(Items.PURPLE_DYE))))
            .item()
            .model((ctx, prov) -> prov.blockSprite(ctx, prov.modLoc("block/iris_top")))
            .build()
            .register();

    public static final BlockEntry<PineappleBlock> PINEAPPLE = REGISTRATE.block("pineapple", PineappleBlock::new)
            .properties(p -> p.mapColor(MapColor.PLANT).randomTicks().noCollission().instabreak().sound(SoundType.GRASS).replaceable().ignitedByLava().pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, droppingChunks(loot, block, TropicraftItems.PINEAPPLE_CUBES,
                    hasBlockStateProperties(block).setProperties(
                            properties().hasProperty(
                                    DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)))))
            .addLayer(() -> RenderType::cutout)
            .blockstate(TropicraftBlocks::doublePlant)
            .item()
            .model((ctx, prov) -> prov.blockSprite(ctx, prov.modLoc("block/pineapple_top")))
            .build()
            .register();

    public static final BlockEntry<BongoDrumBlock> SMALL_BONGO_DRUM = bongoDrum("small_bongo_drum", BongoDrumBlock.Size.SMALL).register();
    public static final BlockEntry<BongoDrumBlock> MEDIUM_BONGO_DRUM = bongoDrum("medium_bongo_drum", BongoDrumBlock.Size.MEDIUM).register();
    public static final BlockEntry<BongoDrumBlock> LARGE_BONGO_DRUM = bongoDrum("large_bongo_drum", BongoDrumBlock.Size.LARGE).register();

    private static BlockBuilder<BongoDrumBlock, Registrate> bongoDrum(String name, BongoDrumBlock.Size size) {
        return REGISTRATE.block(name, p -> new BongoDrumBlock(size, p))
                .properties(p -> p.mapColor(MapColor.TERRACOTTA_WHITE).strength(2.0f).sound(SoundType.WOOD).ignitedByLava().instrument(NoteBlockInstrument.BASS))
                .tag(TropicraftTags.Blocks.BONGOS)
                .blockstate((ctx, prov) -> {
                    AABB bb = size.shape.bounds();
                    prov.simpleBlock(ctx.get(),
                            prov.models().cubeBottomTop(ctx.getName(), prov.modLoc("block/bongo_side"), prov.modLoc("block/bongo_bottom"), prov.modLoc("block/bongo_top"))
                                    .element()
                                    .from((float) bb.minX * 16, (float) bb.minY * 16, (float) bb.minZ * 16)
                                    .to((float) bb.maxX * 16, (float) bb.maxY * 16, (float) bb.maxZ * 16)
                                    .allFaces((dir, face) -> face
                                            .texture(dir.getAxis().isHorizontal() ? "#side" : dir == Direction.DOWN ? "#bottom" : "#top")
                                            .cullface(dir.getAxis().isVertical() ? dir : null))
                                    .end());
                })
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
                        .pattern(StringUtils.repeat('T', size.recipeColumns))
                        .pattern(StringUtils.repeat('B', size.recipeColumns))
                        .pattern(StringUtils.repeat('B', size.recipeColumns))
                        .define('T', IGUANA_LEATHER.get())
                        .define('B', MAHOGANY_PLANKS.get())
                        .group("tropicraft:bongos")
                        .unlockedBy("has_" + prov.safeName(IGUANA_LEATHER.get()), has(IGUANA_LEATHER.get()))
                        .save(prov))
                .simpleItem();
    }

    public static final BlockEntry<LadderBlock> BAMBOO_LADDER = REGISTRATE.block("bamboo_ladder", LadderBlock::new)
            .initialProperties(() -> Blocks.BAMBOO)
            .addLayer(() -> RenderType::cutout)
            .tag(BlockTags.CLIMBABLE, BlockTags.MINEABLE_WITH_AXE)
            .blockstate((ctx, prov) -> {
                ResourceLocation texture = prov.blockTexture(ctx.get());
                ModelFile model = prov.models().withExistingParent(ctx.getName(), "ladder")
                        .texture("particle", texture)
                        .texture("texture", texture);
                prov.getVariantBuilder(ctx.get()) // TODO make horizontalBlock etc support this case
                        .forAllStatesExcept(state -> ConfiguredModel.builder()
                                        .modelFile(model)
                                        .rotationY(((int) state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360)
                                        .build(),
                                LadderBlock.WATERLOGGED);
            })
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 4)
                    .pattern("S S").pattern("BSB").pattern("S S")
                    .define('S', BAMBOO_STICK.get())
                    .define('B', Items.BAMBOO)
                    .unlockedBy("has_bamboo", has(Items.BAMBOO))
                    .save(prov))
            .item()
            .model(TropicraftBlocks::blockSprite)
            .build()
            .register();

    public static final BlockEntry<BoardwalkBlock> BAMBOO_BOARDWALK = boardwalk("bamboo_boardwalk", BAMBOO_SLAB, Either.right(Tropicraft.location("block/bamboo_side"))).register();
    public static final BlockEntry<BoardwalkBlock> PALM_BOARDWALK = boardwalk("palm_boardwalk", PALM_SLAB, Either.left(PALM_PLANKS)).register();
    public static final BlockEntry<BoardwalkBlock> MAHOGANY_BOARDWALK = boardwalk("mahogany_boardwalk", MAHOGANY_SLAB, Either.left(MAHOGANY_PLANKS)).register();
    public static final BlockEntry<BoardwalkBlock> MANGROVE_BOARDWALK = boardwalk("mangrove_boardwalk", MANGROVE_SLAB, Either.left(MANGROVE_PLANKS)).register();

    public static final BlockEntry<BambooChestBlock> BAMBOO_CHEST = REGISTRATE.block("bamboo_chest", BambooChestBlock::new)
            .initialProperties(BAMBOO_BUNDLE)
            .properties(p -> p.strength(1.0f))
            .blockstate((ctx, prov) -> noModelBlock(ctx, prov, prov.modLoc("block/bamboo_side")))
            .blockEntity(BambooChestBlockEntity::new)
            .renderer(() -> BambooChestRenderer::new)
            .build()
            .item(itemWithRenderer(TropicraftItemRenderers::bambooChest))
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), "item/chest")
                    .texture("particle", prov.modLoc("block/bamboo_side")))
            .build()
            .addMiscData(ProviderType.LANG, prov -> {
                prov.add(Tropicraft.ID + ".container.bambooChest", "Bamboo Chest");
                prov.add(Tropicraft.ID + ".container.bambooChestDouble", "Large Bamboo Chest");
            })
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
                    .pattern("BBB").pattern("B B").pattern("BBB")
                    .define('B', Items.BAMBOO)
                    .unlockedBy("has_bamboo", has(Items.BAMBOO))
                    .save(prov))
            .register();

    public static final BlockEntityEntry<BambooChestBlockEntity> BAMBOO_CHEST_ENTITY = BlockEntityEntry.cast(BAMBOO_CHEST.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<SifterBlock> SIFTER = REGISTRATE.block("sifter", SifterBlock::new)
            .initialProperties(() -> Blocks.OAK_PLANKS)
            .properties(Properties::noOcclusion)
            .addLayer(() -> RenderType::cutout)
            .blockEntity(SifterBlockEntity::new)
            .renderer(() -> SifterRenderer::new)
            .build()
            .setData(ProviderType.LANG, (ctx, prov) -> prov.addBlockWithTooltip(ctx, "Place any type of tropics or regular sand in the sifter. What treasures are hidden inside?"))
            .recipe((ctx, prov) -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
                        .pattern("XXX").pattern("XIX").pattern("XXX")
                        .define('X', ItemTags.PLANKS)
                        .define('I', Tags.Items.GLASS_BLOCKS)
                        .group("tropicraft:sifter")
                        .unlockedBy("has_glass", has(Tags.Items.GLASS_BLOCKS))
                        .save(prov);
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
                        .pattern("XXX").pattern("XIX").pattern("XXX")
                        .define('X', ItemTags.PLANKS)
                        .define('I', Tags.Items.GLASS_PANES)
                        .group("tropicraft:sifter")
                        .unlockedBy("has_glass_pane", has(Tags.Items.GLASS_PANES))
                        .save(prov, Tropicraft.location("sifter_with_glass_pane"));
            })
            .simpleItem()
            .register();

    public static final BlockEntityEntry<SifterBlockEntity> SIFTER_ENTITY = BlockEntityEntry.cast(SIFTER.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<DrinkMixerBlock> DRINK_MIXER = REGISTRATE.block("drink_mixer", DrinkMixerBlock::new)
            .properties(p -> p.mapColor(MapColor.STONE).strength(2.0f, 30.0f).noOcclusion().instrument(NoteBlockInstrument.BASEDRUM))
            .blockstate((ctx, prov) -> noModelBlock(ctx, prov, prov.modLoc("block/chunk")))
            .blockEntity(DrinkMixerBlockEntity::new)
            .renderer(() -> DrinkMixerRenderer::new)
            .build()
            .item(itemWithRenderer(TropicraftItemRenderers::drinkMixer))
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), prov.modLoc("item/tall_machine"))
                    .texture("particle", prov.modLoc("block/chunk")))
            .build()
            .addLayer(() -> RenderType::cutout)
            .setData(ProviderType.LANG, (ctx, prov) -> prov.addBlockWithTooltip(ctx, "Place two drink ingredients on the mixer, then place an empty mug on the base, then ???, then enjoy!"))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
                    .pattern("XXX").pattern("XYX").pattern("XXX")
                    .define('X', CHUNK.get())
                    .define('Y', BAMBOO_MUG.get())
                    .unlockedBy("has_bamboo_mug", has(BAMBOO_MUG.get()))
                    .save(prov))
            .register();

    public static final BlockEntityEntry<DrinkMixerBlockEntity> DRINK_MIXER_ENTITY = BlockEntityEntry.cast(DRINK_MIXER.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<AirCompressorBlock> AIR_COMPRESSOR = REGISTRATE.block("air_compressor", AirCompressorBlock::new)
            .properties(p -> p.mapColor(MapColor.STONE).strength(2.0f, 30.0f).noOcclusion().instrument(NoteBlockInstrument.BASEDRUM))
            .blockstate((ctx, prov) -> noModelBlock(ctx, prov, prov.modLoc("block/chunk")))
            .blockEntity(AirCompressorBlockEntity::new)
            .renderer(() -> AirCompressorRenderer::new)
            .build()
            .item(itemWithRenderer(TropicraftItemRenderers::airCompressor))
            .model((ctx, prov) -> prov.withExistingParent(ctx.getName(), prov.modLoc("item/tall_machine"))
                    .texture("particle", prov.modLoc("block/chunk")))
            .build()
            .addLayer(() -> RenderType::cutout)
            .setData(ProviderType.LANG, (ctx, prov) -> prov.addBlockWithTooltip(ctx, "Place an empty scuba harness in the compressor to fill it with air!"))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 1)
                    .pattern("XXX")
                    .pattern("XYX")
                    .pattern("XXX")
                    .define('X', CHUNK.get())
                    .define('Y', AZURITE.get())
                    .unlockedBy("has_" + prov.safeName(CHUNK.get()), has(CHUNK.get()))
                    .unlockedBy("has_" + prov.safeName(AZURITE.get()), has(AZURITE.get()))
                    .save(prov))
            .register();

    public static final BlockEntityEntry<AirCompressorBlockEntity> AIR_COMPRESSOR_ENTITY = BlockEntityEntry.cast(AIR_COMPRESSOR.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<VolcanoBlock> VOLCANO = REGISTRATE.block("volcano", VolcanoBlock::new)
            .initialProperties(() -> Blocks.BEDROCK)
            .properties(Properties::noLootTable)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().getExistingFile(prov.mcLoc("block/bedrock"))))
            .simpleBlockEntity(VolcanoBlockEntity::new)
            .register();

    public static final BlockEntityEntry<VolcanoBlockEntity> VOLCANO_ENTITY = BlockEntityEntry.cast(VOLCANO.getSibling(Registries.BLOCK_ENTITY_TYPE));

    public static final BlockEntry<TikiTorchBlock> TIKI_TORCH = REGISTRATE.block("tiki_torch", TikiTorchBlock::new)
            .initialProperties(() -> Blocks.TORCH)
            .properties(p -> p.sound(SoundType.WOOD).lightLevel(state -> state.getValue(TikiTorchBlock.SECTION) == TorchSection.UPPER ? 15 : 0))
            .loot((loot, block) -> loot.add(block, createSinglePropConditionTable(loot, block, TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.UPPER)))
            .addLayer(() -> RenderType::cutout)
            .blockstate((ctx, prov) -> {
                ModelFile tikiLower = prov.models().torch("tiki_torch_lower", prov.modLoc("block/tiki_torch_lower"));
                ModelFile tikiUpper = prov.models().torch("tiki_torch_upper", prov.modLoc("block/tiki_torch_upper"));
                prov.getVariantBuilder(ctx.get())
                        .forAllStates(state -> ConfiguredModel.builder()
                                .modelFile(state.getValue(TikiTorchBlock.SECTION) == TorchSection.UPPER ? tikiUpper : tikiLower).build());
            })
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
                    .pattern("Y").pattern("X").pattern("X")
                    .define('X', BAMBOO_STICK.get())
                    .define('Y', ItemTags.COALS)
                    .unlockedBy("has_bamboo_stick", has(BAMBOO_STICK.get()))
                    .save(prov))
            .item().defaultModel().build()
            .register();

    public static final BlockEntry<FlowerPotBlock> BAMBOO_FLOWER_POT = REGISTRATE.block("bamboo_flower_pot", p -> new FlowerPotBlock(null, () -> Blocks.AIR, p))
            .properties(p -> p.strength(0.2f, 5.0f).sound(SoundType.BAMBOO).pushReaction(PushReaction.DESTROY))
            .addLayer(() -> RenderType::cutout)
            .blockstate((ctx, prov) -> flowerPot(ctx, prov, ctx, prov.modLoc("block/bamboo_side")))
            .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get())
                    .pattern("# #").pattern(" # ")
                    .define('#', Items.BAMBOO)
                    .unlockedBy("has_bamboo", has(Items.BAMBOO))
                    .save(prov))
            .item().defaultModel().build()
            .register();

    public static final BlockEntry<CoffeeBushBlock> COFFEE_BUSH = REGISTRATE.block("coffee_bush", CoffeeBushBlock::new)
            .properties(p -> p.mapColor(MapColor.GRASS).strength(0.15f).sound(SoundType.GRASS).noOcclusion().pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.add(block, coffee(loot, TropicraftBlocks.COFFEE_BUSH.get(), TropicraftItems.RAW_COFFEE_BEAN)))
            .addLayer(() -> RenderType::cutout)
            .blockstate((ctx, prov) -> prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
                int age = state.getValue(CoffeeBushBlock.AGE);
                BlockModelBuilder model = prov.models().withExistingParent(ctx.getName() + "_stage_" + age, prov.modLoc("coffee_bush"))
                        .texture("bush", prov.modLoc("block/" + ctx.getName() + "_stage" + age));
                return ConfiguredModel.builder().modelFile(model).build();
            }))
            .register();

    public static final BlockEntry<GrowableSinglePlantBlock> GOLDEN_LEATHER_FERN = REGISTRATE.block("small_golden_leather_fern", p -> new GrowableSinglePlantBlock(p, () -> TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN))
            .initialProperties(() -> Blocks.FERN)
            .properties(p -> p.offsetType(BlockBehaviour.OffsetType.XZ))
            .addLayer(() -> RenderType::cutout)
            .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cross(ctx.getName(), prov.modLoc("block/small_golden_leather_fern"))))
            .lang("Golden Leather Fern")
            .item()
            .model((ctx, prov) -> prov.generated(ctx, prov.modLoc("item/golden_leather_fern")))
            .build()
            .register();

    public static final BlockEntry<GrowableDoublePlantBlock> TALL_GOLDEN_LEATHER_FERN = REGISTRATE.block("tall_golden_leather_fern", p -> new GrowableDoublePlantBlock(p, () -> TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN).setPickItem(() -> TropicraftBlocks.GOLDEN_LEATHER_FERN))
            .initialProperties(() -> Blocks.LARGE_FERN)
            .loot((loot, block) -> loot.dropOther(block, GOLDEN_LEATHER_FERN.get()))
            .addLayer(() -> RenderType::cutout)
            .blockstate(TropicraftBlocks::doublePlant)
            .register();

    public static final BlockEntry<HugePlantBlock> LARGE_GOLDEN_LEATHER_FERN = REGISTRATE.block("golden_leather_fern", p -> new HugePlantBlock(p).setPickItem(() -> TropicraftBlocks.GOLDEN_LEATHER_FERN))
            .properties(p -> p.mapColor(MapColor.PLANT).noOcclusion().noCollission().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY))
            .loot((loot, block) -> loot.dropOther(block, GOLDEN_LEATHER_FERN.get()))
            .addLayer(() -> RenderType::cutout)
            .blockstate((ctx, prov) -> {
                BlockModelBuilder cross = prov.models().withExistingParent(ctx.getName(), prov.modLoc("block/huge_cross"))
                        .texture("cross", prov.modLoc("block/large_golden_leather_fern"))
                        .texture("particle", prov.blockTexture(GOLDEN_LEATHER_FERN.get()));
                prov.getMultipartBuilder(ctx.get())
                        .part().modelFile(cross).addModel()
                        .condition(HugePlantBlock.TYPE, HugePlantBlock.Type.CENTER);
            })
            .lang("Large Golden Leather Fern")
            .register();

    // Short and tall seagrass
    public static final BlockEntry<CustomSeagrassBlock> EEL_GRASS = seagrass("eel_grass", "Enhalus acoroides", () -> TropicraftBlocks.TALL_EEL_GRASS).register();
    public static final BlockEntry<CustomTallSeagrassBlock> TALL_EEL_GRASS = tallSeagrass("tall_eel_grass", "Enhalus acoroides", EEL_GRASS).register();
    public static final BlockEntry<CustomSeagrassBlock> FLOWERING_EEL_GRASS = seagrass("flowering_eel_grass", "Enhalus acoroides", () -> TropicraftBlocks.FLOWERING_TALL_EEL_GRASS).register();
    public static final BlockEntry<CustomTallSeagrassBlock> FLOWERING_TALL_EEL_GRASS = tallSeagrass("flowering_tall_eel_grass", "Enhalus acoroides", FLOWERING_EEL_GRASS).register();
    public static final BlockEntry<ScientificNameBlock> MATTED_EEL_GRASS = mattedSeagrassBlock("matted_eel_grass", "Enhalus acoroides").register();
    public static final BlockEntry<ScientificNameBlock> EEL_GRASS_BLOCK = seagrassBlock("eel_grass", "Enhalus acoroides").register();

    public static final BlockEntry<CustomSeagrassBlock> FERN_SEAGRASS = seagrass("fern_seagrass", "Halophila spinulosa", () -> TropicraftBlocks.TALL_FERN_SEAGRASS).register();
    public static final BlockEntry<CustomTallSeagrassBlock> TALL_FERN_SEAGRASS = tallSeagrass("tall_fern_seagrass", "Halophila spinulosa", FERN_SEAGRASS).register();
    public static final BlockEntry<ScientificNameBlock> MATTED_FERN_SEAGRASS = mattedSeagrassBlock("matted_fern_seagrass", "Halophila spinulosa").register();
    public static final BlockEntry<ScientificNameBlock> FERN_SEAGRASS_BLOCK = seagrassBlock("fern_seagrass", "Halophila spinulosa").register();

    public static final BlockEntry<CustomSeagrassBlock> SICKLE_SEAGRASS = seagrass("sickle_seagrass", "Thalassodendron ciliatum", () -> TropicraftBlocks.TALL_SICKLE_SEAGRASS).register();
    public static final BlockEntry<CustomTallSeagrassBlock> TALL_SICKLE_SEAGRASS = tallSeagrass("tall_sickle_seagrass", "Thalassodendron ciliatum", SICKLE_SEAGRASS).register();
    public static final BlockEntry<ScientificNameBlock> MATTED_SICKLE_SEAGRASS = mattedSeagrassBlock("matted_sickle_seagrass", "Thalassodendron ciliatum").register();
    public static final BlockEntry<ScientificNameBlock> SICKLE_SEAGRASS_BLOCK = seagrassBlock("sickle_seagrass", "Thalassodendron ciliatum").register();

    // Short only seagrass
    public static final BlockEntry<CustomSeagrassBlock> NOODLE_SEAGRASS = seagrass("noodle_seagrass", "Syringodium isoetifolium", null).register();
    public static final BlockEntry<ScientificNameBlock> MATTED_NOODLE_SEAGRASS = mattedSeagrassBlock("matted_noodle_seagrass", "Syringodium isoetifolium").register();
    public static final BlockEntry<ScientificNameBlock> NOODLE_SEAGRASS_BLOCK = seagrassBlock("noodle_seagrass", "Syringodium isoetifolium").register();

    private static BlockBuilder<CustomSeagrassBlock, Registrate> seagrass(String name, String scientificName, @Nullable Supplier<BlockEntry<? extends TallSeagrassBlock>> tall) {
        return REGISTRATE.block(name, p -> new CustomSeagrassBlock(p, scientificName, () -> tall.get().get()))
                .initialProperties(() -> Blocks.SEAGRASS)
                .loot((loot, block) -> loot.add(block, onlyWithSilkTouchOrShears(loot, block)))
                .addLayer(() -> RenderType::cutout)
                .blockstate((ctx, prov) -> {
                    ResourceLocation texture = prov.blockTexture(ctx.get());
                    prov.simpleBlock(ctx.get(), prov.models().withExistingParent(ctx.getName(), prov.mcLoc("template_seagrass"))
                            .texture("texture", texture)
                            .texture("particle", texture));
                })
                .item()
                .model(TropicraftBlocks::blockSprite)
                .build();
    }

    private static BlockBuilder<CustomTallSeagrassBlock, Registrate> tallSeagrass(String name, String scientificName, BlockEntry<? extends SeagrassBlock> normal) {
        return REGISTRATE.block(name, p -> new CustomTallSeagrassBlock(p, scientificName, normal::get))
                .initialProperties(() -> Blocks.SEAGRASS)
                .loot((loot, block) -> loot.add(block, onlyWithSilkTouchOrShears(loot, normal.get())))
                .addLayer(() -> RenderType::cutout)
                .blockstate((ctx, prov) -> {
                    ModelFile top = prov.models().withExistingParent(ctx.getName() + "_top", prov.mcLoc("template_seagrass"))
                            .texture("texture", prov.modLoc("block/" + ctx.getName() + "_top"))
                            .texture("particle", prov.modLoc("block/" + ctx.getName() + "_top"));

                    ModelFile bottom = prov.models().withExistingParent(ctx.getName() + "_bottom", prov.mcLoc("template_seagrass"))
                            .texture("texture", prov.modLoc("block/" + ctx.getName() + "_bottom"))
                            .texture("particle", prov.modLoc("block/" + ctx.getName() + "_bottom"));

                    MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());
                    builder.part().modelFile(top).addModel()
                            .condition(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER)
                            .end();
                    builder.part().modelFile(bottom).addModel()
                            .condition(TallSeagrassBlock.HALF, DoubleBlockHalf.LOWER)
                            .end();
                });
    }

    private static BlockBuilder<ScientificNameBlock, Registrate> seagrassBlock(String name, String scientificName) {
        return REGISTRATE.block(name + "_block", p -> new ScientificNameBlock(p, scientificName))
                .initialProperties(() -> Blocks.SAND)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models()
                        .cubeAll(ctx.getName(), prov.modLoc("block/matted_" + name + "_top"))))
                .simpleItem();
    }

    private static BlockBuilder<ScientificNameBlock, Registrate> mattedSeagrassBlock(String name, String scientificName) {
        return REGISTRATE.block(name, p -> new ScientificNameBlock(p, scientificName))
                .initialProperties(() -> Blocks.SAND)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cubeBottomTop(ctx.getName(),
                        prov.modLoc("block/" + ctx.getName() + "_side"),
                        prov.modLoc("block/" + TropicraftBlocks.PURIFIED_SAND.getId().getPath()),
                        prov.modLoc("block/" + ctx.getName() + "_top")
                )))
                .simpleItem();
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
                .addLayer(() -> RenderType::cutout)
                .tag(BlockTags.FLOWER_POTS)
                .blockstate((ctx, prov) -> flowerPot(ctx, prov, BAMBOO_FLOWER_POT, prov.modLoc("block/bamboo_side")))
                .register();
    }

    private static BlockEntry<FlowerPotBlock> vanillaPot(String name, Supplier<? extends Block> plant) {
        return REGISTRATE.block(name, p -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, plant, p))
                .initialProperties(() -> Blocks.FLOWER_POT)
                .loot((loot, block) -> loot.add(block, pottedPlantLoot(loot, block)))
                .addLayer(() -> RenderType::cutout)
                .tag(BlockTags.FLOWER_POTS)
                .blockstate((ctx, prov) -> flowerPot(ctx, prov, () -> Blocks.FLOWER_POT, ResourceLocation.withDefaultNamespace("block/flower_pot")))
                .register();
    }

    private static void flowerPot(DataGenContext<Block, ? extends FlowerPotBlock> ctx, RegistrateBlockstateProvider prov, Supplier<? extends Block> empty, ResourceLocation particle) {
        Block flower = ctx.get().getPotted();
        boolean isVanilla = flower.builtInRegistryHolder().key().location().getNamespace().equals("minecraft");
        String flowerName = name(flower);
        String parent = flower == Blocks.AIR ? "flower_pot" : !isVanilla ? "flower_pot_cross" : ModelProvider.BLOCK_FOLDER + "/potted_" + flowerName;
        BlockModelBuilder model = prov.models().withExistingParent(ctx.getName(), parent)
                .texture("flowerpot", prov.blockTexture(empty.get()))
                .texture("dirt", prov.mcLoc("block/dirt"))
                .texture("particle", prov.modLoc("block/bamboo_side"));
        if (!isVanilla) {
            if (flower instanceof TropicsFlowerBlock) {
                model.texture("plant", prov.modLoc(ModelProvider.BLOCK_FOLDER + "/flower/" + flowerName));
            } else if (flower instanceof TallFlowerBlock) {
                model.texture("plant", prov.modLoc(ModelProvider.BLOCK_FOLDER + "/" + flowerName + "_top"));
            } else {
                model.texture("plant", prov.blockTexture(flower));
            }
        }
        prov.simpleBlock(ctx.get(), model);
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
                        public String getDescriptionId() {
                            return Util.makeDescriptionId("block", BuiltInRegistries.BLOCK.getKey(this));
                        }

                        @Override
                        public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader world, BlockPos pos, Player player) {
                            return new ItemStack(Items.REDSTONE_TORCH);
                        }
                    })
                    .initialProperties(() -> Blocks.REDSTONE_WALL_TORCH)
                    .addLayer(() -> RenderType::cutoutMipped)
                    .blockstate((ctx, prov) -> {
                        ResourceLocation parent = prov.modLoc("block/jigarbov_wall_torch");
                        ResourceLocation etchTexture = prov.modLoc("block/jigarbov/" + type.getName());

                        BlockModelBuilder modelLit = prov.models().withExistingParent(ctx.getName(), parent)
                                .texture("torch", prov.mcLoc("block/redstone_torch"))
                                .texture("jigarbov", etchTexture);
                        BlockModelBuilder modelOff = prov.models().withExistingParent(ctx.getName() + "_off", parent)
                                .texture("torch", prov.mcLoc("block/redstone_torch_off"))
                                .texture("jigarbov", etchTexture);

                        prov.getVariantBuilder(ctx.get()).forAllStates(state -> {
                            boolean lit = state.getValue(RedstoneTorchBlock.LIT);
                            Direction facing = state.getValue(RedstoneWallTorchBlock.FACING);
                            int angle = ((int) facing.toYRot() + 90) % 360;
                            return ConfiguredModel.builder()
                                    .modelFile(lit ? modelLit : modelOff)
                                    .rotationY(angle)
                                    .build();
                        });
                    })
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
                .blockstate((ctx, prov) -> prov.stairsBlock(ctx.get(), prov.blockTexture(block.get())))
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
                .blockstate((ctx, prov) -> prov.slabBlock(ctx.get(), block.getId(), prov.blockTexture(block.get())))
                .item()
                .tag(itemTag)
                .build();
    }

    private static BlockBuilder<ButtonBlock, Registrate> woodButton(String name, BlockEntry<? extends Block> block, String texture) {
        return REGISTRATE.block(name, p -> new ButtonBlock(BlockSetType.OAK, 30, p))
                .initialProperties(block)
                .properties(p -> p.noCollission().strength(0.5f).pushReaction(PushReaction.DESTROY))
                .tag(BlockTags.WOODEN_BUTTONS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> prov.buttonBlock(ctx.get(), prov.modLoc("block/" + texture)))
                .recipe((ctx, prov) -> RegistrateRecipeProvider.buttonBuilder(ctx.get(), Ingredient.of(block.asItem()))
                        .unlockedBy("has_" + prov.safeName(block.get()), has(block.get()))
                        .group("wooden_button")
                        .save(prov))
                .item()
                .model((ctx, prov) -> prov.buttonInventory(ctx.getName(), prov.modLoc("block/" + texture)))
                .tag(ItemTags.WOODEN_BUTTONS)
                .build();
    }

    private static BlockBuilder<PressurePlateBlock, Registrate> pressurePlate(String name, BlockEntry<? extends Block> block, String texture) {
        return REGISTRATE.block(name, p -> new PressurePlateBlock(BlockSetType.OAK, p))
                .initialProperties(block)
                .properties(p -> p.forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5f).ignitedByLava().pushReaction(PushReaction.DESTROY))
                .tag(BlockTags.WOODEN_PRESSURE_PLATES, BlockTags.MINEABLE_WITH_AXE)
                .recipe((ctx, prov) -> RegistrateRecipeProvider.pressurePlateBuilder(RecipeCategory.REDSTONE, ctx.get(), Ingredient.of(block.asItem()))
                        .unlockedBy("has_" + prov.safeName(block.get()), has(block.get()))
                        .group("wooden_pressure_plate")
                        .save(prov))
                .blockstate((ctx, prov) -> prov.pressurePlateBlock(ctx.get(), prov.modLoc("block/" + texture)))
                .item()
                .model((ctx, prov) -> prov.pressurePlate(ctx.getName(), prov.modLoc("block/" + texture)))
                .build();
    }

    private static BlockBuilder<StandingSignBlock, Registrate> standingSign(WoodType woodType, Supplier<? extends Item> item, String texture) {
        String woodName = ResourceLocation.parse(woodType.name()).getPath();
        return REGISTRATE.block(woodName + "_sign", p -> new StandingSignBlock(woodType, p))
                .initialProperties(() -> Blocks.OAK_SIGN)
                .tag(BlockTags.STANDING_SIGNS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> {
                    BlockModelBuilder model = prov.models().sign(woodName + "_sign", prov.modLoc("block/" + texture));
                    prov.simpleBlock(ctx.get(), model);
                })
                .loot((loot, b) -> loot.dropOther(b, item.get()))
                .setData(ProviderType.LANG, NonNullBiConsumer.noop())
                .onRegisterAfter(Registries.BLOCK_ENTITY_TYPE, b -> extendBlockEntity(BlockEntityType.SIGN, b));
    }

    private static BlockBuilder<WallSignBlock, Registrate> wallSign(WoodType woodType, Supplier<? extends Item> item, String texture) {
        String woodName = ResourceLocation.parse(woodType.name()).getPath();
        return REGISTRATE.block(woodName + "_wall_sign", p -> new WallSignBlock(woodType, p))
                .initialProperties(() -> Blocks.OAK_WALL_SIGN)
                .tag(BlockTags.WALL_SIGNS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> {
                    BlockModelBuilder model = prov.models().sign(woodName + "_sign", prov.modLoc("block/" + texture));
                    prov.simpleBlock(ctx.get(), model);
                })
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
                .addLayer(() -> RenderType::cutout)
                .tag(BlockTags.SAPLINGS)
                .blockstate((ctx, prov) -> prov.simpleBlock(ctx.get(), prov.models().cross(ctx.getName(), prov.blockTexture(ctx.get()))))
                .item()
                .model(TropicraftBlocks::blockSprite)
                .tag(ItemTags.SAPLINGS)
                .build();
    }

    private static BlockBuilder<LeavesBlock, Registrate> leaves(String name, BlockEntry<SaplingBlock> sapling, float[] saplingRates, boolean decay) {
        return REGISTRATE.block(name, decay ? LeavesBlock::new : TropicraftLeavesBlock::new)
                .initialProperties(() -> Blocks.OAK_LEAVES)
                .loot((loot, block) -> loot.add(block, loot.createLeavesDrops(block, sapling.get(), saplingRates)))
                .tag(BlockTags.LEAVES, BlockTags.MINEABLE_WITH_HOE)
                .item()
                .tag(ItemTags.LEAVES)
                .build();
    }

    private static BlockBuilder<LeavesBlock, Registrate> leaves(String name, boolean decay) {
        return REGISTRATE.block(name, decay ? LeavesBlock::new : TropicraftLeavesBlock::new)
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

    private static BlockBuilder<LeavesBlock, Registrate> fruitLeaves(String name, Supplier<SaplingBlock> sapling, Supplier<? extends Item> fruit) {
        return REGISTRATE.block(name, LeavesBlock::new)
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

    private static BlockBuilder<RotatedPillarBlock, Registrate> log(String name, MapColor topColor, MapColor sideColor, @Nullable Supplier<? extends RotatedPillarBlock> strippedLog) {
        return REGISTRATE.block(name, p -> strippedLog != null ? new TropicraftLogBlock(p, strippedLog) : new RotatedPillarBlock(p))
                .initialProperties(() -> Blocks.OAK_LOG)
                .properties(p -> rotatedPillarProperties(p, topColor, sideColor))
                .tag(BlockTags.LOGS, BlockTags.LOGS_THAT_BURN, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> prov.logBlock(ctx.get()))
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
                .blockstate((ctx, prov) -> {
                    ResourceLocation barkTexture = prov.blockTexture(bark.get());
                    prov.axisBlock(ctx.get(), barkTexture, barkTexture);
                })
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ctx.get(), 3)
                        .pattern("##").pattern("##")
                        .define('#', bark.get())
                        .group("bark")
                        .unlockedBy("has_log", has(Blocks.ACACIA_LOG))
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
                                .hasPostProcess((state, world, pos) -> true)
                )
                .addLayer(() -> RenderType::cutoutMipped)
                .tag(TropicraftTags.Blocks.ROOTS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> {
                    ResourceLocation roots = prov.modLoc("block/" + name);

                    ModelFile stem = prov.models().withExistingParent(name + "_stem", prov.modLoc("block/mangrove_roots/stem"))
                            .texture("roots", roots);
                    ModelFile stemShort = prov.models().withExistingParent(name + "_stem_short", prov.modLoc("block/mangrove_roots/stem_short"))
                            .texture("roots", roots);
                    ModelFile connectionLow = prov.models().withExistingParent(name + "_connection_low", prov.modLoc("block/mangrove_roots/connection_low"))
                            .texture("roots", roots);
                    ModelFile connectionHigh = prov.models().withExistingParent(name + "_connection_high", prov.modLoc("block/mangrove_roots/connection_high"))
                            .texture("roots", roots);

                    ModelFile appendagesHigh = prov.models().withExistingParent(name + "_appendages_high", prov.modLoc("block/mangrove_roots/appendages"))
                            .texture("appendages", prov.modLoc("block/" + name + "_appendages_high"));
                    ModelFile appendagesHighShort = prov.models().withExistingParent(name + "_appendages_high_short", prov.modLoc("block/mangrove_roots/appendages"))
                            .texture("appendages", prov.modLoc("block/" + name + "_appendages_high_short"));
                    ModelFile appendagesGrounded = prov.models().withExistingParent(name + "_appendages_ground", prov.modLoc("block/mangrove_roots/appendages"))
                            .texture("appendages", prov.modLoc("block/" + name + "_appendages_ground"));
                    ModelFile appendagesGroundedShort = prov.models().withExistingParent(name + "_appendages_ground_short", prov.modLoc("block/mangrove_roots/appendages"))
                            .texture("appendages", prov.modLoc("block/" + name + "_appendages_ground_short"));

                    MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());

                    builder.part().modelFile(stem).addModel()
                            .condition(MangroveRootsBlock.TALL, true);
                    builder.part().modelFile(stemShort).addModel()
                            .condition(MangroveRootsBlock.TALL, false);

                    builder.part().modelFile(appendagesGrounded).addModel()
                            .condition(MangroveRootsBlock.TALL, true)
                            .condition(MangroveRootsBlock.GROUNDED, true);
                    builder.part().modelFile(appendagesGroundedShort).addModel()
                            .condition(MangroveRootsBlock.TALL, false)
                            .condition(MangroveRootsBlock.GROUNDED, true);
                    builder.part().modelFile(appendagesHigh).addModel()
                            .condition(MangroveRootsBlock.TALL, true)
                            .condition(MangroveRootsBlock.GROUNDED, false);
                    builder.part().modelFile(appendagesHighShort).addModel()
                            .condition(MangroveRootsBlock.TALL, false)
                            .condition(MangroveRootsBlock.GROUNDED, false);

                    for (int i = 0; i < 4; i++) {
                        EnumProperty<MangroveRootsBlock.Connection> connection = MangroveRootsBlock.CONNECTIONS[i];
                        int rotation = (i * 90 + 270) % 360;

                        builder.part().modelFile(connectionHigh).rotationY(rotation).uvLock(true).addModel()
                                .condition(connection, MangroveRootsBlock.Connection.HIGH);
                        builder.part().modelFile(connectionLow).rotationY(rotation).uvLock(true).addModel()
                                .condition(connection, MangroveRootsBlock.Connection.LOW);
                    }
                })
                .item()
                .model((ctx, prov) -> prov.blockItem(ctx, "_stem"))
                .build();
    }

    private static BlockBuilder<MangroveLeavesBlock, Registrate> mangroveLeaves(String name, Supplier<PropaguleBlock> propagule) {
        return REGISTRATE.block(name, p -> new MangroveLeavesBlock(p, propagule))
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
                .addLayer(() -> RenderType::cutout)
                .tag(BlockTags.SAPLINGS)
                .blockstate((ctx, prov) -> {
                    BlockModelBuilder planted = prov.models().cross(ctx.getName() + "_planted", prov.modLoc("block/" + ctx.getName() + "_planted"));
                    BlockModelBuilder hanging = prov.models().cross(ctx.getName() + "_hanging", prov.blockTexture(ctx.get()));
                    prov.getVariantBuilder(ctx.get())
                            .partialState().with(PropaguleBlock.PLANTED, false).addModels(new ConfiguredModel(hanging))
                            .partialState().with(PropaguleBlock.PLANTED, true).addModels(new ConfiguredModel(planted));
                })
                .setData(ProviderType.LANG, (ctx, prov) -> prov.addBlockWithTooltip(ctx, scientificName))
                .item()
                .model(TropicraftBlocks::blockSprite)
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

    private static BlockBuilder<FenceBlock, Registrate> woodenFence(String name, BlockEntry<? extends Block> block) {
        return REGISTRATE.block(name, FenceBlock::new)
                .initialProperties(block)
                .tag(BlockTags.WOODEN_FENCES, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> prov.fenceBlock(ctx.get(), prov.blockTexture(block.get())))
                .recipe((ctx, prov) -> prov.fence(DataIngredient.items(block.get()), RecipeCategory.DECORATIONS, ctx, "wooden_fence"))
                .item()
                .tag(ItemTags.WOODEN_FENCES)
                .model((ctx, prov) -> prov.fenceInventory(ctx.getName(), prov.modLoc("block/" + prov.name(block))))
                .build();
    }

    private static BlockBuilder<FenceGateBlock, Registrate> fenceGate(String name, BlockEntry<? extends Block> block) {
        return REGISTRATE.block(name, p -> new FenceGateBlock(p, SoundEvents.FENCE_GATE_OPEN, SoundEvents.FENCE_GATE_CLOSE))
                .initialProperties(block)
                .tag(BlockTags.FENCE_GATES, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> prov.fenceGateBlock(ctx.get(), prov.blockTexture(block.get())))
                .recipe((ctx, prov) -> prov.fenceGate(DataIngredient.items(block.get()), RecipeCategory.DECORATIONS, ctx, "wooden_fence_gate"))
                .simpleItem();
    }

    private static BlockBuilder<DoorBlock, Registrate> woodenDoor(String name, BlockEntry<? extends Block> material) {
        return REGISTRATE.block(name, p -> new DoorBlock(BlockSetType.OAK, p))
                .initialProperties(() -> Blocks.OAK_DOOR)
                .loot((loot, block) -> loot.add(block, createSinglePropConditionTable(loot, block, DoorBlock.HALF, DoubleBlockHalf.LOWER)))
                .addLayer(() -> RenderType::cutout)
                .tag(BlockTags.WOODEN_DOORS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> prov.doorBlock(ctx.get(), prov.modLoc("block/" + ctx.getName() + "_bottom"), prov.modLoc("block/" + ctx.getName() + "_top")))
                .recipe((ctx, prov) -> prov.trapDoor(DataIngredient.items(material.get()), RecipeCategory.DECORATIONS, ctx, "wooden_door"))
                .item()
                .defaultModel()
                .tag(ItemTags.WOODEN_DOORS)
                .build();
    }

    private static BlockBuilder<TrapDoorBlock, Registrate> trapdoor(String name, BlockEntry<? extends Block> material) {
        return REGISTRATE.block(name, p -> new TrapDoorBlock(BlockSetType.OAK, p))
                .initialProperties(() -> Blocks.OAK_TRAPDOOR)
                .addLayer(() -> RenderType::cutout)
                .tag(BlockTags.WOODEN_TRAPDOORS, BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> prov.trapdoorBlock(ctx.get(), prov.blockTexture(ctx.get()), true))
                .recipe((ctx, prov) -> prov.trapDoor(DataIngredient.items(material.get()), RecipeCategory.DECORATIONS, ctx, "wooden_trapdoor"))
                .item()
                .tag(ItemTags.WOODEN_TRAPDOORS)
                .model((ctx, prov) -> prov.blockItem(ctx, "_bottom"))
                .build();
    }

    private static BlockBuilder<BoardwalkBlock, Registrate> boardwalk(String name, BlockEntry<SlabBlock> slab, Either<Supplier<Block>, ResourceLocation> texture) {
        return REGISTRATE.block(name, BoardwalkBlock::new)
                .initialProperties(slab)
                .properties(Properties::noOcclusion)
                .tag(BlockTags.MINEABLE_WITH_AXE)
                .blockstate((ctx, prov) -> {
                    ResourceLocation resolvedTexture = texture.map(b -> prov.blockTexture(b.get()), Function.identity());
                    ModelFile shortModel = prov.models().withExistingParent(ctx.getName() + "_short", prov.modLoc("block/boardwalk/short"))
                            .texture("planks", resolvedTexture);
                    ModelFile shortPostModel = prov.models().withExistingParent(ctx.getName() + "_short_post", prov.modLoc("block/boardwalk/short_post"))
                            .texture("planks", resolvedTexture);
                    ModelFile tallModel = prov.models().withExistingParent(ctx.getName() + "_tall", prov.modLoc("block/boardwalk/tall"))
                            .texture("planks", resolvedTexture);
                    ModelFile tallPostModel = prov.models().withExistingParent(ctx.getName() + "_tall_post", prov.modLoc("block/boardwalk/tall_post"))
                            .texture("planks", resolvedTexture);
                    ModelFile tallConnectionModel = prov.models().withExistingParent(ctx.getName() + "_tall_connection", prov.modLoc("block/boardwalk/tall_connection"))
                            .texture("planks", resolvedTexture);

                    MultiPartBlockStateBuilder builder = prov.getMultipartBuilder(ctx.get());

                    Direction.Axis[] horizontals = new Direction.Axis[]{Direction.Axis.X, Direction.Axis.Z};
                    for (Direction.Axis axis : horizontals) {
                        int rotation = axis == Direction.Axis.X ? 270 : 0;

                        builder.part().modelFile(shortModel).rotationY(rotation).uvLock(true).addModel()
                                .condition(BoardwalkBlock.TYPE, BoardwalkBlock.Type.SHORTS)
                                .condition(BoardwalkBlock.AXIS, axis);

                        builder.part().modelFile(tallModel).rotationY(rotation).uvLock(true).addModel()
                                .condition(BoardwalkBlock.TYPE, BoardwalkBlock.Type.TALLS)
                                .condition(BoardwalkBlock.AXIS, axis);

                        builder.part().modelFile(tallConnectionModel).rotationY(rotation).uvLock(true).addModel()
                                .condition(BoardwalkBlock.TYPE, BoardwalkBlock.Type.BACKS)
                                .condition(BoardwalkBlock.AXIS, axis);

                        builder.part().modelFile(tallConnectionModel).rotationY((rotation + 180) % 360).uvLock(true).addModel()
                                .condition(BoardwalkBlock.TYPE, BoardwalkBlock.Type.FRONTS)
                                .condition(BoardwalkBlock.AXIS, axis);
                    }

                    builder.part().modelFile(shortPostModel).addModel()
                            .condition(BoardwalkBlock.TYPE, BoardwalkBlock.Type.SHORT_POSTS);
                    builder.part().modelFile(tallPostModel).addModel()
                            .condition(BoardwalkBlock.TYPE, BoardwalkBlock.Type.TALL_POSTS);
                })
                .recipe((ctx, prov) -> ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ctx.get(), 3)
                        .pattern("XXX")
                        .pattern("S S")
                        .define('X', slab.get())
                        .define('S', Tags.Items.RODS_WOODEN)
                        .group("tropicraft:boardwalk")
                        .unlockedBy("has_" + prov.safeName(slab.get()), has(slab.get()))
                        .save(prov))
                .item()
                .model((ctx, prov) -> prov.blockItem(ctx, "_short"))
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
                .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(ItemTags.SWORDS)))
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

    private static ModelFile cubeTop(DataGenContext<Block, BlockTropicraftSand> ctx, RegistrateBlockstateProvider prov, String suffix) {
        return prov.models().cubeTop(ctx.getName() + "_" + suffix, prov.blockTexture(ctx.get()), prov.modLoc("block/" + ctx.getName() + "_" + suffix));
    }

    private static ModelFile fuzzyStairs(RegistrateBlockstateProvider prov, String name, String parent, String side, String end, String cross) {
        return prov.models().withExistingParent(name, prov.modLoc(parent))
                .texture("side", prov.modLoc("block/" + side))
                .texture("bottom", prov.modLoc("block/" + end))
                .texture("top", prov.modLoc("block/" + end))
                .texture("cross", prov.modLoc("block/" + cross));
    }

    private static ModelFile fuzzyStairs(RegistrateBlockstateProvider prov, String name, String side, String end, String cross) {
        return fuzzyStairs(prov, name, "stairs_fuzzy", side, end, cross);
    }

    private static ModelFile fuzzyStairsOuter(RegistrateBlockstateProvider prov, String name, String side, String end, String cross) {
        return fuzzyStairs(prov, name, "stairs_fuzzy_outer", side, end, cross);
    }

    private static void doublePlant(DataGenContext<Block, ? extends DoublePlantBlock> ctx, RegistrateBlockstateProvider prov) {
        BlockModelProvider models = prov.models();
        prov.getVariantBuilder(ctx.get())
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).addModels(new ConfiguredModel(models.cross(ctx.getName() + "_bottom", prov.modLoc("block/" + ctx.getName() + "_bottom"))))
                .partialState().with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER).addModels(new ConfiguredModel(models.cross(ctx.getName() + "_top", prov.modLoc("block/" + ctx.getName() + "_top"))));
    }

    private static void noModelBlock(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider prov, ResourceLocation particle) {
        prov.simpleBlock(ctx.get(), prov.models().getBuilder(ctx.getName()).texture("particle", particle));
    }

    private static void simpleBlockAllRotations(DataGenContext<Block, ? extends Block> ctx, RegistrateBlockstateProvider prov) {
        prov.simpleBlock(ctx.get(), ConfiguredModel.allRotations(prov.cubeAll(ctx.get()), false));
    }

    private static ItemModelBuilder blockSprite(DataGenContext<Item, ? extends ItemLike> ctx, RegistrateItemModelProvider prov) {
        return prov.blockSprite(ctx);
    }

    private static NonNullBiFunction<Block, Item.Properties, BlockItem> itemWithRenderer(Supplier<IClientItemExtensions> properties) {
        return (b, p) -> new BlockItem(b, p) {
            @Override
            public void initializeClient(Consumer<IClientItemExtensions> consumer) {
                consumer.accept(properties.get());
            }
        };
    }

    private static String name(ItemLike item) {
        return item.asItem().builtInRegistryHolder().key().location().getPath();
    }

    private static void extendBlockEntity(BlockEntityType<?> type, Block block) {
        ((BlockEntityTypeAccessor) type).tropicraft$setValidBlocks(ImmutableSet.<Block>builder()
                .addAll(((BlockEntityTypeAccessor) type).tropicraft$getValidBlocks())
                .add(block)
                .build()
        );
    }
}

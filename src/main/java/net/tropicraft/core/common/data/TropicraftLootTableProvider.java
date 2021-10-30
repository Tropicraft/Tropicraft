package net.tropicraft.core.common.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.TableBonus;
import net.minecraft.loot.functions.ApplyBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TikiTorchBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.item.TropicraftItems;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class TropicraftLootTableProvider extends LootTableProvider {

    public TropicraftLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootParameterSets.BLOCK)
        );
    }
    
    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationresults) {
        // Nothing for now, but chest loot tables eventually
    }
    
    private static class Blocks extends BlockLootTables {

        private static final float[] FRUIT_SAPLING_RATES = new float[]{1/10F, 1/8F, 1/6F, 1/5F};
        private static final float[] SAPLING_RATES = new float[]{0.05F, 0.0625F, 0.083333336F, 0.1F};
        private static final float[] RARE_SAPLING_RATES = new float[]{0.025F, 0.027777778F, 0.03125F, 0.041666668F, 0.1F};
        
        @Override
        protected void addTables() {
            dropsSelf(TropicraftBlocks.CHUNK);
            
            // Ores and storage blocks
            dropsOreItem(TropicraftBlocks.AZURITE_ORE, TropicraftItems.AZURITE);
            dropsOreItem(TropicraftBlocks.EUDIALYTE_ORE, TropicraftItems.EUDIALYTE);
            dropsOreItem(TropicraftBlocks.ZIRCON_ORE, TropicraftItems.ZIRCON);
            dropsSelf(TropicraftBlocks.SHAKA_ORE);
            dropsSelf(TropicraftBlocks.MANGANESE_ORE);
            
            dropsSelf(TropicraftBlocks.AZURITE_BLOCK);
            dropsSelf(TropicraftBlocks.EUDIALYTE_BLOCK);
            dropsSelf(TropicraftBlocks.ZIRCON_BLOCK);
            dropsSelf(TropicraftBlocks.ZIRCONIUM_BLOCK);
            dropsSelf(TropicraftBlocks.SHAKA_BLOCK);
            dropsSelf(TropicraftBlocks.MANGANESE_BLOCK);
            
            // All flowers
            TropicraftBlocks.FLOWERS.values().forEach(this::dropsSelf);
            
            // Sands
            dropsSelf(TropicraftBlocks.PURIFIED_SAND);
            dropsSelf(TropicraftBlocks.PACKED_PURIFIED_SAND);
            dropsSelf(TropicraftBlocks.CORAL_SAND);
            dropsSelf(TropicraftBlocks.FOAMY_SAND);
            dropsSelf(TropicraftBlocks.VOLCANIC_SAND);
            dropsSelf(TropicraftBlocks.MINERAL_SAND);

            // Mud
            dropsSelf(TropicraftBlocks.MUD);

            add(TropicraftBlocks.MUD_WITH_PIANGUAS.get(), applyExplosionDecay(TropicraftBlocks.MUD_WITH_PIANGUAS.get(),
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .add(ItemLootEntry.lootTableItem(TropicraftBlocks.MUD_WITH_PIANGUAS.get())
                                            .when(HAS_SILK_TOUCH)
                                            .otherwise(ItemLootEntry.lootTableItem(TropicraftBlocks.MUD.get()))
                                    )
                            )
                            .withPool(LootPool.lootPool()
                                    .when(HAS_NO_SILK_TOUCH)
                                    .add(ItemLootEntry.lootTableItem(TropicraftItems.PIANGUAS.get())
                                            .apply(ApplyBonus.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                    )
                            )
            ));

            // Bundles
            dropsSelf(TropicraftBlocks.BAMBOO_BUNDLE);
            dropsSelf(TropicraftBlocks.THATCH_BUNDLE);
            
            // Planks & Logs
            dropsSelf(TropicraftBlocks.MAHOGANY_PLANKS);
            dropsSelf(TropicraftBlocks.PALM_PLANKS);
            
            dropsSelf(TropicraftBlocks.MAHOGANY_LOG);
            dropsSelf(TropicraftBlocks.PALM_LOG);
            
            dropsSelf(TropicraftBlocks.MAHOGANY_WOOD);
            dropsSelf(TropicraftBlocks.PALM_WOOD);

            dropsSelf(TropicraftBlocks.RED_MANGROVE_LOG);
            dropsSelf(TropicraftBlocks.RED_MANGROVE_WOOD);
            dropsSelf(TropicraftBlocks.RED_MANGROVE_ROOTS);

            dropsSelf(TropicraftBlocks.LIGHT_MANGROVE_LOG);
            dropsSelf(TropicraftBlocks.LIGHT_MANGROVE_WOOD);
            dropsSelf(TropicraftBlocks.LIGHT_MANGROVE_ROOTS);

            dropsSelf(TropicraftBlocks.BLACK_MANGROVE_LOG);
            dropsSelf(TropicraftBlocks.BLACK_MANGROVE_WOOD);
            dropsSelf(TropicraftBlocks.BLACK_MANGROVE_ROOTS);

            dropsSelf(TropicraftBlocks.STRIPPED_MANGROVE_LOG);
            dropsSelf(TropicraftBlocks.STRIPPED_MANGROVE_WOOD);
            dropsSelf(TropicraftBlocks.MANGROVE_PLANKS);

            // Stairs & Slabs
            dropsSelf(TropicraftBlocks.BAMBOO_STAIRS);
            dropsSelf(TropicraftBlocks.THATCH_STAIRS);
            dropsSelf(TropicraftBlocks.CHUNK_STAIRS);
            dropsSelf(TropicraftBlocks.PALM_STAIRS);
            dropsSelf(TropicraftBlocks.MAHOGANY_STAIRS);
            dropsSelf(TropicraftBlocks.THATCH_STAIRS_FUZZY);
            dropsSelf(TropicraftBlocks.MANGROVE_STAIRS);

            slab(TropicraftBlocks.BAMBOO_SLAB);
            slab(TropicraftBlocks.THATCH_SLAB);
            slab(TropicraftBlocks.CHUNK_SLAB);
            slab(TropicraftBlocks.PALM_SLAB);
            slab(TropicraftBlocks.MAHOGANY_SLAB);
            slab(TropicraftBlocks.MANGROVE_SLAB);

            // Leaves
            leaves(TropicraftBlocks.MAHOGANY_LEAVES, TropicraftBlocks.MAHOGANY_SAPLING, RARE_SAPLING_RATES);
            leaves(TropicraftBlocks.PALM_LEAVES, TropicraftBlocks.PALM_SAPLING, SAPLING_RATES);
            leavesNoSapling(TropicraftBlocks.KAPOK_LEAVES); // TODO Should we have kapok as its own actual tree/wood?
            leavesNoSapling(TropicraftBlocks.FRUIT_LEAVES); // TODO Should there be general purpose fruit leaves? This makes saplings pretty rare
            fruitLeaves(TropicraftBlocks.GRAPEFRUIT_LEAVES, TropicraftBlocks.GRAPEFRUIT_SAPLING, TropicraftItems.GRAPEFRUIT);
            fruitLeaves(TropicraftBlocks.LEMON_LEAVES, TropicraftBlocks.LEMON_SAPLING, TropicraftItems.LEMON);
            fruitLeaves(TropicraftBlocks.LIME_LEAVES, TropicraftBlocks.LIME_SAPLING, TropicraftItems.LIME);
            fruitLeaves(TropicraftBlocks.ORANGE_LEAVES, TropicraftBlocks.ORANGE_SAPLING, TropicraftItems.ORANGE);
            leavesNoSapling(TropicraftBlocks.RED_MANGROVE_LEAVES);
            leavesNoSapling(TropicraftBlocks.TALL_MANGROVE_LEAVES);
            leavesNoSapling(TropicraftBlocks.TEA_MANGROVE_LEAVES);
            leavesNoSapling(TropicraftBlocks.BLACK_MANGROVE_LEAVES);

            // Saplings
            dropsSelf(TropicraftBlocks.MAHOGANY_SAPLING);
            dropsSelf(TropicraftBlocks.PALM_SAPLING);
            dropsSelf(TropicraftBlocks.GRAPEFRUIT_SAPLING);
            dropsSelf(TropicraftBlocks.LEMON_SAPLING);
            dropsSelf(TropicraftBlocks.LIME_SAPLING);
            dropsSelf(TropicraftBlocks.ORANGE_SAPLING);
            dropsSelf(TropicraftBlocks.RED_MANGROVE_PROPAGULE);
            dropsSelf(TropicraftBlocks.TALL_MANGROVE_PROPAGULE);
            dropsSelf(TropicraftBlocks.TEA_MANGROVE_PROPAGULE);
            dropsSelf(TropicraftBlocks.BLACK_MANGROVE_PROPAGULE);

            // Fences, Gates, and Walls
            dropsSelf(TropicraftBlocks.BAMBOO_FENCE);
            dropsSelf(TropicraftBlocks.THATCH_FENCE);
            dropsSelf(TropicraftBlocks.CHUNK_FENCE);
            dropsSelf(TropicraftBlocks.PALM_FENCE);
            dropsSelf(TropicraftBlocks.MAHOGANY_FENCE);
            dropsSelf(TropicraftBlocks.MANGROVE_FENCE);

            dropsSelf(TropicraftBlocks.BAMBOO_FENCE_GATE);
            dropsSelf(TropicraftBlocks.THATCH_FENCE_GATE);
            dropsSelf(TropicraftBlocks.CHUNK_FENCE_GATE);
            dropsSelf(TropicraftBlocks.PALM_FENCE_GATE);
            dropsSelf(TropicraftBlocks.MAHOGANY_FENCE_GATE);
            dropsSelf(TropicraftBlocks.MANGROVE_FENCE_GATE);

            dropsSelf(TropicraftBlocks.CHUNK_WALL);

            // Doors and Trapdoors
            doubleBlock(TropicraftBlocks.BAMBOO_DOOR);
            doubleBlock(TropicraftBlocks.THATCH_DOOR);
            doubleBlock(TropicraftBlocks.PALM_DOOR);
            doubleBlock(TropicraftBlocks.MAHOGANY_DOOR);
            doubleBlock(TropicraftBlocks.MANGROVE_DOOR);

            dropsSelf(TropicraftBlocks.BAMBOO_TRAPDOOR);
            dropsSelf(TropicraftBlocks.THATCH_TRAPDOOR);
            dropsSelf(TropicraftBlocks.PALM_TRAPDOOR);
            dropsSelf(TropicraftBlocks.MAHOGANY_TRAPDOOR);
            dropsSelf(TropicraftBlocks.MANGROVE_TRAPDOOR);

            // Misc remaining blocks
            doubleBlock(TropicraftBlocks.IRIS);
            add(TropicraftBlocks.PINEAPPLE.get(), b -> droppingChunks(b, TropicraftItems.PINEAPPLE_CUBES,
                BlockStateProperty.hasBlockStateProperties(b).setProperties(
                        StatePropertiesPredicate.Builder.properties().hasProperty(
                                DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))));

            dropsSelf(TropicraftBlocks.REEDS);

            dropsSelf(TropicraftBlocks.SMALL_BONGO_DRUM);
            dropsSelf(TropicraftBlocks.MEDIUM_BONGO_DRUM);
            dropsSelf(TropicraftBlocks.LARGE_BONGO_DRUM);
            
            dropsSelf(TropicraftBlocks.BAMBOO_LADDER);

            dropsSelf(TropicraftBlocks.BAMBOO_BOARDWALK);
            dropsSelf(TropicraftBlocks.PALM_BOARDWALK);
            dropsSelf(TropicraftBlocks.MAHOGANY_BOARDWALK);
            dropsSelf(TropicraftBlocks.MANGROVE_BOARDWALK);

            dropsSelf(TropicraftBlocks.BAMBOO_CHEST);
            dropsSelf(TropicraftBlocks.SIFTER);
            dropsSelf(TropicraftBlocks.DRINK_MIXER);
            dropsSelf(TropicraftBlocks.AIR_COMPRESSOR);

            add(TropicraftBlocks.TIKI_TORCH.get(), b -> createSinglePropConditionTable(b, TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.UPPER));
            
            add(TropicraftBlocks.COCONUT.get(), b -> droppingChunks(b, TropicraftItems.COCONUT_CHUNK));
            
            dropsSelf(TropicraftBlocks.BAMBOO_FLOWER_POT);
            TropicraftBlocks.ALL_POTTED_PLANTS.forEach(ro -> add(ro.get(), b -> droppingFlowerPotAndFlower((FlowerPotBlock) b)));

            add(TropicraftBlocks.COFFEE_BUSH.get(), dropNumberOfItems(TropicraftBlocks.COFFEE_BUSH.get(), TropicraftItems.RAW_COFFEE_BEAN, 1, 3));

            dropsSelf(TropicraftBlocks.GOLDEN_LEATHER_FERN);
            dropsOther(TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN, TropicraftBlocks.GOLDEN_LEATHER_FERN);
            dropsOther(TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN, TropicraftBlocks.GOLDEN_LEATHER_FERN);
        }
        
        private void dropsSelf(Supplier<? extends Block> block) {
            dropSelf(block.get());
        }

        private void dropsOther(Supplier<? extends Block> block, Supplier<? extends IItemProvider> drops) {
            dropOther(block.get(), drops.get());
        }
        
        private void dropsOreItem(Supplier<? extends Block> block, Supplier<? extends IItemProvider> item) {
            add(block.get(), b -> createOreDrop(b, item.get().asItem()));
        }
        
        private void slab(Supplier<? extends SlabBlock> block) {
            add(block.get(), BlockLootTables::createSlabItemTable);
        }

        private void leaves(Supplier<? extends LeavesBlock> block, Supplier<? extends SaplingBlock> sapling, float[] rates) {
            add(block.get(), b -> createLeavesDrops(b, sapling.get(), rates));
        }
        
        private void leavesNoSapling(Supplier<? extends LeavesBlock> block) {
            add(block.get(), Blocks::droppingWithSticks);
        }
        
        private void fruitLeaves(Supplier<? extends LeavesBlock> block, Supplier<? extends SaplingBlock> sapling, Supplier<? extends IItemProvider> fruit) {
            add(block.get(), b -> droppingWithChancesSticksAndFruit(b, sapling.get(), fruit.get(), FRUIT_SAPLING_RATES));
        }
        
        protected static LootTable.Builder onlyWithSilkTouchOrShears(Block block) {
            return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SHEARS_OR_SILK_TOUCH).setRolls(ConstantRange.exactly(1)).add(ItemLootEntry.lootTableItem(block)));
        }
        
        protected static LootTable.Builder droppingWithSticks(Block block) {
            return onlyWithSilkTouchOrShears(block).withPool(LootPool.lootPool()
                        .setRolls(ConstantRange.exactly(1))
                        .when(HAS_NO_SHEARS_OR_SILK_TOUCH)
                        .add(applyExplosionDecay(block, ItemLootEntry.lootTableItem(Items.STICK)
                                .apply(SetCount.setCount(RandomValueRange.between(1.0F, 2.0F))))
                                .when(TableBonus.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
        }
        
        protected static LootTable.Builder droppingWithChancesSticksAndFruit(Block block, Block sapling, IItemProvider fruit, float[] chances) {
            return createLeavesDrops(block, sapling, chances)
                    .withPool(LootPool.lootPool().setRolls(ConstantRange.exactly(1))
                            .when(HAS_NO_SHEARS_OR_SILK_TOUCH)
                            .add(applyExplosionDecay(block, ItemLootEntry.lootTableItem(fruit))));
        }
        
        private void doubleBlock(Supplier<? extends Block> block) {
            add(block.get(), b -> createSinglePropConditionTable(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
        }

        // Same as droppingAndFlowerPot but with variable flower pot item
        protected static LootTable.Builder droppingFlowerPotAndFlower(FlowerPotBlock fullPot) {
            return LootTable.lootTable().withPool(applyExplosionCondition(fullPot.getEmptyPot(), LootPool.lootPool()
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(fullPot.getEmptyPot()))))
                    .withPool(applyExplosionCondition(fullPot.getContent(), LootPool.lootPool()
                            .setRolls(ConstantRange.exactly(1))
                            .add(ItemLootEntry.lootTableItem(fullPot.getContent()))));
        }
        
        private static LootPool.Builder droppingChunksPool(Block block, Supplier<? extends IItemProvider> chunk) {
            return LootPool.lootPool().add(ItemLootEntry.lootTableItem(chunk.get())
                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(TropicraftTags.Items.SWORDS)))
                    .apply(SetCount.setCount(RandomValueRange.between(1.0F, 4.0F)))
                    .otherwise(applyExplosionCondition(block, ItemLootEntry.lootTableItem(block))));
        }
        
        protected static LootTable.Builder droppingChunks(Block block, Supplier<? extends IItemProvider> chunk) {
            return LootTable.lootTable().withPool(droppingChunksPool(block, chunk));
        }
        
        protected static LootTable.Builder droppingChunks(Block block, Supplier<? extends IItemProvider> chunk, ILootCondition.IBuilder condition) {
            return LootTable.lootTable().withPool(droppingChunksPool(block, chunk).when(condition));
        }

        private static LootTable.Builder dropNumberOfItems(Block block, Supplier<? extends IItemProvider> drop, final int minDrops, final int maxDrops) {
            return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                    .add(ItemLootEntry.lootTableItem(drop.get()))
                        .apply(SetCount.setCount(RandomValueRange.between(minDrops, maxDrops)))));
        }
        
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return TropicraftBlocks.BLOCKS.getEntries().stream()
                    .map(Supplier::get)
                    .filter(block -> {
                        ResourceLocation lootTable = block.getLootTable();
                        return lootTable != null && lootTable.getNamespace().equals(Constants.MODID);
                    })
                    .collect(Collectors.toList());
        }
    }
}

package net.tropicraft.core.common.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.tropicraft.Constants;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.PapayaBlock;
import net.tropicraft.core.common.block.TikiTorchBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;
import net.tropicraft.core.common.item.RecordMusic;
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
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(Pair.of(Blocks::new, LootContextParamSets.BLOCK),
                Pair.of(Entities::new, LootContextParamSets.ENTITY));
    }
    
    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationresults) {
        // Nothing for now, but chest loot tables eventually
    }

    private static class Entities extends EntityLoot{
        @Override
        protected void addTables(){
            this.add(TropicraftEntities.TROPI_CREEPER.get(),
                    LootTable.lootTable().withPool(LootPool.lootPool().add(LootItem.lootTableItem(TropicraftItems.MUSIC_DISCS.get(RecordMusic.EASTERN_ISLES).get())
                            .when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER,
                                    EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS))))));
            dropItemsWithEnchantBonus(TropicraftEntities.IGUANA, TropicraftItems.IGUANA_LEATHER, TropicraftItems.SCALE,
                    ConstantValue.exactly(3));
            this.add(TropicraftEntities.TROPI_SKELLY.get(),
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                            .add(LootItem.lootTableItem(Items.BONE).apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                                    .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))));
            dropItemsWithEnchantBonus(TropicraftEntities.EIH, TropicraftBlocks.CHUNK.get().asItem(), ConstantValue.exactly(3));
            dropItem(TropicraftEntities.SEA_TURTLE, TropicraftItems.TURTLE_SHELL);
            dropItemsWithEnchantBonus(TropicraftEntities.MARLIN, TropicraftItems.FRESH_MARLIN, UniformGenerator.between(1, 3));
            dropItemsWithEnchantBonus(TropicraftEntities.FAILGULL, Items.FEATHER, UniformGenerator.between(1, 2));
            dropItemsWithEnchantBonus(TropicraftEntities.DOLPHIN, TropicraftItems.TROPICAL_FERTILIZER,
                    UniformGenerator.between(1, 3));
            noDrops(TropicraftEntities.SEAHORSE);
            this.add(TropicraftEntities.TREE_FROG.get(),
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1)).when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.THIS,
                                            EntityPredicate.Builder.entity().flags(new EntityFlagsPredicate(null, null, null, null, false))
                                                    .nbt(new NbtPredicate(Util.make(new CompoundTag(), c -> c.putInt("Type", TreeFrogEntity.Type.GREEN.ordinal()))))))
                                    .add(LootItem.lootTableItem(TropicraftItems.POISON_FROG_SKIN.get())))
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                                    .add(LootItem.lootTableItem(TropicraftItems.FROG_LEG.get()))));
            dropItem(TropicraftEntities.SEA_URCHIN, TropicraftItems.SEA_URCHIN_ROE);
            noDrops(TropicraftEntities.SEA_URCHIN_EGG_ENTITY);
            dropItem(TropicraftEntities.STARFISH, TropicraftItems.STARFISH);
            noDrops(TropicraftEntities.STARFISH_EGG);
            noDrops(TropicraftEntities.V_MONKEY);
            noDrops(TropicraftEntities.RIVER_SARDINE);
            dropItem(TropicraftEntities.RIVER_SARDINE, TropicraftItems.RAW_FISH);
            dropItem(TropicraftEntities.PIRANHA, TropicraftItems.RAW_FISH);
            dropItem(TropicraftEntities.TROPICAL_FISH, TropicraftItems.RAW_FISH);
            dropItem(TropicraftEntities.EAGLE_RAY, TropicraftItems.RAW_RAY);
            this.add(TropicraftEntities.TROPI_SPIDER.get(),
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.STRING)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
                            .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))))
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(Items.SPIDER_EYE)
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(-1.0F, 1.0F)))
                            .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F))))
                            .when(LootItemKilledByPlayerCondition.killedByPlayer())));
            noDrops(TropicraftEntities.TROPI_SPIDER_EGG);
            noDrops(TropicraftEntities.ASHEN);
            dropItemsWithEnchantBonus(TropicraftEntities.HAMMERHEAD, TropicraftItems.TROPICAL_FERTILIZER,
                    UniformGenerator.between(1, 3));
            noDrops(TropicraftEntities.SEA_TURTLE_EGG);
            noDrops(TropicraftEntities.TROPI_BEE);
            noDrops(TropicraftEntities.COWKTAIL);
            dropItemsWithEnchantBonus(TropicraftEntities.MAN_O_WAR, Items.SLIME_BALL, UniformGenerator.between(1, 2));
            noDrops(TropicraftEntities.TAPIR);
            noDrops(TropicraftEntities.JAGUAR);
            dropItemsWithEnchantBonus(TropicraftEntities.BROWN_BASILISK_LIZARD, TropicraftItems.SCALE, UniformGenerator.between(1,2));
            dropItemsWithEnchantBonus(TropicraftEntities.GREEN_BASILISK_LIZARD, TropicraftItems.SCALE, UniformGenerator.between(1,2));
            dropItemsWithEnchantBonus(TropicraftEntities.HUMMINGBIRD, Items.FEATHER, UniformGenerator.between(1, 2));
            noDrops(TropicraftEntities.FIDDLER_CRAB);
            noDrops(TropicraftEntities.SPIDER_MONKEY);
            noDrops(TropicraftEntities.WHITE_LIPPED_PECCARY);
            noDrops(TropicraftEntities.CUBERA);
        }

        // Drops a single item, not affected by enchantment, and several other items
        // that are affected by Enchantment
        // Looting will at most double yield with Looting III
        public <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistryObject<EntityType<T>> entity, RegistryObject<Item> loot, RegistryObject<Item> multiLoot, NumberProvider range) {
            this.add(entity.get(), LootTable.lootTable()
                    .withPool(
                            LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(loot.get())))
                    .withPool(LootPool.lootPool().setRolls(range).add(LootItem.lootTableItem(multiLoot.get())
                            .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0F, 1F / 3F))))));
        }

        // Just drops a single item, not affected by enchantment
        public <T extends LivingEntity> void dropItem(RegistryObject<EntityType<T>> entity, RegistryObject<Item> loot) {
            this.add(entity.get(), LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(loot.get()))));
        }

        public <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistryObject<EntityType<T>> entity, RegistryObject<Item> loot, NumberProvider range) {
            dropItemsWithEnchantBonus(entity, loot.get(), range);
        }

        // TODO: MAY BE INCORRECT IN PORTING THIS METHOD
        // Drops several items that are affected by Enchantment
        // Looting will at most double yield with Looting III
        public <T extends LivingEntity> void dropItemsWithEnchantBonus(RegistryObject<EntityType<T>> entity, Item loot, NumberProvider range) {
            this.add(entity.get(),
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(range).add(LootItem.lootTableItem(loot)
                            .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0F, 1F / 3F))))));
        }

        public <T extends LivingEntity> void noDrops(RegistryObject<EntityType<T>> entity) {
            this.add(entity.get(), LootTable.lootTable());
        }

        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
            return TropicraftEntities.ENTITIES.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }



    private static class Blocks extends BlockLoot {

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
                                    .add(LootItem.lootTableItem(TropicraftBlocks.MUD_WITH_PIANGUAS.get())
                                            .when(HAS_SILK_TOUCH)
                                            .otherwise(LootItem.lootTableItem(TropicraftBlocks.MUD.get()))
                                    )
                            )
                            .withPool(LootPool.lootPool()
                                    .when(HAS_NO_SILK_TOUCH)
                                    .add(LootItem.lootTableItem(TropicraftItems.PIANGUAS.get())
                                            .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
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

            dropsSelf(TropicraftBlocks.PAPAYA_LOG);
            dropsSelf(TropicraftBlocks.PAPAYA_WOOD);

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
            leaves(TropicraftBlocks.PAPAYA_LEAVES, TropicraftBlocks.PAPAYA_SAPLING, SAPLING_RATES);

            // Saplings
            dropsSelf(TropicraftBlocks.MAHOGANY_SAPLING);
            dropsSelf(TropicraftBlocks.PALM_SAPLING);
            dropsSelf(TropicraftBlocks.GRAPEFRUIT_SAPLING);
            dropsSelf(TropicraftBlocks.LEMON_SAPLING);
            dropsSelf(TropicraftBlocks.LIME_SAPLING);
            dropsSelf(TropicraftBlocks.ORANGE_SAPLING);
            dropsSelf(TropicraftBlocks.PAPAYA_SAPLING);
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
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(b).setProperties(
                        StatePropertiesPredicate.Builder.properties().hasProperty(
                                DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))));

            dropsSelf(TropicraftBlocks.REEDS);


            add(TropicraftBlocks.MUD_WITH_PIANGUAS.get(), applyExplosionDecay(TropicraftBlocks.MUD_WITH_PIANGUAS.get(),
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .add(LootItem.lootTableItem(TropicraftBlocks.MUD_WITH_PIANGUAS.get())
                                            .when(HAS_SILK_TOUCH)
                                            .otherwise(LootItem.lootTableItem(TropicraftBlocks.MUD.get()))
                                    )
                            )
                            .withPool(LootPool.lootPool()
                                    .when(HAS_NO_SILK_TOUCH)
                                    .add(LootItem.lootTableItem(TropicraftItems.PIANGUAS.get())
                                            .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                                    )
                            )
            ));

            add(TropicraftBlocks.PAPAYA.get(), b -> {
                    return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(applyExplosionCondition(b, LootItem.lootTableItem(TropicraftBlocks.PAPAYA.get().asItem())
                                            .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)))
                                                .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(TropicraftBlocks.PAPAYA.get()).setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(PapayaBlock.AGE, 1))))
                                    ));
                });

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

        private void dropsOther(Supplier<? extends Block> block, Supplier<? extends ItemLike> drops) {
            dropOther(block.get(), drops.get());
        }
        
        private void dropsOreItem(Supplier<? extends Block> block, Supplier<? extends ItemLike> item) {
            add(block.get(), b -> createOreDrop(b, item.get().asItem()));
        }
        
        private void slab(Supplier<? extends SlabBlock> block) {
            add(block.get(), BlockLoot::createSlabItemTable);
        }

        private void leaves(Supplier<? extends LeavesBlock> block, Supplier<? extends SaplingBlock> sapling, float[] rates) {
            add(block.get(), b -> createLeavesDrops(b, sapling.get(), rates));
        }
        
        private void leavesNoSapling(Supplier<? extends LeavesBlock> block) {
            add(block.get(), Blocks::droppingWithSticks);
        }
        
        private void fruitLeaves(Supplier<? extends LeavesBlock> block, Supplier<? extends SaplingBlock> sapling, Supplier<? extends ItemLike> fruit) {
            add(block.get(), b -> droppingWithChancesSticksAndFruit(b, sapling.get(), fruit.get(), FRUIT_SAPLING_RATES));
        }
        
        protected static LootTable.Builder onlyWithSilkTouchOrShears(Block block) {
            return LootTable.lootTable().withPool(LootPool.lootPool().when(HAS_SHEARS_OR_SILK_TOUCH).setRolls(ConstantValue.exactly(1)).add(LootItem.lootTableItem(block)));
        }
        
        protected static LootTable.Builder droppingWithSticks(Block block) {
            return onlyWithSilkTouchOrShears(block).withPool(LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1))
                        .when(HAS_NO_SHEARS_OR_SILK_TOUCH)
                        .add(applyExplosionDecay(block, LootItem.lootTableItem(Items.STICK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F))))
                                .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
        }
        
        protected static LootTable.Builder droppingWithChancesSticksAndFruit(Block block, Block sapling, ItemLike fruit, float[] chances) {
            return createLeavesDrops(block, sapling, chances)
                    .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .when(HAS_NO_SHEARS_OR_SILK_TOUCH)
                            .add(applyExplosionDecay(block, LootItem.lootTableItem(fruit))));
        }
        
        private void doubleBlock(Supplier<? extends Block> block) {
            add(block.get(), b -> createSinglePropConditionTable(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
        }

        // Same as droppingAndFlowerPot but with variable flower pot item
        protected static LootTable.Builder droppingFlowerPotAndFlower(FlowerPotBlock fullPot) {
            return LootTable.lootTable().withPool(applyExplosionCondition(fullPot.getEmptyPot(), LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(fullPot.getEmptyPot()))))
                    .withPool(applyExplosionCondition(fullPot.getContent(), LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(fullPot.getContent()))));
        }
        
        private static LootPool.Builder droppingChunksPool(Block block, Supplier<? extends ItemLike> chunk) {
            return LootPool.lootPool().add(LootItem.lootTableItem(chunk.get())
                    .when(MatchTool.toolMatches(ItemPredicate.Builder.item().of(TropicraftTags.Items.SWORDS)))
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 4.0F)))
                    .otherwise(applyExplosionCondition(block, LootItem.lootTableItem(block))));
        }
        
        protected static LootTable.Builder droppingChunks(Block block, Supplier<? extends ItemLike> chunk) {
            return LootTable.lootTable().withPool(droppingChunksPool(block, chunk));
        }
        
        protected static LootTable.Builder droppingChunks(Block block, Supplier<? extends ItemLike> chunk, LootItemCondition.Builder condition) {
            return LootTable.lootTable().withPool(droppingChunksPool(block, chunk).when(condition));
        }

        private static LootTable.Builder dropNumberOfItems(Block block, Supplier<? extends ItemLike> drop, final int minDrops, final int maxDrops) {
            return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                    .add(LootItem.lootTableItem(drop.get()))
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))));
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

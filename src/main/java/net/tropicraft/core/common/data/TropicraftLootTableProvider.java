package net.tropicraft.core.common.data;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;

import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import net.minecraft.world.storage.loot.ConstantRange;
import net.minecraft.world.storage.loot.IRandomRange;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSet;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTable.Builder;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.ValidationTracker;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.conditions.MatchTool;
import net.minecraft.world.storage.loot.conditions.TableBonus;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.core.common.TropicraftTags;
import net.tropicraft.core.common.block.TikiTorchBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.entity.TropicraftEntities;
import net.tropicraft.core.common.entity.neutral.TreeFrogEntity;
import net.tropicraft.core.common.item.RecordMusic;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropicraftLootTableProvider extends LootTableProvider {

    public TropicraftLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(Blocks::new, LootParameterSets.BLOCK),
                Pair.of(Entities::new, LootParameterSets.ENTITY)
        );
    }
    
    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationresults) {
        // Nothing for now, but chest loot tables eventually
    }
    
    private static class Entities extends EntityLootTables {
    	@Override
    	protected void addTables() {

    		registerCreeperLoot();
			registerEntityLoot(TropicraftEntities.IGUANA, TropicraftItems.IGUANA_LEATHER, TropicraftItems.SCALE, ConstantRange.of(3));
			registerMinimalLootTable(TropicraftEntities.TROPI_SKELLY);
			registerEntityLoot(TropicraftEntities.EIH, TropicraftBlocks.CHUNK.get().asItem(), ConstantRange.of(3));
			registerEntityLoot(TropicraftEntities.SEA_TURTLE, TropicraftItems.TURTLE_SHELL);
			registerEntityLoot(TropicraftEntities.MARLIN, TropicraftItems.FRESH_MARLIN, RandomValueRange.of(1, 3));
			registerMinimalLootTable(TropicraftEntities.FAILGULL);
			registerEntityLoot(TropicraftEntities.DOLPHIN, TropicraftItems.TROPICAL_FERTILIZER, RandomValueRange.of(1, 3));
			registerMinimalLootTable(TropicraftEntities.SEAHORSE);
			registerFrogLoot();
			registerEntityLoot(TropicraftEntities.SEA_URCHIN, TropicraftItems.SEA_URCHIN_ROE);
			registerMinimalLootTable(TropicraftEntities.SEA_URCHIN_EGG_ENTITY);
			registerEntityLoot(TropicraftEntities.STARFISH, TropicraftItems.STARFISH);
			registerMinimalLootTable(TropicraftEntities.STARFISH_EGG);
			registerMinimalLootTable(TropicraftEntities.V_MONKEY);
			registerMinimalLootTable(TropicraftEntities.RIVER_SARDINE);
			registerEntityLoot(TropicraftEntities.RIVER_SARDINE, TropicraftItems.RAW_FISH);
			registerEntityLoot(TropicraftEntities.PIRANHA, TropicraftItems.RAW_FISH);
			registerEntityLoot(TropicraftEntities.TROPICAL_FISH, TropicraftItems.RAW_FISH);
			registerEntityLoot(TropicraftEntities.EAGLE_RAY, TropicraftItems.RAW_RAY);
			registerMinimalLootTable(TropicraftEntities.TROPI_SPIDER);
			registerMinimalLootTable(TropicraftEntities.TROPI_SPIDER_EGG);
			registerMinimalLootTable(TropicraftEntities.ASHEN);
			registerEntityLoot(TropicraftEntities.HAMMERHEAD, TropicraftItems.TROPICAL_FERTILIZER, RandomValueRange.of(1, 3));
			registerMinimalLootTable(TropicraftEntities.SEA_TURTLE_EGG);
			registerMinimalLootTable(TropicraftEntities.TROPI_BEE);
			registerMinimalLootTable(TropicraftEntities.COWKTAIL);
			registerEntityLoot(TropicraftEntities.MAN_O_WAR, Items.SLIME_BALL, RandomValueRange.of(3, 4));
}

		//Only Green frogs should drop poison skins!
    	private static final ILootCondition.IBuilder GREEN = 
    			EntityHasProperty.builder(LootContext.EntityTarget.THIS, EntityPredicate.Builder.create()
    			    .flags(new EntityFlagsPredicate(null, null, null, null, false))
    			    .nbt(new NBTPredicate(Util.make(new CompoundNBT(), c -> c.putInt("Type", TreeFrogEntity.Type.GREEN.ordinal())))));
    	public void registerFrogLoot() {
			this.registerLootTable(TropicraftEntities.TREE_FROG.get(), LootTable.builder()
				.addLootPool(LootPool.builder().rolls(ConstantRange.of(1)).acceptCondition(GREEN)
					.addEntry(ItemLootEntry.builder(TropicraftItems.POISON_FROG_SKIN.get())))
				.addLootPool(LootPool.builder().rolls(ConstantRange.of(2))
							.addEntry(ItemLootEntry.builder(TropicraftItems.FROG_LEG.get())))
			);
		}

    	public void registerCreeperLoot() {
    		this.registerLootTable(TropicraftEntities.TROPI_CREEPER.get(), LootTable.builder().addLootPool(LootPool.builder().addEntry(ItemLootEntry.builder(TropicraftItems.MUSIC_DISCS.get(RecordMusic.EASTERN_ISLES).get())).acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.create().type(EntityTypeTags.SKELETONS)))));
		}
    	
    	//Drops a single item, not affected by enchantment, and several other items that are affected by Enchantment
    	//Looting will at most double yield with Looting III
		public <T extends LivingEntity> void registerEntityLoot(RegistryObject<EntityType<T>> entity, RegistryObject<Item> loot,
				RegistryObject<Item> multiLoot, IRandomRange range) {
			this.registerLootTable(entity.get(), LootTable.builder()
			.addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
					.addEntry(ItemLootEntry.builder(loot.get())))
			.addLootPool(LootPool.builder().rolls(range)
					.addEntry(ItemLootEntry.builder(multiLoot.get())
							.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0F, 1F / 3F))))));
		}

		//Just drops a single item, not affected by enchantment
		public <T extends LivingEntity> void registerEntityLoot(RegistryObject<EntityType<T>> entity, RegistryObject<Item> loot) {
			this.registerLootTable(entity.get(), LootTable.builder()
					.addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
							.addEntry(ItemLootEntry.builder(loot.get()))));
		}

		public <T extends LivingEntity> void registerEntityLoot(RegistryObject<EntityType<T>> entity, RegistryObject<Item> loot, IRandomRange range) {
			registerEntityLoot(entity, loot.get(), range);
		}

    	//Drops several items that are affected by Enchantment
    	//Looting will at most double yield with Looting III
		public <T extends LivingEntity> void registerEntityLoot(RegistryObject<EntityType<T>> entity, Item loot, IRandomRange range) {
			this.registerLootTable(entity.get(), LootTable.builder()
					.addLootPool(LootPool.builder().rolls(range)
							.addEntry(ItemLootEntry.builder(loot)
									.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0F, 1F / 3F))))));
		}
		
		public <T extends LivingEntity> void registerMinimalLootTable(RegistryObject<EntityType<T>> entity) {
			this.registerLootTable(entity.get(), LootTable.builder());
		}
		
        @Override
        protected Iterable<EntityType<?>> getKnownEntities() {
        	return TropicraftEntities.ENTITIES.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
        
        
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
            
            // Stairs & Slabs
            dropsSelf(TropicraftBlocks.BAMBOO_STAIRS);
            dropsSelf(TropicraftBlocks.THATCH_STAIRS);
            dropsSelf(TropicraftBlocks.CHUNK_STAIRS);
            dropsSelf(TropicraftBlocks.PALM_STAIRS);
            dropsSelf(TropicraftBlocks.MAHOGANY_STAIRS);
            dropsSelf(TropicraftBlocks.THATCH_STAIRS_FUZZY);
            
            slab(TropicraftBlocks.BAMBOO_SLAB);
            slab(TropicraftBlocks.THATCH_SLAB);
            slab(TropicraftBlocks.CHUNK_SLAB);
            slab(TropicraftBlocks.PALM_SLAB);
            slab(TropicraftBlocks.MAHOGANY_SLAB);
            
            // Leaves
            leaves(TropicraftBlocks.MAHOGANY_LEAVES, TropicraftBlocks.MAHOGANY_SAPLING, RARE_SAPLING_RATES);
            leaves(TropicraftBlocks.PALM_LEAVES, TropicraftBlocks.PALM_SAPLING, SAPLING_RATES);
            leavesNoSapling(TropicraftBlocks.KAPOK_LEAVES); // TODO Should we have kapok as its own actual tree/wood?
            leavesNoSapling(TropicraftBlocks.FRUIT_LEAVES); // TODO Should there be general purpose fruit leaves? This makes saplings pretty rare
            fruitLeaves(TropicraftBlocks.GRAPEFRUIT_LEAVES, TropicraftBlocks.GRAPEFRUIT_SAPLING, TropicraftItems.GRAPEFRUIT);
            fruitLeaves(TropicraftBlocks.LEMON_LEAVES, TropicraftBlocks.LEMON_SAPLING, TropicraftItems.LEMON);
            fruitLeaves(TropicraftBlocks.LIME_LEAVES, TropicraftBlocks.LIME_SAPLING, TropicraftItems.LIME);
            fruitLeaves(TropicraftBlocks.ORANGE_LEAVES, TropicraftBlocks.ORANGE_SAPLING, TropicraftItems.ORANGE);
            
            // Saplings
            dropsSelf(TropicraftBlocks.MAHOGANY_SAPLING);
            dropsSelf(TropicraftBlocks.PALM_SAPLING);
            dropsSelf(TropicraftBlocks.GRAPEFRUIT_SAPLING);
            dropsSelf(TropicraftBlocks.LEMON_SAPLING);
            dropsSelf(TropicraftBlocks.LIME_SAPLING);
            dropsSelf(TropicraftBlocks.ORANGE_SAPLING);
            
            // Fences, Gates, and Walls
            dropsSelf(TropicraftBlocks.BAMBOO_FENCE);
            dropsSelf(TropicraftBlocks.THATCH_FENCE);
            dropsSelf(TropicraftBlocks.CHUNK_FENCE);
            dropsSelf(TropicraftBlocks.PALM_FENCE);
            dropsSelf(TropicraftBlocks.MAHOGANY_FENCE);
            
            dropsSelf(TropicraftBlocks.BAMBOO_FENCE_GATE);
            dropsSelf(TropicraftBlocks.THATCH_FENCE_GATE);
            dropsSelf(TropicraftBlocks.CHUNK_FENCE_GATE);
            dropsSelf(TropicraftBlocks.PALM_FENCE_GATE);
            dropsSelf(TropicraftBlocks.MAHOGANY_FENCE_GATE);

            dropsSelf(TropicraftBlocks.CHUNK_WALL);

            // Doors and Trapdoors
            doubleBlock(TropicraftBlocks.BAMBOO_DOOR);
            doubleBlock(TropicraftBlocks.THATCH_DOOR);
            doubleBlock(TropicraftBlocks.PALM_DOOR);
            doubleBlock(TropicraftBlocks.MAHOGANY_DOOR);
            
            dropsSelf(TropicraftBlocks.BAMBOO_TRAPDOOR);
            dropsSelf(TropicraftBlocks.THATCH_TRAPDOOR);
            dropsSelf(TropicraftBlocks.PALM_TRAPDOOR);
            dropsSelf(TropicraftBlocks.MAHOGANY_TRAPDOOR);
            
            // Misc remaining blocks
            doubleBlock(TropicraftBlocks.IRIS);
            registerLootTable(TropicraftBlocks.PINEAPPLE.get(), b -> droppingChunks(b, TropicraftItems.PINEAPPLE_CUBES,
                BlockStateProperty.builder(b).fromProperties(
                        StatePropertiesPredicate.Builder.newBuilder().withProp(
                                DoublePlantBlock.HALF, DoubleBlockHalf.UPPER))));
            
            dropsSelf(TropicraftBlocks.SMALL_BONGO_DRUM);
            dropsSelf(TropicraftBlocks.MEDIUM_BONGO_DRUM);
            dropsSelf(TropicraftBlocks.LARGE_BONGO_DRUM);
            
            dropsSelf(TropicraftBlocks.BAMBOO_LADDER);
            
            dropsSelf(TropicraftBlocks.BAMBOO_CHEST);
            dropsSelf(TropicraftBlocks.SIFTER);
            dropsSelf(TropicraftBlocks.DRINK_MIXER);
            dropsSelf(TropicraftBlocks.AIR_COMPRESSOR);

            registerLootTable(TropicraftBlocks.TIKI_TORCH.get(), b -> droppingWhen(b, TikiTorchBlock.SECTION, TikiTorchBlock.TorchSection.UPPER));
            
            registerLootTable(TropicraftBlocks.COCONUT.get(), b -> droppingChunks(b, TropicraftItems.COCONUT_CHUNK));
            
            dropsSelf(TropicraftBlocks.BAMBOO_FLOWER_POT);
            TropicraftBlocks.ALL_POTTED_PLANTS.forEach(ro -> registerLootTable(ro.get(), b -> droppingFlowerPotAndFlower((FlowerPotBlock) b)));

            registerLootTable(TropicraftBlocks.COFFEE_BUSH.get(), dropNumberOfItems(TropicraftBlocks.COFFEE_BUSH.get(), TropicraftItems.RAW_COFFEE_BEAN, 1, 3));
        }
        
        private void dropsSelf(Supplier<? extends Block> block) {
            registerDropSelfLootTable(block.get());
        }
        
        private void dropsOreItem(Supplier<? extends Block> block, Supplier<? extends IItemProvider> item) {
            registerLootTable(block.get(), b -> droppingItemWithFortune(b, item.get().asItem()));
        }
        
        private void slab(Supplier<? extends SlabBlock> block) {
            registerLootTable(block.get(), BlockLootTables::droppingSlab);
        }

        private void leaves(Supplier<? extends LeavesBlock> block, Supplier<? extends SaplingBlock> sapling, float[] rates) {
            registerLootTable(block.get(), b -> droppingWithChancesAndSticks(b, sapling.get(), rates));
        }
        
        private void leavesNoSapling(Supplier<? extends LeavesBlock> block) {
            registerLootTable(block.get(), Blocks::droppingWithSticks);
        }
        
        private void fruitLeaves(Supplier<? extends LeavesBlock> block, Supplier<? extends SaplingBlock> sapling, Supplier<? extends IItemProvider> fruit) {
            registerLootTable(block.get(), b -> droppingWithChancesSticksAndFruit(b, sapling.get(), fruit.get(), FRUIT_SAPLING_RATES));
        }
        
        protected static LootTable.Builder onlyWithSilkTouchOrShears(Block block) {
            return LootTable.builder().addLootPool(LootPool.builder().acceptCondition(SILK_TOUCH_OR_SHEARS).rolls(ConstantRange.of(1)).addEntry(ItemLootEntry.builder(block)));
        }
        
        protected static LootTable.Builder droppingWithSticks(Block block) {
            return onlyWithSilkTouchOrShears(block).addLootPool(LootPool.builder()
                        .rolls(ConstantRange.of(1))
                        .acceptCondition(NOT_SILK_TOUCH_OR_SHEARS)
                        .addEntry(withExplosionDecay(block, ItemLootEntry.builder(Items.STICK)
                                .acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 2.0F))))
                                .acceptCondition(TableBonus.builder(Enchantments.FORTUNE, 0.02F, 0.022222223F, 0.025F, 0.033333335F, 0.1F))));
        }
        
        protected static LootTable.Builder droppingWithChancesSticksAndFruit(Block block, Block sapling, IItemProvider fruit, float[] chances) {
            return droppingWithChancesAndSticks(block, sapling, chances)
                    .addLootPool(LootPool.builder().rolls(ConstantRange.of(1))
                            .acceptCondition(NOT_SILK_TOUCH_OR_SHEARS)
                            .addEntry(withExplosionDecay(block, ItemLootEntry.builder(fruit))));
        }
        
        private void doubleBlock(Supplier<? extends Block> block) {
            registerLootTable(block.get(), b -> droppingWhen(b, DoorBlock.HALF, DoubleBlockHalf.LOWER));
        }

        // Same as droppingAndFlowerPot but with variable flower pot item
        protected static LootTable.Builder droppingFlowerPotAndFlower(FlowerPotBlock fullPot) {
            return LootTable.builder().addLootPool(withSurvivesExplosion(fullPot.getEmptyPot(), LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(fullPot.getEmptyPot()))))
                    .addLootPool(withSurvivesExplosion(fullPot.func_220276_d(), LootPool.builder()
                            .rolls(ConstantRange.of(1))
                            .addEntry(ItemLootEntry.builder(fullPot.func_220276_d()))));
        }
        
        private static LootPool.Builder droppingChunksPool(Block block, Supplier<? extends IItemProvider> chunk) {
            return LootPool.builder().addEntry(ItemLootEntry.builder(chunk.get())
                    .acceptCondition(MatchTool.builder(ItemPredicate.Builder.create().tag(TropicraftTags.Items.SWORDS)))
                    .acceptFunction(SetCount.builder(RandomValueRange.of(1.0F, 4.0F)))
                    .alternatively(withSurvivesExplosion(block, ItemLootEntry.builder(block))));
        }
        
        protected static LootTable.Builder droppingChunks(Block block, Supplier<? extends IItemProvider> chunk) {
            return LootTable.builder().addLootPool(droppingChunksPool(block, chunk));
        }
        
        protected static LootTable.Builder droppingChunks(Block block, Supplier<? extends IItemProvider> chunk, ILootCondition.IBuilder condition) {
            return LootTable.builder().addLootPool(droppingChunksPool(block, chunk).acceptCondition(condition));
        }

        private static LootTable.Builder dropNumberOfItems(Block block, Supplier<? extends IItemProvider> drop, final int minDrops, final int maxDrops) {
            return LootTable.builder().addLootPool(withSurvivesExplosion(block, LootPool.builder()
            		.addEntry(ItemLootEntry.builder(drop.get()))
                    	.acceptFunction(SetCount.builder(RandomValueRange.of(minDrops, maxDrops)))));
        }
        
        @Override
        protected Iterable<Block> getKnownBlocks() {
            return TropicraftBlocks.BLOCKS.getEntries().stream().map(Supplier::get).collect(Collectors.toList());
        }
    }
}

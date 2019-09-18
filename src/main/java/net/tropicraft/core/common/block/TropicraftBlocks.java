package net.tropicraft.core.common.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.BlockHardnessValues;
import net.tropicraft.core.client.tileentity.DrinkMixerItemstackRenderer;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class TropicraftBlocks {
    public static final Block CHUNK = new Block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(2, 30));
    public static final Block AZURITE_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block EUDIALYTE_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block MANGANESE_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block SHAKA_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block ZIRCON_ORE = new BlockTropicraftOre(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));

    public static final Block AZURITE_BLOCK = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block EUDIALYTE_BLOCK = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));
    public static final Block ZIRCON_BLOCK = new BlockTropicraftOreBlock(Block.Properties.create(Material.ROCK, MaterialColor.GRAY).sound(SoundType.STONE).hardnessAndResistance(3, 5));

    public static final Block ACAI_VINE = Builder.flower();
    public static final Block ANEMONE = Builder.flower();
    public static final Block BROMELIAD = Builder.flower();
    public static final Block CANNA = Builder.flower();
    public static final Block COMMELINA_DIFFUSA = Builder.flower();
    public static final Block CROCOSMIA = Builder.flower();
    public static final Block CROTON = Builder.flower();
    public static final Block DRACAENA = Builder.flower();
    public static final Block FERN = Builder.flower();
    public static final Block FOLIAGE = Builder.flower();
    public static final Block MAGIC_MUSHROOM = Builder.flower();
    public static final Block ORANGE_ANTHURIUM = Builder.flower();
    public static final Block ORCHID = Builder.flower();
    public static final Block PATHOS = Builder.flower();
    public static final Block RED_ANTHURIUM = Builder.flower();

    public static final Block[] SMALL_FLOWERS = new Block[]{
        ACAI_VINE, ANEMONE, BROMELIAD, CANNA, COMMELINA_DIFFUSA, CROCOSMIA, CROTON, DRACAENA, FERN,
            FOLIAGE, MAGIC_MUSHROOM, ORANGE_ANTHURIUM, ORCHID, PATHOS, RED_ANTHURIUM
    };

    public static final Block[] TROPICS_FLOWERS = new Block[]{
            ACAI_VINE, ANEMONE, BROMELIAD, CANNA, COMMELINA_DIFFUSA, CROCOSMIA, CROTON, DRACAENA, FERN,
            FOLIAGE, ORANGE_ANTHURIUM, ORCHID, PATHOS, RED_ANTHURIUM
    };

    public static final Block PURIFIED_SAND = Builder.sand(MaterialColor.SAND);
    public static final Block PACKED_PURIFIED_SAND = Builder.sand(MaterialColor.SAND, 2, 30);
    public static final Block CORAL_SAND = Builder.sand(MaterialColor.PINK);
    public static final Block FOAMY_SAND = Builder.sand(MaterialColor.GREEN);
    public static final Block VOLCANIC_SAND = Builder.sand(MaterialColor.BLACK);
    public static final Block MINERAL_SAND = Builder.sand(MaterialColor.SAND);

    public static final Block BAMBOO_BUNDLE = Builder.bundle(Block.Properties.create(Material.WOOD).sound(SoundType.BAMBOO));
    public static final Block THATCH_BUNDLE = Builder.bundle(Block.Properties.create(Material.WOOD).sound(SoundType.PLANT));

    public static final Block MAHOGANY_PLANKS = Builder.plank(MaterialColor.BROWN);
    public static final Block PALM_PLANKS = Builder.plank(MaterialColor.WOOD);
    public static final Block MAHOGANY_LOG = Builder.log(MaterialColor.WOOD, MaterialColor.BROWN);
    public static final Block PALM_LOG = Builder.log(MaterialColor.WOOD, MaterialColor.BROWN);

    public static final Block PALM_STAIRS = Builder.stairs(PALM_PLANKS.getDefaultState(), Material.WOOD, MaterialColor.BROWN, BlockRenderLayer.SOLID, SoundType.WOOD);
    public static final Block MAHOGANY_STAIRS = Builder.stairs(MAHOGANY_PLANKS.getDefaultState(), Material.WOOD, MaterialColor.BROWN, BlockRenderLayer.SOLID, SoundType.WOOD);
    public static final Block THATCH_STAIRS = Builder.stairs(THATCH_BUNDLE.getDefaultState(), Material.WOOD, MaterialColor.YELLOW, BlockRenderLayer.SOLID, SoundType.PLANT);
    public static final Block THATCH_STAIRS_FUZZY = Builder.stairs(THATCH_BUNDLE.getDefaultState(), Material.WOOD, MaterialColor.YELLOW, BlockRenderLayer.CUTOUT_MIPPED, SoundType.PLANT);
    public static final Block BAMBOO_STAIRS = Builder.stairs(BAMBOO_BUNDLE.getDefaultState(), Material.BAMBOO, MaterialColor.YELLOW, BlockRenderLayer.SOLID, SoundType.BAMBOO);
    public static final Block CHUNK_STAIRS = Builder.stairs(CHUNK.getDefaultState(), Material.ROCK, MaterialColor.BLACK, BlockRenderLayer.SOLID, SoundType.STONE);

    public static final Block COCONUT = new CoconutBlock(Block.Properties.create(Material.GOURD).hardnessAndResistance(2.0f).sound(SoundType.STONE));

    public static final Block BAMBOO_SLAB = Builder.slab(Block.Properties.create(Material.BAMBOO, MaterialColor.GREEN).sound(SoundType.BAMBOO).hardnessAndResistance(BlockHardnessValues.BAMBOO.hardness, BlockHardnessValues.BAMBOO.resistance));
    public static final Block THATCH_SLAB = Builder.slab(Block.Properties.create(Material.WOOD, MaterialColor.YELLOW).sound(SoundType.PLANT).hardnessAndResistance(BlockHardnessValues.THATCH.hardness, BlockHardnessValues.THATCH.resistance));
    public static final Block CHUNK_SLAB = Builder.slab(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).sound(SoundType.STONE).hardnessAndResistance(BlockHardnessValues.CHUNK.hardness, BlockHardnessValues.CHUNK.resistance));
    public static final Block PALM_SLAB = Builder.slab(Block.Properties.create(Material.WOOD, MaterialColor.BROWN).sound(SoundType.WOOD).hardnessAndResistance(BlockHardnessValues.PALM.hardness, BlockHardnessValues.PALM.resistance));
    public static final Block MAHOGANY_SLAB = Builder.slab(Block.Properties.create(Material.WOOD, MaterialColor.BROWN).sound(SoundType.WOOD).hardnessAndResistance(BlockHardnessValues.MAHOGANY.hardness, BlockHardnessValues.MAHOGANY.resistance));

    public static final Block MAHOGANY_LEAVES = Builder.leaves();
    public static final Block PALM_LEAVES = Builder.leaves();
    public static final Block KAPOK_LEAVES = Builder.leaves();
    public static final Block FRUIT_LEAVES = Builder.leaves();
    public static final Block GRAPEFRUIT_LEAVES = Builder.leaves();
    public static final Block LEMON_LEAVES = Builder.leaves();
    public static final Block LIME_LEAVES = Builder.leaves();
    public static final Block ORANGE_LEAVES = Builder.leaves();

    public static final Block GRAPEFRUIT_SAPLING = Builder.sapling(TropicraftTrees.GRAPEFRUIT);
    public static final Block LEMON_SAPLING = Builder.sapling(TropicraftTrees.LEMON);
    public static final Block LIME_SAPLING = Builder.sapling(TropicraftTrees.LIME);
    public static final Block ORANGE_SAPLING = Builder.sapling(TropicraftTrees.ORANGE);
    public static final Block MAHOGANY_SAPLING = Builder.sapling(TropicraftTrees.RAINFOREST);
    public static final Block PALM_SAPLING = Builder.sapling(TropicraftTrees.PALM, Blocks.SAND, CORAL_SAND, FOAMY_SAND, VOLCANIC_SAND, PURIFIED_SAND, MINERAL_SAND);

    public static final Block BAMBOO_FENCE = Builder.fence(Material.BAMBOO, MaterialColor.GREEN);
    public static final Block THATCH_FENCE = Builder.fence(Material.PLANTS, MaterialColor.YELLOW);
    public static final Block CHUNK_FENCE = Builder.fence(Material.ROCK, MaterialColor.BLACK);
    public static final Block PALM_FENCE = Builder.fence(Material.WOOD, MaterialColor.BROWN);
    public static final Block MAHOGANY_FENCE = Builder.fence(Material.WOOD, MaterialColor.BROWN);

    public static final Block BAMBOO_FENCE_GATE = Builder.fenceGate(Material.BAMBOO, MaterialColor.GREEN);
    public static final Block THATCH_FENCE_GATE = Builder.fenceGate(Material.PLANTS, MaterialColor.YELLOW);
    public static final Block CHUNK_FENCE_GATE = Builder.fenceGate(Material.ROCK, MaterialColor.BLACK);
    public static final Block PALM_FENCE_GATE = Builder.fenceGate(Material.WOOD, MaterialColor.BROWN);
    public static final Block MAHOGANY_FENCE_GATE = Builder.fenceGate(Material.WOOD, MaterialColor.BROWN);

    public static final Block IRIS = new TallFlowerBlock(Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT));
    public static final Block PINEAPPLE = new PineappleBlock(Block.Properties.create(Material.TALL_PLANTS).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT));

    public static final Block SMALL_BONGO_DRUM = Builder.bongo(BongoDrumBlock.Size.SMALL);
    public static final Block MEDIUM_BONGO_DRUM = Builder.bongo(BongoDrumBlock.Size.MEDIUM);
    public static final Block LARGE_BONGO_DRUM = Builder.bongo(BongoDrumBlock.Size.LARGE);

    public static final Block BAMBOO_LADDER = new LadderBlock(Block.Properties.create(Material.BAMBOO).sound(SoundType.BAMBOO)) {};

    public static final Block BAMBOO_CHEST = new BambooChestBlock(Block.Properties.create(Material.BAMBOO).sound(SoundType.BAMBOO));
    public static final Block SIFTER = new SifterBlock(Block.Properties.create(Material.WOOD));
    public static final Block DRINK_MIXER = new DrinkMixerBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 30));
    
    public static final Block TIKI_TORCH = new TikiTorchBlock(Block.Properties.from(Blocks.TORCH).sound(SoundType.WOOD).lightValue(0));
    public static final Block BAMBOO_DOOR = new DoorBlock(Block.Properties.create(Material.BAMBOO).hardnessAndResistance(1.0F).sound(SoundType.BAMBOO)) {};

    public static final FlowerPotBlock BAMBOO_FLOWER_POT = Builder.tropicraftPot(Blocks.AIR);
    public static final Block BAMBOO_POTTED_PALM_SAPLING = Builder.tropicraftPot(PALM_SAPLING);
    public static final Block BAMBOO_POTTED_MAHOGANY_SAPLING = Builder.tropicraftPot(MAHOGANY_SAPLING);
    public static final Block BAMBOO_POTTED_GRAPEFRUIT_SAPLING = Builder.tropicraftPot(GRAPEFRUIT_SAPLING);
    public static final Block BAMBOO_POTTED_LEMON_SAPLING = Builder.tropicraftPot(LEMON_SAPLING);
    public static final Block BAMBOO_POTTED_LIME_SAPLING = Builder.tropicraftPot(LIME_SAPLING);
    public static final Block BAMBOO_POTTED_ORANGE_SAPLING = Builder.tropicraftPot(ORANGE_SAPLING);
    public static final Block BAMBOO_POTTED_COMMELINA_DIFFUSA = Builder.tropicraftPot(COMMELINA_DIFFUSA);
    public static final Block BAMBOO_POTTED_CROCOSMIA = Builder.tropicraftPot(CROCOSMIA);
    public static final Block BAMBOO_POTTED_ORCHID = Builder.tropicraftPot(ORCHID);
    public static final Block BAMBOO_POTTED_CANNA = Builder.tropicraftPot(CANNA);
    public static final Block BAMBOO_POTTED_ANEMONE = Builder.tropicraftPot(ANEMONE);
    public static final Block BAMBOO_POTTED_ORANGE_ANTHURIUM = Builder.tropicraftPot(ORANGE_ANTHURIUM);
    public static final Block BAMBOO_POTTED_RED_ANTHURIUM = Builder.tropicraftPot(RED_ANTHURIUM);
    public static final Block BAMBOO_POTTED_MAGIC_MUSHROOM = Builder.tropicraftPot(MAGIC_MUSHROOM);
    public static final Block BAMBOO_POTTED_PATHOS = Builder.tropicraftPot(PATHOS);
    public static final Block BAMBOO_POTTED_ACAI_VINE = Builder.tropicraftPot(ACAI_VINE);
    public static final Block BAMBOO_POTTED_CROTON = Builder.tropicraftPot(CROTON);
    public static final Block BAMBOO_POTTED_DRACAENA = Builder.tropicraftPot(DRACAENA);
    public static final Block BAMBOO_POTTED_TROPICRAFT_FERN = Builder.tropicraftPot(FERN);
    public static final Block BAMBOO_POTTED_FOLIAGE = Builder.tropicraftPot(FOLIAGE);
    public static final Block BAMBOO_POTTED_BROMELIAD = Builder.tropicraftPot(BROMELIAD);

    public static final Block POTTED_COMMELINA_DIFFUSA = Builder.vanillaPot(COMMELINA_DIFFUSA);
    public static final Block POTTED_CROCOSMIA = Builder.vanillaPot(CROCOSMIA);
    public static final Block POTTED_ORCHID = Builder.vanillaPot(ORCHID);
    public static final Block POTTED_CANNA = Builder.vanillaPot(CANNA);
    public static final Block POTTED_ANEMONE = Builder.vanillaPot(ANEMONE);
    public static final Block POTTED_ORANGE_ANTHURIUM = Builder.vanillaPot(ORANGE_ANTHURIUM);
    public static final Block POTTED_RED_ANTHURIUM = Builder.vanillaPot(RED_ANTHURIUM);
    public static final Block POTTED_MAGIC_MUSHROOM = Builder.vanillaPot(MAGIC_MUSHROOM);
    public static final Block POTTED_PATHOS = Builder.vanillaPot(PATHOS);
    public static final Block POTTED_ACAI_VINE = Builder.vanillaPot(ACAI_VINE);
    public static final Block POTTED_CROTON = Builder.vanillaPot(CROTON);
    public static final Block POTTED_DRACAENA = Builder.vanillaPot(DRACAENA);
    public static final Block POTTED_FERN = Builder.vanillaPot(FERN);
    public static final Block POTTED_FOLIAGE = Builder.vanillaPot(FOLIAGE);
    public static final Block POTTED_BROMELIAD = Builder.vanillaPot(BROMELIAD);
    public static final Block POTTED_PALM_SAPLING = Builder.vanillaPot(PALM_SAPLING);
    public static final Block POTTED_MAHOGANY_SAPLING = Builder.vanillaPot(MAHOGANY_SAPLING);
    public static final Block POTTED_GRAPEFRUIT_SAPLING = Builder.vanillaPot(GRAPEFRUIT_SAPLING);
    public static final Block POTTED_LEMON_SAPLING = Builder.vanillaPot(LEMON_SAPLING);
    public static final Block POTTED_LIME_SAPLING = Builder.vanillaPot(LIME_SAPLING);
    public static final Block POTTED_ORANGE_SAPLING = Builder.vanillaPot(ORANGE_SAPLING);
    public static final Block BAMBOO_POTTED_OAK_SAPLING = Builder.tropicraftPot(Blocks.OAK_SAPLING);
    public static final Block BAMBOO_POTTED_SPRUCE_SAPLING = Builder.tropicraftPot(Blocks.SPRUCE_SAPLING);
    public static final Block BAMBOO_POTTED_BIRCH_SAPLING = Builder.tropicraftPot(Blocks.BIRCH_SAPLING);
    public static final Block BAMBOO_POTTED_JUNGLE_SAPLING = Builder.tropicraftPot(Blocks.JUNGLE_SAPLING);
    public static final Block BAMBOO_POTTED_ACACIA_SAPLING = Builder.tropicraftPot(Blocks.ACACIA_SAPLING);
    public static final Block BAMBOO_POTTED_DARK_OAK_SAPLING = Builder.tropicraftPot(Blocks.DARK_OAK_SAPLING);
    public static final Block BAMBOO_POTTED_FERN = Builder.tropicraftPot(Blocks.FERN);
    public static final Block BAMBOO_POTTED_DANDELION = Builder.tropicraftPot(Blocks.DANDELION);
    public static final Block BAMBOO_POTTED_POPPY = Builder.tropicraftPot(Blocks.POPPY);
    public static final Block BAMBOO_POTTED_BLUE_ORCHID = Builder.tropicraftPot(Blocks.BLUE_ORCHID);
    public static final Block BAMBOO_POTTED_ALLIUM = Builder.tropicraftPot(Blocks.ALLIUM);
    public static final Block BAMBOO_POTTED_AZURE_BLUET = Builder.tropicraftPot(Blocks.AZURE_BLUET);
    public static final Block BAMBOO_POTTED_RED_TULIP = Builder.tropicraftPot(Blocks.RED_TULIP);
    public static final Block BAMBOO_POTTED_ORANGE_TULIP = Builder.tropicraftPot(Blocks.ORANGE_TULIP);
    public static final Block BAMBOO_POTTED_WHITE_TULIP = Builder.tropicraftPot(Blocks.WHITE_TULIP);
    public static final Block BAMBOO_POTTED_PINK_TULIP = Builder.tropicraftPot(Blocks.PINK_TULIP);
    public static final Block BAMBOO_POTTED_OXEYE_DAISY = Builder.tropicraftPot(Blocks.OXEYE_DAISY);
    public static final Block BAMBOO_POTTED_CORNFLOWER = Builder.tropicraftPot(Blocks.CORNFLOWER);
    public static final Block BAMBOO_POTTED_LILY_OF_THE_VALLEY = Builder.tropicraftPot(Blocks.LILY_OF_THE_VALLEY);
    public static final Block BAMBOO_POTTED_WITHER_ROSE = Builder.tropicraftPot(Blocks.WITHER_ROSE);
    public static final Block BAMBOO_POTTED_RED_MUSHROOM = Builder.tropicraftPot(Blocks.RED_MUSHROOM);
    public static final Block BAMBOO_POTTED_BROWN_MUSHROOM = Builder.tropicraftPot(Blocks.BROWN_MUSHROOM);
    public static final Block BAMBOO_POTTED_DEAD_BUSH = Builder.tropicraftPot(Blocks.DEAD_BUSH);
    public static final Block BAMBOO_POTTED_CACTUS = Builder.tropicraftPot(Blocks.CACTUS);

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
            registerBlockDefault(event, "chunk", CHUNK);
            // TODO oredict these
            // TODO datafix these
            registerBlockDefault(event, "azurite_ore", AZURITE_ORE);
            registerBlockDefault(event, "eudialyte_ore", EUDIALYTE_ORE);
            registerBlockDefault(event, "manganese_ore", MANGANESE_ORE);
            registerBlockDefault(event, "shaka_ore", SHAKA_ORE);
            registerBlockDefault(event, "zircon_ore", ZIRCON_ORE);
            registerBlockDefault(event, "azurite_block", AZURITE_BLOCK);
            registerBlockDefault(event, "eudialyte_block", EUDIALYTE_BLOCK);
            registerBlockDefault(event, "zircon_block", ZIRCON_BLOCK);
            registerBlockDefault(event, "acai_vine", ACAI_VINE);
            registerBlockDefault(event, "anemone", ANEMONE);
            registerBlockDefault(event, "bromeliad", BROMELIAD);
            registerBlockDefault(event, "canna", CANNA);
            registerBlockDefault(event, "commelina_diffusa", COMMELINA_DIFFUSA);
            registerBlockDefault(event, "crocosmia", CROCOSMIA);
            registerBlockDefault(event, "croton", CROTON);
            registerBlockDefault(event, "dracaena", DRACAENA);
            registerBlockDefault(event, "fern", FERN);
            registerBlockDefault(event, "foliage", FOLIAGE);
            registerBlockDefault(event, "magic_mushroom", MAGIC_MUSHROOM);
            registerBlockDefault(event, "orange_anthurium", ORANGE_ANTHURIUM);
            registerBlockDefault(event, "orchid", ORCHID);
            registerBlockDefault(event, "pathos", PATHOS);
            registerBlockDefault(event, "red_anthurium", RED_ANTHURIUM);
            registerBlockDefault(event, "purified_sand", PURIFIED_SAND);
            registerBlockDefault(event, "coral_sand", CORAL_SAND);
            registerBlockDefault(event, "foamy_sand", FOAMY_SAND);
            registerBlockDefault(event, "volcanic_sand", VOLCANIC_SAND);
            registerBlockDefault(event, "mineral_sand", MINERAL_SAND);
            registerBlockDefault(event, "bamboo_bundle", BAMBOO_BUNDLE);
            registerBlockDefault(event, "thatch_bundle", THATCH_BUNDLE);
            registerBlockDefault(event, "mahogany_planks", MAHOGANY_PLANKS);
            registerBlockDefault(event, "palm_planks", PALM_PLANKS);
            registerBlockDefault(event, "mahogany_log", MAHOGANY_LOG);
            registerBlockDefault(event, "palm_log", PALM_LOG);
            registerBlockDefault(event, "palm_stairs", PALM_STAIRS);
            registerBlockDefault(event, "mahogany_stairs", MAHOGANY_STAIRS);
            registerBlockDefault(event, "thatch_stairs", THATCH_STAIRS);
            registerBlockDefault(event, "thatch_stairs_fuzzy", THATCH_STAIRS_FUZZY);
            registerBlockDefault(event, "bamboo_stairs", BAMBOO_STAIRS);
            registerBlockDefault(event, "chunk_stairs", CHUNK_STAIRS);
            registerBlockDefault(event, "coconut", COCONUT);
            registerBlockDefault(event, "packed_purified_sand", PACKED_PURIFIED_SAND);
            registerBlockDefault(event, "bamboo_slab", BAMBOO_SLAB);
            registerBlockDefault(event, "thatch_slab", THATCH_SLAB);
            registerBlockDefault(event, "chunk_slab", CHUNK_SLAB);
            registerBlockDefault(event, "palm_slab", PALM_SLAB);
            registerBlockDefault(event, "mahogany_slab", MAHOGANY_SLAB);
            registerBlockDefault(event, "mahogany_leaves", MAHOGANY_LEAVES);
            registerBlockDefault(event, "palm_leaves", PALM_LEAVES);
            registerBlockDefault(event, "kapok_leaves", KAPOK_LEAVES);
            registerBlockDefault(event, "fruit_leaves", FRUIT_LEAVES);
            registerBlockDefault(event, "grapefruit_leaves", GRAPEFRUIT_LEAVES);
            registerBlockDefault(event, "lemon_leaves", LEMON_LEAVES);
            registerBlockDefault(event, "lime_leaves", LIME_LEAVES);
            registerBlockDefault(event, "orange_leaves", ORANGE_LEAVES);
            registerBlockDefault(event, "grapefruit_sapling", GRAPEFRUIT_SAPLING);
            registerBlockDefault(event, "lemon_sapling", LEMON_SAPLING);
            registerBlockDefault(event, "lime_sapling", LIME_SAPLING);
            registerBlockDefault(event, "orange_sapling", ORANGE_SAPLING);
            registerBlockDefault(event, "palm_sapling", PALM_SAPLING);
            registerBlockDefault(event, "mahogany_sapling", MAHOGANY_SAPLING);
            registerBlockDefault(event, "bamboo_fence", BAMBOO_FENCE);
            registerBlockDefault(event, "thatch_fence", THATCH_FENCE);
            registerBlockDefault(event, "chunk_fence", CHUNK_FENCE);
            registerBlockDefault(event, "palm_fence", PALM_FENCE);
            registerBlockDefault(event, "mahogany_fence", MAHOGANY_FENCE);
            registerBlockDefault(event, "bamboo_fence_gate", BAMBOO_FENCE_GATE);
            registerBlockDefault(event, "thatch_fence_gate", THATCH_FENCE_GATE);
            registerBlockDefault(event, "chunk_fence_gate", CHUNK_FENCE_GATE);
            registerBlockDefault(event, "palm_fence_gate", PALM_FENCE_GATE);
            registerBlockDefault(event, "mahogany_fence_gate", MAHOGANY_FENCE_GATE);
            registerBlockDefault(event, "iris", IRIS);
            registerBlockDefault(event, "pineapple", PINEAPPLE);
            registerBlockDefault(event, "small_bongo_drum", SMALL_BONGO_DRUM);
            registerBlockDefault(event, "medium_bongo_drum", MEDIUM_BONGO_DRUM);
            registerBlockDefault(event, "large_bongo_drum", LARGE_BONGO_DRUM);
            registerBlockDefault(event, "bamboo_ladder", BAMBOO_LADDER);
            registerBlockDefault(event, "bamboo_chest", BAMBOO_CHEST);
            registerBlockDefault(event, "sifter", SIFTER);
            registerBlockDefault(event, "drink_mixer", DRINK_MIXER);
            registerBlockDefault(event, "tiki_torch", TIKI_TORCH);
            registerBlockDefault(event, "bamboo_door", BAMBOO_DOOR);
            registerBlockDefault(event, "bamboo_flower_pot", BAMBOO_FLOWER_POT);
            registerBlockDefault(event, "bamboo_potted_commelina_diffusa", BAMBOO_POTTED_COMMELINA_DIFFUSA);
            registerBlockDefault(event, "bamboo_potted_crocosmia", BAMBOO_POTTED_CROCOSMIA);
            registerBlockDefault(event, "bamboo_potted_orchid", BAMBOO_POTTED_ORCHID);
            registerBlockDefault(event, "bamboo_potted_canna", BAMBOO_POTTED_CANNA);
            registerBlockDefault(event, "bamboo_potted_anemone", BAMBOO_POTTED_ANEMONE);
            registerBlockDefault(event, "bamboo_potted_orange_anthurium", BAMBOO_POTTED_ORANGE_ANTHURIUM);
            registerBlockDefault(event, "bamboo_potted_red_anthurium", BAMBOO_POTTED_RED_ANTHURIUM);
            registerBlockDefault(event, "bamboo_potted_magic_mushroom", BAMBOO_POTTED_MAGIC_MUSHROOM);
            registerBlockDefault(event, "bamboo_potted_pathos", BAMBOO_POTTED_PATHOS);
            registerBlockDefault(event, "bamboo_potted_acai_vine", BAMBOO_POTTED_ACAI_VINE);
            registerBlockDefault(event, "bamboo_potted_croton", BAMBOO_POTTED_CROTON);
            registerBlockDefault(event, "bamboo_potted_dracaena", BAMBOO_POTTED_DRACAENA);
            registerBlockDefault(event, "bamboo_potted_tropicraft_fern", BAMBOO_POTTED_TROPICRAFT_FERN);
            registerBlockDefault(event, "bamboo_potted_foliage", BAMBOO_POTTED_FOLIAGE);
            registerBlockDefault(event, "bamboo_potted_bromeliad", BAMBOO_POTTED_BROMELIAD);
            registerBlockDefault(event, "bamboo_potted_palm_sapling", BAMBOO_POTTED_PALM_SAPLING);
            registerBlockDefault(event, "bamboo_potted_mahogany_sapling", BAMBOO_POTTED_MAHOGANY_SAPLING);
            registerBlockDefault(event, "bamboo_potted_grapefruit_sapling", BAMBOO_POTTED_GRAPEFRUIT_SAPLING);
            registerBlockDefault(event, "bamboo_potted_lemon_sapling", BAMBOO_POTTED_LEMON_SAPLING);
            registerBlockDefault(event, "bamboo_potted_lime_sapling", BAMBOO_POTTED_LIME_SAPLING);
            registerBlockDefault(event, "bamboo_potted_orange_sapling", BAMBOO_POTTED_ORANGE_SAPLING);
            registerBlockDefault(event, "potted_commelina_diffusa", POTTED_COMMELINA_DIFFUSA);
            registerBlockDefault(event, "potted_crocosmia", POTTED_CROCOSMIA);
            registerBlockDefault(event, "potted_orchid", POTTED_ORCHID);
            registerBlockDefault(event, "potted_canna", POTTED_CANNA);
            registerBlockDefault(event, "potted_anemone", POTTED_ANEMONE);
            registerBlockDefault(event, "potted_orange_anthurium", POTTED_ORANGE_ANTHURIUM);
            registerBlockDefault(event, "potted_red_anthurium", POTTED_RED_ANTHURIUM);
            registerBlockDefault(event, "potted_magic_mushroom", POTTED_MAGIC_MUSHROOM);
            registerBlockDefault(event, "potted_pathos", POTTED_PATHOS);
            registerBlockDefault(event, "potted_acai_vine", POTTED_ACAI_VINE);
            registerBlockDefault(event, "potted_croton", POTTED_CROTON);
            registerBlockDefault(event, "potted_dracaena", POTTED_DRACAENA);
            registerBlockDefault(event, "potted_fern", POTTED_FERN);
            registerBlockDefault(event, "potted_foliage", POTTED_FOLIAGE);
            registerBlockDefault(event, "potted_bromeliad", POTTED_BROMELIAD);
            registerBlockDefault(event, "potted_palm_sapling", POTTED_PALM_SAPLING);
            registerBlockDefault(event, "potted_mahogany_sapling", POTTED_MAHOGANY_SAPLING);
            registerBlockDefault(event, "potted_grapefruit_sapling", POTTED_GRAPEFRUIT_SAPLING);
            registerBlockDefault(event, "potted_lemon_sapling", POTTED_LEMON_SAPLING);
            registerBlockDefault(event, "potted_lime_sapling", POTTED_LIME_SAPLING);
            registerBlockDefault(event, "potted_orange_sapling", POTTED_ORANGE_SAPLING);
            registerBlockDefault(event, "bamboo_potted_oak_sapling", BAMBOO_POTTED_OAK_SAPLING);
            registerBlockDefault(event, "bamboo_potted_spruce_sapling", BAMBOO_POTTED_SPRUCE_SAPLING);
            registerBlockDefault(event, "bamboo_potted_birch_sapling", BAMBOO_POTTED_BIRCH_SAPLING);
            registerBlockDefault(event, "bamboo_potted_jungle_sapling", BAMBOO_POTTED_JUNGLE_SAPLING);
            registerBlockDefault(event, "bamboo_potted_acacia_sapling", BAMBOO_POTTED_ACACIA_SAPLING);
            registerBlockDefault(event, "bamboo_potted_dark_oak_sapling", BAMBOO_POTTED_DARK_OAK_SAPLING);
            registerBlockDefault(event, "bamboo_potted_fern", BAMBOO_POTTED_FERN);
            registerBlockDefault(event, "bamboo_potted_dandelion", BAMBOO_POTTED_DANDELION);
            registerBlockDefault(event, "bamboo_potted_poppy", BAMBOO_POTTED_POPPY);
            registerBlockDefault(event, "bamboo_potted_blue_orchid", BAMBOO_POTTED_BLUE_ORCHID);
            registerBlockDefault(event, "bamboo_potted_allium", BAMBOO_POTTED_ALLIUM);
            registerBlockDefault(event, "bamboo_potted_azure_bluet", BAMBOO_POTTED_AZURE_BLUET);
            registerBlockDefault(event, "bamboo_potted_red_tulip", BAMBOO_POTTED_RED_TULIP);
            registerBlockDefault(event, "bamboo_potted_orange_tulip", BAMBOO_POTTED_ORANGE_TULIP);
            registerBlockDefault(event, "bamboo_potted_white_tulip", BAMBOO_POTTED_WHITE_TULIP);
            registerBlockDefault(event, "bamboo_potted_pink_tulip", BAMBOO_POTTED_PINK_TULIP);
            registerBlockDefault(event, "bamboo_potted_oxeye_daisy", BAMBOO_POTTED_OXEYE_DAISY);
            registerBlockDefault(event, "bamboo_potted_cornflower", BAMBOO_POTTED_CORNFLOWER);
            registerBlockDefault(event, "bamboo_potted_lily_of_the_valley", BAMBOO_POTTED_LILY_OF_THE_VALLEY);
            registerBlockDefault(event, "bamboo_potted_wither_rose", BAMBOO_POTTED_WITHER_ROSE);
            registerBlockDefault(event, "bamboo_potted_red_mushroom", BAMBOO_POTTED_RED_MUSHROOM);
            registerBlockDefault(event, "bamboo_potted_brown_mushroom", BAMBOO_POTTED_BROWN_MUSHROOM);
            registerBlockDefault(event, "bamboo_potted_dead_bush", BAMBOO_POTTED_DEAD_BUSH);
            registerBlockDefault(event, "bamboo_potted_cactus", BAMBOO_POTTED_CACTUS);
        }

        @SubscribeEvent
        public static void onItemRegistry(final RegistryEvent.Register<Item> event) {
            registerItemDefault(event, CHUNK);
            registerItemDefault(event, AZURITE_ORE);
            registerItemDefault(event, EUDIALYTE_ORE);
            registerItemDefault(event, ZIRCON_ORE);
            registerItemDefault(event, MANGANESE_ORE);
            registerItemDefault(event, SHAKA_ORE);
            registerItemDefault(event, AZURITE_BLOCK);
            registerItemDefault(event, EUDIALYTE_BLOCK);
            registerItemDefault(event, ZIRCON_BLOCK);
            registerItemDefault(event, ACAI_VINE);
            registerItemDefault(event, ANEMONE);
            registerItemDefault(event, BROMELIAD);
            registerItemDefault(event, CANNA);
            registerItemDefault(event, COMMELINA_DIFFUSA);
            registerItemDefault(event, CROCOSMIA);
            registerItemDefault(event, CROTON);
            registerItemDefault(event, DRACAENA);
            registerItemDefault(event, FERN);
            registerItemDefault(event, FOLIAGE);
            registerItemDefault(event, MAGIC_MUSHROOM);
            registerItemDefault(event, ORANGE_ANTHURIUM);
            registerItemDefault(event, ORCHID);
            registerItemDefault(event, PATHOS);
            registerItemDefault(event, RED_ANTHURIUM);
            registerItemDefault(event, PURIFIED_SAND);
            registerItemDefault(event, CORAL_SAND);
            registerItemDefault(event, FOAMY_SAND);
            registerItemDefault(event, VOLCANIC_SAND);
            registerItemDefault(event, MINERAL_SAND);
            registerItemDefault(event, BAMBOO_BUNDLE);
            registerItemDefault(event, THATCH_BUNDLE);
            registerItemDefault(event, MAHOGANY_PLANKS);
            registerItemDefault(event, PALM_PLANKS);
            registerItemDefault(event, MAHOGANY_LOG);
            registerItemDefault(event, PALM_LOG);
            registerItemDefault(event, PALM_STAIRS);
            registerItemDefault(event, MAHOGANY_STAIRS);
            registerItemDefault(event, THATCH_STAIRS);
            registerItemDefault(event, THATCH_STAIRS_FUZZY);
            registerItemDefault(event, BAMBOO_STAIRS);
            registerItemDefault(event, CHUNK_STAIRS);
            registerItemDefault(event, COCONUT);
            registerItemDefault(event, PACKED_PURIFIED_SAND);
            registerItemDefault(event, BAMBOO_SLAB);
            registerItemDefault(event, THATCH_SLAB);
            registerItemDefault(event, CHUNK_SLAB);
            registerItemDefault(event, PALM_SLAB);
            registerItemDefault(event, MAHOGANY_SLAB);
            registerItemDefault(event, MAHOGANY_LEAVES);
            registerItemDefault(event, PALM_LEAVES);
            registerItemDefault(event, KAPOK_LEAVES);
            registerItemDefault(event, FRUIT_LEAVES);
            registerItemDefault(event, GRAPEFRUIT_LEAVES);
            registerItemDefault(event, LEMON_LEAVES);
            registerItemDefault(event, LIME_LEAVES);
            registerItemDefault(event, ORANGE_LEAVES);
            registerItemDefault(event, GRAPEFRUIT_SAPLING);
            registerItemDefault(event, LEMON_SAPLING);
            registerItemDefault(event, LIME_SAPLING);
            registerItemDefault(event, ORANGE_SAPLING);
            registerItemDefault(event, PALM_SAPLING);
            registerItemDefault(event, MAHOGANY_SAPLING);
            registerItemDefault(event, BAMBOO_FENCE);
            registerItemDefault(event, THATCH_FENCE);
            registerItemDefault(event, CHUNK_FENCE);
            registerItemDefault(event, PALM_FENCE);
            registerItemDefault(event, MAHOGANY_FENCE);
            registerItemDefault(event, BAMBOO_FENCE_GATE);
            registerItemDefault(event, THATCH_FENCE_GATE);
            registerItemDefault(event, CHUNK_FENCE_GATE);
            registerItemDefault(event, PALM_FENCE_GATE);
            registerItemDefault(event, MAHOGANY_FENCE_GATE);
            registerItemDefault(event, IRIS);
            registerItemDefault(event, PINEAPPLE);
            registerItemDefault(event, SMALL_BONGO_DRUM);
            registerItemDefault(event, MEDIUM_BONGO_DRUM);
            registerItemDefault(event, LARGE_BONGO_DRUM);
            registerItemDefault(event, BAMBOO_LADDER);
            registerItemDefault(event, BAMBOO_CHEST);
            registerItemDefault(event, SIFTER);
            registerItem(event, DRINK_MIXER, () -> DrinkMixerItemstackRenderer::new);
            registerItemDefault(event, TIKI_TORCH);
            registerItemDefault(event, BAMBOO_DOOR);
            registerItemDefault(event, BAMBOO_FLOWER_POT);
        }

        private static void registerBlockDefault(final RegistryEvent.Register<Block> event, final String name, final Block block) {
            event.getRegistry().register(block.setRegistryName(new ResourceLocation(Constants.MODID, name)));
        }

        private static Item registerItemDefault(final RegistryEvent.Register<Item> event, final Block block) {
            return registerItem(event, block, Tropicraft.TROPICRAFT_ITEM_GROUP);
        }

        private static Item registerItem(final RegistryEvent.Register<Item> event, final Block block, Item blockItem) {
            blockItem = blockItem.setRegistryName(block.getRegistryName());
            event.getRegistry().register(blockItem);
            return blockItem;
        }

        private static Item registerItem(final RegistryEvent.Register<Item> event, final Block block, final Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
            final Item.Properties props = new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP).setTEISR(renderMethod);
            final Item itemBlock = new BlockItem(block, props);
            event.getRegistry().register(itemBlock.setRegistryName(block.getRegistryName()));
            return itemBlock;
        }

        private static Item registerItem(final RegistryEvent.Register<Item> event, final Block block, final ItemGroup itemGroup) {
            final Item itemBlock = new BlockItem(block, new Item.Properties().group(itemGroup)).setRegistryName(block.getRegistryName());
            event.getRegistry().register(itemBlock);
            return itemBlock;
        }
    }
}

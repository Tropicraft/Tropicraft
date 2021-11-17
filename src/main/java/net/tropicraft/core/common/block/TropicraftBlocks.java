package net.tropicraft.core.common.block;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Constants;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.tileentity.SimpleItemStackRenderer;
import net.tropicraft.core.common.Foods;
import net.tropicraft.core.common.block.TikiTorchBlock.TorchSection;
import net.tropicraft.core.common.block.huge_plant.HugePlantBlock;
import net.tropicraft.core.common.block.jigarbov.JigarbovTorchType;
import net.tropicraft.core.common.block.tileentity.AirCompressorTileEntity;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TropicraftBlocks {
    
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Constants.MODID);
    public static final DeferredRegister<Item> BLOCKITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Constants.MODID);
    
    public static final RegistryObject<PortalWaterBlock> PORTAL_WATER = registerNoItem(
            "portal_water", () -> new PortalWaterBlock(Block.Properties.of(Material.WATER).noDrops()));

    public static final RegistryObject<Block> CHUNK = register(
            "chunk", Builder.block(Block.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK).strength(6.0F, 30F)));
    
    public static final RegistryObject<Block> AZURITE_ORE = register(
            "azurite_ore", Builder.ore(MaterialColor.COLOR_GRAY));
    public static final RegistryObject<Block> EUDIALYTE_ORE = register(
            "eudialyte_ore", Builder.ore(MaterialColor.COLOR_GRAY));
    public static final RegistryObject<Block> MANGANESE_ORE = register(
            "manganese_ore", Builder.ore(MaterialColor.COLOR_BLACK));
    public static final RegistryObject<Block> SHAKA_ORE = register(
            "shaka_ore", Builder.ore(MaterialColor.COLOR_BLACK));
    public static final RegistryObject<Block> ZIRCON_ORE = register(
            "zircon_ore", Builder.ore(MaterialColor.COLOR_GRAY));

    public static final RegistryObject<Block> AZURITE_BLOCK = register(
            "azurite_block", Builder.oreBlock(MaterialColor.COLOR_LIGHT_BLUE));
    public static final RegistryObject<Block> EUDIALYTE_BLOCK = register(
            "eudialyte_block", Builder.oreBlock(MaterialColor.COLOR_PINK));
    public static final RegistryObject<Block> MANGANESE_BLOCK = register(
            "manganese_block", Builder.oreBlock(MaterialColor.COLOR_PURPLE));
    public static final RegistryObject<Block> SHAKA_BLOCK = register(
            "shaka_block", Builder.oreBlock(MaterialColor.COLOR_BLUE));
    public static final RegistryObject<Block> ZIRCON_BLOCK = register(
            "zircon_block", Builder.oreBlock(MaterialColor.COLOR_RED));
    public static final RegistryObject<Block> ZIRCONIUM_BLOCK = register(
            "zirconium_block", Builder.oreBlock(MaterialColor.COLOR_PINK));

    public static final Map<TropicraftFlower, RegistryObject<FlowerBlock>> FLOWERS = Arrays.<TropicraftFlower>stream(TropicraftFlower.values())
            .collect(Collectors.toMap(Function.identity(), f -> register(f.getId(), Builder.flower(f)),
                    (f1, f2) -> { throw new IllegalStateException(); }, () -> new EnumMap<>(TropicraftFlower.class)));

    public static final RegistryObject<Block> PURIFIED_SAND = register("purified_sand", Builder.sand(MaterialColor.SAND));
    public static final RegistryObject<Block> PACKED_PURIFIED_SAND = register("packed_purified_sand", Builder.sand(MaterialColor.SAND, 2, 30));
    public static final RegistryObject<Block> CORAL_SAND = register("coral_sand", Builder.sand(MaterialColor.COLOR_PINK));
    public static final RegistryObject<Block> FOAMY_SAND = register("foamy_sand", Builder.sand(MaterialColor.COLOR_GREEN));
    public static final RegistryObject<Block> VOLCANIC_SAND = register("volcanic_sand", Builder.volcanicSand(MaterialColor.COLOR_LIGHT_GRAY));
    public static final RegistryObject<Block> MINERAL_SAND = register("mineral_sand", Builder.sand(MaterialColor.SAND));

    public static final RegistryObject<Block> MUD = register("mud", Builder.mud());
    public static final RegistryObject<Block> MUD_WITH_PIANGUAS = register("mud_with_pianguas", Builder.mud());

    public static final RegistryObject<RotatedPillarBlock> BAMBOO_BUNDLE = register(
            "bamboo_bundle", Builder.bundle(BlockBehaviour.Properties.of(Material.BAMBOO, MaterialColor.PLANT).sound(SoundType.BAMBOO).strength(0.2F, 5.0F)));
    public static final RegistryObject<RotatedPillarBlock> THATCH_BUNDLE = register(
            "thatch_bundle", Builder.bundle(Block.Properties.of(Material.GRASS, MaterialColor.WOOD).sound(SoundType.GRASS).strength(0.2F, 5.0F)));

    public static final RegistryObject<Block> MAHOGANY_PLANKS = register("mahogany_planks", Builder.plank(MaterialColor.COLOR_BROWN));
    public static final RegistryObject<Block> PALM_PLANKS = register("palm_planks", Builder.plank(MaterialColor.WOOD));
    public static final RegistryObject<RotatedPillarBlock> MAHOGANY_LOG = register("mahogany_log", Builder.log(MaterialColor.WOOD, MaterialColor.COLOR_BROWN));
    public static final RegistryObject<RotatedPillarBlock> PALM_LOG = register("palm_log", Builder.log(MaterialColor.COLOR_GRAY, MaterialColor.COLOR_BROWN));
    public static final RegistryObject<RotatedPillarBlock> MAHOGANY_WOOD = register("mohogany_wood", Builder.wood(MaterialColor.WOOD));
    public static final RegistryObject<RotatedPillarBlock> PALM_WOOD = register("palm_wood", Builder.wood(MaterialColor.COLOR_GRAY));

    public static final RegistryObject<StairBlock> PALM_STAIRS = register(
            "palm_stairs", Builder.stairs(PALM_PLANKS));
    public static final RegistryObject<StairBlock> MAHOGANY_STAIRS = register(
            "mahogany_stairs", Builder.stairs(MAHOGANY_PLANKS));
    public static final RegistryObject<StairBlock> THATCH_STAIRS = register(
            "thatch_stairs", Builder.stairs(THATCH_BUNDLE));
    public static final RegistryObject<StairBlock> THATCH_STAIRS_FUZZY = register(
            "thatch_stairs_fuzzy", Builder.stairs(THATCH_BUNDLE));
    public static final RegistryObject<StairBlock> BAMBOO_STAIRS = register(
            "bamboo_stairs", Builder.stairs(BAMBOO_BUNDLE));
    public static final RegistryObject<StairBlock> CHUNK_STAIRS = register(
            "chunk_stairs", Builder.stairs(CHUNK));

    public static final RegistryObject<Block> COCONUT = register(
            "coconut", () -> new CoconutBlock(Block.Properties.of(Material.WOOD).strength(2.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final RegistryObject<SlabBlock> BAMBOO_SLAB = register(
            "bamboo_slab", Builder.slab(BAMBOO_BUNDLE));
    public static final RegistryObject<SlabBlock> THATCH_SLAB = register(
            "thatch_slab", Builder.slab(THATCH_BUNDLE));
    public static final RegistryObject<SlabBlock> CHUNK_SLAB = register(
            "chunk_slab", Builder.slab(CHUNK));
    public static final RegistryObject<SlabBlock> PALM_SLAB = register(
            "palm_slab", Builder.slab(PALM_PLANKS));
    public static final RegistryObject<SlabBlock> MAHOGANY_SLAB = register(
            "mahogany_slab", Builder.slab(MAHOGANY_PLANKS));

    public static final RegistryObject<LeavesBlock> MAHOGANY_LEAVES = register("mahogany_leaves", Builder.leaves(false));
    public static final RegistryObject<LeavesBlock> PALM_LEAVES = register("palm_leaves", Builder.leaves(false));
    public static final RegistryObject<LeavesBlock> KAPOK_LEAVES = register("kapok_leaves", Builder.leaves(false));
    public static final RegistryObject<LeavesBlock> FRUIT_LEAVES = register("fruit_leaves", Builder.leaves(true));
    public static final RegistryObject<LeavesBlock> GRAPEFRUIT_LEAVES = register("grapefruit_leaves", Builder.leaves(true));
    public static final RegistryObject<LeavesBlock> LEMON_LEAVES = register("lemon_leaves", Builder.leaves(true));
    public static final RegistryObject<LeavesBlock> LIME_LEAVES = register("lime_leaves", Builder.leaves(true));
    public static final RegistryObject<LeavesBlock> ORANGE_LEAVES = register("orange_leaves", Builder.leaves(true));
    public static final RegistryObject<LeavesBlock> PAPAYA_LEAVES = register("papaya_leaves", Builder.leaves(false));

    public static final RegistryObject<SaplingBlock> GRAPEFRUIT_SAPLING = register("grapefruit_sapling", Builder.sapling(TropicraftTrees.GRAPEFRUIT));
    public static final RegistryObject<SaplingBlock> LEMON_SAPLING = register("lemon_sapling", Builder.sapling(TropicraftTrees.LEMON));
    public static final RegistryObject<SaplingBlock> LIME_SAPLING = register("lime_sapling", Builder.sapling(TropicraftTrees.LIME));
    public static final RegistryObject<SaplingBlock> ORANGE_SAPLING = register("orange_sapling", Builder.sapling(TropicraftTrees.ORANGE));
    public static final RegistryObject<SaplingBlock> PAPAYA_SAPLING = register("papaya_sapling", Builder.sapling(TropicraftTrees.PAPAYA));
    public static final RegistryObject<SaplingBlock> MAHOGANY_SAPLING = register("mahogany_sapling", Builder.sapling(TropicraftTrees.RAINFOREST));
    public static final RegistryObject<SaplingBlock> PALM_SAPLING = register(
            "palm_sapling", Builder.sapling(TropicraftTrees.PALM, () -> Blocks.SAND, CORAL_SAND, FOAMY_SAND, VOLCANIC_SAND, PURIFIED_SAND, MINERAL_SAND));

    public static final RegistryObject<RotatedPillarBlock> PAPAYA_LOG = register("papaya_log", Builder.log(MaterialColor.COLOR_GRAY, MaterialColor.COLOR_BROWN));
    public static final RegistryObject<RotatedPillarBlock> PAPAYA_WOOD = register("papaya_wood", Builder.wood(MaterialColor.COLOR_GRAY));

    public static final RegistryObject<RotatedPillarBlock> RED_MANGROVE_LOG = register("red_mangrove_log", Builder.log(MaterialColor.COLOR_GRAY, MaterialColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG));
    public static final RegistryObject<RotatedPillarBlock> RED_MANGROVE_WOOD = register("red_mangrove_wood", Builder.wood(MaterialColor.COLOR_GRAY, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD));
    public static final RegistryObject<Block> RED_MANGROVE_ROOTS = register("red_mangrove_roots", Builder.mangroveRoots());

    public static final RegistryObject<RotatedPillarBlock> LIGHT_MANGROVE_LOG = register("light_mangrove_log", Builder.log(MaterialColor.COLOR_GRAY, MaterialColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG));
    public static final RegistryObject<RotatedPillarBlock> LIGHT_MANGROVE_WOOD = register("light_mangrove_wood", Builder.wood(MaterialColor.COLOR_GRAY, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD));
    public static final RegistryObject<Block> LIGHT_MANGROVE_ROOTS = register("light_mangrove_roots", Builder.mangroveRoots());

    public static final RegistryObject<RotatedPillarBlock> BLACK_MANGROVE_LOG = register("black_mangrove_log", Builder.log(MaterialColor.COLOR_GRAY, MaterialColor.COLOR_BROWN, () -> TropicraftBlocks.STRIPPED_MANGROVE_LOG));
    public static final RegistryObject<RotatedPillarBlock> BLACK_MANGROVE_WOOD = register("black_mangrove_wood", Builder.wood(MaterialColor.COLOR_GRAY, () -> TropicraftBlocks.STRIPPED_MANGROVE_WOOD));
    public static final RegistryObject<Block> BLACK_MANGROVE_ROOTS = register("black_mangrove_roots", Builder.mangroveRoots());

    public static final RegistryObject<LeavesBlock> RED_MANGROVE_LEAVES = register("red_mangrove_leaves", Builder.mangroveLeaves(() -> TropicraftBlocks.RED_MANGROVE_PROPAGULE));
    public static final RegistryObject<LeavesBlock> TALL_MANGROVE_LEAVES = register("tall_mangrove_leaves", Builder.mangroveLeaves(() -> TropicraftBlocks.TALL_MANGROVE_PROPAGULE));
    public static final RegistryObject<LeavesBlock> TEA_MANGROVE_LEAVES = register("tea_mangrove_leaves", Builder.mangroveLeaves(() -> TropicraftBlocks.TEA_MANGROVE_PROPAGULE));
    public static final RegistryObject<LeavesBlock> BLACK_MANGROVE_LEAVES = register("black_mangrove_leaves", Builder.mangroveLeaves(() -> TropicraftBlocks.BLACK_MANGROVE_PROPAGULE));
    public static final RegistryObject<PropaguleBlock> RED_MANGROVE_PROPAGULE = register("red_mangrove_propagule", Builder.propagule(TropicraftTrees.RED_MANGROVE));
    public static final RegistryObject<PropaguleBlock> TALL_MANGROVE_PROPAGULE = register("tall_mangrove_propagule", Builder.propagule(TropicraftTrees.TALL_MANGROVE));
    public static final RegistryObject<PropaguleBlock> TEA_MANGROVE_PROPAGULE = register("tea_mangrove_propagule", Builder.propagule(TropicraftTrees.TEA_MANGROVE));
    public static final RegistryObject<PropaguleBlock> BLACK_MANGROVE_PROPAGULE = register("black_mangrove_propagule", Builder.propagule(TropicraftTrees.BLACK_MANGROVE));

    public static final RegistryObject<RotatedPillarBlock> STRIPPED_MANGROVE_LOG = register("stripped_mangrove_log", Builder.log(MaterialColor.COLOR_GRAY, MaterialColor.COLOR_BROWN));
    public static final RegistryObject<RotatedPillarBlock> STRIPPED_MANGROVE_WOOD = register("stripped_mangrove_wood", Builder.wood(MaterialColor.COLOR_GRAY));
    public static final RegistryObject<Block> MANGROVE_PLANKS = register("mangrove_planks", Builder.plank(MaterialColor.WOOD));
    public static final RegistryObject<StairBlock> MANGROVE_STAIRS = register("mangrove_stairs", Builder.stairs(MANGROVE_PLANKS));
    public static final RegistryObject<SlabBlock> MANGROVE_SLAB = register("mangrove_slab", Builder.slab(MANGROVE_PLANKS));
    public static final RegistryObject<FenceBlock> MANGROVE_FENCE = register("mangrove_fence", Builder.fence(MANGROVE_PLANKS));
    public static final RegistryObject<FenceGateBlock> MANGROVE_FENCE_GATE = register("mangrove_fence_gate", Builder.fenceGate(MANGROVE_PLANKS));
    public static final RegistryObject<DoorBlock> MANGROVE_DOOR = register("mangrove_door", () -> new DoorBlock(Block.Properties.copy(Blocks.OAK_DOOR)) {});
    public static final RegistryObject<TrapDoorBlock> MANGROVE_TRAPDOOR = register("mangrove_trapdoor", () -> new TrapDoorBlock(Block.Properties.copy(MANGROVE_DOOR.get())) {});

    public static final RegistryObject<ReedsBlock> REEDS = register("reeds", () -> new ReedsBlock(Block.Properties.copy(Blocks.SUGAR_CANE)));
    public static final RegistryObject<PapayaBlock> PAPAYA = registerWithFood("papaya", () -> new PapayaBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.WOOD).noOcclusion()), Foods.PAPAYA);

    public static final RegistryObject<FenceBlock> BAMBOO_FENCE = register("bamboo_fence", Builder.fence(BAMBOO_BUNDLE));
    public static final RegistryObject<FenceBlock> THATCH_FENCE = register("thatch_fence", Builder.fence(THATCH_BUNDLE));
    public static final RegistryObject<FenceBlock> CHUNK_FENCE = register("chunk_fence", Builder.fence(CHUNK));
    public static final RegistryObject<FenceBlock> PALM_FENCE = register("palm_fence", Builder.fence(PALM_PLANKS));
    public static final RegistryObject<FenceBlock> MAHOGANY_FENCE = register("mahogany_fence", Builder.fence(MAHOGANY_PLANKS));

    public static final RegistryObject<FenceGateBlock> BAMBOO_FENCE_GATE = register("bamboo_fence_gate", Builder.fenceGate(BAMBOO_BUNDLE));
    public static final RegistryObject<FenceGateBlock> THATCH_FENCE_GATE = register("thatch_fence_gate", Builder.fenceGate(THATCH_BUNDLE));
    public static final RegistryObject<FenceGateBlock> CHUNK_FENCE_GATE = register("chunk_fence_gate", Builder.fenceGate(CHUNK));
    public static final RegistryObject<FenceGateBlock> PALM_FENCE_GATE = register("palm_fence_gate", Builder.fenceGate(PALM_PLANKS));
    public static final RegistryObject<FenceGateBlock> MAHOGANY_FENCE_GATE = register("mahogany_fence_gate", Builder.fenceGate(MAHOGANY_PLANKS));

    public static final RegistryObject<WallBlock> CHUNK_WALL = register("chunk_wall", Builder.wall(CHUNK));
    
    public static final RegistryObject<DoorBlock> BAMBOO_DOOR = register(
            "bamboo_door", () -> new DoorBlock(Block.Properties.copy(BAMBOO_BUNDLE.get()).strength(1.0F)) {});
    public static final RegistryObject<DoorBlock> PALM_DOOR = register(
            "palm_door", () -> new DoorBlock(Block.Properties.copy(Blocks.OAK_DOOR)) {});
    public static final RegistryObject<DoorBlock> MAHOGANY_DOOR = register(
            "mahogany_door", () -> new DoorBlock(Block.Properties.copy(Blocks.OAK_DOOR)) {});
    public static final RegistryObject<DoorBlock> THATCH_DOOR = register(
            "thatch_door", () -> new DoorBlock(Block.Properties.copy(THATCH_BUNDLE.get())) {});
    
    public static final RegistryObject<TrapDoorBlock> BAMBOO_TRAPDOOR = register(
            "bamboo_trapdoor", () -> new TrapDoorBlock(Block.Properties.copy(BAMBOO_DOOR.get()).noOcclusion()) {});
    public static final RegistryObject<TrapDoorBlock> PALM_TRAPDOOR = register(
            "palm_trapdoor", () -> new TrapDoorBlock(Block.Properties.copy(PALM_DOOR.get())) {});
    public static final RegistryObject<TrapDoorBlock> MAHOGANY_TRAPDOOR = register(
            "mahogany_trapdoor", () -> new TrapDoorBlock(Block.Properties.copy(MAHOGANY_DOOR.get())) {});
    public static final RegistryObject<TrapDoorBlock> THATCH_TRAPDOOR = register(
            "thatch_trapdoor", () -> new TrapDoorBlock(Block.Properties.copy(THATCH_BUNDLE.get())) {});

    public static final RegistryObject<TallFlowerBlock> IRIS = register(
            "iris", () -> new TallFlowerBlock(Block.Properties.of(Material.REPLACEABLE_PLANT).noCollission().strength(0).sound(SoundType.GRASS)));
    public static final RegistryObject<PineappleBlock> PINEAPPLE = register(
            "pineapple", () -> new PineappleBlock(Block.Properties.of(Material.REPLACEABLE_PLANT).randomTicks().noCollission().strength(0).sound(SoundType.GRASS)));

    public static final RegistryObject<BongoDrumBlock> SMALL_BONGO_DRUM = register("small_bongo_drum", Builder.bongo(BongoDrumBlock.Size.SMALL));
    public static final RegistryObject<BongoDrumBlock> MEDIUM_BONGO_DRUM = register("medium_bongo_drum", Builder.bongo(BongoDrumBlock.Size.MEDIUM));
    public static final RegistryObject<BongoDrumBlock> LARGE_BONGO_DRUM = register("large_bongo_drum", Builder.bongo(BongoDrumBlock.Size.LARGE));

    public static final RegistryObject<LadderBlock> BAMBOO_LADDER = register(
            "bamboo_ladder", () -> new LadderBlock(Block.Properties.copy(Blocks.BAMBOO)) {});

    public static final RegistryObject<Block> BAMBOO_BOARDWALK = register("bamboo_boardwalk", () -> new BoardwalkBlock(Block.Properties.copy(BAMBOO_SLAB.get()).noOcclusion()));
    public static final RegistryObject<Block> PALM_BOARDWALK = register("palm_boardwalk", () -> new BoardwalkBlock(Block.Properties.copy(PALM_SLAB.get()).noOcclusion()));
    public static final RegistryObject<Block> MAHOGANY_BOARDWALK = register("mahogany_boardwalk", () -> new BoardwalkBlock(Block.Properties.copy(MAHOGANY_SLAB.get()).noOcclusion()));
    public static final RegistryObject<Block> MANGROVE_BOARDWALK = register("mangrove_boardwalk", () -> new BoardwalkBlock(Block.Properties.copy(MANGROVE_SLAB.get()).noOcclusion()));

    public static final RegistryObject<BambooChestBlock> BAMBOO_CHEST = register(
            "bamboo_chest", () -> new BambooChestBlock(Block.Properties.copy(BAMBOO_BUNDLE.get()).strength(1), () -> TropicraftTileEntityTypes.BAMBOO_CHEST.get()), () -> TropicraftTileEntityTypes.BAMBOO_CHEST.get());

    public static final RegistryObject<SifterBlock> SIFTER = register(
            "sifter", () -> new SifterBlock(Block.Properties.copy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistryObject<DrinkMixerBlock> DRINK_MIXER = register(
            "drink_mixer", () -> new DrinkMixerBlock(Block.Properties.of(Material.STONE).strength(2, 30).noOcclusion()), () -> TropicraftTileEntityTypes.DRINK_MIXER.get());

    public static final RegistryObject<AirCompressorBlock> AIR_COMPRESSOR = register(
            "air_compressor", () -> new AirCompressorBlock(Block.Properties.of(Material.STONE).strength(2, 30).noOcclusion()), () -> TropicraftTileEntityTypes.AIR_COMPRESSOR.get());

    public static final RegistryObject<VolcanoBlock> VOLCANO = registerNoItem(
            "volcano", () -> new VolcanoBlock(Block.Properties.copy(Blocks.BEDROCK).noDrops()));
    
    public static final RegistryObject<TikiTorchBlock> TIKI_TORCH = register(
            "tiki_torch", () -> new TikiTorchBlock(Block.Properties.copy(Blocks.TORCH).sound(SoundType.WOOD).lightLevel(state -> state.getValue(TikiTorchBlock.SECTION) == TorchSection.UPPER ? 15 : 0)));
    
    public static final RegistryObject<FlowerPotBlock> BAMBOO_FLOWER_POT = register(
            "bamboo_flower_pot", Builder.tropicraftPot());

    public static final RegistryObject<CoffeeBushBlock> COFFEE_BUSH = registerNoItem(
            "coffee_bush", () -> new CoffeeBushBlock(Block.Properties.of(Material.PLANT, MaterialColor.GRASS).strength(0.15f).sound(SoundType.GRASS).noOcclusion()));

    public static final RegistryObject<BushBlock> GOLDEN_LEATHER_FERN = register(
            "small_golden_leather_fern",
            () -> new GrowableSinglePlantBlock(Block.Properties.copy(Blocks.FERN), () -> TropicraftBlocks.TALL_GOLDEN_LEATHER_FERN)
    );

    public static final RegistryObject<DoublePlantBlock> TALL_GOLDEN_LEATHER_FERN = registerNoItem(
            "tall_golden_leather_fern",
            () -> new GrowableDoublePlantBlock(Block.Properties.copy(Blocks.LARGE_FERN), () -> TropicraftBlocks.LARGE_GOLDEN_LEATHER_FERN)
                    .setPickItem(() -> TropicraftBlocks.GOLDEN_LEATHER_FERN)
    );

    public static final RegistryObject<HugePlantBlock> LARGE_GOLDEN_LEATHER_FERN = registerNoItem(
            "golden_leather_fern", // TODO: update name before release
            () -> new HugePlantBlock(Block.Properties.copy(Blocks.LARGE_FERN))
                    .setPickItem(() -> TropicraftBlocks.GOLDEN_LEATHER_FERN)
    );

    @SuppressWarnings("unchecked")
    private static final Set<RegistryObject<? extends Block>> POTTABLE_PLANTS = ImmutableSet.<RegistryObject<? extends Block>>builder()
            .add(PALM_SAPLING, MAHOGANY_SAPLING, GRAPEFRUIT_SAPLING, LEMON_SAPLING, LIME_SAPLING, ORANGE_SAPLING)
            .add(IRIS)
            .addAll(FLOWERS.values())
            .build();
    
    public static final List<RegistryObject<FlowerPotBlock>> BAMBOO_POTTED_TROPICS_PLANTS = ImmutableList.copyOf(POTTABLE_PLANTS.stream()
            .map(b -> registerNoItem("bamboo_potted_" + b.getId().getPath(), Builder.tropicraftPot(b)))
            .collect(Collectors.toList()));
    
    public static final List<RegistryObject<FlowerPotBlock>> VANILLA_POTTED_TROPICS_PLANTS = ImmutableList.copyOf(POTTABLE_PLANTS.stream()
            .map(b -> registerNoItem("potted_" + b.getId().getPath(), Builder.vanillaPot(b)))
            .collect(Collectors.toList()));
    
    public static final List<RegistryObject<FlowerPotBlock>> BAMBOO_POTTED_VANILLA_PLANTS = ImmutableList.copyOf(
            Stream.of(Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING,
                Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.FERN, Blocks.DANDELION, Blocks.POPPY,
                Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP,
                Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY,
                Blocks.WITHER_ROSE, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.DEAD_BUSH, Blocks.CACTUS)
            .map(b -> registerNoItem("bamboo_potted_" + b.getRegistryName().getPath(), Builder.tropicraftPot(() -> b)))
            .collect(Collectors.toList()));
    
    public static final List<RegistryObject<FlowerPotBlock>> ALL_POTTED_PLANTS = ImmutableList.<RegistryObject<FlowerPotBlock>>builder()
            .addAll(BAMBOO_POTTED_TROPICS_PLANTS)
            .addAll(VANILLA_POTTED_TROPICS_PLANTS)
            .addAll(BAMBOO_POTTED_VANILLA_PLANTS)
            .build();

    public static final Map<JigarbovTorchType, RegistryObject<RedstoneWallTorchBlock>> JIGARBOV_WALL_TORCHES = Arrays.stream(JigarbovTorchType.values())
            .collect(Collectors.toMap(Function.identity(),
                    type -> registerNoItem("jigarbov_" + type.getName() + "_wall_torch", () -> {
                        return new RedstoneWallTorchBlock(Block.Properties.copy(Blocks.REDSTONE_WALL_TORCH).lootFrom(() -> Blocks.REDSTONE_TORCH)) {
                            @Override
                            public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
                                return new ItemStack(Items.REDSTONE_TORCH);
                            }
                        };
                    })
            ));

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup) {
        return register(name, sup, TropicraftBlocks::itemDefault);
    }

    private static <T extends Block> RegistryObject<T> registerWithFood(String name, Supplier<? extends T> sup, FoodProperties foo) {
        return register(name, sup, TropicraftBlocks::itemDefault);
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Supplier<? extends BlockEntityType> supBE) {
        return register(name, sup, block -> item(block, supBE));
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, CreativeModeTab tab) {
        return register(name, sup, block -> item(block, tab));
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
        RegistryObject<T> ret = registerNoItem(name, sup);
        BLOCKITEMS.register(name, itemCreator.apply(ret));
        return ret;
    }
    
    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
        return BLOCKS.register(name, sup);
    }

    private static Supplier<BlockItem> itemDefault(final RegistryObject<? extends Block> block) {
        return item(block, Tropicraft.TROPICRAFT_ITEM_GROUP);
    }

    private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, FoodProperties food) {
        return () -> new BlockItem(block.get(), new Item.Properties().tab(Tropicraft.TROPICRAFT_ITEM_GROUP).food(food));
    }

    private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, Supplier<? extends BlockEntityType> supBE) {
        return () -> new BlockItem(block.get(), new Item.Properties().tab(Tropicraft.TROPICRAFT_ITEM_GROUP)){
            @Override
            public void initializeClient(Consumer<IItemRenderProperties> consumer) {
                consumer.accept(new IItemRenderProperties() {
                    @Override
                    public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                        return new SimpleItemStackRenderer(supBE);
                    }
                });
            }
        };
    }

    private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final CreativeModeTab itemGroup) {
        return () -> new BlockItem(block.get(), new Item.Properties().tab(itemGroup));
    }
}

package net.tropicraft.core.common.block;

import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.Info;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.tileentity.DrinkMixerItemstackRenderer;
import net.tropicraft.core.common.item.TropicraftItems;

public class TropicraftBlocks {
    
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, Info.MODID);
    public static final DeferredRegister<Item> ITEMS = TropicraftItems.ITEMS;

    public static final RegistryObject<Block> CHUNK = register(
            "chunk", Builder.block(Block.Properties.create(Material.ROCK, MaterialColor.BLACK).hardnessAndResistance(6.0F, 30F)));
    
    public static final RegistryObject<Block> AZURITE_ORE = register(
            "azurite_ore", Builder.ore(MaterialColor.GRAY));
    public static final RegistryObject<Block> EUDIALYTE_ORE = register(
            "eudialyte_ore", Builder.ore(MaterialColor.GRAY));
    public static final RegistryObject<Block> MANGANESE_ORE = register(
            "manganese_ore", Builder.ore(MaterialColor.BLACK));
    public static final RegistryObject<Block> SHAKA_ORE = register(
            "shaka_ore", Builder.ore(MaterialColor.BLACK));
    public static final RegistryObject<Block> ZIRCON_ORE = register(
            "zircon_ore", Builder.ore(MaterialColor.GRAY));

    public static final RegistryObject<Block> AZURITE_BLOCK = register(
            "azurite_block", Builder.oreBlock(MaterialColor.GRAY));
    public static final RegistryObject<Block> EUDIALYTE_BLOCK = register(
            "eudialyte_block", Builder.oreBlock(MaterialColor.GRAY));
    public static final RegistryObject<Block> ZIRCON_BLOCK = register(
            "zircon_block", Builder.oreBlock(MaterialColor.GRAY));

    public static final RegistryObject<Block> ACAI_VINE = register("acai_vine", Builder.flower());
    public static final RegistryObject<Block> ANEMONE = register("anemone", Builder.flower());
    public static final RegistryObject<Block> BROMELIAD = register("bromeliad", Builder.flower());
    public static final RegistryObject<Block> CANNA = register("canna", Builder.flower());
    public static final RegistryObject<Block> COMMELINA_DIFFUSA = register("commelina_diffusa", Builder.flower());
    public static final RegistryObject<Block> CROCOSMIA = register("crocosmia", Builder.flower());
    public static final RegistryObject<Block> CROTON = register("croton", Builder.flower());
    public static final RegistryObject<Block> DRACAENA = register("dracaena", Builder.flower());
    public static final RegistryObject<Block> FERN = register("tropical_fern", Builder.flower());
    public static final RegistryObject<Block> FOLIAGE = register("foliage", Builder.flower());
    public static final RegistryObject<Block> MAGIC_MUSHROOM = register("magic_mushroom", Builder.flower());
    public static final RegistryObject<Block> ORANGE_ANTHURIUM = register("orange_anthurium", Builder.flower());
    public static final RegistryObject<Block> ORCHID = register("orchid", Builder.flower());
    public static final RegistryObject<Block> PATHOS = register("pathos", Builder.flower());
    public static final RegistryObject<Block> RED_ANTHURIUM = register("red_anthurium", Builder.flower());

    @SuppressWarnings("unchecked")
    public static final RegistryObject<Block>[] SMALL_FLOWERS = new RegistryObject[]{
        ACAI_VINE, ANEMONE, BROMELIAD, CANNA, COMMELINA_DIFFUSA, CROCOSMIA, CROTON, DRACAENA, FERN,
            FOLIAGE, MAGIC_MUSHROOM, ORANGE_ANTHURIUM, ORCHID, PATHOS, RED_ANTHURIUM
    };

    @SuppressWarnings("unchecked")
    public static final RegistryObject<Block>[] TROPICS_FLOWERS = new RegistryObject[]{
        ACAI_VINE, ANEMONE, BROMELIAD, CANNA, COMMELINA_DIFFUSA, CROCOSMIA, CROTON, DRACAENA, FERN,
            FOLIAGE, ORANGE_ANTHURIUM, ORCHID, PATHOS, RED_ANTHURIUM
    };

    public static final RegistryObject<Block> PURIFIED_SAND = register("purified_sand", Builder.sand(MaterialColor.SAND));
    public static final RegistryObject<Block> PACKED_PURIFIED_SAND = register("packed_purified_sand", Builder.sand(MaterialColor.SAND, 2, 30));
    public static final RegistryObject<Block> CORAL_SAND = register("coral_sand", Builder.sand(MaterialColor.PINK));
    public static final RegistryObject<Block> FOAMY_SAND = register("foamy_sand", Builder.sand(MaterialColor.GREEN));
    public static final RegistryObject<Block> VOLCANIC_SAND = register("volcanic_sand", Builder.sand(MaterialColor.BLACK));
    public static final RegistryObject<Block> MINERAL_SAND = register("mineral_sand", Builder.sand(MaterialColor.SAND));

    public static final RegistryObject<Block> BAMBOO_BUNDLE = register(
            "bamboo_bundle", Builder.bundle(Block.Properties.create(Material.WOOD).sound(SoundType.BAMBOO).hardnessAndResistance(0.2F, 5.0F)));
    public static final RegistryObject<Block> THATCH_BUNDLE = register(
            "thatch_bundle", Builder.bundle(Block.Properties.create(Material.WOOD).sound(SoundType.PLANT).hardnessAndResistance(0.2F, 5.0F)));

    public static final RegistryObject<Block> MAHOGANY_PLANKS = register("mahogany_planks", Builder.plank(MaterialColor.BROWN));
    public static final RegistryObject<Block> PALM_PLANKS = register("palm_planks", Builder.plank(MaterialColor.WOOD));
    public static final RegistryObject<Block> MAHOGANY_LOG = register("mahogany_log", Builder.log(MaterialColor.WOOD, MaterialColor.BROWN));
    public static final RegistryObject<Block> PALM_LOG = register("palm_log", Builder.log(MaterialColor.WOOD, MaterialColor.BROWN));

    public static final RegistryObject<Block> PALM_STAIRS = register(
            "palm_stairs", Builder.stairs(PALM_PLANKS));
    public static final RegistryObject<Block> MAHOGANY_STAIRS = register(
            "mahogany_stairs", Builder.stairs(MAHOGANY_PLANKS));
    public static final RegistryObject<Block> THATCH_STAIRS = register(
            "thatch_stairs", Builder.stairs(THATCH_BUNDLE));
    public static final RegistryObject<Block> THATCH_STAIRS_FUZZY = register(
            "thatch_stairs_fuzzy", Builder.stairs(THATCH_BUNDLE, BlockRenderLayer.CUTOUT_MIPPED));
    public static final RegistryObject<Block> BAMBOO_STAIRS = register(
            "bamboo_stairs", Builder.stairs(BAMBOO_BUNDLE));
    public static final RegistryObject<Block> CHUNK_STAIRS = register(
            "chunk_stairs", Builder.stairs(CHUNK));

    public static final RegistryObject<Block> COCONUT = register(
            "coconut", () -> new CoconutBlock(Block.Properties.create(Material.GOURD).hardnessAndResistance(2.0f).sound(SoundType.STONE)));

    public static final RegistryObject<Block> BAMBOO_SLAB = register(
            "bamboo_slab", Builder.slab(BAMBOO_BUNDLE));
    public static final RegistryObject<Block> THATCH_SLAB = register(
            "thatch_slab", Builder.slab(THATCH_BUNDLE));
    public static final RegistryObject<Block> CHUNK_SLAB = register(
            "chunk_slab", Builder.slab(CHUNK));
    public static final RegistryObject<Block> PALM_SLAB = register(
            "palm_slab", Builder.slab(PALM_PLANKS));
    public static final RegistryObject<Block> MAHOGANY_SLAB = register(
            "mahogany_slab", Builder.slab(MAHOGANY_PLANKS));

    public static final RegistryObject<Block> MAHOGANY_LEAVES = register("mahogany_leaves", Builder.leaves());
    public static final RegistryObject<Block> PALM_LEAVES = register("palm_leaves", Builder.leaves());
    public static final RegistryObject<Block> KAPOK_LEAVES = register("kapok_leaves", Builder.leaves());
    public static final RegistryObject<Block> FRUIT_LEAVES = register("fruit_leaves", Builder.leaves());
    public static final RegistryObject<Block> GRAPEFRUIT_LEAVES = register("grapefruit_leaves", Builder.leaves());
    public static final RegistryObject<Block> LEMON_LEAVES = register("lemon_leaves", Builder.leaves());
    public static final RegistryObject<Block> LIME_LEAVES = register("lime_leaves", Builder.leaves());
    public static final RegistryObject<Block> ORANGE_LEAVES = register("orange_leaves", Builder.leaves());

    public static final RegistryObject<SaplingBlock> GRAPEFRUIT_SAPLING = register("grapefruit_sapling", Builder.sapling(TropicraftTrees.GRAPEFRUIT));
    public static final RegistryObject<SaplingBlock> LEMON_SAPLING = register("lemon_sapling", Builder.sapling(TropicraftTrees.LEMON));
    public static final RegistryObject<SaplingBlock> LIME_SAPLING = register("lime_sapling", Builder.sapling(TropicraftTrees.LIME));
    public static final RegistryObject<SaplingBlock> ORANGE_SAPLING = register("orange_sapling", Builder.sapling(TropicraftTrees.ORANGE));
    public static final RegistryObject<SaplingBlock> MAHOGANY_SAPLING = register("mahogany_sapling", Builder.sapling(TropicraftTrees.RAINFOREST));
    public static final RegistryObject<SaplingBlock> PALM_SAPLING = register(
            "palm_sapling", Builder.sapling(TropicraftTrees.PALM, () -> Blocks.SAND, CORAL_SAND, FOAMY_SAND, VOLCANIC_SAND, PURIFIED_SAND, MINERAL_SAND));

    public static final RegistryObject<Block> BAMBOO_FENCE = register("bamboo_fence", Builder.fence(BAMBOO_BUNDLE));
    public static final RegistryObject<Block> THATCH_FENCE = register("thatch_fence", Builder.fence(THATCH_BUNDLE));
    public static final RegistryObject<Block> CHUNK_FENCE = register("chunk_fence", Builder.fence(CHUNK));
    public static final RegistryObject<Block> PALM_FENCE = register("palm_fence", Builder.fence(PALM_PLANKS));
    public static final RegistryObject<Block> MAHOGANY_FENCE = register("mahogany_fence", Builder.fence(MAHOGANY_PLANKS));

    public static final RegistryObject<Block> BAMBOO_FENCE_GATE = register("bamboo_fence_gate", Builder.fenceGate(BAMBOO_BUNDLE));
    public static final RegistryObject<Block> THATCH_FENCE_GATE = register("thatch_fence_gate", Builder.fenceGate(THATCH_BUNDLE));
    public static final RegistryObject<Block> CHUNK_FENCE_GATE = register("chunk_fence_gate", Builder.fenceGate(CHUNK));
    public static final RegistryObject<Block> PALM_FENCE_GATE = register("palm_fence_gate", Builder.fenceGate(PALM_PLANKS));
    public static final RegistryObject<Block> MAHOGANY_FENCE_GATE = register("mahogany_fence_gate", Builder.fenceGate(MAHOGANY_PLANKS));

    public static final RegistryObject<Block> BAMBOO_DOOR = register(
            "bamboo_door", () -> new DoorBlock(Block.Properties.create(Material.BAMBOO).hardnessAndResistance(1.0F).sound(SoundType.BAMBOO)) {});
    public static final RegistryObject<Block> MAHOGANY_DOOR = register(
            "mahogany_door", () -> new DoorBlock(Block.Properties.from(Blocks.OAK_DOOR)) {});
    public static final RegistryObject<Block> THATCH_DOOR = register(
            "thatch_door", () -> new DoorBlock(Block.Properties.from(THATCH_BUNDLE.get())) {});
    
    public static final RegistryObject<Block> BAMBOO_TRAPDOOR = register(
            "bamboo_trapdoor", () -> new TrapDoorBlock(Block.Properties.from(BAMBOO_DOOR.get())) {});
    public static final RegistryObject<Block> MAHOGANY_TRAPDOOR = register(
            "mahogany_trapdoor", () -> new TrapDoorBlock(Block.Properties.from(Blocks.OAK_TRAPDOOR)) {});
    public static final RegistryObject<Block> THATCH_TRAPDOOR = register(
            "thatch_trapdoor", () -> new TrapDoorBlock(Block.Properties.from(THATCH_BUNDLE.get())) {});

    public static final RegistryObject<Block> IRIS = register(
            "iris", () -> new TallFlowerBlock(Block.Properties.create(Material.TALL_PLANTS).doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT)));
    public static final RegistryObject<Block> PINEAPPLE = register(
            "pineapple", () -> new PineappleBlock(Block.Properties.create(Material.TALL_PLANTS).tickRandomly().doesNotBlockMovement().hardnessAndResistance(0).sound(SoundType.PLANT)));

    public static final RegistryObject<Block> SMALL_BONGO_DRUM = register("small_bongo_drum", Builder.bongo(BongoDrumBlock.Size.SMALL));
    public static final RegistryObject<Block> MEDIUM_BONGO_DRUM = register("medium_bongo_drum", Builder.bongo(BongoDrumBlock.Size.MEDIUM));
    public static final RegistryObject<Block> LARGE_BONGO_DRUM = register("large_bongo_drum", Builder.bongo(BongoDrumBlock.Size.LARGE));

    public static final RegistryObject<Block> BAMBOO_LADDER = register(
            "bamboo_ladder", () -> new LadderBlock(Block.Properties.create(Material.BAMBOO).sound(SoundType.BAMBOO)) {});

    public static final RegistryObject<Block> BAMBOO_CHEST = register(
            "bamboo_chest", () -> new BambooChestBlock(Block.Properties.create(Material.BAMBOO).sound(SoundType.BAMBOO)));
    public static final RegistryObject<Block> SIFTER = register(
            "sifter", () -> new SifterBlock(Block.Properties.create(Material.WOOD)));
    public static final RegistryObject<Block> DRINK_MIXER = register(
            "drink_mixer", () -> new DrinkMixerBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(2, 30)),
            () -> drinkMixerRenderer());
    public static final RegistryObject<Block> VOLCANO = register(
            "volcano", () -> new VolcanoBlock(Block.Properties.from(Blocks.BEDROCK)));
    
    public static final RegistryObject<Block> TIKI_TORCH = register(
            "tiki_torch", () -> new TikiTorchBlock(Block.Properties.from(Blocks.TORCH).sound(SoundType.WOOD).lightValue(0)));
    
    public static final RegistryObject<FlowerPotBlock> BAMBOO_FLOWER_POT = register(
            "bamboo_flower_pot", Builder.tropicraftPot());
    
    static {
        Stream.of(PALM_SAPLING, MAHOGANY_SAPLING, GRAPEFRUIT_SAPLING, LEMON_SAPLING, LIME_SAPLING,
                ORANGE_SAPLING, COMMELINA_DIFFUSA, CROCOSMIA, ORCHID, CANNA, ANEMONE, ORANGE_ANTHURIUM,
                RED_ANTHURIUM, MAGIC_MUSHROOM, PATHOS, ACAI_VINE, CROTON, DRACAENA, FERN, FOLIAGE, BROMELIAD)
        .forEach(block -> {
            String name = block.getId().getPath();
            registerNoItem("bamboo_potted_" + name, Builder.tropicraftPot(block));
            registerNoItem("potted_" + name, Builder.vanillaPot(block));
        });

        Stream.of(Blocks.OAK_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.JUNGLE_SAPLING,
                Blocks.ACACIA_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.FERN, Blocks.DANDELION, Blocks.POPPY,
                Blocks.BLUE_ORCHID, Blocks.ALLIUM, Blocks.AZURE_BLUET, Blocks.RED_TULIP, Blocks.ORANGE_TULIP,
                Blocks.WHITE_TULIP, Blocks.PINK_TULIP, Blocks.OXEYE_DAISY, Blocks.CORNFLOWER, Blocks.LILY_OF_THE_VALLEY,
                Blocks.WITHER_ROSE, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.DEAD_BUSH, Blocks.CACTUS)
        .forEach(block -> registerNoItem("bamboo_potted_" + block.getRegistryName().getPath(), Builder.tropicraftPot(() -> block)));
    }

    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup) {
        return register(name, sup, TropicraftBlocks::itemDefault);
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
        return register(name, sup, block -> item(block, renderMethod));
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, ItemGroup tab) {
        return register(name, sup, block -> item(block, tab));
    }
    
    private static <T extends Block> RegistryObject<T> register(String name, Supplier<? extends T> sup, Function<RegistryObject<T>, Supplier<? extends Item>> itemCreator) {
        RegistryObject<T> ret = registerNoItem(name, sup);
        ITEMS.register(name, itemCreator.apply(ret));
        return ret;
    }
    
    private static <T extends Block> RegistryObject<T> registerNoItem(String name, Supplier<? extends T> sup) {
        return BLOCKS.register(name, sup);
    }

    private static Supplier<BlockItem> itemDefault(final RegistryObject<? extends Block> block) {
        return item(block, Tropicraft.TROPICRAFT_ITEM_GROUP);
    }

    private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final Supplier<Callable<ItemStackTileEntityRenderer>> renderMethod) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(Tropicraft.TROPICRAFT_ITEM_GROUP).setTEISR(renderMethod));
    }

    private static Supplier<BlockItem> item(final RegistryObject<? extends Block> block, final ItemGroup itemGroup) {
        return () -> new BlockItem(block.get(), new Item.Properties().group(itemGroup));
    }
    
    @OnlyIn(Dist.CLIENT)
    private static Callable<ItemStackTileEntityRenderer> drinkMixerRenderer() {
        return DrinkMixerItemstackRenderer::new;
    }
}

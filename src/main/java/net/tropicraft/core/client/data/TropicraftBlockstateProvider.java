package net.tropicraft.core.client.data;

import static net.minecraftforge.client.model.generators.ConfiguredModel.allRotations;
import static net.minecraftforge.client.model.generators.ConfiguredModel.allYRotations;

import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.FenceBlock;
import net.minecraft.block.FenceGateBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.LadderBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.block.SaplingBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.StairsBlock;
import net.minecraft.block.TallFlowerBlock;
import net.minecraft.block.TrapDoorBlock;
import net.minecraft.block.WallBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.BlockTropicraftSand;
import net.tropicraft.core.common.block.BongoDrumBlock;
import net.tropicraft.core.common.block.TikiTorchBlock;
import net.tropicraft.core.common.block.TikiTorchBlock.TorchSection;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.TropicsFlowerBlock;

public class TropicraftBlockstateProvider extends BlockStateProvider {

    public TropicraftBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Info.MODID, exFileHelper);
    }
    
    public ExistingFileHelper getExistingHelper() {
        return existingFileHelper;
    }
    
    @Override
    public String getName() {
        return "Tropicraft Blockstates/Block Models";
    }

    @Override
    protected void registerStatesAndModels() {        
        simpleBlock(TropicraftBlocks.CHUNK, applyRotations());
        
        // Ores and storage blocks
        simpleBlock(TropicraftBlocks.AZURITE_ORE);
        simpleBlock(TropicraftBlocks.EUDIALYTE_ORE);
        simpleBlock(TropicraftBlocks.MANGANESE_ORE);
        simpleBlock(TropicraftBlocks.SHAKA_ORE);
        simpleBlock(TropicraftBlocks.ZIRCON_ORE);
        
        simpleBlock(TropicraftBlocks.AZURITE_BLOCK);
        simpleBlock(TropicraftBlocks.EUDIALYTE_BLOCK);
        simpleBlock(TropicraftBlocks.ZIRCON_BLOCK);
        
        // All flowers
        TropicraftBlocks.FLOWERS.entrySet().forEach(e ->
            simpleBlock(e.getValue(), withExistingParent(e.getKey().getId(), "block/cross")
                    .texture("cross", "tropicraft:block/flower/" + e.getKey().getId())));
        
        // Purified sand
        ModelFile normal = cubeAll(TropicraftBlocks.PURIFIED_SAND);
        ModelFile calcified = cubeTop(TropicraftBlocks.PURIFIED_SAND, "calcified");
        ModelFile dune1 = cubeTop(TropicraftBlocks.PURIFIED_SAND, "dune1");
        ModelFile dune2 = cubeTop(TropicraftBlocks.PURIFIED_SAND, "dune2");
        ModelFile starfish = cubeTop(TropicraftBlocks.PURIFIED_SAND, "starfish");

        getVariantBuilder(TropicraftBlocks.PURIFIED_SAND.get())
            .partialState().with(BlockTropicraftSand.UNDERWATER, false)
                .addModels(allRotations(normal, false, 50))
                .addModels(allYRotations(calcified, 0, false, 5))
            .partialState().with(BlockTropicraftSand.UNDERWATER, true)
                .addModels(allRotations(normal, false, 50))
                .addModels(allYRotations(dune1, 0, false, 10))
                .addModels(allYRotations(dune2, 0, false, 10))
                .addModels(allYRotations(starfish, 0, false));
        
        // Other sands
        simpleBlock(TropicraftBlocks.PACKED_PURIFIED_SAND, applyRotations());
        simpleBlock(TropicraftBlocks.CORAL_SAND, applyRotations());
        simpleBlock(TropicraftBlocks.FOAMY_SAND, applyRotations());
        simpleBlock(TropicraftBlocks.VOLCANIC_SAND, applyRotations());
        simpleBlock(TropicraftBlocks.MINERAL_SAND, applyRotations());
        
        // Bundles
        axisBlock(TropicraftBlocks.BAMBOO_BUNDLE, "bamboo");
        axisBlock(TropicraftBlocks.THATCH_BUNDLE, "thatch");
        
        // Planks & Logs
        simpleBlock(TropicraftBlocks.MAHOGANY_PLANKS);
        simpleBlock(TropicraftBlocks.PALM_PLANKS);
        
        logBlock(TropicraftBlocks.MAHOGANY_LOG.get());
        logBlock(TropicraftBlocks.PALM_LOG.get());
        
        // Stairs & Slabs
        stairsBlock(TropicraftBlocks.BAMBOO_STAIRS, "bamboo_side", "bamboo_end");
        stairsBlock(TropicraftBlocks.THATCH_STAIRS, "thatch_side", "thatch_end");
        stairsBlock(TropicraftBlocks.CHUNK_STAIRS, "chunk");
        stairsBlock(TropicraftBlocks.PALM_STAIRS, "palm_planks");
        stairsBlock(TropicraftBlocks.MAHOGANY_STAIRS, "mahogany_planks");

        ModelFile fuzzyThatch = fuzzyStairs("thatch_stairs_fuzzy", "thatch_side", "thatch_end", "thatch_grass");
        ModelFile fuzzyThatchOuter = fuzzyStairsOuter("thatch_stairs_fuzzy_outer", "thatch_side", "thatch_end", "thatch_grass");
        stairsBlock(TropicraftBlocks.THATCH_STAIRS_FUZZY.get(), fuzzyThatch, getExistingFile(modLoc("thatch_stairs_inner")), fuzzyThatchOuter);
        
        slabBlock(TropicraftBlocks.BAMBOO_SLAB, TropicraftBlocks.BAMBOO_BUNDLE, "bamboo_side", "bamboo_end");
        slabBlock(TropicraftBlocks.THATCH_SLAB, TropicraftBlocks.THATCH_BUNDLE, "thatch_side", "thatch_end");
        slabBlock(TropicraftBlocks.CHUNK_SLAB, TropicraftBlocks.CHUNK);
        slabBlock(TropicraftBlocks.PALM_SLAB, TropicraftBlocks.PALM_PLANKS);
        slabBlock(TropicraftBlocks.MAHOGANY_SLAB, TropicraftBlocks.MAHOGANY_PLANKS);
        
        // Leaves
        simpleBlock(TropicraftBlocks.MAHOGANY_LEAVES);
        simpleBlock(TropicraftBlocks.PALM_LEAVES);
        simpleBlock(TropicraftBlocks.KAPOK_LEAVES);
        simpleBlock(TropicraftBlocks.FRUIT_LEAVES);
        simpleBlock(TropicraftBlocks.GRAPEFRUIT_LEAVES);
        simpleBlock(TropicraftBlocks.LEMON_LEAVES);
        simpleBlock(TropicraftBlocks.LIME_LEAVES);
        simpleBlock(TropicraftBlocks.ORANGE_LEAVES);
        
        // Saplings
        sapling(TropicraftBlocks.MAHOGANY_SAPLING);
        sapling(TropicraftBlocks.PALM_SAPLING);
        sapling(TropicraftBlocks.GRAPEFRUIT_SAPLING);
        sapling(TropicraftBlocks.LEMON_SAPLING);
        sapling(TropicraftBlocks.LIME_SAPLING);
        sapling(TropicraftBlocks.ORANGE_SAPLING);
        
        // Fences, Gates, and Walls
        fenceBlock(TropicraftBlocks.BAMBOO_FENCE, "bamboo_side");
        fenceBlock(TropicraftBlocks.THATCH_FENCE, "thatch_side");
        fenceBlock(TropicraftBlocks.CHUNK_FENCE, "chunk");
        fenceBlock(TropicraftBlocks.PALM_FENCE, "palm_planks");
        fenceBlock(TropicraftBlocks.MAHOGANY_FENCE, "mahogany_planks");
        
        fenceGateBlock(TropicraftBlocks.BAMBOO_FENCE_GATE, "bamboo_side");
        fenceGateBlock(TropicraftBlocks.THATCH_FENCE_GATE, "thatch_side");
        fenceGateBlock(TropicraftBlocks.CHUNK_FENCE_GATE, "chunk");
        fenceGateBlock(TropicraftBlocks.PALM_FENCE_GATE, "palm_planks");
        fenceGateBlock(TropicraftBlocks.MAHOGANY_FENCE_GATE, "mahogany_planks");

        wallBlock(TropicraftBlocks.CHUNK_WALL, "chunk");

        // Doors and Trapdoors
        doorBlock(TropicraftBlocks.BAMBOO_DOOR);
        doorBlock(TropicraftBlocks.THATCH_DOOR);
        doorBlock(TropicraftBlocks.PALM_DOOR);
        doorBlock(TropicraftBlocks.MAHOGANY_DOOR);
        
        trapdoorBlock(TropicraftBlocks.BAMBOO_TRAPDOOR);
        trapdoorBlock(TropicraftBlocks.THATCH_TRAPDOOR);
        trapdoorBlock(TropicraftBlocks.PALM_TRAPDOOR);
        trapdoorBlock(TropicraftBlocks.MAHOGANY_TRAPDOOR);
        
        // Misc remaining blocks
        doublePlant(TropicraftBlocks.IRIS);
        doublePlant(TropicraftBlocks.PINEAPPLE);
        
        bongo(TropicraftBlocks.SMALL_BONGO_DRUM);
        bongo(TropicraftBlocks.MEDIUM_BONGO_DRUM);
        bongo(TropicraftBlocks.LARGE_BONGO_DRUM);
        
        ModelFile bambooLadder = withExistingParent(name(TropicraftBlocks.BAMBOO_LADDER), "ladder")
                .texture("particle", blockTexture(TropicraftBlocks.BAMBOO_LADDER))
                .texture("texture", blockTexture(TropicraftBlocks.BAMBOO_LADDER));
        getVariantBuilder(TropicraftBlocks.BAMBOO_LADDER.get()) // TODO make horizontalBlock etc support this case
            .forAllStatesExcept(state -> ConfiguredModel.builder()
                    .modelFile(bambooLadder)
                    .rotationY((int) state.get(BlockStateProperties.HORIZONTAL_FACING).getHorizontalAngle())
                    .build(),
                LadderBlock.WATERLOGGED);
        
        noModelBlock(TropicraftBlocks.BAMBOO_CHEST, modBlockLoc("bamboo_side"));
        simpleBlock(TropicraftBlocks.SIFTER);
        noModelBlock(TropicraftBlocks.DRINK_MIXER, blockTexture(TropicraftBlocks.CHUNK));
        
        simpleBlock(TropicraftBlocks.VOLCANO, getExistingFile(mcLoc("block/bedrock")));
        
        ModelFile tikiLower = torch("tiki_torch_lower", modBlockLoc("tiki_torch_lower"));
        ModelFile tikiUpper = torch("tiki_torch_upper", modBlockLoc("tiki_torch_upper"));
        getVariantBuilder(TropicraftBlocks.TIKI_TORCH.get())
            .forAllStates(state -> ConfiguredModel.builder()
                    .modelFile(state.get(TikiTorchBlock.SECTION) == TorchSection.UPPER ? tikiUpper : tikiLower).build());
        
        simpleBlock(TropicraftBlocks.COCONUT, cross("coconut", modBlockLoc("coconut")));
        
        flowerPot(TropicraftBlocks.BAMBOO_FLOWER_POT, TropicraftBlocks.BAMBOO_FLOWER_POT, modBlockLoc("bamboo_side"));
        
        for (RegistryObject<FlowerPotBlock> block : TropicraftBlocks.BAMBOO_POTTED_TROPICS_PLANTS) {
            flowerPot(block, TropicraftBlocks.BAMBOO_FLOWER_POT, modBlockLoc("bamboo_side"));
        }
        for (RegistryObject<FlowerPotBlock> block : TropicraftBlocks.VANILLA_POTTED_TROPICS_PLANTS) {
            flowerPot(block, Blocks.FLOWER_POT.delegate);
        }
        for (RegistryObject<FlowerPotBlock> block : TropicraftBlocks.BAMBOO_POTTED_VANILLA_PLANTS) {
            flowerPot(block, TropicraftBlocks.BAMBOO_FLOWER_POT, modBlockLoc("bamboo_side"));
        }
        
        simpleBlock(TropicraftBlocks.WATER_BARRIER, getBuilder(name(TropicraftBlocks.WATER_BARRIER)).texture("particle", mcLoc("item/barrier")));
        
        withExistingParent("bamboo_item_frame", "item_frame")
            .texture("particle", modBlockLoc("bamboo_side"))
            .texture("wood", modBlockLoc("bamboo_side"));
        
        withExistingParent("bamboo_item_frame_map", "item_frame_map")
            .texture("particle", modBlockLoc("bamboo_side"))
            .texture("wood", modBlockLoc("bamboo_side"));
    }

    private static Function<ModelFile, ConfiguredModel[]> applyRotations() {
        return f -> allRotations(f, false);
    }
    
    private static Function<ModelFile, ConfiguredModel[]> applyYRotations(int x) {
        return f -> allYRotations(f, x, false);
    }
    
    private String name(Supplier<? extends Block> block) {
        return block.get().getRegistryName().getPath();
    }
    
    private ResourceLocation blockTexture(Supplier<? extends Block> block) {
        ResourceLocation base = block.get().getRegistryName();
        return new ResourceLocation(base.getNamespace(), folder + "/" + base.getPath());
    }
    
    private ResourceLocation modBlockLoc(String texture) {
        return modLoc("block/" + texture);
    }
    
    private ModelFile cubeAll(Supplier<? extends Block> block) {
        return cubeAll(block.get());
    }
    
    private ModelFile cubeTop(Supplier<? extends Block> block, String suffix) {
        return cubeTop(name(block) + "_" + suffix, blockTexture(block), modBlockLoc(name(block) + "_" + suffix));
    }
    
    private void simpleBlock(Supplier<? extends Block> block) {
        simpleBlock(block.get());
    }
    
    private void simpleBlock(Supplier<? extends Block> block, ModelFile model) {
        simpleBlock(block.get(), model);
    }
    
    private void simpleBlock(Supplier<? extends Block> block, Function<ModelFile, ConfiguredModel[]> expander) {
        simpleBlock(block.get(), expander);
    }
    
    private void noModelBlock(Supplier<? extends Block> block) {
        noModelBlock(block, blockTexture(block));
    }
    
    private void noModelBlock(Supplier<? extends Block> block, ResourceLocation particle) {
        simpleBlock(block, getBuilder(name(block)).texture("particle", particle));
    }
    
    private void axisBlock(Supplier<? extends RotatedPillarBlock> block, String texture) {
        axisBlock(block.get(), modBlockLoc(texture));
    }
    
    private void stairsBlock(Supplier<? extends StairsBlock> block, String name) {
        stairsBlock(block, name, name);
    }
    
    private void stairsBlock(Supplier<? extends StairsBlock> block, String side, String topBottom) {
        stairsBlock(block.get(), modBlockLoc(side), modBlockLoc(topBottom), modBlockLoc(topBottom));
    }
    
    private ModelFile fuzzyStairs(String name, String parent, String side, String end, String cross) {
        return withExistingParent(name, modLoc(parent))
                .texture("side", modBlockLoc(side))
                .texture("bottom", modBlockLoc(end))
                .texture("top", modBlockLoc(end))
                .texture("cross", modBlockLoc(cross));
    }
    
    private ModelFile fuzzyStairs(String name, String side, String end, String cross) {
        return fuzzyStairs(name, "stairs_fuzzy", side, end, cross);
    }
    
    private ModelFile fuzzyStairsOuter(String name, String side, String end, String cross) {
        return fuzzyStairs(name, "stairs_fuzzy_outer", side, end, cross);
    }
    
    private void slabBlock(Supplier<? extends SlabBlock> block, Supplier<? extends Block> doubleslab) {
        slabBlock(block, doubleslab, name(doubleslab));
    }
    
    private void slabBlock(Supplier<? extends SlabBlock> block, Supplier<? extends Block> doubleslab, String texture) {
        slabBlock(block, doubleslab, texture, texture);
    }
    
    private void slabBlock(Supplier<? extends SlabBlock> block, Supplier<? extends Block> doubleslab, String side, String end) {
        slabBlock(block.get(), doubleslab.get().getRegistryName(), modBlockLoc(side), modBlockLoc(end), modBlockLoc(end));
    }

    private void sapling(Supplier<? extends SaplingBlock> block) {
        simpleBlock(block, cross(name(block), blockTexture(block)));
    }

    private void fenceBlock(Supplier<? extends FenceBlock> block, String texture) {
        fenceBlock(block.get(), modBlockLoc(texture));
        fenceInventory(name(block) + "_inventory", modBlockLoc(texture));
    }
    
    private void fenceGateBlock(Supplier<? extends FenceGateBlock> block, String texture) {
        fenceGateBlock(block.get(), modBlockLoc(texture));
    }
    
    private void wallBlock(Supplier<? extends WallBlock> block, String texture) {
        wallBlock(block.get(), modBlockLoc(texture));
        wallInventory(name(block) + "_inventory", modBlockLoc(texture));
    }
    
    private void doorBlock(Supplier<? extends DoorBlock> block) {
        doorBlock(block.get(), modBlockLoc(name(block) + "_bottom"), modBlockLoc(name(block) + "_top"));
    }
    
    private void trapdoorBlock(Supplier<? extends TrapDoorBlock> block) {
        trapdoorBlock(block.get(), blockTexture(block), true);
    }
    
    private void doublePlant(Supplier<? extends DoublePlantBlock> block) {
        String name = name(block);
        getVariantBuilder(block.get())
            .partialState().with(TallFlowerBlock.HALF, DoubleBlockHalf.LOWER).addModels(new ConfiguredModel(cross(name + "_bottom", modBlockLoc(name + "_bottom"))))
            .partialState().with(TallFlowerBlock.HALF, DoubleBlockHalf.UPPER).addModels(new ConfiguredModel(cross(name + "_top", modBlockLoc(name + "_top"))));
    }
    
    private void bongo(Supplier<? extends BongoDrumBlock> block) {
        BongoDrumBlock.Size size = block.get().getSize();
        AxisAlignedBB bb = size.shape.getBoundingBox();
        simpleBlock(block.get(), 
            cubeBottomTop(name(block), modBlockLoc("bongo_side"), modBlockLoc("bongo_bottom"), modBlockLoc("bongo_top"))
                .element()
                    .from((float) bb.minX * 16, (float) bb.minY * 16, (float) bb.minZ * 16)
                    .to((float) bb.maxX * 16, (float) bb.maxY * 16, (float) bb.maxZ * 16)
                    .allFaces((dir, face) -> face
                            .texture(dir.getAxis().isHorizontal() ? "#side" : dir == Direction.DOWN ? "#bottom": "#top")
                            .cullface(dir.getAxis().isVertical() ? dir : null))
                    .end());
    }
    
    private void flowerPot(Supplier<? extends FlowerPotBlock> full, Supplier<? extends Block> empty) {
        flowerPot(full, empty, blockTexture(empty));
    }

    private void flowerPot(Supplier<? extends FlowerPotBlock> full, Supplier<? extends Block> empty, ResourceLocation particle) {
        Block flower = full.get().func_220276_d();
        boolean isVanilla = flower.getRegistryName().getNamespace().equals("minecraft");
        String parent = flower == Blocks.AIR ? "flower_pot" : !isVanilla ? "flower_pot_cross" : folder + "/potted_" + name(flower.delegate);
        BlockModelBuilder model = withExistingParent(name(full), parent)
                .texture("flowerpot", blockTexture(empty))
                .texture("dirt", mcLoc("block/dirt"))
                .texture("particle", modBlockLoc("bamboo_side"));
        if (!isVanilla) {
            model.texture("plant", flower instanceof TropicsFlowerBlock ? modLoc(folder + "/flower/" + name(flower.delegate)) : blockTexture(flower));
        }
        simpleBlock(full, model);
    }
}

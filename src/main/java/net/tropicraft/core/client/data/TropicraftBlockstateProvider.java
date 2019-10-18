package net.tropicraft.core.client.data;

import static net.minecraftforge.client.model.generators.ConfiguredModel.allRotations;
import static net.minecraftforge.client.model.generators.ConfiguredModel.allYRotations;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.block.FenceBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockstateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.BlockTropicraftSand;
import net.tropicraft.core.common.block.TropicraftBlocks;

public class TropicraftBlockstateProvider extends BlockstateProvider {

    public TropicraftBlockstateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Info.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {        
        simple(TropicraftBlocks.CHUNK, allRotations(cubeAll("chunk", "block/chunk"), false));
        
        simple(TropicraftBlocks.AZURITE_ORE);
        simple(TropicraftBlocks.EUDIALYTE_ORE);
        simple(TropicraftBlocks.MANGANESE_ORE);
        simple(TropicraftBlocks.SHAKA_ORE);
        simple(TropicraftBlocks.ZIRCON_ORE);
        
        simple(TropicraftBlocks.AZURITE_BLOCK);
        simple(TropicraftBlocks.EUDIALYTE_BLOCK);
        simple(TropicraftBlocks.ZIRCON_BLOCK);
        
        TropicraftBlocks.FLOWERS.entrySet().forEach(e ->
            simple(e.getValue(), withExistingParent(e.getKey().getId(), "block/cross")
                    .texture("cross", "tropicraft:block/flower/" + e.getKey().getId())));
        
        // Purified sand
        ModelFile normal = cubeAll(TropicraftBlocks.PURIFIED_SAND);
        ModelFile calcified = cubeTop(TropicraftBlocks.PURIFIED_SAND, "purified_sand_calcified");
        ModelFile dune1 = cubeTop(TropicraftBlocks.PURIFIED_SAND, "purified_sand_dune1");
        ModelFile dune2 = cubeTop(TropicraftBlocks.PURIFIED_SAND, "purified_sand_dune2");
        ModelFile starfish = cubeTop(TropicraftBlocks.PURIFIED_SAND, "purified_sand_starfish");

        getVariantBuilder(TropicraftBlocks.PURIFIED_SAND.get())
            .partialState().with(BlockTropicraftSand.UNDERWATER, false)
                .addModels(allRotations(normal, false, 50))
                .addModels(allYRotations(calcified, 0, false, 5))
            .partialState().with(BlockTropicraftSand.UNDERWATER, true)
                .addModels(allRotations(normal, false, 50))
                .addModels(allYRotations(dune1, 0, false, 10))
                .addModels(allYRotations(dune2, 0, false, 10))
                .addModels(allYRotations(starfish, 0, false));
            
        fence(TropicraftBlocks.BAMBOO_FENCE, "bamboo", "bamboo_side");
        fence(TropicraftBlocks.THATCH_FENCE, "thatch", "thatch_side");
        fence(TropicraftBlocks.CHUNK_FENCE, "chunk", "chunk");
        fence(TropicraftBlocks.PALM_FENCE, "palm", "palm_planks");
        fence(TropicraftBlocks.MAHOGANY_FENCE, "mahogany", "mahogany_planks");
    }
    
    private BlockModelBuilder cubeAll(Supplier<? extends Block> block) {
        String name = block.get().getRegistryName().getPath();
        return cubeAll(name, "block/" + name);
    }
    
    private BlockModelBuilder cubeTop(Supplier<? extends Block> block, String variantName) {
        return withExistingParent(variantName, "block/cube_top")
                .texture("side", "block/" + block.get().getRegistryName().getPath())
                .texture("top", "block/" + variantName);
    }
    
    private void simple(Supplier<? extends Block> block) {
        simple(block, cubeAll(block));
    }
    
    private void simple(Supplier<? extends Block> block, ModelFile model) {
        simple(block, new ConfiguredModel(model));
    }
    
    private void simple(Supplier<? extends Block> block, ConfiguredModel... models) {
        getVariantBuilder(block.get())
            .partialState().setModels(models);
    }
    
    private void fence(Supplier<? extends FenceBlock> block, String name, String textureName) {
        ModelFile post = withExistingParent(name + "_fence_post", "block/fence_post")
                .texture("texture", new ResourceLocation(this.modid, "block/" + textureName));
        
        ModelFile side = withExistingParent(name + "_fence_side", "block/fence_side")
                .texture("texture", new ResourceLocation(this.modid, "block/" + textureName));
        
        getMultipartBuilder(block.get())
                .part().modelFile(post).addModel().build()
                .part().modelFile(side).uvLock(true).addModel()
                    .condition(FenceBlock.NORTH, true).build()
                .part().modelFile(side).rotationY(90).uvLock(true).addModel()
                    .condition(FenceBlock.EAST, true).build()
                .part().modelFile(side).rotationY(180).uvLock(true).addModel()
                    .condition(FenceBlock.SOUTH, true).build()
                .part().modelFile(side).rotationY(270).uvLock(true).addModel()
                    .condition(FenceBlock.WEST, true).build();
    }
}

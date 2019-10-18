package net.tropicraft.core.client.data;

import java.util.Arrays;
import java.util.function.Supplier;
import java.util.stream.IntStream;

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

        fence(TropicraftBlocks.BAMBOO_FENCE, "bamboo", "bamboo_side");
        fence(TropicraftBlocks.THATCH_FENCE, "thatch", "thatch_side");
        fence(TropicraftBlocks.CHUNK_FENCE, "chunk", "chunk");
        fence(TropicraftBlocks.PALM_FENCE, "palm", "palm_planks");
        fence(TropicraftBlocks.MAHOGANY_FENCE, "mahogany", "mahogany_planks");
    }
    
    private IntStream validRotations() {
        return IntStream.range(0, 4).map(i -> i * 90);
    }

    private ConfiguredModel[] allRotationsWithX(ModelFile model, int x, boolean uvlock) {
        return validRotations()
                .mapToObj(y -> new ConfiguredModel(model, x, y, uvlock))
                .toArray(ConfiguredModel[]::new);
    }
    
    private ConfiguredModel[] allRotations(ModelFile model, boolean uvlock) {
        return validRotations()
                .mapToObj(x -> allRotationsWithX(model, x, uvlock))
                .flatMap(Arrays::stream)
                .toArray(ConfiguredModel[]::new);
    }
    
    private BlockModelBuilder withExistingParent(String name, String parent) {
        return getBuilder(name).parent(getExistingFile(parent));
    }
    
    private BlockModelBuilder cubeAll(String name, String texture) {
        return withExistingParent(name, "block/cube_all").texture("all", new ResourceLocation(Info.MODID, texture));
    }
    
    private void simple(Supplier<? extends Block> block) {
        String name = block.get().getRegistryName().getPath();
        simple(block, cubeAll(name, "block/" + name));
    }
    
    private void simple(Supplier<? extends Block> block, ModelFile model) {
        simple(block, new ConfiguredModel(model));
    }
    
    private void simple(Supplier<? extends Block> block, ConfiguredModel... models) {
        getVariantBuilder(block.get())
            .partialState().setModel(models);
    }
    
    private void fence(Supplier<? extends FenceBlock> block, String name, String textureName) {
        ModelFile post = withExistingParent(name + "_fence_post", "block/fence_post")
                .texture("texture", new ResourceLocation(Info.MODID, "block/" + textureName));
        
        ModelFile side = withExistingParent(name + "_fence_side", "block/fence_side")
                .texture("texture", new ResourceLocation(Info.MODID, "block/" + textureName));
        
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

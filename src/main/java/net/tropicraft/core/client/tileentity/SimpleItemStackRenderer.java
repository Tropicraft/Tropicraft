package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.tropicraft.core.common.block.AirCompressorBlock;
import net.tropicraft.core.common.block.BambooChestBlock;
import net.tropicraft.core.common.block.DrinkMixerBlock;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.AirCompressorTileEntity;
import net.tropicraft.core.common.block.tileentity.BambooChestTileEntity;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class SimpleItemStackRenderer<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer {
    private BlockEntity dummy;
    private BambooChestTileEntity chestBlockEntity = new BambooChestTileEntity(BlockPos.ZERO, TropicraftBlocks.BAMBOO_CHEST.get().defaultBlockState());
    private DrinkMixerTileEntity drinkMixerBlockEntity= new DrinkMixerTileEntity(BlockPos.ZERO, TropicraftBlocks.DRINK_MIXER.get().defaultBlockState());
    private AirCompressorTileEntity airCompressorBlockEntity= new AirCompressorTileEntity(BlockPos.ZERO, TropicraftBlocks.AIR_COMPRESSOR.get().defaultBlockState());

    private Supplier<? extends BlockEntityType> blockEntityType;

    
    public SimpleItemStackRenderer(Supplier<? extends BlockEntityType> supBE) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        blockEntityType = supBE;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        if (dummy == null) {
            dummy = ForgeRegistries.BLOCK_ENTITIES.getValue(blockEntityType.get().getRegistryName()).create(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
        }
        //BlockEntityRenderDispatcher.instance.getRenderer(te).render(te, 0, matrixStack, buffer, combinedLight, combinedOverlay);

        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            Block block = ((BlockItem) item).getBlock();
            if(block instanceof BambooChestBlock){
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(chestBlockEntity, matrixStack, buffer, combinedLight, combinedOverlay);
            }

            else if(block instanceof DrinkMixerBlock){
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(drinkMixerBlockEntity, matrixStack, buffer, combinedLight, combinedOverlay);
            }

            else if(block instanceof AirCompressorBlock){
                Minecraft.getInstance().getBlockEntityRenderDispatcher().renderItem(airCompressorBlockEntity, matrixStack, buffer, combinedLight, combinedOverlay);
            }

            else{
                BlockEntityRenderer<BlockEntity> renderer = Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(dummy);
                renderer.render(null, 0, matrixStack, buffer, combinedLight, combinedOverlay);
            }
        }
    }
}

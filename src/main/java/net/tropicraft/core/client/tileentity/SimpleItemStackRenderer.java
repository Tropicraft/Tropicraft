package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.LazyLoadedValue;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class SimpleItemStackRenderer<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer {
    private BlockEntity dummy;
    private final ResourceLocation typeId;
    
    public SimpleItemStackRenderer(ResourceLocation typeId) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.typeId = typeId;
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {

        if (dummy == null) {
            dummy = ForgeRegistries.BLOCK_ENTITIES.getValue(typeId).create(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
        }

        Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(dummy).render(dummy, 0, matrixStack, buffer, combinedLight, combinedOverlay);
        //BlockEntityRenderDispatcher.instance.getRenderer(te).render(te, 0, matrixStack, buffer, combinedLight, combinedOverlay);
    }
}

package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.LazyLoadedValue;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class SimpleItemStackRenderer<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer {

    private final LazyLoadedValue<T> te;
    
    public SimpleItemStackRenderer(Supplier<T> te) {
        this.te = new LazyLoadedValue<>(te);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transform, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        T te = this.te.get();
        BlockEntityRenderDispatcher.instance.getRenderer(te).render(te, 0, matrixStack, buffer, combinedLight, combinedOverlay);
    }
}

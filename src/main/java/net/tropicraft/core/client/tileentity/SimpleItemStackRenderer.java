package net.tropicraft.core.client.tileentity;

import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.function.Supplier;

public class SimpleItemStackRenderer<T extends BlockEntity> extends BlockEntityWithoutLevelRenderer {
    private final Supplier<T> te;
    
    public SimpleItemStackRenderer(Supplier<T> te) {
        super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        this.te = Suppliers.memoize(te::get);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        T te = this.te.get();
        Minecraft.getInstance().getBlockEntityRenderDispatcher().getRenderer(te).render(te, 0, poseStack, buffer, combinedLight, combinedOverlay);
    }
}

package net.tropicraft.core.client.tileentity;

import java.util.function.Supplier;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.LazyValue;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimpleItemStackRenderer<T extends TileEntity> extends ItemStackTileEntityRenderer {

    private final LazyValue<T> te;
    
    public SimpleItemStackRenderer(Supplier<T> te) {
        this.te = new LazyValue<>(te);
    }

    @Override
    public void render(ItemStack itemStackIn, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
    	T te = this.te.getValue();
        TileEntityRendererDispatcher.instance.getRenderer(te).render(te, 0, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn);
    }
}

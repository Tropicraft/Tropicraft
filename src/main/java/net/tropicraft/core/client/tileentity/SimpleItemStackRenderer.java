package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SimpleItemStackRenderer<T extends TileEntity> extends ItemStackTileEntityRenderer {

    private final T te;
    
    public SimpleItemStackRenderer(T te) {
        this.te = te;
    }

    @Override
    public void renderByItem(final ItemStack itemstack) {
        TileEntityRendererDispatcher.instance.getRenderer(te).render(te, 0.0D, 0.0D, 0.0D, 0.0F, -1);
    }
}

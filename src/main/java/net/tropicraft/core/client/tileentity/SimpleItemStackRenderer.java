package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;

@OnlyIn(Dist.CLIENT)
public class DrinkMixerItemstackRenderer extends ItemStackTileEntityRenderer {

    private final DrinkMixerTileEntity mixer = new DrinkMixerTileEntity();

    @Override
    public void renderByItem(final ItemStack itemstack) {
        TileEntityRendererDispatcher.instance.getRenderer(DrinkMixerTileEntity.class).render(mixer, 0.0D, 0.0D, 0.0D, 0.0F, -1);
    }
}

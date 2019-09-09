package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;
import net.tropicraft.core.common.block.tileentity.TropicraftTileEntityTypes;

public class DrinkMixerItemstackRenderer extends ItemStackTileEntityRenderer {

    private static final DrinkMixerTileEntity mixer = TropicraftTileEntityTypes.DRINK_MIXER.create();

    @Override
    public void renderByItem(final ItemStack itemstack) {
        TileEntityRendererDispatcher.instance.getRenderer(DrinkMixerTileEntity.class).render(mixer, 0.0D, 0.0D, 0.0D, 0.0F, -1);
    }
}

package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.block.model.ModelBlock;
import net.tropicraft.core.common.block.BlockDrinkMixer;
import net.tropicraft.core.common.block.tileentity.IMachineTile;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;

public abstract class TileEntityMachineRenderer<T extends TileEntity & IMachineTile> extends TileEntitySpecialRenderer<T> {

	/**
	 * EIHMixer model instance
	 */
	private final ModelBlock model;


	public TileEntityMachineRenderer(ModelBlock model) {
	    this.model = model;
	}

	@Override
    public void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, int destroyStage) {
	    if (te == null) {
	        return;
	    }
        GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5f,(float)y+1.5f,(float)z+0.5f);
		GlStateManager.rotate(180f, 1f, 0f, 1f);

		if (te == null || te.getWorld() == null) {
			GlStateManager.rotate(180f, 0f, 1f, 0f);
		} else {
			EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockDrinkMixer.FACING);
			GlStateManager.rotate(facing.getHorizontalAngle(), 0, 1, 0);
		}

		if (te != null && te.isActive()) {
			float angle = MathHelper.sin((float)(25f * 2f * Math.PI * (te.getProgress() + partialTicks) / TileEntityDrinkMixer.TICKS_TO_MIX)) * 15f;
			GlStateManager.rotate(angle, 0f, 1f, 0f);
		}

		TropicraftRenderUtils.bindTextureTE(model.getTexture(te.isActive()));
		model.renderAsBlock();
		
		if (te != null) {
		    renderIngredients(te);
		}

		GlStateManager.popMatrix();
	}
	
	public abstract void renderIngredients(T te);
}

package net.tropicraft.core.client.tileentity;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.block.model.MachineModel;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

public abstract class TileEntityMachineRenderer<T extends TileEntity & IMachineTile> extends TileEntitySpecialRenderer<T> {

    private final Block block;
	private final MachineModel<T> model;

	public TileEntityMachineRenderer(Block block, MachineModel<T> model) {
	    this.block = block;
	    this.model = model;
	}

	@Override
    public void renderTileEntityAt(T te, double x, double y, double z, float partialTicks, int destroyStage) {

        GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5f,(float)y+1.5f,(float)z+0.5f);
		GlStateManager.rotate(180f, 1f, 0f, 1f);

		if (te == null || te.getWorld() == null) {
			GlStateManager.rotate(180f, 0f, 1f, 0f);
		} else {
		    IBlockState state = te.getWorld().getBlockState(te.getPos());
		    EnumFacing facing;
		    if (state.getBlock() != this.block) {
		        facing = EnumFacing.NORTH;
		    }
			facing = te.getFacing(state);
			GlStateManager.rotate(facing.getHorizontalAngle(), 0, 1, 0);
		}

		if (te != null && te.isActive()) {
		    animationTransform(te, partialTicks);
		}

		TropicraftRenderUtils.bindTextureTE(model.getTexture(te));
		model.renderAsBlock(te);
		
		if (te != null) {
		    renderIngredients(te);
		}

		GlStateManager.popMatrix();
	}
	
	protected void animationTransform(T te, float partialTicks) {
        float angle = MathHelper.sin((float) (25f * 2f * Math.PI * te.getProgress(partialTicks))) * 15f;
        GlStateManager.rotate(angle, 0f, 1f, 0f);
	}
	
	protected abstract void renderIngredients(T te);
}

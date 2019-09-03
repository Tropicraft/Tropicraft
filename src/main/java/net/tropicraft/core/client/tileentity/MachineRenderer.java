package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.MachineModel;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

public abstract class MachineRenderer<T extends TileEntity & IMachineTile> extends TileEntityRenderer<T> {
    private final Block block;
	private final MachineModel<T> model;

	public MachineRenderer(final Block block, final MachineModel<T> model) {
	    this.block = block;
	    this.model = model;
	}

	@Override
    public void render(T te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
		GlStateManager.translatef((float) x + 0.5f,(float) y + 1.5f,(float) z + 0.5f);
		GlStateManager.rotatef(180f, 1f, 0f, 1f);

		if (te == null || te.getWorld() == null) {
			GlStateManager.rotatef(180f, 0f, 1f, 0f);
		} else {
		    BlockState state = te.getWorld().getBlockState(te.getPos());
		    Direction facing;
		    if (state.getBlock() != this.block) {
		        facing = Direction.NORTH;
		    } else {
		        facing = te.getDirection(state);
		    }
			GlStateManager.rotatef(facing.getHorizontalAngle(), 0, 1, 0);
		}

		if (te != null && te.isActive()) {
		    animationTransform(te, partialTicks);
		}

		GlStateManager.enableRescaleNormal();
		TropicraftRenderUtils.bindTextureTE(model.getTexture(te));
		model.renderAsBlock(te);
		
		if (te != null) {
		    renderIngredients(te);
		}

		GlStateManager.disableRescaleNormal();

		GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager.popMatrix();
	}
	
	protected void animationTransform(T te, float partialTicks) {
        float angle = MathHelper.sin((float) (25f * 2f * Math.PI * te.getProgress(partialTicks))) * 15f;
        GlStateManager.rotatef(angle, 0f, 1f, 0f);
	}
	
	protected abstract void renderIngredients(T te);
}

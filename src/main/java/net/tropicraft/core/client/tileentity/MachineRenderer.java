package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.MachineModel;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

public abstract class MachineRenderer<T extends TileEntity & IMachineTile> extends TileEntityRenderer<T> {
    private final Block block;
	private final MachineModel<T> model;

	public MachineRenderer(final TileEntityRendererDispatcher rendererDispatcher, final Block block, final MachineModel<T> model) {
		super(rendererDispatcher);
	    this.block = block;
	    this.model = model;
	}

	@Override
    public void render(T te, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
		stack.push();
		stack.translate(0.5f, 1.5f, 0.5f);
		stack.rotate(Vector3f.XP.rotationDegrees(180));
		stack.rotate(Vector3f.ZP.rotation(180));

		if (te == null || te.getWorld() == null) {
			stack.rotate(Vector3f.YP.rotationDegrees(180));
		} else {
		    BlockState state = te.getWorld().getBlockState(te.getPos());
		    Direction facing;
		    if (state.getBlock() != this.block) {
		        facing = Direction.NORTH;
		    } else {
		        facing = te.getDirection(state);
		    }
			stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalAngle()));
		}

		if (te != null && te.isActive()) {
		    animationTransform(te, partialTicks);
		}

		RenderSystem.enableRescaleNormal();
//		TropicraftRenderUtils.bindTextureTE(model.getTexture(te));
//		model.renderAsBlock(te);

		TropicraftRenderUtils.renderModel(getMaterial(), model, stack, buffer, combinedLightIn, combinedOverlayIn);

		if (te != null) {
		    renderIngredients(te, stack, buffer);
		}

		RenderSystem.disableRescaleNormal();

		//GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();
	}

	protected abstract Material getMaterial();
	
	protected void animationTransform(T te, float partialTicks) {
        float angle = MathHelper.sin((float) (25f * 2f * Math.PI * te.getProgress(partialTicks))) * 15f;
        GlStateManager.rotatef(angle, 0f, 1f, 0f);
	}
	
	protected abstract void renderIngredients(final T te, final MatrixStack stack, final IRenderTypeBuffer buffer);
}

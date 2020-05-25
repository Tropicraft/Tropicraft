package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.MachineModel;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

public abstract class MachineRenderer<T extends TileEntity & IMachineTile> extends TileEntityRenderer<T> {
    private final Block block;
	protected final MachineModel<T> model;

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
		//stack.rotate(Vector3f.ZP.rotationDegrees(180));

		if (te == null || te.getWorld() == null) {
			stack.rotate(Vector3f.YP.rotationDegrees(-90));
		} else {
		    BlockState state = te.getWorld().getBlockState(te.getPos());
		    Direction facing;
		    if (state.getBlock() != this.block) {
		        facing = Direction.NORTH;
		    } else {
		        facing = te.getDirection(state);
		    }
			stack.rotate(Vector3f.YP.rotationDegrees(facing.getHorizontalAngle() + 90));
		}

		if (te != null && te.isActive()) {
		    animationTransform(te, stack, partialTicks);
		}

//		TropicraftRenderUtils.bindTextureTE(model.getTexture(te));
//		model.renderAsBlock(te);

		// Get light above, since block is solid
		TropicraftRenderUtils.renderModel(getMaterial(), model, stack, buffer, getCombinedLight(te.getWorld(), te.getPos().up()), combinedOverlayIn);

		if (te != null) {
		    renderIngredients(te, stack, buffer, combinedLightIn, combinedOverlayIn);
		}

		//GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		stack.pop();
	}

	protected int getCombinedLight(final IWorld world, final BlockPos pos) {
		// Get light above, since block is solid
		return world == null ? 0xF000F0 : WorldRenderer.getCombinedLight(world, pos.up());
	}

	protected abstract Material getMaterial();
	
	protected void animationTransform(T te, MatrixStack stack, float partialTicks) {
        float angle = MathHelper.sin((float) (25f * 2f * Math.PI * te.getProgress(partialTicks))) * 15f;
        stack.rotate(Vector3f.YP.rotationDegrees(angle));
	}
	
	protected abstract void renderIngredients(final T te, final MatrixStack stack, final IRenderTypeBuffer buffer, int packedLightIn, int combinedOverlayIn);
}

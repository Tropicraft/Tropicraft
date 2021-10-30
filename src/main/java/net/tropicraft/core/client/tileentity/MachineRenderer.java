package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import com.mojang.math.Vector3f;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.MachineModel;
import net.tropicraft.core.common.block.tileentity.IMachineTile;

public abstract class MachineRenderer<T extends BlockEntity & IMachineTile> extends BlockEntityRenderer<T> {
    private final Block block;
    protected final MachineModel<T> model;

    public MachineRenderer(final BlockEntityRenderDispatcher rendererDispatcher, final Block block, final MachineModel<T> model) {
        super(rendererDispatcher);
        this.block = block;
        this.model = model;
    }

    @Override
    public void render(T te, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        stack.pushPose();
        stack.translate(0.5f, 1.5f, 0.5f);
        stack.mulPose(Vector3f.XP.rotationDegrees(180));
        //stack.rotate(Vector3f.ZP.rotationDegrees(180));

        if (te == null || te.getLevel() == null) {
            stack.mulPose(Vector3f.YP.rotationDegrees(-90));
        } else {
            BlockState state = te.getLevel().getBlockState(te.getBlockPos());
            Direction facing;
            if (state.getBlock() != this.block) {
                facing = Direction.NORTH;
            } else {
                facing = te.getDirection(state);
            }
            stack.mulPose(Vector3f.YP.rotationDegrees(facing.toYRot() + 90));
        }

        if (te != null && te.isActive()) {
            animationTransform(te, stack, partialTicks);
        }

//        TropicraftRenderUtils.bindTextureTE(model.getTexture(te));
//        model.renderAsBlock(te);

        // Get light above, since block is solid
        TropicraftRenderUtils.renderModel(getMaterial(), model, stack, buffer, combinedLightIn, combinedOverlayIn);

        if (te != null) {
            renderIngredients(te, stack, buffer, combinedLightIn, combinedOverlayIn);
        }

        //GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        stack.popPose();
    }

    protected abstract Material getMaterial();
    
    protected void animationTransform(T te, PoseStack stack, float partialTicks) {
        float angle = Mth.sin((float) (25f * 2f * Math.PI * te.getProgress(partialTicks))) * 15f;
        stack.mulPose(Vector3f.YP.rotationDegrees(angle));
    }
    
    protected abstract void renderIngredients(final T te, final PoseStack stack, final MultiBufferSource buffer, int packedLightIn, int combinedOverlayIn);
}

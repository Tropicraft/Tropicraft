package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnchantingTableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.client.extensions.IBlockEntityRendererExtension;
import net.tropicraft.core.client.entity.model.MachineModel;
import net.tropicraft.core.common.block.tileentity.IMachineBlock;

public abstract class MachineRenderer<T extends BlockEntity & IMachineBlock> implements BlockEntityRenderer<T> {
    private final Block block;
    protected final MachineModel model;

    public MachineRenderer(BlockEntityRendererProvider.Context context, Block block, MachineModel model) {
        this.block = block;
        this.model = model;
    }

    @Override
    public void render(T te, float partialTicks, PoseStack stack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        stack.pushPose();
        stack.translate(0.5f, 1.5f, 0.5f);
        stack.mulPose(Axis.XP.rotationDegrees(180));
        //stack.rotate(Axis.ZP.rotationDegrees(180));

        if (te == null || te.getLevel() == null) {
            stack.mulPose(Axis.YP.rotationDegrees(-90));
        } else {
            BlockState state = te.getLevel().getBlockState(te.getBlockPos());
            Direction facing;
            if (state.getBlock() != block) {
                facing = Direction.NORTH;
            } else {
                facing = te.getDirection(state);
            }
            stack.mulPose(Axis.YP.rotationDegrees(facing.toYRot() + 90));
        }

        if (te != null && te.isActive()) {
            animationTransform(te, stack, partialTicks);
        }

//        TropicraftRenderUtils.bindTextureTE(model.getTexture(te));
//        model.renderAsBlock(te);

        // Get light above, since block is solid
        RenderType renderType = model.renderType(getMaterial().texture());
        model.renderToBuffer(stack, buffer.getBuffer(renderType), combinedLightIn, combinedOverlayIn);

        if (te != null) {
            renderIngredients(te, stack, buffer, combinedLightIn, combinedOverlayIn);
        }

        //GlStateManager.color4f(1.0f, 1.0f, 1.0f, 1.0f);
        stack.popPose();
    }

    protected abstract Material getMaterial();

    protected void animationTransform(T te, PoseStack stack, float partialTicks) {
        float angle = Mth.sin((float) (25.0f * 2.0f * Math.PI * te.getProgress(partialTicks))) * 15.0f;
        stack.mulPose(Axis.YP.rotationDegrees(angle));
    }

    protected abstract void renderIngredients(T te, PoseStack stack, MultiBufferSource buffer, int packedLightIn, int combinedOverlayIn);

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0);
    }
}

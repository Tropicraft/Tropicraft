package net.tropicraft.core.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.client.entity.model.MachineModel;
import net.tropicraft.core.common.block.tileentity.IMachineBlock;

public abstract class MachineBlockEntityRenderer<T extends BlockEntity & IMachineBlock> implements BlockEntityRenderer<T> {
    protected final MachineModel model;

    public MachineBlockEntityRenderer(MachineModel model) {
        this.model = model;
    }

    @Override
    public void render(T blockEntity, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords, int overlayCoords, Vec3 cameraPos) {
        poseStack.pushPose();
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        Direction direction = blockEntity.getDirection(blockEntity.getBlockState());
        poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot() + 90));

        if (blockEntity.isActive()) {
            animationTransform(blockEntity, poseStack, partialTicks);
        }

        model.renderToBuffer(poseStack, getMaterial().buffer(bufferSource, model::renderType), lightCoords, overlayCoords);

        renderIngredients(blockEntity, poseStack, bufferSource, lightCoords, overlayCoords);

        poseStack.popPose();
    }

    protected abstract Material getMaterial();

    protected void animationTransform(T blockEntity, PoseStack poseStack, float partialTicks) {
        float angle = Mth.sin((float) (25.0f * 2.0f * Math.PI * blockEntity.getProgress(partialTicks))) * 15.0f;
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
    }

    protected abstract void renderIngredients(T blockEntity, PoseStack poseStack, MultiBufferSource bufferSource, int lightCoords, int overlayCoords);

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0);
    }
}

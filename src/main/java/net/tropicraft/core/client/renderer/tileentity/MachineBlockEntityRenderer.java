package net.tropicraft.core.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.sprite.SpriteGetter;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.client.entity.model.MachineModel;
import net.tropicraft.core.common.block.tileentity.IMachineBlock;
import org.jspecify.annotations.Nullable;

public abstract class MachineBlockEntityRenderer<T extends BlockEntity & IMachineBlock, S extends MachineBlockEntityRenderer.RenderState> implements BlockEntityRenderer<T, S> {
    protected final MachineModel model;
    private final SpriteGetter sprites;

    public MachineBlockEntityRenderer(MachineModel model, SpriteGetter sprites) {
        this.model = model;
        this.sprites = sprites;
    }

    @Override
    public void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.direction = blockEntity.getDirection(blockEntity.getBlockState());
        state.active = blockEntity.isActive();
        if (state.active) {
            state.progress = blockEntity.getProgress(partialTicks);
        }
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.5f, 1.5f, 0.5f);
        poseStack.mulPose(Axis.XP.rotationDegrees(180));

        Direction direction = state.direction;
        poseStack.mulPose(Axis.YP.rotationDegrees(direction.toYRot() + 90));

        if (state.active) {
            animationTransform(state, poseStack);
        }

        submitNodeCollector.submitModel(model, Unit.INSTANCE, poseStack, state.lightCoords, OverlayTexture.NO_OVERLAY, CommonColors.WHITE, getSprite(), sprites, EntityRenderState.NO_OUTLINE, null);

        submitIngredients(state, poseStack, submitNodeCollector);

        poseStack.popPose();
    }

    protected abstract SpriteId getSprite();

    protected void animationTransform(S state, PoseStack poseStack) {
        float angle = Mth.sin((float) (25.0f * 2.0f * Math.PI * state.progress)) * 15.0f;
        poseStack.mulPose(Axis.YP.rotationDegrees(angle));
    }

    protected abstract void submitIngredients(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector);

    @Override
    public AABB getRenderBoundingBox(T blockEntity) {
        BlockPos pos = blockEntity.getBlockPos();
        return new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1.0, pos.getY() + 2.0, pos.getZ() + 1.0);
    }

    public static abstract class RenderState extends BlockEntityRenderState {
        public Direction direction = Direction.NORTH;
        public boolean active;
        public float progress;
    }
}

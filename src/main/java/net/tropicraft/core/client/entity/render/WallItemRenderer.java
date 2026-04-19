package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ItemFrameRenderState;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

public class WallItemRenderer extends EntityRenderer<WallItemEntity, ItemFrameRenderState> {
    private final ItemModelResolver itemModelResolver;

    public WallItemRenderer(EntityRendererProvider.Context context) {
        super(context);
        itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public ItemFrameRenderState createRenderState() {
        return new ItemFrameRenderState();
    }

    @Override
    public void extractRenderState(WallItemEntity entity, ItemFrameRenderState state, float partialTick) {
        super.extractRenderState(entity, state, partialTick);
        state.direction = entity.getDirection();
        itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.FIXED, entity);
        state.rotation = entity.getRotation();
    }

    @Override
    public void submit(ItemFrameRenderState state, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        stack.pushPose();

        if (state.direction.getAxis().isHorizontal()) {
            stack.mulPose(Axis.YP.rotationDegrees(180.0f - state.direction.get2DDataValue() * 90.0f));
        } else {
            stack.mulPose(Axis.XP.rotationDegrees(-90.0f * state.direction.getAxisDirection().getStep()));
        }

        stack.mulPose(Axis.ZP.rotationDegrees(state.rotation * 360 / 8.0f));
        if (!state.item.isEmpty()) {
            state.item.submit(stack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, EntityRenderState.NO_OUTLINE);
        }
        stack.popPose();

        super.submit(state, stack, submitNodeCollector, camera);
    }
}

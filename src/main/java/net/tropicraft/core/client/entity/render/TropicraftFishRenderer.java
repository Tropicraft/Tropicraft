package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.animal.fish.AbstractFish;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.TropicraftSpecialRenderHelper;
import net.tropicraft.core.client.entity.model.TropicraftFishModel;
import net.tropicraft.core.client.entity.render.state.FishRenderState;
import net.tropicraft.core.common.entity.underdasea.IAtlasFish;

// TODO: Please rework this :(
public abstract class TropicraftFishRenderer<T extends AbstractFish> extends MobRenderer<T, FishRenderState, TropicraftFishModel> {
    private static final Identifier TEXTURE = Tropicraft.id("textures/entity/tropical_fish.png");

    private final TropicraftSpecialRenderHelper renderHelper;

    public TropicraftFishRenderer(EntityRendererProvider.Context context, TropicraftFishModel modelbase, float f) {
        super(context, modelbase, f);
        renderHelper = new TropicraftSpecialRenderHelper();
    }

    /**
     * This override is a hack
     */

    @Override
    public void submit(FishRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        boolean isVisible = isBodyVisible(state);
        boolean shouldRender = !isVisible && !state.isInvisibleToPlayer;
        if (isVisible || shouldRender) {
            submitNodeCollector.submitCustomGeometry(poseStack, getRenderType(state, isVisible, shouldRender, state.appearsGlowing()), (pose, buffer) -> {
                PoseStack stack = new PoseStack();
                stack.mulPose(pose.pose());
                renderFishy(state, stack, buffer, state.lightCoords, getOverlayCoords(state, 0.0f));
            });
        }
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    protected void renderFishy(FishRenderState state, PoseStack stack, VertexConsumer buffer, int light, int overlay) {
        stack.pushPose();
        stack.mulPose(Axis.YP.rotationDegrees(state.yRot));
        stack.mulPose(Axis.XP.rotationDegrees(180));
        stack.scale(0.3f, 0.3f, 0.5f);
        stack.translate(0.85f, -0.3f, 0.0f);

        int fishTex = state.atlasSlot * 2;
        renderHelper.renderFish(stack, buffer, fishTex, light, overlay);

        stack.translate(-1.7f, 0, 0);
        stack.translate(0.85f, 0, 0.025f);
        stack.mulPose(Axis.YP.rotation(model.tail.yRot));
        stack.translate(-.85f, 0, -0.025f);
        renderHelper.renderFish(stack, buffer, fishTex + 1, light, overlay);

        stack.popPose();
    }

    @Override
    protected void scale(FishRenderState state, PoseStack stack) {
        stack.scale(0.75f, 0.20f, 0.20f);
    }

    @Override
    public FishRenderState createRenderState() {
        return new FishRenderState();
    }

    @Override
    public void extractRenderState(T entity, FishRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.atlasSlot = entity instanceof IAtlasFish fish ? fish.getAtlasSlot() : 0;
    }

    @Override
    public Identifier getTextureLocation(FishRenderState state) {
        return TEXTURE;
    }
}

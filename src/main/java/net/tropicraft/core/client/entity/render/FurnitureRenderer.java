package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.render.state.FurnitureRenderState;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import org.joml.Quaternionf;

public abstract class FurnitureRenderer<T extends FurnitureEntity, S extends FurnitureRenderState> extends EntityRenderer<T, S> {
    private static final Axis DEFAULT_ROCKING_AXIS = angle -> new Quaternionf().rotationAxis(angle, 1.0f, 0.0f, 1.0f);

    private final Identifier baseTexture;
    private final Identifier colorTexture;
    private final EntityModel<? super S> model;
    private final float scale;

    public FurnitureRenderer(EntityRendererProvider.Context context, String textureName, EntityModel<? super S> model) {
        this(context, textureName, model, 1);
    }

    public FurnitureRenderer(EntityRendererProvider.Context context, String textureName, EntityModel<? super S> model, float scale) {
        super(context);
        baseTexture = Tropicraft.id("textures/entity/" + textureName + "_base_layer.png");
        colorTexture = Tropicraft.id("textures/entity/" + textureName + "_color_layer.png");
        this.model = model;
        this.scale = scale;
    }

    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.color = entity.getColor();
        state.rockingAngle = getRockingAngle(entity, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
    }

    @Override
    public void submit(S state, PoseStack stack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        stack.pushPose();
        stack.translate(0, getYOffset(), 0);
        stack.mulPose(Axis.YP.rotationDegrees(180 - state.yRot));
        // it used to scale by 0.25, but for some reason this gets it to be around the proper size again?
        stack.scale(scale, scale, scale);
        setupTransforms(stack);

        float rockingAngle = state.rockingAngle;
        if (!Mth.equal(rockingAngle, 0.0f)) {
            stack.mulPose(getRockingAxis().rotationDegrees(rockingAngle));
        }

        int color = state.color.getTextureDiffuseColor();

        // Draw uncolored layer
        stack.scale(-1.0f, -1.0f, 1.0f);
        submitNodeCollector.submitModel(model, state, stack, baseTexture, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);

        // Draw the colored part
        submitNodeCollector.submitModel(model, state, stack, model.renderType(colorTexture), state.lightCoords, OverlayTexture.NO_OVERLAY, color, null, state.outlineColor, null);

        super.submit(state, stack, submitNodeCollector, camera);
        stack.popPose();
    }

    protected double getYOffset() {
        return 0.3125;
    }

    protected void setupTransforms(PoseStack stack) {

    }

    protected float getRockingAngle(T entity, float partialTicks) {
        float f2 = entity.getTimeSinceHit() - partialTicks;
        float f3 = entity.getDamage() - partialTicks;
        if (f3 < 0.0f) {
            f3 = 0.0f;
        }
        if (f2 > 0.0f) {
            return ((Mth.sin(f2) * f2 * f3) / getRockAmount()) * (float) entity.getForwardDirection();
        }
        return 0;
    }

    protected Axis getRockingAxis() {
        return DEFAULT_ROCKING_AXIS;
    }

    protected float getRockAmount() {
        return 10;
    }
}

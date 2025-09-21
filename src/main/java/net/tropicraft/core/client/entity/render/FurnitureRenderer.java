package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.entity.render.state.FurnitureRenderState;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import org.joml.Quaternionf;

public abstract class FurnitureRenderer<T extends FurnitureEntity, S extends FurnitureRenderState> extends EntityRenderer<T, S> {
    private static final Axis DEFAULT_ROCKING_AXIS = angle -> new Quaternionf().rotationAxis(angle, 1.0f, 0.0f, 1.0f);

    private final ResourceLocation baseTexture;
    private final ResourceLocation colorTexture;
    private final EntityModel<? super S> model;
    private final float scale;

    public FurnitureRenderer(EntityRendererProvider.Context context, String textureName, EntityModel<? super S> model) {
        this(context, textureName, model, 1);
    }

    public FurnitureRenderer(EntityRendererProvider.Context context, String textureName, EntityModel<? super S> model, float scale) {
        super(context);
        baseTexture = Tropicraft.location("textures/entity/" + textureName + "_base_layer.png");
        colorTexture = Tropicraft.location("textures/entity/" + textureName + "_color_layer.png");
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
    public void render(S state, PoseStack stack, MultiBufferSource buffer, int packedLightIn) {
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
        VertexConsumer builder = buffer.getBuffer(model.renderType(baseTexture));
        stack.scale(-1.0f, -1.0f, 1.0f);
        model.renderToBuffer(stack, builder, packedLightIn, OverlayTexture.NO_OVERLAY);

        // Draw the colored part
        builder = buffer.getBuffer(model.renderType(colorTexture));
        model.renderToBuffer(stack, builder, packedLightIn, OverlayTexture.NO_OVERLAY, color);

        super.render(state, stack, buffer, packedLightIn);
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

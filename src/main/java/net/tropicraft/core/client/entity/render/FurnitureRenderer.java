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
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;
import org.joml.Quaternionf;

public class FurnitureRenderer<T extends FurnitureEntity> extends EntityRenderer<T> {
    private static final Axis DEFAULT_ROCKING_AXIS = angle -> new Quaternionf().rotationAxis(angle, 1.0f, 0.0f, 1.0f);

    private final String textureName;
    private final EntityModel<T> model;
    private final float scale;

    public FurnitureRenderer(EntityRendererProvider.Context context, String textureName, EntityModel<T> model) {
        this(context, textureName, model, 1);
    }

    public FurnitureRenderer(EntityRendererProvider.Context context, String textureName, EntityModel<T> model, float scale) {
        super(context);
        this.textureName = textureName;
        this.model = model;
        this.scale = scale;
    }

    @Override
    public void render(T furniture, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLightIn) {
        stack.pushPose();
        stack.translate(0, getYOffset(), 0);
        stack.mulPose(Axis.YP.rotationDegrees(180 - entityYaw));
        // it used to scale by 0.25, but for some reason this gets it to be around the proper size again?
        stack.scale(scale, scale, scale);
        setupTransforms(stack);

        float rockingAngle = getRockingAngle(furniture, partialTicks);
        if (!Mth.equal(rockingAngle, 0.0f)) {
            stack.mulPose(getRockingAxis().rotationDegrees(rockingAngle));
        }

        int color = furniture.getColor().getTextureDiffuseColor();

        // Draw uncolored layer
        VertexConsumer ivertexbuilder = buffer.getBuffer(model.renderType(TropicraftRenderUtils.getTextureEntity(textureName + "_base_layer")));
        stack.scale(-1.0f, -1.0f, 1.0f);
        model.renderToBuffer(stack, ivertexbuilder, getPackedLightCoords(furniture, partialTicks), OverlayTexture.NO_OVERLAY);

        // Draw the colored part
        ivertexbuilder = buffer.getBuffer(model.renderType(TropicraftRenderUtils.getTextureEntity(textureName + "_color_layer")));
        model.renderToBuffer(stack, ivertexbuilder, getPackedLightCoords(furniture, partialTicks), OverlayTexture.NO_OVERLAY, color);

        super.render(furniture, entityYaw, partialTicks, stack, buffer, packedLightIn);
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

    @Override
    public ResourceLocation getTextureLocation(T furniture) {
        return null;
    }
}

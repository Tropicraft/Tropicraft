package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;

public class FurnitureRenderer<T extends FurnitureEntity> extends EntityRenderer<T> {
    private final String textureName;
    private final EntityModel<T> model;
    private final float scale;
    private float red = 0.0F, green = 0.0F, blue = 0.0F;

    public FurnitureRenderer(EntityRendererManager renderManager, String textureName, EntityModel<T> model) {
        this(renderManager, textureName, model, 1);
    }
    
    public FurnitureRenderer(EntityRendererManager renderManager, String textureName, EntityModel<T> model, float scale) {
        super(renderManager);
        this.textureName = textureName;
        this.model = model;
        this.scale = scale;
    }

    @Override
    public void render(T furniture, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int packedLightIn) {
        stack.push();
        stack.translate(0, getYOffset(), 0);
        stack.rotate(Vector3f.YP.rotationDegrees(180 - entityYaw));
        // it used to scale by 0.25, but for some reason this gets it to be around the proper size again?
        stack.scale(scale, scale, scale);
        setupTransforms(stack);

        final float rockingAngle = getRockingAngle(furniture, partialTicks);;
        if (!MathHelper.epsilonEquals(rockingAngle, 0.0F)) {
            stack.rotate(new Quaternion(getRockingAxis(), rockingAngle, true));
        }

        final int color = furniture.getColor().getColorValue();
        red = ColorHelper.getRed(color);
        green = ColorHelper.getGreen(color);
        blue = ColorHelper.getBlue(color);

        // Draw uncolored layer
        IVertexBuilder ivertexbuilder = buffer.getBuffer(model.getRenderType(TropicraftRenderUtils.getTextureEntity(textureName + "_base_layer")));
        stack.scale(-1.0F, -1.0F, 1.0F);
        model.render(stack, ivertexbuilder, getPackedLight(furniture, partialTicks), OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

        // Draw the colored part
        ivertexbuilder = buffer.getBuffer(model.getRenderType(TropicraftRenderUtils.getTextureEntity(textureName + "_color_layer")));
        model.render(stack, ivertexbuilder, getPackedLight(furniture, partialTicks), OverlayTexture.NO_OVERLAY, red, green, blue, 1.0F);

        super.render(furniture, entityYaw, partialTicks, stack, buffer, packedLightIn);
        stack.pop();
    }
    
    protected double getYOffset() {
        return 0.3125;
    }
    
    protected void setupTransforms(MatrixStack stack) {

    }
    
    protected float getRockingAngle(T entity, float partialTicks) {
        float f2 = entity.getTimeSinceHit() - partialTicks;
        float f3 = entity.getDamage() - partialTicks;
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }
        if (f2 > 0.0F) {
            return ((MathHelper.sin(f2) * f2 * f3) / getRockAmount()) * (float) entity.getForwardDirection();
        }
        return 0;
    }

    protected Vector3f getRockingAxis() {
        return new Vector3f(1.0F, 0.0F, 1.0F);
    }

    protected float getRockAmount() {
        return 10;
    }

    @Override
    public ResourceLocation getEntityTexture(final T furniture) {
        return null;
    }
}

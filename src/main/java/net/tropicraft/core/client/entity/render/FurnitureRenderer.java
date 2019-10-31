package net.tropicraft.core.client.entity.render;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.entity.placeable.FurnitureEntity;

public class FurnitureRenderer<T extends FurnitureEntity> extends EntityRenderer<T> {

    private final String textureName;
    private final EntityModel<T> model;
    private final float scale;
    private float red = 0.0F, green = 0.0F, blue = 0.0F;

    public FurnitureRenderer(EntityRendererManager renderManager, String textureName, EntityModel<T> model) {
        this(renderManager, textureName, model, 0.0625f);
    }
    
    public FurnitureRenderer(EntityRendererManager renderManager, String textureName, EntityModel<T> model, float scale) {
        super(renderManager);
        this.textureName = textureName;
        this.model = model;
        this.scale = scale;
    }

    @Override
    public void doRender(T entity, double x, double y, double z, float yaw, float partialTicks) {
        GlStateManager.pushMatrix();
        double yOffset = getYOffset();
        GlStateManager.translated(x, y + yOffset, z);
        GlStateManager.rotatef(180F - yaw, 0.0F, 1.0F, 0.0F);
        
        float rockingAngle = getRockingAngle(entity, partialTicks);
        if (rockingAngle != 0F) {
            boolean useZ = rockOnZAxis();
            GlStateManager.translated(0, -yOffset, 0);
            GlStateManager.rotatef(rockingAngle, useZ ? 0 : 1, 0, useZ ? 1 : 0);
            GlStateManager.translated(0, yOffset, 0);
        }

        int color = entity.getColor().getColorValue();
        red = ColorHelper.getRed(color);
        green = ColorHelper.getGreen(color);
        blue = ColorHelper.getBlue(color);

        // Draw uncolored layer
        bindTexture(TropicraftRenderUtils.getTextureEntity(textureName + "_base_layer"));
        GlStateManager.scalef(-1F, -1F, 1.0F);
        model.render(entity, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, scale);

        // Draw the colored part
        GlStateManager.pushMatrix();
        GlStateManager.color3f(red, green, blue);
        bindTexture(TropicraftRenderUtils.getTextureEntity(textureName + "_color_layer"));
        model.render(entity, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, scale);
        GlStateManager.disableBlend();
        GlStateManager.color3f(1, 1, 1);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }
    
    protected double getYOffset() {
        return 0.3125;
    }
    
    protected void setupTransforms() {
    }
    
    protected float getRockingAngle(T entity, float partialTicks) {
        float f2 = entity.getTimeSinceHit() - partialTicks;
        float f3 = entity.getDamage();
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }
        if (f2 > 0.0F) {
            return ((MathHelper.sin(f2) * f2 * f3) / 10F) * (float) entity.getForwardDirection();
        }
        return 0;
    }
    
    protected boolean rockOnZAxis() {
        return false;
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(final T chair) {
        return null;
    }
}

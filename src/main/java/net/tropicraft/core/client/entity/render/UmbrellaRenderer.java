package net.tropicraft.core.client.entity.render;

import java.nio.FloatBuffer;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.UmbrellaModel;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.entity.placeable.UmbrellaEntity;

public class UmbrellaRenderer extends EntityRenderer<UmbrellaEntity> {

    final UmbrellaModel model = new UmbrellaModel();
    FloatBuffer color;
    float red = 0.0F, green = 0.0F, blue = 0.0F, alpha = 1.0F;

    public UmbrellaRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    public void doRender(UmbrellaEntity entityUmbrella, double x, double y, double z, float yaw, float partialTicks) {
        this.shadowSize = 2.5f;
        this.shadowOpaque = 1;
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y, z);
        GlStateManager.rotatef(180F - yaw, 0.0F, 1.0F, 0.0F);
        float f2 = (float) entityUmbrella.getTimeSinceHit() - partialTicks;
        float f3 = entityUmbrella.getDamage();
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }
        if (f2 > 0.0F) {
            GlStateManager.rotatef(((MathHelper.sin(f2) * f2 * f3) / 10F) * (float) entityUmbrella.getForwardDirection(), 1.0F, 0.0F, 0.0F);
        }

        int umbrellaColor = entityUmbrella.getColor().getColorValue();
        red = ColorHelper.getRed(umbrellaColor);
        green = ColorHelper.getGreen(umbrellaColor);
        blue = ColorHelper.getBlue(umbrellaColor);

        // Draw arms of umbrella
        bindTexture(TropicraftRenderUtils.getTextureEntity("umbrella_layer"));
        GlStateManager.scalef(-1F, -1F, 1.0F);
        model.render(entityUmbrella, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.25F);

        // Draw the colored part of the umbrella
        GlStateManager.pushMatrix();
        GlStateManager.color3f(red, green, blue);
        bindTexture(TropicraftRenderUtils.getTextureEntity("umbrella_color_layer"));
        model.render(entityUmbrella, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.25F);
        GlStateManager.disableBlend();
        GlStateManager.color3f(1, 1, 1);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        super.doRender(entityUmbrella, x, y, z, yaw, partialTicks);
    }
    
    @Override
    public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks) {
        if (this.renderManager.options != null) {
            if (this.renderManager.options.entityShadows && this.shadowSize > 0.0F && !entityIn.isInvisible() && this.renderManager.isRenderShadow()) {
                // Don't do distance culling for umbrella shadows
                UmbrellaEntity entityUmbrella = (UmbrellaEntity) entityIn;
                float f2 = (float) entityUmbrella.getTimeSinceHit() - partialTicks;
                float f3 = entityUmbrella.getDamage();
                if (f3 < 0.0F) {
                    f3 = 0.0F;
                }
                // Calculate which direction the umbrella is "shaking"
                Vec3d offsetVec = Vec3d.ZERO;
                if (f2 > 0.0F) {
                    float offset = ((MathHelper.sin(f2) * f2 * f3) / 100F) * (float) entityUmbrella.getForwardDirection();
                    offsetVec = offsetVec.add(offset, 0, 0).rotateYaw((float) Math.toRadians(90 - yaw));
                }
                // Move around the shadow renderer based on the shake
                entityIn.lastTickPosX += offsetVec.x;
                entityIn.posX += offsetVec.x;
                entityIn.lastTickPosZ += offsetVec.z;
                entityIn.posZ += offsetVec.z;
                this.renderShadow(entityIn, x + offsetVec.x, y, z + offsetVec.z, shadowOpaque, partialTicks);
                entityIn.lastTickPosX -= offsetVec.x;
                entityIn.posX -= offsetVec.x;
                entityIn.lastTickPosZ -= offsetVec.z;
                entityIn.posZ -= offsetVec.z;
            }

            if (entityIn.canRenderOnFire() && !entityIn.isSpectator()) {
                this.renderEntityOnFire(entityIn, x, y, z, partialTicks);
            }
        }
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(final UmbrellaEntity umbrella) {
        return null;
    }
}

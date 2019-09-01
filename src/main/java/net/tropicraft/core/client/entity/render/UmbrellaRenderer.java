package net.tropicraft.core.client.entity.render;

import java.nio.FloatBuffer;

import javax.annotation.Nullable;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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

        float f4 = 0.75F;
        GlStateManager.scalef(f4, f4, f4);
        GlStateManager.scalef(1.0F / f4, 1.0F / f4, 1.0F / f4);

        int umbrellaColor = entityUmbrella.getColor();
        red = ColorHelper.getRed(umbrellaColor);
        green = ColorHelper.getGreen(umbrellaColor);
        blue = ColorHelper.getBlue(umbrellaColor);

        UmbrellaModel model = new UmbrellaModel();
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
        // Change the color mode back to modulation
        GlStateManager.texEnv(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        super.doRender(entityUmbrella, x, y, z, yaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(final UmbrellaEntity umbrella) {
        return null;
    }
}

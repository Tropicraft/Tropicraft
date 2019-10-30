package net.tropicraft.core.client.entity.render;

import java.nio.FloatBuffer;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ChairModel;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.entity.placeable.ChairEntity;

public class ChairRenderer extends EntityRenderer<ChairEntity> {

    final ChairModel model = new ChairModel();
    FloatBuffer color;
    float red = 0.0F, green = 0.0F, blue = 0.0F, alpha = 1.0F;

    public ChairRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    public void doRender(ChairEntity entity, double x, double y, double z, float yaw, float partialTicks) {
        this.shadowSize = 0.65f;
        this.shadowOpaque = 1;
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y + 0.3125, z);
        GlStateManager.rotatef(180F - yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translated(0, 0, -0.15);
        float f2 = (float) entity.getTimeSinceHit() - partialTicks;
        float f3 = entity.getDamage();
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }
        if (f2 > 0.0F) {
            GlStateManager.rotatef(((MathHelper.sin(f2) * f2 * f3) / 10F) * (float) entity.getForwardDirection(), 1.0F, 0.0F, 0.0F);
        }

        int color = entity.getColor().getColorValue();
        red = ColorHelper.getRed(color);
        green = ColorHelper.getGreen(color);
        blue = ColorHelper.getBlue(color);

        // Draw uncolored layer
        bindTexture(TropicraftRenderUtils.getTextureEntity("chair_layer"));
        GlStateManager.scalef(-1F, -1F, 1.0F);
        new ChairModel().render(entity, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0625F);

        // Draw the colored part
        GlStateManager.pushMatrix();
        GlStateManager.color3f(red, green, blue);
        bindTexture(TropicraftRenderUtils.getTextureEntity("chair_color_layer"));
        model.render(entity, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.disableBlend();
        GlStateManager.color3f(1, 1, 1);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(final ChairEntity chair) {
        return null;
    }
}

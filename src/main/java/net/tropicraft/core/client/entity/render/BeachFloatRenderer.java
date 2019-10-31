package net.tropicraft.core.client.entity.render;

import java.nio.FloatBuffer;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.BeachFloatModel;
import net.tropicraft.core.common.ColorHelper;
import net.tropicraft.core.common.entity.placeable.BeachFloatEntity;

public class BeachFloatRenderer extends EntityRenderer<BeachFloatEntity> {

    final BeachFloatModel model = new BeachFloatModel();
    FloatBuffer color;
    float red = 0.0F, green = 0.0F, blue = 0.0F, alpha = 1.0F;

    public BeachFloatRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
        shadowSize = .5F;
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void doRender(BeachFloatEntity entity, double x, double y, double z, float yaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(x, y + 1.5125, z);
        GlStateManager.rotatef(-yaw, 0.0F, 1.0F, 0.0F);
        GlStateManager.translated(0, 0, 0);
        float f2 = (float) entity.getTimeSinceHit() - partialTicks;
        float f3 = entity.getDamage();
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }
        if (f2 > 0.0F) {
            GlStateManager.translated(0, -1.5125, 0);
            GlStateManager.rotatef(((MathHelper.sin(f2) * f2 * f3) / 10F) * (float) entity.getForwardDirection(), 0.0F, 0.0F, 1.0F);
            GlStateManager.translated(0, 1.5125, 0);
        }

        int color = entity.getColor().getColorValue();
        red = ColorHelper.getRed(color);
        green = ColorHelper.getGreen(color);
        blue = ColorHelper.getBlue(color);

        // Draw uncolored layer
        bindTexture(TropicraftRenderUtils.getTextureEntity("beach_float_layer"));
        GlStateManager.scalef(-1F, -1F, 1.0F);
        model.render(entity, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0625F);

        // Draw the colored part
        GlStateManager.pushMatrix();
        GlStateManager.color3f(red, green, blue);
        bindTexture(TropicraftRenderUtils.getTextureEntity("beach_float_color_layer"));
        model.render(entity, 0.0F, 1.0F, 0.1F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.disableBlend();
        GlStateManager.color3f(1, 1, 1);
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
        super.doRender(entity, x, y, z, yaw, partialTicks);
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(final BeachFloatEntity chair) {
        return null;
    }

    private static float rotationYawHead, prevRotationYawHead, rotationPitch, prevRotationPitch;

    @SubscribeEvent
    public void onRenderPlayer(RenderPlayerEvent.Pre event) {
        PlayerEntity p = event.getPlayer();
        Entity riding = p.getRidingEntity();
        if (riding instanceof BeachFloatEntity) {
            BeachFloatEntity floaty = (BeachFloatEntity) riding;
            GlStateManager.pushMatrix();
            GlStateManager.translated(event.getX(), event.getY(), event.getZ());

            GlStateManager.rotatef(-(floaty.prevRotationYaw + (event.getPartialRenderTick() * (floaty.rotationYaw - floaty.prevRotationYaw))), 0, 1, 0);
            GlStateManager.translated(0, 1.55, 1.55);
            GlStateManager.rotatef(-90, 1, 0, 0);

            // Cancel out player camera rotation
            float f = p.renderYawOffset - p.prevRenderYawOffset;
            while (f < -180)
                f += 360;
            while (f >= 180)
                f -= 360;
            f = p.prevRenderYawOffset + (event.getPartialRenderTick() * f);

            GlStateManager.rotatef(f, 0, 1, 0);

            // Lock in head
            rotationYawHead = p.rotationYawHead;
            prevRotationYawHead = p.prevRotationYawHead;
            p.rotationYawHead = p.renderYawOffset;
            p.prevRotationYawHead = p.prevRenderYawOffset;
            rotationPitch = p.rotationPitch;
            prevRotationPitch = p.prevRotationPitch;
            p.rotationPitch = 10f;
            p.prevRotationPitch = 10f;

            GlStateManager.translated(-event.getX(), -event.getY(), -event.getZ());
        }
    }

    @SubscribeEvent
    public void onRenderPlayerPost(RenderPlayerEvent.Post event) {
        PlayerEntity p = event.getPlayer();
        if (p.getRidingEntity() instanceof BeachFloatEntity) {
            GlStateManager.popMatrix();
            p.rotationYawHead = rotationYawHead;
            p.prevRotationYawHead = prevRotationYawHead;
            p.rotationPitch = rotationPitch;
            p.prevRotationPitch = prevRotationPitch;
        }
    }

    @SubscribeEvent
    public void onRenderPlayerSpecials(RenderLivingEvent.Specials.Pre<?, ?> event) {
        if (event.getEntity().getRidingEntity() instanceof BeachFloatEntity) {
            event.setCanceled(true);
        }
    }
}

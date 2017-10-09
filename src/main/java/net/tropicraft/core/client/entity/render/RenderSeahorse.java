package net.tropicraft.core.client.entity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelSeahorse;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntitySeahorse;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderSeahorse extends RenderTropicraftWaterMob {

    private ModelSeahorse modelSeahorse;

    public RenderSeahorse(ModelBase model, float shadowSize) {
        super(Minecraft.getMinecraft().getRenderManager(), model, shadowSize);
        modelSeahorse = (ModelSeahorse)model;
    }

    @Override
    public void doRender(EntityTropicraftWaterBase par1EntityLiving, double x, double y, double z, float yaw, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y-2.5f, z);

        GlStateManager.rotate(par1EntityLiving.swimYaw+90f, 0F, 1.0F, 0.0F);
        super.doRender(par1EntityLiving, 0, 0, 0, par1EntityLiving.swimYaw, partialTicks);
        GlStateManager.popMatrix();
    }


    @Override
    protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
        EntitySeahorse seahorse = null;

        if (entity instanceof EntitySeahorse)
            seahorse = (EntitySeahorse)entity;

        if (seahorse == null)
            return TropicraftRenderUtils.getTextureEntity("seahorse/razz");
        else
            return TropicraftRenderUtils.getTextureEntity(String.format("seahorse/%s", seahorse.getColorName()));
    }
}
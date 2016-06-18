package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.core.client.entity.model.ModelEIH;
import net.tropicraft.core.common.entity.hostile.EntityEIH;

import org.lwjgl.opengl.GL11;

public class RenderEIH extends RenderLiving {

	private static final ResourceLocation TEXTURE_SLEEP = new ResourceLocation(Info.MODID + ":textures/entity/eih/headtext.png");
	
    public RenderEIH() {
        super(Minecraft.getMinecraft().getRenderManager(), new ModelEIH(), 0.75F);
    }

    @Override
    public void doRender(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1) {
        super.doRender((EntityEIH) entityliving, d, d1, d2, f, f1);
    }

    protected void preRenderScale(EntityEIH entityeih, float f) {
        GL11.glScalef(2.0F, 1.75F, 2.0F);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        preRenderScale((EntityEIH) entityliving, f);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        EntityEIH eih = (EntityEIH)entity;
        //TODO: sync anger/awake state to client for texture swap
        return TEXTURE_SLEEP;
        /*String texture_path = "eih/head" + (eih.isAngry() ? "angry" : eih.isAwake() ? "aware" : "") + "text";
        return TropicraftUtils.bindTextureEntity(texture_path);*/
    }
}

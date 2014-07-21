package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelEIH;
import net.tropicraft.entity.hostile.EntityEIH;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderEIH extends RenderLiving {

    public RenderEIH(ModelEIH modeleih, float f) {
        super(modeleih, f);
    }

    @Override
    public void doRender(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1) {
        super.doRender((EntityEIH) entityliving, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1) {
        doRender((EntityEIH) entity, d, d1, d2, f, f1);
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
        String texture_path = "eih/head" + (eih.isAngry() ? "angry" : eih.isAwake() ? "aware" : "") + "text";
        return TropicraftUtils.bindTextureEntity(texture_path);
    }
}

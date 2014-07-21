package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.entity.underdasea.EntityTurtleEgg;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderTurtleEgg extends RenderLiving {

    public RenderTurtleEgg(ModelBase modelbase, float f) {
        super(modelbase, f);
    }

    @Override
	protected ResourceLocation getEntityTexture(Entity entity) {
    	return TropicraftUtils.bindTextureEntity("turtle/eggText");
	}

    public void renderTurtleEgg(EntityTurtleEgg entityTurtleEgg, double d, double d1, double d2,
            float f, float f1) {
        super.doRender(entityTurtleEgg, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1) {
        renderTurtleEgg((EntityTurtleEgg) entityliving, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(Entity entity, double d, double d1, double d2,
            float f, float f1) {
        renderTurtleEgg((EntityTurtleEgg) entity, d, d1, d2, f, f1);
    }

    protected void preRenderScale(EntityTurtleEgg egg, float f) {
        GL11.glScalef(.5F, .5F, .5F);
    }

    @Override
    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
        preRenderScale((EntityTurtleEgg) entityliving, f);
    }
}
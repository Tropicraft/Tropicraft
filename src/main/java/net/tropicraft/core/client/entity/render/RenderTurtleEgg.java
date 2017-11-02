package net.tropicraft.core.client.entity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.underdasea.EntityTurtleEgg;

public class RenderTurtleEgg extends RenderLiving<EntityTurtleEgg> {

    public RenderTurtleEgg(ModelBase modelbase, float f) {
        super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
    }

    @Override
	protected ResourceLocation getEntityTexture(EntityTurtleEgg entity) {
    	return TropicraftRenderUtils.bindTextureEntity("turtle/eggText");
	}

    public void renderTurtleEgg(EntityTurtleEgg entityTurtleEgg, double d, double d1, double d2,
            float f, float f1) {
    		this.shadowOpaque = 0.5f;
    		this.shadowSize = 0.3f;
        super.doRender(entityTurtleEgg, d, d1, d2, f, f1);
    }

    @Override
    public void doRender(EntityTurtleEgg entityliving, double d, double d1, double d2,
            float f, float f1) {
        renderTurtleEgg((EntityTurtleEgg) entityliving, d, d1, d2, f, f1);
    }

    protected void preRenderScale(EntityTurtleEgg egg, float f) {
        GL11.glScalef(.5F, .5F, .5F);
    }

//    @Override
//    protected void preRenderCallback(EntityLivingBase entityliving, float f) {
//        preRenderScale((EntityTurtleEgg) entityliving, f);
//    }
}
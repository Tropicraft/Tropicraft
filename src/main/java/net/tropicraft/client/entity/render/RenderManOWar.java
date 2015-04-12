package net.tropicraft.client.entity.render;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelManOWar;
import net.tropicraft.entity.underdasea.EntityManOWar;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderManOWar extends RenderLiving {

    private ModelBase scaleAmount;

    public RenderManOWar(ModelBase modelbase, ModelBase modelbase1, float f) {
        super(modelbase, f);
        mainModel = (ModelManOWar) modelbase;
        scaleAmount = modelbase1;
    }

    protected int func_40287_a(EntityManOWar manowar, int i, float f) {
        if (i == 0) {
            setRenderPassModel(scaleAmount);
            GL11.glEnable(2977 /*
                     * GL_NORMALIZE
                     */);
            GL11.glEnable(3042 /*
                     * GL_BLEND
                     */);
            GL11.glBlendFunc(770, 771);
            return 1;
        }
        if (i == 1) {
            GL11.glDisable(3042 /*
                     * GL_BLEND
                     */);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
        return -1;
    }

    public void renderManOWar(EntityLiving entityliving, double d, double d1, double d2,
            float f, float f1) {
        super.doRender(entityliving, d, d1, d2, f, f1);
        ((ModelManOWar) mainModel).isOnGround = entityliving.onGround;

    }

    @Override
    protected int shouldRenderPass(EntityLivingBase entityliving, int i, float f) {
        return func_40287_a((EntityManOWar) entityliving, i, f);
    }
    
    @Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
    	return TropicraftUtils.bindTextureEntity("manowar");
	}
}
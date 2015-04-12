package net.tropicraft.client.entity.render;

import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.client.entity.model.ModelSeaUrchin;
import net.tropicraft.entity.underdasea.EntitySeaUrchin;
import net.tropicraft.util.TropicraftUtils;

import org.lwjgl.opengl.GL11;

public class RenderSeaUrchin extends RenderLiving {
	/**
	 * Amount freshly hatched sea urchins are scaled down while rendering.
	 */
	public static final float BABY_RENDER_SCALE = 0.5f;

	/**
	 * Amount mature sea urchins are scaled down while rendering.
	 */
	public static final float ADULT_RENDER_SCALE = 1f;

	public RenderSeaUrchin() {
		super(new ModelSeaUrchin(), 0.5f);
	}

	@Override
	public void doRender(EntityLiving par1EntityLiving, double par2,
			double par4, double par6, float par8, float par9) {
		super.doRender(par1EntityLiving, par2, par4, par6, par8, par9);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
		EntitySeaUrchin urchin = (EntitySeaUrchin) par1EntityLiving;
		float growthProgress = urchin.getGrowthProgress();
		float scale = BABY_RENDER_SCALE + growthProgress*(ADULT_RENDER_SCALE-BABY_RENDER_SCALE);
		float preScale = 0.5f;
		
		GL11.glScalef(preScale*scale, preScale*scale, preScale*scale);
	}

	@Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TropicraftUtils.bindTextureEntity("seaurchin");
	}
}
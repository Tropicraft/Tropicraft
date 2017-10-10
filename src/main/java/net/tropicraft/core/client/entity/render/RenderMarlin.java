package net.tropicraft.core.client.entity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelMarlin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityMarlin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderMarlin extends RenderTropicraftWaterMob {

	private ModelMarlin marlin;

	public RenderMarlin(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
		marlin = (ModelMarlin) mainModel;
	}
    
    @Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
    		return TropicraftRenderUtils.bindTextureEntity(entity.getTexture());
	}

	

	@Override
	public void doRender(EntityTropicraftWaterBase entityliving, double d, double d1, double d2, float f, float f1) {
		marlin.inWater = entityliving.isInWater();
		this.renderWaterMob((EntityTropicraftWaterBase) entityliving, d, d1, d2, f1);
	}

	@Override
	protected void preRenderCallback(EntityTropicraftWaterBase entityliving, float f) {
		preRenderScale((EntityMarlin) entityliving, f);
	}

	protected void preRenderScale(EntityMarlin entitymarlin, float f) {
		GL11.glScalef(1F, 1F, 1F);
	}
}
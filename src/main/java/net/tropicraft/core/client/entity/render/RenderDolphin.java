package net.tropicraft.core.client.entity.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelDolphin;
import net.tropicraft.core.client.entity.model.ModelMarlin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityDolphin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityMarlin;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

public class RenderDolphin extends RenderTropicraftWaterMob {

	private ModelDolphin dolphin;

	public RenderDolphin(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
		dolphin = (ModelDolphin) mainModel;
	}
    
    @Override

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.bindTextureEntity("dolphin");
	}

	

	@Override
	public void doRender(EntityTropicraftWaterBase entityliving, double d, double d1, double d2, float f, float f1) {
	//	dolphin.inWater = entityliving.isInWater();
	//	System.out.println(entityliving.hurtTime);
		this.renderWaterMob((EntityTropicraftWaterBase) entityliving, d, d1, d2, f1);
	}

	@Override
	protected void preRenderCallback(EntityTropicraftWaterBase entityliving, float f) {
		preRenderScale((EntityDolphin) entityliving, f);
	}

	protected void preRenderScale(EntityDolphin entitymarlin, float f) {
		GL11.glScalef(1F, 1F, 1F);
	}
}
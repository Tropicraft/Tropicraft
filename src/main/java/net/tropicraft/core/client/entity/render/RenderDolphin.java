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
	
	private boolean refreshModel = false;

	public RenderDolphin(ModelBase modelbase, float f) {
		super(Minecraft.getMinecraft().getRenderManager(), modelbase, f);
		dolphin = (ModelDolphin) mainModel;
	}
    
    @Override
	protected ResourceLocation getEntityTexture(EntityTropicraftWaterBase entity) {
		return TropicraftRenderUtils.bindTextureEntity(entity.getTexture());
	}

	@Override
	public void doRender(EntityTropicraftWaterBase entityliving, double d, double d1, double d2, float f, float f1) {
		if(refreshModel && entityliving.ticksExisted % 20 == 0) {
			this.mainModel = new ModelDolphin();
			this.dolphin = new ModelDolphin();
			refreshModel = false;
		}
		
		//refreshModel = true;
		
		
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
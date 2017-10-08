package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelSeaUrchin;
import net.tropicraft.core.common.entity.underdasea.EntitySeaUrchin;

public class RenderSeaUrchin extends RenderLiving<EntitySeaUrchin> {
	/**
	 * Amount freshly hatched sea urchins are scaled down while rendering.
	 */
	public static final float BABY_RENDER_SCALE = 0.5f;

	/**
	 * Amount mature sea urchins are scaled down while rendering.
	 */
	public static final float ADULT_RENDER_SCALE = 1f;

	public RenderSeaUrchin() {
		super(Minecraft.getMinecraft().getRenderManager(), new ModelSeaUrchin(), 0.5f);
	}

	@Override
	protected void preRenderCallback(EntitySeaUrchin urchin, float par2) {
		this.shadowSize = 0.15f;
		this.shadowOpaque = 0.5f;
		float growthProgress = urchin.getGrowthProgress();
		float scale = BABY_RENDER_SCALE + growthProgress*(ADULT_RENDER_SCALE-BABY_RENDER_SCALE);
		float preScale = 0.5f;
		
		GlStateManager.scale(preScale*scale, preScale*scale, preScale*scale);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntitySeaUrchin entity) {
		return TropicraftRenderUtils.bindTextureEntity("seaurchin");
	}
}
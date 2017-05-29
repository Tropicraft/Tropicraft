package net.tropicraft.core.client.entity.render;

import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelEagleRay;
import net.tropicraft.core.common.entity.underdasea.EntityEagleRay;

public class RenderEagleRay extends RenderLiving<EntityEagleRay> {

	public RenderEagleRay() {
		super(Minecraft.getMinecraft().getRenderManager(), new ModelEagleRay(), 1f);
	}

	@Override
	protected void preRenderCallback(EntityEagleRay par1EntityLiving, float par2) {
		glTranslatef(0f, 1.25f, 0f);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityEagleRay entity) {
		return TropicraftRenderUtils.bindTextureEntity("ray/eagleray");
	}

}
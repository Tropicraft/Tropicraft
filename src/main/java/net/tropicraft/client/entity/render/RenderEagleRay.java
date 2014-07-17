package net.tropicraft.client.entity.render;

import static org.lwjgl.opengl.GL11.glTranslatef;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.util.TropicraftUtils;

public class RenderEagleRay extends RenderLiving {
	public RenderEagleRay() {
		super(new net.tropicraft.client.entity.model.ModelEagleRay(), 1f);
	}

	@Override
	protected void preRenderCallback(EntityLivingBase par1EntityLiving, float par2) {
		glTranslatef(0f, 1.25f,0f);
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return TropicraftUtils.bindTextureEntity("ray/eagleray");
	}
}
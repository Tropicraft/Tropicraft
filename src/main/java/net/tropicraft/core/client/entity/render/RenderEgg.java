package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.ModelEgg;
import net.tropicraft.core.common.entity.egg.EntityEgg;

public class RenderEgg extends RenderLiving<EntityEgg> {

	public RenderEgg() {
		super(Minecraft.getMinecraft().getRenderManager(), new ModelEgg(), 1f);
		this.shadowOpaque = 0.5f;
	}

	@Override
	public void doRender(EntityEgg egg, double x, double y, double z, float yaw, float ticks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate(x, y, z);
	
		if(egg.shouldEggRenderFlat()) {
			this.shadowSize = 0.0f;
			drawFlatEgg(egg, 0, 0.05f, 0, yaw, ticks);
		}else {
			this.shadowSize = 0.2f;
			GlStateManager.scale(0.5f, 0.65f, 0.5f);
			super.doRender(egg, 0, 0, 0, yaw, ticks);
		}
		GlStateManager.popMatrix();
	}
	
	public void drawFlatEgg(EntityEgg ent, double x, double y, double z, float yaw, float ticks) {
		GlStateManager.pushMatrix();
		GlStateManager.enableRescaleNormal();
		this.bindEntityTexture(ent);
		Tessellator tessellator = Tessellator.getInstance();
		GlStateManager.translate(x, y, z);

		GlStateManager.rotate(180f - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

		GlStateManager.scale(0.25f, 0.25f, 0.25f);

		VertexBuffer buffer = tessellator.getBuffer();
		buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
		GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
		buffer.pos(-.5, -.25, 0).tex(0, 1).endVertex();
		buffer.pos( .5, -.25, 0).tex(1, 1).endVertex();
		buffer.pos( .5,  .75, 0).tex(1, 0).endVertex();
		buffer.pos(-.5,  .75, 0).tex(0, 0).endVertex();
		tessellator.draw();

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();
	}
	
	@Override
	protected ResourceLocation getEntityTexture(EntityEgg entity) {
		return TropicraftRenderUtils.bindTextureEntity(entity.getEggTexture());
	}
}
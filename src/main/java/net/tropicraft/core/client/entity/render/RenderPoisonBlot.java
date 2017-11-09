package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.projectile.EntityPoisonBlot;

public class RenderPoisonBlot extends Render<EntityPoisonBlot>{

	public RenderPoisonBlot() {
		super(Minecraft.getMinecraft().getRenderManager());
	}


	@Override
	public void doRender(EntityPoisonBlot entity, double x, double y, double z, float entityYaw, float partialTicks) {
			GlStateManager.pushMatrix();
			GlStateManager.enableRescaleNormal();
			this.bindEntityTexture(entity);
			Tessellator tessellator = Tessellator.getInstance();
			GlStateManager.translate(x, y, z);

			GlStateManager.rotate(180f - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);

			//GlStateManager.scale(0.25f, 0.25f, 0.25f);

			VertexBuffer buffer = tessellator.getBuffer();
			buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
			GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
			buffer.pos(-.5, -.5, 0).tex(0, 1).endVertex();
			buffer.pos( .5, -.5, 0).tex(1, 1).endVertex();
			buffer.pos( .5,  .5, 0).tex(1, 0).endVertex();
			buffer.pos(-.5,  .5, 0).tex(0, 0).endVertex();
			tessellator.draw();

			GlStateManager.disableRescaleNormal();
			GlStateManager.popMatrix();
	}


	@Override
	protected ResourceLocation getEntityTexture(EntityPoisonBlot entity) {
		return TropicraftRenderUtils.getTextureEntity("treefrog/blot");
	}

}

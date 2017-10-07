package net.tropicraft.core.client.tileentity;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.BlockSeaweed.TileSeaweed;

public class TileEntitySeaweedRenderer extends TileEntitySpecialRenderer<TileSeaweed> {

	private static final ResourceLocation TEXTURE_LOC = new ResourceLocation(Info.MODID, "textures/blocks/seaweed.png");

	private static final double SWAY_AMOUNT = 0.2;
	private static final double SWAY_ANGLE = Math.PI / 2;

	/** Number of ticks to complete a single sway */
	private static final double SWAY_PERIOD = 30;

	/** Amount of ticks each successive segment "lags" behind" */
	private static final double SWAY_LAG = 7;

	@Override
	public void renderTileEntityAt(TileSeaweed te, double x, double y, double z, float partialTicks, int destroyStage) {
		Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE_LOC);

		VertexBuffer buf = Tessellator.getInstance().getBuffer();

		GlStateManager.pushMatrix();
		{
			GlStateManager.translate(x, y, z);
			GlStateManager.disableLighting();
			GlStateManager.disableCull();
			GlStateManager.color(0.8f, 0.8f, 0.8f);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

			buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);

			Vec3d bot = new Vec3d(0.5, 1, 0.5);
			Vec3d prevSway = new Vec3d(0, 1, 0);

			for (int i = 0; i < te.getHeight(); i++) {
				Vec3d sway = computeSwayVector(SWAY_ANGLE, i, te.getWorld().getTotalWorldTime() + partialTicks);
				Vec3d top = bot.add(sway);
				
				int texture = i == te.getHeight() - 1 ? 7 : (te.getHeight() + i) % 7;
				float v = (7 - texture) / 8f;

				Vec3d angle = new Vec3d(1, 0, 1);
				renderCorner(buf, bot, prevSway, angle);
				buf.tex(0, v + 1/8f).endVertex();
				renderCorner(buf, top, sway, angle);
				buf.tex(0, v).endVertex();
				
				angle = new Vec3d(-1, 0, -1);
				renderCorner(buf, top, sway, angle);
				buf.tex(1, v).endVertex();
				renderCorner(buf, bot, prevSway, angle);
				buf.tex(1, v + 1/8f).endVertex();

				angle = new Vec3d(-1, 0, 1);
				renderCorner(buf, bot, prevSway, angle);
				buf.tex(0, v + 1/8f).endVertex();
				renderCorner(buf, top, sway, angle);
				buf.tex(0, v).endVertex();

				angle = new Vec3d(1, 0, -1);
				renderCorner(buf, top, sway, angle);
				buf.tex(1, v).endVertex();
				renderCorner(buf, bot, prevSway, angle);
				buf.tex(1, v + 1/8f).endVertex();

				bot = top;
			}

			Tessellator.getInstance().draw();

			buf.setTranslation(0, 0, 0);
		}
		GlStateManager.popMatrix();
	}

	private Vec3d computeSwayVector(double angle, int height, float time) {
		double sway = Math.sin((time + ((height + 1) * SWAY_LAG)) / SWAY_PERIOD) * SWAY_AMOUNT;
		return new Vec3d(0, 1, 0).addVector(Math.sin(angle) * sway, 0, Math.cos(angle) * sway).normalize();
	}

	private Vec3d computeCorner(Vec3d center, Vec3d sway, Vec3d angle) {
		return center.add(sway.crossProduct(angle).normalize().scale(0.5));
	}

	private void renderCorner(VertexBuffer buf, Vec3d center, Vec3d sway, Vec3d angle) {
		Vec3d pos = computeCorner(center, sway, angle);
		buf.pos(pos.xCoord, pos.yCoord, pos.zCoord);
	}

	@Override
	public boolean isGlobalRenderer(TileSeaweed te) {
		return true;
	}
}

package net.tropicraft.core.client.tileentity;

import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.animation.FastTESR;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.tropicraft.Info;
import net.tropicraft.core.common.block.BlockSeaweed.TileSeaweed;

@EventBusSubscriber
public class TileEntitySeaweedRenderer extends FastTESR<TileSeaweed> {

	private static final TextureAtlasSprite[] SPRITES = new TextureAtlasSprite[8];

	private static final double SWAY_AMOUNT = 0.2;

	/** Number of ticks to complete a single sway */
	private static final double SWAY_PERIOD = 30;

	/** Amount of ticks each successive segment "lags" behind" */
	private static final double SWAY_LAG = 7;

	@Override
	public void renderTileEntityFast(TileSeaweed te, double x, double y, double z, float partialTicks, int destroyStage, VertexBuffer buf) {

		buf.setTranslation(x, y, z);

		// GlStateManager.color(0.8f, 0.8f, 0.8f);
		// OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);

		Vec3d bot = new Vec3d(0.5, 1, 0.5).add(te.getOffset());
		Vec3d prevSway = new Vec3d(0, 1, 0);

		for (int i = 0; i < te.getHeight(); i++) {
			Vec3d sway = computeSwayVector(te.getSwayAngle(), i, te.getWorld().getTotalWorldTime() + partialTicks + te.getSwayDelay());
			Vec3d top = bot.add(sway);

			int texture = i == te.getHeight() - 1 ? SPRITES.length - 1: (te.getHeight() + i) % (SPRITES.length - 1);
			TextureAtlasSprite sprite = SPRITES[texture];

			renderQuad(buf, sprite, bot, top, sway, prevSway, new Vec3d(1, 0, 1));
			renderQuad(buf, sprite, bot, top, sway, prevSway, new Vec3d(-1, 0, 1));

			bot = top;
		}

		buf.setTranslation(0, 0, 0);
	}

	private Vec3d computeSwayVector(double angle, int height, double time) {
		double sway = Math.sin((time + ((height + 1) * SWAY_LAG)) / SWAY_PERIOD) * SWAY_AMOUNT;
		return new Vec3d(0, 1, 0).addVector(Math.sin(angle) * sway, 0, Math.cos(angle) * sway).normalize();
	}

	private Vec3d computeCorner(Vec3d center, Vec3d sway, Vec3d angle) {
		return center.add(sway.crossProduct(angle).normalize().scale(0.5));
	}

	private void renderCorner(VertexBuffer buf, Vec3d center, Vec3d sway, Vec3d angle) {
		Vec3d pos = computeCorner(center, sway, angle);
		buf.pos(pos.xCoord, pos.yCoord, pos.zCoord).color(204, 204, 204, 255);
	}
	
	private void finishVert(VertexBuffer buf, float u, float v) {
		buf.tex(u, v).lightmap(240, 240).endVertex();
	}
	
	private void renderQuad(VertexBuffer buf, TextureAtlasSprite sprite, Vec3d bot, Vec3d top, Vec3d sway, Vec3d prevSway, Vec3d angle) {
		renderCorner(buf, bot, prevSway, angle);
		finishVert(buf, sprite.getMinU(), sprite.getMaxV());
		renderCorner(buf, top, sway, angle);
		finishVert(buf, sprite.getMinU(), sprite.getMinV());

		angle = angle.scale(-1);
		renderCorner(buf, top, sway, angle);
		finishVert(buf, sprite.getMaxU(), sprite.getMinV());
		renderCorner(buf, bot, prevSway, angle);
		finishVert(buf, sprite.getMaxU(), sprite.getMaxV());
	}

	@Override
	public boolean isGlobalRenderer(TileSeaweed te) {
		return true;
	}
	
	@SubscribeEvent
	public static void onTextureStitch(TextureStitchEvent.Pre event) {
		for (int i = 0; i < SPRITES.length; i++) {
			SPRITES[i] = event.getMap().registerSprite(new ResourceLocation(Info.MODID, "blocks/seaweed_section_" + i));
		}
	}
}

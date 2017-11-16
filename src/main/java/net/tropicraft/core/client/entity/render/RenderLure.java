package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityHook;

@SideOnly(Side.CLIENT)
public class RenderLure extends Render<EntityHook> {

	public RenderLure(RenderManager renderManagerIn) {
		super(renderManagerIn);
	}

	public void doRender(EntityHook entity, double x, double y, double z, float entityYaw, float partialTicks) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) x, (float) y, (float) z);
		GlStateManager.enableRescaleNormal();
		GlStateManager.scale(0.5F, 0.5F, 0.5F);
		this.bindEntityTexture(entity);
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder vertexbuffer = tessellator.getBuffer();

		GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
		GlStateManager.rotate(
				(float) (this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX,
				1.0F, 0.0F, 0.0F);

		if (this.renderOutlines) {
			GlStateManager.enableColorMaterial();
			GlStateManager.enableOutlineMode(this.getTeamColor(entity));
		}

		double startX = 1;
		double startY = 0;
		double sizeX = 1D / 16D;
		double sizeY = 1D / 16D;
		if (entity.getAngler() != null) {
			// I have a purple lure, fight me IRL to remove
			if (entity.getAngler().getName().equals("Mr_okushama")) {
				startX = 2;
			}
		}

		if (startX > 0) {
			startX /= 16;
		}
		if (startY > 0) {
			startY /= 16;
		}

		vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
		vertexbuffer.pos(-0.5D, -0.5D, 0.0D).tex(startX + sizeX, startY + sizeY).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(0.5D, -0.5D, 0.0D).tex(startX, startY + sizeY).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(0.5D, 0.5D, 0.0D).tex(startX, startY).normal(0.0F, 1.0F, 0.0F).endVertex();
		vertexbuffer.pos(-0.5D, 0.5D, 0.0D).tex(startX + sizeX, startY).normal(0.0F, 1.0F, 0.0F).endVertex();
		tessellator.draw();

		if (this.renderOutlines) {
			GlStateManager.disableOutlineMode();
			GlStateManager.disableColorMaterial();
		}

		GlStateManager.disableRescaleNormal();
		GlStateManager.popMatrix();

		if (entity.angler != null && !this.renderOutlines) {
			int k = entity.angler.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
			float f7 = entity.angler.getSwingProgress(partialTicks);
			float f8 = MathHelper.sin(MathHelper.sqrt(f7) * (float) Math.PI);
			float f9 = (entity.angler.prevRenderYawOffset
					+ (entity.angler.renderYawOffset - entity.angler.prevRenderYawOffset) * partialTicks)
					* 0.017453292F;
			double d0 = (double) MathHelper.sin(f9);
			double d1 = (double) MathHelper.cos(f9);
			double d2 = (double) k * 0.45D;
			double d4;
			double d5;
			double d6;
			double d7;

			// first person
			if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0)
					&& entity.angler == Minecraft.getMinecraft().player) {
				Vec3d vec3d = new Vec3d((double) k * -0.3D, -0.05D, 0.4D);
				vec3d = vec3d.rotatePitch(-(entity.angler.prevRotationPitch
						+ (entity.angler.rotationPitch - entity.angler.prevRotationPitch) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(-(entity.angler.prevRotationYaw
						+ (entity.angler.rotationYaw - entity.angler.prevRotationYaw) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(f8 * 0.5F);
				vec3d = vec3d.rotatePitch(-f8 * 0.7F);
				d4 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * (double) partialTicks + vec3d.x;
				d5 = entity.angler.prevPosY + (entity.angler.posY - entity.angler.prevPosY) * (double) partialTicks + vec3d.y;
				d6 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * (double) partialTicks + vec3d.z;
				d7 = (double) entity.angler.getEyeHeight();
			} else {
				d4 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * (double) partialTicks - d1 * d2 - d0 * 0.8D;
				d5 = entity.angler.prevPosY + (double) entity.angler.getEyeHeight()
						+ (entity.angler.posY - entity.angler.prevPosY) * (double) partialTicks - 0.45D;
				d6 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * (double) partialTicks - d0 * d2 + d1 * 0.8D;
				d7 = entity.angler.isSneaking() ? -0.1875D : 0.0D;
			}

			double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
			double d8 = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + 0.25D;
			double d9 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
			double d10 = (double) ((float) (d4 - d13));
			double d11 = (double) ((float) (d5 - d8)) + d7;
			double d12 = (double) ((float) (d6 - d9));
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);
			int l = 32;

			for (int i1 = 0; i1 <= l; ++i1) {
				float f10 = (float) i1 / l;
				vertexbuffer.pos(x + d10 * (double) f10, y + d11 * (double) (f10 * f10 + f10) * 0.5D + 0.25D,
						z + d12 * (double) f10).color(255, 255, 255, 255).endVertex();
			}

			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}

	protected ResourceLocation getEntityTexture(EntityHook entity) {
		return TropicraftRenderUtils.bindTextureEntity("lures");
	}
}
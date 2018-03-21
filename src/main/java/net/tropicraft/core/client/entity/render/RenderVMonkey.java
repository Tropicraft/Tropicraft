package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.Info;
import net.tropicraft.core.client.entity.model.ModelVMonkey;
import net.tropicraft.core.client.entity.render.layers.LayerHeldItemVMonkey;
import net.tropicraft.core.common.entity.passive.EntityVMonkey;
import net.tropicraft.core.common.entity.underdasea.atlantoku.EntityTropicraftWaterBase;

import javax.annotation.Resource;

public class RenderVMonkey extends RenderLiving<EntityVMonkey> {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Info.MODID + ":textures/entity/monkeytext.png");
	private static final ResourceLocation ANGRY_TEXTURE = new ResourceLocation(Info.MODID + ":textures/entity/monkey_angrytext.png");

    public RenderVMonkey(ModelVMonkey modelMonkey) {
        super(Minecraft.getMinecraft().getRenderManager(), modelMonkey, 0.5F);
        this.shadowSize = 0.3f;
        this.shadowOpaque = 0.5f;
        this.addLayer(new LayerHeldItemVMonkey(this, modelMonkey));
    }

//	@Override
//	public void doRender(EntityVMonkey entity, double x, double y, double z, float entityYaw,
//						 float partialTicks) {
//		GlStateManager.pushMatrix();
//		GlStateManager.disableCull();
//		GlStateManager.translate(4, 0f, 4f);
//		String n = entity.getCustomNameTag();
//		entity.setCustomNameTag("");
//		entity.outOfWaterTime = 0;
//		this.renderWaterMob(entity, x - 4, y - 1, z - 4, partialTicks);
//		GlStateManager.popMatrix();
//		entity.setCustomNameTag(n);
//		if (Minecraft.getMinecraft().pointedEntity != null && entity.getCustomNameTag().length() > 0) {
//			if (Minecraft.getMinecraft().pointedEntity.equals(entity)) {
//				this.renderEntityName(entity, x, y, z, entity.getCustomNameTag(), 7d);
//			}
//		}
//
//	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
    @Override
	protected ResourceLocation getEntityTexture(EntityVMonkey entity) {
        return entity.isAngry() ? ANGRY_TEXTURE : TEXTURE;
	}
}

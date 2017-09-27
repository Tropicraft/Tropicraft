package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.placeable.EntityWallItem;


public class RenderWallItem extends Render<EntityWallItem> {

	public RenderWallItem() {
		super(Minecraft.getMinecraft().getRenderManager());
	}
	
	@Override
	public void doRender(EntityWallItem entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.posX + x;
        double d1 = (double)blockpos.getY() - entity.posY + y;
        double d2 = (double)blockpos.getZ() - entity.posZ + z;
        GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.translate(0.0F, 0.0F, 0.475F);
        TropicraftRenderUtils.renderItem(entity.getDisplayedItem(), 1);
        GlStateManager.popMatrix();
        this.renderName(entity, x + (double)((float)entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (double)((float)entity.facingDirection.getFrontOffsetZ() * 0.3F));
    }
	
	@Override
	protected ResourceLocation getEntityTexture(EntityWallItem entity) {
		return TextureMap.LOCATION_BLOCKS_TEXTURE;
	}

}

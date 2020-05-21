package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

import javax.annotation.Nullable;


public class WallItemRenderer extends EntityRenderer<WallItemEntity> {

	public WallItemRenderer(final EntityRendererManager entityRendererManager) {
		super(entityRendererManager);
	}
	
	@Override
	public void doRender(WallItemEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.posX + x;
        double d1 = (double)blockpos.getY() - entity.posY + y;
        double d2 = (double)blockpos.getZ() - entity.posZ + z;
        GlStateManager.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotatef(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        GlStateManager.translatef(0.0F, 0.0F, 0.475F);
        TropicraftRenderUtils.renderItem(entity.getDisplayedItem(), 1);
        GlStateManager.popMatrix();
        renderName(entity, x + (double)((float)entity.getHorizontalFacing().getXOffset() * 0.3F), y - 0.25D, z + (double)((float)entity.getHorizontalFacing().getZOffset() * 0.3F));
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(WallItemEntity wallItemEntity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}

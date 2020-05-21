package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

import javax.annotation.Nullable;

import static net.tropicraft.core.client.TropicraftRenderUtils.bindTexture;


public class WallItemRenderer extends EntityRenderer<WallItemEntity> {

	public WallItemRenderer(final EntityRendererManager entityRendererManager) {
		super(entityRendererManager);
	}
	
	@Override
    public void render(final WallItemEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        stack.push();
        BlockPos blockpos = entity.getHangingPosition();
//        double d0 = (double)blockpos.getX() - entity.getPosX() + x;
//        double d1 = (double)blockpos.getY() - entity.getPosY() + y;
//        double d2 = (double)blockpos.getZ() - entity.posZ + z;
//        stack.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        stack.rotate(Vector3f.YP.rotationDegrees(180.0F - entity.rotationYaw));
        bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        stack.translate(0.0F, 0.0F, 0.475F);
        TropicraftRenderUtils.renderItem(entity.getDisplayedItem(), 1, false, stack, bufferIn, packedLightIn, 0, null);
        stack.pop();
        renderName(entity, entity.getDisplayName().getString(), stack, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(WallItemEntity wallItemEntity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}

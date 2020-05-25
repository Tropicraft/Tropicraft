package net.tropicraft.core.client.entity.render;

import javax.annotation.Nullable;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;


public class WallItemRenderer extends EntityRenderer<WallItemEntity> {

	public WallItemRenderer(final EntityRendererManager entityRendererManager) {
		super(entityRendererManager);
	}
	
	@Override
    public void render(final WallItemEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        stack.push();
        stack.rotate(Vector3f.XP.rotationDegrees(entity.rotationPitch));
        stack.rotate(Vector3f.YP.rotationDegrees(180.0F - entity.rotationYaw));
        stack.rotate(Vector3f.ZP.rotationDegrees(entity.getRotation() * 360 / 8F));        
        TropicraftRenderUtils.renderItem(entity.getDisplayedItem(), 1, false, stack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, null);
        stack.pop();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getEntityTexture(WallItemEntity wallItemEntity) {
        return AtlasTexture.LOCATION_BLOCKS_TEXTURE;
    }
}

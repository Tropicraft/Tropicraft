package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

import javax.annotation.Nullable;


public class WallItemRenderer extends EntityRenderer<WallItemEntity> {

    public WallItemRenderer(final EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
    }
    
    @Override
    public void render(final WallItemEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.mulPose(Vector3f.XP.rotationDegrees(entity.xRot));
        stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - entity.yRot));
        stack.mulPose(Vector3f.ZP.rotationDegrees(entity.getRotation() * 360 / 8F));        
        TropicraftRenderUtils.renderItem(entity.getItem(), 1, false, stack, bufferIn, packedLightIn, OverlayTexture.NO_OVERLAY, null);
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(WallItemEntity wallItemEntity) {
        return AtlasTexture.LOCATION_BLOCKS;
    }
}

package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PlayerHeadItem;
import net.minecraft.world.level.Level;
import net.tropicraft.core.common.entity.placeable.WallItemEntity;

public class WallItemRenderer extends EntityRenderer<WallItemEntity> {

    public WallItemRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(WallItemEntity entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(entity.getXRot()));
        stack.mulPose(Axis.YP.rotationDegrees(180.0f - entity.getYRot()));
        stack.mulPose(Axis.ZP.rotationDegrees(entity.getRotation() * 360 / 8.0f));
        ItemStack itemStack = entity.getItem();
        int seed = entity.getId();
        Level level = entity.level();
        if (!itemStack.isEmpty()) {
            stack.pushPose();
            stack.scale((float) 1, (float) 1, (float) 1);

            // TODO what is this now?
            if (/*!Minecraft.getInstance().getItemRenderer().shouldRenderItemIn3D(stack) || */itemStack.getItem() instanceof PlayerHeadItem) {
                stack.mulPose(Axis.YP.rotationDegrees(180.0f));
            }
            Minecraft.getInstance().getItemRenderer().renderStatic(itemStack, ItemDisplayContext.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, stack, bufferIn, level, seed);
            stack.popPose();
        }
        stack.popPose();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
    }

    @Override
    public ResourceLocation getTextureLocation(WallItemEntity wallItemEntity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}

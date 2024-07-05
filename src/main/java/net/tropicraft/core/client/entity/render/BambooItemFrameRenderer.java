package net.tropicraft.core.client.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.core.common.entity.BambooItemFrame;
import net.tropicraft.core.common.item.TropicraftItems;

public class BambooItemFrameRenderer extends EntityRenderer<BambooItemFrame> {
    public static final ModelResourceLocation LOCATION_MODEL = new ModelResourceLocation(TropicraftItems.BAMBOO_ITEM_FRAME.getId(), "map=false");
    private static final ModelResourceLocation LOCATION_MODEL_MAP = new ModelResourceLocation(TropicraftItems.BAMBOO_ITEM_FRAME.getId(), "map=true");
    private final Minecraft mc = Minecraft.getInstance();
    private final ItemRenderer itemRenderer;

    public BambooItemFrameRenderer(EntityRendererProvider.Context context) {
        super(context);
        itemRenderer = mc.getItemRenderer();
    }

    @Override
    public void render(BambooItemFrame entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        matrixStackIn.pushPose();
        Direction direction = entityIn.getDirection();
        Vec3 Vector3d = getRenderOffset(entityIn, partialTicks);
        matrixStackIn.translate(-Vector3d.x, -Vector3d.y, -Vector3d.z);
        double d0 = 0.46875;
        matrixStackIn.translate((double) direction.getStepX() * 0.46875, (double) direction.getStepY() * 0.46875, (double) direction.getStepZ() * 0.46875);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(entityIn.getXRot()));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0f - entityIn.getYRot()));
        BlockRenderDispatcher blockrendererdispatcher = mc.getBlockRenderer();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShaper().getModelManager();
        ModelResourceLocation modelresourcelocation = entityIn.getItem().getItem() instanceof MapItem ? LOCATION_MODEL_MAP : LOCATION_MODEL;
        matrixStackIn.pushPose();
        matrixStackIn.translate(-0.5, -0.5, -0.5);
        blockrendererdispatcher.getModelRenderer().renderModel(matrixStackIn.last(), bufferIn.getBuffer(Sheets.solidBlockSheet()), null, modelmanager.getModel(modelresourcelocation), 1.0f, 1.0f, 1.0f, packedLightIn, OverlayTexture.NO_OVERLAY);
        matrixStackIn.popPose();
        ItemStack itemstack = entityIn.getItem();
        if (!itemstack.isEmpty()) {
            MapItemSavedData mapdata = MapItem.getSavedData(itemstack, entityIn.level());
            matrixStackIn.translate(0.0, 0.0, 0.4375);
            int i = mapdata != null ? entityIn.getRotation() % 4 * 2 : entityIn.getRotation();
            matrixStackIn.mulPose(Axis.ZP.rotationDegrees((float) i * 360.0f / 8.0f));
            if (mapdata != null) {
                matrixStackIn.mulPose(Axis.ZP.rotationDegrees(180.0f));
                float f = 0.0078125f;
                matrixStackIn.scale(0.0078125f, 0.0078125f, 0.0078125f);
                matrixStackIn.translate(-64.0, -64.0, 0.0);
                matrixStackIn.translate(0.0, 0.0, -1.0);
                MapId id = itemstack.get(DataComponents.MAP_ID);
                if (mapdata != null && id != null) {
                    mc.gameRenderer.getMapRenderer().render(matrixStackIn, bufferIn, id, mapdata, true, packedLightIn);
                }
            } else {
                matrixStackIn.scale(0.5f, 0.5f, 0.5f);
                itemRenderer.renderStatic(itemstack, ItemDisplayContext.FIXED, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entityIn.level(), entityIn.getId());
            }
        }

        matrixStackIn.popPose();
    }

    @Override
    public Vec3 getRenderOffset(BambooItemFrame entityIn, float partialTicks) {
        return new Vec3((float) entityIn.getDirection().getStepX() * 0.3f, -0.25, (float) entityIn.getDirection().getStepZ() * 0.3f);
    }

    @Override
    public ResourceLocation getTextureLocation(BambooItemFrame entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    protected boolean shouldShowName(BambooItemFrame entity) {
        if (Minecraft.renderNames() && !entity.getItem().isEmpty() && entity.getItem().has(DataComponents.CUSTOM_NAME) && entityRenderDispatcher.crosshairPickEntity == entity) {
            double dist = entityRenderDispatcher.distanceToSqr(entity);
            float f = entity.isDiscrete() ? 32.0f : 64.0f;
            return dist < (double) (f * f);
        } else {
            return false;
        }
    }

    @Override
    protected void renderNameTag(BambooItemFrame entityIn, Component displayNameIn, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, float partialTicks) {
        super.renderNameTag(entityIn, entityIn.getItem().getHoverName(), matrixStackIn, bufferIn, packedLightIn, partialTicks);
    }
}

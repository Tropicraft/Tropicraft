package net.tropicraft.core.client.entity.render;

import javax.annotation.Nullable;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ModelManager;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.tropicraft.core.common.entity.BambooItemFrame;

public class BambooItemFrameRenderer extends EntityRenderer<BambooItemFrame> {
    private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
    public static final ResourceLocation LOCATION_BLOCK = new ResourceLocation("tropicraft", "bamboo_item_frame");
    private static final ModelResourceLocation LOCATION_MODEL = new ModelResourceLocation(LOCATION_BLOCK, "map=false");
    private static final ModelResourceLocation LOCATION_MODEL_MAP = new ModelResourceLocation(LOCATION_BLOCK, "map=true");
    private final Minecraft mc = Minecraft.getInstance();
    private final ItemRenderer itemRenderer;

    public BambooItemFrameRenderer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
        itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    public void doRender(BambooItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks) {
        GlStateManager.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.posX + x;
        double d1 = (double)blockpos.getY() - entity.posY + y;
        double d2 = (double)blockpos.getZ() - entity.posZ + z;
        GlStateManager.translated(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotatef(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        renderManager.textureManager.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher blockrendererdispatcher = mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        ModelResourceLocation modelresourcelocation = entity.getDisplayedItem().getItem() instanceof FilledMapItem ? LOCATION_MODEL_MAP : LOCATION_MODEL;
        GlStateManager.pushMatrix();
        GlStateManager.translatef(-0.5F, -0.5F, -0.5F);
        if (renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.setupSolidRenderingTextureCombine(this.getTeamColor(entity));
        }

        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(modelmanager.getModel(modelresourcelocation), 1.0F, 1.0F, 1.0F, 1.0F);
        if (renderOutlines) {
            GlStateManager.tearDownSolidRenderingTextureCombine();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        if (entity.getDisplayedItem().getItem() == Items.FILLED_MAP) {
            GlStateManager.pushLightingAttributes();
            RenderHelper.enableStandardItemLighting();
        }

        GlStateManager.translatef(0.0F, 0.0F, 0.4375F);
        renderItem(entity);
        if (entity.getDisplayedItem().getItem() == Items.FILLED_MAP) {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popAttributes();
        }

        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
        renderName(entity, x + (double)((float)entity.getHorizontalFacing().getXOffset() * 0.3F), y - 0.25D, z + (double)((float)entity.getHorizontalFacing().getZOffset() * 0.3F));
    }

    @Nullable
    protected ResourceLocation getEntityTexture(BambooItemFrame entity) {
        return null;
    }

    private void renderItem(BambooItemFrame itemFrame) {
        ItemStack itemstack = itemFrame.getDisplayedItem();
        if (!itemstack.isEmpty()) {
            GlStateManager.pushMatrix();
            MapData mapdata = FilledMapItem.getMapData(itemstack, itemFrame.world);
            int i = mapdata != null ? itemFrame.getRotation() % 4 * 2 : itemFrame.getRotation();
            GlStateManager.rotatef((float)i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);
            if (mapdata != null) {
                GlStateManager.disableLighting();
                renderManager.textureManager.bindTexture(MAP_BACKGROUND_TEXTURES);
                GlStateManager.rotatef(180.0F, 0.0F, 0.0F, 1.0F);
                float f = 0.0078125F;
                GlStateManager.scalef(0.0078125F, 0.0078125F, 0.0078125F);
                GlStateManager.translatef(-64.0F, -64.0F, 0.0F);
                GlStateManager.translatef(0.0F, 0.0F, -1.0F);
                if (mapdata != null) {
                    mc.gameRenderer.getMapItemRenderer().renderMap(mapdata, true);
                }
            } else {
                GlStateManager.scalef(0.5F, 0.5F, 0.5F);
                itemRenderer.renderItem(itemstack, ItemCameraTransforms.TransformType.FIXED);
            }
            GlStateManager.popMatrix();
        }
    }

    protected void renderName(BambooItemFrame entity, double x, double y, double z) {
        if (Minecraft.isGuiEnabled() && !entity.getDisplayedItem().isEmpty() && entity.getDisplayedItem().hasDisplayName() && renderManager.pointedEntity == entity) {
            double d0 = entity.getDistanceSq(renderManager.info.getProjectedView());
            float f = entity.shouldRenderSneaking() ? 32.0F : 64.0F;
            if (!(d0 >= (double)(f * f))) {
                String s = entity.getDisplayedItem().getDisplayName().getFormattedText();
                renderLivingLabel(entity, s, x, y, z, 64);
            }
        }
    }
}

package net.tropicraft.core.client.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSkull;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.storage.MapData;
import net.tropicraft.core.common.entity.placeable.EntityBambooItemFrame;

public class RenderBambooItemFrame extends Render<EntityBambooItemFrame> {

    private static final ResourceLocation MAP_BACKGROUND_TEXTURES = new ResourceLocation("textures/map/map_background.png");
    private final Minecraft mc = Minecraft.getMinecraft();
    private final ModelResourceLocation itemFrameModel = new ModelResourceLocation("tropicraft:bamboo_item_frame", "normal");
    private final ModelResourceLocation mapModel = new ModelResourceLocation("tropicraft:bamboo_item_frame", "map");
    private RenderItem itemRenderer;
	
	public RenderBambooItemFrame() {
		super(Minecraft.getMinecraft().getRenderManager());
		itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityBambooItemFrame entity) {
		return null;
	}

    /**
     * Renders the desired {@code T} type Entity.
     */
	@Override
    public void doRender(EntityBambooItemFrame entity, double x, double y, double z, float entityYaw, float partialTicks) {
		System.out.println(itemFrameModel);
        GlStateManager.pushMatrix();
        BlockPos blockpos = entity.getHangingPosition();
        double d0 = (double)blockpos.getX() - entity.posX + x;
        double d1 = (double)blockpos.getY() - entity.posY + y;
        double d2 = (double)blockpos.getZ() - entity.posZ + z;
        GlStateManager.translate(d0 + 0.5D, d1 + 0.5D, d2 + 0.5D);
        GlStateManager.rotate(180.0F - entity.rotationYaw, 0.0F, 1.0F, 0.0F);
        this.renderManager.renderEngine.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        BlockRendererDispatcher blockrendererdispatcher = this.mc.getBlockRendererDispatcher();
        ModelManager modelmanager = blockrendererdispatcher.getBlockModelShapes().getModelManager();
        IBakedModel ibakedmodel;

        if (entity.getDisplayedItem() != null && entity.getDisplayedItem().getItem() == Items.FILLED_MAP) {
            ibakedmodel = modelmanager.getModel(this.mapModel);
        } else {
            ibakedmodel = modelmanager.getModel(this.itemFrameModel);
            //System.out.println(ibakedmodel.);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translate(-0.5F, -0.5F, -0.5F);

        if (this.renderOutlines) {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        blockrendererdispatcher.getBlockModelRenderer().renderModelBrightnessColor(ibakedmodel, 1.0F, 1.0F, 1.0F, 1.0F);

        if (this.renderOutlines) {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.popMatrix();
        GlStateManager.translate(0.0F, 0.0F, 0.4375F);
        this.renderItem(entity);
        GlStateManager.popMatrix();
        //System.out.println(entity);
       // System.out.println(entity.facingDirection);
        this.renderName(entity, x + (double)((float)entity.facingDirection.getFrontOffsetX() * 0.3F), y - 0.25D, z + (double)((float)entity.facingDirection.getFrontOffsetZ() * 0.3F));
    }
	
	private void renderItem(EntityBambooItemFrame itemFrame) {
        ItemStack itemstack = itemFrame.getDisplayedItem();

        if (itemstack != null) {
            EntityItem entityitem = new EntityItem(itemFrame.world, 0.0D, 0.0D, 0.0D, itemstack);
            Item item = entityitem.getEntityItem().getItem();
            entityitem.getEntityItem().stackSize = 1;
            entityitem.hoverStart = 0.0F;
            GlStateManager.pushMatrix();
            GlStateManager.disableLighting();
            int i = itemFrame.getRotation();

            if (item instanceof net.minecraft.item.ItemMap) {
                i = i % 4 * 2;
            }

            GlStateManager.rotate((float)i * 360.0F / 8.0F, 0.0F, 0.0F, 1.0F);

            if (item instanceof net.minecraft.item.ItemMap) {
                this.renderManager.renderEngine.bindTexture(MAP_BACKGROUND_TEXTURES);
                GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
                float f = 0.0078125F;
                GlStateManager.scale(f, f, f);
                GlStateManager.translate(-64.0F, -64.0F, 0.0F);
                MapData mapdata = Items.FILLED_MAP.getMapData(entityitem.getEntityItem(), itemFrame.world);
                GlStateManager.translate(0.0F, 0.0F, -1.0F);

                if (mapdata != null) {
                    this.mc.entityRenderer.getMapItemRenderer().renderMap(mapdata, true);
                }
            } else {
                GlStateManager.scale(0.5F, 0.5F, 0.5F);

                if (!this.itemRenderer.shouldRenderItemIn3D(entityitem.getEntityItem()) || item instanceof ItemSkull) {
                    GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
                }

                GlStateManager.pushAttrib();
                RenderHelper.enableStandardItemLighting();
                this.itemRenderer.renderItem(entityitem.getEntityItem(), ItemCameraTransforms.TransformType.FIXED);
                RenderHelper.disableStandardItemLighting();
                GlStateManager.popAttrib();
            }

            GlStateManager.enableLighting();
            GlStateManager.popMatrix();
        }
    }
}

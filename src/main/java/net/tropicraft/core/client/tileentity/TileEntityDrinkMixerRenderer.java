package net.tropicraft.core.client.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.block.model.ModelBambooMug;
import net.tropicraft.core.client.block.model.ModelDrinkMixer;
import net.tropicraft.core.common.block.BlockDrinkMixer;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.item.ItemCocktail;

public class TileEntityDrinkMixerRenderer extends TileEntitySpecialRenderer<TileEntityDrinkMixer> {

	/**
	 * EIHMixer model instance
	 */
	private ModelDrinkMixer modelMixer = new ModelDrinkMixer();
	private ModelBambooMug modelBambooMug = new ModelBambooMug();
	private RenderItem renderItem;
	private RenderManager renderManager;
	private EntityItem dummyEntityItem = new EntityItem((World)null, 0.0, 0.0, 0.0, new ItemStack(Items.SUGAR));

	public TileEntityDrinkMixerRenderer() {
		renderManager = Minecraft.getMinecraft().getRenderManager();
		renderItem = Minecraft.getMinecraft().getRenderItem();
	}

	@Override
	public void renderTileEntityAt(TileEntityDrinkMixer te, double x, double y,
			double z, float partialTicks, int destroyStage) {
		GlStateManager.pushMatrix();
		GlStateManager.translate((float)x+0.5f,(float)y+1.5f,(float)z+0.5f);
		GlStateManager.rotate(180f, 1f, 0f, 1f);

		if (te == null || te.getWorld() == null) {
			GlStateManager.rotate(180f, 0f, 1f, 0f);
		} else {
			EnumFacing facing = getWorld().getBlockState(te.getPos()).getValue(BlockDrinkMixer.FACING);
			GlStateManager.rotate(facing.getHorizontalAngle(), 0, 1, 0);
		}

		if (te != null && te.isMixing()) {
			float angle = MathHelper.sin((float)(25f * 2f * Math.PI * (te.ticks + partialTicks) / TileEntityDrinkMixer.TICKS_TO_MIX)) * 15f;
			GlStateManager.rotate(angle, 0f, 1f, 0f);
		}

		TropicraftRenderUtils.bindTextureTE("drink_mixer");
		modelMixer.renderEIHMixer();
		
		if (te != null) {
			ItemStack[] ingredients = te.getIngredients();
	
			if (!te.isDoneMixing()) {
				if (ingredients[0] != null) {
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180f, 1f, 0f, 1f);
					GlStateManager.translate(0.3f, -0.5f, 0.05f);
					//GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
					dummyEntityItem.setEntityItemStack(ingredients[0]);
					renderItem.renderItem(ingredients[0], TransformType.FIXED);
					GlStateManager.popMatrix();
				}
	
				if (ingredients[1] != null) {
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180f, 1f, 0f, 1f);
					GlStateManager.translate(-0.3f, -0.5f, 0.05f);
					//GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
					dummyEntityItem.setEntityItemStack(ingredients[1]);
					renderItem.renderItem(ingredients[1], TransformType.FIXED);
					GlStateManager.popMatrix();
				}
				
				if (ingredients[2] != null) {
					GlStateManager.pushMatrix();
					GlStateManager.rotate(180f, 1f, 0f, 1f);
					GlStateManager.translate(0.0f, 0.3f, -.1f);
					GlStateManager.scale(0.8F, 0.8F, 0.8F);
					//GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
					dummyEntityItem.setEntityItemStack(ingredients[2]);
					renderItem.renderItem(ingredients[2], TransformType.FIXED);
					GlStateManager.popMatrix();
				}
			}
	
			if (te.isMixing() || te.result != null) {
				GlStateManager.pushMatrix();
				GlStateManager.translate(-0.2f, -0.25f, 0.0f);
				if (te.isDoneMixing()) {
					modelBambooMug.renderLiquid = true;
					modelBambooMug.liquidColor = ItemCocktail.getCocktailColor(te.result);
				} else {
					modelBambooMug.renderLiquid = false;
				}
				TropicraftRenderUtils.bindTextureTE("bamboo_mug");
				modelBambooMug.renderBambooMug();
				GlStateManager.popMatrix();
			}
		}

		GlStateManager.popMatrix();
	}
}

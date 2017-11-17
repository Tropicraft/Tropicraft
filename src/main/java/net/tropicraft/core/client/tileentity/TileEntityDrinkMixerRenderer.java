package net.tropicraft.core.client.tileentity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.block.model.ModelBambooMug;
import net.tropicraft.core.client.block.model.ModelDrinkMixer;
import net.tropicraft.core.common.block.tileentity.TileEntityDrinkMixer;
import net.tropicraft.core.common.item.ItemCocktail;
import net.tropicraft.core.registry.BlockRegistry;

public class TileEntityDrinkMixerRenderer extends TileEntityMachineRenderer<TileEntityDrinkMixer> {

    private final ModelBambooMug modelBambooMug = new ModelBambooMug();
    private final RenderItem renderItem;
    private EntityItem dummyEntityItem;

    public TileEntityDrinkMixerRenderer() {
        super(BlockRegistry.drinkMixer, new ModelDrinkMixer());
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
    }

    @Override
    public void renderIngredients(TileEntityDrinkMixer te) {
    	if (dummyEntityItem == null) {
    		 dummyEntityItem = new EntityItem(Minecraft.getMinecraft().world, 0.0, 0.0, 0.0, new ItemStack(Items.SUGAR));
    	}
        NonNullList<ItemStack> ingredients = te.getIngredients();

        if (!te.isDoneMixing()) {
            if (!ingredients.get(0).isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(180f, 1f, 0f, 1f);
                GlStateManager.translate(0.3f, -0.5f, 0.05f);
                // GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setItem(ingredients.get(0));
                renderItem.renderItem(ingredients.get(0), TransformType.FIXED);
                GlStateManager.popMatrix();
            }

            if (!ingredients.get(1).isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(180f, 1f, 0f, 1f);
                GlStateManager.translate(-0.3f, -0.5f, 0.05f);
                // GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setItem(ingredients.get(1));
                renderItem.renderItem(ingredients.get(1), TransformType.FIXED);
                GlStateManager.popMatrix();
            }

            if (!ingredients.get(2).isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.rotate(180f, 1f, 0f, 1f);
                GlStateManager.translate(0.0f, 0.3f, -.1f);
                GlStateManager.scale(0.8F, 0.8F, 0.8F);
                // GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setItem(ingredients.get(2));
                renderItem.renderItem(ingredients.get(2), TransformType.FIXED);
                GlStateManager.popMatrix();
            }
        }

        if (te.isMixing() || !te.result.isEmpty()) {
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
}

package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.BambooMugModel;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;
import net.tropicraft.core.common.item.CocktailItem;

public class DrinkMixerRenderer extends MachineRenderer<DrinkMixerTileEntity> {

    private final BambooMugModel modelBambooMug = new BambooMugModel();
    private final ItemRenderer renderItem;
    private ItemEntity dummyEntityItem;

    public DrinkMixerRenderer() {
        super(TropicraftBlocks.DRINK_MIXER.get(), new EIHMachineModel<>());
        this.renderItem = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    public void renderIngredients(DrinkMixerTileEntity te) {
    	if (dummyEntityItem == null) {
    		 dummyEntityItem = new ItemEntity(Minecraft.getInstance().world, 0.0, 0.0, 0.0, new ItemStack(Items.SUGAR));
    	}
        final NonNullList<ItemStack> ingredients = te.getIngredients();

        if (!te.isDoneMixing()) {
            if (!ingredients.get(0).isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.rotatef(180f, 1f, 0f, 1f);
                GlStateManager.translatef(0.3f, -0.5f, 0.05f);
                // GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setItem(ingredients.get(0));
                renderItem.renderItem(ingredients.get(0), ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }

            if (!ingredients.get(1).isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.rotatef(180f, 1f, 0f, 1f);
                GlStateManager.translatef(-0.3f, -0.5f, 0.05f);
                // GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setItem(ingredients.get(1));
                renderItem.renderItem(ingredients.get(1), ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }

            if (!ingredients.get(2).isEmpty()) {
                GlStateManager.pushMatrix();
                GlStateManager.rotatef(180f, 1f, 0f, 1f);
                GlStateManager.translatef(0.0f, 0.3f, -.1f);
                GlStateManager.scalef(0.8F, 0.8F, 0.8F);
                // GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setItem(ingredients.get(2));
                renderItem.renderItem(ingredients.get(2), ItemCameraTransforms.TransformType.FIXED);
                GlStateManager.popMatrix();
            }
        }

        if (te.isMixing() || !te.result.isEmpty()) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(-0.2f, -0.25f, 0.0f);
            if (te.isDoneMixing()) {
                modelBambooMug.renderLiquid = true;
                modelBambooMug.liquidColor = CocktailItem.getCocktailColor(te.result);
            } else {
                modelBambooMug.renderLiquid = false;
            }
            TropicraftRenderUtils.bindTextureTE("bamboo_mug");
            modelBambooMug.renderBambooMug();
            GlStateManager.popMatrix();
        }
    }
}

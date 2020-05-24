package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;

import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.Material;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
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
    private final BambooMugModel modelBambooMug = new BambooMugModel(RenderType::getEntityCutout);
    private final ItemRenderer renderItem;
    private ItemEntity dummyEntityItem;

    public DrinkMixerRenderer(final TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, TropicraftBlocks.DRINK_MIXER.get(), new EIHMachineModel<>(RenderType::getEntitySolid));
        this.renderItem = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    protected Material getMaterial() {
        return TropicraftRenderUtils.getTEMaterial("drink_mixer");
    }

    @Override
    public void renderIngredients(final DrinkMixerTileEntity te, final MatrixStack stack, final IRenderTypeBuffer buffer, int packedLightIn, int combinedOverlayIn) {
    	if (dummyEntityItem == null) {
    		 dummyEntityItem = new ItemEntity(Minecraft.getInstance().world, 0.0, 0.0, 0.0, new ItemStack(Items.SUGAR));
    	}
        final NonNullList<ItemStack> ingredients = te.getIngredients();
        final int combinedLight = getCombinedLight(te.getWorld(), te.getPos());

    	// TODO lots of repeat code - unify!
        if (!te.isDoneMixing()) {
            final ItemStack firstIngredient = ingredients.get(0);
            if (!firstIngredient.isEmpty()) {
                stack.push();
                stack.rotate(Vector3f.XP.rotationDegrees(90));
                stack.rotate(Vector3f.YP.rotationDegrees(90));
                stack.rotate(Vector3f.ZP.rotationDegrees(90));
                stack.translate(0.3f, -0.5f, 0.05f);
                dummyEntityItem.setItem(firstIngredient);
                final IBakedModel bakedModel = TropicraftRenderUtils.getBakedModel(renderItem, firstIngredient);
                renderItem.renderItem(firstIngredient, ItemCameraTransforms.TransformType.FIXED, false, stack, buffer, combinedLight, combinedOverlayIn, bakedModel);
                stack.pop();
            }

            final ItemStack secondIngredient = ingredients.get(1);
            if (!secondIngredient.isEmpty()) {
                stack.push();
                stack.rotate(Vector3f.XP.rotationDegrees(90));
                stack.rotate(Vector3f.YP.rotationDegrees(90));
                stack.rotate(Vector3f.ZP.rotationDegrees(90));
                stack.translate(-0.3f, -0.5f, 0.05f);
                dummyEntityItem.setItem(secondIngredient);
                final IBakedModel bakedModel = TropicraftRenderUtils.getBakedModel(renderItem, secondIngredient);
                renderItem.renderItem(secondIngredient, ItemCameraTransforms.TransformType.FIXED, false, stack, buffer, combinedLight, combinedOverlayIn, bakedModel);
                stack.pop();
            }

            final ItemStack thirdIngredient = ingredients.get(2);
            if (!thirdIngredient.isEmpty()) {
                stack.push();
                stack.rotate(Vector3f.XP.rotationDegrees(180f));
                stack.rotate(Vector3f.ZP.rotationDegrees(180f));
                stack.translate(0.0f, 0.3f, -.1f);
                stack.scale(0.8F, 0.8F, 0.8F);
                // GlStateManager.rotate(0, 0.0F, 1.0F, 0.0F);
                dummyEntityItem.setItem(thirdIngredient);
                final IBakedModel bakedModel = TropicraftRenderUtils.getBakedModel(renderItem, thirdIngredient);
                renderItem.renderItem(thirdIngredient, ItemCameraTransforms.TransformType.FIXED, false, stack, buffer, combinedLight, combinedOverlayIn, bakedModel);
                stack.pop();
            }
        }

        if (te.isMixing() || !te.result.isEmpty()) {
            stack.push();
            stack.translate(-0.2f, -0.25f, 0.0f);
            if (te.isDoneMixing()) {
                modelBambooMug.renderLiquid = true;
                modelBambooMug.liquidColor = CocktailItem.getCocktailColor(te.result);
            } else {
                modelBambooMug.renderLiquid = false;
            }
            IVertexBuilder ivertexbuilder = buffer.getBuffer(modelBambooMug.getRenderType(TropicraftRenderUtils.getTextureTE("bamboo_mug")));
            modelBambooMug.render(stack, ivertexbuilder, combinedLight, combinedOverlayIn, 1, 1, 1, 1);
            stack.pop();
        }
    }
}

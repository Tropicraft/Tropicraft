package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.vector.Vector3f;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.BambooMugModel;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;
import net.tropicraft.core.common.item.CocktailItem;

public class DrinkMixerRenderer extends MachineRenderer<DrinkMixerTileEntity> {
    private final BambooMugModel modelBambooMug = new BambooMugModel(RenderType::entityCutout);
    private final ItemRenderer renderItem;
    private ItemEntity dummyEntityItem;

    private static final float[][] INGREDIENT_OFFSETS = new float[][] {
        {0.3f, -0.5f, 0.05f},
        {-0.3f, -0.5f, 0.05f},
        {0.0f, 0.3f, -0.1f}
    };

    private static final float[][] INGREDIENT_SCALES = new float[][] {
        {1, 1, 1},
        {1, 1, 1},
        {0.8f, 0.8f, 0.8f}
    };

    public DrinkMixerRenderer(final TileEntityRendererDispatcher rendererDispatcher) {
        super(rendererDispatcher, TropicraftBlocks.DRINK_MIXER.get(), new EIHMachineModel<>(RenderType::entitySolid));
        this.renderItem = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    protected RenderMaterial getMaterial() {
        return TropicraftRenderUtils.getTEMaterial("drink_mixer");
    }

    @Override
    public void renderIngredients(final DrinkMixerTileEntity te, final MatrixStack stack, final IRenderTypeBuffer buffer, int combinedLightIn, int combinedOverlayIn) {
        if (dummyEntityItem == null) {
             dummyEntityItem = new ItemEntity(Minecraft.getInstance().level, 0.0, 0.0, 0.0, new ItemStack(Items.SUGAR));
        }
        final NonNullList<ItemStack> ingredients = te.getIngredients();

        if (!te.isDoneMixing()) {
            for (int index = 0; index < ingredients.size(); index++) {
                final ItemStack ingredient = ingredients.get(index);
                if (!ingredient.isEmpty()) {
                    renderIngredient(stack, buffer, combinedOverlayIn, combinedLightIn, ingredient, index);
                }
            }
        }

        if (te.isMixing() || !te.result.isEmpty()) {
            stack.pushPose();
            stack.translate(-0.2f, -0.25f, 0.0f);
            if (te.isDoneMixing()) {
                modelBambooMug.renderLiquid = true;
                modelBambooMug.liquidColor = CocktailItem.getCocktailColor(te.result);
            } else {
                modelBambooMug.renderLiquid = false;
            }
            IVertexBuilder ivertexbuilder = buffer.getBuffer(modelBambooMug.renderType(TropicraftRenderUtils.getTextureTE("bamboo_mug")));
            modelBambooMug.renderToBuffer(stack, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
            stack.popPose();
        }
    }

    private void renderIngredient(final MatrixStack stack, final IRenderTypeBuffer buffer, final int combinedOverlayIn, final int combinedLight, final ItemStack ingredient, final int ingredientIndex) {
        stack.pushPose();
        stack.mulPose(Vector3f.XP.rotationDegrees(90));
        stack.mulPose(Vector3f.YP.rotationDegrees(90));
        stack.mulPose(Vector3f.ZP.rotationDegrees(90));
        final float[] offsets = INGREDIENT_OFFSETS[ingredientIndex];
        final float[] scales = INGREDIENT_SCALES[ingredientIndex];
        stack.translate(offsets[0], offsets[1], offsets[2]);
        stack.scale(scales[0], scales[1], scales[2]);
        dummyEntityItem.setItem(ingredient);
        final IBakedModel bakedModel = TropicraftRenderUtils.getBakedModel(renderItem, ingredient);
        renderItem.render(ingredient, ItemCameraTransforms.TransformType.FIXED, false, stack, buffer, combinedLight, combinedOverlayIn, bakedModel);
        stack.popPose();
    }
}

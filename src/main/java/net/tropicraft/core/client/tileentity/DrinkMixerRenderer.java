package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.core.NonNullList;
import com.mojang.math.Vector3f;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.TropicraftRenderUtils;
import net.tropicraft.core.client.entity.model.BambooMugModel;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.DrinkMixerTileEntity;
import net.tropicraft.core.common.item.CocktailItem;

public class DrinkMixerRenderer extends MachineRenderer<DrinkMixerTileEntity> {
    private final BambooMugModel modelBambooMug; //= new BambooMugModel(RenderType::entityCutout);
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

    public DrinkMixerRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(ctx, TropicraftBlocks.DRINK_MIXER.get(), new EIHMachineModel<>(ctx.bakeLayer(TropicraftRenderLayers.EIHMACHINE_LAYER),RenderType::entitySolid));
        modelBambooMug = new BambooMugModel(ctx.bakeLayer(TropicraftRenderLayers.BAMBOO_MUG), RenderType::entityCutout);
        this.renderItem = Minecraft.getInstance().getItemRenderer();
    }

    @Override
    protected Material getMaterial() {
        return TropicraftRenderUtils.getTEMaterial("drink_mixer");
    }

    @Override
    public void renderIngredients(final DrinkMixerTileEntity te, final PoseStack stack, final MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
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
            VertexConsumer ivertexbuilder = buffer.getBuffer(modelBambooMug.renderType(TropicraftRenderUtils.getTextureTE("bamboo_mug")));
            modelBambooMug.renderToBuffer(stack, ivertexbuilder, combinedLightIn, combinedOverlayIn, 1, 1, 1, 1);
            stack.popPose();
        }
    }

    private void renderIngredient(final PoseStack stack, final MultiBufferSource buffer, final int combinedOverlayIn, final int combinedLight, final ItemStack ingredient, final int ingredientIndex) {
        stack.pushPose();
        stack.mulPose(Vector3f.XP.rotationDegrees(90));
        stack.mulPose(Vector3f.YP.rotationDegrees(90));
        stack.mulPose(Vector3f.ZP.rotationDegrees(90));
        final float[] offsets = INGREDIENT_OFFSETS[ingredientIndex];
        final float[] scales = INGREDIENT_SCALES[ingredientIndex];
        stack.translate(offsets[0], offsets[1], offsets[2]);
        stack.scale(scales[0], scales[1], scales[2]);
        dummyEntityItem.setItem(ingredient);
        final BakedModel bakedModel = TropicraftRenderUtils.getBakedModel(renderItem, ingredient);
        renderItem.render(ingredient, ItemTransforms.TransformType.FIXED, false, stack, buffer, combinedLight, combinedOverlayIn, bakedModel);
        stack.popPose();
    }
}

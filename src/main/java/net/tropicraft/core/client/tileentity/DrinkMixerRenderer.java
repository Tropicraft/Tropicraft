package net.tropicraft.core.client.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.BambooMugModel;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.common.block.TropicraftBlocks;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;
import net.tropicraft.core.common.item.CocktailItem;

import javax.annotation.Nullable;

public class DrinkMixerRenderer extends MachineRenderer<DrinkMixerBlockEntity> {
    private static final ResourceLocation MUG_TEXTURE = Tropicraft.location("textures/block/te/bamboo_mug.png");
    private static final Material MATERIAL = new Material(TextureAtlas.LOCATION_BLOCKS, Tropicraft.location("textures/block/te/drink_mixer.png"));

    private final BambooMugModel emptyMugModel;
    private final BambooMugModel filledMugModel;
    private final ItemRenderer renderItem;
    @Nullable
    private ItemEntity dummyEntityItem;

    private static final float[][] INGREDIENT_OFFSETS = new float[][]{
            {0.3f, -0.5f, 0.05f},
            {-0.3f, -0.5f, 0.05f},
            {0.0f, 0.3f, -0.1f}
    };

    private static final float[][] INGREDIENT_SCALES = new float[][]{
            {1, 1, 1},
            {1, 1, 1},
            {0.8f, 0.8f, 0.8f}
    };

    public DrinkMixerRenderer(BlockEntityRendererProvider.Context context) {
        super(context, TropicraftBlocks.DRINK_MIXER.get(), new EIHMachineModel(context.getModelSet().bakeLayer(TropicraftRenderLayers.EIHMACHINE_LAYER)));
        renderItem = context.getItemRenderer();
        ModelPart mugLayer = context.getModelSet().bakeLayer(TropicraftRenderLayers.BAMBOO_MUG);
        emptyMugModel = new BambooMugModel(mugLayer, RenderType::entityCutout, false);
        filledMugModel = new BambooMugModel(mugLayer, RenderType::entityCutout, true);
    }

    @Override
    protected Material getMaterial() {
        return MATERIAL;
    }

    @Override
    public void renderIngredients(DrinkMixerBlockEntity te, PoseStack stack, MultiBufferSource buffer, int combinedLightIn, int combinedOverlayIn) {
        if (dummyEntityItem == null) {
            dummyEntityItem = new ItemEntity(Minecraft.getInstance().level, 0.0, 0.0, 0.0, new ItemStack(Items.SUGAR));
        }
        NonNullList<ItemStack> ingredients = te.getIngredientsAsItemStacks();

        if (!te.isDoneMixing()) {
            for (int index = 0; index < ingredients.size(); index++) {
                ItemStack ingredient = ingredients.get(index);
                if (!ingredient.isEmpty()) {
                    renderIngredient(stack, buffer, combinedOverlayIn, combinedLightIn, ingredient, index);
                }
            }
        }

        if (te.isMixing() || !te.result.isEmpty()) {
            stack.pushPose();
            stack.translate(-0.2f, -0.25f, 0.0f);
            if (te.isDoneMixing()) {
                int liquidColor = CocktailItem.getCocktail(te.result).color();
                VertexConsumer consumer = buffer.getBuffer(filledMugModel.renderType(MUG_TEXTURE));
                filledMugModel.renderToBuffer(stack, consumer, combinedLightIn, combinedOverlayIn, FastColor.ARGB32.opaque(liquidColor));
            } else {
                VertexConsumer consumer = buffer.getBuffer(emptyMugModel.renderType(MUG_TEXTURE));
                emptyMugModel.renderToBuffer(stack, consumer, combinedLightIn, combinedOverlayIn);
            }
            stack.popPose();
        }
    }

    private void renderIngredient(PoseStack stack, MultiBufferSource buffer, int combinedOverlayIn, int combinedLight, ItemStack ingredient, int ingredientIndex) {
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(90));
        stack.mulPose(Axis.YP.rotationDegrees(90));
        stack.mulPose(Axis.ZP.rotationDegrees(90));
        float[] offsets = INGREDIENT_OFFSETS[ingredientIndex];
        float[] scales = INGREDIENT_SCALES[ingredientIndex];
        stack.translate(offsets[0], offsets[1], offsets[2]);
        stack.scale(scales[0], scales[1], scales[2]);
        dummyEntityItem.setItem(ingredient);
        BakedModel bakedModel = renderItem.getModel(ingredient, null, null, 0);
        renderItem.render(ingredient, ItemDisplayContext.FIXED, false, stack, buffer, combinedLight, combinedOverlayIn, bakedModel);
        stack.popPose();
    }
}

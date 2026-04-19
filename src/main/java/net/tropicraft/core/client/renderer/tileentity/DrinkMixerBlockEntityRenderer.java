package net.tropicraft.core.client.renderer.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.ItemClusterRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.sprite.SpriteId;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Unit;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.tropicraft.Tropicraft;
import net.tropicraft.core.client.TropicraftRenderLayers;
import net.tropicraft.core.client.entity.model.EIHMachineModel;
import net.tropicraft.core.common.block.tileentity.DrinkMixerBlockEntity;
import net.tropicraft.core.common.drinks.Cocktail;
import net.tropicraft.core.common.item.CocktailItem;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DrinkMixerBlockEntityRenderer extends MachineBlockEntityRenderer<DrinkMixerBlockEntity, DrinkMixerBlockEntityRenderer.RenderState> {
    private static final Identifier MUG_TEXTURE = Tropicraft.id("textures/block/te/bamboo_mug.png");
    public static final SpriteId SPRITE = new SpriteId(TextureAtlas.LOCATION_BLOCKS, Tropicraft.id("block/te/drink_mixer"));

    private final Model<Unit> mugModel;
    private final Model<Unit> mugLiquidModel;
    private final ItemModelResolver itemModelResolver;
    private final RandomSource random = RandomSource.create();

    private static final Vector3fc[] INGREDIENT_OFFSETS = new Vector3fc[]{
            new Vector3f(0.3f, -0.5f, 0.05f),
            new Vector3f(-0.3f, -0.5f, 0.05f),
            new Vector3f(0.0f, 0.3f, -0.1f),
    };

    private static final Vector3fc[] INGREDIENT_SCALES = new Vector3fc[]{
            new Vector3f(1, 1, 1),
            new Vector3f(1, 1, 1),
            new Vector3f(0.8f, 0.8f, 0.8f),
    };

    public DrinkMixerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        super(new EIHMachineModel(context.bakeLayer(TropicraftRenderLayers.EIHMACHINE_LAYER)), context.sprites());
        itemModelResolver = context.itemModelResolver();
        mugModel = new Model.Simple(context.bakeLayer(TropicraftRenderLayers.BAMBOO_MUG), RenderTypes::entityCutoutCull);
        mugLiquidModel = new Model.Simple(context.bakeLayer(TropicraftRenderLayers.BAMBOO_MUG_LIQUID), RenderTypes::entityCutoutCull);
    }

    @Override
    protected SpriteId getSprite() {
        return SPRITE;
    }

    @Override
    public RenderState createRenderState() {
        return new RenderState();
    }

    @Override
    public void extractRenderState(DrinkMixerBlockEntity blockEntity, RenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress) {
        super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
        state.mixing = blockEntity.isMixing();
        state.ingredients.clear();
        if (!blockEntity.isDoneMixing()) {
            for (ItemStack ingredient : blockEntity.getDrinkIngredients()) {
                ItemClusterRenderState cluster = new ItemClusterRenderState();
                itemModelResolver.updateForTopItem(cluster.item, ingredient, ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
                cluster.count = ItemClusterRenderState.getRenderedAmount(ingredient.getCount());
                cluster.seed = ItemClusterRenderState.getSeedForItemStack(ingredient);
                state.ingredients.add(cluster);
            }
        }
        state.result = blockEntity.isDoneMixing() ? CocktailItem.getCocktail(blockEntity.result) : null;
    }

    @Override
    protected void submitIngredients(RenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        List<ItemClusterRenderState> ingredients = state.ingredients;
        for (int index = 0; index < ingredients.size(); index++) {
            ItemClusterRenderState ingredient = ingredients.get(index);
            renderIngredient(ingredient, poseStack, submitNodeCollector, state.lightCoords, index);
        }

        if (state.mixing || state.result != null) {
            poseStack.pushPose();
            poseStack.translate(-0.2f, -0.25f, 0.0f);
            submitNodeCollector.submitModel(mugModel, Unit.INSTANCE, poseStack, MUG_TEXTURE, state.lightCoords, OverlayTexture.NO_OVERLAY, EntityRenderState.NO_OUTLINE, null);
            if (state.result != null) {
                int liquidColor = ARGB.opaque(state.result.color());
                submitNodeCollector.submitModel(mugLiquidModel, Unit.INSTANCE, poseStack, mugLiquidModel.renderType(MUG_TEXTURE), state.lightCoords, OverlayTexture.NO_OVERLAY, liquidColor, null, EntityRenderState.NO_OUTLINE, null);
            }
            poseStack.popPose();
        }
    }

    private void renderIngredient(ItemClusterRenderState item, PoseStack stack, SubmitNodeCollector submitNodeCollector, int combinedLight, int ingredientIndex) {
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(90));
        stack.mulPose(Axis.YP.rotationDegrees(90));
        stack.mulPose(Axis.ZP.rotationDegrees(90));
        Vector3fc offsets = INGREDIENT_OFFSETS[ingredientIndex];
        Vector3fc scales = INGREDIENT_SCALES[ingredientIndex];
        stack.translate(offsets.x(), offsets.y(), offsets.z());
        stack.scale(scales.x(), scales.y(), scales.z());
        ItemEntityRenderer.renderMultipleFromCount(stack, submitNodeCollector, combinedLight, item, random);
        stack.popPose();
    }

    public static class RenderState extends MachineBlockEntityRenderer.RenderState {
        public boolean mixing;
        public final List<ItemClusterRenderState> ingredients = new ArrayList<>();
        public @Nullable Cocktail result;
    }
}
